package com.example.testproject.Utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.Model.QuizAllQuestionTopicWiseResponseSchema;
import com.example.testproject.Model.TopicResponseSchema;
import com.example.testproject.common.ApiCallManager;
import com.example.testproject.common.MyApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Hashtable;

public class QuizQuestionsDispatcher {

    private QuizAllQuestionTopicWiseResponseSchema mQuizAllQuestionTopicWiseResponseSchema;
    private final String quizId;
    private final Boolean wasPaused;
    private QuestionsLoadedListener mListener;
    private AsyncTask<String, Void, Void> loadQuestionsTask = new AsyncTask<String, Void, Void>() {
        @Override
        protected Void doInBackground(String... strings) {
            mQuizAllQuestionTopicWiseResponseSchema = AppPreferenceManager.getQuizQuestions(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mListener.OnQuestionsLoaded();
        }
    };


    public QuizQuestionsDispatcher(String quizId, Boolean wasPaused, QuestionsLoadedListener listener) {
        this.quizId = quizId;
        this.wasPaused = wasPaused;
        mListener = listener;
        loadAllQuestions(quizId);
    }

    private void loadAllQuestions(final String quizId) {
        if (wasPaused) {
            //Load from local storage
            loadQuestionsTask.execute(quizId);
        } else {
            //Call API
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
    }

    public QuizAllQuestionTopicWiseResponseSchema getmQuizAllQuestionTopicWiseResponseSchema() {
        return mQuizAllQuestionTopicWiseResponseSchema;
    }

    public interface QuestionsLoadedListener {
        void OnQuestionsLoaded();

        void OnQuestionsFailed(VolleyError error);
    }


}
