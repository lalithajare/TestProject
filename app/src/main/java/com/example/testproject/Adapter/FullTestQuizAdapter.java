package com.example.testproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testproject.Activity.FullTestViewSolutionActivity;
import com.example.testproject.Activity.ResultPannelActivity;
import com.example.testproject.Activity.TestInstructionActivity;
import com.example.testproject.Model.TestQuizSetGet;
import com.example.testproject.R;
import com.example.testproject.Utils.AppPreferenceManager;
import com.example.testproject.Utils.Const;


import java.util.ArrayList;

public class FullTestQuizAdapter extends RecyclerView.Adapter<FullTestQuizAdapter.ViewHolder> {
    Context context;
    ArrayList<TestQuizSetGet> freeTests;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public FullTestQuizAdapter(Context context, ArrayList<TestQuizSetGet> freeTests) {
        this.context = context;
        this.freeTests = freeTests;
    }

    @NonNull
    @Override
    public FullTestQuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.custom_free_test_bottom_layout, parent, false);
        return new FullTestQuizAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FullTestQuizAdapter.ViewHolder holder, final int position) {

        pref = context.getSharedPreferences("testModeValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        holder.textView.setText(freeTests.get(position).getTest_quiz_name());
        holder.freeText_details.setText(freeTests.get(position).getTest_total_marks() + " Marks | " +
                freeTests.get(position).getTest_time() + " mins | " +
                freeTests.get(position).getTest_no_of_qs() + " Questions");

        if (freeTests.get(position).getTest_status().equalsIgnoreCase("2")) {
            holder.iv_lock.setBackgroundResource(R.drawable.ic_lock_icon);
            holder.btn_start_quiz.setText("Coming Soon");
            holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
            holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.resume_test_unlock_back_color));
        } else if (freeTests.get(position).getTest_status().equalsIgnoreCase("1") &&
                freeTests.get(position).getStudent_buy_plan_status().equalsIgnoreCase("1")) {
            holder.iv_lock.setVisibility(View.VISIBLE);

            if (freeTests.get(position).getTest_checked_attended().equalsIgnoreCase("0")) {
                holder.iv_lock.setBackgroundResource(R.drawable.ic_unlock_icon);
                holder.btn_start_quiz.setText("Start Test");
                holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
                holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.start_test_unlock_back_color));

            } else {
                holder.iv_lock.setBackgroundResource(R.drawable.ic_unlock_icon);
                holder.btn_start_quiz.setText("View Analysis");
                holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
                holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.unlock_back_color));

            }

        } else if (freeTests.get(position).getTest_status().equalsIgnoreCase("1")) {
            holder.iv_lock.setVisibility(View.VISIBLE);
            if (freeTests.get(position).getTest_checked_attended().equalsIgnoreCase("0")) {
                holder.iv_lock.setBackgroundResource(R.drawable.ic_unlock_icon);
                holder.btn_start_quiz.setText("Start Test");
                holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
                holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.start_test_unlock_back_color));

            } else {
                holder.iv_lock.setBackgroundResource(R.drawable.ic_unlock_icon);
                holder.btn_start_quiz.setText("View Analysis");
                holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
                holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.unlock_back_color));

            }
        } else if (freeTests.get(position).getStudent_buy_plan_status().equalsIgnoreCase("1")) {
            holder.iv_lock.setVisibility(View.VISIBLE);
            if (freeTests.get(position).getTest_checked_attended().equalsIgnoreCase("0")) {
                holder.iv_lock.setBackgroundResource(R.drawable.ic_unlock_icon);
                holder.btn_start_quiz.setText("Start Test");
                holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
                holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.start_test_unlock_back_color));

            } else {
                holder.iv_lock.setBackgroundResource(R.drawable.ic_unlock_icon);
                holder.btn_start_quiz.setText("View Analysis");
                holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
                holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.unlock_back_color));

            }
        } else {
            holder.iv_lock.setVisibility(View.VISIBLE);
            holder.iv_lock.setBackgroundResource(R.drawable.ic_lock_icon);
            holder.btn_start_quiz.setText("Unlock");
            holder.btn_start_quiz.setTextColor(context.getResources().getColor(R.color.white));
            holder.btn_start_quiz.setBackground(context.getResources().getDrawable(R.drawable.plan_button_selector));

        }

        if (AppPreferenceManager.isQuizStateExists(freeTests.get(position).getTest_quiz_id())) {
            holder.btn_resume_quiz.setVisibility(View.VISIBLE);
            holder.btn_start_quiz.setVisibility(View.GONE);
        } else {
            holder.btn_resume_quiz.setVisibility(View.GONE);
            holder.btn_start_quiz.setVisibility(View.VISIBLE);
        }


        holder.btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz(position, false);
            }
        });

        holder.btn_resume_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz(position, true);
            }
        });

    }

    private void startQuiz(int position, boolean wasPaused) {
        if (freeTests.get(position).getTest_status().equalsIgnoreCase("2")) {
            Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();

        } else if (freeTests.get(position).getTest_status().equalsIgnoreCase("1") &&
                freeTests.get(position).getStudent_buy_plan_status().equalsIgnoreCase("1")) {
            Const.STUDENT_TEST_ID = freeTests.get(position).getStudent_test_taken_id();
            if (freeTests.get(position).getTest_checked_attended().equalsIgnoreCase("1") && !wasPaused) {
//                Intent intent = new Intent(context, ResultPannelActivity.class);
//                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
//                Const.END_TIME_STATUS = "0";
//                context.startActivity(intent);

                //*************************************************REMOVE THIS *******************************************************************************************

                Intent intent = new Intent(context, TestInstructionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("quiz_id", freeTests.get(position).getTest_quiz_id());
                intent.putExtra("was_paused", wasPaused);
                intent.putExtra("quiz_name", freeTests.get(position).getTest_quiz_name());
                intent.putExtra("total_time", freeTests.get(position).getTest_time());
                intent.putExtra("remain_time", freeTests.get(position).getRemaining_time());
                intent.putExtra("changable", freeTests.get(position).getTest_changable());
                intent.putExtra("total_question", freeTests.get(position).getTest_no_of_qs());
                intent.putExtra("direction", freeTests.get(position).getTest_direction_status());
                intent.putExtra("total_marks", freeTests.get(position).getTest_total_marks());
                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
                Const.END_TIME_STATUS = "1";
                context.startActivity(intent);

                //*************************************************REMOVE THIS *******************************************************************************************

            } else {
                Intent intent = new Intent(context, TestInstructionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("quiz_id", freeTests.get(position).getTest_quiz_id());
                intent.putExtra("was_paused", wasPaused);
                intent.putExtra("quiz_name", freeTests.get(position).getTest_quiz_name());
                intent.putExtra("total_time", freeTests.get(position).getTest_time());
                intent.putExtra("remain_time", freeTests.get(position).getRemaining_time());
                intent.putExtra("changable", freeTests.get(position).getTest_changable());
                intent.putExtra("total_question", freeTests.get(position).getTest_no_of_qs());
                intent.putExtra("direction", freeTests.get(position).getTest_direction_status());
                intent.putExtra("total_marks", freeTests.get(position).getTest_total_marks());
                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
                Const.END_TIME_STATUS = "1";
                context.startActivity(intent);

            }

        } else if (freeTests.get(position).getTest_status().equalsIgnoreCase("1")) {
            Const.STUDENT_TEST_ID = freeTests.get(position).getStudent_test_taken_id();
            if (freeTests.get(position).getTest_checked_attended().equalsIgnoreCase("1") && !wasPaused) {
//                Intent intent = new Intent(context, ResultPannelActivity.class);
//                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
//                Const.END_TIME_STATUS = "0";
//                context.startActivity(intent);

                //*************************************************REMOVE THIS *******************************************************************************************

                Intent intent = new Intent(context, TestInstructionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("quiz_id", freeTests.get(position).getTest_quiz_id());
                intent.putExtra("was_paused", wasPaused);
                intent.putExtra("quiz_name", freeTests.get(position).getTest_quiz_name());
                intent.putExtra("total_time", freeTests.get(position).getTest_time());
                intent.putExtra("remain_time", freeTests.get(position).getRemaining_time());
                intent.putExtra("changable", freeTests.get(position).getTest_changable());
                intent.putExtra("total_question", freeTests.get(position).getTest_no_of_qs());
                intent.putExtra("direction", freeTests.get(position).getTest_direction_status());
                intent.putExtra("total_marks", freeTests.get(position).getTest_total_marks());
                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
                Const.END_TIME_STATUS = "1";
                context.startActivity(intent);

                //*************************************************REMOVE THIS *******************************************************************************************


            } else {
                Intent intent = new Intent(context, TestInstructionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
                Const.END_TIME_STATUS = "1";
                intent.putExtra("quiz_id", freeTests.get(position).getTest_quiz_id());
                intent.putExtra("was_paused", wasPaused);
                intent.putExtra("quiz_name", freeTests.get(position).getTest_quiz_name());
                intent.putExtra("total_time", freeTests.get(position).getTest_time());
                intent.putExtra("remain_time", freeTests.get(position).getRemaining_time());
                intent.putExtra("changable", freeTests.get(position).getTest_changable());
                intent.putExtra("total_question", freeTests.get(position).getTest_no_of_qs());
                intent.putExtra("direction", freeTests.get(position).getTest_direction_status());
                intent.putExtra("total_marks", freeTests.get(position).getTest_total_marks());
                context.startActivity(intent);

            }
        } else if (freeTests.get(position).getStudent_buy_plan_status().equalsIgnoreCase("1")) {
            Const.STUDENT_TEST_ID = freeTests.get(position).getStudent_test_taken_id();
            if (freeTests.get(position).getTest_checked_attended().equalsIgnoreCase("1") && !wasPaused) {

//                Intent intent = new Intent(context, ResultPannelActivity.class);
//                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
//                Const.END_TIME_STATUS = "0";
//                context.startActivity(intent);

                //*************************************************REMOVE THIS *******************************************************************************************

                Intent intent = new Intent(context, TestInstructionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("quiz_id", freeTests.get(position).getTest_quiz_id());
                intent.putExtra("was_paused", wasPaused);
                intent.putExtra("quiz_name", freeTests.get(position).getTest_quiz_name());
                intent.putExtra("total_time", freeTests.get(position).getTest_time());
                intent.putExtra("remain_time", freeTests.get(position).getRemaining_time());
                intent.putExtra("changable", freeTests.get(position).getTest_changable());
                intent.putExtra("total_question", freeTests.get(position).getTest_no_of_qs());
                intent.putExtra("direction", freeTests.get(position).getTest_direction_status());
                intent.putExtra("total_marks", freeTests.get(position).getTest_total_marks());
                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
                Const.END_TIME_STATUS = "1";
                context.startActivity(intent);

                //*************************************************REMOVE THIS *******************************************************************************************



            } else {
                Intent intent = new Intent(context, TestInstructionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Const.TEST_ID = freeTests.get(position).getTest_quiz_id();
                Const.END_TIME_STATUS = "1";
                intent.putExtra("quiz_id", freeTests.get(position).getTest_quiz_id());
                intent.putExtra("was_paused", wasPaused);
                intent.putExtra("quiz_name", freeTests.get(position).getTest_quiz_name());
                intent.putExtra("total_time", freeTests.get(position).getTest_time());
                intent.putExtra("remain_time", freeTests.get(position).getRemaining_time());
                intent.putExtra("changable", freeTests.get(position).getTest_changable());
                intent.putExtra("total_question", freeTests.get(position).getTest_no_of_qs());
                intent.putExtra("direction", freeTests.get(position).getTest_direction_status());
                intent.putExtra("total_marks", freeTests.get(position).getTest_total_marks());
                context.startActivity(intent);

            }
        } else {
            Toast.makeText(context, "Buy Plan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return freeTests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, freeText_details;
        Button btn_start_quiz, btn_resume_quiz;
        ImageView iv_lock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.freeText);
            freeText_details = itemView.findViewById(R.id.freeText_details);
            btn_start_quiz = itemView.findViewById(R.id.btn_start_quiz);
            btn_resume_quiz = itemView.findViewById(R.id.btn_resume_quiz);
            iv_lock = itemView.findViewById(R.id.iv_lock);
        }
    }

}
