package com.example.locals.retrofit;

import com.example.locals.models.Guide;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GuideApi {

    @Headers("Accept: application/json")
    @GET("/guide/cityGuides/{city}")
    Call<List<Guide>> getCityGuides(@Path("city") String CityName);

    @Headers("Accept: application/json")
    @GET("/guide/guideDetails/{id}")
    Call<Guide> getGuideDetails(@Path("id") int guideId);
}
