package com.example.testproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Quiz;

import java.util.List;

@Dao
public interface QuizDAO {

    @Query("SELECT * FROM quiz")
    public List<Quiz> getQuizList();

    @Query("SELECT * FROM quiz WHERE quiz_id =:quizId")
    public Quiz getQuizById(String quizId);


    @Insert
    public Long insertQuiz(Quiz quiz);

    @Insert
    public void insertQuizes(List<Quiz> quiz);

    @Update
    public int updateQuiz(Quiz quiz);

    @Delete
    public int deleteQuiz(Quiz quiz);

    @Query("DELETE FROM quiz")
    public void nukeTable();
}
