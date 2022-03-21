package com.elmahousingfinanceug_test.recursiveClasses;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ForegroundCheck implements Application.ActivityLifecycleCallbacks {

    private static final long CHECK_DELAY = 600;
    private static final String TAG = ForegroundCheck.class.getName();

    public interface Listener {
        void onBecameForeground();
        void onBecameBackground();
    }

    private static ForegroundCheck instance;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<>();
    private Runnable check;

    /**
     * Its not strictly necessary to use this method - _usually_ invoking
     * get with a Context gives us a path to retrieve the Application and
     * initialise, but sometimes (e.g. in test harness) the ApplicationContext
     * is != the Application, and the docs make no guarantees.
     * @param application****
     * @return an initialised ForegroundCheck instance
     */
    public static void init(Application application){
        if (instance == null) {
            instance = new ForegroundCheck();
            application.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static ForegroundCheck get(Application application){
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static ForegroundCheck get(Context ctx){
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application)appCtx);
            }
            throw new IllegalStateException(
                    "ForegroundCheck is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    public static ForegroundCheck get(){
        if (instance == null) {
            throw new IllegalStateException(
                    "ForegroundCheck is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground){
            Log.e(TAG, "went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }
        } else {
            Log.e(TAG, "still foreground");

        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.e(TAG, "went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            Log.e(TAG, "Listener threw exception!", exc);
                        }
                    }
                } else {
                    Log.e(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }
    //use if necessary
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override
    public void onActivityStarted(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {}
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (check != null)
            handler.removeCallbacks(check);
    }
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

}