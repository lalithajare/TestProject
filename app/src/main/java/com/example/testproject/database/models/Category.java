package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_category_id")
    private int localCategoryId;

    @ColumnInfo(name = "category_id")
    @SerializedName("category_id")
    private String categoryId;


    @ColumnInfo(name = "course_id")
    @SerializedName("course_id")
    private String courseId;

    @ColumnInfo(name = "section_id")
    @SerializedName("section_id")
    private String sectionId;

    @ColumnInfo(name = "category_name")
    @SerializedName("category_name")
    private String categoryName;

    @ColumnInfo(name = "total_quiz")
    @SerializedName("total_quiz")
    private int totalQuiz;

    @ColumnInfo(name = "full_length_test")
    @SerializedName("full_length_test")
    private int fullLengthTest;

    @ColumnInfo(name = "section_test")
    @SerializedName("section_test")
    private int sectionTest;

    @ColumnInfo(name = "previous_year_test")
    @SerializedName("previous_year_test")
    private int previousYearTest;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(int totalQuiz) {
        this.totalQuiz = totalQuiz;
    }

    public int getFullLengthTest() {
        return fullLengthTest;
    }

    public void setFullLengthTest(int fullLengthTest) {
        this.fullLengthTest = fullLengthTest;
    }

    public int getSectionTest() {
        return sectionTest;
    }

    public void setSectionTest(int sectionTest) {
        this.sectionTest = sectionTest;
    }

    public int getPreviousYearTest() {
        return previousYearTest;
    }

    public void setPreviousYearTest(int previousYearTest) {
        this.previousYearTest = previousYearTest;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getLocalCategoryId() {
        return localCategoryId;
    }

    public void setLocalCategoryId(int localCategoryId) {
        this.localCategoryId = localCategoryId;
    }
}
