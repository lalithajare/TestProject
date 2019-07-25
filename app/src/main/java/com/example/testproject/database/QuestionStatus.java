package com.example.testproject.database;

public enum QuestionStatus {

    ATTEMPTED(1), PENDING(2), NOT_ATTEMPTED(3);

    private int mStatus;

    QuestionStatus(int status) {
        mStatus = status;
    }

    public int getmStatus() {
        return mStatus;
    }
}
