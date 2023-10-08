package com.example.locals.retrofit;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    public void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.32.6:9090")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public void initializeRetrofitAuth(){

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.32.6:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    //TODO rewrite this
//    public void initializeRetrofitLogin() {
//        String codeChallenge = "QYPAZ5NU8yvtlQ9erXrUYR-T5AGCjCF47vN-KsaI2A8";
//        retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.56.1:8080/oauth2/authorize?" +
//                        "response_type=code&" +
//                        "client_id=client&" +
//                        "scope=openid&" +
//                        "redirect_uri=http://locals-app.com/redirect&" +
//                        "code_challenge="+
//                        codeChallenge
//                        +"&code_challenge_method=S256")
//                .addConverterFactory(GsonConverterFactory.create(new Gson()))
//                .build();
//    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
