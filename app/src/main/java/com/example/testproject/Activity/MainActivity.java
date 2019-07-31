package com.example.testproject.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.FullTestTopicAdapter;
import com.example.testproject.Model.CoursesSetterGetter;
import com.example.testproject.Model.QuizTopic;
import com.example.testproject.R;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.InternetCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Spinner spinner_exam;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> adapter;
    ArrayList<CoursesSetterGetter> courseList = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();
    String exam_name,compareValue;
    RecyclerView recyclerView;
    ArrayList<QuizTopic> fullTestTopics = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner_exam=findViewById(R.id.spinner_exam);

        recyclerView = findViewById(R.id.free_test_recycle);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.commit();
        if (InternetCheck.isInternetOn(this)) {
            getExamTopic();
        }else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }
    private void getExamTopic() {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_COURSE, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("FullResponse", "onResponse: " + response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray("Courses");
                        courseList.clear();
                        strings.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject courseObject = jsonArray.getJSONObject(i);
                            CoursesSetterGetter coursesSetterGetter = new CoursesSetterGetter();
                            coursesSetterGetter.setCourse_id(courseObject.getString("course_id"));
                            coursesSetterGetter.setCourse_name(courseObject.getString("course_name"));
                            courseList.add(coursesSetterGetter);
                            strings.add(courseList.get(i).getCourse_name());
                        }
                        spinner_exam.setVisibility(View.VISIBLE);
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings) {

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                // TODO Auto-generated method stub

                                View view = super.getView(position, convertView, parent);

                                TextView text = (TextView) view.findViewById(android.R.id.text1);
                                text.setTextColor(Color.BLACK);
                                text.setGravity(Gravity.CENTER_VERTICAL);
                                text.setTextSize(14);
                                return view;

                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // TODO Auto-generated method stub

                                View view = super.getView(position, convertView, parent);
                                TextView text = (TextView) view.findViewById(android.R.id.text1);
                                text.setTextColor(Color.BLACK);
                                text.setTextSize(14);
                                return view;

                            }
                        };
                        spinner_exam.setAdapter(adapter);
                        exam_name = pref.getString("exam_name", "");

                        if (exam_name != null) {
                            spinner_exam.setSelection(adapter.getPosition(exam_name));

                        } else {

                            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, strings) {

                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    // TODO Auto-generated method stub

                                    View view = super.getView(position, convertView, parent);

                                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                                    text.setTextColor(Color.BLACK);
                                    text.setGravity(Gravity.CENTER_VERTICAL);
                                    text.setTextSize(14);
                                    return view;

                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    // TODO Auto-generated method stub

                                    View view = super.getView(position, convertView, parent);
                                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                                    text.setTextColor(Color.BLACK);
                                    text.setTextSize(14);
                                    return view;

                                }
                            };
                            spinner_exam.setAdapter(adapter);

                        }
                        spinner_exam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                compareValue = spinner_exam.getSelectedItem().toString();
                                editor.putString("exam_name", compareValue);
                                editor.putString("exam_id", courseList.get(position).getCourse_id());
                                editor.putInt("exam_position", position);
                                editor.commit();
                                getFullTest();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    void getFullTest(){

        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_progress_bar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_TEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    Log.e("onResponse: ", status);
                    if (status.equalsIgnoreCase("200")){
                        JSONArray jsonArray = object.getJSONArray("question_list");
                        QuizTopic quizTopic;
                        fullTestTopics.clear();
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            quizTopic = new QuizTopic(object1.getString("category_id"), object1.getString("category_name"),
                                    object1.getString("total_quiz"),object1.getString("section_id"),
                                    object1.getString("full_length_test"),object1.getString("section_test"),
                                    object1.getString("previous_year_test"));
                            fullTestTopics.add(quizTopic);
                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        FullTestTopicAdapter fullTestTopicAdapter = new FullTestTopicAdapter(getApplicationContext(), fullTestTopics);
                        recyclerView.setAdapter(fullTestTopicAdapter);
                    }else {

                        recyclerView.setVisibility(View.GONE);
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
                recyclerView.setVisibility(View.GONE);
                //av_courses_loader.hide();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("exam_id", getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE).getString("exam_id", null));
                Log.d("FullTest", "getParams: "+params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(this).addToRequestQueue(request);
    }
}
