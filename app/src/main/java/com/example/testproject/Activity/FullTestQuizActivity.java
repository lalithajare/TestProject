package com.example.testproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.testproject.Adapter.BigGridReviewAdapter;
import com.example.testproject.Adapter.BigRecycleReviewAdapter;
import com.example.testproject.Adapter.FullQuizTestPageAdapter;
import com.example.testproject.Model.AnswerSetGet;
import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.Model.FullTopicTest;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppPreferenceManager;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;

import com.example.testproject.Utils.CustomCountDownTimer;
import com.example.testproject.Utils.CustomViewPager;
import com.example.testproject.Utils.InternetCheck;
import com.example.testproject.Utils.UtilFunctions;
import com.example.testproject.common.ApiCallManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;


public class FullTestQuizActivity extends ParentQuizActivity {
    private static final String TAG = FullTestQuizActivity.class.getSimpleName();

    private ImageView noDataImage;
    private TextView tryAgainText;
    Button finishButton, submitButton, markButton;
    ProgressBar answerLoad;
    TextView totalQuestion;
    SimpleDateFormat df;
    Calendar c;
    private String currentTime;
    //    CountDownTimer countDownTimer;
    Context context;
    private ArrayList<FullQuestionSetGet> quesList;
    private ArrayList<AnswerSetGet> ansList;
    HashMap<String, ArrayList<AnswerSetGet>> listHashMap;
    RelativeLayout rl_top;
    ImageButton iv_review, iv_close;
    BigGridReviewAdapter gridReviewAdapter;
    BigRecycleReviewAdapter listReviewAdapter;
    GridView question_gridView;
    //    ImageButton iv_play;
    public DrawerLayout drawerLayout;
    public View drawerView;
    RecyclerView question_listView;
    ImageButton btn_grid, btn_list;
    RelativeLayout rl_grid, rl_list, rl_ques;
    Switch switch_btn;
    TextView tv_eng, tv_hindi;
    Spinner spinner_topic, spinner_review;
    ArrayList<FullTopicTest> topicList = new ArrayList<>();
    private ArrayList<String> strings = new ArrayList<>();
    ArrayAdapter<String> adapter;
    RelativeLayout rl_close, rl_view_marked;
    TextView tv_total_time;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //Added Later
    String quiz_id;
    String totalTime;
    Boolean wasPaused;
    CustomViewPager viewPager;
    AlertDialog mOfflineAttemptsDialogue;
    RelativeLayout rl_timer;
    ImageView iv_play;
    CustomCountDownTimer customCountDownTimer;
    ArrayList<Hashtable<String, String>> attemptedOfflineQuestions = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_test_quiz);


        initData();


        context = FullTestQuizActivity.this;

        //Added Later
        rl_timer = findViewById(R.id.rl_timer);
        iv_play = findViewById(R.id.iv_play);

        answerLoad = findViewById(R.id.ans_load);
        tv_total_time = findViewById(R.id.tv_total_time);
        iv_review = findViewById(R.id.iv_review);
        totalQuestion = findViewById(R.id.text_no_of_question);
        finishButton = findViewById(R.id.test_finish_button);
        submitButton = findViewById(R.id.submit_ans);
        markButton = findViewById(R.id.pre_ans);
        // clearButton=findViewById(R.id.clear_ans);
        viewPager = findViewById(R.id.viewpager);
        noDataImage = findViewById(R.id.no_data_image);
        tryAgainText = findViewById(R.id.try_again_text);
        markButton.setVisibility(View.GONE);
        // clearButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
//        iv_play = findViewById(R.id.iv_play);
        question_gridView = findViewById(R.id.question_gridView);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        iv_review = findViewById(R.id.iv_review);
        iv_close = findViewById(R.id.iv_close);
        switch_btn = findViewById(R.id.switch_btn);
        tv_eng = findViewById(R.id.tv_eng);
        tv_hindi = findViewById(R.id.tv_hindi);
        question_listView = findViewById(R.id.question_listView);
        btn_grid = findViewById(R.id.btn_grid);
        btn_list = findViewById(R.id.btn_list);
        rl_grid = findViewById(R.id.rl_grid);
        rl_list = findViewById(R.id.rl_list);
        spinner_topic = findViewById(R.id.spinner_topic);
        spinner_review = findViewById(R.id.spinner_review);
        rl_ques = findViewById(R.id.rl_ques);
        rl_view_marked = findViewById(R.id.rl_view_marked);
        c = Calendar.getInstance();
        df = new SimpleDateFormat("HH:mm:ss");
        currentTime = df.format(c.getTime());
        rl_top = findViewById(R.id.rl_top);
        rl_close = findViewById(R.id.rl_close);
        spinner_topic.setEnabled(true);
        spinner_review.setEnabled(true);
        pref = context.getSharedPreferences("HashValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markButton.getText().toString().equalsIgnoreCase("Mark for Review")) {
                    if (Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
                        Const.hashMapSelectMarkReview.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                        Const.hashMapMarkSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());

                    } else {
                        Const.hashMapMarkSelected.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                        Const.hashMapSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());

                    }
                    markButton.setText("Mark for\n Review");
                    rl_view_marked.setVisibility(View.VISIBLE);

                } else {
                    if (Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
                        Const.hashMapMarkSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapSelectMarkReview.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapSelected.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                        markButton.setText("Mark for Review");
                        rl_view_marked.setVisibility(View.GONE);
                    } else {
                        Const.hashMapMarkSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapSelectMarkReview.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        markButton.setText("Mark for Review");
                        rl_view_marked.setVisibility(View.GONE);
                    }

                }
                for (Map.Entry<String, Boolean> entry : Const.hashMapSelected.entrySet()) {
                    editor.putBoolean(entry.getKey(), true);
                }
                editor.commit();

            }
        });
        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_hindi.setTextColor(getResources().getColor(R.color.white));
                    tv_eng.setTextColor(getResources().getColor(R.color.clearColor));

                } else {
                    tv_eng.setTextColor(getResources().getColor(R.color.white));
                    tv_hindi.setTextColor(getResources().getColor(R.color.clearColor));
                }
            }
        });

       /* if (Const.answerStoreHash.size()>0 &&
                Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())){
            clearButton.setText("Clear\n Selection");
            clearButton.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            clearButton.setText("Clear Selection");
            clearButton.setTextColor(Color.parseColor("#B9BBBB"));
        }
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())){
                    //Const.answerStoreHash.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                    //clearButton.setTextColor(Color.parseColor("#B9BBBB"));
                    //clearButton.setText("Clear Selection");
                    clearQuiz();
                }


            }
        });*/

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
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
                adapter = new ArrayAdapter<String>(Objects.requireNonNull(getApplicationContext()), R.layout.review_spinner_layout, strings);
                spinner_review.setAdapter(adapter);
                spinner_review.setSelection(spinner_topic.getSelectedItemPosition());

                gridReviewAdapter = new BigGridReviewAdapter(context, quesList);
                question_gridView.setAdapter(gridReviewAdapter);

                listReviewAdapter = new BigRecycleReviewAdapter(context, quesList);
                question_listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
                question_listView.setAdapter(listReviewAdapter);
            }
        });
        btn_list.setBackgroundResource(R.drawable.btn_list_off);
        btn_grid.setBackgroundResource(R.drawable.btn_grid_on);

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

        if (InternetCheck.isInternetOn(Objects.requireNonNull(getApplicationContext()))) {
            callTopicsAPI();
        } else {
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customCountDownTimer.isRunning()) {
                    if (markButton.getText().toString().equalsIgnoreCase("Mark for\n Review")
                            && Const.answerCheckHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
                        Const.hashMapSelectMarkReview.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                        Const.hashMapSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapMarkSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());

                    } else {
                        Const.hashMapSelectMarkReview.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                        Const.hashMapSelected.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                    }

                    if (InternetCheck.isInternetOn(Objects.requireNonNull(getApplicationContext()))) {
                        callSubmitAnswerAPI();
                    } else {
//                    Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
                        Hashtable<String, String> params = new Hashtable<>();
                        params.put("test_id", Const.STUDENT_TEST_ID);
                        params.put("qus_id", Const.CHOOSE_QUESTION_ID);
                        params.put("answers_id", Const.ANSWER_ID);
                        params.put("question_status", "1");
                        attemptedOfflineQuestions.add(params);
                        nextQuestion();
                    }
                    for (Map.Entry<String, Boolean> entry : Const.hashMapSelected.entrySet()) {
                        editor.putBoolean(entry.getKey(), true);
                    }
                    editor.commit();
                } else {
                    Toast.makeText(getApplicationContext(), "The Test is Paused, please resume it.", Toast.LENGTH_LONG).show();
                }

            }
        });

        spinner_review.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == spinner_topic.getSelectedItemPosition()) {

                } else {
                    Const.TYPE_ID = topicList.get(position).full_length_type_id;
                    spinner_topic.setSelection(spinner_review.getSelectedItemPosition());
                    drawerLayout.closeDrawer(drawerView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Added Later
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submitting answers, please wait...");
        builder.setMessage("Answers to the questions you attempted while being offline are submitted");
        mOfflineAttemptsDialogue = builder.create();
        mOfflineAttemptsDialogue.setCancelable(false);

        rl_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customCountDownTimer.isRunning()) {
                    customCountDownTimer.pause();
                    viewPager.setPagingEnabled(false);
                    iv_play.setImageResource(R.drawable.ic_timer_pause);
                } else {
                    customCountDownTimer.resume();
                    viewPager.setPagingEnabled(true);
                    iv_play.setImageResource(R.drawable.ic_timer_play);
                }
            }
        });
    }

    public void submitOfflineAnswersAndFinish(String testId, String questId, String ansId) {
        ApiCallManager.getInstance(this).callSubmitAnswerAPI(testId, questId, ansId, new ApiCallManager.ApiResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (attemptedOfflineQuestions.size() > 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Hashtable<String, String> attempt = attemptedOfflineQuestions.get(attemptedOfflineQuestions.size() - 1);
                            submitOfflineAnswersAndFinish(attempt.get("test_id"), attempt.get("qus_id"), attempt.get("answers_id"));
                            attemptedOfflineQuestions.remove(attempt);
                        }
                    }, 500);
                } else {
                    mOfflineAttemptsDialogue.dismiss();
                    callFinishTestAPI();
                }
            }

            @Override
            public void onError(VolleyError error) {
                answerLoad.setVisibility(View.GONE);
                Log.e(TAG, error.getLocalizedMessage());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (attemptedOfflineQuestions.size() > 0) {
                            Hashtable<String, String> attempt = attemptedOfflineQuestions.get(attemptedOfflineQuestions.size() - 1);
                            submitOfflineAnswersAndFinish(attempt.get("test_id"), attempt.get("qus_id"), attempt.get("answers_id"));
                            attemptedOfflineQuestions.remove(attempt);
                        }
                    }
                }, 500);
            }
        });
    }

    private void nextQuestion() {
        if (submitButton.getText().toString().equalsIgnoreCase("Save")) {
            submitButton.setVisibility(View.GONE);
            if (spinner_topic.getSelectedItemPosition() == Const.QUIZ_LENGTH - 1) {

            } else {
                submitButton.setText("Save & Next");
                spinner_topic.setSelection(spinner_topic.getSelectedItemPosition() + 1);
            }
        }
        if (submitButton.getText().toString().equalsIgnoreCase("Save & Next")) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private void initData() {
        quiz_id = getIntent().getStringExtra("quiz_id");
        totalTime = getIntent().getStringExtra("time");
        wasPaused = getIntent().getBooleanExtra("was_paused", false);
    }

    private void callTopicsAPI() {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_LENGTH_QUIZ_TOPIC, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject topicJSON = new JSONObject(response);
//                    AppPreferenceManager.saveExamTopic(topicJSON);
                    parseExamTopicResponse(topicJSON);
                    setTopicUI();
                    if (wasPaused) {
                        resumeWithSavedTestState();
                    } else {
                        startChangebleTimer(Float.parseFloat(getIntent().getStringExtra("time")));
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("quiz_id", quiz_id);
                params.put("student_id", "6");
                params.put("start_time", currentTime);
                params.put("remaining_time", "0");
                Log.d("FullLength", "getParams: " + params);
                return params;
            }
        };

        AppWebService.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void parseExamTopicResponse(JSONObject object) throws JSONException {
        Log.d("FullResponse", "onResponse: " + object);
        String status = object.getString("status_code");
        String message = object.getString("message");
        if (status.equalsIgnoreCase("200")) {
            JSONObject msgObject = object.getJSONObject("message");
            Const.STUDENT_TEST_ID = msgObject.getString("student_test_id");
            JSONArray jsonArray = msgObject.getJSONArray("quiz_dtls");
            topicList.clear();
            strings.clear();
            FullTopicTest fullTopicTest;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = jsonArray.getJSONObject(i);
                fullTopicTest = new FullTopicTest(object1.getString("type_id"), object1.getString("type_name"),
                        object1.getString("duration"), object1.getString("total_question"),
                        object1.getString("count"));

                topicList.add(fullTopicTest);
                strings.add(topicList.get(i).full_length_type_name);
            }


        } else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            spinner_topic.setVisibility(View.GONE);

        }
    }

    private void setTopicUI() {
        adapter = new ArrayAdapter<String>(this, R.layout.free_spinner_layout, strings);
        spinner_topic.setAdapter(adapter);
        spinner_topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Const.TYPE_ID = topicList.get(position).full_length_type_id;
                Const.TYPE_NAME = topicList.get(position).full_length_type_name;

                if (submitButton.getVisibility() == View.VISIBLE) {
                    submitButton.setVisibility(View.GONE);
                    submitButton.setText("Save & Next");
                }
                rl_ques.setVisibility(View.GONE);

                if (InternetCheck.isInternetOn(FullTestQuizActivity.this)) {
                    callFullChangeTestQuesAPI();
                } else {
                    Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void callFullChangeTestQuesAPI() {
        noDataImage.setVisibility(View.GONE);
        tryAgainText.setVisibility(View.GONE);
        markButton.setVisibility(View.GONE);
        //clearButton.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_progress_bar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_LENGTH_QUIZ_TOPIC_QUES, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    rl_top.setVisibility(View.VISIBLE);
                    rl_ques.setVisibility(View.VISIBLE);
                    markButton.setVisibility(View.VISIBLE);
                    //clearButton.setVisibility(View.VISIBLE);
                    rl_view_marked.setVisibility(View.GONE);
                    markButton.setText("Mark for Review");
                    //clearButton.setText("Clear Selection");
                    JSONObject examJSON = new JSONObject(response);
//                    AppPreferenceManager.saveExam(examJSON);
                    parseExamResponse(examJSON);
                    dialog.dismiss();
                } catch (JSONException e) {
                    noDataImage.setVisibility(View.VISIBLE);
                    tryAgainText.setVisibility(View.VISIBLE);
                    dialog.dismiss();

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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("quiz_id", quiz_id);
                params.put("type_id", Const.TYPE_ID);
                Log.d("Quiz", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppWebService.getInstance(this).addToRequestQueue(request);

    }

    private void parseExamResponse(JSONObject jsonObject) throws JSONException {
        String status = jsonObject.getString("status_code");
        String message = jsonObject.getString("message");
        if (status.equalsIgnoreCase("200")) {
            JSONArray messageJsonArray = jsonObject.getJSONArray("message");
            Log.d("Length", "onResponse: " + messageJsonArray.length());
            quesList = new ArrayList<>();
            listHashMap = new HashMap<>();
            FullQuestionSetGet questionSetGet;
            AnswerSetGet answerSetGet;
            for (int i = 0; i < messageJsonArray.length(); i++) {
                JSONObject object1 = messageJsonArray.getJSONObject(i);
                questionSetGet = new FullQuestionSetGet();
                questionSetGet.setTest_question(object1.getString("question"));
                questionSetGet.setTest_question_id(object1.getString("question_id"));
                questionSetGet.setTest_directions(object1.getString("directions"));
                quesList.add(questionSetGet);
                JSONArray jsonArray1 = object1.getJSONArray("answer_dtls");
                ansList = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject object2 = jsonArray1.getJSONObject(j);
                    answerSetGet = new AnswerSetGet();
                    answerSetGet.setGoal_answers_id(object2.getString("answer_id"));
                    answerSetGet.setGoal_answer(object2.getString("answer"));
                    ansList.add(answerSetGet);
                }
                listHashMap.put(questionSetGet.getTest_question_id(), ansList);
            }

            setQuizPagerUI(messageJsonArray);

        } else {
            noDataImage.setVisibility(View.VISIBLE);
            tryAgainText.setVisibility(View.VISIBLE);
            rl_top.setVisibility(View.GONE);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    private void setQuizPagerUI(JSONArray messageJsonArray) {
        FullQuizTestPageAdapter quizTestPageAdapter = new FullQuizTestPageAdapter(getApplicationContext(), quesList, listHashMap, submitButton/*,clearButton*/);
        viewPager.setAdapter(quizTestPageAdapter);
        totalQuestion.setText(1 + "/" + listHashMap.size());
        if (Const.hashMapMarkSelected.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
            markButton.setText("Mark for\n Review");
            rl_view_marked.setVisibility(View.VISIBLE);
        } else if (Const.hashMapSelectMarkReview.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
            markButton.setText("Mark for\n Review");
            rl_view_marked.setVisibility(View.VISIBLE);
        }/*else if (Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())){
                            clearButton.setText("Clear\n Selection");
                            clearButton.setTextColor(Color.parseColor("#FFFFFF"));
                        }*/ else {
            markButton.setText("Mark for Review");
            rl_view_marked.setVisibility(View.GONE);
            // clearButton.setText("Clear Selection");
            //  clearButton.setTextColor(Color.parseColor("#B9BBBB"));
        }
        Const.TOTAL_QUIZ_QUES = String.valueOf(messageJsonArray.length());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("UseSparseArrays")
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (Const.hashMapMarkSelected.containsKey(quesList.get(position).getTest_question_id())) {
                        markButton.setText("Mark for\n Review");
                        rl_view_marked.setVisibility(View.VISIBLE);
                    } else if (Const.hashMapSelectMarkReview.containsKey(quesList.get(position).getTest_question_id())) {
                        markButton.setText("Mark for\n Review");
                        rl_view_marked.setVisibility(View.VISIBLE);
                    }/*else if (Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())){
                                        clearButton.setText("Clear\n Selection");
                                        clearButton.setTextColor(Color.parseColor("#FFFFFF"));
                                    }*/ else {
                        markButton.setText("Mark for Review");
                        rl_view_marked.setVisibility(View.GONE);
                        //clearButton.setText("Clear Selection");
                        //clearButton.setTextColor(Color.parseColor("#B9BBBB"));
                    }

                } else {
                    if (Const.hashMapMarkSelected.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
                        markButton.setText("Mark for\n Review");
                        rl_view_marked.setVisibility(View.VISIBLE);
                    } else if (Const.hashMapSelectMarkReview.containsKey(quesList.get(position).getTest_question_id())) {
                        markButton.setText("Mark for\n Review");
                        rl_view_marked.setVisibility(View.VISIBLE);
                    }/*else if (Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())){
                                        clearButton.setText("Clear\n Selection");
                                        clearButton.setTextColor(Color.parseColor("#FFFFFF"));
                                    }*/ else {
                        markButton.setText("Mark for Review");
                        rl_view_marked.setVisibility(View.GONE);
                        //clearButton.setText("Clear Selection");
                        //clearButton.setTextColor(Color.parseColor("#B9BBBB"));

                    }
                }

                if (position + 1 == listHashMap.size()) {
                    submitButton.setText("Save");//& Finish
                } else {
                    submitButton.setText("Save & Next");
                }

                totalQuestion.setText(position + 1 + "/" + listHashMap.size());
                submitButton.setVisibility(View.GONE);
                Const.QUES_STATUS = "0";
                //clearButton.setEnabled(false);
                // clearButton.setTextColor(Color.parseColor("#B9BBBB"));

                //clearButton.setBackgroundResource(R.drawable.disable_back_gradient);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        gridReviewAdapter = new BigGridReviewAdapter(context, quesList);
        question_gridView.setAdapter(gridReviewAdapter);

        listReviewAdapter = new BigRecycleReviewAdapter(context, quesList);
        question_listView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
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

        if (wasPaused) {
            String index = AppPreferenceManager.getQuizPageIndex(quiz_id);
            viewPager.setCurrentItem(Integer.parseInt(index));
        }

    }

    /*============================================Save Answer===========================================*/
    private void callSubmitAnswerAPI() {
        answerLoad.setVisibility(View.VISIBLE);
        answerLoad.setClickable(false);
        answerLoad.setIndeterminate(true);
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.SAVE_FULL_ANS, new Response.Listener<String>() {
            @SuppressLint("SimpleDateFormat")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status_code");
                    String message = jsonObject.getString("message");
                    Log.e("Result Status", status);
                    if (status.equalsIgnoreCase("200")) {
                        answerLoad.setVisibility(View.GONE);
                        nextQuestion();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "Time out error, try again later", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong, try again later", Toast.LENGTH_LONG).show();
                }
                Hashtable<String, String> params = new Hashtable<>();
                params.put("test_id", Const.STUDENT_TEST_ID);
                params.put("qus_id", Const.CHOOSE_QUESTION_ID);
                params.put("answers_id", Const.ANSWER_ID);
                params.put("question_status", "1");
                attemptedOfflineQuestions.add(params);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.STUDENT_TEST_ID);
                params.put("qus_id", Const.CHOOSE_QUESTION_ID);
                params.put("answers_id", Const.ANSWER_ID);
                params.put("question_status", "1");
                Log.d("SubmitFullAnswerValue", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(this).addToRequestQueue(request);

    }


    private void resumeWithSavedTestState() {
        String resumeTime = AppPreferenceManager.getQuizPauseTime(quiz_id);
        String strHours = resumeTime.split(":")[0];
        String strMins = resumeTime.split(":")[1];
        String strSecs = resumeTime.split(":")[2];
        float actualTimeLeft = Float.parseFloat(strHours) * 60f + Float.parseFloat(strMins) + Float.parseFloat(strSecs) / 60f;
        startChangebleTimer(actualTimeLeft);
        ArrayList<Hashtable<String, String>> offlineAttempts = AppPreferenceManager.getOfflineAttemptState(quiz_id);
        if (offlineAttempts != null)
            attemptedOfflineQuestions.addAll(offlineAttempts);
    }

    //Timer Changed
    private void startChangebleTimer(final float minute) {
        customCountDownTimer = new CustomCountDownTimer((long) (60 * minute * 1000), 500, true) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tv_total_time.setText(String.format("%02d", seconds / 3600) + ":" + String.format("%02d", (seconds % 3600) / 60) + ":" + String.format("%02d", seconds % 60));
            }

            @Override
            public void onFinish() {
                if (tv_total_time.getText().equals("00:00:00")) {
                    if (submitButton.getVisibility() == View.VISIBLE) {
                        submitButton.setVisibility(View.GONE);
                        submitButton.setText("Save & Next");
                    }

                    if (attemptedOfflineQuestions.isEmpty()) {
                        customCountDownTimer.cancel();
                        callFinishTestAPI();
                    } else {
                        Hashtable<String, String> attempt = attemptedOfflineQuestions.get(attemptedOfflineQuestions.size() - 1);
                        mOfflineAttemptsDialogue.show();
                        submitOfflineAnswersAndFinish(attempt.get("test_id"), attempt.get("qus_id"), attempt.get("answers_id"));
                        attemptedOfflineQuestions.remove(attempt);
                    }
                }
            }
        };
        customCountDownTimer.create();
    }


    public void showFinishTestDialogue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit alert");
        builder.setMessage("Are you sure to finish the test?");
        builder.setIcon(R.drawable.ic_info);
        builder.setCancelable(false);
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                c = Calendar.getInstance();
                df = new SimpleDateFormat("HH:mm:ss");
                Const.END_TIME = df.format(c.getTime());

                if (customCountDownTimer != null) {
                    customCountDownTimer.cancel();
                    customCountDownTimer = null;
                }

                if (!attemptedOfflineQuestions.isEmpty()) {
                    mOfflineAttemptsDialogue.show();
                    Hashtable<String, String> attempt = attemptedOfflineQuestions.get(attemptedOfflineQuestions.size() - 1);
                    submitOfflineAnswersAndFinish(attempt.get("test_id"), attempt.get("qus_id"), attempt.get("answers_id"));
                    attemptedOfflineQuestions.remove(attempt);
                } else {
                    callFinishTestAPI();
                }

            }
        });
        builder.setNegativeButton("I'm just kidding", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    //Renamed
    private void callFinishTestAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_SECTION_MARK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            Log.d("MarkSection", "onResponse: " + status);

                            if (status.equalsIgnoreCase("203")) {
                                startActivity(new Intent(context, ResultPannelActivity.class));
                                Const.answerStoreHash.clear();
                                Const.answerQuestionStoreHash.clear();
                                Const.questionAnswerStoreHash.clear();
                                Const.hashMapSelected.clear();
                                Const.hashMapMarkSelected.clear();
                                Const.hashMapSelectMarkReview.clear();
                                Const.answerCheckHash.clear();
                                AppPreferenceManager.deleteTestState(quiz_id);
                                finish();
                            }
                            progressDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UtilFunctions.showToast("Test submition failed, try again");
                progressDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.TEST_ID);
                params.put("student_id", "6");
                Log.d("FinishTest", "getParams: " + params);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(drawerView)) {
            drawerLayout.closeDrawer(drawerView);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit alert");
            builder.setMessage("Are you sure to quit? The test will be paused.");
            builder.setIcon(R.drawable.ic_info);
            builder.setCancelable(false);
            builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    c = Calendar.getInstance();
                    df = new SimpleDateFormat("HH:mm:ss");
                    Const.END_TIME = df.format(c.getTime());

                    if (customCountDownTimer != null) {
                        customCountDownTimer.cancel();
                        customCountDownTimer = null;
                    }

                    saveTestState();
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

    }

    private void saveTestState() {
        String timePaused = tv_total_time.getText().toString().trim();
        AppPreferenceManager.addQuizState(quiz_id, timePaused, String.valueOf(viewPager.getCurrentItem()));
        AppPreferenceManager.saveOfflineAttemptStates(quiz_id, attemptedOfflineQuestions);
    }

}
