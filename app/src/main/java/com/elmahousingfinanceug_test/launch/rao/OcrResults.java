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
