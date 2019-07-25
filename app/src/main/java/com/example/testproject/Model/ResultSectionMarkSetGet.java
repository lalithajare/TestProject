package com.example.testproject.Model;

public class ResultSectionMarkSetGet {
    String section_mark, section_mark_msg;

    public ResultSectionMarkSetGet(String section_mark, String section_mark_msg) {
        this.section_mark = section_mark;
        this.section_mark_msg = section_mark_msg;
    }

    public String getSection_mark() {
        return section_mark;
    }

    public void setSection_mark(String section_mark) {
        this.section_mark = section_mark;
    }

    public String getSection_mark_msg() {
        return section_mark_msg;
    }

    public void setSection_mark_msg(String section_mark_msg) {
        this.section_mark_msg = section_mark_msg;
    }
}
