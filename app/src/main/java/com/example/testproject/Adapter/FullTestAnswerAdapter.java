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

import com.example.testproject.Model.AnswerSetGet;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import java.util.List;

public class FullTestAnswerAdapter extends RecyclerView.Adapter<FullTestAnswerAdapter.ansViewHolder> {
    private Context context;
    private List<AnswerSetGet> ansList;

    private int selectedPosition = -1;
    private Button btn_save_next, btn_clear;
    private String question, questionId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public FullTestAnswerAdapter(Context context, List<AnswerSetGet> ansList
            , String question, String questionId, Button btn_save_next
            , Integer selectedPosition) {
        this.context = context;
        this.ansList = ansList;
        this.btn_save_next = btn_save_next;
        //this.btn_clear = btn_clear;
        this.question = question;
        this.questionId = questionId;
        if (selectedPosition != -1) {
            this.selectedPosition = selectedPosition;
        }
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
        viewHolder.ans_body.loadDataWithBaseURL(null, ansList.get(position).getGoal_answer(), "text/html", "utf-8", null);
        pref = context.getSharedPreferences("HashValue", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        final int finalPosition = position;
        viewHolder.card_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                Const.CHOOSE_QUESTION_ID = questionId;
                Const.ANSWER_ID = ansList.get(selectedPosition).getGoal_answers_id();
                btn_save_next.setVisibility(View.VISIBLE);
                Const.answerStoreHash.put(questionId, ansList.get(selectedPosition).getGoal_answer());
                Const.answerCheckHash.put(questionId, String.valueOf(selectedPosition));
                Const.answerQuestionStoreHash.put(question, ansList.get(selectedPosition).getGoal_answer());
                notifyDataSetChanged();
            }
        });
        viewHolder.rl_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                Const.CHOOSE_QUESTION_ID = questionId;
                Const.ANSWER_ID = ansList.get(selectedPosition).getGoal_answers_id();
                btn_save_next.setVisibility(View.VISIBLE);
                Const.answerStoreHash.put(questionId, ansList.get(selectedPosition).getGoal_answer());
                Const.answerCheckHash.put(questionId, String.valueOf(selectedPosition));
                Const.answerQuestionStoreHash.put(question, ansList.get(selectedPosition).getGoal_answer());
                notifyDataSetChanged();
            }
        });
        viewHolder.ans_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                Const.CHOOSE_QUESTION_ID = questionId;
                Const.ANSWER_ID = ansList.get(selectedPosition).getGoal_answers_id();
                btn_save_next.setVisibility(View.VISIBLE);
                Const.answerStoreHash.put(questionId, ansList.get(selectedPosition).getGoal_answer());
                Const.answerCheckHash.put(questionId, String.valueOf(selectedPosition));
                Const.answerQuestionStoreHash.put(question, ansList.get(selectedPosition).getGoal_answer());
                notifyDataSetChanged();
            }
        });
        if (selectedPosition == position) {
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable(R.drawable.a));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#E2F0FF"));
        } else {
            viewHolder.rl_ans.setBackground(context.getResources().getDrawable(R.drawable.b));
            viewHolder.ans_body.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
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
