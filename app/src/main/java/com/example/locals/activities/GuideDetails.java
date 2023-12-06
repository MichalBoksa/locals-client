package com.example.locals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.adapters.BulletListAdapter;
import com.example.locals.fragments.AddFavoritesListFragment;
import com.example.locals.fragments.AddGuideBookingFragment;
import com.example.locals.models.Guide;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuideDetails extends AppCompatActivity {
    private String REDIRECT_URI ="urltocallback://guide_details";

    ImageView guideImage;
    ImageView backArrow;
    TextView guideNameTV;
    TextView guideCityTV;
    TextView guidePriceTV;
    TextView guideAboutMeTV;
    TextView guideWhatToOfferTV;
    RecyclerView languagesRV;
    RecyclerView activitiesRV;

    BulletListAdapter languagesAdapter;
    BulletListAdapter activitiesAdapter;
    Button bookLocalBtn;
    Button contactBtn;
    AddGuideBookingFragment dialogFragment;
    int guideId;

    @Override
    protected void onResume() {
        super.onResume();
        PKCE.AuthorizationTokenResume(this,REDIRECT_URI);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_details);
        if(PKCE.isJWTexpired(this)) {
            PKCE.refreshToken(this, REDIRECT_URI);
        }
        RetrofitService retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        guideId = getIntent().getExtras().getInt("GUIDE_ID");


        guideImage = findViewById(R.id.imageGuideDetails);
        backArrow = findViewById(R.id.backGuideDetailsBtn);
        guideNameTV = findViewById(R.id.nameGuideDetailsTV);
        guideCityTV = findViewById(R.id.cityGuideDetailsTV);
        guidePriceTV = findViewById(R.id.priceGuideDetailsTV);
        guideAboutMeTV = findViewById(R.id.aboutMeGuideDetailsTV);
        guideWhatToOfferTV = findViewById(R.id.GuidingDescGuideDetailsTV);
        bookLocalBtn = findViewById(R.id.bookGuideDetailsBtn);
        setOnClickListeners();

        final Call<Guide> getGuideDetails = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getGuideDetails("Bearer " + PKCE.getAccessToken(this),guideId);
        
        getGuideDetails.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                if(response.body() != null) {
                    Glide.with(GuideDetails.this)
                            .load(response.body().getImageURL())
                            .into(guideImage);
                    guideNameTV.setText(response.body().getName());
                    guideCityTV.setText(response.body().getCity());
                    guidePriceTV.setText(response.body().getPrice().toString());
                    guideAboutMeTV.setText(response.body().getAboutMe());
                    guideWhatToOfferTV.setText(response.body().getWhatToOffer());
                    setActivitiesRecyclerView(Arrays.asList(response.body().getActivities().split(",")));
                    setLanguagesRecyclerView(Arrays.asList(response.body().getLanguages().split(",")));
                }
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideDetails.this, "call error",Toast.LENGTH_LONG).show();

            }
        });

    }

    public void setOnClickListeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideDetails.this, Home.class);
                startActivity(intent);
            }
        });

        bookLocalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragment = new AddGuideBookingFragment();
                Bundle args = new Bundle();
                args.putInt("GUIDE_ID", guideId);
                dialogFragment.setArguments(args);
                dialogFragment.show(getSupportFragmentManager(),"addGuideBooking");
            }
        });
    }

    private void setLanguagesRecyclerView(List<String> languagesList) {
        languagesRV = findViewById(R.id.languagesGuideDetailsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        languagesRV.setLayoutManager(layoutManager);
        languagesAdapter = new BulletListAdapter(this,languagesList);
        languagesRV.setAdapter(languagesAdapter);
    }

    private void setActivitiesRecyclerView(List<String> activitiesList) {
        activitiesRV = findViewById(R.id.activitiesGuideDetailsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        activitiesRV.setLayoutManager(layoutManager);
        languagesAdapter = new BulletListAdapter(this,activitiesList);
        activitiesRV.setAdapter(languagesAdapter);
    }
}
