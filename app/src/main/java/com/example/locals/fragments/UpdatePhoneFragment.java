package com.example.locals.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.locals.R;
import com.example.locals.activities.Home;
import com.example.locals.models.User;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePhoneFragment extends DialogFragment {
    private RetrofitService retrofitAuth;
    private Button updateBtn;
    private EditText phoneET;
    private ImageView cancelBtn;
    private String phone;
    private String accessCode;
    private User user;
    private Gson gson;
    private SharedPreferences sharedPref;
    public UpdatePhoneFragment(String phone) {
        this.phone = phone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_phone,container,false);

        retrofitAuth = new RetrofitService();
        retrofitAuth.initializeRetrofitAuth();
        accessCode = PKCE.getAccessToken(UpdatePhoneFragment.this.getActivity());
        phoneET = view.findViewById(R.id.phoneUpdateFragmentET);
        phoneET.setText(phone);
        updateBtn = view.findViewById(R.id.editPhoneFragmentBTN);
        cancelBtn = view.findViewById(R.id.closePhoneUpdate);

        sharedPref = UpdatePhoneFragment.this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
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
                phone = phoneET.getText().toString();
                RScall();
                dismiss();
            }
        });
    }

    private void RScall() {
        final Call<ResponseBody> addNewList = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .saveUserPhone("Bearer " + accessCode,user.getEmail() ,phone);

        addNewList.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                phoneET.setText(phone);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(UpdatePhoneFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();
            }
        });
    }
}