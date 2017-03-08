package com.tsl.baseapp.api;

import com.google.gson.JsonObject;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.UpdateUser;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.model.objects.user.UserList;
import com.tsl.baseapp.utils.Constants;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kevinlavi on 4/28/16.
 */
public interface BaseApi {

    @POST("login")
    Observable<Token> loginUser(@Body User auth);

    @POST("register")
    Observable<User> signUpUser(@Body User auth);

    @POST("forgot-password")
    Observable<Void> forgotPassword(@Body JsonObject auth);

    @POST("users/change-password")
    Observable<JsonObject> changePassword(@Header(Constants.AUTHORIZATION) String token, @Body User creds);

    @POST("change-email")
    Observable<Void> changeEmail(@Header(Constants.AUTHORIZATION) String token, @Body User creds);

    @GET("users/me")
    Observable<User> getCurrentUser(@Header(Constants.AUTHORIZATION) String token);

    @GET("users")
    Observable<UserList> getUserList(@Header(Constants.AUTHORIZATION) String token, @Query("page") int page);

    @PATCH("users/{id}")
    Observable<User> updateUser(@Header(Constants.AUTHORIZATION) String token, @Path("id") int id, @Body UpdateUser user);
}
