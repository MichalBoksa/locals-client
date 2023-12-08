package com.example.locals.fragments;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locals.R;
import com.example.locals.activities.Home;
import com.example.locals.activities.UserProfile;
import com.example.locals.adapters.AddSingleElementToFavoriteListAdapter;
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.models.Favorites;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddElementToFavoriteListFragment extends DialogFragment {
    private ImageView cancelBtn;
    private Button addBtn;
    private RecyclerView recyclerView;
    private List<Favorites> favoritesList;
    private AddSingleElementToFavoriteListAdapter adapter;
    private RetrofitService retrofit;
    private Gson gson;
    private User user;
    private SharedPreferences sharedPref;
    private String placeIdBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_element_to_favorit_list, container, false);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        Bundle bundle = getArguments();
        placeIdBundle = bundle.getString("GUIDE_ID","");
        gson = new Gson();
        sharedPref = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);
        RSFavoritesCall();
        cancelBtn = view.findViewById(R.id.closeAddElementToFavoritesList);
        addBtn = view.findViewById(R.id.AddFavoritesListFragmentBTN);
        recyclerView = view.findViewById(R.id.AddFavoritesListFragmentRV);
        setOnClickListeners();
        return view ;
    }

    private void setOnClickListeners() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RSAddPlaceToFavorites();
                dismiss();
            }
        });
    }

    public void setFavoritesListRecyclerView(List<Favorites> favList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AddSingleElementToFavoriteListAdapter(favList, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void RSFavoritesCall() {
        final Call<ArrayList<Favorites>> getUserFavorites = retrofit
                .getRetrofit()
                .create(FavoritesApi.class)
                .getUserFavorites("Bearer " + PKCE.getAccessToken(getActivity()), user.getId());

        getUserFavorites.enqueue(new Callback<ArrayList<Favorites>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {
                if(response.body() != null && !response.body().isEmpty()) {
                    favoritesList = response.body();
                    setFavoritesListRecyclerView(favoritesList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorites>> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(getActivity(), "favorites call error" + call,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void RSAddPlaceToFavorites(){
        final Call<Void> addFavoritesListItem = retrofit
                .getRetrofit()
                .create(FavoritesApi.class)
                .addListElement("Bearer " + PKCE.getAccessToken(getActivity()),adapter.getCheckedIds(), placeIdBundle);

        addFavoritesListItem.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(getActivity(), "add element call error" + call,Toast.LENGTH_LONG).show();
            }
        });
    }
}