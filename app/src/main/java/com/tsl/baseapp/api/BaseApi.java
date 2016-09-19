package com.tsl.baseapp.api;

import com.google.gson.JsonObject;
import com.tsl.baseapp.model.objects.project.ProjectsResults;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.ChangePasswordCredentials;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kevinlavi on 4/28/16.
 */
public interface BaseApi {

    @POST("login")
    Observable<Token> loginUser(@Body User auth);

    @POST("register")
    Observable<User> signUpUser(@Body User auth);

    @GET("/v1/user/")
    Observable<User> getUser(@Header(Constants.AUTHORIZATION) String token);

    @POST("forgot-password")
    Observable<Void> forgotPassword(@Body JsonObject auth);

    @POST("v1/auth/change-password/")
    Observable<Void> changePassword(@Header(Constants.AUTHORIZATION) String token, @Body ChangePasswordCredentials creds);

    @GET("v1/projects/")
    Observable<ProjectsResults> getProjects(@Header(Constants.AUTHORIZATION) String token);
}
