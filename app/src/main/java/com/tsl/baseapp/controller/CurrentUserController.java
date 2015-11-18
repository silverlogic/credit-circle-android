package com.tsl.baseapp.Controller;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.tsl.baseapp.Model.Api.ApiManager;
import com.tsl.baseapp.Model.Objects.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Kevin on 10/29/15.
 */
public class CurrentUserController {

    private ApiManager mApiManager;
    private CurrentUserControllerCallBackListener mListener;
    private Context mContext;

    public CurrentUserController(CurrentUserControllerCallBackListener listener, Context ctx){
        mListener = listener;
        mApiManager = new ApiManager();
        mContext = ctx;
    }


    public void getCurrentUser(String token){
        mApiManager.getAppApi().getCurrentUser(token, new Callback<String>() {
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

                    mListener.onUserComplete(mUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                mListener.onUserFailure();
                Log.d("GET USER ERROR:: " , error.toString());

            }
        });
    }

    public void getCurrentFacebookUser(String token){
        mApiManager.getAppApi().getCurrentUser(token, new Callback<String>() {
            @Override
            public void success(final String user, Response response) {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("GraphResponse", "-------------" + response.toString());
                                try {
                                    Profile mProfile = Profile.getCurrentProfile();
                                    JSONObject jsonUser = new JSONObject(user);
                                    final User mUser = new User();

                                    mUser.setId(jsonUser.getInt("id"));
                                    mUser.setFirst_name(mProfile.getFirstName());
                                    mUser.setLast_name(mProfile.getLastName());
                                    mUser.setSlug(jsonUser.getString("slug"));
                                    mUser.setFacebook(String.valueOf(mProfile.getLinkUri()));
                                    mUser.setInstagram(jsonUser.getString("instagram"));
                                    mUser.setLinkedin(jsonUser.getString("linkedin"));
                                    String profilePhoto = "https://graph.facebook.com/" + mProfile.getId() + "/picture?type=large";
                                    mUser.setImg_url(profilePhoto);
                                    mUser.setTagline(jsonUser.getString("tagline"));
                                    String mEmail = response.getJSONObject().getString("email");
                                    mUser.setEmail(mEmail);

                                    mListener.onUserComplete(mUser);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void failure(RetrofitError error) {
                mListener.onUserFailure();
                Log.d("GET USER ERROR:: " , error.toString());

            }
        });
    }


    public interface CurrentUserControllerCallBackListener {

        void onUserComplete(User user);

        void onUserFailure();

    }
}
