package com.kshprimeindustries.tuneupme;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

import com.kshprimeindustries.tuneupme.listeners.ActivityTracker;
import com.kshprimeindustries.tuneupme.listeners.FlipService;
import com.kshprimeindustries.tuneupme.listeners.NetworkChangeReceiver;

public class TuneUpMeApplication extends Application {

    public static String ngrock_url = "https://cd5f-111-223-180-137.ngrok-free.app";
    public static int sqliteVersion = 2;


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ActivityTracker.getInstance().register(this);
        startService(new Intent(this, FlipService.class));

    }


}
