package com.example.locals.retrofit;

import com.example.locals.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    @GET("/users/getUser/{email}")
    Call<User> getUser(@Header("Authorization") String token, @Path("email") String email);

    @Headers("Accept: application/json")
    @GET("/users/getUserById/{id}")
    Call<User> getUserId(@Header("Authorization") String token, @Path("id") Integer id);

    @PUT("/users/updateImage/{email}")
    Call<ResponseBody> saveUserImage(@Header("Authorization") String token, @Path("email") String email, @Body String image);

    @PUT("/users/updateEmail/{email}")
    Call<ResponseBody> saveUserEmail(@Header("Authorization") String token, @Path("email") String email, @Body String newEmail);

    @PUT("/users/updatePhone/{email}")
    Call<ResponseBody> saveUserPhone(@Header("Authorization") String token, @Path("email") String email, @Body String phone);

    @PUT("/updateToGuide/{email}")
    Call<Void> updateToGuide(@Header("Authorization") String token, @Path("email") String email);

    @DELETE("/deleteUser/{email}")
    Call<Void> deleteUser(@Header("Authorization") String token, @Path("email") String email);

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
