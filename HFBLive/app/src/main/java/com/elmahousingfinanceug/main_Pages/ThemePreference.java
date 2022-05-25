package com.elmahousingfinanceug.main_Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;

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
    }

    public void white(View h){
        am.saveTheme("White");
        am.animate_View(white);
        am.animate_ViewZz(white);
        scroll.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void orange(View o){
        am.saveTheme("Orange");
        am.animate_View(bLogo_orange);
        am.animate_ViewZz(bLogo_orange);
        scroll.setBackgroundColor(getResources().getColor(R.color.logo_orange));
    }

    public void blue(View b){
        am.saveTheme("LBlue");
        am.animate_View(bLightest_blue);
        am.animate_ViewZz(bLightest_blue);
        scroll.setBackgroundColor(getResources().getColor(R.color.lightest_blue));
    }

    public void light_grey(View lg){
        am.saveTheme("LGrey");
        am.animate_View(blight_grey);
        am.animate_ViewZz(blight_grey);
        scroll.setBackgroundColor(getResources().getColor(R.color.light_grey));
    }

    public void oOrange (View o){
        am.saveTheme("oOrange");
        am.animate_View(bOrange);
        am.animate_ViewZz(bOrange);
        scroll.setBackgroundColor(getResources().getColor(R.color.orange));
    }

    public void lLBlue (View o){
        am.saveTheme("lLBlue");
        am.animate_View(bLighter_blue);
        am.animate_ViewZz(bLighter_blue);
        scroll.setBackgroundColor(getResources().getColor(R.color.blue));
    }
}
