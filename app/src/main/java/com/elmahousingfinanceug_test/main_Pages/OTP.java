package com.elmahousingfinanceug_test.main_Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity implements ResponseListener {
    private EditText edtOTP;
    private TextView send, txtDidNot,resend;
    private CountDownTimer countDownTimer;
    private AllMethods am;
    private String Merchant, quest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Merchant = getIntent().getStringExtra("Merchant");
        am = new AllMethods(this);
        am.disableScreenShot(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        edtOTP = findViewById(R.id.edtOTP);
        txtDidNot = findViewById(R.id.txtDidNot);
        resend = findViewById(R.id.resend);

        toolbar.setTitle(getString(R.string.otpCap));

        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtOTP.getText().length() >= 6) {
                    verification();
//                    String otp = edtOTP.getText().toString();
//                    am.saveOTPStatus("1");
//                    am.saveOTP(otp);
//                    finish();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        RegisterNewListener();
        generate();

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generate();
            }
        });
    }

//    public void oneTime(View w) {
//        generate();
//
//        /*
//        switch (w.getId()) {
//            case R.id.send:
//                if (edtOTP.getText().length() < 6) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.otpCap).concat(" ").concat(getString(R.string.required)));
//                } else {
//                    verification();
//                }
//                break;
//            case R.id.resend:
//                generate();
//                break;
//        }*/
//    }

    private void generate() {
        quest = (
//                "FORMID:O-OTPCREATE:" +
//                        "SERVICENAME:" + Merchant + ":" +
//                        "CUSTOMERMOBILENUMBER:" + am.getUserPhone() + ":" +
//                        "BANKID:" + am.getBankID() + ":"

//        "FORMID:O-OTPCREATE:CUSTOMERMOBILENUMBER:" + am.getUserPhone() +
//                ":SERVICENAME:" + Merchant +
//                ":CUSTOMERID:" + am.getCustomerID()+
//                ":APPNAME:" + am.getBankName()+
//                ":BANKID:"+ am.getBankID()  +
//                ":TRXSOURCE:APP"

//        "FORMID:O-OTPCREATE:SERVICENAME:" + Merchant +
//                ":CUSTOMERMOBILENUMBER:" + am.getUserPhone() +
//                ":BANKID:"+ am.getBankID()  +
//                ":CUSTOMERID:" + am.getCustomerID()+
//                ":TRXSOURCE:APP"

//                "FORMID:O-OTPCREATE" +
//                        ":SERVICENAME:" + am.getMerchantID() +
//                        ":INFOFIELD7:" + am.getMerchantID() +
//                        ":EMAILID:tendoabraham@gmail.com" +
//                        ":CUSTOMERMOBILENUMBER:" + am.getUserPhone() +
//                        ":BANKID:" + am.getBankID() + ":"



                "FORMID:O-OTPCREATE" +
                        ":SERVICENAME:" + am.getMerchantID() +
                        ":INFOFIELD7:" + am.getMerchantID() +
                        ":CUSTOMERMOBILENUMBER:" + am.getUserPhone() +
                        ":BANKID:" + am.getBankID() + ":"
        );

        am.get(this, quest, getString(R.string.generatingOtp),"OC");
    }

    private void verification() {

        quest = (
                "FORMID:O-OTPVERIFY:" +
                        "SERVICENAME:" + Merchant + ":" +
                        "CUSTOMERMOBILENUMBER:" + am.getUserPhone()  + ":" +
                        "OTPKEY:" + edtOTP.getText().toString().trim() + ":" +
                        "BANKID:" + am.getBankID() +":"
        );
        am.get_(this, quest, getString(R.string.verifying),"OV");

    }

    private void RegisterNewListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(aVoid -> registerReceiver(gotten, new IntentFilter("SMSReceived")));
        task.addOnFailureListener(e -> {
            String error = e.toString();
        });
    }

    private final BroadcastReceiver gotten = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            try {
                String theSms = i.getStringExtra("SMSbody");
                String status = i.getStringExtra("STATUS");
                assert status != null;
                switch (status) {
                    case "000":
                        assert theSms != null;
                        edtOTP.setText(theSms.trim());
                        break;
                    case "091":
                        unregisterReceiver(gotten);
                        am.ToastMessageLong(OTP.this, theSms);
                        RegisterNewListener();
                        break;
                    case "092":
                        am.myDialog(OTP.this, getString(R.string.alert), theSms);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResponse(String response, String step) {
        switch (step) {
            case "OC":
                countDownTimer = new CountDownTimer(150000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        txtDidNot.setText(MessageFormat.format("{0} {1} {2} {3}",
                                getString(R.string.didNot),
                                getString(R.string.resend),
                                getString(R.string.in),
                                String.format(Locale.getDefault(), "%2d min : %02d sec.",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60)));

                        txtDidNot.setVisibility(View.VISIBLE);
                        //proceed.setVisibility(View.VISIBLE);
                        resend.setVisibility(View.GONE);
                    }
                    public void onFinish() {
                        edtOTP.setText("");
                        txtDidNot.setVisibility(View.GONE);
                        resend.setVisibility(View.VISIBLE);
                    }
                };
                countDownTimer.start();
                break;
            case "OV":
                if(countDownTimer!=null) {
                    countDownTimer.cancel();
                }

                am.saveOTPStatus("1");
                finish();
                /*
                switch (Merchant) {
                    case "PINRESET":
                        finish();
                        startActivity(new Intent(this, PinReset.class));
                        break;
                    case "SELFREG":
                        finishAffinity();
                        startActivity(new Intent(this, Login.class).putExtra("gone", true));
                        break;
                    case "RAO":
                        am.saveRaoVerify(true);
                        finish();
                        break;
                    default:
                        finish();
                        break;
                }*/
                break;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if(countDownTimer!=null) {
                countDownTimer.cancel();
            }
            unregisterReceiver(gotten);
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}