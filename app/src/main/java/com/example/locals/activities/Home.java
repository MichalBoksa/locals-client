package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.locals.MainActivity;
import com.example.locals.R;
import com.example.locals.adapters.CityHomeAdapter;
import com.example.locals.adapters.FavouritesListAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.models.City;
import com.example.locals.models.Favourites;
import com.example.locals.models.LocationDetails;
import com.example.locals.models.LocationSearch;
import com.example.locals.models.Place;
import com.example.locals.retrofit.LocationApi;
import com.example.locals.retrofit.RetrofitService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

   private RecyclerView cityRecyclerView;
   private RecyclerView placeRecyclerView;
   private RecyclerView favoritesRecyclerView;
   private CityHomeAdapter cityHomeAdapter;
   private PlaceHomeAdapter placeHomeAdapter;
    private FavouritesListAdapter favoritsListAdapter;
    private List<City> cityList = new ArrayList<>();
    private List<Place> placeList = new ArrayList<>();
    private List<Favourites> favoritesList = new ArrayList<>();
    private ImageView favoritesImage;
    private ImageView guidesImage;
    private ImageView userProfileImage;
    private final String cityName = "Paris";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RetrofitService retrofit = new RetrofitService();
        retrofit.initializeRetrofit();

//  COMMENTED TO LIMIT API CALLS CUZ FIRST 5000 IT'S FREE :/
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
                Toast.makeText(Home.this, "call error",Toast.LENGTH_LONG).show();

            }
        });

        cityList.add(new City("Test City", "https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));
        cityList.add(new City("Test City", "https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));
        cityList.add(new City("Test City", "https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));
        cityList.add(new City("Test City", "https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));
        cityList.add(new City("Test City", "https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"));
        setCityRecyclerView(cityList);



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


        Favourites fav = new Favourites();
        fav.setPlaceList(placeList);
        fav.setName("testList");
        fav.setDate(LocalDate.of(2022,10,10));
        favoritesList.add(fav);
        favoritesList.add(fav);
        favoritesList.add(fav);
        favoritesList.add(fav);
        favoritesList.add(fav);
        setFavoritesRecyclerView(favoritesList);

        setOnClickListeners();
    }

    private void setCityRecyclerView(List<City> cityList) {
        cityRecyclerView = findViewById(R.id.cityRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        cityRecyclerView.setLayoutManager(layoutManager);
        cityHomeAdapter = new CityHomeAdapter(this, cityList);
        cityRecyclerView.setAdapter(cityHomeAdapter);
    }

    private void setPlacesRecyclerView(List<LocationDetails> placeList) {
        placeRecyclerView = findViewById(R.id.placesRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        placeRecyclerView.setLayoutManager(layoutManager);
        placeHomeAdapter = new PlaceHomeAdapter(this,placeList);
        placeRecyclerView.setAdapter(placeHomeAdapter);
    }

    private void setFavoritesRecyclerView(List<Favourites> favoritesList) {
        favoritesRecyclerView = findViewById(R.id.favoritesRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritsListAdapter = new FavouritesListAdapter(this,favoritesList);
        favoritesRecyclerView.setAdapter(favoritsListAdapter);
    }

    public void setOnClickListeners(){
        favoritesImage = findViewById(R.id.favoritesIconHome);
        guidesImage = findViewById(R.id.guideIconHome);
        userProfileImage = findViewById(R.id.userProfileIconHome);
        favoritesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PlacesList.class);
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
}