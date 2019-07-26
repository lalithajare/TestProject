package com.example.testproject.database.daos;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.QuizDetails;
import com.example.testproject.database.models.SectionPattern;

import java.util.List;

public interface SectionPatternDAO {

    @Query("SELECT * FROM section_pattern")
    public List<SectionPattern> getSectionPatternList();

    @Query("SELECT * FROM section_pattern WHERE quiz_id =:quizId")
    public Quiz getSectionPatternById(String quizId);


    @Insert
    public Long insertSectionPattern(SectionPattern sectionPattern);


    @Insert
    public Long insertSectionPatternList(List<SectionPattern> sectionPattern);

    @Update
    public int updateSectionPattern(SectionPattern sectionPattern);

    @Delete
    public int deleteSectionPattern(SectionPattern sectionPattern);

    @Query("DELETE FROM section_pattern")
    public void nukeTable();
}
