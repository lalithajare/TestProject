package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "course")
public class Course implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_course_id")
    private int localCourseId;

    @ColumnInfo(name = "course_id")
    @SerializedName("course_id")
    private String courseId;

    @ColumnInfo(name = "course_name")
    @SerializedName("course_name")
    private String courseName;

    public int getLocalCourseId() {
        return localCourseId;
    }

    public void setLocalCourseId(int localCourseId) {
        this.localCourseId = localCourseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
