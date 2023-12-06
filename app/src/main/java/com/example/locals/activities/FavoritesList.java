package com.example.locals.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.adapters.FavoritesListAdapter;
import com.example.locals.fragments.AddFavoritesListFragment;
import com.example.locals.models.Favorites;
import com.example.locals.utils.PKCE;

import java.util.ArrayList;
import java.util.List;

public class FavoritesList extends AppCompatActivity {
    private String REDIRECT_URI ="urltocallback://fav_list";

    RecyclerView favoritesListRV;
    ImageView addNewList;
    ImageView backArrow;
    FavoritesListAdapter adapter;
    ArrayList<Favorites> favoritesList;
    AddFavoritesListFragment dialogFragment;

    @Override
    protected void onResume() {
        super.onResume();
        PKCE.AuthorizationTokenResume(this,REDIRECT_URI);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_list);
        if(PKCE.isJWTexpired(this)) {
            PKCE.refreshToken(this, REDIRECT_URI);
        }
        backArrow = findViewById(R.id.backArrowFavoritesList);
        addNewList = findViewById(R.id.addNewFavoritesList);
        Bundle bundle = getIntent().getExtras();
        favoritesList = bundle.getParcelableArrayList("USER_FAVORITES_LIST");
        setFavoritesListRV(favoritesList);
        setOnClickListeners();

    }

    private void setFavoritesListRV(List<Favorites> favoritesList) {
        favoritesListRV = findViewById(R.id.favoritesListRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        favoritesListRV.setLayoutManager(layoutManager);
        adapter = new FavoritesListAdapter(this,favoritesList);
        favoritesListRV.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoritesList.this, Home.class);
                startActivity(intent);
            }
        });

        addNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFragment = new AddFavoritesListFragment();
                dialogFragment.show(getSupportFragmentManager(),"addFavoriteList");

            }
        });
    }
}
