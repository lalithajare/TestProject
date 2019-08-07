package com.example.testproject.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "topics", primaryKeys = {"section_id", "quiz_id"})
public class TopicResponseSchema implements Serializable {

    @SerializedName("section_id")
    @ColumnInfo(name = "section_id")
    @NonNull
    private String sectionId;

    @SerializedName("quiz_id")
    @ColumnInfo(name = "quiz_id")
    @NonNull
    private String quizId;

    //Local Usage
    @SerializedName("topic_index")
    @ColumnInfo(name = "topic_index")
    private int topicIndex;

    @SerializedName("is_resumable")
    @ColumnInfo(name = "is_resumable")
    private boolean isResumable;
    //Local Usage

    @SerializedName("section_name")
    @ColumnInfo(name = "section_name")
    private String sectionName;

    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    private String duration;

    @Ignore
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

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public int getTopicIndex() {
        return topicIndex;
    }

    public void setTopicIndex(int topicIndex) {
        this.topicIndex = topicIndex;
    }

    public boolean isResumable() {
        return isResumable;
    }

    public void setResumable(boolean resumable) {
        isResumable = resumable;
    }
}
