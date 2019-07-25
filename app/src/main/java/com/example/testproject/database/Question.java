package com.example.testproject.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

@Entity(tableName = "questions")
public class Question {
    @ColumnInfo(name = "question_statement")
    private String questionStatement;

    @ColumnInfo(name = "question_options")
    private String questionOptions;

    @ColumnInfo(name = "question_answer")
    private String questionAnswer;

    @ColumnInfo(name = "question_status")
    private int questionStatus;

    public String getQuestionStatement() {
        return questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public String getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(String questionOptions) {
        this.questionOptions = questionOptions;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public int getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(int questionStatus) {
        this.questionStatus = questionStatus;
    }
}
