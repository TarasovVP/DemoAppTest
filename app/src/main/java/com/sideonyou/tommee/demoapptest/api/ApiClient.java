package com.sideonyou.tommee.demoapptest.api;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.sideonyou.tommee.demoapptest.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {
    private static final String TAG = ApiClient.class.getSimpleName();
    private static ApiClient sInstance;
    private ApiService service;
    private Context context;

    public static ApiClient getInstance() {
        if (sInstance == null) {
            sInstance = new ApiClient();
        }
        return sInstance;
    }

    public static ApiService getService() {
        return getInstance().service;
    }


    public void init(ApiConfig apiConfig) {
        context = apiConfig.getContext();

        // init OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.url_base))
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // init Service
        service = retrofit.create(ApiService.class);
    }
}
