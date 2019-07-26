package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * This ENTITY is Linked with 'Quiz'
 * 'quiz_id' in this ENTITY is related to 'Quiz' ENTITY
 */
@Entity(tableName = "question")
public class Question {

    @ColumnInfo(name = "local_question_id")
    private int localQuestionId;

    @ColumnInfo(name = "question_id")
    @SerializedName("question_id")
    private String questionId;

    @ColumnInfo(name = "quiz_id")
    @SerializedName("quiz_id")
    private String quizId;

    @ColumnInfo(name = "directions")
    @SerializedName("directions")
    private String directions;

    @ColumnInfo(name = "question")
    @SerializedName("question")
    private String question;

    @ColumnInfo(name = "desc_stt")
    @SerializedName("desc_stt")
    private int descStt;

    @ColumnInfo(name = "qus_stt")
    @SerializedName("qus_stt")
    private int qusStt;

    public int getLocalQuestionId() {
        return localQuestionId;
    }

    public void setLocalQuestionId(int localQuestionId) {
        this.localQuestionId = localQuestionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getDescStt() {
        return descStt;
    }

    public void setDescStt(int descStt) {
        this.descStt = descStt;
    }

    public int getQusStt() {
        return qusStt;
    }

    public void setQusStt(int qusStt) {
        this.qusStt = qusStt;
    }
}
