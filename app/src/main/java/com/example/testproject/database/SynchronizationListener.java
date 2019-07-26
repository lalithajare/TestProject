package com.example.testproject.database;

public interface SynchronizationListener {

    //Called when syncing of Courses is done
    void onCoursesSaved();

    //Called when syncing of Categories is done
    void onCategoriesSaved();

    //Called when syncing of Quiz is done
    void onQuizesSaved();

    //Called when syncing of QuizDetail is done
    void onQuizDetailsSaved();

    //Called when syncing of SectionPattern is done
    void onSectionPatternSaved();

    //Called when syncing of Quiz is Question
    void onQuestionsSaved();

    //Called when syncing of Answers is done
    void onAnswersSaved();

}
