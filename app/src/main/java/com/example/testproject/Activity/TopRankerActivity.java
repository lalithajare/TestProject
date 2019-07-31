package com.example.testproject.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.TopRankerAdapter;
import com.example.testproject.Model.TopRankerSetGet;
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

public class TopRankerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    Context context;
    RecyclerView rv_top_rankers;
    List<TopRankerSetGet> topRankerList = new ArrayList<>();
    TopRankerAdapter topRankerAdapter;
    RecyclerView.LayoutManager layoutManager_ranker;
    ProgressDialog progressDialog;
    CardView cardView_top4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranker);
        context = TopRankerActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_page);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.topRankers);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv_top_rankers=findViewById(R.id.rv_top_rankers);
        cardView_top4=findViewById(R.id.cardView_top4);

        rv_top_rankers.setHasFixedSize(true);
        //topRankerAdapter = new TopRankerAdapter(mContext, topRankerList);
        layoutManager_ranker = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_top_rankers.setLayoutManager(layoutManager_ranker);
        populateTopRankerList();

    }

    private void populateTopRankerList() {
        progressDialog = new ProgressDialog(this);
        //progressDialog.setMax(100);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_TOP_RANKER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status_code");
                            String message = object.getString("message");
                            if (status.equalsIgnoreCase("203")){
                                JSONArray jsonArray = object.getJSONArray("marks_scored");
                                topRankerList.clear();
                                TopRankerSetGet topRankerSetGet;
                                cardView_top4.setVisibility(View.VISIBLE);
                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject object1 = jsonArray.getJSONObject(i);
                                    topRankerSetGet=new TopRankerSetGet();
                                    topRankerSetGet.setRanker_user_id(object1.getString("user_id"));
                                    topRankerSetGet.setRanker_user_name(object1.getString("user_name"));
                                    topRankerSetGet.setRanker_user_img(object1.getString("user_img"));
                                    topRankerSetGet.setRanker_correct_mark(object1.getString("correct_mark"));
                                    topRankerSetGet.setRanker_total_marks(object1.getString("total_marks"));
                                    topRankerList.add(topRankerSetGet);
                                }
                                topRankerAdapter = new TopRankerAdapter(context, topRankerList);
                                rv_top_rankers.setAdapter(topRankerAdapter);
                            }else {
                                cardView_top4.setVisibility(View.GONE);
                            }

                            progressDialog.hide();
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
                params.put("student_id", "6");
                Log.d("TopRanker", "getParams: "+params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(context).addToRequestQueue(request);
    }
}
