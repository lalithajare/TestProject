package com.example.testproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testproject.Activity.FreeTestChapterActivity;
import com.example.testproject.Model.QuizTopic;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import java.util.ArrayList;

public class FullTestTopicAdapter extends RecyclerView.Adapter<FullTestTopicAdapter.ViewHolder> {
    Context context;
    ArrayList<QuizTopic> fullTestTopics;

    public FullTestTopicAdapter(Context context, ArrayList<QuizTopic> fullTestTopics) {
        this.context = context;
        this.fullTestTopics = fullTestTopics;

    }

    @NonNull
    @Override
    public FullTestTopicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.free_test_gridview, parent, false);
        return new FullTestTopicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FullTestTopicAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(fullTestTopics.get(position).name);
        holder.tv_no_quiz.setText("Total Tests : " + fullTestTopics.get(position).noQuiz);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FreeTestChapterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("category_id", fullTestTopics.get(position).typeId);
                intent.putExtra("category_name", fullTestTopics.get(position).name);
                Const.CATEGORY_ID= fullTestTopics.get(position).typeId;
                Const.CATEGORY_NAME= fullTestTopics.get(position).name;
                Const.SECTION_ID= fullTestTopics.get(position).section_id;
                Const.ALL= fullTestTopics.get(position).noQuiz;
                Const.FULL_LENGTH= fullTestTopics.get(position).full_length_test;
                Const.SECTIONAL= fullTestTopics.get(position).section_test;
                Const.PREV_YEAR= fullTestTopics.get(position).previous_year_test;
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return fullTestTopics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, tv_no_quiz;
        CardView cardView;

        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.free_image);
            textView = view.findViewById(R.id.free_text);
            cardView = view.findViewById(R.id.free_card);
            tv_no_quiz = view.findViewById(R.id.tv_no_quiz);
        }
    }
}
