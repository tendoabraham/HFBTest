package com.elmahousingfinanceug_test.launch.rao;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;


public class joeSingleChoice extends RelativeLayout {
    Context cc;
    TextView option, option2, option3, option4;
    ImageView imga,imgb,imgc,imgd;
    LinearLayout aMr, bMrs, cMiss, dOther;
    EditText otherEditText;
    private String selectedTitle = "Mr";
    private int selectedTitleViewID = 0;

    public joeSingleChoice(Context context) {
        super(context);
        this.cc = context;
        init(context,null);
    }


    public joeSingleChoice(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public joeSingleChoice(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        inflate(getContext(), R.layout.joe_multichoice, this);
        this.aMr = findViewById(R.id.aMr);
        this.bMrs = findViewById(R.id.bMrs);
        this.cMiss = findViewById(R.id.cMiss);
        this.dOther = findViewById(R.id.dOther);
        this.imga = findViewById(R.id.aimg);
        this.imgb = findViewById(R.id.bimg);
        this.imgc = findViewById(R.id.cimg);
        this.imgd = findViewById(R.id.imgd);
        this.option = findViewById(R.id.option);
        this.option2 = findViewById(R.id.option2);
        this.option3 = findViewById(R.id.option3);
        this.option4 = findViewById(R.id.option4);
        this.otherEditText = findViewById(R.id.otherEditText);

        final AllMethods p = new AllMethods(context);

        aMr.setOnClickListener(v -> {
            imga.setBackground(getResources().getDrawable(R.drawable.choicebtn_selected));
            imgb.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgc.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgd.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            selectedTitle = "Mr";
            option.setTextColor(Color.parseColor("#32A6DE"));
            option2.setTextColor(Color.parseColor("#3D3D3D"));
            option3.setTextColor(Color.parseColor("#3D3D3D"));
            option4.setTextColor(Color.parseColor("#3D3D3D"));
            selectedTitleViewID = v.getId();
            otherEditText.setVisibility(GONE);
        });

        bMrs.setOnClickListener(v -> {
            imga.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgb.setBackground(getResources().getDrawable(R.drawable.choicebtn_selected));
            imgc.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgd.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            selectedTitle = "Mrs";
            option2.setTextColor(Color.parseColor("#32A6DE"));
            option.setTextColor(Color.parseColor("#3D3D3D"));
            option3.setTextColor(Color.parseColor("#3D3D3D"));
            option4.setTextColor(Color.parseColor("#3D3D3D"));
            selectedTitleViewID = v.getId();
            otherEditText.setVisibility(GONE);
        });

        cMiss.setOnClickListener(v -> {
            imga.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgb.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgc.setBackground(getResources().getDrawable(R.drawable.choicebtn_selected));
            imgd.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            selectedTitle = "Miss";
            option3.setTextColor(Color.parseColor("#32A6DE"));
            option2.setTextColor(Color.parseColor("#3D3D3D"));
            option.setTextColor(Color.parseColor("#3D3D3D"));
            option4.setTextColor(Color.parseColor("#3D3D3D"));
            selectedTitleViewID = v.getId();
            otherEditText.setVisibility(GONE);
        });

        dOther.setOnClickListener(v -> {
            imga.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgb.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgc.setBackground(getResources().getDrawable(R.drawable.choicebtn_unselected));
            imgd.setBackground(getResources().getDrawable(R.drawable.choicebtn_selected));
            selectedTitle = "Other";
            option3.setTextColor(Color.parseColor("#3D3D3D"));
            option2.setTextColor(Color.parseColor("#3D3D3D"));
            option.setTextColor(Color.parseColor("#3D3D3D"));
            option4.setTextColor(Color.parseColor("#32A6DE"));
            selectedTitleViewID = v.getId();
            otherEditText.setVisibility(VISIBLE);
            if (p.getProceed()) {
                otherEditText.setText(p.getSavedData("OtherTitle"));
            }
        });

        otherEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                p.putSavedData("OtherTitle",s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public String getSelectedTitle(){
        return selectedTitle;
    }

    public int getViewTitleID(){
        return selectedTitleViewID;
    }
}
