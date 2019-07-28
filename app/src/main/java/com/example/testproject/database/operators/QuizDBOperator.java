package com.example.testproject.database.operators;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.testproject.common.MyApplication;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Quiz;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class QuizDBOperator {
    private DBOperationsHelper mHelper;

    public QuizDBOperator(DBOperationsHelper helper) {
        mHelper = helper;
    }

    @SuppressLint("StaticFieldLeak")
    public void addAllQuizes(List<Quiz> quizList) {
        new AsyncTask<List<Quiz>, Void, Long[]>() {
            @Override
            protected Long[] doInBackground(List<Quiz>... lists) {
                Long[] result = MyApplication.getAppInstance().getDbInstance().getQuizDAO().insertQuizes(lists[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Long[] result) {
                super.onPostExecute(result);
                mHelper.onListInserted(result);
            }
        }.execute(quizList);
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteTableData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyApplication.getAppInstance().getDbInstance().getQuizDAO().nukeTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mHelper.onTableDeleted();
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void searchForQuiz(String categoryId) {
        new AsyncTask<String, Void, Quiz>() {
            @Override
            protected Quiz doInBackground(String... ids) {
                Quiz result = MyApplication.getAppInstance().getDbInstance().getQuizDAO().getQuizById(ids[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Quiz result) {
                super.onPostExecute(result);
                mHelper.onItemSearched(result);
            }
        }.execute(categoryId);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchForQuizListByCategoryId(String catId) {
        new AsyncTask<String, Void, List<Quiz>>() {
            @Override
            protected List<Quiz> doInBackground(String... ids) {
                List<Quiz> result = MyApplication.getAppInstance().getDbInstance().getQuizDAO().getQuizListByCategoryId(ids[0]);
                return result;
            }

            @Override
            protected void onPostExecute(List<Quiz> result) {
                super.onPostExecute(result);
                mHelper.onItemListSearched(result);
            }
        }.execute(catId);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchForQuizList() {
        new AsyncTask<Void, Void, ArrayList<Quiz>>() {
            @Override
            protected ArrayList<Quiz> doInBackground(Void... voids) {
                ArrayList<Quiz> result = (ArrayList<Quiz>) MyApplication.getAppInstance()
                        .getDbInstance().getQuizDAO().getQuizList();
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Quiz> result) {
                super.onPostExecute(result);
                mHelper.onItemListSearched(result);
            }
        }.execute();
    }
}
