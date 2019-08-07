package com.example.testproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Question.class}, version = 1)
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

    public abstract QuestionDAO getQuestioNDAO();
}
