package com.example.testproject.common;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {

    private static MyApplication appInstance;
    private static SharedPreferences sharedPreferences;

    public static MyApplication getAppInstance() {
        if (appInstance == null) {
            synchronized (MyApplication.class) {
                appInstance = new MyApplication();
            }
        }
        return appInstance;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        sharedPreferences = getSharedPreferences("EXAM_APP", MODE_PRIVATE);
    }

}
