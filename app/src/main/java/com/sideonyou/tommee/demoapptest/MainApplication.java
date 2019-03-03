package com.sideonyou.tommee.demoapptest;

import android.app.Application;

import com.sideonyou.tommee.demoapptest.api.ApiClient;
import com.sideonyou.tommee.demoapptest.api.ApiConfig;

import io.realm.Realm;


/**
 * Created by oanhdao on 4/25/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createService();
        createDatabase();

    }

    private void createDatabase() {
        Realm.init(this);
    }

    private void createService() {
        ApiConfig apiConfig = new ApiConfig(this, getString(R.string.url_base));
        ApiClient.getInstance().init(apiConfig);
    }
}
