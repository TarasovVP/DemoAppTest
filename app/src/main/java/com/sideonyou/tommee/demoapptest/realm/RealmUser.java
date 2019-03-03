package com.sideonyou.tommee.demoapptest.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Quang_TD on 7/23/2017.
 */

public class RealmUser extends RealmObject {
    @PrimaryKey
    private int id;

    private String name;

    private String email;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
