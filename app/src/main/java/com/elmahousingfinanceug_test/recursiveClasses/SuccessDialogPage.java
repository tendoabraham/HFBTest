package com.elmahousingfinanceug_test.recursiveClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Settings;

import java.util.Calendar;

public class SuccessDialogPage extends BaseAct {
    ImageView successImage;
    TextView messageText,okBTN;
    DataBaseClass db;
    int animationTime = 1900;
    String dateAndTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_screen);

        db = new DataBaseClass(this);
        successImage = findViewById(R.id.successImage);
        messageText = findViewById(R.id.messageText);
        okBTN = findViewById(R.id.okBTN);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            messageText.setText(extras.getString("message"));
            dateAndTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            db.InsertLog(am.E_P(dateAndTime),am.E_P(messageText.getText().toString().trim()));
        }

        YoYo.with(Techniques.BounceInDown).duration(animationTime).playOn(successImage);
        YoYo.with(Techniques.FadeInRight).duration(animationTime).playOn(findViewById(R.id.messageText));
        YoYo.with(Techniques.SlideInUp).duration(animationTime).playOn(findViewById(R.id.okBTN));

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (am.getChangePin() && !am.getChangeTrxPin()) {
                    am.nJe();
                    am.ToastMessageLong(getApplicationContext(),getString(R.string.loginNpin));
                } else if(am.getFirstTimeUser()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        am.saveChangePin(false);
        am.saveChangeTrxPin(false);
        super.onDestroy();
    }
}
