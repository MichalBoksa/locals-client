package com.example.locals.retrofit;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    public void initializeRetrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                .setDateFormat("yyyy-MM-dd")
                .create();
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.32.5:9090")
                .baseUrl("http://192.168.56.1:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void initializeRetrofitAuth() {

        retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.32.5:8080")
                .baseUrl("http://192.168.56.1:8080")
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

    private class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Base64.decode(json.getAsString(), Base64.NO_WRAP);
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
        }
    }
}
