package com.example.testproject.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "answers", primaryKeys = {"question_id", "quiz_id", "topic_id", "answer_id"})
public class AnswerDetailsSchema implements Serializable {

    @SerializedName("answer_id")
    @ColumnInfo(name = "answer_id")
    @NonNull
    private String answerId;

    @ColumnInfo(name = "question_id")
    @SerializedName("question_id")
    @NonNull
    private String questionId;

    @SerializedName("answer")
    private String answer;

    //For local purpose
    @ColumnInfo(name = "is_selected")
    @SerializedName("is_selected")
    private boolean isSelected;

    @ColumnInfo(name = "topic_id")
    @SerializedName("topic_id")
    @NonNull
    private String topicId;

    @ColumnInfo(name = "quiz_id")
    @SerializedName("quiz_id")
    @NonNull
    private String quizId;
    //For local purpose


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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
