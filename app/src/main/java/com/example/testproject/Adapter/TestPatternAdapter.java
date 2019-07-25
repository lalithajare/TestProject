package com.example.testproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testproject.Model.TestPattern;
import com.example.testproject.R;

import java.util.ArrayList;

public class TestPatternAdapter extends RecyclerView.Adapter<TestPatternAdapter.patternViewHolder> {

    Context context;
    ArrayList<TestPattern> patternArrayList;
    public TestPatternAdapter(Context context, ArrayList<TestPattern> patternArrayList) {
        this.context=context;
        this.patternArrayList=patternArrayList;
    }

    @NonNull
    @Override
    public TestPatternAdapter.patternViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater!=null;
        View view= inflater.inflate(R.layout.test_patten_view,parent,false);
        return new TestPatternAdapter.patternViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestPatternAdapter.patternViewHolder patternHolder, int position) {

        patternHolder.tv_test_name.setText(patternArrayList.get(position).getSection_name());
        patternHolder.tv_right_mark.setText("+"+patternArrayList.get(position).getCorrect_mark()+"/");
        patternHolder.tv_wrong_mark.setText("-"+patternArrayList.get(position).getNegetive_mark());

    }

    @Override
    public int getItemCount() {
        return patternArrayList.size();
    }
    class patternViewHolder extends RecyclerView.ViewHolder{

        TextView tv_test_name,tv_right_mark,tv_wrong_mark;
        public patternViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_test_name=itemView.findViewById(R.id.tv_test_name);
            tv_right_mark=itemView.findViewById(R.id.tv_right_mark);
            tv_wrong_mark=itemView.findViewById(R.id.tv_wrong_mark);

        }
    }
}
