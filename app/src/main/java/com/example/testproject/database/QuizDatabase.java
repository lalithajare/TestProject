package com.example.testproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.testproject.Model.AnswerDetailsSchema;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.Model.TopicResponseSchema;

@Database(entities = {QuestionDetailsResponseSchema.class, AnswerDetailsSchema.class, TopicResponseSchema.class}, version = 1)
public abstract class QuizDatabase extends RoomDatabase {
    private static QuizDatabase INSTANCE;

    public static QuizDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (QuizDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        QuizDatabase.class, "quiz.db"
                ).build();
            }
        }
        return INSTANCE;
    }

    public abstract QuestionDAO getQuestionDAO();

    public abstract AnswerDAO getOptionDAO();

    public abstract TopicDAO getTopicDAO();
}
