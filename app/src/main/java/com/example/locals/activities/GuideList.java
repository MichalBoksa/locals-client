package com.example.locals.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.locals.R;
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.models.Guide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuideList extends AppCompatActivity {

    ImageView backArrow;
    RecyclerView recyclerView;
    GuideListAdapter adapter;
    List<Guide> guideList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_list);

        backArrow = findViewById(R.id.backArrowGuideList);
        setOnClickListeners();

        setRecyclerView(guideList);
    }

    public void setOnClickListeners (){
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideList.this, Home.class);
                startActivity(intent);
            }
        });
    }

    public void setRecyclerView(List<Guide> guideList) {
        recyclerView = findViewById(R.id.guideListRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GuideListAdapter(this,guideList);
        recyclerView.setAdapter(adapter);
    }

}