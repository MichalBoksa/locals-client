package com.example.locals.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.MessageDigest;
import java.util.Date;

public class PKCE {

    public static final String codeVerifier = codeVerifier();
    public static final String codeChallenge = codeChallenge(codeVerifier);


    public static String codeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
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

        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static String getJWTUser(String jwt) {
        DecodedJWT decode = JWT.decode(jwt);
        return decode.getClaim("username").asString();
    }


    public static String getAccessToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return sharedPref.getString("access_code","");
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        return sharedPref.getString("refresh","");
    }

    public static Long getTokenExpiryTime(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        return sharedPref.getLong("expiry",0);
    }

    public static String getAuthToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        return sharedPref.getString("auth_code","");
    }

    public static boolean isJWTexpired(Context context) {
        DecodedJWT jwt = JWT.decode(PKCE.getAccessToken(context));
        return jwt.getExpiresAt().before(new Date());
    }

}
