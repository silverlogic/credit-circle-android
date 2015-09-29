package com.tsl.baseapp.controller;

import android.util.Log;

import com.tsl.baseapp.model.api.ApiManager;
import com.tsl.baseapp.model.objects.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Kevin on 9/17/15.
 */
public class Controller {

    private static final String TAG = Controller.class.getSimpleName();
    private PlayersCallbackListener mListener;
    private ApiManager mApiManager;

    public Controller(PlayersCallbackListener listener){
        mListener = listener;
        mApiManager = new ApiManager();
    }


    public void startFetchingRB(){
        mApiManager.getPlayersApi().getPlayersRB(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d(TAG, "JSON :: " + s);

                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray rankings = json.getJSONArray("Rankings");
                    Log.d(TAG, "JSON ARRAY :: " + rankings);

                    for (int i = 0; i < rankings.length(); i++) {

                        JSONObject object = rankings.getJSONObject(i);

                        Player player = new Player();
                        player.setName(object.getString("name"));
                        player.setPosition(object.getString("position"));
                        player.setTeam(object.getString("team"));
                        player.setStandardScoring(object.getString("standard"));
                        player.setPprScoring(object.getString("ppr"));

                        Log.d(TAG, "JSON PLAYER :: " + player);

                        mListener.onFetchProgress(player);

                    }

                } catch (JSONException e) {
                    mListener.onFetchFailed();
                    Log.d(TAG, "JSON FAILED :: " + e);

                }

                mListener.onFetchComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error :: " + error.getMessage());
                mListener.onFetchComplete();
            }
        });

    }

    public void startFetchingWR(){
        mApiManager.getPlayersApi().getPlayersWR(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d(TAG, "JSON :: " + s);

                try {
                    JSONObject json = new JSONObject(s);
                    JSONArray rankings = json.getJSONArray("Rankings");
                    Log.d(TAG, "JSON ARRAY :: " + rankings);

                    for (int i = 0; i < rankings.length(); i++) {

                        JSONObject object = rankings.getJSONObject(i);

                        Player player = new Player();
                        player.setName(object.getString("name"));
                        player.setPosition(object.getString("position"));
                        player.setTeam(object.getString("team"));
                        player.setStandardScoring(object.getString("standard"));
                        player.setPprScoring(object.getString("ppr"));

                        Log.d(TAG, "JSON PLAYER :: " + player);

                        mListener.onFetchProgress(player);

                    }

                } catch (JSONException e) {
                    mListener.onFetchFailed();
                    Log.d(TAG, "JSON FAILED :: " + e);

                }

                mListener.onFetchComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error :: " + error.getMessage());
                mListener.onFetchComplete();
            }
        });

    }

    public interface PlayersCallbackListener{

        void onFetchStart();

        void onFetchProgress(Player player);

        void onFetchProgress(List<Player> playerList);

        void onFetchComplete();

        void onFetchFailed();
    }
}
