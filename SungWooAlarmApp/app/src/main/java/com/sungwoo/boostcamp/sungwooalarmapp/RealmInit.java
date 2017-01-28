package com.sungwoo.boostcamp.sungwooalarmapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by psw10 on 2017-01-23.
 */
//처음 Realm을 초기화시키기 위한 클래스
public class RealmInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}