package com.example.testproject.database.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Answer;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.SectionPattern;

import java.util.List;

@Dao
public interface AnswerDAO {
    @Query("SELECT * FROM answer_details")
    public List<Answer> getAnswerList();

    @Query("SELECT * FROM answer_details WHERE answer_id =:answerId")
    public Answer getAnswerById(String answerId);

    @Query("SELECT * FROM answer_details WHERE question_id =:questionId")
    public Answer getAnswerByQuestionId(String questionId);


    @Insert
    public Long insertAnswer(Answer answer);

    @Insert
    public void insertAnswers(List<Answer> answers);

    @Update
    public int updateAnswer(Answer answer);

    @Delete
    public int deleteAnswer(Answer answer);

    @Query("DELETE FROM answer_details")
    public void nukeTable();
}
