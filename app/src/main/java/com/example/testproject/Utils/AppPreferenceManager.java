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

    private static final String SAVED_TOPIC_STATES = "quizes_ids_and_times_page_index";
    private static final String SAVED_TOPICS_OFFLINE_ATTEMPT_STATES = "quizes_offline_attempts";
    private static final String SAVED_MARKED_QUESTIONS = "marked_questions";
    private static final String MAP_QUIZ_TIME = "quiz_time";

    public static void addTopicState(String quizId, String topicId, String quizPauseTime, String pagerIndex) {

        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_TOPIC_STATES, new HashSet<String>());
        for (String state : savedQuizStates) {
            if (TextUtils.equals(state.split(DELIMITER)[0], quizId) && TextUtils.equals(state.split(DELIMITER)[1], topicId)) {
                savedQuizStates.remove(state);
                break;
            }
        }
        savedQuizStates.add(quizId + DELIMITER + topicId + DELIMITER + quizPauseTime + DELIMITER + pagerIndex);
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putStringSet(SAVED_TOPIC_STATES, savedQuizStates).apply();

        //Save quiz time
        Set<String> savedQuizTimes = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(MAP_QUIZ_TIME, new HashSet<String>());
        for (String quizTime : savedQuizTimes) {
            if (TextUtils.equals(quizTime.split(DELIMITER)[0], quizId)) {
                savedQuizTimes.remove(quizTime);
                break;
            }
        }
        savedQuizTimes.add(quizId + DELIMITER + quizPauseTime);
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putStringSet(MAP_QUIZ_TIME, savedQuizTimes).apply();
    }


    public static boolean isQuizStateExists(String quizId) {
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_TOPIC_STATES, new HashSet<String>());
        for (String states : savedQuizStates) {
            if (TextUtils.equals(states.split(DELIMITER)[0], quizId)) {
                return true;
            }
        }
        return false;
    }

    //return time in HH:mm:ss
    public static String getQuizPauseTime(String quizId) {
        Set<String> savedQuizTimes = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(MAP_QUIZ_TIME, new HashSet<String>());
        for (String quizTime : savedQuizTimes) {
            if (TextUtils.equals(quizTime.split(DELIMITER)[0], quizId)) {
                return quizTime.split(DELIMITER)[1];
            }
        }
        return null;
    }

    //return time in HH:mm:ss
    public static String getTopicPageIndex(String quizId, String topicId) {
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_TOPIC_STATES, new HashSet<String>());
        for (String states : savedQuizStates) {
            if (TextUtils.equals(states.split(DELIMITER)[0], quizId) && TextUtils.equals(states.split(DELIMITER)[1], topicId)) {
                return states.split(DELIMITER)[3];
            }
        }
        return "0";
    }


    public static void deleteTestState(String quizId, String topicId) {

        //Delete quiz states
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_TOPIC_STATES, new HashSet<String>());
        for (String state : savedQuizStates) {
            if (TextUtils.equals(state.split(DELIMITER)[0], quizId)
                    && TextUtils.equals(state.split(DELIMITER)[1], topicId)) {
                savedQuizStates.remove(state);
                MyApplication.getAppInstance()
                        .getSharedPreferences()
                        .edit().putStringSet(SAVED_TOPIC_STATES, savedQuizStates).apply();
                break;
            }
        }

        //Delete offline attempts
        HashMap<String, ArrayList<Hashtable<String, String>>> attempts = new HashMap<>();
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_TOPICS_OFFLINE_ATTEMPT_STATES, new Gson().toJson(attempts)).apply();

        //Delete Paused Times
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putStringSet(MAP_QUIZ_TIME, new HashSet<String>()).apply();

        //Delete Marked Questions
        Hashtable<String, Set<String>> markedQues = new Hashtable<>();
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_MARKED_QUESTIONS, new Gson().toJson(markedQues)).apply();

    }

    public static void saveOfflineAttemptStates(String quizId, String topicId, ArrayList<Hashtable<String, String>> offlineAttempts) {
        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();
        attemptStates.put(quizId + DELIMITER + topicId, offlineAttempts);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_TOPICS_OFFLINE_ATTEMPT_STATES, new Gson().toJson(attemptStates)).apply();
    }

    public static HashMap<String, ArrayList<Hashtable<String, String>>> getAllTestsAndOfflineAttemptStates() {
        java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Hashtable<String, String>>>>() {
        }.getType();
        return new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_TOPICS_OFFLINE_ATTEMPT_STATES, new Gson().toJson(new HashMap<String, ArrayList<Hashtable<String, String>>>())), type);
    }

    public static ArrayList<Hashtable<String, String>> getOfflineAttemptState(String quizId, String topicId) {
        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();
        if (attemptStates != null)
            return attemptStates.get(quizId + DELIMITER + topicId);
        return null;
    }


    public static void addMarkedQuestions(String quizId, String topicId, ArrayList<String> questions) {
        Hashtable<String, Set<String>> markedQues = getAllMarkedQuestions();
        Set<String> topicMarkedQues = markedQues.get(quizId + DELIMITER + topicId);
        if (topicMarkedQues != null && !topicMarkedQues.isEmpty()) {
            topicMarkedQues.clear();
        }
        topicMarkedQues = (Set<String>) questions;
        markedQues.put(quizId + DELIMITER + topicId, topicMarkedQues);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_MARKED_QUESTIONS, new Gson().toJson(markedQues)).apply();
    }

    //    <quiz_id,<topic_id,question_id>>
    public static Hashtable<String, Set<String>> getAllMarkedQuestions() {
        java.lang.reflect.Type type = new TypeToken<Hashtable<String, Set<String>>>() {
        }.getType();
        Hashtable<String, Set<String>> markedQues = new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_MARKED_QUESTIONS, new Gson().toJson(new Hashtable<String, Set<String>>())), type);
        return markedQues;
    }

    //    <quiz_id,<topic_id,question_id>>
    public static ArrayList<String> getMarkedQuestions(String quizId, String topicId) {
        java.lang.reflect.Type type = new TypeToken<Hashtable<String, Set<String>>>() {
        }.getType();
        Hashtable<String, Set<String>> markedQues = new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_MARKED_QUESTIONS, new Gson().toJson(new Hashtable<String, Set<String>>())), type);
        return (ArrayList<String>) markedQues.get(quizId + DELIMITER + topicId);
    }

}
