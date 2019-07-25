package com.example.testproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;


import java.util.ArrayList;


public class BigRecycleReviewAdapter extends RecyclerView.Adapter<BigRecycleReviewAdapter.reviewViewHolder> {
    Context context;
    ArrayList<FullQuestionSetGet> quesList;
    public BigRecycleReviewAdapter(Context context, ArrayList<FullQuestionSetGet> quesList) {
        this.context=context;
        this.quesList=quesList;
    }

    @NonNull
    @Override
    public BigRecycleReviewAdapter.reviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater!=null;
        View view=inflater.inflate(R.layout.review_question_list_view,parent,false);
        return new BigRecycleReviewAdapter.reviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BigRecycleReviewAdapter.reviewViewHolder viewHolder, final int position) {
        viewHolder. tv_no_ques.setText(String.valueOf(position+1));
        viewHolder. tv_ques.loadDataWithBaseURL(null, quesList.get(position).getTest_question(), "text/html", "utf-8", null);
        if ((Const.hashMapSelected.containsKey(quesList.get(position).getTest_question_id()) == Boolean.TRUE)) {
            viewHolder.rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.answered_bg));
            viewHolder.tv_no_ques.setTextColor(context.getResources().getColor(R.color.white));
        } else if ((Const.hashMapMarkSelected.containsKey(quesList.get(position).getTest_question_id()) == Boolean.TRUE)) {
            viewHolder.tv_no_ques.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.marked_bg));
        }else if ((Const.hashMapSelectMarkReview.containsKey(quesList.get(position).getTest_question_id()) == Boolean.TRUE)) {
            viewHolder.tv_no_ques.setTextColor(context.getResources().getColor(R.color.answeredColor));
            viewHolder.rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mark_answered_bg));
        }else {
            viewHolder.tv_no_ques.setTextColor(context.getResources().getColor(R.color.md_black_1000));
            viewHolder.rl_option.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.not_visit_bg));

        }

    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }
    class reviewViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_no_ques;
        WebView tv_ques;
        RelativeLayout rl_option,rv_ques_list;
        CardView card_ans;
        public reviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_no_ques = itemView.findViewById(R.id.tv_no_ques);
            rl_option=itemView.findViewById(R.id.rl_option);
            rv_ques_list=itemView.findViewById(R.id.rv_ques_list);
            tv_ques=itemView.findViewById(R.id.tv_ques);
        }
    }
}
