package com.tsl.baseapp.model.api;

import com.google.gson.GsonBuilder;
import com.tsl.baseapp.model.utilities.Constants;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Kevin on 9/17/15.
 */
public class ApiManager {

    private PlayersApi mPlayersApi;

    public PlayersApi getPlayersApi(){

        if (mPlayersApi == null){
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(String.class, new StringDesirializer());

            mPlayersApi = new RestAdapter.Builder()
                    .setEndpoint(Constants.BASE_URL)
                    .setConverter(new GsonConverter(gson.create()))
                    .build()
                    .create(PlayersApi.class);
        }

        return mPlayersApi;
    }
}
