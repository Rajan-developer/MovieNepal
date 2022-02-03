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

    //convert the date yy-mm-dd format to 22 Jan 20121 format
    public static String DateConverter(String date) {

        int firstDashPos = date.indexOf("-");
        int secondDashIndex = date.lastIndexOf("-");
        String day = date.substring(secondDashIndex + 1, date.length());
        String month = date.substring(firstDashPos + 1, secondDashIndex);
        String year = date.substring(0, firstDashPos);

        day = DayConverter(Integer.valueOf(day));
        month = MonthConverter(Integer.valueOf(month));

        return day + " " + month + ", " + year;

    }

    public static String DayConverter(int day) {
        String newDay;
        if (day < 10) newDay = "0" + day;
        else newDay = String.valueOf(day);

        return newDay;
    }

    public static String MonthConverter(int month) {
        String newMonth;
        switch (month) {
            case 1:
                newMonth = "Jan";
                break;
            case 2:
                newMonth = "Feb";
                break;
            case 3:
                newMonth = "Mar";
                break;
            case 4:
                newMonth = "Apr";
                break;
            case 5:
                newMonth = "May";
                break;
            case 6:
                newMonth = "Jun";
                break;
            case 7:
                newMonth = "Jul";
                break;
            case 8:
                newMonth = "Aug";
                break;
            case 9:
                newMonth = "Sep";
                break;
            case 10:
                newMonth = "Oct";
                break;
            case 11:
                newMonth = "Nov";
                break;
            case 12:
                newMonth = "Dec";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }

        return newMonth;
    }


    //convert video run time to ..hr ..min
    public static String timeConverter(long movieRunTime) {
        String timeInterval = "";

        String hour = String.valueOf(movieRunTime / 60) + " hr";
        String min = String.valueOf(movieRunTime % 60) + " mins";


        return timeInterval = hour + " " + min;

    }

}
