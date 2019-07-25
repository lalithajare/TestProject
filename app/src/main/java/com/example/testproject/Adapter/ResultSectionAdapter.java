package com.example.testproject.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Model.ResultSectionMarkSetGet;
import com.example.testproject.Model.ResultSectionSetGet;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.example.testproject.Activity.ResultPannelActivity.layoutManager_section_mark;
import static com.example.testproject.Activity.ResultPannelActivity.progressBar_section_average;
import static com.example.testproject.Activity.ResultPannelActivity.progressBar_section_topper;
import static com.example.testproject.Activity.ResultPannelActivity.progressBar_section_you;
import static com.example.testproject.Activity.ResultPannelActivity.resultSectionMarkAdapter;
import static com.example.testproject.Activity.ResultPannelActivity.resultSectionMarkList;
import static com.example.testproject.Activity.ResultPannelActivity.rv_section_rank;
import static com.example.testproject.Activity.ResultPannelActivity.tv_average_section_mark;
import static com.example.testproject.Activity.ResultPannelActivity.tv_topper_section_mark;
import static com.example.testproject.Activity.ResultPannelActivity.tv_you_section_mark;

public class ResultSectionAdapter extends RecyclerView.Adapter<ResultSectionAdapter.resultSectionViewHolder> {
    Context context;
    List<ResultSectionSetGet> resultSectionList;
    int row_index=0;
    public ResultSectionAdapter(Context context, List<ResultSectionSetGet> resultSectionList) {
        this.context=context;
        this.resultSectionList=resultSectionList;

    }

    @NonNull
    @Override
    public ResultSectionAdapter.resultSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.result_section_view,parent,false);
        return new resultSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultSectionAdapter.resultSectionViewHolder holder, final int position) {

        holder.tv_result_section_name.setText(resultSectionList.get(position).getResult_sectional_name());
        holder.rl_section_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                Const.SECTION_ANALYSIS_ID=resultSectionList.get(position).getResult_sectional_id();
                if (Const.MARK_ID.equalsIgnoreCase("")){
                    Const.MARK_ID="0";
                }
                getSectionMark();
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

    private void getSectionMark() {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_MARK_SCORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            Log.d("MarkSection", "onResponse: "+status);

                            if (status.equalsIgnoreCase("203")){
                                JSONObject resultObject = object.getJSONObject("marks_scored");

                                progressBar_section_you.setProgress((int)(resultObject.getLong("your_marks_for_all")));
                                progressBar_section_you.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_mark));
                                progressBar_section_topper.setProgress((int)(resultObject.getLong("topper_marks_for_all")));
                                progressBar_section_topper.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_mark));
                                progressBar_section_average.setProgress((int)(resultObject.getLong("average_marks_for_all")));
                                progressBar_section_average.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_mark));


                                tv_average_section_mark.setText(resultObject.getString("average_marks_for_all"));
                                tv_topper_section_mark.setText(resultObject.getString("topper_marks_for_all"));
                                tv_you_section_mark.setText(resultObject.getString("your_marks_for_all"));

                                rv_section_rank.setHasFixedSize(true);
                                resultSectionMarkAdapter = new ResultSectionMarkAdapter(context, resultSectionMarkList);
                                layoutManager_section_mark = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                rv_section_rank.setLayoutManager(layoutManager_section_mark);
                                resultSectionMarkList.clear();
                                resultSectionMarkList.add(new ResultSectionMarkSetGet( resultObject.getString("marks_btn"),"Marks"));
                                resultSectionMarkList.add(new ResultSectionMarkSetGet(resultObject.getString("correct_btn"),"Correct"));
                                resultSectionMarkList.add(new ResultSectionMarkSetGet(resultObject.getString("wrong_btn"),"Wrong"));
                                rv_section_rank.setAdapter(resultSectionMarkAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", Const.TEST_ID);
                params.put("section_id",  Const.SECTION_ANALYSIS_ID);
                params.put("button_id", Const.MARK_ID);
                params.put("student_id", "6");
                Log.d("SectionMark", "getParams: "+params);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public int getItemCount() {
        return resultSectionList.size();
    }
    class resultSectionViewHolder extends RecyclerView.ViewHolder{
        TextView tv_result_section_name;
        RelativeLayout rl_section_topic;
        ImageView iv_section_img;
        public resultSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_result_section_name=itemView.findViewById(R.id.tv_result_section_name);
            rl_section_topic=itemView.findViewById(R.id.rl_section_topic);
            iv_section_img=itemView.findViewById(R.id.iv_section_img);
        }
    }
}
