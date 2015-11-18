package com.tsl.baseapp.Controller;

import android.content.Context;
import android.util.Log;
import com.tsl.baseapp.Model.Api.ApiManager;
import com.tsl.baseapp.Model.Objects.Token;
import com.tsl.baseapp.Model.Objects.User;
import com.tsl.baseapp.Model.Utilities.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Kevin on 10/15/15.
 */
public class SignUpLoginController {

    private static final String TAG = SignUpLoginController.class.getSimpleName();
    private ApiManager mApiManager;
    private SignUpLoginCallBackListener mListener;
    private Context mContext;


    public SignUpLoginController(SignUpLoginCallBackListener listener, Context ctx){
        mListener = listener;
        mApiManager = new ApiManager();
        mContext = ctx;
    }

    public void signUp(final User nUser) {
        mApiManager.getAppApi().registerUser(nUser, new Callback<String>() {
            @Override
            public void success(String user, Response response) {
                try {
                    JSONObject jsonUser = new JSONObject(user);
                    User mUser = new User();

                    mUser.setId(jsonUser.getInt("id"));
                    mUser.setEmail(jsonUser.getString("email"));
                    mUser.setFirst_name(jsonUser.getString("first_name"));
                    mUser.setLast_name(jsonUser.getString("last_name"));
                    mUser.setSlug(jsonUser.getString("slug"));
                    mUser.setFacebook(jsonUser.getString("facebook"));
                    mUser.setInstagram(jsonUser.getString("instagram"));
                    mUser.setLinkedin(jsonUser.getString("linkedin"));
                    mUser.setImg_url(jsonUser.getString("image_url"));
                    mUser.setTagline(jsonUser.getString("tagline"));
                    mUser.setPassword(nUser.getPassword());

                    SaveSharedPreference.setCurrentUser(mUser, mContext);

                    login(mUser);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mListener.onSignUpFailed();

            }
        });

    }

    public void login(final User user) {
        mApiManager.getAppApi().loginUser(user, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                SaveSharedPreference.setCurrentPassword(mContext, user.getPassword());
                mListener.onLoginComplete(token.getToken());

            }

            @Override
            public void failure(RetrofitError error) {
                mListener.onLoginFailure();
                Log.d(TAG, "ErrorUSER :: " + error.getMessage());
                Log.d(TAG, "ErrorUSER :: " + error.getResponse().toString());
                Log.d(TAG, "ErrorUSER :: " + error.getBody().toString());
            }
        });

    }

    public void updateUser(User mUser, String token){
        Log.d("USERNAME:: ", mUser.getFirst_name());
        mApiManager.getAppApi().updateUser(token, mUser, new Callback<String>() {
            @Override
            public void success(String user, Response response) {
                mListener.onSignUpComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ErrorUPDATE :: " + error.getMessage());
                Log.d(TAG, "ErrorUPDATE :: " + error.getResponse().toString());
                Log.d(TAG, "ErrorUPDATE :: " + error.getBody().toString());
                mListener.onSignUpFailed();

            }
        });
    }

    public void changePassword(String token, User user){
        mApiManager.getAppApi().changePassword(token, user, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d("SUCCESS:: ", s);
                mListener.onPassChanged();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ErrorUPDATE :: " + error.getMessage());
                Log.d(TAG, "ErrorUPDATE :: " + error.getResponse().toString());
                Log.d(TAG, "ErrorUPDATE :: " + error.getBody().toString());
                mListener.onPassFailed();
            }
        });
    }

    public void facebookLogin(User user){
        mApiManager.getAppApi().socialLogin(user, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                mListener.onFacebookLoggedIn(token.getToken());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "ErrorUSER :: " + error.getMessage());
                Log.d(TAG, "ErrorUSER :: " + error.getResponse().toString());
                Log.d(TAG, "ErrorUSER :: " + error.getBody().toString());
                mListener.onLoginFailure();
            }
        });
    }

    public interface SignUpLoginCallBackListener {

        void onLoginComplete(String token);

        void onLoginFailure();

        void onSignUpComplete();

        void onSignUpFailed();

        void onPassChanged();

        void onPassFailed();

        void onFacebookLoggedIn(String token);
    }
}
