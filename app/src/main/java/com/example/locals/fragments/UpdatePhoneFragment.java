package com.example.locals.fragments;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePhoneFragment extends DialogFragment {
    private RetrofitService retrofit;
    private Button updateBtn;
    private EditText phoneET;
    private ImageView cancelBtn;
    private String phone;
    private String accessCode;

    public UpdatePhoneFragment(String phone) {
        this.phone = phone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_phone,container,false);

        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        accessCode = PKCE.getAccessToken(UpdatePhoneFragment.this.getActivity());
        phoneET = view.findViewById(R.id.phoneUpdateFragmentET);
        phoneET.setText(phone);
        updateBtn = view.findViewById(R.id.editPhoneFragmentBTN);
        cancelBtn = view.findViewById(R.id.closePhoneUpdate);

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
                RScall();
                dismiss();
            }
        });
    }

    private void RScall() {
       String email = PKCE.getJWTUser(accessCode);
        final Call<ResponseBody> addNewList = retrofit
                .getRetrofit()
                .create(UserApi.class)
                .saveUserPhone("Bearer " + accessCode,email ,phone);

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