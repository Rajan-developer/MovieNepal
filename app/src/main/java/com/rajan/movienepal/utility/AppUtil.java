package com.rajan.movienepal.utility;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.rajan.movienepal.R;

/**
 * Created by Hp on 5/25/2015.
 */
public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();


    /**
     * find if there is a working internet connection available in device
     */
    public static boolean isInternetConnectionAvailable(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected()) haveConnectedWifi = true;
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected()) haveConnectedMobile = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return haveConnectedWifi || haveConnectedMobile;

    }

    public static void showSnackBar(View view, Context context, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    /* use of AppLog class to get package name*/
    public static String getPackageName(Context context) {
        if (context != null) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                AppLog.showLog("AppUtil", "packageName " + packageInfo.packageName);

                return packageInfo.packageName;
            } catch (PackageManager.NameNotFoundException ex) {
                // should never happen
                throw new RuntimeException("Could not get package name: " + ex);
            }
        } else {
            return "";
        }
    }

}
