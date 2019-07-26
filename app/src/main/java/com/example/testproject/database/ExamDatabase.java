package com.example.testproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.testproject.database.daos.AnswerDAO;
import com.example.testproject.database.daos.CategoryDAO;
import com.example.testproject.database.daos.CourseDAO;
import com.example.testproject.database.daos.QuestionDAO;
import com.example.testproject.database.daos.QuizDAO;
import com.example.testproject.database.daos.QuizDetailsDAO;
import com.example.testproject.database.daos.SectionPatternDAO;
import com.example.testproject.database.models.Answer;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.models.Course;
import com.example.testproject.database.models.Question;
import com.example.testproject.database.models.Quiz;
import com.example.testproject.database.models.QuizDetails;
import com.example.testproject.database.models.SectionPattern;

@Database(entities = {Course.class, Category.class, Quiz.class,
        QuizDetails.class, SectionPattern.class, Question.class,
        Answer.class,}, version = 1)
public abstract class ExamDatabase extends RoomDatabase {

    private static ExamDatabase INSTANCE;

    public static ExamDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (ExamDatabase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        ExamDatabase.class, "test.db"
                ).build();
            }
        }
        return INSTANCE;
    }

    public abstract CourseDAO getCourseDAO();

    public abstract CategoryDAO getCategoryDAO();

    public abstract QuizDAO getQuizDAO();

    public abstract QuizDetailsDAO getQuizDetailsDAO();

    public abstract SectionPatternDAO getSectionPatternDAO();

    public abstract QuestionDAO getQuestionDAO();

    public abstract AnswerDAO getAnswerDAO();

}
