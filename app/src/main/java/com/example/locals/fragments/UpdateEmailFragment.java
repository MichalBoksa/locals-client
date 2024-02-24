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
import com.example.locals.models.User;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmailFragment extends DialogFragment {
    private RetrofitService retrofitAuth;
    private Button updateBtn;
    private EditText emailET;
    private ImageView cancelBtn;
    private String email;
    private String accessCode;
    private User user;
    private Gson gson;
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_email,container,false);
        retrofitAuth = new RetrofitService();
        retrofitAuth.initializeRetrofitAuth();
        accessCode = PKCE.getAccessToken(UpdateEmailFragment.this.getActivity());
        sharedPref = UpdateEmailFragment.this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);
        email = user.getEmail();
        emailET = view.findViewById(R.id.emailUpdateFragmentET);
        emailET.setText(email);
        updateBtn = view.findViewById(R.id.editEmailFragmentBTN);
        cancelBtn = view.findViewById(R.id.closeEmailUpdate);



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
            //TODO update guide email
            @Override
            public void onClick(View view) {
                String newEmail = emailET.getText().toString();
                GsonBuilder gsonBuilder = new GsonBuilder();
                user.setEmail(newEmail);
                sharedPref.edit().putString("USER",gsonBuilder.create().toJson(user)).apply();
                RScall(newEmail);
                dismiss();
            }
        });
    }

    private void RScall(String newEmail) {

        final Call<ResponseBody> addNewList = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .saveUserEmail("Bearer " + accessCode, user.getEmail() , newEmail);

        addNewList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                emailET.setText(newEmail);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(UpdateEmailFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();
            }
        });
    }
}