package com.example.locals.retrofit;

import com.example.locals.models.Booking;
import com.example.locals.utils.PKCE;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BookingApi {

    @Headers("Accept: application/json")
    @POST("/booking/addBooking")
    Call<Void> addBooking(@Header("Authorization") String token, @Body Booking booking);
}
