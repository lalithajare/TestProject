package com.example.testproject.database.db_usecases;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.FullTestTopicAdapter;
import com.example.testproject.Model.QuizTopic;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.common.MyApplication;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Course;
import com.example.testproject.database.operators.CategoryDBOperator;
import com.example.testproject.database.operators.CourseDBOperator;
import com.example.testproject.database.operators.DBOperationsHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CategoryDBUseCases {
    private CategoryDBOperator mOperator;
    private Gson mGson;
    private DBOperationsHelper mHelper;

    public CategoryDBUseCases(DBOperationsHelper helper) {
        mGson = new Gson();
        mHelper = helper;
    }

    public Category getCategoryFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, Category.class);
    }

    public List<Category> getCategoriesFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, new TypeToken<List<Category>>() {
        }.getType());
    }

    public QuizTopic getConvertedCategoryUsecase(Category category) {
        QuizTopic quizTopic = new QuizTopic(category.getCategoryId(), category.getCategoryName(),
                String.valueOf(category.getTotalQuiz()), category.getSectionId(),
                String.valueOf(category.getFullLengthTest()), String.valueOf(category.getSectionTest()),
                String.valueOf(category.getPreviousYearTest()));
        return quizTopic;
    }

    public void getAllCategoryListUsecase() {
        mOperator = new CategoryDBOperator(mHelper);
        mOperator.searchForCategoryList();
    }

    public void checkIfCategoriesExistAndAdd(List<Category> categoryListToAdd,String courseId) {
        for(Category category : categoryListToAdd){
            category.setCourseId(courseId);
        }

        mOperator = new CategoryDBOperator(new DBOperationsHelper() {
            @Override
            public void onListInserted(Long[] result) {
                super.onListInserted(result);
                mHelper.onListInserted(result);
            }

        });
        mOperator.addAllCategories(categoryListToAdd);
    }

    public void getCategoryListByCourseIdUsecase(String courseId) {
        mOperator = new CategoryDBOperator(mHelper);
        mOperator.searchForCategoryListByCourseId(courseId);
    }
}
