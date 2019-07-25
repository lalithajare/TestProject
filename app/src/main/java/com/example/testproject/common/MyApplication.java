package com.example.testproject.common;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication appInstance;

    public static MyApplication getAppInstance() {
        if (appInstance == null) {
            synchronized (MyApplication.class) {
                appInstance = new MyApplication();
            }
        }
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

}
