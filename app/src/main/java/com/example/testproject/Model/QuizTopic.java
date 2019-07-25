package com.example.testproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizTopic implements Parcelable {
    public String typeId, name, noQuiz, section_id, full_length_test, section_test, previous_year_test;
    public QuizTopic(String typeId, String name, String noQuiz,
                     String section_id, String full_length_test, String section_test, String previous_year_test ){
        this.typeId = typeId;
        this.name = name;
        this.noQuiz = noQuiz;
        this.section_id=section_id;
        this.full_length_test = full_length_test;
        this.section_test = section_test;
        this.previous_year_test=previous_year_test;
    }

    protected QuizTopic(Parcel in) {
        typeId = in.readString();
        name = in.readString();
        noQuiz = in.readString();
        section_id=in.readString();
        full_length_test = in.readString();
        section_test = in.readString();
        previous_year_test=in.readString();
    }

    public static final Creator<QuizTopic> CREATOR = new Creator<QuizTopic>() {
        @Override
        public QuizTopic createFromParcel(Parcel in) {
            return new QuizTopic(in);
        }

        @Override
        public QuizTopic[] newArray(int size) {
            return new QuizTopic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(typeId);
        parcel.writeString(name);
        parcel.writeString(noQuiz);
        parcel.writeString(section_id);
        parcel.writeString(full_length_test);
        parcel.writeString(section_test);
        parcel.writeString(previous_year_test);
    }
}
