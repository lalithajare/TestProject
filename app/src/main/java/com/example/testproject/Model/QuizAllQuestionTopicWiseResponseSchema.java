package com.example.testproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizAllQuestionTopicWiseResponseSchema implements Serializable {

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("marks_scored")
    private ArrayList<TopicResponseSchema> topicResonseSchema;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TopicResponseSchema> getTopicResonseSchema() {
        return topicResonseSchema;
    }

    public void setTopicResonseSchema(ArrayList<TopicResponseSchema> topicResonseSchema) {
        this.topicResonseSchema = topicResonseSchema;
    }
}
