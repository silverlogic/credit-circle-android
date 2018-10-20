package com.tsl.money2020.api;

import com.google.gson.JsonObject;
import com.tsl.money2020.model.objects.api.PaginatedResponse;
import com.tsl.money2020.model.objects.token.Token;
import com.tsl.money2020.model.objects.user.SocialAuth;
import com.tsl.money2020.model.objects.user.UpdateUser;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.model.objects.user.UserList;
import com.tsl.money2020.utils.Constants;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
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

    @POST("forgot-password/reset")
    Observable<Void> forgotPasswordReset(@Body User user);

    @POST("users/change-password")
    Observable<JsonObject> changePassword(@Header(Constants.AUTHORIZATION) String token, @Body User creds);

    @POST("change-email")
    Observable<Void> changeEmail(@Header(Constants.AUTHORIZATION) String token, @Body User creds);

    @POST("change-email/{id}/confirm")
    Observable<Void> changeEmailConfirm(@Path("id") int id, @Body User token);

    @POST("change-email/{id}/verify")
    Observable<Void> changeEmailVerify(@Path("id") int id, @Body User token);

    @POST("users/{id}/confirm-email")
    Observable<Void> confirmEmail(@Path("id") int id, @Body User token);

    @GET("users/me")
    Observable<User> getCurrentUser(@Header(Constants.AUTHORIZATION) String token);

    @GET("users")
    Observable<PaginatedResponse<User>> getUserList(@Header(Constants.AUTHORIZATION) String token, @Query("page") int page);

    @PATCH("users/{id}")
    Observable<User> updateUser(@Header(Constants.AUTHORIZATION) String token, @Path("id") int id, @Body UpdateUser user);

    @GET("users")
    Observable<UserList> searchUser(@Header(Constants.AUTHORIZATION) String token, @Query("page") int page, @Query("q") String query);

    @POST("social-auth")
    Observable<Token> socialLogin(@Body SocialAuth auth);

    @POST("social-auth")
    Observable<SocialAuth> socialLoginTwitter(@Body SocialAuth auth);

}
