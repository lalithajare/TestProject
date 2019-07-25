package com.example.testproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.BigSolutionGridReviewAdapter;
import com.example.testproject.Adapter.BigSolutionRecycleReviewAdapter;
import com.example.testproject.Adapter.FullSolutionTestPageAdapter;
import com.example.testproject.Model.AnswerSolutionSetGet;
import com.example.testproject.Model.FreeTestTopic;
import com.example.testproject.Model.QuestionSolutionSetGet;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class FullTestViewSolutionActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private AVLoadingIndicatorView av_caf_loader;
    private ImageView noDataImage;
    private TextView tryAgainText;
    ProgressBar answerLoad;
    Context context;
    private ArrayList<QuestionSolutionSetGet> quesSolutionList = new ArrayList<>();
    private ArrayList<AnswerSolutionSetGet> ansSolutionList;
    HashMap<String, ArrayList<AnswerSolutionSetGet>> solutionListHashMap = new HashMap<>();
    Spinner spinner_topic,spinner_review;
    private ArrayList<String> strings = new ArrayList<>();
    ArrayList<FreeTestTopic> topicList = new ArrayList<>();
    RelativeLayout rl_ques;
    TextView tv_eng,tv_hindi;
    public DrawerLayout drawerLayout;
    public View drawerView;
    RecyclerView question_listView;
    ImageButton btn_grid, btn_list;
    ImageButton iv_review,iv_close;
    BigSolutionGridReviewAdapter gridReviewAdapter;
    BigSolutionRecycleReviewAdapter listReviewAdapter;
    GridView question_gridView;
    Switch switch_btn;
    RelativeLayout rl_top,rl_lang, rl_grid,rl_list;
    ArrayAdapter<String> adapter;
    RelativeLayout rl_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_test_view_solution);
        context = FullTestViewSolutionActivity.this;
        answerLoad = findViewById(R.id.ans_load);
        viewPager = findViewById(R.id.viewpager);
        av_caf_loader = findViewById(R.id.av_caf_loader);
        noDataImage = findViewById(R.id.no_data_image);
        tryAgainText = findViewById(R.id.try_again_text);
        spinner_topic=findViewById(R.id.spinner_topic);
        rl_ques=findViewById(R.id.rl_ques);
        question_gridView = findViewById(R.id.question_gridView);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        iv_review=findViewById(R.id.iv_review);
        iv_close=findViewById(R.id.iv_close);
        switch_btn=findViewById(R.id.switch_btn);
        tv_eng=findViewById(R.id.tv_eng);
        tv_hindi=findViewById(R.id.tv_hindi);
        question_listView=findViewById(R.id.question_listView);
        btn_grid=findViewById(R.id.btn_grid);
        btn_list=findViewById(R.id.btn_list);
        rl_top=findViewById(R.id.rl_top);
        rl_lang=findViewById(R.id.rl_lang);
        rl_grid=findViewById(R.id.rl_grid);
        rl_list=findViewById(R.id.rl_list);
        rl_close=findViewById(R.id.rl_close);
        spinner_review=findViewById(R.id.spinner_review);
        tryAgainText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (InternetCheck.isInternetOn(Objects.requireNonNull(getApplicationContext()))) {
                    getFullTestTopic();
                } else {
                    Toast.makeText(context, "No internet", Toast.LENGTH_LONG);
                }
            }
        });
        noDataImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (InternetCheck.isInternetOn(Objects.requireNonNull(getApplicationContext()))) {
                    getFullTestTopic();

                } else {
                    Toast.makeText(context, "No internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (InternetCheck.isInternetOn(context)) {
            getFullTestTopic();

        } else {
            Toast.makeText(context, "No internet", Toast.LENGTH_LONG).show();
        }
        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tv_hindi.setTextColor(getResources().getColor(R.color.white));
                    tv_eng.setTextColor(getResources().getColor(R.color.clearColor));

                }else {
                    tv_eng.setTextColor(getResources().getColor(R.color.white));
                    tv_hindi.setTextColor(getResources().getColor(R.color.clearColor));
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);

            }
        });
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);

            }
        });
        iv_review.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);

                adapter = new ArrayAdapter<String>(Objects.requireNonNull(getApplicationContext()), R.layout.review_spinner_layout, strings);
                spinner_review.setAdapter(adapter);
                spinner_review.setSelection(spinner_topic.getSelectedItemPosition());

                gridReviewAdapter = new BigSolutionGridReviewAdapter(context, quesSolutionList);
                question_gridView.setAdapter(gridReviewAdapter);

                listReviewAdapter = new BigSolutionRecycleReviewAdapter(context, quesSolutionList);
                question_listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,false));
                question_listView.setAdapter(listReviewAdapter);
            }
        });
        btn_list.setBackgroundResource(R.drawable.btn_list_off);
        btn_grid.setBackgroundResource(R.drawable.btn_grid_on);

        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_grid.setBackgroundResource(R.drawable.btn_grid_on);
                btn_list.setBackgroundResource(R.drawable.btn_list_off);
                question_listView.setVisibility(View.GONE);
                question_gridView.setVisibility(View.VISIBLE);

            }
        });
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_grid.setBackgroundResource(R.drawable.btn_grid_off);
                btn_list.setBackgroundResource(R.drawable.btn_list_on);
                question_gridView.setVisibility(View.GONE);
                question_listView.setVisibility(View.VISIBLE);

            }
        });
        rl_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_grid.setBackgroundResource(R.drawable.btn_grid_on);
                btn_list.setBackgroundResource(R.drawable.btn_list_off);
                question_listView.setVisibility(View.GONE);
                question_gridView.setVisibility(View.VISIBLE);

            }
        });
        rl_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_grid.setBackgroundResource(R.drawable.btn_grid_off);
                btn_list.setBackgroundResource(R.drawable.btn_list_on);
                question_gridView.setVisibility(View.GONE);
                question_listView.setVisibility(View.VISIBLE);

            }
        });
        spinner_review.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position==spinner_topic.getSelectedItemPosition()){

                }else {
                    Const.TYPE_ID = topicList.get(position).courseId;
                    rl_ques.setVisibility(View.GONE);
                    spinner_topic.setSelection(spinner_review.getSelectedItemPosition() );
                    drawerLayout.closeDrawer(drawerView);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getFullTestTopic() {
        noDataImage.setVisibility(View.GONE);
        tryAgainText.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_LENGTH_TOPIC_LIST, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("FullResponse", "onResponse: " + response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray("message");
                        topicList.clear();
                        strings.clear();
                        FreeTestTopic fullTopicTest;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            fullTopicTest = new FreeTestTopic(object1.getString("type_id"), object1.getString("type_name"));
                            topicList.add(fullTopicTest);
                            strings.add(topicList.get(i).courseName);
                        }
                        adapter = new ArrayAdapter<String>(Objects.requireNonNull(context), R.layout.free_spinner_layout, strings);
                        spinner_topic.setAdapter(adapter);
                        spinner_topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                                Const.TYPE_ID = topicList.get(position).courseId;
                                rl_ques.setVisibility(View.GONE);
                                getViewSolition();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    noDataImage.setVisibility(View.VISIBLE);
                    tryAgainText.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noDataImage.setVisibility(View.VISIBLE);
                tryAgainText.setVisibility(View.VISIBLE);
                av_caf_loader.hide();
            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.STUDENT_TEST_ID);
                Log.d("FullTopicValue", "getParams: " + params);
                return params;
            }
        };

        AppWebService.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
    private void getViewSolition() {
        noDataImage.setVisibility(View.GONE);
        tryAgainText.setVisibility(View.GONE);
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_progress_bar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_VIEW_FULL_QUIZ_SOLUTION, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.e("Response ", response);
                try {
                    iv_review.setVisibility(View.VISIBLE);
                    rl_ques.setVisibility(View.VISIBLE);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status_code");
                    String message = jsonObject.getString("message");
                    if (status.equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        quesSolutionList.clear();
                        solutionListHashMap.clear();
                        QuestionSolutionSetGet questionSetGet;
                        AnswerSolutionSetGet answerSetGet;
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object1 = jsonArray.getJSONObject(i);
                            questionSetGet = new QuestionSolutionSetGet();
                            questionSetGet.setSolution_question_id(object1.getString("question_id"));
                            questionSetGet.setSolution_question(object1.getString("question"));
                            questionSetGet.setSolution(object1.getString("solution"));
                            questionSetGet.setSolution_directions(object1.getString("directions"));
                            quesSolutionList.add(questionSetGet);
                            JSONArray jsonArray1 = object1.getJSONArray("answer_dtls");
                            ansSolutionList = new ArrayList<>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject object2 = jsonArray1.getJSONObject(j);
                                answerSetGet = new AnswerSolutionSetGet();
                                answerSetGet.setSolution_answer(object2.getString("answer"));
                                answerSetGet.setSolution_answer_status(object2.getString("answer_status"));
                                answerSetGet.setSolution_student_answer(object2.getString("student_answer"));
                                ansSolutionList.add(answerSetGet);
                            }
                            solutionListHashMap.put(questionSetGet.getSolution_question_id(), ansSolutionList);

                        }
                        FullSolutionTestPageAdapter solutionTestPageAdapter = new FullSolutionTestPageAdapter(context, quesSolutionList, solutionListHashMap);
                        viewPager.setAdapter(solutionTestPageAdapter);

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                        gridReviewAdapter = new BigSolutionGridReviewAdapter(context, quesSolutionList);
                        question_gridView.setAdapter(gridReviewAdapter);

                        listReviewAdapter = new BigSolutionRecycleReviewAdapter(context, quesSolutionList);
                        question_listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayout.VERTICAL,false));
                        question_listView.setAdapter(listReviewAdapter);

                        question_listView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

                            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onSingleTapUp(MotionEvent motionEvent) {
                                    return true;
                                }

                            });

                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                                View view1 = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                                if (view1 != null && gestureDetector.onTouchEvent(motionEvent)) {

                                    int recyclerViewPosition = Recyclerview.getChildAdapterPosition(view1);
                                    viewPager.setCurrentItem(recyclerViewPosition);
                                    drawerLayout.closeDrawer(drawerView);

                                }

                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        });

                        question_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                viewPager.setCurrentItem(position);
                                drawerLayout.closeDrawer(drawerView);

                            }
                        });

                    } else {
                        noDataImage.setVisibility(View.VISIBLE);
                        tryAgainText.setVisibility(View.VISIBLE);
                        iv_review.setVisibility(View.GONE);

                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();

                } catch (JSONException e) {
                    noDataImage.setVisibility(View.VISIBLE);
                    tryAgainText.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    iv_review.setVisibility(View.GONE);

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError)
                    Log.e("Time Out ", "error");
                noDataImage.setVisibility(View.VISIBLE);
                tryAgainText.setVisibility(View.VISIBLE);
                dialog.dismiss();
                rl_top.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.STUDENT_TEST_ID);
                params.put("type_id", Const.TYPE_ID);
                Log.d("FullSolutionValue", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppWebService.getInstance(this).addToRequestQueue(request);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (drawerLayout.isDrawerOpen(drawerView)){
                    drawerLayout.closeDrawer(drawerView);

                }else {
                    finish();

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    builder.show();*/
                }

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
