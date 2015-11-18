package com.tsl.baseapp.Model.Api;

import com.tsl.baseapp.Model.Objects.Token;
import com.tsl.baseapp.Model.Objects.User;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Kevin on 9/17/15.
 */
public interface AppApi {


    @GET("/projects")
    void getProjects(@Header("Authorization") String token,
                     @Query("page") int page,
                     Callback<String> projects);

    @POST("/auth/register/")
    void registerUser(@Body User user, Callback<String> callback);

    @POST("/auth/login/")
    void loginUser(@Body User user, Callback<Token> callback);

    @POST("/social-auth-with-token/")
    void socialLogin(@Body User user, Callback<Token> token);

    @GET("/user/")
    void getCurrentUser(@Header("Authorization") String token,
                        Callback<String> currentUser);

    @PATCH("/user/")
    void updateUser(@Header("Authorization") String token,
                    @Body User user,
                    Callback<String> updateUser);

    @POST("/auth/change-password/")
    void changePassword(@Header("Authorization") String token,
                        @Body User user,
                        Callback<String> changePass);


}
