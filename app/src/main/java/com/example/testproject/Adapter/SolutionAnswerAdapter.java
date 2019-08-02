package com.example.testproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.testproject.Model.AnswerSolutionSetGet;
import com.example.testproject.R;

import java.util.ArrayList;
import java.util.List;

public class SolutionAnswerAdapter extends RecyclerView.Adapter<SolutionAnswerAdapter.soluionViewHolder> {
    private Context context;
    private List<AnswerSolutionSetGet> ansSolutionList;
    String question,questionId;

    public SolutionAnswerAdapter(Context context,
                                 ArrayList<AnswerSolutionSetGet> ansSolutionList,
                                 String question, String questionId) {
        this.context = context;
        this.ansSolutionList = ansSolutionList;
        this.question = question;
        this.questionId = questionId;
    }

    @NonNull
    @Override
    public SolutionAnswerAdapter.soluionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View  view = LayoutInflater.from(context).inflate(R.layout.custom_free_test_answer_view, parent, false);
        return new SolutionAnswerAdapter.soluionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionAnswerAdapter.soluionViewHolder viewHolder, int position) {
        int no=position+1;
        //Spanned htmlAsSpanned = Html.fromHtml(ansSolutionList.get(position).getSolution_answer());
        viewHolder.tv_ans_no.setText("("+(char)(no+'A'-1)+ ")");
        viewHolder.ans_body.loadDataWithBaseURL(null, ansSolutionList.get(position).getSolution_answer(), "text/html", "utf-8", null);
       // viewHolder.radioButton.setText("("+(char)(no+'A'-1)+ ")  "+htmlAsSpanned);
        if (ansSolutionList.get(position).getSolution_answer_status().equalsIgnoreCase("1")){
            //viewHolder.radioButton.setBackground(mContext.getResources().getDrawable(R.drawable.answer_right_selector));
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_right_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#D6FFD7"));

        }else if (ansSolutionList.get(position).getSolution_student_answer().equalsIgnoreCase("2")){
           // viewHolder.radioButton.setBackground(mContext.getResources().getDrawable(R.drawable.answer_wrong_selector));
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_wrong_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFD5D5"));

        }else if (ansSolutionList.get(position).getSolution_student_answer().equalsIgnoreCase("1")){
            //viewHolder.radioButton.setBackground(mContext.getResources().getDrawable(R.drawable.answer_right_selector));
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable( R.drawable.answer_right_selector));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#D6FFD7"));

        }else {
           // viewHolder.radioButton.setBackground(mContext.getResources().getDrawable(R.drawable.b));
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable( R.drawable.b));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }



    }

    @Override
    public int getItemCount() {
        return  ansSolutionList.size();
    }

    class soluionViewHolder extends RecyclerView.ViewHolder{
        //private RadioButton radioButton;
        LinearLayout rl_ans;
        TextView tv_ans_no;
        WebView ans_body;

        public soluionViewHolder(@NonNull View itemView) {
            super(itemView);
            //radioButton = itemView.findViewById(R.id.ans_radio);
            rl_ans=itemView.findViewById(R.id.rl_ans);
            tv_ans_no = itemView.findViewById(R.id.tv_ans_no);
            ans_body = itemView.findViewById(R.id.ans_body);

        }
    }


}
