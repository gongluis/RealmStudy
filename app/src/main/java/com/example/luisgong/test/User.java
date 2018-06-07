package com.example.luisgong.test;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class User implements RealmModel{
    @PrimaryKey
    private int id;
    private String name;
    private int age;
    private String enlishname;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEnlishname() {
        return enlishname;
    }

    public void setEnlishname(String enlishname) {
        this.enlishname = enlishname;
    }

}
