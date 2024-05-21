package com.elmahousingfinanceug_test.launch.rao.ocr;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.icebergocr.utils.OCRData;

public class OCRState {

    private Context context;
    private SharedPreferences sharedPreferences;

    public OCRState(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("OCR", Context.MODE_PRIVATE);
    }

    public void save(OCRData ocrData) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data", new OCRTypeConverter().covert(ocrData));
        editor.apply();
    }

    public OCRData ocrData() {
        return new OCRTypeConverter().covert(sharedPreferences.getString("data", ""));
    }

}
