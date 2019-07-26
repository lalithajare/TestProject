package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "quiz")
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_quiz_id")
    private int localQuizId;

    @ColumnInfo(name = "category_id")
    @SerializedName("category_id")
    private String categoryId;

    @ColumnInfo(name = "quiz_id")
    @SerializedName("quiz_id")
    private String quizId;

    @ColumnInfo(name = "quiz_name")
    @SerializedName("quiz_name")
    private String quizName;

    @ColumnInfo(name = "no_of_qs")
    @SerializedName("no_of_qs")
    private String noOfQs;

    @ColumnInfo(name = "time")
    @SerializedName("time")
    private String time;

    @ColumnInfo(name = "total_marks")
    @SerializedName("total_marks")
    private int totalMarks;

    @ColumnInfo(name = "changable")
    @SerializedName("changable")
    private String changable;

    @ColumnInfo(name = "direction_status")
    @SerializedName("direction_status")
    private String directionStatus;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private int status;

    @ColumnInfo(name = "checked_attended")
    @SerializedName("checked_attended")
    private int checkedAttended;

    @ColumnInfo(name = "remaining_time")
    @SerializedName("remaining_time")
    private int remainingTime;

    @ColumnInfo(name = "student_test_taken_id")
    @SerializedName("student_test_taken_id")
    private int studentTestTakenId;

    @ColumnInfo(name = "student_buy_plan_status")
    @SerializedName("student_buy_plan_status")
    private int studentBuyPlanStatus;

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getNoOfQs() {
        return noOfQs;
    }

    public void setNoOfQs(String noOfQs) {
        this.noOfQs = noOfQs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getChangable() {
        return changable;
    }

    public void setChangable(String changable) {
        this.changable = changable;
    }

    public String getDirectionStatus() {
        return directionStatus;
    }

    public void setDirectionStatus(String directionStatus) {
        this.directionStatus = directionStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCheckedAttended() {
        return checkedAttended;
    }

    public void setCheckedAttended(int checkedAttended) {
        this.checkedAttended = checkedAttended;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getStudentTestTakenId() {
        return studentTestTakenId;
    }

    public void setStudentTestTakenId(int studentTestTakenId) {
        this.studentTestTakenId = studentTestTakenId;
    }

    public int getStudentBuyPlanStatus() {
        return studentBuyPlanStatus;
    }

    public void setStudentBuyPlanStatus(int studentBuyPlanStatus) {
        this.studentBuyPlanStatus = studentBuyPlanStatus;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getLocalQuizId() {
        return localQuizId;
    }

    public void setLocalQuizId(int localQuizId) {
        this.localQuizId = localQuizId;
    }
}
