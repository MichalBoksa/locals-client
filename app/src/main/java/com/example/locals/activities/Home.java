package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.locals.R;
import com.example.locals.adapters.FavoritesListAdapter;
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.models.Favorites;
import com.example.locals.models.Guide;
import com.example.locals.models.LocationDetails;
import com.example.locals.models.Place;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.LocationApi;
import com.example.locals.retrofit.RetrofitService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

   private RecyclerView guideRecyclerView;
   private RecyclerView placeRecyclerView;
   private RecyclerView favoritesRecyclerView;
   private GuideListAdapter guideAdapter;
   private PlaceHomeAdapter placeHomeAdapter;
    private FavoritesListAdapter favoritsListAdapter;
    private List<Guide> guideList = new ArrayList<>();
    private List<Place> placeList = new ArrayList<>();
    private ArrayList<Favorites> favoritesList = new ArrayList<Favorites>();
    private ImageView favoritesImage;
    private ImageView guidesImage;
    private ImageView userProfileImage;
    private final String cityName = "Paris";

    RetrofitService retrofit;
    Integer a = 4;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        setRecommendedPlaces();
        setUserFavorites();
        setRecommendedGuides();



        Place place = new Place();
        place.setPlaceName("test Place");
        place.setDescription("test desc");
        place.setRating(4.3);
        place.setImages(new ArrayList<>(Collections.singletonList("https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")));
              placeList.add(place);
              placeList.add(place);
              placeList.add(place);
              placeList.add(place);
              placeList.add(place);
//        setPlacesRecyclerView(placeList);

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
        favoritesImage = findViewById(R.id.favoritesIconHome);
        guidesImage = findViewById(R.id.guideIconHome);
        userProfileImage = findViewById(R.id.userProfileIconHome);
        favoritesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FavoritesList.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("USER_FAVORITES_LIST",favoritesList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        guidesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, GuideList.class);
                startActivity(intent);
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void setRecommendedPlaces() {

//  COMMENTED TO LIMIT API CALLS CUZ FIRST 5000 IS FREE :/
//        final Call<List<LocationDetails>> getCityAttractions = retrofit
//                                                                .getRetrofit()
//                                                                .create(LocationApi.class)
//                                                                .getCityAttractions(cityName);
//        getCityAttractions.enqueue(new Callback<List<LocationDetails>>() {
//            @Override
//            public void onResponse(Call<List<LocationDetails>> call, Response<List<LocationDetails>> response) {
//                if(response.body() != null && !response.body().isEmpty()) {
//                    setPlacesRecyclerView(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<LocationDetails>> call, Throwable t) {
//                System.out.println(call);
//                Toast.makeText(Home.this, "call error",Toast.LENGTH_LONG).show();
//            }
//        });

        //HERE I MAKE ONLY ONE API CALL
        final Call<LocationDetails> getPlaceDetails = retrofit
                .getRetrofit()
                .create(LocationApi.class)
                .getLocationDetails("189258");

        getPlaceDetails.enqueue(new Callback<LocationDetails>() {
            @Override
            public void onResponse(Call<LocationDetails> call, Response<LocationDetails> response) {
                if(response.body() != null) {
                    List<LocationDetails> list = Arrays.asList(response.body());
                    setPlacesRecyclerView(list);
                }
            }
            @Override
            public void onFailure(Call<LocationDetails> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(Home.this, "location call error",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setRecommendedGuides() {

        final Call<List<Guide>> getCityGuides = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getCityGuides(cityName);

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

        //TODO change userId
        final Call<ArrayList<Favorites>> getUserFavorites = retrofit
                .getRetrofit()
                .create(FavoritesApi.class)
                .getUserFavorites(a);

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


}