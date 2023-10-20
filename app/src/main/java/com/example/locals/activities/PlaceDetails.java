package com.example.locals.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.locals.R;

public class PlaceDetails extends AppCompatActivity {

    ImageView backIcon;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        setOnClickListeners();

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
}
