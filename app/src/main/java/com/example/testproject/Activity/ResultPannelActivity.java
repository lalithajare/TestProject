package com.example.testproject.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.ResultSectionAdapter;
import com.example.testproject.Adapter.ResultSectionMarkAdapter;
import com.example.testproject.Adapter.TopRankerAdapter;
import com.example.testproject.Model.ResultSectionMarkSetGet;
import com.example.testproject.Model.ResultSectionSetGet;
import com.example.testproject.Model.TopRankerSetGet;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ResultPannelActivity extends AppCompatActivity {
    private static final String TAG = ResultPannelActivity.class.getSimpleName();
    Toolbar toolbar;
    TextView toolbar_title;
    Context context;

    ProgressDialog progressDialog;

    RecyclerView rv_top_rankers, rv_section_topic;
    public static RecyclerView rv_section_rank;

    List<TopRankerSetGet> topRankerList = new ArrayList<>();
    TopRankerAdapter topRankerAdapter;

    RecyclerView.LayoutManager layoutManager_ranker;
    RecyclerView.LayoutManager layoutManager_section_topic;
    public static RecyclerView.LayoutManager layoutManager_section_mark;

    TextView tv_view_top_ranker, tv_send_rate_us, tv_result_test_name;

    ResultSectionAdapter resultSectionAdapter;
    List<ResultSectionSetGet> resultSectionList = new ArrayList<>();
    RatingBar ratingBar;

    public static ResultSectionMarkAdapter resultSectionMarkAdapter;
    public static List<ResultSectionMarkSetGet> resultSectionMarkList = new ArrayList<>();

    TextView tv_result_name, tv_result_score, tv_result_rank, tv_result_time, tv_result_share;
    RelativeLayout rl_refresh_rank;

    TextView tv_correct_mark, tv_wrong_mark, tv_skip_mark;
    ProgressBar progressBar_correct, progressBar_wrong, progressBar_skip, progressBar_refresh;

    Button btn_again_attempt, btn_view_solution, btn_section_view_solution;

    public static TextView tv_you_section_mark, tv_topper_section_mark, tv_average_section_mark;
    public static ProgressBar progressBar_section_you, progressBar_section_topper, progressBar_section_average;

    ImageView iv_result_refresh;
    CardView cardView_top4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_pannel);
        context = ResultPannelActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_page);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Result");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rv_top_rankers = findViewById(R.id.rv_top_rankers);
        rv_section_topic = findViewById(R.id.rv_section_topic);
        rv_section_rank = findViewById(R.id.rv_section_rank);

        tv_view_top_ranker = findViewById(R.id.tv_view_top_ranker);
        ratingBar = findViewById(R.id.ratingBar);
        tv_send_rate_us = findViewById(R.id.tv_send_rate_us);

        tv_result_name = findViewById(R.id.tv_result_name);
        tv_result_score = findViewById(R.id.tv_result_score);
        tv_result_rank = findViewById(R.id.tv_result_rank);
        tv_result_time = findViewById(R.id.tv_result_time);
        tv_result_share = findViewById(R.id.tv_result_share);
        tv_correct_mark = findViewById(R.id.tv_correct_mark);
        tv_wrong_mark = findViewById(R.id.tv_wrong_mark);
        tv_skip_mark = findViewById(R.id.tv_skip_mark);
        tv_you_section_mark = findViewById(R.id.tv_you_section_mark);
        tv_topper_section_mark = findViewById(R.id.tv_topper_section_mark);
        tv_average_section_mark = findViewById(R.id.tv_average_section_mark);
        tv_result_test_name = findViewById(R.id.tv_result_test_name);

        btn_again_attempt = findViewById(R.id.btn_again_attempt);
        btn_view_solution = findViewById(R.id.btn_view_solution);
        btn_section_view_solution = findViewById(R.id.btn_section_view_solution);

        progressBar_section_you = findViewById(R.id.progressBar_section_you);
        progressBar_section_topper = findViewById(R.id.progressBar_section_topper);
        progressBar_section_average = findViewById(R.id.progressBar_section_average);
        progressBar_correct = findViewById(R.id.progressBar_correct);
        progressBar_wrong = findViewById(R.id.progressBar_wrong);
        progressBar_skip = findViewById(R.id.progressBar_skip);

        progressBar_refresh = findViewById(R.id.progressBar_refresh);
        rl_refresh_rank = findViewById(R.id.rl_refresh_rank);
        iv_result_refresh = findViewById(R.id.iv_result_refresh);

        cardView_top4 = findViewById(R.id.cardView_top4);

        rv_top_rankers.setHasFixedSize(true);
        layoutManager_ranker = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_top_rankers.setLayoutManager(layoutManager_ranker);
        populateTopRankerList();

        rv_section_topic.setHasFixedSize(true);
        layoutManager_section_topic = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv_section_topic.setLayoutManager(layoutManager_section_topic);

        rv_section_rank.setHasFixedSize(true);
        resultSectionMarkAdapter = new ResultSectionMarkAdapter(context, resultSectionMarkList);
        layoutManager_section_mark = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_section_rank.setLayoutManager(layoutManager_section_mark);

        tv_view_top_ranker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TopRankerActivity.class));
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                tv_send_rate_us.setVisibility(View.VISIBLE);
            }
        });
        tv_send_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Thanks for your rating " + ratingBar.getRating() + "★", Toast.LENGTH_SHORT).show();
            }
        });

        btn_view_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FullTestViewSolutionActivity.class));
            }
        });

        rl_refresh_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_refresh.setVisibility(View.VISIBLE);
                rl_refresh_rank.setVisibility(View.GONE);
                Const.END_TIME_STATUS = "0";
                getTestScore();

            }
        });
        getTestScore();
        getOverallAnalysis();
        populateResultSectionList();

    }

    private void getOverallAnalysis() {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_OVERALL_ANALYSIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            if (status.equalsIgnoreCase("203")) {
                                JSONObject scoreObject = object.getJSONObject("marks_scored");
                                tv_correct_mark.setText(scoreObject.getString("count_correct_ans"));
                                tv_wrong_mark.setText(scoreObject.getString("count_wrong_ans"));
                                tv_skip_mark.setText(scoreObject.getString("skipped"));
                                progressBar_correct.setProgress(Integer.valueOf(scoreObject.getString("count_correct_ans")));
                                progressBar_wrong.setProgress(Integer.valueOf(scoreObject.getString("count_wrong_ans")));
                                progressBar_skip.setProgress(Integer.valueOf(scoreObject.getString("skipped")));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR : " + error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.TEST_ID);
                params.put("test_taken_id", Const.STUDENT_TEST_TAKEN_ID);
                params.put("student_id", "6");
                params.put("test_end_time", Const.END_TIME);
                params.put("end_time_status", Const.END_TIME_STATUS);
                Log.d("OverALlResult", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);
    }

    private void getTestScore() {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_RESULT_ANALYSIS_SCORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            Log.d("ScoreBoard", "onResponse: " + response);
                            if (status.equalsIgnoreCase("203")) {
                                JSONObject scoreObject = object.getJSONObject("marks_scored");
                                progressBar_refresh.setVisibility(View.GONE);
                                rl_refresh_rank.setVisibility(View.VISIBLE);
                                tv_result_test_name.setText(scoreObject.getString("quiz_name"));
                                tv_result_name.setText(getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE).getString("user_name", null));
                                tv_result_score.setText(scoreObject.getString("total_score") + " / " + scoreObject.getString("test_full_marks"));
                                tv_result_rank.setText(scoreObject.getString("rank_count") + " / " + scoreObject.getString("total_given_exam"));

                                if (!scoreObject.getString("hours").equalsIgnoreCase("0")) {
                                    if (!scoreObject.getString("minutes").equalsIgnoreCase("0")) {
                                        tv_result_time.setText(scoreObject.getString("hours") + "hr : " + scoreObject.getString("minutes")
                                                + "min : " + scoreObject.getString("seconds") +
                                                "sec / " + scoreObject.getString("test_total_time") + "min");

                                    } else {
                                        tv_result_time.setText(scoreObject.getString("seconds") +
                                                "sec / " + scoreObject.getString("test_total_time") + "min");
                                    }
                                } else {
                                    if (!scoreObject.getString("minutes").equalsIgnoreCase("0")) {
                                        tv_result_time.setText(scoreObject.getString("minutes")
                                                + "min : " + scoreObject.getString("seconds") +
                                                "sec / " + scoreObject.getString("test_total_time") + "min");

                                    } else {
                                        tv_result_time.setText(scoreObject.getString("seconds") +
                                                "sec / " + scoreObject.getString("test_total_time") + "min");
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.TEST_ID);
                params.put("student_id", "6");
                params.put("test_end_time", Const.END_TIME);
                params.put("test_taken_id", Const.STUDENT_TEST_TAKEN_ID);
                params.put("end_time_status", Const.END_TIME_STATUS);
                Log.d("ResultScore", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);

    }


    private void populateResultSectionList() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_SECTIONAL_ANALYSIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            if (status.equalsIgnoreCase("203")) {
                                JSONArray countJsonArray = object.getJSONArray("all_count");
                                JSONObject sectionObject = countJsonArray.getJSONObject(0);
                                progressBar_section_you.setProgress((int) (sectionObject.getLong("your_marks")));
                                progressBar_section_you.setProgressDrawable(getResources().getDrawable(R.drawable.progress_drawable_mark));
                                progressBar_section_topper.setProgress((int) (sectionObject.getLong("topper_marks")));
                                progressBar_section_topper.setProgressDrawable(getResources().getDrawable(R.drawable.progress_drawable_mark));
                                progressBar_section_average.setProgress((int) (sectionObject.getLong("avg_marks")));
                                progressBar_section_average.setProgressDrawable(getResources().getDrawable(R.drawable.progress_drawable_mark));

                                tv_you_section_mark.setText(sectionObject.getString("your_marks"));
                                tv_average_section_mark.setText(sectionObject.getString("avg_marks"));
                                tv_topper_section_mark.setText(sectionObject.getString("topper_marks"));

                                JSONArray sectionJsonArray = object.getJSONArray("marks_scored");
                                resultSectionList.clear();
                                ResultSectionSetGet resultSectionSetGet;
                                for (int i = 0; i < sectionJsonArray.length(); i++) {
                                    JSONObject object1 = sectionJsonArray.getJSONObject(i);
                                    resultSectionSetGet = new ResultSectionSetGet();
                                    resultSectionSetGet.setResult_sectional_id(object1.getString("sectional_id"));
                                    resultSectionSetGet.setResult_sectional_name(object1.getString("sectional_name"));
                                    resultSectionList.add(resultSectionSetGet);
                                }
                                resultSectionAdapter = new ResultSectionAdapter(context, resultSectionList);
                                rv_section_topic.setAdapter(resultSectionAdapter);

                                JSONArray buttonJsonArray = object.getJSONArray("button");
                                JSONObject buttonObject = buttonJsonArray.getJSONObject(0);
                                resultSectionMarkList.clear();
                                resultSectionMarkList.add(new ResultSectionMarkSetGet(buttonObject.getString("marks"), "Marks"));
                                resultSectionMarkList.add(new ResultSectionMarkSetGet(buttonObject.getString("correct"), "Correct"));
                                resultSectionMarkList.add(new ResultSectionMarkSetGet(buttonObject.getString("wrong"), "Wrong"));
                                rv_section_rank.setAdapter(resultSectionMarkAdapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "ERROR : " + error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.TEST_ID);
                params.put("test_taken_id", Const.STUDENT_TEST_TAKEN_ID);
                params.put("student_id", "6");
                Log.d("TopRankerScore", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);

    }

    private void populateTopRankerList() {

        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_TOP_RANKER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            if (status.equalsIgnoreCase("203")) {
                                JSONArray jsonArray = object.getJSONArray("marks_scored");
                                topRankerList.clear();
                                TopRankerSetGet topRankerSetGet;
                                cardView_top4.setVisibility(View.VISIBLE);


                                if (jsonArray.length() > 4) {
                                    tv_view_top_ranker.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < 4; i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        topRankerSetGet = new TopRankerSetGet();
                                        topRankerSetGet.setRanker_user_id(object1.getString("user_id"));
                                        topRankerSetGet.setRanker_user_name(object1.getString("user_name"));
                                        topRankerSetGet.setRanker_user_img(object1.getString("user_img"));
                                        topRankerSetGet.setRanker_correct_mark(object1.getString("correct_mark"));
                                        topRankerSetGet.setRanker_total_marks(object1.getString("total_marks"));
                                        topRankerList.add(topRankerSetGet);
                                    }
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        topRankerSetGet = new TopRankerSetGet();
                                        topRankerSetGet.setRanker_user_id(object1.getString("user_id"));
                                        topRankerSetGet.setRanker_user_name(object1.getString("user_name"));
                                        topRankerSetGet.setRanker_user_img(object1.getString("user_img"));
                                        topRankerSetGet.setRanker_correct_mark(object1.getString("correct_mark"));
                                        topRankerSetGet.setRanker_total_marks(object1.getString("total_marks"));
                                        topRankerList.add(topRankerSetGet);
                                    }
                                    tv_view_top_ranker.setVisibility(View.GONE);

                                }
                                topRankerAdapter = new TopRankerAdapter(context, topRankerList);
                                rv_top_rankers.setAdapter(topRankerAdapter);
                            } else {
                                cardView_top4.setVisibility(View.GONE);
                            }

                            /*progressDialog.hide();*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.TEST_ID);
                params.put("student_id", "6");
                Log.d("TopRanker", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);
    }
}
