package com.elmahousingfinanceug_test.main_Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;

public class ThemePreference extends BaseAct {
    ScrollView scroll;
    LinearLayout bckColors,white,bLogo_orange,bLightest_blue,blight_grey,bOrange,bLighter_blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_setting);

        gToolBar(getString(R.string.backColor));

        scroll = findViewById(R.id.scroll);
        bckColors = findViewById(R.id.colors);
        white = findViewById(R.id.bWhite);
        bLogo_orange = findViewById(R.id.blogo_orange);
        bLightest_blue = findViewById(R.id.blightest_blue);
        blight_grey = findViewById(R.id.blight_grey);
        bOrange = findViewById(R.id.borange);
        bLighter_blue = findViewById(R.id.blighter_blue);

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                white();
            }
        });

        bLogo_orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orange();
            }
        });

        bLightest_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blue();
            }
        });

        blight_grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                light_grey();
            }
        });

        bOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oOrange();
            }
        });

        bLighter_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lLBlue();
            }
        });

    }

    public void white(){
        am.saveTheme("White");
        am.animate_View(white);
        am.animate_ViewZz(white);
        scroll.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void orange(){
        am.saveTheme("Orange");
        am.animate_View(bLogo_orange);
        am.animate_ViewZz(bLogo_orange);
        scroll.setBackgroundColor(getResources().getColor(R.color.logo_orange));
    }

    public void blue(){
        am.saveTheme("LBlue");
        am.animate_View(bLightest_blue);
        am.animate_ViewZz(bLightest_blue);
        scroll.setBackgroundColor(getResources().getColor(R.color.lightest_blue));
    }

    public void light_grey(){
        am.saveTheme("LGrey");
        am.animate_View(blight_grey);
        am.animate_ViewZz(blight_grey);
        scroll.setBackgroundColor(getResources().getColor(R.color.light_grey));
    }

    public void oOrange (){
        am.saveTheme("oOrange");
        am.animate_View(bOrange);
        am.animate_ViewZz(bOrange);
        scroll.setBackgroundColor(getResources().getColor(R.color.orange));
    }

    public void lLBlue (){
        am.saveTheme("lLBlue");
        am.animate_View(bLighter_blue);
        am.animate_ViewZz(bLighter_blue);
        scroll.setBackgroundColor(getResources().getColor(R.color.blue));
    }
}
