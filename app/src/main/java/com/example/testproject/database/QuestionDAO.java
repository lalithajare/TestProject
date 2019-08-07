package com.example.testproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.testproject.Model.QuestionDetailsResponseSchema;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertQuestions(List<QuestionDetailsResponseSchema> questions);

    @Query("SELECT * FROM questions WHERE quiz_id =:quizId AND topic_id =:topicId")
    public List<QuestionDetailsResponseSchema> getQuestions(String quizId, String topicId);

    @Query("SELECT * FROM questions WHERE quiz_id =:quizId AND topic_id =:topicId AND is_marked=1")
    public List<QuestionDetailsResponseSchema> getMarkedQuestions(String quizId, String topicId);

    @Query("DELETE FROM questions WHERE quiz_id =:quizId")
    void deleteQuizRecord(String quizId);


}
