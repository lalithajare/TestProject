package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * This ENTITY is linked with the 'Question'
 * This ENTITY is actually an 'Option' of Question
 * 'question_id' in the 'question_id' in 'qUESTION' ENTITY
 */
@Entity(tableName = "answer_details")
public class Answer {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_answer_id")
    private int localAnswerId;

    @ColumnInfo(name = "question_id")
    @SerializedName("question_id")
    private String questionId;
    @ColumnInfo(name = "answer_id")
    @SerializedName("answer_id")
    private String answerId;
    @ColumnInfo(name = "answer")
    @SerializedName("answer")
    private String answer;

    public int getLocalAnswerId() {
        return localAnswerId;
    }

    public void setLocalAnswerId(int localAnswerId) {
        this.localAnswerId = localAnswerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
