package com.example.locals.retrofit;

import com.example.locals.models.Booking;
import com.example.locals.utils.PKCE;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookingApi {

    @Headers("Accept: application/json")
    @POST("/booking/addBooking")
    Call<Void> addBooking(@Header("Authorization") String token, @Body Booking booking);

    @Headers("Accept: application/json")
    @GET("/booking/getUserBookings/{email}")
    Call<List<Booking>> getBookings(@Header("Authorization") String token, @Path("email") String email);
}
