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
        Guide guide = new Guide();
        guide.setName("Michal");
        guide.setGuideCity("Lodz");
        guide.setLanguages(Arrays.asList("English","Polish","German"));
        guide.setImageURL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSBvP_5lvlONHOO560KFrjgkWl-ybK3RAsbe1XrX8PfnOs1rGRv-py-1wVgE9jRroAGs2o&usqp=CAU");
        guide.setPrice(15);
        guide.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus molestie rutrum elementum.");
        guideList.add(guide);
        guideList.add(guide);
        guideList.add(guide);
        guideList.add(guide);
        guideList.add(guide);
        guideList.add(guide);
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