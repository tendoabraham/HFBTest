package com.elmahousingfinanceug.main_Pages;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;

public class About_Us extends BaseAct {
    TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        gToolBar(getString(R.string.aboutUs));

        appVersion = findViewById(R.id.appVersion);
        appVersion.setText(String.format("%s %s - %s", getString(R.string.app_name), getString(R.string.version), am.version()));
    }

    public void webCS(View s) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.craftsilicon.com")));
        } catch (ActivityNotFoundException activityException) {
            am.myDialog(this,getString(R.string.error),getString(R.string.linkfailed));
        }
    }
}
