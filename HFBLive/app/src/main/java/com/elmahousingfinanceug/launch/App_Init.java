package com.elmahousingfinanceug.launch;

import android.content.Context;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.ForegroundCheck;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;

public class App_Init extends MultiDexApplication {
    private static int themeId;
    private static String themeSetting;

    @Override
    public void onCreate() {
        super.onCreate();
        reloadTheme(this);
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        ForegroundCheck.init(this);
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .certificatePinner(new CertificatePinner.Builder()
                        .add("app.craftsilicon.com", "sha256/yReO2aD62JQk2BVKHaE+mwoy9624bigH6e81cmz30gw=")
                        .add("app.craftsilicon.com", "sha256/RRM1dGqnDFsCJXBTHky16vi1obOlCgFFn/yOhI/y+ho=")
                        .add("app.craftsilicon.com", "sha256/WoiWRyIOVNa9ihaBciRSC7XHjliYS9VwUGOIud4PB18=")
                        .build())
                .connectTimeout(55, TimeUnit.SECONDS)
                .readTimeout(55, TimeUnit.SECONDS)
                .writeTimeout(55, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(this,okHttpClient);
    }

    public static int getThemeId()
    {
        return themeId;
    }

    public static String getThemeSetting()
    {
        return themeSetting;
    }

    public static void reloadTheme(Context context) {
        themeSetting = PreferenceManager.getDefaultSharedPreferences(context).getString("theme", "");
        assert themeSetting != null;
        switch (themeSetting){
            case "":
                themeId = R.style.Theme_I;
                break;
            case "White":
                themeId = R.style.Theme_II;
                break;
            case "Orange":
                themeId = R.style.Theme_III;
                break;
            case "LBlue":
                themeId = R.style.Theme_IV;
                break;
            case "LGrey":
                themeId = R.style.Theme_V;
                break;
            case "oOrange":
                themeId = R.style.Theme_VI;
                break;
            case "lLBlue":
                themeId = R.style.Theme_VII;
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        deleteCache(this);
        super.onLowMemory();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.getMessage();
        }
    } public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }
}
