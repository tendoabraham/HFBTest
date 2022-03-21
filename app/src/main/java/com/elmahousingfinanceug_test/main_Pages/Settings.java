package com.elmahousingfinanceug_test.main_Pages;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.launch.App_Init;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.FingerprintUiHelper;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;

public class Settings extends BaseAct implements ResponseListener, VolleyResponse, FingerprintUiHelper.Callback{
    EditText ETOldLPin,ETNewLPin,ETConfirmLPin,ETOldTPin,ETNewTPin,ETConfirmTPin, lCon;
    LinearLayout CLPLayout,CTPLayout,appBar,fingerPrintStuff,pinCon,fingerInput;
    TextView idle_Btn,DisplayCLPBTN,DisplayCTPBTN,displayThemes,fingerprint_status;
    SwitchCompat fingerSwitch;
    ImageView fingerprint_icon;
    Boolean scheduledRestart = false;
    private boolean confirmed = false;
    String quest="",lang="";
    SharedPreferences sp;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new themeListener();
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private FingerprintUiHelper mFingerprintUiHelper;
    private static final int FIN = 798;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        gToolBar(getString(R.string.settings));

        sp = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        sp.registerOnSharedPreferenceChangeListener(listener);

        appBar = findViewById(R.id.appBarLayout);
        fingerPrintStuff = findViewById(R.id.fingerPrintStuff);
        fingerSwitch = findViewById(R.id.fingerSwitch);
        pinCon = findViewById(R.id.pinCon);
        lCon = findViewById(R.id.lCon);
        fingerInput = findViewById(R.id.fingerInput);
        fingerprint_icon = findViewById(R.id.fingerprint_icon);
        fingerprint_status = findViewById(R.id.fingerprint_status);
        CLPLayout = findViewById(R.id.CLPLayout);
        CTPLayout = findViewById(R.id.CTPLayout);

        idle_Btn = findViewById(R.id.inactivity);
        DisplayCLPBTN = findViewById(R.id.DisplayCLPBTN);
        DisplayCTPBTN = findViewById(R.id.DisplayCTPBTN);
        displayThemes = findViewById(R.id.DisplayThemes);

        ETOldLPin = findViewById(R.id.ETOldLPin);
        ETOldTPin = findViewById(R.id.ETOldTPin);

        ETNewLPin = findViewById(R.id.ETNewLPin);
        ETNewTPin = findViewById(R.id.ETNewTPin);

        ETConfirmLPin = findViewById(R.id.ETConfirmLPin);
        ETConfirmTPin = findViewById(R.id.ETConfirmTPin);

        if(am.getManLog()){
            fingerSwitch.setChecked(true);
        } else {
            fingerSwitch.setChecked(false);
        }

        fingerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    lCon.setText("");
                    if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        fingerSwitch.setChecked(false);
                        ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.USE_FINGERPRINT}, FIN);
                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        am.ToastMessageLong(getApplication(),getString(R.string.oneFinger));
                        fingerSwitch.setChecked(false);
                        startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                    } else if (!keyguardManager.isKeyguardSecure()) {
                        am.ToastMessageLong(getApplication(),getString(R.string.lockScreen));
                        fingerSwitch.setChecked(false);
                        startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                    } else if(confirmed) {
                        pinCon.setVisibility(View.GONE);
                    } else {
                        pinCon.setVisibility(View.VISIBLE);
                    }
                } else {
                    lCon.setText("");
                    confirmed = false;
                    pinCon.setVisibility(View.GONE);
                    fingerInput.setVisibility(View.GONE);
                    am.saveManLog(false);
                }
            }
        });

        ETOldLPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETOldLPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ETOldTPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETOldTPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ETConfirmLPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETConfirmLPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ETConfirmTPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETConfirmTPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void resetViews() {
        if(!am.getFirstTimeUser()){
            CLPLayout.setVisibility(View.GONE);
            CTPLayout.setVisibility(View.GONE);
            DisplayCLPBTN.setVisibility(View.VISIBLE);
            DisplayCTPBTN.setVisibility(View.VISIBLE);
            reveal();
        }
    }

    public void setClicks(View s){
        switch (s.getId()){
            case R.id.go:
                if (lCon.getText().toString().trim().length() < 4) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.confirmGo));
                } else {
                    quest = (
                            "FORMID:LOGIN:" +
                                    "LOGINMPIN:" + lCon.getText().toString().trim() + ":" +
                                    "LOGINTYPE:PIN:"
                    );
                    am.get(this, quest,getString(R.string.loggingIn),"CON");
                }
                break;
            case R.id.inactivity:
                final String [] milliseconds = {"120000","180000","240000","300000"};
                final CharSequence[] items = {getString(R.string.min2),getString(R.string.min3),getString(R.string.min4),getString(R.string.min5)};
                int checked = 0;
                for(int i=0;i<milliseconds.length;i++){
                    if(String.valueOf(am.getIdleTime()).equals(milliseconds[i])){
                        checked = i;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setCancelable(true).setTitle(R.string.autoLogOut)
                        .setSingleChoiceItems(items,checked,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                am.saveIdleTime(milliseconds[item]);
                                dialog.dismiss();
                            }
                        }).setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                gDialog = builder.show();
                break;
        }
    }

    private void reveal() {
        try {
            if(SDK_INT >= M) {
                fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                if(fingerprintManager != null && keyguardManager != null) {
                    if (!fingerprintManager.isHardwareDetected()) {
                        fingerSwitch.setVisibility(View.GONE);
                        am.saveManLog(false);
                    } else {
                        fingerSwitch.setVisibility(View.VISIBLE);
                        if(fingerSwitch.isChecked() && !am.getManLog() && confirmed) {
                            pinCon.setVisibility(View.GONE);
                            mFingerprintUiHelper = new FingerprintUiHelper(fingerprintManager, fingerprint_icon, fingerprint_status,this);
                            fingerInput.setVisibility(View.VISIBLE);
                            mFingerprintUiHelper.startListening(null);
                        } else {
                            lCon.setText("");
                            pinCon.setVisibility(View.GONE);
                            fingerInput.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisplayCLPBTN(View view) {
        if(!am.getFirstTimeUser()){
            CTPLayout.setVisibility(View.GONE);
            DisplayCTPBTN.setVisibility(View.VISIBLE);
        }
        DisplayCLPBTN.setVisibility(View.GONE);
        CLPLayout.setVisibility(View.VISIBLE);
    }

    public void CLPCloseBTN(View view) {
        if(am.getFirstTimeUser()){
            confirm();
        } else {
            resetViews();
        }
    }

    public void CLPChangeBTN(View view) {
        if (Validation(ETOldLPin.getText().toString().trim(),ETNewLPin.getText().toString().trim(),ETConfirmLPin.getText().toString().trim())) {
            am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.notAccPin));
        } else {
            if(am.getFirstTimeUser()) {
                quest = (
                        "FORMID:PINCHANGE:" +
                                "OLDPIN:" + ETOldLPin.getText().toString().trim() /*am.E_P0117(ETOldLPin.getText().toString().trim())*/+ ":" +
                                "NEWPIN:" + ETConfirmLPin.getText().toString().trim() /*am.E_P0117(ETConfirmLPin.getText().toString().trim())*/ + ":" +
                                "PINTYPE:" + "CHANGEPIN" + ":" +
                                "BANKID:" + am.getBankID() + ":"
                );
            } else {
                quest = (
                        "FORMID:PINCHANGE:" +
                                "OLDPIN:" + ETOldLPin.getText().toString().trim() /*am.E_P0117(ETOldLPin.getText().toString().trim()) */+ ":" +
                                "NEWPIN:" + ETConfirmLPin.getText().toString().trim() /*am.E_P0117(ETConfirmLPin.getText().toString().trim())*/ + ":" +
                                "PINTYPE:" + "CHANGELPIN" + ":" +
                                "BANKID:" + am.getBankID() + ":"
                );
            }
            am.saveChangeTrxPin(false);
            am.get(this,quest,getString(R.string.processingReq),"CGE");
        }
    }

    public void DisplayCTPBTN(View view) {
        CLPLayout.setVisibility(View.GONE);
        DisplayCTPBTN.setVisibility(View.GONE);
        CTPLayout.setVisibility(View.VISIBLE);
        DisplayCLPBTN.setVisibility(View.VISIBLE);
    }

    public void CTPCloseBTN(View view) {
        resetViews();
    }

    public void CTPChangeBTN(View view) {
        if (Validation(ETOldTPin.getText().toString().trim(),ETNewTPin.getText().toString().trim(),ETConfirmTPin.getText().toString().trim())) {
            am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.notAccPin));
        } else {
             quest = (
                     "FORMID:PINCHANGE:" +
                             "OLDPIN:" + ETOldTPin.getText().toString().trim()  /*am.E_P0117(ETOldTPin.getText().toString().trim())*/ + ":" +
                             "NEWPIN:" + ETConfirmTPin.getText().toString().trim()  /*am.E_P0117(ETConfirmTPin.getText().toString().trim())*/ + ":" +
                             "PINTYPE:" + "CHANGETPIN" + ":" +
                             "BANKID:" + am.getBankID() + ":"
            );
            am.saveChangeTrxPin(true);
            am.get(this,quest,getString(R.string.processingReq),"CGE");
        }
    }

    public Boolean Validation(String OldPin, String NewPin, String ConfirmPin) {
        int check = 0;
        String[] breakDown = ConfirmPin.split("");

        if (OldPin.equals(NewPin)) {
            check = 0;
        } else if (OldPin.length() < 4) {
            check = 0;
        } else if (NewPin.length() < 4) {
            check = 0;
        } else if (ConfirmPin.length() < 4) {
            check = 0;
        } else if (!NewPin.equals(ConfirmPin)) {
            check = 0;
        }
        // TODO: 8/23/2018
        else if (breakDown[0].equals(breakDown[1])) {
            check = 0;
        } else if (breakDown[1].equals(breakDown[2])) {
            check = 0;
        } else if (breakDown[2].equals(breakDown[3])) {
            check = 0;
        } else {
            check = 1;
        }
        return check == 0;
    }

    public void displayTheme(View view) {
        resetViews();
        startActivity(new Intent(this,ThemePreference.class));
    }

    private class themeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences spref, String key) {
            if (key.equals("theme") && !spref.getString(key, "").equals(App_Init.getThemeSetting())) {
                scheduledRestart = true;
            }
        }
    }

    private void confirm(){
        am.myDialog(Settings.this, getString(R.string.alert), getString(R.string.firstLogin));
    }

    @Override
    protected void onResume() {
        if(am.getFirstTimeUser()) {
            appBar.setVisibility(View.GONE);
            fingerPrintStuff.setVisibility(View.GONE);
            idle_Btn.setVisibility(View.GONE);
            DisplayCLPBTN.setVisibility(View.GONE);
            DisplayCTPBTN.setVisibility(View.GONE);
            displayThemes.setVisibility(View.GONE);
            CTPLayout.setVisibility(View.GONE);
            CLPLayout.setVisibility(View.VISIBLE);
        } else if(scheduledRestart) {
            scheduledRestart = false;
            App_Init.reloadTheme(Settings.this);
            gDialog = new Dialog(Settings.this);
            //noinspection ConstantConditions
            gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            gDialog.setContentView(R.layout.dialog_confirm);
            final TextView txtTitle = gDialog.findViewById(R.id.dialog_title);
            final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
            final TextView noBTN = gDialog.findViewById(R.id.noBTN);
            final TextView yesBTN = gDialog.findViewById(R.id.yesBTN);
            txtTitle.setText(getString(R.string.alert));
            txtMessage.setText(getString(R.string.nextLaunch));
            yesBTN.setText(getString(R.string.relaunch));
            noBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gDialog.cancel();
                }
            });
            yesBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    am.nJe();
                    am.ToastMessageLong(getApplicationContext(),getString(R.string.login));
                    gDialog.cancel();
                }
            });
            gDialog.setCancelable(true);
            gDialog.show();
        } else {
            resetViews();
        }
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FIN) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                am.ToastMessageLong(this, getString(R.string.permWare));
            }
        }
    }

    @Override
    public void onAuthenticated() {
        try {
            am.Binder(lang);
            am.saveManLog(true);
            fingerInput.setVisibility(View.GONE);
            am.ToastMessageLong(this,getString(R.string.finAllow));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mFingerprintUiHelper.stopListening();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onBackPressed() {
        if(am.getFirstTimeUser()) {
            confirm();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step) {
            case "CGE":
                if(!am.getChangeTrxPin()) am.saveManLog(false);
                am.saveChangePin(true);
                finish();
                startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
                break;
            case "CON":
                String [] splits = response.split(":");
                String status = splits[1];
                switch (status) {
                    case "000":
                    case "197":
                    case "OK":
                    case "00":
                        lang = lCon.getText().toString().trim();
                        confirmed = true;
                        reveal();
                        break;
                    default:
                        confirmed = false;
                        am.ToastMessage(getApplicationContext(),getString(R.string.invalidPass));
                        am.nJe();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        am.saveFirstTimeUser(false);
        super.onDestroy();
    }
}
