package com.example.locals.retrofit;

import com.example.locals.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {

    @Headers("Accept: application/json")
    @GET("/RSUser/getUser/{email}")
    Call<User> getUser(@Header("Authorization") String token, @Path("email") String email);

    @PUT("/RSUser/updateImage/{email}")
    Call<ResponseBody> saveUserImage(@Header("Authorization") String token, @Path("email") String email, @Body byte[] image);


    //TODO check annotations
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/oauth2/authorize")
    Call<ResponseBody> getAuthCode(
            @Field("response_type") String responseType,
            @Field("client_id") String clientId,
            @Field("scope") String scope,
            @Field("redirect_uri") String redirectUri,
            @Field("code_challenge") String codeChallenge,
            @Field("code_challenge_method") String codeChallengeMethod
    );
}
