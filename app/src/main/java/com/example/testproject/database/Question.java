package com.example.testproject.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(tableName = "questions")
public class Question {

    @ColumnInfo(name = "question_id")
    private String questionId;

    @ColumnInfo(name = "quiz_id")
    private String quizId;

    @ColumnInfo(name = "topic_id")
    private String topicId;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "is_marked")
    private Boolean isMarked;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getMarked() {
        return isMarked;
    }

    public void setMarked(Boolean marked) {
        isMarked = marked;
    }
}
