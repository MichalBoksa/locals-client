package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.locals.MainActivity;
import com.example.locals.R;

public class SplashScreen extends AppCompatActivity {

    TextView appName;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        appName = findViewById(R.id.appNameSplash);
        lottie = findViewById(R.id.lottieSplash);

        appName.animate().translationY(-1600).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(4000).setStartDelay(1900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        },5000);
    }
}