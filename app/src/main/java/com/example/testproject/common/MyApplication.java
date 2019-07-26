package com.example.testproject.common;

import android.app.Application;

import com.example.testproject.database.ExamDatabase;

public class MyApplication extends Application {

    private static MyApplication appInstance;
    private static ExamDatabase dbInstance;

    public static MyApplication getAppInstance() {
        if (appInstance == null) {
            synchronized (MyApplication.class) {
                appInstance = new MyApplication();
            }
        }
        return appInstance;
    }

    public ExamDatabase getDbInstance() {
        return dbInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        dbInstance = ExamDatabase.getDatabase(this);
    }

}
