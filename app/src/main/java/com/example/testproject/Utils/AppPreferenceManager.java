package com.example.testproject.Utils;

import android.text.TextUtils;

import com.example.testproject.Model.QuizAllQuestionTopicWiseResponseSchema;
import com.example.testproject.common.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class AppPreferenceManager {

    public static final String DELIMITER = "~";

    private static final String SAVED_TOPIC_STATES = "quizes_ids_and_times_page_index";
    private static final String SAVED_TOPICS_OFFLINE_ATTEMPT_STATES = "quizes_offline_attempts";
    private static final String SAVED_QUIZ_QUESTIONS = "quiz_questions";
    private static final String MAP_QUIZ_TIME = "quiz_time";
    private static final String SAVED_CHOSEN_ANSWERS = "chosen_answers";

    // *************************************** QUIZ - GENERAL STATE **************************************************

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

    // *************************************** QUIZ - GENERAL STATE **************************************************


    // *************************************** QUIZ - DELETE RECORDS **************************************************


    public static void deleteQuizState(String quizId) {

        //Delete quiz states
        Set<String> savedQuizStates = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(SAVED_TOPIC_STATES, new HashSet<String>());
        for (String state : savedQuizStates) {
            if (TextUtils.equals(state.split(DELIMITER)[0], quizId)) {
                savedQuizStates.remove(state);
                MyApplication.getAppInstance()
                        .getSharedPreferences()
                        .edit().putStringSet(SAVED_TOPIC_STATES, savedQuizStates).apply();
                break;
            }
        }

        //Delete offline attempts
        HashMap<String, ArrayList<Hashtable<String, String>>> attempts = getAllTestsAndOfflineAttemptStates();
        attempts.remove(quizId);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_TOPICS_OFFLINE_ATTEMPT_STATES, new Gson().toJson(attempts)).apply();

        //Delete Paused Times
        Set<String> savedQuizTimes = MyApplication.getAppInstance()
                .getSharedPreferences()
                .getStringSet(MAP_QUIZ_TIME, new HashSet<String>());
        for (String quizTime : savedQuizTimes) {
            if (TextUtils.equals(quizTime.split(DELIMITER)[0], quizId)) {
                savedQuizTimes.remove(quizTime);
            }
        }
        MyApplication.getAppInstance()
                .getSharedPreferences()
                .edit().putStringSet(MAP_QUIZ_TIME, savedQuizTimes).apply();

        //Delete Quiz Questions
        getAllSavedQuizesQuestions().remove(quizId);

        //Delete Quiz saved answers
        java.lang.reflect.Type type = new TypeToken<HashMap<String, HashMap<String, ArrayList<String>>>>() {
        }.getType();
        HashMap<String, HashMap<String, ArrayList<String>>> quizAnswers = new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_CHOSEN_ANSWERS, new Gson().toJson(new HashMap<String, HashMap<String, ArrayList<String>>>())), type);
        quizAnswers.remove(quizId);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_CHOSEN_ANSWERS
                , new Gson().toJson(quizAnswers)).apply();
    }
    // *************************************** QUIZ - DELETE RECORDS **************************************************


    // *************************************** QUIZ - OFFLINE ATTEMPTS **************************************************

    public static void saveOfflineAttemptStates(String quizId, ArrayList<Hashtable<String, String>> offlineAttempts) {
        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();
        attemptStates.put(quizId, offlineAttempts);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_TOPICS_OFFLINE_ATTEMPT_STATES, new Gson().toJson(attemptStates)).apply();
    }

    public static HashMap<String, ArrayList<Hashtable<String, String>>> getAllTestsAndOfflineAttemptStates() {
        java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Hashtable<String, String>>>>() {
        }.getType();
        return new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_TOPICS_OFFLINE_ATTEMPT_STATES, new Gson().toJson(new HashMap<String, ArrayList<Hashtable<String, String>>>())), type);
    }

    public static ArrayList<Hashtable<String, String>> getOfflineAttemptState(String quizId) {
        HashMap<String, ArrayList<Hashtable<String, String>>> attemptStates = getAllTestsAndOfflineAttemptStates();
        if (attemptStates != null)
            return attemptStates.get(quizId);
        return null;
    }

    // *************************************** QUIZ - OFFLINE ATTEMPTS **************************************************


    // *************************************** QUIZ - QUESTIONS **************************************************

    //quiz_id, QuizAllQuestionTopicWiseResponseSchema
    public static HashMap<String, QuizAllQuestionTopicWiseResponseSchema> getAllSavedQuizesQuestions() {
        java.lang.reflect.Type type = new TypeToken<HashMap<String, QuizAllQuestionTopicWiseResponseSchema>>() {
        }.getType();
        return new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_QUIZ_QUESTIONS, new Gson().toJson(new HashMap<String, QuizAllQuestionTopicWiseResponseSchema>())), type);
    }

    public static QuizAllQuestionTopicWiseResponseSchema getQuizQuestions(String quizId) {
        return getAllSavedQuizesQuestions().get(quizId);
    }

    public static void saveQuizQuestions(String quizId, QuizAllQuestionTopicWiseResponseSchema quizAllQuestionTopicWiseResponseSchema) {
        HashMap<String, QuizAllQuestionTopicWiseResponseSchema> savedQuizes = getAllSavedQuizesQuestions();
        savedQuizes.remove(quizId);
        savedQuizes.put(quizId, quizAllQuestionTopicWiseResponseSchema);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_QUIZ_QUESTIONS, new Gson().toJson(savedQuizes)).apply();
    }
    // *************************************** QUIZ - QUESTIONS **************************************************


    // ***************************************  CHOSEN OPTIONS **************************************************

    public static void addAnswer(String quizId, String topicId, String questAns) {
        java.lang.reflect.Type type = new TypeToken<HashMap<String, HashMap<String, ArrayList<String>>>>() {
        }.getType();
        HashMap<String, HashMap<String, ArrayList<String>>> quizAnswers = new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_CHOSEN_ANSWERS, new Gson().toJson(new HashMap<String, HashMap<String, ArrayList<String>>>())), type);
        HashMap<String, ArrayList<String>> topicAnswers = quizAnswers.get(quizId);
        ArrayList<String> questAnsList;
        if (topicAnswers != null && topicAnswers.size() > 0) {
            if (topicAnswers.get(topicId) != null && topicAnswers.get(topicId).size() > 0) {
                questAnsList = topicAnswers.get(topicId);
            } else {
                questAnsList = new ArrayList<>();
            }
        } else {
            topicAnswers = new HashMap();
            questAnsList = new ArrayList<>();
        }
        questAnsList.add(questAns);
        topicAnswers.put(topicId, questAnsList);
        quizAnswers.put(quizId, topicAnswers);
        MyApplication.getAppInstance().getSharedPreferences().edit().putString(SAVED_CHOSEN_ANSWERS
                , new Gson().toJson(quizAnswers)).apply();
    }

    //ArrayList<String> ==> String -> question_id~optionIndex
    public static HashMap<String, ArrayList<String>> getAllChosenOptionForQuiz(String quizId) {
        java.lang.reflect.Type type = new TypeToken<HashMap<String, HashMap<String, ArrayList<String>>>>() {
        }.getType();
        HashMap<String, HashMap<String, ArrayList<String>>> answers = new Gson().fromJson(MyApplication.getAppInstance()
                .getSharedPreferences()
                .getString(SAVED_CHOSEN_ANSWERS, new Gson().toJson(new HashMap<String, HashMap<String, ArrayList<String>>>())), type);
        return answers.get(quizId);
    }

    // ***************************************  CHOSEN OPTIONS **************************************************

}
