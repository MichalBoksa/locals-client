package com.example.locals.retrofit;

import com.example.locals.utils.OAuthToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OAuth2Api {
    @Headers("Accept: application/json")
    @POST("/oauth2/token")
    @FormUrlEncoded
    Call<OAuthToken> getJWT(
            @Header("Authorization") String authorization,
                    @Field("client_id") String clientId,
                    @Field("redirect_uri") String redirectUri,
                    @Field("grant_type") String grantType,
                    @Field("code") String code,
                    @Field("code_verifier") String codeVerifier
    );

    @Headers("Accept: application/json")
    @POST("/oauth2/token")
    @FormUrlEncoded
    Call<OAuthToken> getRefreshToken(
            @Header("Authorization") String authorization,
            @Field("client_id") String clientId,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("code_verifier") String codeVerifier
    );
}
