package com.example.testproject.database.db_usecases;

import com.example.testproject.Model.QuizTopic;
import com.example.testproject.Model.TestQuizSetGet;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.operators.CategoryDBOperator;
import com.example.testproject.database.operators.CourseDBOperator;
import com.example.testproject.database.operators.DBOperationsHelper;
import com.example.testproject.database.operators.QuizDBOperator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class QuizUseCases {

    private QuizDBOperator mOperator;
    private Gson mGson;
    private DBOperationsHelper mHelper;

    public QuizUseCases(DBOperationsHelper helper) {
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

    public void addQuizList(List<Quiz> quizListToAdd) {
        mOperator = new QuizDBOperator(new DBOperationsHelper() {
            @Override
            public void onListInserted(Long[] result) {
                super.onListInserted(result);
                mHelper.onListInserted(result);
            }

        });
        mOperator.addAllQuizes(quizListToAdd);
    }

    public void getAllQuizList() {
        mOperator = new QuizDBOperator(mHelper);
        mOperator.searchForQuizList();
    }

    public void getQuizListByCategoryId(String catId) {
        mOperator = new QuizDBOperator(mHelper);
        mOperator.searchForQuizListByCategoryId(catId);
    }
}
