package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by psw10 on 2017-01-23.
 */

public class RealmInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}