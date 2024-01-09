package com.example.locals.utils;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.locals.MainActivity;
import com.example.locals.activities.Home;
import com.example.locals.retrofit.OAuth2Api;
import com.example.locals.retrofit.RetrofitService;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PKCE {

    public static final String codeVerifier = codeVerifier();
    public static final String codeChallenge = codeChallenge(codeVerifier);
    private final static String clientSecret = "client:secret";
    private final static String authorization = Base64.getUrlEncoder().withoutPadding().encodeToString(clientSecret.getBytes(StandardCharsets.UTF_8));


    public static String codeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
//        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("CODE_VERIFIER", Base64.getUrlEncoder().withoutPadding().encodeToString(code));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    public static String codeChallenge(String codeVerifier) {
        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            bytes = md.digest(codeVerifier.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

//        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("CODE_CHALLENGE", Base64
//                .getUrlEncoder()
//                .withoutPadding()
//                .encodeToString(bytes));
        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static String getJWTUser(String jwt) {
        DecodedJWT decode = JWT.decode(jwt);
        return decode.getClaim("username").asString();
    }

    public static List<String> getJWTAuthorities(String jwt) {
        DecodedJWT decode = JWT.decode(jwt);
        return decode.getClaim("authorities").asList(String.class);
    }


    public static String getAccessToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("access_code", "");
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("refresh", "");
    }

    public static Long getTokenExpiryTime(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getLong("expiry", 0);
    }

    public static String getAuthToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("auth_code", "");
    }

    public static void logoutTokens(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        sharedPref.edit().putString("auth_code", "").apply();
        sharedPref.edit().putLong("expiry", 0).apply();
        sharedPref.edit().putString("refresh", "").apply();
        sharedPref.edit().putString("access_code", "").apply();
    }

    public static boolean isJWTexpired(Context context) {
        DecodedJWT jwt = JWT.decode(PKCE.getAccessToken(context));
        return jwt.getExpiresAt().before(new Date());
    }

    public static void refreshToken(Context context, String REDIRECT_URI) {
        RetrofitService retrofit = new RetrofitService();
        retrofit.initializeRetrofitAuth();

        String refreshToken = PKCE.getRefreshToken(context);
        final Call<OAuthToken> refreshTokenCall = retrofit
                .getRetrofit()
                .create(OAuth2Api.class)
                .getRefreshToken("Basic " + authorization, "client", REDIRECT_URI, "refresh_token", refreshToken, PKCE.codeVerifier);

        refreshTokenCall.enqueue(new Callback<OAuthToken>() {
            @Override
            public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                if (response.body() != null) {
                    PKCE.saveTokenData(response, context);
                }

                if(response.code() == 400) {
                    PKCE.getAuthorizationToken(REDIRECT_URI,context);
                }
            }

            @Override
            public void onFailure(Call<OAuthToken> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(context, "Token error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void saveTokenData(Response<OAuthToken> response, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("access_code", response.body().getAccessToken());
        editor.putString("refresh", response.body().getRefreshToken());
        editor.putLong("expiry", response.body().getExpiresIn());
        editor.commit();
    }

    public static void getAuthorizationToken(String REDIRECT_URI, Context context) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://192.168.32.5:8080/oauth2/authorize?" +
                  intent.setData(Uri.parse("http://192.168.56.1:8080/oauth2/authorize?" +
                "response_type=code&" +
                "client_id=client&" +
                "scope=openid&" +
                "redirect_uri=" + REDIRECT_URI +
                "&code_challenge=" + PKCE.codeChallenge +
                "&code_challenge_method=S256"));
        startActivity(context,intent,null);
    }

    public static void AuthorizationTokenResume(AppCompatActivity context, String REDIRECT_URI){
        Uri data = context.getIntent().getData();
        RetrofitService retrofit = new RetrofitService();
        retrofit.initializeRetrofitAuth();

        if(data != null && !TextUtils.isEmpty(data.getScheme())) {
            String code = data.getQueryParameter("code");

            final Call<OAuthToken> accessTokenCall = retrofit
                    .getRetrofit()
                    .create(OAuth2Api.class)
                    .getJWT("Basic " + authorization,"client",REDIRECT_URI,"authorization_code",code,PKCE.codeVerifier);

            accessTokenCall.enqueue(new Callback<OAuthToken>() {
                @Override
                public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                    if(response.body() != null) {
                        PKCE.saveTokenData(response,context);
                        Intent intent = new Intent(context, Home.class);
                        context.startActivity(intent);
                    }
                }
                @Override
                public void onFailure(Call<OAuthToken> call, Throwable t) {
                    System.out.println(call);
                    Toast.makeText(context, "Token error",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
