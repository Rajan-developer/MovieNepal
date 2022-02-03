package com.rajan.movienepal.utility;

import android.util.Log;

import com.rajan.movienepal.BuildConfig;


/**
 * Created by lokex on 4/16/15.
 */
public class AppLog {

    public static void showLog(String tag, String message){
        if(BuildConfig.DEBUG) Log.i(tag, message);
    }
}
