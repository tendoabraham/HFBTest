package com.elmahousingfinanceug_test.launch.rao;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.elmahousingfinanceug_test.launch.rao.ocr.OCRState;
import com.example.icebergocr.utils.OCRData;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OcrResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OCRData ocrData = EventBus.getDefault()
                .getStickyEvent(OCRData.class);

        new OCRState(this).save(ocrData);
        finish();

//        String[] names = ocrData.getGivenName().split(" ");
//        String firstName = ocrData.getGivenName();
//        String  secondName = "";
//
//        if (names.length > 1)
//            for (int i = 0; i < names.length; i++) {
//                firstName = names[0];
//                secondName=names[1];
//            }


    }


    private void killEvent() {
        EventBus.getDefault().removeStickyEvent(OCRData.class);
        EventBus.getDefault().unregister(this);
    }

    private void startEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        killEvent();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onMessageEvent(OCRData event) {
        Log.e("EVENT BUS", new Gson().toJson(event));
    }
}
