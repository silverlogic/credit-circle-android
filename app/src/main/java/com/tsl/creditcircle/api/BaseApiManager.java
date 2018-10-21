package com.tsl.creditcircle.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tsl.creditcircle.api.expandable.Expandable;
import com.tsl.creditcircle.api.expandable.ExpandablePreprocessor;
import com.tsl.creditcircle.api.realm.RealmPostProcessor;
import com.tsl.creditcircle.utils.RxErrorHandlingCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.gsonfire.GsonFireBuilder;
import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class BaseApiManager {

    private BaseApi mAppApi;
    private String BASE_URL;


    public BaseApi getAppApi(){


        BASE_URL = "http://silverlogic.ngrok.io/api/";

        if (mAppApi == null){

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            // Create GsonFireBuilder
            GsonFireBuilder fireBuilder = new GsonFireBuilder();
            fireBuilder.registerPreProcessor(Expandable.class, new ExpandablePreprocessor());
            fireBuilder.registerPostProcessor(RealmObject.class, new RealmPostProcessor());

            // Create GsonBuilder
            GsonBuilder gsonBuilder = fireBuilder.createGsonBuilder();
            // Configure gsonBuilder as usual

            // Create gson for GsonBuilder
            Gson gson = gsonBuilder.create();

            mAppApi = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(builder.build())
                    .build()
                    .create(BaseApi.class);
        }

        return mAppApi;
    }
}
