package com.example.locals.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.locals.R;
import com.example.locals.models.Favorites;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterGuideFragment extends DialogFragment {

    private RetrofitService retrofit;
    private RetrofitService retrofitAuth;
    private List<String> activities;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    CheckBox checkBox7;
    ImageView cancelBTN;
    Button saveGuide;
    EditText aboutMe;
    EditText whatToOffer;
    EditText languages;
    EditText price;
    EditText city;
    SharedPreferences sharedPref;
    Gson gson;
    User user;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_guide,container,false);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        retrofitAuth = new RetrofitService();
        retrofitAuth.initializeRetrofitAuth();
        checkBox1 = view.findViewById(R.id.checkBox1);
        checkBox2 = view.findViewById(R.id.checkBox2);
        checkBox3 = view.findViewById(R.id.checkBox3);
        checkBox4 = view.findViewById(R.id.checkBox4);
        checkBox5 = view.findViewById(R.id.checkBox5);
        checkBox6 = view.findViewById(R.id.checkBox6);
        checkBox7 = view.findViewById(R.id.checkBox7);
        cancelBTN = view.findViewById(R.id.closeBecomeLocal);
        aboutMe = view.findViewById(R.id.aboutMeBecomeLocalTV);whatToOffer = view.findViewById(R.id.whatToshowBecomeLocalTV);
        languages = view.findViewById(R.id.languagesBecomeLocalTV);
        price = view.findViewById(R.id.priceBecomeLocalTV);
        city = view.findViewById(R.id.cityBecomeLocalTV);
        saveGuide = view.findViewById(R.id.CreateLocalsBookingFragmentBTN);
        gson = new Gson();
        sharedPref = RegisterGuideFragment.this.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);
        activities = new ArrayList<>();
        setOnClickListeners();
        return view;
    }

    private void setOnClickListeners() {
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b) activities.add(compoundButton.getText().toString());
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) activities.add(compoundButton.getText().toString());
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) activities.add(compoundButton.getText().toString());
            }
        });

        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) activities.add(compoundButton.getText().toString());
            }
        });

        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) activities.add(compoundButton.getText().toString());
            }
        });

        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) activities.add(compoundButton.getText().toString());
            }
        });

        checkBox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) activities.add(compoundButton.getText().toString());
            }
        });

        saveGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guide guide = new Guide();

                User user = gson.fromJson(sharedPref.getString("USER",null), User.class);
                guide.setName(user.getName());
                guide.setEmail(user.getEmail());
                guide.setPhoneNumber(user.getPhoneNumber());
                guide.setActivities(String.join(",",activities));
                guide.setAboutMe(aboutMe.getText().toString());
                guide.setWhatToOffer(whatToOffer.getText().toString());
                guide.setLanguages(languages.getText().toString());
                guide.setPrice(Integer.parseInt(price.getText().toString()));
                guide.setImageURL(user.getImageUri());
                guide.setCity(city.getText().toString());
                RScall(guide);
            }
        });
    }

    private void RScall(Guide guide) {
        final Call<Void> addNewList = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .createGuide("Bearer " + PKCE.getAccessToken(this.getContext()), guide);

        addNewList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AScall();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(RegisterGuideFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void AScall() {
      //  PKCE.getAuthorizationToken();
        final Call<Void> addNewList = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .updateToGuide("Basic " + PKCE.getAuthToken(this.getContext()),user.getEmail());

        addNewList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Toast.makeText(RegisterGuideFragment.this.getActivity(), "You've become a local!",Toast.LENGTH_LONG).show();
                dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(RegisterGuideFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();

            }
        });
    }
}