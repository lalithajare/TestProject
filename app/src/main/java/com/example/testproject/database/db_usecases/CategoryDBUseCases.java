package com.example.testproject.database.db_usecases;

import com.example.testproject.Model.QuizTopic;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Course;
import com.example.testproject.database.operators.CategoryDBOperator;
import com.example.testproject.database.operators.CourseDBOperator;
import com.example.testproject.database.operators.DBOperationsHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CategoryDBUseCases {
    private CategoryDBOperator mOperator;
    private Gson mGson;
    private DBOperationsHelper mHelper;

    public CategoryDBUseCases(DBOperationsHelper helper) {
        mGson = new Gson();
        mHelper = helper;
    }

    private Category getCourseFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, Category.class);
    }

    private List<Category> getCoursesFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, new TypeToken<List<Category>>() {
        }.getType());
    }

    public void getConvertedCourseList() {
        mOperator = new CategoryDBOperator(new DBOperationsHelper() {
            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);
                ArrayList<QuizTopic> quizTopicArrayList = new ArrayList<>();
                for (T item : list) {
                    Category category = (Category) item;
                    QuizTopic quizTopic = new QuizTopic(category.getCategoryId(), category.getCategoryName(),
                            String.valueOf(category.getTotalQuiz()), category.getSectionId(),
                            String.valueOf(category.getFullLengthTest()), String.valueOf(category.getSectionTest()),
                            String.valueOf(category.getPreviousYearTest()));
                    quizTopicArrayList.add(quizTopic);
                }
                mHelper.onListConvertedToLocalType(quizTopicArrayList);
            }
        });
        mOperator.searchForCategoryList();
    }

    
}
