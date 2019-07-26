package com.example.testproject.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * This model is inside 'Quiz' model
 * 'Quiz' model consists of SectionPattern[] in json response coming from server
 * Hence 'quiz_id' in this table linked with 'quiz_id' of 'Quiz'
 */
@Entity(tableName = "section_pattern")
public class SectionPattern {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_section_pattern_id")
    private int localSectionPatternId;

    @ColumnInfo(name = "quiz_id")
    @SerializedName("quiz_id")
    private String quizId;

    @ColumnInfo(name = "section_name")
    @SerializedName("section_name")
    private String sectionName;

    @ColumnInfo(name = "correct_mark")
    @SerializedName("correct_mark")
    private String correctMark;

    @ColumnInfo(name = "negetive_mark")
    @SerializedName("negetive_mark")
    private String negetiveMark;

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getCorrectMark() {
        return correctMark;
    }

    public void setCorrectMark(String correctMark) {
        this.correctMark = correctMark;
    }

    public String getNegetiveMark() {
        return negetiveMark;
    }

    public void setNegetiveMark(String negetiveMark) {
        this.negetiveMark = negetiveMark;
    }

    public int getLocalSectionPatternId() {
        return localSectionPatternId;
    }

    public void setLocalSectionPatternId(int localSectionPatternId) {
        this.localSectionPatternId = localSectionPatternId;
    }
}
