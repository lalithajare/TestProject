package com.example.testproject.URLs;

/**
 * Created by INDID on 10-02-2018.
 */

public class UrlsAvision {

    public final static String URL_COURSE, URL_FULL_TEST, URL_FULL_LENGTH_QUIZ, URL_FULL_LENGTH_QUIZ_TEST,
            URL_FULL_LENGTH_TOPIC_LIST, URL_VIEW_FULL_QUIZ_SOLUTION, URL_FULL_LENGTH_QUIZ_TOPIC_QUES,
            URL_FULL_LENGTH_QUIZ_ALL_QUES, SAVE_FULL_ANS, URL_FULL_LENGTH_QUIZ_TOPIC, FULL_END_TEST,
            URL_CLEAR_ANSWER, URL_TOP_RANKER, URL_MARK_SCORE, URL_OVERALL_ANALYSIS, URL_RESULT_ANALYSIS_SCORE, URL_SECTIONAL_ANALYSIS,
            URL_SECTION_MARK;

    static {
        String BASE_URL = "https://avisiongroup.in/avision_app/index.php/api/";
        URL_COURSE = BASE_URL + "fetch_main_course_api";
        URL_FULL_TEST = BASE_URL + "full_lenth_list";


        URL_FULL_LENGTH_QUIZ = BASE_URL + "full_length_quiz";


        URL_FULL_LENGTH_QUIZ_TEST = BASE_URL + "full_length_quiz_test";


        URL_FULL_LENGTH_TOPIC_LIST = BASE_URL + "full_view_solution_section";
        URL_VIEW_FULL_QUIZ_SOLUTION = BASE_URL + "full_view_solution";
        URL_FULL_LENGTH_QUIZ_TOPIC_QUES = BASE_URL + "full_length_question_list";
        URL_FULL_LENGTH_QUIZ_ALL_QUES = BASE_URL + "all_section_wise_questionm_fetch";
        SAVE_FULL_ANS = BASE_URL + "save_student_full_question_answer";
        URL_FULL_LENGTH_QUIZ_TOPIC = BASE_URL + "full_length_quiz_question";
        FULL_END_TEST = BASE_URL + "student_end_full_test";
        URL_CLEAR_ANSWER = BASE_URL + "clear_student_response";
        URL_TOP_RANKER = BASE_URL + "top_rankers";
        URL_MARK_SCORE = BASE_URL + "graph_score_new";
        URL_OVERALL_ANALYSIS = BASE_URL + "overall_analysis";
        URL_RESULT_ANALYSIS_SCORE = BASE_URL + "rank_n_score";
        URL_SECTIONAL_ANALYSIS = BASE_URL + "sectional_result";
        URL_SECTION_MARK = BASE_URL + "sectional_marks_count";

    }
}
