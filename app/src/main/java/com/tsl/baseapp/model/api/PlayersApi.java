package com.tsl.baseapp.model.api;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Kevin on 9/17/15.
 */
public interface PlayersApi {

    @GET("/weekly-rankings/json/ndzivngvzrmy/RB/2/0/")
    void getPlayersRB(Callback<String> players);

    @GET("/weekly-rankings/json/ndzivngvzrmy/WR/2/0/")
    void getPlayersWR(Callback<String> players);

}
