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
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.models.Guide;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuideList extends AppCompatActivity {

    ImageView backArrow;
    RecyclerView recyclerView;
    GuideListAdapter adapter;
    EditText searchTab;
    ImageView searchLocalsIcon;
    List<Guide> guideList = new ArrayList<>();
    RetrofitService retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);
        searchTab = findViewById(R.id.searchTabLocalsList);
        searchLocalsIcon = findViewById(R.id.searchLocalsIconGuideList);
        backArrow = findViewById(R.id.backArrowGuideList);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();

        setOnClickListeners();

        setGuideRecyclerView(guideList);
    }

    public void setOnClickListeners (){
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideList.this, Home.class);
                startActivity(intent);
            }
        });

        searchLocalsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = searchTab.getText().toString();
                if(text != null && !text.isEmpty() ){
                    setCityGuides(text);
                }

            }
        });
    }

    public void setGuideRecyclerView(List<Guide> guideList) {
        recyclerView = findViewById(R.id.guideListRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GuideListAdapter(this,guideList);
        recyclerView.setAdapter(adapter);
    }

    public void setCityGuides(String cityName) {

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
                Toast.makeText(GuideList.this, "guide call error",Toast.LENGTH_LONG).show();
            }
        });
    }

}