package com.sideonyou.tommee.demoapptest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.sideonyou.tommee.demoapptest.R;
import com.sideonyou.tommee.demoapptest.api.ApiClient;
import com.sideonyou.tommee.demoapptest.model.User;
import com.sideonyou.tommee.demoapptest.realm.RealmUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.exceptions.RealmMigrationNeededException;


public class MainActivity extends AppCompatActivity {

    private List<User> listUsers;

    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    private Realm realmUI;
    private SwipeRefreshLayout srLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        fetchDataCaching();
    }

    private void initComponent() {
        realmUI = Realm.getDefaultInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        listUsers = new ArrayList<>();
        adapter = new RecycleViewAdapter(getApplicationContext(), listUsers);
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        srLayout = (SwipeRefreshLayout) findViewById(R.id.srLayout);
        srLayout.setOnRefreshListener(this::fetchDataWithoutCaching);
    }
    private void fetchDataCaching() {
        Observable<List<User>> observable = ApiClient.getService().getAllUsers();
        observable
                .delay(1L, java.util.concurrent.TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::writeToDB)
                .observeOn(Schedulers.computation())
                .map(this::readAllFromDB)
                .mergeWith(Observable.just(readAllFromDB(null)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::display, this::processError);
    }
    private void fetchDataWithoutCaching() {
        Observable<List<User>> observable = ApiClient.getService().getAllUsers();
        observable
                .delay(1L, java.util.concurrent.TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(this::writeToDB)
                .observeOn(Schedulers.computation())
                .map(this::readAllFromDB)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::display, this::processError);
    }

    private void processError(Throwable throwable) {
        Toast.makeText(this, "Check your network connection!!", Toast.LENGTH_SHORT).show();

    }
    private void display(List<User> listUsers) {
        if (srLayout.isRefreshing()) srLayout.setRefreshing(false);
        this.listUsers.clear();
        for (int i = 0; i < listUsers.size(); i++) {
            this.listUsers.add(listUsers.get(i));
        }
        adapter.notifyDataSetChanged();
    }
    private RealmUser findInDb(Realm realm, int id) {
        return realm.where(RealmUser.class).equalTo("id", id).findFirst();
    }

    private List<User> findAllInDB(Realm realm) {
        List<RealmUser> realmUser = realm.where(RealmUser.class).findAll();
        List<User> list = new ArrayList<>();
        for (int i = 0; i < realmUser.size(); i++) {
            User user = new User();
            user.setId(realmUser.get(i).getId());
            user.setName(realmUser.get(i).getName());
            user.setEmail(realmUser.get(i).getEmail());
            list.add(user);
        }
        return list;
    }
    private List<User> readAllFromDB(List<User> issueResponses) {
        return findAllInDB(Realm.getDefaultInstance());
    }
    private List<User> writeToDB(List<User> listUsers) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(transactionRealm -> {
            for (int i = 0; i < listUsers.size(); i++) {
                User userResponse = listUsers.get(i);
                RealmUser userRealm = findInDb(transactionRealm, userResponse.getId());
                if (userRealm == null) {
                    userRealm = transactionRealm.createObject(RealmUser.class, userResponse.getId());
                }
                userRealm.setEmail(userResponse.getEmail());
                userRealm.setName(userResponse.getName());
            }
        });
        realm.close();
        return listUsers;

    }


    @Override protected void onDestroy() {
        super.onDestroy();
        realmUI.close();
    }
}



