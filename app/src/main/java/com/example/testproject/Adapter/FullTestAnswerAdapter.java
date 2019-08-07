package com.example.testproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testproject.Model.AnswerDetailsSchema;
import com.example.testproject.R;

import java.util.List;

public class FullTestAnswerAdapter extends RecyclerView.Adapter<FullTestAnswerAdapter.ansViewHolder> {
    private Context context;
    private List<AnswerDetailsSchema> ansList;

    private Button btn_save_next, btn_clear;
    private String question, questionId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public FullTestAnswerAdapter(Context context, List<AnswerDetailsSchema> ansList
            , String question, String questionId, Button btn_save_next) {
        this.context = context;
        this.ansList = ansList;
        this.btn_save_next = btn_save_next;
        //this.btn_clear = btn_clear;
        this.question = question;
        this.questionId = questionId;
    }

    @NonNull
    @Override
    public FullTestAnswerAdapter.ansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.custom_free_test_answer_view, parent, false);
        return new FullTestAnswerAdapter.ansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ansViewHolder viewHolder, final int position) {

        //AnswerSetGet answerSetGet = ansList.get(position);
        int no = position + 1;
        viewHolder.tv_ans_no.setText("(" + (char) (no + 'A' - 1) + ")");
        viewHolder.ans_body.loadDataWithBaseURL(null, ansList.get(position).getAnswer(), "text/html", "utf-8", null);
        pref = context.getSharedPreferences("HashValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        viewHolder.card_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(position);
            }
        });
        viewHolder.rl_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(position);
            }
        });
        viewHolder.ans_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(position);
            }
        });
        if (ansList.get(position).isSelected()) {
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable(R.drawable.a));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#E2F0FF"));
        } else {
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable(R.drawable.b));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    private void selectOption(int position) {
        for (AnswerDetailsSchema answerDetailsSchema : ansList) {
            answerDetailsSchema.setSelected(false);
        }
        ansList.get(position).setSelected(true);
        btn_save_next.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ansList.size();
    }

    class ansViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ans_no;
        WebView ans_body;
        LinearLayout rl_ans;
        CardView card_ans;

        public ansViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ans_no = itemView.findViewById(R.id.tv_ans_no);
            ans_body = itemView.findViewById(R.id.ans_body);
            rl_ans = itemView.findViewById(R.id.rl_ans);
            card_ans = itemView.findViewById(R.id.card_ans);
        }
    }


}
