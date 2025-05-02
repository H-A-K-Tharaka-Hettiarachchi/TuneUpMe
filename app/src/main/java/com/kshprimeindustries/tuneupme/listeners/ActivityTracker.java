package com.kshprimeindustries.tuneupme.listeners;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityTracker implements Application.ActivityLifecycleCallbacks {
    private static ActivityTracker instance;
    private Activity currentActivity;

    private ActivityTracker() {}

    public static synchronized ActivityTracker getInstance() {
        if (instance == null) {
            instance = new ActivityTracker();
        }
        return instance;
    }

    public void register(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (currentActivity == activity) {
            currentActivity = null;
        }
    }

    // Other lifecycle methods (empty)
    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityStarted(Activity activity) {}
    @Override public void onActivityStopped(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
    @Override public void onActivityDestroyed(Activity activity) {}
}
