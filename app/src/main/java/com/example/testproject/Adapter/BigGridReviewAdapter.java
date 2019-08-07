package com.example.testproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.Model.QuestionDetailsResponseSchema;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import java.util.ArrayList;

public class BigGridReviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<QuestionDetailsResponseSchema> quesList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public BigGridReviewAdapter(Context context, ArrayList<QuestionDetailsResponseSchema> quesList) {
        this.context = context;
        this.quesList = quesList;
    }

    @Override
    public int getCount() {
        return quesList.size();
    }

    @Override
    public Object getItem(int position) {
        return quesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.review_question_grid_view, parent, false);

        }
        TextView tv_no_ques = view.findViewById(R.id.tv_no_ques);
        RelativeLayout rl_option = view.findViewById(R.id.rl_option);
        tv_no_ques.setText(String.valueOf(position + 1));


        QuestionDetailsResponseSchema questionDetailsResponseSchema = quesList.get(position);

        if (questionDetailsResponseSchema.isAnswered()) {
            if (questionDetailsResponseSchema.isAnswered() && questionDetailsResponseSchema.isMarked()) {
                rl_option.setBackground(ContextCompat.getDrawable(context, R.drawable.mark_answered_bg));
                tv_no_ques.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                rl_option.setBackground(ContextCompat.getDrawable(context, R.drawable.answered_bg));
                tv_no_ques.setTextColor(context.getResources().getColor(R.color.white));
            }
        } else if (questionDetailsResponseSchema.isMarked()) {
            tv_no_ques.setTextColor(context.getResources().getColor(R.color.answeredColor));
            rl_option.setBackground(ContextCompat.getDrawable(context, R.drawable.marked_bg));
        } else {
            tv_no_ques.setTextColor(context.getResources().getColor(R.color.md_black_1000));
            rl_option.setBackground(ContextCompat.getDrawable(context, R.drawable.not_visit_bg));
        }

        return view;
    }
}
