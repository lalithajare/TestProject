package com.example.testproject.Utils;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.example.testproject.Model.QuizAllQuestionTopicWiseResponseSchema;
import com.example.testproject.Model.TopicResponseSchema;
import com.example.testproject.common.ApiCallManager;
import com.example.testproject.common.MyApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Hashtable;

public class QuizQuestionsDispatcher {

    private QuizAllQuestionTopicWiseResponseSchema mQuizAllQuestionTopicWiseResponseSchema;
    private QuestionsLoadedListener mListener;

    public QuizQuestionsDispatcher(String quizId, QuestionsLoadedListener listener) {
        mListener = listener;
        loadAllQuestions(quizId);
    }

    private void loadAllQuestions(String quizId) {
        ApiCallManager.getInstance(MyApplication.getAppInstance()).callTopicWiseQuestionsAPI(quizId, new ApiCallManager.ApiResponseListener() {
            @Override
            public void onSuccess(String response) {
                mQuizAllQuestionTopicWiseResponseSchema = new Gson().fromJson(response, QuizAllQuestionTopicWiseResponseSchema.class);
                mListener.OnQuestionsLoaded();
            }

            @Override
            public void onError(VolleyError error) {
                mListener.OnQuestionsFailed(error);
            }
        });

    }

    public QuizAllQuestionTopicWiseResponseSchema getmQuizAllQuestionTopicWiseResponseSchema() {
        return mQuizAllQuestionTopicWiseResponseSchema;
    }

    public interface QuestionsLoadedListener {
        void OnQuestionsLoaded();

        void OnQuestionsFailed(VolleyError error);
    }


}
