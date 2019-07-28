package com.example.testproject.Activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.example.testproject.Utils.AppPreferenceManager;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.UtilFunctions;
import com.example.testproject.database.DatabaseInitizationService;
import com.example.testproject.database.db_usecases.CategoryDBUseCases;
import com.example.testproject.database.db_usecases.CourseDBUseCases;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.operators.DBOperationsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner_exam;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> adapter;
    ArrayList<CoursesSetterGetter> courseList = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();
    String exam_name, compareValue;
    RecyclerView recyclerView;
    Button btnSync;
    ArrayList<QuizTopic> fullTestTopics = new ArrayList<>();

    RelativeLayout relPendingTest;
    TextView txtPendingTestName;
    TextView txtTimerValue;

    Bundle offlineValues;


    //DB operations
    CourseDBUseCases mCourseDBUseCases;
    CategoryDBUseCases mCategoryDBUseCases;
    DatabaseInitizationService mDatabaseInitizationService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            UtilFunctions.showToast("Service connected");
            DatabaseInitizationService.LocalBinder localBinder = (DatabaseInitizationService.LocalBinder) iBinder;
            mDatabaseInitizationService = localBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            UtilFunctions.showToast("Service disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        offlineValues = AppPreferenceManager.getOfflineValues();

        relPendingTest = findViewById(R.id.relPendingTest);
        txtPendingTestName = findViewById(R.id.txtPendingTestName);
        txtTimerValue = findViewById(R.id.txtTimerValue);

        spinner_exam = findViewById(R.id.spinner_exam);
        btnSync = findViewById(R.id.btnSync);
        recyclerView = findViewById(R.id.free_test_recycle);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
//        if (InternetCheck.isInternetOn(this)) {
        getExamTopic();
//        } else {
//            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
//        }

        /*if (AppPreferenceManager.getExam() != null) {
            relPendingTest.setVisibility(View.VISIBLE);
        } else {
            relPendingTest.setVisibility(View.GONE);
        }*/

        btnSync.setOnClickListener(this);
        relPendingTest.setOnClickListener(this);
    }

    private void getExamTopic() {
        final StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_COURSE, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("FullResponse", "onResponse: " + response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        final JSONArray jsonArray = object.getJSONArray("Courses");
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
                        initCoursesUI();
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

        //************************************************************************************************************
        //************************************************************************************************************
        //***************************** CHECK IF COURSES EXIST IN DB, IF NOT FETCH FROM API **************************
        //************************************************************************************************************
        //************************************************************************************************************

        mCourseDBUseCases = new CourseDBUseCases(new DBOperationsHelper() {
            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);

                if (list.isEmpty()) {
                    request.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppWebService.getInstance(getApplicationContext()).addToRequestQueue(request);
                } else {
                    mCourseDBUseCases.getConvertedCourseListUsecase();
                }
            }

            @Override
            public <T> void onListConvertedToLocalType(List<T> list) {
                super.onListConvertedToLocalType(list);
                for (T item : list) {
                    CoursesSetterGetter coursesSetterGetter = (CoursesSetterGetter) item;
                    courseList.add(coursesSetterGetter);
                    strings.add(coursesSetterGetter.getCourse_name());
                }
                initCoursesUI();
            }

        });
        mCourseDBUseCases.getAllCoursesUsecase();

        //************************************************************************************************************
        //************************************************************************************************************
        //***************************** CHECK IF COURSES EXIST IN DB, IF NOT FETCH FROM API **************************
        //************************************************************************************************************
        //************************************************************************************************************

    }

    private void initCoursesUI() {

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
                getFullTest(courseList.get(position).getCourse_id());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void getFullTest(String courseId) {
        mCategoryDBUseCases = new CategoryDBUseCases(new DBOperationsHelper() {
            @Override
            public <T> void onItemListSearched(List<T> list) {
                super.onItemListSearched(list);
                if (list.isEmpty()) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.full_screen_progress_bar);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(true);
                    dialog.show();
                    final StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_TEST, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status_code");
                                String message = object.getString("message");
                                Log.e("onResponse: ", status);
                                if (status.equalsIgnoreCase("200")) {
                                    JSONArray jsonArray = object.getJSONArray("question_list");
                                    QuizTopic quizTopic;
                                    fullTestTopics.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object1 = jsonArray.getJSONObject(i);
                                        quizTopic = new QuizTopic(object1.getString("category_id"), object1.getString("category_name"),
                                                object1.getString("total_quiz"), object1.getString("section_id"),
                                                object1.getString("full_length_test"), object1.getString("section_test"),
                                                object1.getString("previous_year_test"));
                                        fullTestTopics.add(quizTopic);
                                    }
                                    setQuizTopicAdapter();
                                } else {
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
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new Hashtable<>();
                            params.put("exam_id", getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE).getString("exam_id", null));
                            Log.d("FullTest", "getParams: " + params);
                            return params;
                        }
                    };
                    request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppWebService.getInstance(MainActivity.this).addToRequestQueue(request);
                } else {
                    fullTestTopics.clear();
                    for (T item : list) {
                        Category category = (Category) item;
                        fullTestTopics.add(mCategoryDBUseCases.getConvertedCategoryUsecase(category));
                        setQuizTopicAdapter();
                    }
                    setQuizTopicAdapter();
                }
            }
        });
        mCategoryDBUseCases.getCategoryListByCourseIdUsecase(courseId);
    }

    private void setQuizTopicAdapter() {
        recyclerView.setVisibility(View.VISIBLE);
        FullTestTopicAdapter fullTestTopicAdapter = new FullTestTopicAdapter(getApplicationContext(), fullTestTopics);
        recyclerView.setAdapter(fullTestTopicAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSync:
                Intent startIntent = new Intent(MainActivity.this, DatabaseInitizationService.class);
                startService(startIntent);
                bindService(startIntent, mServiceConnection, 0);
                break;

            case R.id.relPendingTest:

                String testType = offlineValues.getString("testType");
                if (TextUtils.equals(testType, FullTestQuizActivity.class.getSimpleName())) {
                    Intent intent = new Intent(this, FullTestQuizActivity.class);
                    intent.putExtra("is_from_home", true);
                    startActivity(intent);
                }
                break;
        }
    }
}
