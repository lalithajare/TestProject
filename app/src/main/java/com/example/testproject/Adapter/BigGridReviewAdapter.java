package com.example.testproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import java.util.ArrayList;

public class BigGridReviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<FullQuestionSetGet> quesList;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public BigGridReviewAdapter(Context context, ArrayList<FullQuestionSetGet> quesList) {
        this.context=context;
        this.quesList=quesList;
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
        RelativeLayout rl_option=view.findViewById(R.id.rl_option);
        tv_no_ques.setText(String.valueOf(position+1));
        pref = context.getSharedPreferences("HashValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        /*for (Map.Entry<String, ?> entry : pref.getAll().entrySet()) {
            Const.hashMapSelected.put(entry.getKey(), true);*/

            if ((Const.hashMapSelected.containsKey(quesList.get(position).getTest_question_id()) == Boolean.TRUE)) {
                rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.answered_bg));
                tv_no_ques.setTextColor(context.getResources().getColor(R.color.white));
            } else if ((Const.hashMapMarkSelected.containsKey(quesList.get(position).getTest_question_id()) == Boolean.TRUE)) {
                tv_no_ques.setTextColor(context.getResources().getColor(R.color.white));
                rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.marked_bg));
            }else if ((Const.hashMapSelectMarkReview.containsKey(quesList.get(position).getTest_question_id()) == Boolean.TRUE)) {
                tv_no_ques.setTextColor(context.getResources().getColor(R.color.answeredColor));
                rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mark_answered_bg));
            }else {
                tv_no_ques.setTextColor(context.getResources().getColor(R.color.md_black_1000));
                rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.not_visit_bg));

            }
       /* }*/

        return view;
    }
}
