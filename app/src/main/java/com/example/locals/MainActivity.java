package com.example.locals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.locals.activities.Home;
import com.example.locals.retrofit.OAuth2Api;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.OAuthToken;
import com.example.locals.utils.PKCE;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

   private String REDIRECT_URI ="urltocallback://callback";
   private final static String clientSecret = "client:secret";
   public final static String authorization = Base64.getUrlEncoder().withoutPadding().encodeToString(clientSecret.getBytes(StandardCharsets.UTF_8));
   private SharedPreferences sharedPref;
   private SharedPreferences.Editor editor;
   private String accessToken;
   private RetrofitService retrofit;
   private Long expiryTime;
   private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         retrofit = new RetrofitService();
        retrofit.initializeRetrofitAuth();
        initializeComponents();
    }

    private void initializeComponents() {
        Button buttonSignIn = findViewById(R.id.buttonLogin);
        accessToken = PKCE.getAccessToken(this);
        expiryTime = PKCE.getTokenExpiryTime(this);

       // UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
      buttonSignIn.setOnClickListener(view -> {
          if(accessToken == null || accessToken.isEmpty() ) {
                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  Toast.makeText(this.getApplicationContext(),"toast",Toast.LENGTH_LONG);
                  intent.setData(Uri.parse("http://192.168.32.6:8080/oauth2/authorize?" +
//                  intent.setData(Uri.parse("http://192.168.56.1:8080/oauth2/authorize?" +
                          "response_type=code&" +
                          "client_id=client&" +
                          "scope=openid&" +
                          "redirect_uri=" + REDIRECT_URI +
                          "&code_challenge="+ PKCE.codeChallenge +
                          "&code_challenge_method=S256"));

                  //TODO check condition
                  //if (intent.resolveActivity(getPackageManager()) != null) {
                      startActivity(intent);
           //       }
                }



          else {
              if (!accessToken.isEmpty() && PKCE.isJWTexpired(this)) {
                  String refreshToken = PKCE.getRefreshToken(this);
                  final Call<OAuthToken> refreshTokenCall = retrofit
                      .getRetrofit()
                      .create(OAuth2Api.class)
                      .getRefreshToken("Basic " + authorization,"client",REDIRECT_URI,"refresh_token",refreshToken,PKCE.codeVerifier);

                  refreshTokenCall.enqueue(new Callback<OAuthToken>() {
                      @Override
                      public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                          if(response.body() != null) {
                              saveTokenData(response);
                              Intent intent = new Intent(MainActivity.this, Home.class);
                              startActivity(intent);
                          }
                      }

                      @Override
                      public void onFailure(Call<OAuthToken> call, Throwable t) {
                          System.out.println(call);
                          Toast.makeText(MainActivity.this, "Token error",Toast.LENGTH_LONG).show();
                      }
                  });

              }

              Intent intent = new Intent(MainActivity.this, Home.class);
              startActivity(intent);}
    }
                );
    }


    @Override
    protected void onResume() {
        super.onResume();
        Uri data = getIntent().getData();

        if(data != null && !TextUtils.isEmpty(data.getScheme())) {
             code = data.getQueryParameter("code");

            final Call<OAuthToken> accessTokenCall = retrofit
                    .getRetrofit()
                    .create(OAuth2Api.class)
                    .getJWT("Basic " + authorization,"client",REDIRECT_URI,"authorization_code",code,PKCE.codeVerifier);

            accessTokenCall.enqueue(new Callback<OAuthToken>() {
                @Override
                public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                    if(response.body() != null) {
                      saveTokenData(response);
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        startActivity(intent);
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


    private void saveTokenData(Response<OAuthToken> response) {
        sharedPref = MainActivity.this.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("access_code", response.body().getAccessToken());
        editor.putString("auth_code", code);
        editor.putString("refresh", response.body().getRefreshToken());
        editor.putLong("expiry", response.body().getExpiresIn());
        editor.commit();
    }


}