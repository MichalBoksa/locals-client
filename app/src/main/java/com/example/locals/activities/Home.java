package com.example.locals.activities;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;

import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.locals.MainActivity;
import com.example.locals.R;
import com.example.locals.adapters.FavoritesListAdapter;
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.models.Favorites;
import com.example.locals.models.Guide;
import com.example.locals.models.LocationDetails;
import com.example.locals.models.Place;
import com.example.locals.models.User;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.LocationApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    private String REDIRECT_URI ="urltocallback://home";

   private RecyclerView guideRecyclerView;
   private RecyclerView placeRecyclerView;
   private RecyclerView favoritesRecyclerView;
   private GuideListAdapter guideAdapter;
   private PlaceHomeAdapter placeHomeAdapter;
    private FavoritesListAdapter favoritsListAdapter;
    private List<Guide> guideList = new ArrayList<>();
    private List<Place> placeList = new ArrayList<>();
    private ArrayList<Favorites> favoritesList = new ArrayList<Favorites>();
    private ImageView favoritesImageIcon;
    private ImageView guidesImageIcon;
    private ImageView userProfileImageIcon;
    private CircleImageView userImage;
    private ImageView searchTripIcon;
    private EditText searchTab;
    private final String cityName = "Paris";
    private TextView usernameTV;
    private User user;
    private Gson gson;
    private SharedPreferences sharedPref;
   private RetrofitService retrofit;
   private RetrofitService retrofitAuth;

    @Override
    protected void onResume() {
        super.onResume();
        PKCE.AuthorizationTokenResume(this,REDIRECT_URI);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(PKCE.isJWTexpired(this)) {
            PKCE.refreshToken(this, REDIRECT_URI);
        }
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        retrofitAuth = new RetrofitService();
        retrofitAuth.initializeRetrofitAuth();
//        gson = new Gson();
//        sharedPref = Home.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        user = gson.fromJson(sharedPref.getString("USER",null), User.class);
        setUser();
        setOnClickListeners();
    }


    private void setGuideRecyclerView(List<Guide> guideList) {
        guideRecyclerView = findViewById(R.id.cityRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        guideRecyclerView.setLayoutManager(layoutManager);
        guideAdapter = new GuideListAdapter(this, guideList);
        guideRecyclerView.setAdapter(guideAdapter);
    }

    private void setPlacesRecyclerView(List<LocationDetails> placeList) {
        placeRecyclerView = findViewById(R.id.placesRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        placeRecyclerView.setLayoutManager(layoutManager);
        placeHomeAdapter = new PlaceHomeAdapter(this,placeList);
        placeRecyclerView.setAdapter(placeHomeAdapter);
    }

    private void setFavoritesRecyclerView(List<Favorites> favoritesList) {
        favoritesRecyclerView = findViewById(R.id.favoritesRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritsListAdapter = new FavoritesListAdapter(this,favoritesList);
        favoritesRecyclerView.setAdapter(favoritsListAdapter);
    }

    public void setOnClickListeners(){
        favoritesImageIcon = findViewById(R.id.favoritesIconHome);
        guidesImageIcon = findViewById(R.id.guideIconHome);
        userProfileImageIcon = findViewById(R.id.userProfileIconHome);
        searchTripIcon = findViewById(R.id.searchTripIconHome);
        searchTab = findViewById(R.id.citySearchEditTextHome
        );
        favoritesImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FavoritesList.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("USER_FAVORITES_LIST",favoritesList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        guidesImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, GuideList.class);
                startActivity(intent);
            }
        });

        userProfileImageIcon.setOnClickListener(new View.OnClickListener() {
            String accessCode = PKCE.getAccessToken(Home.this);
            @Override
            public void onClick(View view) {
                Intent intent = PKCE.getJWTAuthorities(accessCode).contains("GUIDE")
                        ? new Intent(Home.this, GuideProfile.class)
                        : new Intent(Home.this, UserProfile.class);
//                Intent intent = new Intent(Home.this, GuideProfile.class);
                startActivity(intent);
            }
        });

        searchTripIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PlacesList.class);
                intent.putExtra("DESTINATION", searchTab.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void setRecommendedPlaces() {

//  COMMENTED TO LIMIT API CALLS CUZ FIRST 5000 IS FREE :/
        final Call<List<LocationDetails>> getCityAttractions = retrofit
                                                                .getRetrofit()
                                                                .create(LocationApi.class)
                                                                .getCityAttractions("Bearer " + PKCE.getAccessToken(this),cityName);
        getCityAttractions.enqueue(new Callback<List<LocationDetails>>() {
            @Override
            public void onResponse(Call<List<LocationDetails>> call, Response<List<LocationDetails>> response) {
                if(response.body() != null && !response.body().isEmpty()) {
                    setPlacesRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<LocationDetails>> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(Home.this, "call error",Toast.LENGTH_LONG).show();
            }
        });

        //HERE I MAKE ONLY ONE API CALL
//        final Call<LocationDetails> getPlaceDetails = retrofit
//                .getRetrofit()
//                .create(LocationApi.class)
//                .getLocationDetails("Bearer " + PKCE.getAccessToken(this), "189258");
//
//        getPlaceDetails.enqueue(new Callback<LocationDetails>() {
//            @Override
//            public void onResponse(Call<LocationDetails> call, Response<LocationDetails> response) {
//                if(response.body() != null) {
//                    List<LocationDetails> list = Arrays.asList(response.body());
//                    setPlacesRecyclerView(list);
//                }
//            }
//            @Override
//            public void onFailure(Call<LocationDetails> call, Throwable t) {
//                System.out.println(call);
//                Toast.makeText(Home.this, "location call error",Toast.LENGTH_LONG).show();
//            }
//        });
    }

    public void setRecommendedGuides() {

        final Call<List<Guide>> getCityGuides = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getCityGuides("Bearer " + PKCE.getAccessToken(this),cityName);

        getCityGuides.enqueue(new Callback<List<Guide>>() {
            @Override
            public void onResponse(Call<List<Guide>> call, Response<List<Guide>> response) {
                if(response.body() != null && !response.body().isEmpty()) {
                    setGuideRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Guide>> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(Home.this, "guide call error",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void setUserFavorites() {

        final Call<ArrayList<Favorites>> getUserFavorites = retrofit
                .getRetrofit()
                .create(FavoritesApi.class)
                .getUserFavorites("Bearer " + PKCE.getAccessToken(this), user.getId());

        getUserFavorites.enqueue(new Callback<ArrayList<Favorites>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {
                if(response.body() != null && !response.body().isEmpty()) {
                    favoritesList = response.body();
                    setFavoritesRecyclerView(favoritesList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorites>> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(Home.this, "favorites call error" + call,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUser() {
        usernameTV = findViewById(R.id.usernameTVHome);
        String accessCode = PKCE.getAccessToken(this);
        final Call<User> getUser = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .getUser("Bearer " + accessCode, PKCE.getJWTUser(accessCode));

        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println(call);
                if(response.body() != null && response.code() != 401 ) {
                    userImage = findViewById(R.id.userImageHome);
                    usernameTV.setText(response.body().getName());
                    if (response.body().getImageUri() != null && response.body().getImageUri().length() > 0) {
                        String b = response.body().getImageUri();
                        b = b.substring(1,b.length()-1);
                        Uri uri = Uri.parse(b);
                        userImage.setImageURI(uri);
                    }
                    SharedPreferences sharedPref = Home.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    GsonBuilder gson = new GsonBuilder();
                    user = response.body();
                    sharedPref.edit().putString("USER",gson.create().toJson(user)).apply();
                    setRecommendedGuides();
                    setRecommendedPlaces();
                    setUserFavorites();
                }
                else{
                    PKCE.getRefreshToken(Home.this);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Home.this, "userData call error",Toast.LENGTH_LONG).show();
            }
        });
    }
}