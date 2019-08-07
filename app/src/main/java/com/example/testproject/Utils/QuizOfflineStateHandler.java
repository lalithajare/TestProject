package com.example.testproject.Utils;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.android.volley.VolleyError;
import com.example.testproject.Model.AnswerDetailsSchema;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.Model.QuizAllQuestionTopicWiseResponseSchema;
import com.example.testproject.Model.TopicResponseSchema;
import com.example.testproject.common.ApiCallManager;
import com.example.testproject.common.MyApplication;
import com.example.testproject.database.AnswerDAO;
import com.example.testproject.database.QuestionDAO;
import com.example.testproject.database.TopicDAO;
import com.google.gson.Gson;

import java.util.ArrayList;

public class QuizOfflineStateHandler {

    private String mQuizId;
    //Contains all questions and options topicwise
    private QuizAllQuestionTopicWiseResponseSchema mQuizAllQuestionTopicWiseResponseSchema;
    //Contains all chosen options
    private Boolean mWasPaused;
    private QuestionsLoadedListener mListener;

    //DATABASE
    private AnswerDAO mAnswerDAO;
    private QuestionDAO mQuestionDAO;
    private TopicDAO mTopicDAO;

    @SuppressLint("StaticFieldLeak")
    private AsyncTask<String, Void, Void> loadQuestionsTask = new AsyncTask<String, Void, Void>() {
        @Override
        protected Void doInBackground(String... strings) {
            //Set Topic List
            ArrayList<TopicResponseSchema> topicList = (ArrayList<TopicResponseSchema>) mTopicDAO.getTopics(mQuizId);
            for (TopicResponseSchema topicResponseSchema : topicList) {

                ArrayList<QuestionDetailsResponseSchema> questionDetailsResponseSchemas
                        = (ArrayList<QuestionDetailsResponseSchema>) mQuestionDAO.getQuestions(mQuizId, topicResponseSchema.getSectionId());

                //Set Question List
                for (QuestionDetailsResponseSchema questionDetailsResponseSchema : questionDetailsResponseSchemas) {
                    ArrayList<AnswerDetailsSchema> answerDetailsSchemas =
                            (ArrayList<AnswerDetailsSchema>) mAnswerDAO.getOptionsForQuestion(questionDetailsResponseSchema.getQuestionId(), topicResponseSchema.getSectionId(), mQuizId);

                    //Set Answer List
                    questionDetailsResponseSchema.setAnswerDetails(answerDetailsSchemas);
                }

                topicResponseSchema.setQuestionArrayList(questionDetailsResponseSchemas);
            }
            mQuizAllQuestionTopicWiseResponseSchema = new QuizAllQuestionTopicWiseResponseSchema();
            mQuizAllQuestionTopicWiseResponseSchema.setTopicResonseSchema(topicList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mListener.OnQuestionsLoaded();
        }
    };

    public QuizOfflineStateHandler(String quizId, Boolean wasPaused, QuestionsLoadedListener listener) {
        mQuizId = quizId;
        mWasPaused = wasPaused;
        mListener = listener;
        mAnswerDAO = MyApplication.getAppInstance().getDbInstance().getOptionDAO();
        mQuestionDAO = MyApplication.getAppInstance().getDbInstance().getQuestionDAO();
        mTopicDAO = MyApplication.getAppInstance().getDbInstance().getTopicDAO();
        loadAllQuestions(quizId);
    }

    private void loadAllQuestions(final String quizId) {
        if (mWasPaused) {
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

    @SuppressLint("StaticFieldLeak")
    public void saveQuiz(final QuizSaveListener listener) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (TopicResponseSchema topicResponseSchema : mQuizAllQuestionTopicWiseResponseSchema.getTopicResonseSchema()) {
                    topicResponseSchema.setQuizId(mQuizId);
                    for (QuestionDetailsResponseSchema questionDetailsResponseSchema : topicResponseSchema.getQuestionArrayList()) {
                        questionDetailsResponseSchema.setTopicId(topicResponseSchema.getSectionId());
                        questionDetailsResponseSchema.setQuizId(mQuizId);

                        for (AnswerDetailsSchema answerDetailsSchema : questionDetailsResponseSchema.getAnswerDetails()) {
                            answerDetailsSchema.setQuestionId(questionDetailsResponseSchema.getQuestionId());
                            answerDetailsSchema.setTopicId(topicResponseSchema.getSectionId());
                            answerDetailsSchema.setQuizId(mQuizId);
                        }

                        mAnswerDAO.insertAnswers(questionDetailsResponseSchema.getAnswerDetails());
                    }
                    mQuestionDAO.insertQuestions(topicResponseSchema.getQuestionArrayList());
                }
                mTopicDAO.insertTopics(mQuizAllQuestionTopicWiseResponseSchema.getTopicResonseSchema());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listener.onQuizSaved();
            }
        }.execute();

    }

    public void deleteQuizFromDB() {
        mQuestionDAO.deleteQuizRecord(mQuizId);
        mAnswerDAO.deleteQuizRecord(mQuizId);
        mTopicDAO.deleteQuizRecord(mQuizId);
    }

    public interface QuestionsLoadedListener {
        void OnQuestionsLoaded();

        void OnQuestionsFailed(VolleyError error);
    }

    public interface QuizSaveListener {
        void onQuizSaved();
    }

}
