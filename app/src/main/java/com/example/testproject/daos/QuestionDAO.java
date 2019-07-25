package com.example.testproject.daos;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.Question;

import java.util.List;

public interface QuestionDAO {

    @Insert
    Long insertQuestion(Question question);

    @Update
    Long updateQuestion(Question question);

    @Query("SELECT * FROM questions")
    List<Question> getQuestions();

    @Query("DELETE FROM questions")
    int nukeTable();

}
