package com.kshprimeindustries.tuneupme.listeners;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.kshprimeindustries.tuneupme.TuneUpMeActivityNetworkNotAvailable;

import es.dmoral.toasty.Toasty;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public   String context;

    @Override
    public void onReceive(Context context, Intent intent) {

//        this.context = context.toString();
        if (isNetworkAvailable(context)) {

            Activity currentActivity = ActivityTracker.getInstance().getCurrentActivity();

            if (currentActivity != null) {
                if (currentActivity.getClass().getSimpleName().equals("TuneUpMeActivityNetworkNotAvailable")){

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (currentActivity != null) {
                            currentActivity.onBackPressed();
                        }
                    });

                }
            }

        } else {

            Intent activityIntent = new Intent(context, TuneUpMeActivityNetworkNotAvailable.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // ✅ Fixes the crash
            context.startActivity(activityIntent);
//            Intent intent1 = new Intent(context, TuneUpMeActivityNetworkNotAvailable.class);
//            context.startActivity(intent1);
        }
    }



    public boolean isNetworkAvailable(Context context) {
        this.context = context.toString();
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.net.Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false;

                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }


}
