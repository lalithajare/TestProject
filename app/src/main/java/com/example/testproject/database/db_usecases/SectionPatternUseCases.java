package com.example.testproject.database.db_usecases;

import com.example.testproject.Model.TestQuizSetGet;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.SectionPattern;
import com.example.testproject.database.operators.DBOperationsHelper;
import com.example.testproject.database.operators.QuizDBOperator;
import com.example.testproject.database.operators.SectionPatternDBOperator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SectionPatternUseCases {
    private SectionPatternDBOperator mOperator;
    private Gson mGson;
    private DBOperationsHelper mHelper;

    public SectionPatternUseCases(DBOperationsHelper helper) {
        mGson = new Gson();
        mHelper = helper;
    }

    public List<Quiz> getQuizesFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, new TypeToken<List<Quiz>>() {
        }.getType());
    }


    public TestQuizSetGet getConvertedQuizUsecase(Quiz quiz) {
        TestQuizSetGet testQuizSetGet = new TestQuizSetGet();

        testQuizSetGet.setTest_quiz_id(quiz.getQuizId());
        testQuizSetGet.setTest_quiz_name(quiz.getQuizName());
        testQuizSetGet.setTest_no_of_qs(quiz.getNoOfQs());
        testQuizSetGet.setTest_time(quiz.getTime());
        testQuizSetGet.setTest_total_marks(String.valueOf(quiz.getTotalMarks()));

        testQuizSetGet.setTest_changable(quiz.getChangable());
        testQuizSetGet.setTest_status(String.valueOf(quiz.getStatus()));
        testQuizSetGet.setTest_direction_status(quiz.getDirectionStatus());
        testQuizSetGet.setTest_checked_attended(String.valueOf(quiz.getCheckedAttended()));

        testQuizSetGet.setStudent_test_taken_id(String.valueOf(quiz.getStudentTestTakenId()));
        testQuizSetGet.setRemaining_time(String.valueOf(quiz.getRemainingTime()));
        testQuizSetGet.setStudent_buy_plan_status(String.valueOf(quiz.getStudentBuyPlanStatus()));

        return testQuizSetGet;
    }

    public void addSectionPatternList(List<SectionPattern> sectionPatternList) {
        mOperator = new SectionPatternDBOperator(new DBOperationsHelper() {
            @Override
            public void onListInserted(Long[] result) {
                super.onListInserted(result);
                mHelper.onListInserted(result);
            }
        });
        mOperator.addAllSectionPatterns(sectionPatternList);
    }

    public void getAllSectionPatternList() {
        mOperator = new SectionPatternDBOperator(mHelper);
//        mOperator.searchForQuizList();
    }

    public void getQuizListByCategoryId(String catId) {
//        mOperator = new QuizDBOperator(mHelper);
//        mOperator.searchForQuizListByCategoryId(catId);
    }
}
