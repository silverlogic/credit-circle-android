package com.tsl.baseapp.model.Api;

import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.model.Utilities.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kevin on 9/17/15.
 */
public class ApiManager {

    private AppApi mAppApi;
    private String BASE_URL;

    public AppApi getAppApi(){

        if (BuildConfig.IS_RELEASE){
            BASE_URL = Constants.PRODUCTION_URL;
        }
        else {
            BASE_URL = Constants.STAGING_URL;
        }

        if (mAppApi == null){

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);


            mAppApi = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AppApi.class);
        }

        return mAppApi;
    }
}
