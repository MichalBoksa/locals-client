package com.example.locals.retrofit;

import com.example.locals.models.LocationDetails;
import com.example.locals.models.LocationSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface LocationApi {

    @Headers("Accept: application/json")
    @GET("/attractions/{locationId}")
    Call<LocationDetails> getLocationDetails(@Path("locationId") String locationId);

    @Headers("Accept: application/json")
    @GET("/attractions/cityAttractions/{cityName}")
    Call<List<LocationDetails>> getCityAttractions(@Path("cityName") String locationId);
}
