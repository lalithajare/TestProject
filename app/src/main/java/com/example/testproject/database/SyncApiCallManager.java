package com.example.testproject.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.testproject.Adapter.FullTestQuizAdapter;
import com.example.testproject.Adapter.FullTestTopicAdapter;
import com.example.testproject.Model.QuizTopic;
import com.example.testproject.Model.TestPattern;
import com.example.testproject.Model.TestQuizSetGet;
import com.example.testproject.URLs.UrlsAvision;
import com.example.testproject.Utils.AppWebService;
import com.example.testproject.Utils.Const;
import com.example.testproject.common.MyApplication;
import com.example.testproject.database.db_usecases.CourseDBUseCases;
import com.example.testproject.database.models.Category;
import com.example.testproject.database.operators.DBOperationsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import static com.example.testproject.Utils.Const.patternArrayList;

public class SyncApiCallManager {

    private Context mContext;
    private static SyncApiCallManager mSyncApiCallManager;

    private SyncApiCallManager(Context context) {
        mContext = context;
    }

    public static SyncApiCallManager getInstance(Context context) {

        if (mSyncApiCallManager == null) {
            mSyncApiCallManager = new SyncApiCallManager(context);
        }

        synchronized (mSyncApiCallManager) {
            return mSyncApiCallManager;
        }
    }

    //************************************************************************************************************
    //******************************************* COURSE API *****************************************************
    //************************************************************************************************************

    public void callCourseAPI(final ApiResponseListener listener) {
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
                        listener.onSuccess(jsonArray.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
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
        AppWebService.getInstance(mContext).addToRequestQueue(request);
    }
    //************************************************************************************************************
    //******************************************* COURSE API *****************************************************
    //************************************************************************************************************

//******************************************************************************************************************************************************

    //************************************************************************************************************
    //******************************************* CATEGORY API ***************************************************
    //************************************************************************************************************

    public void callCategoryListAPI(final String courseId, final ApiResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_TEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    Log.e("onResponse: ", status);
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray("question_list");
                        listener.onSuccess(jsonArray.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("exam_id", courseId);
                Log.d("FullTest", "getParams: " + params);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(mContext).addToRequestQueue(request);
    }

    //************************************************************************************************************
    //******************************************* CATEGORY API ***************************************************
    //************************************************************************************************************

//**********************************************************************************************************************************

    //************************************************************************************************************
    //******************************************* QUIZ API ***************************************************
    //************************************************************************************************************

    public void callQuizAPI(final Category category, final ApiResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.URL_FULL_LENGTH_QUIZ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status_code");
                    String message = object.getString("message");
                    Log.e("onResponse: ", status);
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray("question_list");
                        listener.onSuccess(jsonArray.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("category_id", category.getCategoryId());
                params.put("student_id", "6");
                params.put("section_id", category.getSectionId());
                params.put("question_year_stat", "0");
                Log.d("category_id", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(mContext).addToRequestQueue(request);
    }

    //************************************************************************************************************
    //******************************************* QUIZ API ***************************************************
    //************************************************************************************************************


    public void callSubmitAnswerAPI(final String testId, final String questId, final String answersId, final ApiResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, UrlsAvision.SAVE_FULL_ANS, new Response.Listener<String>() {
            @SuppressLint("SimpleDateFormat")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status_code");
                    String message = jsonObject.getString("message");
                    Log.e("Result Status", status);
                    if (status.equalsIgnoreCase("200")) {
                        listener.onSuccess(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new Hashtable<>();
                params.put("test_id", testId);
                params.put("qus_id", questId);
                params.put("answers_id", answersId);
                params.put("question_status", "1");
                Log.d("SubmitFullAnswerValue", "getParams: " + params);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppWebService.getInstance(MyApplication.getAppInstance()).addToRequestQueue(request);
    }

    public interface ApiResponseListener {
        void onSuccess(String response);

        void onError(VolleyError error);
    }

}
