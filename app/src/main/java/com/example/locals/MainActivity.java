package com.example.locals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.locals.models.User;
import com.example.locals.retrofit.OAuth2Api;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.OAuthToken;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("=== I'm in on create");
        initializeComponents();
    }

    private void initializeComponents() {
        TextInputLayout textInputLayoutLogin = findViewById(R.id.textInputLayoutLoginUsername);
        TextInputEditText textInputEditTextLogin = findViewById(R.id.textInputEditTextLoginUsername);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayoutLoginPassword);
        TextInputEditText textInputEditTextPassword = findViewById(R.id.textInputEditTextLoginPassword);
        MaterialButton buttonSignIn = findViewById(R.id.buttonLogin);
        System.out.println("=== I'm in init comp");
        RetrofitService retrofitService = new RetrofitService();
       // UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
      buttonSignIn.setOnClickListener(view -> {
//                    String username = textInputEditTextLogin.getText().toString().trim();
//                    String password = textInputEditTextPassword.getText().toString().trim();
//
//                    Call<ResponseBody> call = retrofitService
//                                            .getRetrofit()
//                                            .create(UserApi.class)
//
//
//
//                                            .signIn(new User(username,password));

                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  System.out.println("=== I'm here");
                  Toast.makeText(this.getApplicationContext(),"toast",Toast.LENGTH_LONG);
                  intent.setData(Uri.parse("http://192.168.32.6:8080/oauth2/authorize?" +
                          "response_type=code&" +
                          "client_id=client&" +
                          "scope=openid&" +
                          "redirect_uri=urltocallback://callback" +
                          "&code_challenge="+
                          "QYPAZ5NU8yvtlQ9erXrUYR-T5AGCjCF47vN-KsaI2A8"
                          +"&code_challenge_method=S256"));

                  //TODO check condition
                  //if (intent.resolveActivity(getPackageManager()) != null) {
                      System.out.println("=== wyslalem");
                      startActivity(intent);

           //       }
                }

                );



    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();

        //TODO check condition
        if(data != null && !TextUtils.isEmpty(data.getScheme())) {
            String code = data.getQueryParameter("code");
            System.out.println(code);

            RetrofitService retrofit = new RetrofitService();
            retrofit.initializeRetrofitAuth();
            //OAuth2Api oAuth2Api = retrofit.getRetrofit().create(OAuth2Api.class);

            final Call<OAuthToken> accessTokenCall = retrofit
                                                .getRetrofit()
                                                .create(OAuth2Api.class)
                                                .getJWT("Basic Y2xpZW50OnNlY3JldA==","client","urltocallback://callback","authorization_code",code,"qPsH306-ZDDaOE8DFzVn05TkN3ZZoVmI_6x4LsVglQI");

            accessTokenCall.enqueue(new Callback<OAuthToken>() {
                @Override
                public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                    if(response.body() != null) {
                        String authcode = response.body().toString();
                        System.out.println(authcode);
                    }
                }

                @Override
                public void onFailure(Call<OAuthToken> call, Throwable t) {
                    System.out.println(call);
                    Toast.makeText(MainActivity.this, "Token error",Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}