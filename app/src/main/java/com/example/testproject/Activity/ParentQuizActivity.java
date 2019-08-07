package com.example.testproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.testproject.Model.FullTopicTest;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.Model.QuizAllQuestionTopicWiseResponseSchema;
import com.example.testproject.Model.TopicResponseSchema;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppPreferenceManager;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;
import com.example.testproject.Utils.CustomCountDownTimer;
import com.example.testproject.Utils.CustomViewPager;
import com.example.testproject.Utils.InternetCheck;
import com.example.testproject.Utils.QuizOfflineStateHandler;
import com.example.testproject.Utils.UtilFunctions;
import com.example.testproject.common.ApiCallManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

abstract public class ParentQuizActivity extends AppCompatActivity {

    protected ImageView noDataImage;
    protected TextView tryAgainText;
    protected Button finishButton, submitButton, markButton;
    protected ProgressBar answerLoad;
    protected TextView totalQuestion;
    protected SimpleDateFormat df;
    protected Calendar c;
    protected String currentTime;

    protected AppCompatActivity mContext;
    protected ArrayList<QuestionDetailsResponseSchema> quesList;
    protected RelativeLayout rl_top;
    protected ImageButton iv_review, iv_close;
    protected BigGridReviewAdapter gridReviewAdapter;
    protected BigRecycleReviewAdapter listReviewAdapter;
    protected GridView question_gridView;

    protected DrawerLayout drawerLayout;
    protected View drawerView;
    protected RecyclerView question_listView;
    protected ImageButton btn_grid, btn_list;
    protected RelativeLayout rl_grid, rl_list, rl_ques;
    protected Switch switch_btn;
    protected TextView tv_eng, tv_hindi;
    protected Spinner spinner_topic, spinner_review;
    protected ArrayList<FullTopicTest> topicList = new ArrayList<>();
    protected ArrayList<String> strings = new ArrayList<>();
    protected ArrayAdapter<String> adapter;
    protected RelativeLayout rl_close, rl_view_marked;
    protected TextView tv_total_time;
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;

    //Added Later
    protected String quiz_id;
    protected String totalTime;
    protected Boolean wasPaused;
    protected CustomViewPager viewPager;
    protected AlertDialog mOfflineAttemptsDialogue;
    protected RelativeLayout rl_timer;
    protected ImageView iv_play;
    protected CustomCountDownTimer customCountDownTimer;
    protected ArrayList<Hashtable<String, String>> attemptedOfflineQuestions = new ArrayList<>();
    protected Button test_finish_button;
    protected String TAG = ParentQuizActivity.class.getSimpleName();

    protected QuizOfflineStateHandler mQuizOfflineStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_test_quiz);
        initData();
        initViews();
    }

    private void initData() {
        quiz_id = getIntent().getStringExtra("quiz_id");
        totalTime = getIntent().getStringExtra("time");
        wasPaused = getIntent().getBooleanExtra("was_paused", false);

        //SET CURRENT TIME
        c = Calendar.getInstance();
        df = new SimpleDateFormat("HH:mm:ss");
        currentTime = df.format(c.getTime());
    }

    private void initViews() {
        //Added Later
        rl_timer = findViewById(R.id.rl_timer);
        iv_play = findViewById(R.id.iv_play);
        test_finish_button = findViewById(R.id.test_finish_button);

        answerLoad = findViewById(R.id.ans_load);
        tv_total_time = findViewById(R.id.tv_total_time);
        iv_review = findViewById(R.id.iv_review);
        totalQuestion = findViewById(R.id.text_no_of_question);
        finishButton = findViewById(R.id.test_finish_button);
        submitButton = findViewById(R.id.submit_ans);
        markButton = findViewById(R.id.pre_ans);
        viewPager = findViewById(R.id.viewpager);
        noDataImage = findViewById(R.id.no_data_image);
        tryAgainText = findViewById(R.id.try_again_text);
        markButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
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
        rl_top = findViewById(R.id.rl_top);
        rl_close = findViewById(R.id.rl_close);

        test_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFinishTestDialogue();
            }
        });

    }

    protected abstract void setViews();

    protected void submitOfflineAnswersAndFinish(String testId, String questId, String ansId) {
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callFinishTestAPI();
                        }
                    }, 500);
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

    private void setTopicsUI() {

        QuizAllQuestionTopicWiseResponseSchema quizAllQuestionTopicWiseResponseSchema = mQuizOfflineStateHandler.getmQuizAllQuestionTopicWiseResponseSchema();
        for (TopicResponseSchema topicResponseSchema : quizAllQuestionTopicWiseResponseSchema.getTopicResonseSchema()) {
            FullTopicTest fullTopicTest = new FullTopicTest(topicResponseSchema.getSectionId()
                    , topicResponseSchema.getSectionName()
                    , topicResponseSchema.getDuration()
                    , String.valueOf(topicResponseSchema.getQuestionArrayList().size())
                    , String.valueOf(topicResponseSchema.getQuestionArrayList().size()));
            topicList.add(fullTopicTest);
            strings.add(fullTopicTest.full_length_type_name);
        }

        adapter = new ArrayAdapter(ParentQuizActivity.this, R.layout.free_spinner_layout, strings);
        spinner_topic.setAdapter(adapter);
        spinner_topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (quesList != null && !quesList.isEmpty())
                    saveTopicState();

                Const.TYPE_ID = topicList.get(position).full_length_type_id;
                Const.TYPE_NAME = topicList.get(position).full_length_type_name;


                if (submitButton.getVisibility() == View.VISIBLE) {
                    submitButton.setVisibility(View.GONE);
                    submitButton.setText("Save & Next");
                }

                bindQuestionsToTopic();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected void getQuizData() {
        noDataImage.setVisibility(View.GONE);
        tryAgainText.setVisibility(View.GONE);
        markButton.setVisibility(View.GONE);
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_progress_bar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
        mQuizOfflineStateHandler = new QuizOfflineStateHandler(quiz_id, wasPaused, new QuizOfflineStateHandler.QuestionsLoadedListener() {
            @Override
            public void OnQuestionsLoaded() {
                dialog.dismiss();
                topicList.clear();
                strings.clear();
                if (wasPaused) {
                    resumeTime();
                    getSavedOfflineAttempts();
                } else {
                    startChangebleTimer(Float.parseFloat(getIntent().getStringExtra("time")));
                }
//                bindQuestionsToTopic();
                setTopicsUI();
            }

            @Override
            public void OnQuestionsFailed(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError)
                    Log.e("Time Out ", "error");
                noDataImage.setVisibility(View.VISIBLE);
                tryAgainText.setVisibility(View.VISIBLE);
            }
        });
    }

    protected void bindQuestionsToTopic() {

        rl_top.setVisibility(View.VISIBLE);
        rl_ques.setVisibility(View.VISIBLE);
        markButton.setVisibility(View.VISIBLE);
        rl_view_marked.setVisibility(View.GONE);

        quesList = new ArrayList<>();

        ArrayList<TopicResponseSchema> topicResponseSchemas = mQuizOfflineStateHandler.getmQuizAllQuestionTopicWiseResponseSchema().getTopicResonseSchema();
        for (TopicResponseSchema topicResponseSchema : topicResponseSchemas) {
            if (TextUtils.equals(topicResponseSchema.getSectionId(), Const.TYPE_ID)) {
                quesList.addAll(topicResponseSchema.getQuestionArrayList());
                break;
            }
        }
        Const.TOTAL_QUIZ_QUES = String.valueOf(quesList.size());
        setQuizPagerUI();
    }

    protected void setQuizPagerUI() {
        FullQuizTestPageAdapter quizTestPageAdapter = new FullQuizTestPageAdapter(getApplicationContext(), quesList, submitButton/*,clearButton*/);
        viewPager.setAdapter(quizTestPageAdapter);
        totalQuestion.setText(1 + "/" + quesList.size());
        setMarkForReviewUI(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("UseSparseArrays")
            @Override
            public void onPageSelected(int position) {

                setMarkForReviewUI(position);

                if (position + 1 == quesList.size()) {
                    submitButton.setText("Save");//Finish
                } else {
                    submitButton.setText("Save & Next");
                }
                totalQuestion.setText(position + 1 + "/" + quesList.size());
                submitButton.setVisibility(View.GONE);
                Const.QUES_STATUS = "0";
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        gridReviewAdapter = new BigGridReviewAdapter(mContext, quesList);
        question_gridView.setAdapter(gridReviewAdapter);

        listReviewAdapter = new BigRecycleReviewAdapter(mContext, quesList);
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


//        String index = AppPreferenceManager.getTopicPageIndex(quiz_id, Const.TYPE_ID);

        for (TopicResponseSchema topicResponseSchema : mQuizOfflineStateHandler.getmQuizAllQuestionTopicWiseResponseSchema().getTopicResonseSchema()) {
            if (TextUtils.equals(Const.TYPE_ID, topicResponseSchema.getSectionId())) {
                int index = topicResponseSchema.getTopicIndex();
                viewPager.setCurrentItem(index);
            }
        }

    }

    private void setMarkForReviewUI(int position) {
        if (quesList.get(position).isMarked()) {
            rl_view_marked.setVisibility(View.VISIBLE);
            markButton.setVisibility(View.GONE);
        } else {
            markButton.setVisibility(View.VISIBLE);
            rl_view_marked.setVisibility(View.GONE);
        }
    }

    protected void nextQuestion() {
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

    protected void callSubmitAnswerAPI() {
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

    protected void resumeTime() {
        String resumeTime = AppPreferenceManager.getQuizPauseTime(quiz_id);
        String strHours = resumeTime.split(":")[0];
        String strMins = resumeTime.split(":")[1];
        String strSecs = resumeTime.split(":")[2];
        float actualTimeLeft = Float.parseFloat(strHours) * 60f + Float.parseFloat(strMins) + Float.parseFloat(strSecs) / 60f;
        startChangebleTimer(actualTimeLeft);
    }

    protected void getSavedOfflineAttempts() {
        ArrayList<Hashtable<String, String>> offlineAttempts = AppPreferenceManager.getOfflineAttemptState(quiz_id);
        if (offlineAttempts != null && !offlineAttempts.isEmpty())
            attemptedOfflineQuestions.addAll(offlineAttempts);
    }

    //Timer Changed
    protected abstract void startChangebleTimer(final float minute);

    protected void showFinishTestDialogue() {
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callFinishTestAPI();
                        }
                    }, 500);
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
    public void callFinishTestAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_SECTION_MARK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            Log.d("MarkSection", "onResponse: " + status);

                            if (status.equalsIgnoreCase("203")) {
                                startActivity(new Intent(mContext, ResultPannelActivity.class));
                                AppPreferenceManager.deleteQuizState(quiz_id);
                                deleteQuizFromDB();
                                finish();
                            } else if (!TextUtils.isEmpty(message)) {
                                UtilFunctions.showToast(message);
                            }
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
        AppWebService.getInstance(mContext).addToRequestQueue(request);
    }

    protected void saveCurrentTopic() {
        for (TopicResponseSchema topicResponseSchema : mQuizOfflineStateHandler.getmQuizAllQuestionTopicWiseResponseSchema().getTopicResonseSchema()) {
            topicResponseSchema.setResumable(false);
            if (TextUtils.equals(Const.TYPE_ID, topicResponseSchema.getSectionId())) {
                topicResponseSchema.setTopicIndex(viewPager.getCurrentItem());
                topicResponseSchema.setResumable(true);
            }
        }
    }

    protected void saveTopicState() {
        String timePaused = tv_total_time.getText().toString().trim();
        AppPreferenceManager.addTopicState(quiz_id, Const.TYPE_ID, timePaused, String.valueOf(viewPager.getCurrentItem()));
    }

    protected void saveOfflineAttempts() {
        AppPreferenceManager.saveOfflineAttemptStates(quiz_id, attemptedOfflineQuestions);
    }

    protected void saveQuiz(QuizOfflineStateHandler.QuizSaveListener listener) {
        mQuizOfflineStateHandler.saveQuiz(listener);
    }

    protected void deleteQuizFromDB() {
        mQuizOfflineStateHandler.deleteQuizFromDB();
    }

    protected void saveCurrentQuizState(final boolean isMarkedForReview) {
        // "Mark for Review" is clicked
        QuestionDetailsResponseSchema questionDetailsResponseSchema = quesList.get(viewPager.getCurrentItem());
        questionDetailsResponseSchema.setAnswered(true);
        gridReviewAdapter.notifyDataSetChanged();
        listReviewAdapter.notifyItemChanged(viewPager.getCurrentItem());
        saveCurrentTopic();
        saveTopicState();
        saveQuiz(new QuizOfflineStateHandler.QuizSaveListener() {
            @Override
            public void onQuizSaved() {
                if (!isMarkedForReview) {
                    //Save answer in Local storage
                    AppPreferenceManager.addAnswer(quiz_id, Const.TYPE_ID, Const.CHOOSE_QUESTION_ID + AppPreferenceManager.DELIMITER + Const.ANSWER_ID);
                    if (InternetCheck.isInternetOn(ParentQuizActivity.this)) {
                        callSubmitAnswerAPI();
                        return;
                    } else {
                        Hashtable<String, String> params = new Hashtable<>();
                        params.put("test_id", Const.STUDENT_TEST_ID);
                        params.put("qus_id", Const.CHOOSE_QUESTION_ID);
                        params.put("answers_id", Const.ANSWER_ID);
                        params.put("question_status", "1");
                        attemptedOfflineQuestions.add(params);
                        saveOfflineAttempts();
                    }
                }
                nextQuestion();
            }
        });

    }
}
