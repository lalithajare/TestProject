package com.example.testproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.QuizDetails;

import java.util.List;

@Dao
public interface QuizDetailsDAO {
    @Query("SELECT * FROM quiz_details")
    public List<QuizDetails> getQuizDetailsList();

    @Query("SELECT * FROM quiz_details WHERE local_quiz_detail_id =:quizDetailsId")
    public QuizDetails getQuizDetailsById(String quizDetailsId);


    @Insert
    public Long insertQuizDetails(QuizDetails quizDetails);

    @Insert
    public void insertQuizDetailsList(List<QuizDetails> quizDetails);

    @Update
    public int updateQuizDetails(QuizDetails quizDetails);

    @Delete
    public int deleteQuizDetails(QuizDetails quizDetails);

    @Query("DELETE FROM quiz_details")
    public void nukeTable();
}
