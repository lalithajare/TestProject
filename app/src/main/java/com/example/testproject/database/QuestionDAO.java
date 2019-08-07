package com.example.testproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Query("SELECT * FROM questions WHERE quiz_id =:quizId AND topic_id =:topicId")
    public List<Question> getQuestions(String quizId, String topicId);

    @Query("SELECT * FROM questions WHERE quiz_id =:quizId AND topic_id =:topicId AND is_marked=1")
    public List<Question> getMarkedQuestions(String quizId, String topicId);

}
