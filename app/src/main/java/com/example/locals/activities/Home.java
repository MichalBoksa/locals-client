package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.locals.R;
import com.example.locals.adapters.CityHomeAdapter;
import com.example.locals.adapters.FavoritsListAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.models.City;
import com.example.locals.models.Favorites;
import com.example.locals.models.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity {

    RecyclerView cityRecyclerView;
    RecyclerView placeRecyclerView;
    RecyclerView favoritesRecyclerView;
    CityHomeAdapter cityHomeAdapter;
    PlaceHomeAdapter placeHomeAdapter;
    FavoritsListAdapter favoritsListAdapter;
    List<City> cityList = new ArrayList<>();
    List<Place> placeList = new ArrayList<>();
    List<Favorites> favoritesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        setPlacesRecyclerView(placeList);


        Favorites fav = new Favorites();
        fav.setPlaceList(placeList);
        fav.setName("testList");
        fav.setDate(LocalDate.of(2022,10,10));
        favoritesList.add(fav);
        favoritesList.add(fav);
        favoritesList.add(fav);
        favoritesList.add(fav);
        favoritesList.add(fav);
        setFavoritesRecyclerView(favoritesList);
    }

    private void setCityRecyclerView(List<City> cityList) {
        cityRecyclerView = findViewById(R.id.cityRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        cityRecyclerView.setLayoutManager(layoutManager);
        cityHomeAdapter = new CityHomeAdapter(this, cityList);
        cityRecyclerView.setAdapter(cityHomeAdapter);
    }

    private void setPlacesRecyclerView(List<Place> placeList) {
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
        favoritsListAdapter = new FavoritsListAdapter(this,favoritesList);
        favoritesRecyclerView.setAdapter(favoritsListAdapter);
    }
}