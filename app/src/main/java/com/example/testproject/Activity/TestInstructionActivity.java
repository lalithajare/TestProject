package com.example.testproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testproject.Adapter.TestPatternAdapter;
import com.example.testproject.Model.TestPattern;
import com.example.testproject.R;

import java.util.ArrayList;

import static com.example.testproject.Utils.Const.patternArrayList;

public class TestInstructionActivity extends AppCompatActivity {
    TextView tv_total_ques, tv_total_time, tv_total_mark, tv_mode_change, tv_mode_msg;
    RecyclerView rv_pattern_name;
    Button btn_start_test;
    TestPatternAdapter testPatternAdapter;
    Context context;
    WebView wv_instruction;
    Spinner spinner_lang;
    private Animation mShakeAnimation;
    Toolbar toolbar;
    TextView toolbar_title;
    LinearLayout ll_below_start_test;
    PopupWindow popupWindow;
    RelativeLayout relativeLayout;
    Button btn_save_test;
    RadioGroup rg_mode;
    RadioButton rb_mode_a, rb_mode_b;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    boolean wasPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_instruction);
        context = TestInstructionActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getIntent().getStringExtra("quiz_name"));
        tv_total_ques = findViewById(R.id.tv_total_ques);
        tv_total_time = findViewById(R.id.tv_total_time);
        tv_total_mark = findViewById(R.id.tv_total_mark);
        wv_instruction = findViewById(R.id.wv_instruction);
        rv_pattern_name = findViewById(R.id.rv_pattern_name);
        btn_start_test = findViewById(R.id.btn_start_test);
        spinner_lang = findViewById(R.id.spinner_lang);
        tv_mode_change = findViewById(R.id.tv_mode_change);
        relativeLayout = findViewById(R.id.relativeLayout);
        tv_mode_msg = findViewById(R.id.tv_mode_msg);
        ll_below_start_test = findViewById(R.id.ll_below_start_test);
        testPatternAdapter = new TestPatternAdapter(context, patternArrayList);
        rv_pattern_name.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
        rv_pattern_name.setAdapter(testPatternAdapter);
        wasPaused = getIntent().getBooleanExtra("was_paused", false);
        tv_total_ques.setText(getIntent().getStringExtra("total_question"));
        tv_total_time.setText(getIntent().getStringExtra("total_time"));
        tv_total_mark.setText(getIntent().getStringExtra("total_marks"));
        wv_instruction.loadDataWithBaseURL(null, getIntent().getStringExtra("direction"), "text/html", "utf-8", null);
        mShakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        btn_start_test.startAnimation(mShakeAnimation);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pref = context.getSharedPreferences("testModeValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        btn_start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_mode_msg.getText().toString().equalsIgnoreCase("Mode A (Actual Exam Setting)")) {
                    if (getIntent().getStringExtra("changable").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, FullTestQuizActivity.class);
                        intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                        intent.putExtra("was_paused", wasPaused);
                        intent.putExtra("quiz_name", getIntent().getStringExtra("quiz_name"));
                        intent.putExtra("time", getIntent().getStringExtra("total_time"));
                        intent.putExtra("changable", getIntent().getStringExtra("changable"));
                        intent.putExtra("remain_time", getIntent().getStringExtra("remain_time"));
                        intent.putExtra("total_quiz_question", getIntent().getStringExtra("total_question"));
                        startActivity(intent);
                        finish();
                        editor.putString("mode_change", "modeChangable");
                        editor.apply();

                    } else {
                        Intent intent = new Intent(context, FullNotChangeTestQuizActivity.class);
                        intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                        intent.putExtra("was_paused", wasPaused);
                        intent.putExtra("quiz_name", getIntent().getStringExtra("quiz_name"));
                        intent.putExtra("time", getIntent().getStringExtra("total_time"));
                        intent.putExtra("changable", getIntent().getStringExtra("changable"));
                        intent.putExtra("remain_time", getIntent().getStringExtra("remain_time"));
                        intent.putExtra("total_quiz_question", getIntent().getStringExtra("total_question"));
                        startActivity(intent);
                        finish();
                        editor.putString("mode_change", "modeNotchangable");
                        editor.apply();
                    }
                } else {
                    Intent intent = new Intent(context, FullTestQuizActivity.class);
                    intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                    intent.putExtra("was_paused", wasPaused);
                    intent.putExtra("quiz_name", getIntent().getStringExtra("quiz_name"));
                    intent.putExtra("time", getIntent().getStringExtra("total_time"));
                    intent.putExtra("changable", getIntent().getStringExtra("changable"));
                    intent.putExtra("total_quiz_question", getIntent().getStringExtra("total_question"));
                    intent.putExtra("remain_time", getIntent().getStringExtra("remain_time"));
                    startActivity(intent);
                    finish();
                    editor.putString("mode_change", "modeChangable");
                    editor.apply();
                }
            }
        });
        ll_below_start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_mode_msg.getText().toString().equalsIgnoreCase("Mode A (Actual Exam Setting)")) {
                    if (getIntent().getStringExtra("changable").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, FullTestQuizActivity.class);
                        intent.putExtra("was_paused", wasPaused);
                        intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                        intent.putExtra("quiz_name", getIntent().getStringExtra("quiz_name"));
                        intent.putExtra("time", getIntent().getStringExtra("total_time"));
                        intent.putExtra("changable", getIntent().getStringExtra("changable"));
                        intent.putExtra("total_quiz_question", getIntent().getStringExtra("total_question"));
                        startActivity(intent);
                        finish();

                    } else {
                        Intent intent = new Intent(context, FullNotChangeTestQuizActivity.class);
                        intent.putExtra("was_paused", wasPaused);
                        intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                        intent.putExtra("quiz_name", getIntent().getStringExtra("quiz_name"));
                        intent.putExtra("time", getIntent().getStringExtra("total_time"));
                        intent.putExtra("changable", getIntent().getStringExtra("changable"));
                        intent.putExtra("total_quiz_question", getIntent().getStringExtra("total_question"));
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(context, FullTestQuizActivity.class);
                    intent.putExtra("was_paused", wasPaused);
                    intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                    intent.putExtra("quiz_name", getIntent().getStringExtra("quiz_name"));
                    intent.putExtra("time", getIntent().getStringExtra("total_time"));
                    intent.putExtra("changable", getIntent().getStringExtra("changable"));
                    intent.putExtra("total_quiz_question", getIntent().getStringExtra("total_question"));
                    startActivity(intent);
                    finish();
                }

            }
        });

        if (getIntent().getStringExtra("changable").equalsIgnoreCase("1")) {
            tv_mode_change.setVisibility(View.GONE);

        } else {
            tv_mode_change.setVisibility(View.VISIBLE);

        }


        tv_mode_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                View customView = layoutInflater.inflate(R.layout.test_instruction_popup, null, false);
                popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
                rg_mode = customView.findViewById(R.id.rg_mode);
                rb_mode_a = customView.findViewById(R.id.rb_mode_a);
                rb_mode_b = customView.findViewById(R.id.rb_mode_b);
                btn_save_test = customView.findViewById(R.id.btn_save_test);
                if (tv_mode_msg.getText().toString().equalsIgnoreCase("Mode A (Actual Exam Setting)")) {
                    tv_mode_msg.setText(getResources().getString(R.string.modeAMsg));
                    rb_mode_a.setChecked(true);
                    rb_mode_b.setChecked(false);

                } else {
                    rb_mode_b.setChecked(true);
                    rb_mode_a.setChecked(false);

                }
                rg_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        switch (checkedId) {
                            case R.id.rb_mode_a:
                                // tv_mode_msg.setText(getResources().getString(R.string.modeAMsg));
                                break;
                            case R.id.rb_mode_b:
                                //Toast.makeText(mContext, "ModeB", Toast.LENGTH_SHORT).show();
                                //tv_mode_msg.setText(getResources().getString(R.string.modeBMsg));

                                break;

                        }

                    }
                });
                btn_save_test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rb_mode_a.isChecked()) {
                            popupWindow.dismiss();
                            tv_mode_msg.setText(getResources().getString(R.string.modeAMsg));

                        } else {
                            rb_mode_b.setChecked(true);
                            rb_mode_a.setChecked(false);
                            popupWindow.dismiss();
                            tv_mode_msg.setText(getResources().getString(R.string.modeBMsg));

                        }
                    }
                });
                popupWindow.getContentView().setFocusableInTouchMode(true);
                popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                dimBehind(popupWindow);

            }
        });
    }

    private static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        assert wm != null;
        wm.updateViewLayout(container, p);
    }
}
