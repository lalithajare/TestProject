package com.example.testproject.database.operators;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.testproject.common.MyApplication;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.SectionPattern;

import java.util.ArrayList;
import java.util.List;

public class SectionPatternDBOperator {
    private DBOperationsHelper mHelper;

    public SectionPatternDBOperator(DBOperationsHelper helper) {
        mHelper = helper;
    }

    @SuppressLint("StaticFieldLeak")
    public void addAllSectionPatterns(List<SectionPattern> sectionPatternList) {
        new AsyncTask<List<SectionPattern>, Void, Long[]>() {
            @SafeVarargs
            @Override
            protected final Long[] doInBackground(List<SectionPattern>... lists) {
                return MyApplication.getAppInstance().getDbInstance().getSectionPatternDAO().insertSectionPatternList(lists[0]);
            }

            @Override
            protected void onPostExecute(Long[] result) {
                super.onPostExecute(result);
                mHelper.onListInserted(result);
            }
        }.execute(sectionPatternList);
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteTableData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyApplication.getAppInstance().getDbInstance().getSectionPatternDAO().nukeTable();
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
    public void searchForSectionPattern(String localSectionPatternId) {
        new AsyncTask<String, Void, SectionPattern>() {
            @Override
            protected SectionPattern doInBackground(String... ids) {
                SectionPattern result = MyApplication.getAppInstance().getDbInstance().getSectionPatternDAO().getSectionPatternByLocalId(ids[0]);
                return result;
            }

            @Override
            protected void onPostExecute(SectionPattern result) {
                super.onPostExecute(result);
                mHelper.onItemSearched(result);
            }
        }.execute(localSectionPatternId);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchForSectionPatternByQuizId(String quizId) {
        new AsyncTask<String, Void, List<SectionPattern>>() {
            @Override
            protected List<SectionPattern> doInBackground(String... ids) {
                List<SectionPattern> result = MyApplication.getAppInstance().getDbInstance().getSectionPatternDAO().getSectionPatternByQuizId(ids[0]);
                return result;
            }

            @Override
            protected void onPostExecute(List<SectionPattern> result) {
                super.onPostExecute(result);
                mHelper.onItemListSearched(result);
            }
        }.execute(quizId);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchForSectionPatternList() {
        new AsyncTask<Void, Void, ArrayList<SectionPattern>>() {
            @Override
            protected ArrayList<SectionPattern> doInBackground(Void... voids) {
                ArrayList<SectionPattern> result = (ArrayList<SectionPattern>) MyApplication.getAppInstance()
                        .getDbInstance().getSectionPatternDAO().getSectionPatternList();
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<SectionPattern> result) {
                super.onPostExecute(result);
                mHelper.onItemListSearched(result);
            }
        }.execute();
    }

}
