package com.example.testproject.Utils;

import android.widget.Toast;

import com.example.testproject.common.MyApplication;

public class UtilFunctions {

    public static void showToast(String msg) {
        Toast.makeText(MyApplication.getAppInstance(), msg, Toast.LENGTH_SHORT).show();
    }


}
