package com.example.testproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.testproject.Model.AnswerDetailsSchema;
import com.example.testproject.Model.TopicResponseSchema;

import java.util.List;

@Dao
public interface TopicDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertTopics(List<TopicResponseSchema> topicList);

    @Query("SELECT * FROM topics WHERE quiz_id = :quizId")
    List<TopicResponseSchema> getTopics(String quizId);

    @Query("DELETE FROM topics WHERE quiz_id = :quizId")
    void deleteQuizRecord(String quizId);
}
