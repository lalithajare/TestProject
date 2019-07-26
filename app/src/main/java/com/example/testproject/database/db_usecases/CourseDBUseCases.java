package com.example.testproject.database.db_usecases;

import com.example.testproject.Model.CoursesSetterGetter;
import com.example.testproject.database.models.Course;
import com.example.testproject.database.operators.CourseDBOperator;
import com.example.testproject.database.operators.DBOperationsHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CourseDBUseCases {

    private CourseDBOperator mOperator;
    private Gson mGson;
    private DBOperationsHelper mHelper;

    public CourseDBUseCases(DBOperationsHelper helper) {
        mGson = new Gson();
        mHelper = helper;
    }

    private Course getCourseFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, Course.class);
    }

    private List<Course> getCoursesFromString(String jsonObject) {
        return mGson.fromJson(jsonObject, new TypeToken<List<Course>>() {
        }.getType());
    }

    public void getConvertedCourseListUsecase() {
        mOperator = new CourseDBOperator(new DBOperationsHelper() {
            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);
                ArrayList<CoursesSetterGetter> coursesSetterGetterArrayList = new ArrayList<>();
                for (T item : list) {
                    Course course = (Course) item;
                    CoursesSetterGetter coursesSetterGetter = new CoursesSetterGetter();
                    coursesSetterGetter.setCourse_id(course.getCourseId());
                    coursesSetterGetter.setCourse_name(course.getCourseName());
                    coursesSetterGetterArrayList.add(coursesSetterGetter);
                }
                mHelper.onListConvertedToLocalType(coursesSetterGetterArrayList);
            }
        });
        mOperator.searchForCourseList();
    }

    public void getAllCoursesUsecase() {
        mOperator = new CourseDBOperator(mHelper);
        mOperator.searchForCourseList();
    }

    public void deleteLocalCourseData() {
        mOperator.deleteTableData();
    }

    public void updateCourse(Course course) {
        mOperator.updateCourse(course);
    }

    public void checkIfCoursesExistAndAdd(String jsonObject) {
        final List<Course> courseListToAdd = getCoursesFromString(jsonObject);
        mOperator = new CourseDBOperator(new DBOperationsHelper() {

            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);
                if (list.size() == 0) {
                    mOperator.addAllCourses(courseListToAdd);
                }
            }

            @Override
            public void onListInserted(Long[] result) {
                super.onListInserted(result);
                mHelper.onListInserted(result);
            }

        });
        mOperator.searchForCourseList();
    }

}
