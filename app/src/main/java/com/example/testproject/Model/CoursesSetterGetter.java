package com.example.testproject.Model;

/**
 * Created by INDID on 15-03-2018.
 */

public class CoursesSetterGetter {
    private String course_name;
    private String course_id;
    private String course_img;
    private String course_desc;
    private String study_pdf;
    private int exam_id;

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_img() {
        return course_img;
    }

    public void setCourse_img(String course_img) {
        this.course_img = course_img;
    }

    public String getCourse_desc() {
        return course_desc;
    }

    public void setCourse_desc(String course_desc) {
        this.course_desc = course_desc;
    }

    public String getStudy_pdf() {
        return study_pdf;
    }

    public void setStudy_pdf(String study_pdf) {
        this.study_pdf = study_pdf;
    }
}
