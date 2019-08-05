package com.example.testproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionDetailsResponseSchema implements Serializable {

    @SerializedName("question_id")
    private String questionId;

    @SerializedName("directions")
    private String directions;

    @SerializedName("question")
    private String question;

    @SerializedName("answer_dtls")
    private ArrayList<AnswerDetailsSchema> answerDetails;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

    public ArrayList<AnswerDetailsSchema> getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(ArrayList<AnswerDetailsSchema> answerDetails) {
        this.answerDetails = answerDetails;
    }
}
