package com.example.testproject.Utils;

import android.os.Bundle;

import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.common.MyApplication;
import com.example.testproject.database.models.Course;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppPreferenceManager {

    private static final String TEST_RESPONSE = "test_repsonse";
    private static final String TEST_TIME = "test_time";
    private static final String TEST_VALUES = "test_values";
    private static final String TEST_TOPIC_RESPONSE = "test_topic_values";


    public static void saveExam(JSONObject testJSON) {
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putString(TEST_RESPONSE, testJSON.toString()).apply();
    }


    public static JSONObject getExam() {
        String testResponse = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(TEST_RESPONSE, "");
        try {
            return new JSONObject(testResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveExamTopic(JSONObject testJSON) {
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putString(TEST_TOPIC_RESPONSE, testJSON.toString()).apply();
    }


    public static JSONObject getExamTopic() {
        String testResponse = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(TEST_TOPIC_RESPONSE, "");
        try {
            return new JSONObject(testResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTimerValue(String time) {
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putString(TEST_TIME, time).apply();
    }

    public static String getTimerValue() {
        return MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(TEST_TIME, "");
    }

    public static void saveOfflineValues(Bundle bundle) {
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putString(TEST_VALUES, new Gson().toJson(bundle)).apply();
    }

    public static Bundle getOfflineValues() {
        String strValues = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(TEST_VALUES, "");
        return new Gson().fromJson(strValues, Bundle.class);
    }

    public static void clearPrefs() {
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().clear();
    }

}
