package com.example.testproject.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Model.SectionSetGet;
import com.example.testproject.Model.TestPattern;
import com.example.testproject.Model.TestQuizSetGet;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.example.testproject.Activity.FreeTestChapterActivity.freeTests;
import static com.example.testproject.Activity.FreeTestChapterActivity.recyclerView;
import static com.example.testproject.Activity.FreeTestChapterActivity.testAdapter;
import static com.example.testproject.Utils.Const.patternArrayList;
import static com.example.testproject.Activity.FreeTestChapterActivity.noDataImage;


public class TestSectionAdapter extends RecyclerView.Adapter<TestSectionAdapter.sectionViewHolder>{
    Context context;
    List<SectionSetGet> sectionList;
    int row_index=0;

    public TestSectionAdapter(Context context, List<SectionSetGet> sectionList) {
        this.context=context;
        this.sectionList=sectionList;
    }

    @NonNull
    @Override
    public TestSectionAdapter.sectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.result_section_view,parent,false);
        return new sectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestSectionAdapter.sectionViewHolder holder, final int position) {
        holder.tv_result_section_name.setText(sectionList.get(position).getSection_name());
        holder.rl_section_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                Const.TEST_ID=String.valueOf(position);
                getTest();
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            holder.rl_section_topic.setBackground(context.getResources().getDrawable( R.drawable.result_section_selector));
            holder.tv_result_section_name.setTextColor(Color.parseColor("#ffffff"));
            holder.iv_section_img.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.rl_section_topic.setBackground(context.getResources().getDrawable( R.drawable.result_section_default));
            holder.tv_result_section_name.setTextColor(Color.parseColor("#000000"));
            holder.iv_section_img.setVisibility(View.GONE);

        }

    }

    private void getTest() {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_progress_bar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
        recyclerView.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_LENGTH_QUIZ_TEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    //Const.STUDENT_STATUS=object.getString("student_status");
                    Log.e("onResponse: ", status);
                    if (status.equalsIgnoreCase("200")){
                        JSONArray jsonArray = object.getJSONArray("question_list");
                        freeTests.clear();
                        TestQuizSetGet testQuizSetGet;
                        noDataImage.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            testQuizSetGet=new TestQuizSetGet();
                            testQuizSetGet.setTest_quiz_id(object1.getString("quiz_id"));
                            testQuizSetGet.setTest_quiz_name(object1.getString("quiz_name"));
                            testQuizSetGet.setTest_direction_status(object1.getString("Direction Status"));
                            testQuizSetGet.setTest_no_of_qs(object1.getString("no_of_qs"));
                            testQuizSetGet.setTest_time(object1.getString("time"));
                            testQuizSetGet.setTest_total_marks(object1.getString("total_marks"));
                            testQuizSetGet.setTest_changable(object1.getString("changable"));
                            testQuizSetGet.setTest_status(object1.getString("status"));
                            testQuizSetGet.setTest_checked_attended(object1.getString("checked_attended"));
                            testQuizSetGet.setRemaining_time(object1.getString("remaining_time"));
                            testQuizSetGet.setStudent_test_taken_id(object1.getString("student_test_taken_id"));
                            testQuizSetGet.setStudent_buy_plan_status(object1.getString("student_buy_plan_status"));
                            JSONArray testPatternArray = object1.getJSONArray("Sectional Pattern");
                            //patternArrayList.clear();
                            patternArrayList = new ArrayList<>();
                            if(testPatternArray != null ){
                                for (int j=0; j<testPatternArray.length(); j++){
                                    JSONObject testPatternObject = testPatternArray.getJSONObject(j);
                                    TestPattern testPattern =new TestPattern();
                                    testPattern.setSection_name(testPatternObject.getString("section_name"));
                                    testPattern.setCorrect_mark(testPatternObject.getString("correct_mark"));
                                    testPattern.setNegetive_mark(testPatternObject.getString("negetive_mark"));
                                    patternArrayList.add(testPattern);
                                }
                            }
                            freeTests.add(testQuizSetGet);
                        }

                        testAdapter = new FullTestQuizAdapter(context, freeTests);
                        recyclerView.setAdapter(testAdapter);
                    }else {
                        recyclerView.setVisibility(View.GONE);
                        noDataImage.setVisibility(View.VISIBLE);
                    }

                    //av_courses_loader.hide();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //av_courses_loader.hide();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("category_id", Const.CATEGORY_ID);
                params.put("section_id", Const.SECTION_ID);
                params.put("question_year_stat",Const.TEST_ID);
                params.put("student_id", "6");
                Log.d("FreeTest1", "getParams: "+params);
                //Log.d("FullName", "getParams: "+params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }
    class sectionViewHolder extends RecyclerView.ViewHolder{
        TextView tv_result_section_name;
        RelativeLayout rl_section_topic;
        ImageView iv_section_img;
        public sectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_result_section_name=itemView.findViewById(R.id.tv_result_section_name);
            rl_section_topic=itemView.findViewById(R.id.rl_section_topic);
            iv_section_img=itemView.findViewById(R.id.iv_section_img);
        }
    }
}
