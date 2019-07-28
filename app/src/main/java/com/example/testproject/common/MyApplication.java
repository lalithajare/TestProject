package com.example.testproject.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.testproject.database.ExamDatabase;

public class MyApplication extends Application {

    private static MyApplication appInstance;
    private static ExamDatabase dbInstance;
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

    public ExamDatabase getDbInstance() {
        return dbInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        dbInstance = ExamDatabase.getDatabase(this);
        sharedPreferences = getSharedPreferences("EXAM_APP", MODE_PRIVATE);
    }

}
