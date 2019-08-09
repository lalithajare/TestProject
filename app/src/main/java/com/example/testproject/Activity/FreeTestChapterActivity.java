package com.example.testproject.Activity;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.FullTestQuizAdapter;
import com.example.testproject.Adapter.TestSectionAdapter;
import com.example.testproject.Model.SectionSetGet;
import com.example.testproject.Model.TestPattern;
import com.example.testproject.Model.TestQuizSetGet;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;
import com.example.testproject.Utils.InternetCheck;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.example.testproject.Utils.Const.patternArrayList;

public class FreeTestChapterActivity extends AppCompatActivity {
    TextView toolbar_title;
    Context context;
    public static ArrayList<TestQuizSetGet> freeTests = new ArrayList<>();
    public static RecyclerView recyclerView;
    AVLoadingIndicatorView av_courses_loader;
    public static ImageView noDataImage;
    SwipeRefreshLayout swipe_container;
    RelativeLayout relativeLayout;
    Toolbar toolbar;
    RecyclerView.LayoutManager layoutManager_section_topic;
    TestSectionAdapter testSectionAdapter;
    List<SectionSetGet> sectionList = new ArrayList<>();
    RecyclerView free_test_section;
    public static FullTestQuizAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_test_chapter);
        context = FreeTestChapterActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(Const.CATEGORY_NAME);
        recyclerView = findViewById(R.id.free_test_recycle);
        free_test_section = findViewById(R.id.free_test_section);
        relativeLayout = findViewById(R.id.relativeLayout);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        av_courses_loader = findViewById(R.id.av_courses_loader);
        noDataImage = findViewById(R.id.no_data_image);
        av_courses_loader.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (InternetCheck.isInternetOn(context)) {
            getFullTest(Const.CATEGORY_ID);
        } else {
            Toast.makeText(context, "No internet", Toast.LENGTH_LONG).show();
        }
        swipe_container = findViewById(R.id.swipe_container);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_container.setRefreshing(false);
                getFullTest(Const.CATEGORY_ID);

            }
        });
        free_test_section.setHasFixedSize(true);
        testSectionAdapter = new TestSectionAdapter(context, sectionList);
        layoutManager_section_topic = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        free_test_section.setLayoutManager(layoutManager_section_topic);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFullTest(Const.CATEGORY_ID);
    }


    private void getFullTest(final String categoryId) {
        noDataImage.setVisibility(View.GONE);
        av_courses_loader.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_progress_bar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_LENGTH_QUIZ_TEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    //Const.STUDENT_STATUS=object.getString("student_status");
                    Log.e("onResponse: ", status);
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray("question_list");
                        freeTests.clear();
                        TestQuizSetGet testQuizSetGet;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            testQuizSetGet = new TestQuizSetGet();
                            testQuizSetGet.setTest_quiz_id(object1.getString("quiz_id"));
                            testQuizSetGet.setTest_quiz_name(object1.getString("quiz_name"));
//                            testQuizSetGet.setTest_direction_status(object1.getString("Direction Status"));
                            testQuizSetGet.setTest_no_of_qs(object1.getString("no_of_qs"));
                            testQuizSetGet.setTest_time(object1.getString("time"));
                            testQuizSetGet.setTest_total_marks(object1.getString("total_marks"));
                            testQuizSetGet.setTest_changable(object1.getString("changable"));
                            testQuizSetGet.setTest_status(object1.getString("status"));
//                            testQuizSetGet.setTest_checked_attended(object1.getString("checked_attended"));
//                            testQuizSetGet.setRemaining_time(object1.getString("remaining_time"));
                            testQuizSetGet.setStudent_test_taken_id(object1.getString("student_test_taken_id"));
//                            testQuizSetGet.setStudent_buy_plan_status(object1.getString("student_buy_plan_status"));
//                            JSONArray testPatternArray = object1.getJSONArray("Sectional Pattern");
                            //patternArrayList.clear();
                            patternArrayList = new ArrayList<>();
//                            if (testPatternArray != null) {
//                                for (int j = 0; j < testPatternArray.length(); j++) {
//                                    JSONObject testPatternObject = testPatternArray.getJSONObject(j);
//                                    TestPattern testPattern = new TestPattern();
//                                    testPattern.setSection_name(testPatternObject.getString("section_name"));
//                                    testPattern.setCorrect_mark(testPatternObject.getString("correct_mark"));
//                                    testPattern.setNegetive_mark(testPatternObject.getString("negetive_mark"));
//                                    patternArrayList.add(testPattern);
//                                }
//                            }
                            freeTests.add(testQuizSetGet);
                        }
                        noDataImage.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        populatesectionList();

                        testAdapter = new FullTestQuizAdapter(context, freeTests);
                        recyclerView.setAdapter(testAdapter);
                    } else {
                        noDataImage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    //av_courses_loader.hide();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noDataImage.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("category_id", Const.CATEGORY_ID);
                params.put("student_id", "6");
                params.put("section_id", Const.SECTION_ID);
                params.put("question_year_stat", "0");
                Log.d("category_id", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);

    }

    private void populatesectionList() {
        sectionList.clear();
        sectionList.add(new SectionSetGet("All (" + Const.ALL + " Tests)"));
        sectionList.add(new SectionSetGet("Full Length (" + Const.FULL_LENGTH + " Tests)"));
        sectionList.add(new SectionSetGet("Sectional (" + Const.SECTIONAL + " Tests)"));
        sectionList.add(new SectionSetGet("Previous Year (" + Const.PREV_YEAR + " Tests)"));
        free_test_section.setAdapter(testSectionAdapter);
        testSectionAdapter.notifyDataSetChanged();
    }

}
