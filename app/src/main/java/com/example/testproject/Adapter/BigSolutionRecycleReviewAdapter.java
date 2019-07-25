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
import com.example.testproject.Model.QuestionSolutionSetGet;
import com.example.testproject.R;


import java.util.ArrayList;

public class BigSolutionRecycleReviewAdapter extends RecyclerView.Adapter<BigSolutionRecycleReviewAdapter.reviewViewHolder> {
    Context context;
    ArrayList<QuestionSolutionSetGet> quesList;
    public BigSolutionRecycleReviewAdapter(Context context, ArrayList<QuestionSolutionSetGet> quesList) {
        this.context=context;
        this.quesList=quesList;
    }

    @NonNull
    @Override
    public BigSolutionRecycleReviewAdapter.reviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater!=null;
        View view=inflater.inflate(R.layout.review_question_list_view,parent,false);
        return new BigSolutionRecycleReviewAdapter.reviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BigSolutionRecycleReviewAdapter.reviewViewHolder viewHolder, final int position) {
        viewHolder. tv_no_ques.setText(String.valueOf(position+1));

        viewHolder. tv_ques.loadDataWithBaseURL(null, quesList.get(position).getSolution_question(), "text/html", "utf-8", null);


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
