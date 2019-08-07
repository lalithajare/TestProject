package com.example.testproject.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "questions", primaryKeys = {"question_id", "quiz_id", "topic_id"})
public class QuestionDetailsResponseSchema implements Serializable {

    @SerializedName("question_id")
    @ColumnInfo(name = "question_id")
    @NonNull
    private String questionId;

    //Local usage
    @ColumnInfo(name = "quiz_id")
    @NonNull
    private String quizId;

    @ColumnInfo(name = "topic_id")
    @NonNull
    private String topicId;
    //Local usage


    @SerializedName("directions")
    @ColumnInfo(name = "directions")
    private String directions;

    @SerializedName("question")
    @ColumnInfo(name = "question")
    private String question;

    @Ignore
    @SerializedName("answer_dtls")
    private ArrayList<AnswerDetailsSchema> answerDetails;

    //For local purpose
    @SerializedName("is_marked")
    @ColumnInfo(name = "is_marked")
    private boolean isMarked;

    @SerializedName("is_visited")
    @ColumnInfo(name = "is_visited")
    private boolean isVisited;

    @SerializedName("is_answered")
    @ColumnInfo(name = "is_answered")
    private boolean isAnswered;

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

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}
