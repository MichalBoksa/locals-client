package com.example.locals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.adapters.PlaceDetailsImageAdapter;
import com.example.locals.adapters.PlaceHomeAdapter;
import com.example.locals.models.Guide;
import com.example.locals.models.LocationDetails;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.LocationApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetails extends AppCompatActivity {

    private String REDIRECT_URI ="urltocallback://place_details";

    private ImageView backIcon;
    private TextView placeName;
    private TextView placePrice;
    private TextView placeRating;
    private TextView placeDescription;
    private ImageView placeImage;
    private RecyclerView imagesRecyclerView;
    private RecyclerView guidesRecyclerView;
    private PlaceDetailsImageAdapter imageAdapter;
    private GuideListAdapter guideAdapter;
    private RetrofitService retrofit;
    private String cityName;
    private String placeid;

    @Override
    protected void onResume() {
        super.onResume();
        PKCE.AuthorizationTokenResume(this,REDIRECT_URI);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        placeid = getIntent().getExtras().getString("LOCATION_ID");
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        if(PKCE.isJWTexpired(this)) {
            PKCE.refreshToken(this, REDIRECT_URI);
        }

        placeName = findViewById(R.id.placeNamePlaceDetails);
        placePrice = findViewById(R.id.pricePlaceDetails);
        placeImage = findViewById(R.id.imagePlaceDetails);
        placeDescription = findViewById(R.id.descriptionPlaceDetails);
        placeRating = findViewById(R.id.ratingPlaceDetails);
        setPlaceDetails();
        setOnClickListeners();
        setRecommendedGuides();
    }

    public void setOnClickListeners(){
        backIcon = findViewById(R.id.backIconPlaceDetails);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDetails.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void setImagesRecyclerView(List<String> imageURIs) {
        imagesRecyclerView = findViewById(R.id.imagesPlaceDetailsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        imagesRecyclerView.setLayoutManager(layoutManager);
        imageAdapter = new PlaceDetailsImageAdapter(this,imageURIs);
        imagesRecyclerView.setAdapter(imageAdapter);
    }

    private void setGuideRecyclerView(List<Guide> guides) {
        guidesRecyclerView = findViewById(R.id.guidePlaceDetailsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        guidesRecyclerView.setLayoutManager(layoutManager);
        guideAdapter = new GuideListAdapter(this,guides);
        guidesRecyclerView.setAdapter(guideAdapter);
    }

    public void setPlaceDetails() {
        final Call<LocationDetails> getPlaceDetails = retrofit
                .getRetrofit()
                .create(LocationApi.class)
                .getLocationDetails("Bearer " + PKCE.getAccessToken(this), placeid);

        getPlaceDetails.enqueue(new Callback<LocationDetails>() {
            @Override
            public void onResponse(Call<LocationDetails> call, Response<LocationDetails> response) {
                if(response.body() != null) {
                    placeName.setText(response.body().getName());
                    cityName = response.body().getAddress_obj().getCity();
                    //placePrice.setText(response.body().get);
                    placeRating.setText(response.body().getRating().toString());
                    placeDescription.setText(response.body().getDescription());
                    Glide.with(PlaceDetails.this.placeImage)
                            .load(response.body().getImagesURLList().get(0))
                            .into(placeImage);
                    response.body().getImagesURLList().remove(0);
                    setImagesRecyclerView(response.body().getImagesURLList());
                }

            }
            @Override
            public void onFailure(Call<LocationDetails> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(PlaceDetails.this, "call error",Toast.LENGTH_LONG).show();
            }
        });
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
                Toast.makeText(PlaceDetails.this, "guide call error",Toast.LENGTH_LONG).show();
            }
        });
    }


}
