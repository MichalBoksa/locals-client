package com.example.locals.retrofit;

import com.example.locals.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApi {

    @GET
    Call<User> getUser();

    @POST
    Call<User> saveUser(@Body User user);

    @POST
    Call<ResponseBody> signIn(@Body User user);

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
