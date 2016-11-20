package com.example.jay.databindingdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by JAY on 2016/11/17.
 */

public class User extends BaseObservable{
    private String name;
    private String age;
    private boolean isAdult;

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public User(String name, String age, boolean isAdult) {
        this.name = name;
        this.age = age;
        this.isAdult = isAdult;
    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        /**
         * 通知更新UI界面
         */
        notifyPropertyChanged(BR.name);
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
