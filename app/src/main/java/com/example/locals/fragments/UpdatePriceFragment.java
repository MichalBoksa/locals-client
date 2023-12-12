package com.example.locals.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.locals.R;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePriceFragment extends DialogFragment {
    private RetrofitService retrofit;
    private Button updateBtn;
    private EditText priceET;
    private ImageView cancelBtn;
    private String price;
    private String accessCode;
    private Guide guide;
    private User user;
    private SharedPreferences sharedPref;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_price,container,false);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        accessCode = PKCE.getAccessToken(UpdatePriceFragment.this.getActivity());
        priceET = view.findViewById(R.id.priceUpdateFragmentET);
        priceET.setText(price);
        updateBtn = view.findViewById(R.id.editPriceFragmentBTN);
        cancelBtn = view.findViewById(R.id.closePriceUpdate);
        guide = (Guide) getArguments().getSerializable("GUIDE");
        gson = new Gson();
        sharedPref = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);
        setOnClickListeners();

        return view;
    }

    private void setOnClickListeners() {

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = priceET.getText().toString();
                guide.setPrice(Integer.parseInt(price));
                RScall(priceET.getText().toString());
                dismiss();
            }
        });
    }

    private void RScall(String newPrice) {

        final Call<Void> addNewList = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .updateGuide("Bearer " + accessCode,user.getEmail(), guide);

        addNewList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                priceET.setText(newPrice);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(UpdatePriceFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();
            }
        });
    }
}