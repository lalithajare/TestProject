package com.example.testproject.database.operators;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.example.testproject.Model.CoursesSetterGetter;
import com.example.testproject.common.MyApplication;
import com.example.testproject.database.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDBOperator {

    private OperationCompleteListener mCourseOpsListener;

    public CourseDBOperator(OperationCompleteListener courseOpsListener) {
        mCourseOpsListener = courseOpsListener;
    }

    @SuppressLint("StaticFieldLeak")
    public void addAllCourses(List<Course> courseList) {
        new AsyncTask<List<Course>, Void, Long[]>() {
            @Override
            protected Long[] doInBackground(List<Course>... lists) {
                Long[] result = MyApplication.getAppInstance().getDbInstance().getCourseDAO().insertCourses(lists[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Long[] result) {
                super.onPostExecute(result);
                mCourseOpsListener.onListInserted(result);
            }
        }.execute(courseList);
    }

    @SuppressLint("StaticFieldLeak")
    public void addCourse(Course course) {
        new AsyncTask<Course, Void, Long>() {
            @Override
            protected Long doInBackground(Course... course) {
                Long result = MyApplication.getAppInstance().getDbInstance().getCourseDAO().insertCourse(course[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
                mCourseOpsListener.onItemInserted(result);
            }
        }.execute(course);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteCourse(Course course) {
        new AsyncTask<Course, Void, Integer>() {
            @Override
            protected Integer doInBackground(Course... course) {
                int result = MyApplication.getAppInstance().getDbInstance().getCourseDAO().deleteCourse(course[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                mCourseOpsListener.onItemDeleted(result);
            }
        }.execute(course);
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCourse(Course course) {
        new AsyncTask<Course, Void, Integer>() {
            @Override
            protected Integer doInBackground(Course... course) {
                int result = MyApplication.getAppInstance().getDbInstance().getCourseDAO().updateCourse(course[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                mCourseOpsListener.onItemUpdated(result);
            }
        }.execute(course);
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTableData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MyApplication.getAppInstance().getDbInstance().getCourseDAO().nukeTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mCourseOpsListener.onTableDeleted();
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void searchForCourse(String courseId) {
        new AsyncTask<String, Void, Course>() {
            @Override
            protected Course doInBackground(String... ids) {
                Course result = MyApplication.getAppInstance().getDbInstance().getCourseDAO().getCourseById(ids[0]);
                return result;
            }

            @Override
            protected void onPostExecute(Course result) {
                super.onPostExecute(result);
                mCourseOpsListener.onItemSearched(result);
            }
        }.execute(courseId);
    }

    @SuppressLint("StaticFieldLeak")
    public void searchForCourseList() {
        new AsyncTask<Void, Void, ArrayList<Course>>() {
            @Override
            protected ArrayList<Course> doInBackground(Void... voids) {
                ArrayList<Course> result = (ArrayList<Course>) MyApplication.getAppInstance().getDbInstance().getCourseDAO().getCourseList();
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<Course> result) {
                super.onPostExecute(result);
                mCourseOpsListener.onItemListSearched(result);
            }
        }.execute();
    }


}
