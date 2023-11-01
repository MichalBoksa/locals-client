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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.models.Guide;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuideDetails extends AppCompatActivity {

    ImageView guideImage;
    ImageView backArrow;
    TextView guideNameTV;
    TextView guideCityTV;
    TextView guidePriceTV;
    TextView guideAboutMeTV;
    TextView guideWhatToOfferTV;
    RecyclerView languagesRV;
    RecyclerView activitiesRV;
    Button bookLocalBtn;
    Button contactBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_details);

        RetrofitService retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        int guideId = getIntent().getExtras().getInt("GUIDE_ID");

        guideImage = findViewById(R.id.imageGuideDetails);
        backArrow = findViewById(R.id.backGuideDetailsBtn);
        guideNameTV = findViewById(R.id.nameGuideDetailsTV);
        guideCityTV = findViewById(R.id.cityGuideDetailsTV);
        guidePriceTV = findViewById(R.id.priceGuideDetailsTV);
        guideAboutMeTV = findViewById(R.id.aboutMeGuideDetailsTV);
        guideWhatToOfferTV = findViewById(R.id.GuidingDescGuideDetailsTV);

        final Call<Guide> getGuideDetails = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getGuideDetails(guideId);
        
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
                }
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideDetails.this, "call error",Toast.LENGTH_LONG).show();

            }
        });

    }

    public void setOnClickListeners()
    {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideDetails.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
