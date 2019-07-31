package com.example.testproject.Utils;

import android.text.TextUtils;

import com.example.testproject.common.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class AppPreferenceManager {

    private static final String DELIMITER = "~";

    private static final String SAVED_QUIZES_STATES = "quizes_ids_and_times_page_index";
    private static final String SAVED_QUIZES_OFFLINE_ATTEMPT_STATES = "quizes_offline_attempts";

    public static void addQuizState(String quizId, String quizPauseTime, String pagerIndex) {

        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_QUIZES_STATES, new HashSet<String>());
        for (String state : savedQuizStates) {
            if (TextUtils.equals(state.split(DELIMITER)[0], quizId)) {
                savedQuizStates.remove(state);
                break;
            }
        }
        savedQuizStates.add(quizId + DELIMITER + quizPauseTime + DELIMITER + pagerIndex);
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putStringSet(SAVED_QUIZES_STATES, savedQuizStates).apply();
    }

    public static Set<String> getQuizStates() {
        return MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_QUIZES_STATES, new HashSet<String>());
    }

    public static boolean isQuizStateExists(String quizId) {
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_QUIZES_STATES, new HashSet<String>());
        for (String states : savedQuizStates) {
            if (TextUtils.equals(states.split(DELIMITER)[0], quizId)) {
                return true;
            }
        }
        return false;
    }

    //return time in HH:mm:ss
    public static String getQuizPauseTime(String quizId) {
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_QUIZES_STATES, new HashSet<String>());
        for (String states : savedQuizStates) {
            if (TextUtils.equals(states.split(DELIMITER)[0], quizId)) {
                return states.split(DELIMITER)[1];
            }
        }
        return null;
    }

    //return time in HH:mm:ss
    public static String getQuizPageIndex(String quizId) {
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_QUIZES_STATES, new HashSet<String>());
        for (String states : savedQuizStates) {
            if (TextUtils.equals(states.split(DELIMITER)[0], quizId)) {
                return states.split(DELIMITER)[2];
            }
        }
        return null;
    }


    public static void deleteTestState(String quizId) {
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_QUIZES_STATES, new HashSet<String>());

        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();

        for (String state : savedQuizStates) {
            if (TextUtils.equals(state.split(DELIMITER)[0], quizId)) {
                savedQuizStates.remove(state);
                MyApplication.getAppInstance()
                        .getSharedPreferences()
                        .edit().putStringSet(SAVED_QUIZES_STATES, savedQuizStates).apply();
                break;
            }
        }
        attemptStates.remove(quizId);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_QUIZES_OFFLINE_ATTEMPT_STATES, new Gson().toJson(attemptStates)).apply();
    }

    public static void saveOfflineAttemptStates(String quizId, ArrayList<Hashtable<String, String>> offlineAttempts) {
        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();
        attemptStates.put(quizId, offlineAttempts);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_QUIZES_OFFLINE_ATTEMPT_STATES, new Gson().toJson(attemptStates)).apply();
    }

    public static HashMap<String, ArrayList<Hashtable<String, String>>> getAllTestsAndOfflineAttemptStates() {
        java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Hashtable<String, String>>>>() {
        }.getType();
        return new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_QUIZES_OFFLINE_ATTEMPT_STATES, new Gson().toJson(new HashMap<String, ArrayList<Hashtable<String, String>>>())), type);
    }

    public static ArrayList<Hashtable<String, String>> getOfflineAttemptState(String quizId) {
        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();
        if (attemptStates != null)
            return attemptStates.get(quizId);
        return null;
    }

}
