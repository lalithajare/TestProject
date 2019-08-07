package com.example.testproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testproject.Adapter.BigGridReviewAdapter;
import com.example.testproject.Adapter.BigRecycleReviewAdapter;
import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.R;
import com.example.testproject.Utils.AppPreferenceManager;
import com.example.testproject.Utils.Const;
import com.example.testproject.Utils.CustomCountDownTimer;
import com.example.testproject.Utils.InternetCheck;
import com.example.testproject.Utils.QuizOfflineStateHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;


public class FullTestQuizActivity extends ParentQuizActivity {


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TAG = FullTestQuizActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);

        mContext = FullTestQuizActivity.this;

        spinner_topic.setEnabled(true);
        spinner_review.setEnabled(true);

        pref = mContext.getSharedPreferences("HashValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        setViews();

    }

    @Override
    protected void setViews() {
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailsResponseSchema questionDetailsResponseSchema = quesList.get(viewPager.getCurrentItem());
                questionDetailsResponseSchema.setMarked(true);
                markButton.setVisibility(View.GONE);

                saveCurrentQuizState(true);
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
                adapter = new ArrayAdapter<String>(FullTestQuizActivity.this, R.layout.review_spinner_layout, strings);
                spinner_review.setAdapter(adapter);
                spinner_review.setSelection(spinner_topic.getSelectedItemPosition());

                gridReviewAdapter = new BigGridReviewAdapter(mContext, quesList);
                question_gridView.setAdapter(gridReviewAdapter);

                listReviewAdapter = new BigRecycleReviewAdapter(mContext, quesList);
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

        if (mQuizOfflineStateHandler == null) {
            //Load all the questions for Quiz section-wise
            //Then show only the questions related to Topic currently selected
            getQuizData();
        } else {
            //Show only the questions related to Topic currently selected
            bindQuestionsToTopic();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customCountDownTimer.isRunning()) {
                    saveCurrentQuizState(false);
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

    //Timer Changed
    @Override
    protected void startChangebleTimer(final float minute) {
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
                        if (customCountDownTimer != null) {
                            customCountDownTimer.cancel();
                            customCountDownTimer = null;
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callFinishTestAPI();
                            }
                        }, 500);
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

                    dialog.dismiss();

                    c = Calendar.getInstance();
                    df = new SimpleDateFormat("HH:mm:ss");
                    Const.END_TIME = df.format(c.getTime());

                    if (customCountDownTimer != null) {
                        customCountDownTimer.cancel();
                        customCountDownTimer = null;
                    }

                    if (quesList != null && !quesList.isEmpty()) {
                        saveCurrentTopic();
                        saveTopicState();
                        saveOfflineAttempts();
                        saveQuiz(new QuizOfflineStateHandler.QuizSaveListener() {
                            @Override
                            public void onQuizSaved() {
                                finish();
                            }
                        });
                    } else {
                        finish();
                    }
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

}
