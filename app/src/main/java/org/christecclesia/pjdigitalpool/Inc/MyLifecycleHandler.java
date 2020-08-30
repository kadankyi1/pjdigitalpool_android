package org.christecclesia.pjdigitalpool.Inc;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by zatana on 8/19/19.
 */

public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    private static Activity currentActivity = null;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
        // android.util.Log.w("MyLifecycleHandler", "application is in foreground: " + (resumed > paused));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        //android.util.Log.w("MyLifecycleHandler", "application is visible: " + (started > stopped));
    }



    // And these two public static functions
    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static Activity getCurrentActivity(){return currentActivity;}

    public static boolean isApplicationInForeground() {
        return resumed > paused;
    }

}
