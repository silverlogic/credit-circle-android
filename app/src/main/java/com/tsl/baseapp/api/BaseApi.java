package com.tsl.baseapp.api;

import com.tsl.baseapp.model.Objects.token.Token;
import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Objects.user.ChangePasswordCredentials;
import com.tsl.baseapp.model.Objects.user.SignUpCredentials;
import com.tsl.baseapp.model.Objects.user.User;
import com.tsl.baseapp.model.Utilities.Constants;

import clojure.lang.Cons;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by kevinlavi on 4/28/16.
 */
public interface BaseApi {

    @POST("/v1/auth/login/")
    Observable<Token> loginUser(@Body AuthCredentials auth);

    @POST("/v1/auth/register/")
    Observable<User> signUpUser(@Body SignUpCredentials auth);

    @GET("/v1/user/")
    Observable<User> getUser(@Header(Constants.AUTHORIZATION) String token);

    @POST("v1/auth/change-password/")
    Observable<Void> changePassword(@Header(Constants.AUTHORIZATION) String token, @Body ChangePasswordCredentials creds);
}
