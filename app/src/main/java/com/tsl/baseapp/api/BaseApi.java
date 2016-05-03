package com.tsl.baseapp.api;

import com.tsl.baseapp.model.Objects.token.Token;
import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Objects.user.SignUpCredentials;
import com.tsl.baseapp.model.Objects.user.User;

import retrofit2.http.Body;
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
}
