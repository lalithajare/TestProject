package com.example.testproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testproject.Model.AnswerSetGet;
import com.example.testproject.Model.FullQuestionSetGet;
import com.example.testproject.R;
import com.example.testproject.Utils.Const;

import java.util.ArrayList;
import java.util.HashMap;

import at.blogc.android.views.ExpandableTextView;

public class FullNotChangeTestAdapter extends PagerAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<FullQuestionSetGet> quesList;
    HashMap<String,ArrayList<AnswerSetGet>> listHashMap;
    FullTestAnswerAdapter answerAdapter;
    Button submitButton;
    TextView question_no;
    WebView question_body;
    public FullNotChangeTestAdapter(Context context, ArrayList<FullQuestionSetGet> quesList, HashMap<String,ArrayList<AnswerSetGet>> listHashMap, Button submitButton) {
        this.context = context;
        this.quesList = quesList;
        this.listHashMap = listHashMap;
        this.submitButton = submitButton;
        // this.preButton = preButton;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listHashMap.size();
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_NONE;

    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.full_freetest_layout, container, false);
        question_no = myView.findViewById(R.id.question_no);
        question_body = myView.findViewById(R.id.question_body);
        final ExpandableTextView tv_direction=myView.findViewById(R.id.tv_direction);
        final WebView expandableTextView=myView.findViewById(R.id.expandableTextView);
        final Button btn_toggle=myView.findViewById(R.id.btn_toggle);
        int no = position + 1;
        question_no.setText("Q. " + no);
        Spanned htmlAsSpanned = Html.fromHtml(quesList.get(position).getTest_directions());

        if (Const.answerCheckHash.size() > 0 && Const.answerCheckHash.containsKey(quesList.get(position).getTest_question_id())) {
            answerAdapter = new FullTestAnswerAdapter(context, listHashMap.get(quesList.get(position).getTest_question_id()), quesList.get(position).getTest_question(), quesList.get(position).getTest_question_id(), submitButton, Integer.parseInt(Const.answerCheckHash.get(quesList.get(position).getTest_question_id())));
        } else {
            answerAdapter = new FullTestAnswerAdapter(context, listHashMap.get(quesList.get(position).getTest_question_id()), quesList.get(position).getTest_question(), quesList.get(position).getTest_question_id(), submitButton, -1);
        }
        question_body.loadDataWithBaseURL(null, quesList.get(position).getTest_question(), "text/html", "utf-8", null);

        if (quesList.get(position).getTest_directions().equalsIgnoreCase("")){
            expandableTextView.setVisibility(View.GONE);
            btn_toggle.setVisibility(View.GONE);
            tv_direction.setVisibility(View.GONE);
        }else {
            expandableTextView.setVisibility(View.GONE);
            btn_toggle.setVisibility(View.VISIBLE);
            expandableTextView.loadDataWithBaseURL(null, quesList.get(position).getTest_directions(), "text/html", "utf-8", null);
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
                        expandableTextView.loadDataWithBaseURL(null, quesList.get(position).getTest_directions(), "text/html", "utf-8", null);
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
        ans_listView.setAdapter(answerAdapter);
        ((ViewPager)container).addView(myView);
        return myView;

    }

}
