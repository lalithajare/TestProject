package com.example.testproject.Utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.example.testproject.Model.AnswerDetailsSchema;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.Model.QuizAllQuestionTopicWiseResponseSchema;
import com.example.testproject.Model.TopicResponseSchema;
import com.example.testproject.common.ApiCallManager;
import com.example.testproject.common.MyApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class QuizQuestionsDispatcher {

    //Contains all questions and options topicwise
    private QuizAllQuestionTopicWiseResponseSchema mQuizAllQuestionTopicWiseResponseSchema;

    //Contains all chosen options
    private HashMap<String, ArrayList<String>> mChosenOptionsForQuiz;

    private final String quizId;
    private final Boolean wasPaused;
    private QuestionsLoadedListener mListener;

    private AsyncTask<String, Void, Void> loadQuestionsTask = new AsyncTask<String, Void, Void>() {
        @Override
        protected Void doInBackground(String... strings) {
            mQuizAllQuestionTopicWiseResponseSchema = AppPreferenceManager.getQuizQuestions(strings[0]);
            mChosenOptionsForQuiz = AppPreferenceManager.getAllChosenOptionForQuiz(quizId);
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

    public Integer getChosenOptionForQuestion(String topicId, String questionId) {
        ArrayList<String> questAnsForTopic = mChosenOptionsForQuiz.get(topicId);
        for (String questionAnswer : questAnsForTopic) {
            if (TextUtils.equals(questionAnswer.split(AppPreferenceManager.DELIMITER)[0], questionId)) {
                return Integer.parseInt(questionAnswer.split(AppPreferenceManager.DELIMITER)[1]);
            }
        }
        return null;
    }

//    public void initializeChosenAnswers(String topicId) {
//        ArrayList<String> questAnsForTopic = mChosenOptionsForQuiz.get(topicId);
//        TopicResponseSchema curentTopicResponseSchema;
//        ArrayList<QuestionDetailsResponseSchema> topicQuestions = null;
//
//        for (TopicResponseSchema topicResponseSchema : mQuizAllQuestionTopicWiseResponseSchema.getTopicResonseSchema()) {
//            if (TextUtils.equals(topicResponseSchema.getSectionId(), topicId)) {
//                curentTopicResponseSchema = topicResponseSchema;
//                topicQuestions = curentTopicResponseSchema.getQuestionArrayList();
//                break;
//            }
//        }
//
//        if (questAnsForTopic != null && !questAnsForTopic.isEmpty()) {
//            for (String questionAnswer : questAnsForTopic) {
//                for (QuestionDetailsResponseSchema questionDetailsResponseSchema : topicQuestions) {
//                    for (int i = 0; i < questionDetailsResponseSchema.getAnswerDetails().size(); i++) {
//                        AnswerDetailsSchema answerDetailsSchema = questionDetailsResponseSchema.getAnswerDetails().get(i);
//                        if (TextUtils.equals(answerDetailsSchema.getAnswerId(), questionAnswer.split(AppPreferenceManager.DELIMITER)[1])) {
//                            //Initialize chose options
//                            Const.answerStoreHash.put(questionDetailsResponseSchema.getQuestionId(), answerDetailsSchema.getAnswer());
//                            Const.answerCheckHash.put(questionDetailsResponseSchema.getQuestionId(), String.valueOf(i));
//                            Const.answerQuestionStoreHash.put(questionDetailsResponseSchema.getQuestion(), answerDetailsSchema.getAnswer());
//                        }
//                    }
//                }
//            }
//        }
//    }

    public interface QuestionsLoadedListener {
        void OnQuestionsLoaded();

        void OnQuestionsFailed(VolleyError error);
    }


}
