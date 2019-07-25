package com.example.testproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testproject.Model.AnswerSolutionSetGet;
import com.example.testproject.Model.QuestionSolutionSetGet;
import com.example.testproject.R;


import java.util.ArrayList;
import java.util.HashMap;

import at.blogc.android.views.ExpandableTextView;

public class FullSolutionTestPageAdapter extends PagerAdapter {
    Context context;
    ArrayList<QuestionSolutionSetGet> quesSolutionList;
    private LayoutInflater inflater;
    HashMap<String,ArrayList<AnswerSolutionSetGet>> solutionListHashMap;
    public FullSolutionTestPageAdapter(Context context, ArrayList<QuestionSolutionSetGet> quesSolutionList, HashMap<String,ArrayList<AnswerSolutionSetGet>> solutionListHashMap) {
        this.context = context;
        this.quesSolutionList = quesSolutionList;
        this.solutionListHashMap = solutionListHashMap;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return solutionListHashMap.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_NONE;

    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, final int position) {
        View myView = inflater.inflate(R.layout.custom_freetest_full_solution_layout, view, false);
        TextView question_no = myView.findViewById(R.id.question_no);
        WebView question_body = myView.findViewById(R.id.question_body);
        WebView tv_solution=myView.findViewById(R.id.tv_solution);
        final ExpandableTextView tv_direction=myView.findViewById(R.id.tv_direction);
        final WebView expandableTextView=myView.findViewById(R.id.expandableTextView);
        final Button btn_toggle=myView.findViewById(R.id.btn_toggle);
        int no = position + 1;
        question_no.setText("Q. " + no);
        Spanned htmlAsSpanned = Html.fromHtml(quesSolutionList.get(position).getSolution_directions());
        SolutionAnswerAdapter solutionAnswerAdapter;
        solutionAnswerAdapter=new SolutionAnswerAdapter(context, solutionListHashMap.get(quesSolutionList.get(position).getSolution_question_id()), quesSolutionList.get(position).getSolution_question(), quesSolutionList.get(position).getSolution_question_id());
        if (!quesSolutionList.get(position).getSolution().equalsIgnoreCase("")){
            tv_solution.setVisibility(View.VISIBLE);
            tv_solution.loadDataWithBaseURL(null, "Solution:\n"+quesSolutionList.get(position).getSolution(), "text/html", "utf-8", null);

        }else {
            tv_solution.setVisibility(View.GONE);
        }
        question_body.loadDataWithBaseURL(null, quesSolutionList.get(position).getSolution_question(), "text/html", "utf-8", null);

        if (quesSolutionList.get(position).getSolution_directions().equalsIgnoreCase("")){
            expandableTextView.setVisibility(View.GONE);
            btn_toggle.setVisibility(View.GONE);
            tv_direction.setVisibility(View.GONE);
        }else {
            expandableTextView.loadDataWithBaseURL(null, quesSolutionList.get(position).getSolution_directions(), "text/html", "utf-8", null);
            expandableTextView.setVisibility(View.GONE);
            btn_toggle.setVisibility(View.VISIBLE);
            tv_direction.setVisibility(View.VISIBLE);
            tv_direction.setText(htmlAsSpanned);
        }
        btn_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (btn_toggle.getText().toString().equalsIgnoreCase("Read More")){
                    if (tv_direction.getVisibility()==View.VISIBLE){
                        btn_toggle.setText(R.string.readMore);
                        expandableTextView.setVisibility(View.VISIBLE);
                        tv_direction.setVisibility(View.GONE);
                        expandableTextView.loadDataWithBaseURL(null, quesSolutionList.get(position).getSolution_directions(), "text/html", "utf-8", null);
                        if (expandableTextView.getVisibility()==View.VISIBLE){
                            btn_toggle.setText(R.string.readLess);
                            tv_direction.setVisibility(View.GONE);

                        }
                    }
                }else {
                    expandableTextView.setVisibility(View.GONE);
                    btn_toggle.setText(R.string.readMore);
                    tv_direction.setVisibility(View.VISIBLE);

                }

            }
        });

        RecyclerView ans_listView = myView.findViewById(R.id.ans_listView);
        ans_listView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false ));
        ans_listView.setAdapter(solutionAnswerAdapter);
        view.addView(myView);
        return myView;

    }
}
