package com.elmahousingfinanceug.launch.rao;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.elmahousingfinanceug.R;

import java.io.ByteArrayOutputStream;


/**
 * Created by Brian.Tuitoek on 11/13/2018.
 */

public class ImagePick extends RelativeLayout {
    TextView textView;
    public ImageView imageView,
    placeholder;
    TextView tag;
    Context c;
    int picked = 0;
    public ImagePick(Context context) {
        super(context);
        this.c = context;
        init(context,null);
    }

    public ImagePick(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public ImagePick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        inflate(getContext(), R.layout.joe_imageview, this);
        this.imageView = findViewById(R.id.imageView);
        this.textView = findViewById(R.id.textView);
        this.placeholder = findViewById(R.id.placeholder);
        this.tag = findViewById(R.id.textViewtag);

        if(attributeSet != null){
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.cicEditText);
            try {

                if(typedArray.hasValue(R.styleable.cicEditText_label)){
                    textView.setText(typedArray.getString(R.styleable.cicEditText_label));
                }
            } finally {
                typedArray.recycle();
            }
        }
    }


    public String getImage(){
       /* BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bb = bos.toByteArray();
        return Base64.encodeToString(bb,Base64.DEFAULT);*/

        Drawable d = imageView.getDrawable();
        Bitmap bitmapOrg1 = ((BitmapDrawable) d).getBitmap();
       /* ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
        bitmapOrg1.compress(Bitmap.CompressFormat.JPEG, 20, bao1);
        byte[] ba1 = bao1.toByteArray();*/
        return ConvertImageToBase64(bitmapOrg1);
    }

    public static String ConvertImageToBase64(Bitmap bitmap) {
        String generatedBase64 = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream2);
            byte[] bytes2 = byteArrayOutputStream2.toByteArray();
            generatedBase64 = Base64.encodeToString(bytes2, Base64.DEFAULT);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return generatedBase64;
    }
    public void setImage(Bitmap img){
        imageView.setImageBitmap(img);
        picked = 1;
        placeholder.setVisibility(GONE);
    }
    public void setLabel(String label){
        textView.setText(label);
    }
    public String getLabel(){
        return textView.getText().toString();
    }
    public String getTag(){

        return tag.getText().toString();
    }
    public void setTag(String label){
        tag.setText(label);
    }


    public boolean  isValid()
    {
        if (picked == 0) {
            //Toasty.info(c, "Pick an image for " + getLabel(), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        else
            return true;

    }

    public void setImageBase64(String pass) {
        byte[] decodedString = Base64.decode(pass, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        setImage(decodedByte);
    }
}
