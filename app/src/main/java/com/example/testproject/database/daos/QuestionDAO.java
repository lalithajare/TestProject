package com.example.testproject.database.daos;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.testproject.database.models.Answer;
import com.example.testproject.database.models.Question;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.SectionPattern;

import java.util.List;

public interface QuestionDAO {
    @Query("SELECT * FROM question")
    public List<Question> getQuestionList();

    @Query("SELECT * FROM question WHERE question_id =:questionId")
    public Quiz getQuestionById(String questionId);

    @Query("SELECT * FROM question WHERE quiz_id =:quizId")
    public Quiz getQuestionByQuizId(String quizId);

    @Insert
    public Long insertQuestion(Question question);

    @Insert
    public Long insertQuestions(List<Question> questions);

    @Update
    public int updateQuestion(Question question);

    @Delete
    public int deleteQuestion(Question question);

    @Query("DELETE FROM question")
    public void nukeTable();
}
