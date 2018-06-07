package com.example.luisgong.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;


import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);//数据库初始化
        RealmConfiguration configuration =new RealmConfiguration.Builder()
                .name("luis.realm")
                .schemaVersion(1)
                .migration(new CustomMigration())//升级数据库
                .build();
        Realm.setDefaultConfiguration(configuration);

        /*//测试
        Realm realm =Realm.getDefaultInstance();
        Log.i("TAG", realm.getPath());

        realm.beginTransaction();
        Dog dog = realm.createObject(Dog.class);
        dog.setAge(2);
        dog.setName("tom");
        dog.setWeight(20);
        realm.commitTransaction();
        realm.close();*/

    }
    public static Context getAppContext(){
        return MyApplication.context;
    }
}
