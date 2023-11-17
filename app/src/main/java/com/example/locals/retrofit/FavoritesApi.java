package com.example.locals.retrofit;

import com.example.locals.models.Favorites;
import com.example.locals.models.LocationDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoritesApi {

    @Headers("Accept: application/json")
    @GET("/favorites/userFavorites/{id}")
    Call<ArrayList<Favorites>> getUserFavorites(@Header("Authorization") String token, @Path("id") int userId);

    @Headers("Accept: application/json")
    @GET("/attractions/favoritesLocations/{locationIds}")
    Call<List<LocationDetails>> getFavoritesListDetails(@Header("Authorization") String token,@Path("locationIds") String locationIds);

    @Headers("Accept: application/json")
    @POST("/favorites/addNewList")
    Call<Void> addList(@Header("Authorization") String token,@Body Favorites favorites);
}
