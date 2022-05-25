package com.elmahousingfinanceug.launch.rao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.elmahousingfinanceug.R;


public class CustomerCat extends RelativeLayout {

    Context cc;
    TextView txt_iam_new, txt_new, txt_iam_exist, txt_exist;
    ImageView new_image, existing_image;
    LinearLayout new_Lay, existing_lay;
    private String selectedCategory = "";
    private int selectedID=0;

    public CustomerCat(Context context) {
        super(context);
        this.cc = context;
        init(context,null);
    }

    public CustomerCat(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public CustomerCat(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        inflate(getContext(), R.layout.customer_cat_, this);
        this.new_Lay = findViewById(R.id.new_Lay);
        this.new_image = findViewById(R.id.new_image);
        this.existing_lay = findViewById(R.id.existing_lay);
        this.existing_image = findViewById(R.id.existing_image);
        this.txt_iam_new = findViewById(R.id.txt_iam_new);
        this.txt_new = findViewById(R.id.txt_new);
        this.txt_iam_exist = findViewById(R.id.txt_iam_exist);
        this.txt_exist = findViewById(R.id.txt_exist);

        new_Lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new_image.setImageDrawable(getResources().getDrawable(R.drawable.new_customer_white));
                existing_image.setImageDrawable(getResources().getDrawable(R.drawable.existing_blue));
                new_Lay.setBackground(getResources().getDrawable(R.drawable.button_bg_category));
                existing_lay.setBackground(getResources().getDrawable(R.drawable.button_bg_a));
                txt_iam_new.setTextColor(Color.WHITE);
                txt_new.setTextColor(Color.WHITE);
                txt_iam_exist.setTextColor(Color.BLACK);
                txt_exist.setTextColor(Color.BLACK);
                selectedCategory = "New Customer";
                selectedID = v.getId();
                Intent k = new Intent("populate");
                k.putExtra("Type", "New Customer");
              

                context.sendBroadcast(k);
            }
        });

        existing_lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                new_image.setImageDrawable(getResources().getDrawable(R.drawable.new_customer_blue));
                existing_image.setImageDrawable(getResources().getDrawable(R.drawable.existing_white));
                new_Lay.setBackground(getResources().getDrawable(R.drawable.button_bg_a));
                existing_lay.setBackground(getResources().getDrawable(R.drawable.button_bg_category));
                txt_iam_new.setTextColor(Color.BLACK);
                txt_new.setTextColor(Color.BLACK);
                txt_iam_exist.setTextColor(Color.WHITE);
                txt_exist.setTextColor(Color.WHITE);
                selectedCategory = "Existing Customer";
                selectedID = v.getId();
                Intent k = new Intent("populate");
                k.putExtra("Type", "Existing Customer");
          

                context.sendBroadcast(k);
            }
        });
    }

    public String getSelectedCategory(){
        return selectedCategory;
    }

    public int getSelectedCatID(){
        return selectedID;
    }
}
