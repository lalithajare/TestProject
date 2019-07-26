package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * This entity is linked with 'student_test_id'
 * This entity describes about noOfquestions,type of Quiz, etc
 */
@Entity(tableName = "quiz_details")
public class QuizDetails {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_quiz_detail_id")
    private int localQuizDetailId;

    @ColumnInfo(name = "quiz_id")
    @SerializedName("quiz_id")
    private String quizId;

    @ColumnInfo(name = "student_test_id")
    @SerializedName("student_test_id")
    private String studentTestId;

    @ColumnInfo(name = "type_id")
    @SerializedName("type_id")
    private String typeId;

    @ColumnInfo(name = "type_name")
    @SerializedName("type_name")
    private String typeName;

    @ColumnInfo(name = "duration")
    @SerializedName("duration")
    private String duration;

    @ColumnInfo(name = "total_question")
    @SerializedName("total_question")
    private String totalQuestion;

    @ColumnInfo(name = "count")
    @SerializedName("count")
    private int count;

    public String getStudentTestId() {
        return studentTestId;
    }

    public void setStudentTestId(String studentTestId) {
        this.studentTestId = studentTestId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLocalQuizDetailId() {
        return localQuizDetailId;
    }

    public void setLocalQuizDetailId(int localQuizDetailId) {
        this.localQuizDetailId = localQuizDetailId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
