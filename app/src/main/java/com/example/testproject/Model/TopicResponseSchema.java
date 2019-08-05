package com.example.testproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TopicResponseSchema implements Serializable {
    @SerializedName("section_id")
    private String sectionId;

    @SerializedName("section_name")
    private String sectionName;

    @SerializedName("duration")
    private String duration;

    @SerializedName("qus_dtls")
    private ArrayList<QuestionDetailsResponseSchema> questionArrayList;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<QuestionDetailsResponseSchema> getQuestionArrayList() {
        return questionArrayList;
    }

    public void setQuestionArrayList(ArrayList<QuestionDetailsResponseSchema> questionArrayList) {
        this.questionArrayList = questionArrayList;
    }
}
