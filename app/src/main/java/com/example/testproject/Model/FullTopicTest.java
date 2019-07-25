package com.example.testproject.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class FullTopicTest implements Parcelable {

    public String full_length_type_id,full_length_type_name,full_length_duration,full_length_total_question,
    full_length_count;

    public FullTopicTest(String full_length_type_id, String full_length_type_name, String full_length_duration, String full_length_total_question,
                         String full_length_count) {
        this.full_length_type_id = full_length_type_id;
        this.full_length_type_name = full_length_type_name;
        this.full_length_duration = full_length_duration;
        this.full_length_total_question = full_length_total_question;
        this.full_length_count = full_length_count;

    }

    protected FullTopicTest(Parcel in) {
        full_length_type_id=in.readString();
        full_length_type_name=in.readString();
        full_length_duration=in.readString();
        full_length_total_question=in.readString();
        full_length_count=in.readString();
    }

    public static final Creator<FullTopicTest> CREATOR = new Creator<FullTopicTest>() {
        @Override
        public FullTopicTest createFromParcel(Parcel in) {
            return new FullTopicTest(in);
        }

        @Override
        public FullTopicTest[] newArray(int size) {
            return new FullTopicTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(full_length_type_id);
        parcel.writeString(full_length_type_name);
        parcel.writeString(full_length_duration);
        parcel.writeString(full_length_total_question);
        parcel.writeString(full_length_count);
    }
}
