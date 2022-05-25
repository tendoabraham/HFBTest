package com.elmahousingfinanceug.recursiveClasses;

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
    private static ForegroundCheck instance;

    private boolean foreground = false, paused = true;
    private final Handler handler = new Handler();
    private Runnable check;
    private final List<Listener> listeners = new CopyOnWriteArrayList<>();

    public interface Listener {
        void onBecameForeground();
        void onBecameBackground();
    }

    public static void init(Application application) {
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
                            "at least once with parameter init/get");
        }
        return instance;
    }

    public boolean isForeground(){
        return foreground;
    }

    public boolean isBackground(){
        return !foreground;
    }

    void addListener(Listener listener){
        listeners.add(listener);
    }

    void removeListener(Listener listener){
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null) handler.removeCallbacks(check);

        if (wasBackground){
            Log.i(TAG, "went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!", exc);
                }
            }
        } else {
            Log.i(TAG, "still foreground");

        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        paused = true;

        if (check != null) handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable(){
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.i(TAG, "went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            Log.e(TAG, "Listener threw exception!", exc);
                        }
                    }
                } else {
                    Log.i(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (check != null) handler.removeCallbacks(check);
    }
    @Override
    public void onActivityStarted(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {}
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

}