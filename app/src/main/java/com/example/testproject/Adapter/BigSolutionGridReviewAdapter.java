package com.example.testproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testproject.Model.QuestionSolutionSetGet;
import com.example.testproject.R;

import java.util.ArrayList;

public class BigSolutionGridReviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<QuestionSolutionSetGet> quesList;
    public BigSolutionGridReviewAdapter(Context context, ArrayList<QuestionSolutionSetGet> quesList) {
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

        return view;
    }
}
