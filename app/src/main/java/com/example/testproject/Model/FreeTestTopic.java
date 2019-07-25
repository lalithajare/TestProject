package com.example.testproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FreeTestTopic implements Parcelable {

    public String courseId, courseName;
    public FreeTestTopic(String courseId, String courseName){

        this.courseId = courseId;
        this.courseName = courseName;

    }

    protected FreeTestTopic(Parcel in) {
        courseId = in.readString();
        courseName = in.readString();
    }

    public static final Creator<FreeTestTopic> CREATOR = new Creator<FreeTestTopic>() {
        @Override
        public FreeTestTopic createFromParcel(Parcel in) {
            return new FreeTestTopic(in);
        }

        @Override
        public FreeTestTopic[] newArray(int size) {
            return new FreeTestTopic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseId);
        dest.writeString(courseName);
    }
}
