package com.example.testproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.testproject.Model.AnswerDetailsSchema;

import java.util.List;

@Dao
public interface AnswerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertAnswers(List<AnswerDetailsSchema> answerList);

    @Query("SELECT * FROM answers WHERE question_id =:questionId AND topic_id = :topicId AND quiz_id = :quizId")
    List<AnswerDetailsSchema> getOptionsForQuestion(String questionId, String topicId, String quizId);

    @Query("SELECT * FROM answers WHERE question_id =:questionId AND topic_id = :topicId AND quiz_id = :quizId AND is_selected=1")
    AnswerDetailsSchema getSelectedOptionForQuestion(String questionId, String topicId, String quizId);

    @Query("DELETE FROM answers WHERE question_id =:questionId AND topic_id = :topicId AND quiz_id = :quizId")
    void deleteRecord(String questionId, String topicId, String quizId);

    @Query("DELETE FROM answers WHERE quiz_id = :quizId")
    void deleteQuizRecord(String quizId);
}
