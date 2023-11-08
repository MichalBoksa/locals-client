package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.locals.R;
import com.example.locals.adapters.CityHomeAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.adapters.PlacesListAdapter;
import com.example.locals.models.LocationDetails;
import com.example.locals.models.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesList extends AppCompatActivity {

    ImageView backArrow;
    RecyclerView recyclerView;
    PlacesListAdapter placesAdapter;
    List<LocationDetails> placeList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        backArrow = findViewById(R.id.backArrowTravelSearch);
        setOnClickListeners();

//        Place place = new Place();
//        place.setPlaceName("test Place");
//        place.setDescription("test desc");
//        place.setRating(4.3);
//        place.setCityPlace("Warsaw");
//        place.setImages(new ArrayList<>(Collections.singletonList("https://images.pexels.com/photos/3849167/pexels-photo-3849167.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")));
//        placeList.add(place);
//        placeList.add(place);
//        placeList.add(place);
//        placeList.add(place);
//        placeList.add(place);
//        setRecyclerView(placeList);
    }

    public void setRecyclerView(List<LocationDetails> placeList){
        recyclerView = findViewById(R.id.placesListRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        placesAdapter = new PlacesListAdapter(this,placeList);
        recyclerView.setAdapter(placesAdapter);

    }

    public void setOnClickListeners (){
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlacesList.this,Home.class);
                startActivity(intent);
            }
        });



    }
}