package com.example.testproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Model.ResultSectionMarkSetGet;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static com.example.testproject.Activity.ResultPannelActivity.progressBar_section_average;
import static com.example.testproject.Activity.ResultPannelActivity.progressBar_section_topper;
import static com.example.testproject.Activity.ResultPannelActivity.progressBar_section_you;
import static com.example.testproject.Activity.ResultPannelActivity.tv_average_section_mark;
import static com.example.testproject.Activity.ResultPannelActivity.tv_topper_section_mark;
import static com.example.testproject.Activity.ResultPannelActivity.tv_you_section_mark;


public class ResultSectionMarkAdapter extends RecyclerView.Adapter<ResultSectionMarkAdapter.markViewHolder> {
    Context context;
    List<ResultSectionMarkSetGet> resultSectionMarkList;
    int row_index=0;
    public ResultSectionMarkAdapter(Context context, List<ResultSectionMarkSetGet> resultSectionMarkList) {
        this.context=context;
        this.resultSectionMarkList=resultSectionMarkList;
    }

    @NonNull
    @Override
    public ResultSectionMarkAdapter.markViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.result_section_rank_view,parent,false);
        return new markViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultSectionMarkAdapter.markViewHolder holder, final int position) {
        holder.tv_rank_mark.setText(resultSectionMarkList.get(position).getSection_mark());
        holder.tv_rank_msg.setText(resultSectionMarkList.get(position).getSection_mark_msg());
        if (resultSectionMarkList.get(position).getSection_mark_msg().equalsIgnoreCase("Marks")){
            holder.tv_rank_mark.setTextColor(context.getResources().getColor(R.color.callColor));

        }else if (resultSectionMarkList.get(position).getSection_mark_msg().equalsIgnoreCase("Correct")){
            holder.tv_rank_mark.setTextColor(context.getResources().getColor(R.color.answeredColor));

        }else {
            holder.tv_rank_mark.setTextColor(context.getResources().getColor(R.color.notAnsweredColor));

        }

        holder.rl_section_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=position;
                Const.MARK_ID=String.valueOf(position);
                if (Const.SECTION_ANALYSIS_ID.equalsIgnoreCase("")){
                    Const.SECTION_ANALYSIS_ID="0";
                }
                getMarkSectionWise();
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            holder.rl_section_rank.setBackgroundColor(context.getResources().getColor(R.color.white));

        } else
        {
            holder.rl_section_rank.setBackgroundColor(context.getResources().getColor(R.color.md_grey_300));

        }
    }

    private void getMarkSectionWise() {
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

                                if (Const.MARK_ID.equalsIgnoreCase("0")){

                                    progressBar_section_you.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_mark));
                                    progressBar_section_topper.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_mark));
                                    progressBar_section_average.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_mark));

                                }else if (Const.MARK_ID.equalsIgnoreCase("1")){

                                    progressBar_section_you.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_correct));
                                    progressBar_section_topper.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_correct));
                                    progressBar_section_average.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_correct));

                                }else {

                                    progressBar_section_you.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_wrong));
                                    progressBar_section_topper.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_wrong));
                                    progressBar_section_average.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_wrong));
                                }

                                progressBar_section_you.setProgress((int)(resultObject.getLong("your_marks_for_all")));
                                progressBar_section_topper.setProgress((int)(resultObject.getLong("topper_marks_for_all")));
                                progressBar_section_average.setProgress((int)(resultObject.getLong("average_marks_for_all")));

                                tv_average_section_mark.setText(resultObject.getString("average_marks_for_all"));
                                tv_topper_section_mark.setText(resultObject.getString("topper_marks_for_all"));
                                tv_you_section_mark.setText(resultObject.getString("your_marks_for_all"));

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
                params.put("button_id", Const.MARK_ID);
                params.put("section_id",  Const.SECTION_ANALYSIS_ID);
                params.put("student_id", "6");
                Log.d("GetSectionMark", "getParams: "+params);
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
        return resultSectionMarkList.size();
    }
    class markViewHolder extends RecyclerView.ViewHolder{
        TextView tv_rank_mark,tv_rank_msg;
        RelativeLayout rl_section_rank;

        public markViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rank_msg=itemView.findViewById(R.id.tv_rank_msg);
            tv_rank_mark=itemView.findViewById(R.id.tv_rank_mark);
            rl_section_rank=itemView.findViewById(R.id.rl_section_rank);
        }
    }
}
