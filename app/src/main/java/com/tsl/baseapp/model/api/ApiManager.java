package com.tsl.baseapp.Model.Api;

import com.google.gson.GsonBuilder;
import com.tsl.baseapp.Model.Utilities.Constants;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Kevin on 9/17/15.
 */
public class ApiManager {

    private AppApi mAppApi;

    public AppApi getAppApi(){

        if (mAppApi == null){
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(String.class, new StringDesirializer());

            mAppApi = new RestAdapter.Builder()
                    .setEndpoint(Constants.BASE_URL)
                    .setConverter(new GsonConverter(gson.create()))
                    .build()
                    .create(AppApi.class);
        }

        return mAppApi;
    }
}
