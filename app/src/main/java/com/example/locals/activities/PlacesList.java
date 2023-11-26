package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.locals.R;
import com.example.locals.adapters.CityHomeAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.adapters.PlacesListAdapter;
import com.example.locals.models.LocationDetails;
import com.example.locals.models.Place;
import com.example.locals.retrofit.LocationApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesList extends AppCompatActivity {

    ImageView backArrow;
    ImageView searchTripIcon;
    RecyclerView recyclerView;
    PlacesListAdapter placesAdapter;
    EditText searchTab;
    List<LocationDetails> placeList= new ArrayList<>();
    RetrofitService retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
        String destination = getIntent().getExtras().getString("DESTINATION");
        searchTab = findViewById(R.id.searchTabPlaceListPlaceList);
        backArrow = findViewById(R.id.backArrowTravelSearch);
        searchTripIcon = findViewById(R.id.searchTripIconPlaceList);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        searchTab.setText(destination);
        setAttractions(destination);

        setOnClickListeners();
    }

    public void setPlacesRecyclerView(List<LocationDetails> placeList){
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
                intent.putExtra("DESTINATION", searchTab.getText().toString());
                startActivity(intent);
            }
        });

        searchTripIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlacesList.this, PlacesList.class);
                intent.putExtra("DESTINATION", searchTab.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void setAttractions(String cityName) {
        final Call<List<LocationDetails>> getCityAttractions = retrofit
                .getRetrofit()
                .create(LocationApi.class)
                .getCityAttractions("Bearer " + PKCE.getAccessToken(this), cityName);
        getCityAttractions.enqueue(new Callback<List<LocationDetails>>() {
            @Override
            public void onResponse(Call<List<LocationDetails>> call, Response<List<LocationDetails>> response) {
                if (response.body() != null && !response.body().isEmpty()) {
                    setPlacesRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<LocationDetails>> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(PlacesList.this, "call error", Toast.LENGTH_LONG).show();
            }
        });
    }
}