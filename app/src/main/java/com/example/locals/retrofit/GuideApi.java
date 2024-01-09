package com.example.locals.retrofit;

import com.example.locals.models.Guide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GuideApi {

    @Headers("Accept: application/json")
    @GET("/guide/cityGuides/{city}")
    Call<List<Guide>> getCityGuides(@Header("Authorization") String token, @Path("city") String cityName);

    @Headers("Accept: application/json")
    @GET("/guide/guideDetails/{id}")
    Call<Guide> getGuideDetails(@Header("Authorization") String token, @Path("id") int guideId);

    @Headers("Accept: application/json")
    @GET("/guide/guideDetailsEmail/{email}")
    Call<Guide> getGuideDetailsByEmail(@Header("Authorization") String token, @Path("email") String email);

    @Headers("Accept: application/json")
    @POST("/guide/addNew")
    Call<Void> createGuide(@Header("Authorization") String token, @Body Guide guide);

    @Headers("Accept: application/json")
    @PUT("/guide/update/{email}")
    Call<Void> updateGuide(@Header("Authorization") String token, @Path("email") String email ,@Body Guide guide);

    @Headers("Accept: application/json")
    @PUT("/updateToGuide/{email}")
    Call<Void> updateGuideAuthServer(@Header("Authorization") String token, @Path("email") String email);

    @Headers("Accept: application/json")
    @DELETE("/guide/deleteGuide/{email}")
    Call<Void> deleteGuide(@Header("Authorization") String token, @Path("email") String email);
}
