package com.example.testproject.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
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
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import com.example.testproject.Utils.InternetCheck;

import java.util.Hashtable;
import java.util.Objects;

public class FullNotChangeTestQuizActivity extends ParentQuizActivity {

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = FullTestQuizActivity.class.getSimpleName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_test_quiz);
        mContext = FullNotChangeTestQuizActivity.this;
        spinner_topic.setEnabled(false);
        spinner_review.setEnabled(false);

    }

    @Override
    protected void setViews() {
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

                adapter = new ArrayAdapter<String>(FullNotChangeTestQuizActivity.this, R.layout.review_spinner_layout, strings);
                spinner_review.setAdapter(adapter);
                spinner_review.setSelection(spinner_topic.getSelectedItemPosition());

                gridReviewAdapter = new BigGridReviewAdapter(FullNotChangeTestQuizActivity.this, quesList);
                question_gridView.setAdapter(gridReviewAdapter);

                listReviewAdapter = new BigRecycleReviewAdapter(FullNotChangeTestQuizActivity.this, quesList);
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

        if (InternetCheck.isInternetOn(FullNotChangeTestQuizActivity.this)) {
            callTopicsAPI();
        } else {
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customCountDownTimer.isRunning()) {
                    if (InternetCheck.isInternetOn(FullNotChangeTestQuizActivity.this)) {
                        if (markButton.getText().toString().equalsIgnoreCase("Mark for\n Review")
                                && Const.answerStoreHash.containsKey(quesList.get(viewPager.getCurrentItem()).getTest_question_id())) {
                            Const.hashMapSelectMarkReview.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                            Const.hashMapSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                            Const.hashMapMarkSelected.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());

                        } else {
                            Const.hashMapSelectMarkReview.remove(quesList.get(viewPager.getCurrentItem()).getTest_question_id());
                            Const.hashMapSelected.put(quesList.get(viewPager.getCurrentItem()).getTest_question_id(), true);
                        }
                        callSubmitAnswerAPI();
                    } else {
                        Hashtable<String, String> params = new Hashtable<>();
                        params.put("test_id", Const.STUDENT_TEST_ID);
                        params.put("qus_id", Const.CHOOSE_QUESTION_ID);
                        params.put("answers_id", Const.ANSWER_ID);
                        params.put("question_status", "1");
                        attemptedOfflineQuestions.add(params);
                        nextQuestion();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "The Test is Paused, please resume it.", Toast.LENGTH_LONG).show();
                }

            }
        });

        spinner_review.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


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
}