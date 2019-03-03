package com.sideonyou.tommee.demoapptest.api;

import com.sideonyou.tommee.demoapptest.model.User;

import java.util.List;
import io.reactivex.Observable;

import retrofit2.http.GET;


public interface ApiService {

    @GET("/users")
     Observable<List<User>> getAllUsers();

}
