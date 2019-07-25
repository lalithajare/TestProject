package com.example.testproject.Utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppWebService extends Application {
    private static AppWebService mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    public AppWebService(){

    }
    private AppWebService(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AppWebService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppWebService(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
    }

}
