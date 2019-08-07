package com.example.testproject.common;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.testproject.database.QuizDatabase;

public class MyApplication extends Application {

    private static MyApplication appInstance;
    private static SharedPreferences sharedPreferences;
    private static QuizDatabase dbInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        sharedPreferences = getSharedPreferences("EXAM_APP", MODE_PRIVATE);
        dbInstance = QuizDatabase.getDatabase(this);
    }


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

    public QuizDatabase getDbInstance() {
        return dbInstance;
    }

}
