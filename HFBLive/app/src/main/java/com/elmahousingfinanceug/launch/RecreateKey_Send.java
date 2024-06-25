package com.elmahousingfinanceug.launch;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.launch.rao.AccountOpenSplash;
import com.elmahousingfinanceug.main_Pages.Contact_Us;
import com.elmahousingfinanceug.recursiveClasses.AllMethods;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.elmahousingfinanceug.recursiveClasses.VolleyResponse;

public class RecreateKey_Send extends AppCompatActivity implements ResponseListener, VolleyResponse {
    private EditText eTPhone,eTKey,eTPin;
    private RadioButton bank,sendMe;
    private Dialog rnD;
    private AllMethods am;
    private String quest="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreate_key_s);
        am = new AllMethods(this);
        am.disableScreenShot(this);

        eTPhone = findViewById(R.id.eTPhone);
        eTPin = findViewById(R.id.eTPin);
        eTKey = findViewById(R.id.eTKey);
        RadioGroup group = findViewById(R.id.group);
        bank = findViewById(R.id.bank);
        sendMe = findViewById(R.id.sendMe);

        eTPin.setVisibility(View.GONE);
        eTKey.setVisibility(View.GONE);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (bank.isChecked()) {
                    eTPin.setVisibility(View.GONE);
                    eTKey.setVisibility(View.VISIBLE);
                } else if (sendMe.isChecked()) {
                    eTPin.setVisibility(View.VISIBLE);
                    eTKey.setVisibility(View.GONE);
                }
            }
        });

        eTPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(RecreateKey_Send.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) eTPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void rKeySend(View rB) {
        switch (rB.getId()){
            case R.id.continue1:
                if (eTPhone.getText().toString().trim().length() < 5) {
                    am.myDialog(this,getString(R.string.alert),getString(R.string.invalidMobileNumber));
                } else if (!bank.isChecked() && !sendMe.isChecked()) {
                    am.myDialog(this,getString(R.string.alert),getString(R.string.select1Option));
                } else if (bank.isChecked() && eTKey.getText().toString().trim().length() < 6) {
                    am.myDialog(this,getString(R.string.alert),getString(R.string.invalidRKey));
                } else if (sendMe.isChecked() && eTPin.getText().toString().trim().length() < 4) {
                    am.myDialog(this,getString(R.string.alert),getString(R.string.invalidPin));
                } else {
                    am.saveUserPhone(eTPhone.getText().toString().trim());
                    if (bank.isChecked()) {
                        quest = (
                                "FORMID:VERIFYRECREATEKEY:" +
                                        "MOBILENUMBER:" + am.getUserPhone() + ":" +
                                        "KEY:"+ eTKey.getText().toString().trim() + ":"
                        );
                        //am.connectOldTwo(getString(R.string.verifying),quest,this,"RV");
                        am.get_(this,quest,getString(R.string.verifying),"RV");
                    } else {
                        rnD = new Dialog(this);
                        //noinspection ConstantConditions
                        rnD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        rnD.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        rnD.setContentView(R.layout.dialog_confirm);
                        final TextView txtMessage = rnD.findViewById(R.id.dialog_message),
                                txtNo = rnD.findViewById(R.id.noBTN),
                                txtOk = rnD.findViewById(R.id.yesBTN);
                        txtMessage.setText(String.format("%s %s.",
                                getText(R.string.smsKey),
                                am.getUserPhone()));
                        txtNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rnD.cancel();
                            }
                        });
                        txtOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quest = (
                                        "FORMID:RECREATEKEY:" +
                                                "MOBILENUMBER:" + am.getUserPhone()  + ":" +
                                                "LOGINMPIN:" + eTPin.getText().toString().trim() + ":"
                                );
                                //am.connectOldTwo(getString(R.string.processingReq),quest,RecreateKey_Send.this,"RS");
                                am.get_(RecreateKey_Send.this,quest,getString(R.string.processingReq),"RS");
                                rnD.cancel();
                            }
                        });
                        rnD.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                eTPin.setText("");
                                dialog.dismiss();
                            }
                        });
                        rnD.show();
                    }
                }
                break;

            case R.id.account_opening:
                startActivity(new Intent(this, AccountOpenSplash.class));
                break;
            case R.id.contactBank:
                startActivity(new Intent(this,Contact_Us.class));
                break;
        }
    }

    private final BroadcastReceiver noConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            if (am.netDialog == null || !am.netDialog.isShowing() ) am.promptUser(RecreateKey_Send.this);
        }
    };

    private final BroadcastReceiver thereIsConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            if (am.netDialog != null) am.netDialog.dismiss();
        }
    };

    @Override
    protected void onResume(){
        eTPin.setText("");
        registerReceiver(noConnection,new IntentFilter("OFF"));
        registerReceiver(thereIsConnection,new IntentFilter("ON"));
        registerReceiver(am.Network,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    public void onPause(){
        try {
            if(rnD !=null) rnD.dismiss();
            if(am.netDialog!=null) am.netDialog.dismiss();
            unregisterReceiver(am.Network);
            unregisterReceiver(thereIsConnection);
            unregisterReceiver(noConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step){
            case "RV":
                am.saveCustomerID(response.trim());
                finish();
                startActivity(new Intent(this,Login.class));
                break;
            case "RS":
                am.ToastMessageLong(this,response);
                finish();
                startActivity(new Intent(this,RecreateKey_Verify.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if(am.mDialog!=null) am.mDialog.dismiss();
            am.progressDialog("0");
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
