package com.example.locals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.adapters.PlacesListAdapter;
import com.example.locals.models.LocationDetails;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoritesListDetails extends AppCompatActivity {
    RecyclerView favoritesListDetailsRV;
    PlacesListAdapter adapter;
    ImageView backArrow;
    RetrofitService retrofit;
    TextView listnameTV;
    TextView ifNoLocationsId;
    List<LocationDetails> locationDetailsList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list_details);
        backArrow = findViewById(R.id.backArrowFavoritesListDetails);
        listnameTV = findViewById(R.id.listNameFavoritesListDetailsTV);
        ifNoLocationsId = findViewById(R.id.ifListEmptyFavoritesListDetailsTV);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        String locationIds = getIntent().getExtras().getString("LOCATION_ID");
        String listName = getIntent().getExtras().getString("LIST_NAME");
        listnameTV.setText(listName);
        setOnClickListeners();

        if (locationIds != null && !locationIds.isEmpty()) {
            final Call<List<LocationDetails>> getFavoritesDetails = retrofit
                    .getRetrofit()
                    .create(FavoritesApi.class)
                    .getFavoritesListDetails("Bearer " + PKCE.getAccessToken(this), locationIds);

            getFavoritesDetails.enqueue(new Callback<List<LocationDetails>>() {
                @Override
                public void onResponse(Call<List<LocationDetails>> call, Response<List<LocationDetails>> response) {
                    if (response.body() != null && !response.body().isEmpty()) {
                            setPlacesRecyclerView(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<LocationDetails>> call, Throwable t) {

                }
            });
        }

        else {
            ifNoLocationsId.setText("Add first place to list!");
        }
    }

    private void setOnClickListeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoritesListDetails.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void setPlacesRecyclerView(List<LocationDetails> placeList) {
        favoritesListDetailsRV = findViewById(R.id.FavoritesListDetailsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        favoritesListDetailsRV.setLayoutManager(layoutManager);
        adapter = new PlacesListAdapter(this,placeList);
        favoritesListDetailsRV.setAdapter(adapter);
    }
}
