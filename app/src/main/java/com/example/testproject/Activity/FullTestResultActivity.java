package com.example.testproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
public class FullTestResultActivity extends AppCompatActivity {
    PieChart pieChart;
    PieDataSet dataSet;
    PieData data;
    ProgressBar progressBar;
    ArrayList<Entry> yvalues = new ArrayList<>();
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Integer> colors = new ArrayList<>();
    TextView incorrectAns, correctAns, notAns;
    private TextView tv_total_question, tv_total_time, tv_message;
    LinearLayout linearLayout1, linearLayout2;
    Button btn_view_solution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_test_result);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(false);
        progressBar = findViewById(R.id.wheel_refresh);
        tv_total_question = findViewById(R.id.tv_total_question);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_message = findViewById(R.id.tv_message);
        linearLayout1 = findViewById(R.id.l1);
        linearLayout2 = findViewById(R.id.l2);
        incorrectAns = findViewById(R.id.inc_ans);
        correctAns = findViewById(R.id.c_ans);
        notAns = findViewById(R.id.not_ans);
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        btn_view_solution = findViewById(R.id.btn_view_solution);
        btn_view_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FullTestResultActivity.this, FullTestViewSolutionActivity.class));
                finish();
            }
        });
        endTest();
    }
        public class MyValueFormatter implements ValueFormatter {

            private DecimalFormat mFormat;

            MyValueFormatter() {
                mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
            }

            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // write your logic here
                return mFormat.format(value) + ""; // e.g. append a dollar-sign
            }
        }

        private void endTest () {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlsAvision.FULL_END_TEST, new Response.Listener<String>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status_code");
                        Log.d("RESPONSE", "onResponse: " + response);
                        if (status.equalsIgnoreCase("200")) {
                            pieChart.setVisibility(View.VISIBLE);
                            pieChart.setVisibility(View.VISIBLE);
                            btn_view_solution.setVisibility(View.VISIBLE);
                            JSONObject object = new JSONObject(jsonObject.getString("message"));
                            tv_message.setText(object.getString("final_msg"));
                            tv_total_question.setText("YOU SCORED\n" + object.getString("total_marks") + "/" + Const.TOTAL_QUIZ_QUES);
                            tv_total_time.setText("TIME TAKEN\n" + object.getString("total_time"));

                            pieChart.setDrawingCacheBackgroundColor(getResources().getColor(R.color.transparent));
                            if (object.getInt("corrent_answer") > 0) {

                                yvalues.add(new Entry(object.getInt("corrent_answer"), 0));
                                colors.add(getResources().getColor(R.color.md_green_400));
                                xVals.add("Correct");
                                correctAns.setText(String.valueOf(object.getInt("corrent_number")));
                            }
                            if (object.getInt("wrong_answer") > 0) {
                                yvalues.add(new Entry(object.getInt("wrong_answer"), 1));
                                colors.add(getResources().getColor(R.color.md_red_400));
                                xVals.add("Wrong");
                                incorrectAns.setText(String.valueOf(object.getInt("wrong_number")));
                            }
                            if (object.getInt("skip") > 0) {
                                yvalues.add(new Entry(object.getInt("skip"), 2));
                                colors.add(getResources().getColor(R.color.md_grey_400));
                                xVals.add("Skipped");
                            }
                            if (object.getInt("not_ans") > 0) {
                                yvalues.add(new Entry(object.getInt("not_ans"), 3));
                                colors.add(getResources().getColor(R.color.md_blue_grey_400));
                                xVals.add("N/A");
                                notAns.setText(String.valueOf(object.getInt("not_number")));
                            }

                            pieChart.setDescription("");
                            dataSet = new PieDataSet(yvalues, "");

                            data = new PieData(xVals, dataSet);
                            pieChart.setData(data);
                            pieChart.setDrawHoleEnabled(false);
                            pieChart.setTransparentCircleRadius(20f);
                            pieChart.setRotationEnabled(false);//For Rotation
                            pieChart.setDrawSliceText(false);//For Text
                            pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad); // Rotate Event
                            dataSet.setSliceSpace(1f);
                            dataSet.setColors(colors);
                            data.setValueTextSize(13f);
                            data.setValueTextColor(Color.DKGRAY);
                            data.setValueFormatter(new MyValueFormatter());

                            pieChart.setTouchEnabled(false);
                            linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);
                            Const.answerCheckHash.clear();
                            Const.answerStoreHash.clear();
                            Const.questionAnswerStoreHash.clear();

                        } else {
                            pieChart.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("test_id", Const.STUDENT_TEST_ID);
                    params.put("end_time", Const.END_TIME);
                    Log.d("EndQuiz", "getParams: " + params);
                    return checkParams(params);
                }

                private Map<String, String> checkParams(Map<String, String> map) {
                    for (Map.Entry<String, String> pairs : map.entrySet()) {
                        if (pairs.getValue() == null) {
                            map.put(pairs.getKey(), "");
                        }
                    }
                    return map;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppWebService.getInstance(this).addToRequestQueue(stringRequest);

        }

        public boolean onKeyDown ( int keyCode, KeyEvent event){
            switch (keyCode) {
                case android.view.KeyEvent.KEYCODE_BACK:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Exit alert");
                    builder.setMessage("Do you want to exit ?");
                    builder.setIcon(R.drawable.ic_info);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();


                    //startActivity(new Intent(HomeDirectWorkActivity.this, WorkActivity.class));
                    return true;
            }
            return super.onKeyDown(keyCode, event);
        }

}
