package com.example.testproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnswerDetailsSchema implements Serializable {
    @SerializedName("answer_id")
    private String answerId;

    @SerializedName("answer")
    private String answer;

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
