package com.example.testproject.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public abstract class TestDatabase extends RoomDatabase {

    private static TestDatabase INSTANCE;

    public static TestDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (TestDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        TestDatabase.class, "test.db"
                ).build();
            }
        }
        return INSTANCE;
    }
}
