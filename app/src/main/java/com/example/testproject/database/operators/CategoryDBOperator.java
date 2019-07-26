package com.example.testproject.database.operators;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.testproject.Model.CoursesSetterGetter;
import com.example.testproject.common.MyApplication;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CategoryDBOperator {
    private DBOperationsHelper mHelper;

    public CategoryDBOperator(DBOperationsHelper helper) {
        mHelper = helper;
    }

    @SuppressLint("StaticFieldLeak")
    public void addAllCategories(List<Category> categoryList) {
        new AsyncTask<List<Category>, Void, Long[]>() {
            @Override
            protected Long[] doInBackground(List<Category>... lists) {
                Long[] result = MyApplication.getAppInstance().getDbInstance().getCategoryDAO().insertCategories(lists[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Long[] result) {
                super.onPostExecute(result);
                mHelper.onListInserted(result);
            }
        }.execute(categoryList);
    }

    @SuppressLint("StaticFieldLeak")
    public void addCategory(Category category) {
        new AsyncTask<Category, Void, Long>() {
            @Override
            protected Long doInBackground(Category... category) {
                Long result = MyApplication.getAppInstance().getDbInstance().getCategoryDAO().insertCategory(category[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                mHelper.onItemInserted(result);
            }
        }.execute(category);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteCategory(Category category) {
        new AsyncTask<Category, Void, Integer>() {
            @Override
            protected Integer doInBackground(Category... categories) {
                int result = MyApplication.getAppInstance().getDbInstance().getCategoryDAO().deleteCategory(categories[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                mHelper.onItemDeleted(result);
            }
        }.execute(category);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCategory(Category category) {
        new AsyncTask<Category, Void, Integer>() {
            @Override
            protected Integer doInBackground(Category... category) {
                int result = MyApplication.getAppInstance().getDbInstance().getCategoryDAO().updateCategory(category[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                mHelper.onItemUpdated(result);
            }
        }.execute(category);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTableData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyApplication.getAppInstance().getDbInstance().getCategoryDAO().nukeTable();
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
    public void searchForCourse(String courseId) {
        new AsyncTask<String, Void, Category>() {
            @Override
            protected Category doInBackground(String... ids) {
                Category result = MyApplication.getAppInstance().getDbInstance().getCategoryDAO().getCategoryById(ids[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Category result) {
                super.onPostExecute(result);
                mHelper.onItemSearched(result);
            }
        }.execute(courseId);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchForCategoryList() {
        new AsyncTask<Void, Void, ArrayList<Category>>() {
            @Override
            protected ArrayList<Category> doInBackground(Void... voids) {
                ArrayList<Category> result = (ArrayList<Category>) MyApplication.getAppInstance()
                        .getDbInstance().getCategoryDAO().getCategoryList();
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Category> result) {
                super.onPostExecute(result);
                mHelper.onItemListSearched(result);
            }
        }.execute();
    }

    public abstract class CourseDBOpsHelper implements OperationCompleteListener {


    }
}
