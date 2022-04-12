package com.elmahousingfinanceug_test.launch;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Build.VERSION_CODES.O;
import static android.os.Build.VERSION_CODES.P;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;

import java.io.File;
import java.util.BitSet;

public class Launch_Screen extends AppCompatActivity {
    private AllMethods am;
    private Dialog dialog;
    private static final int M_PHONE_STATE = 778;

    enum ENV_CHECK {
        ROOT(0), DEBUG(1), EMULATOR(2), XPOSED(3), CUSTOM_FIRMWARE (4), INTEGRITY (5), WIRELESS_SECURITY(6);
        private final int idx;
        ENV_CHECK(final int newIdx) {
            idx = newIdx;
        }
        public int getValue() {
            return idx;
        }
    }

    static final BitSet envChecks = new BitSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doProbe(this);
        setContentView(R.layout.activity_launch_screen);
        if(envChecks.get(Launch_Screen.ENV_CHECK.DEBUG.getValue())) {
            showToast(getString(R.string.debugDetected));
            finish();
        } else if(envChecks.get(Launch_Screen.ENV_CHECK.EMULATOR.getValue())) {
            showToast(getString(R.string.emulatorDetected));
            finish();
        } else if(envChecks.get(Launch_Screen.ENV_CHECK.XPOSED.getValue())) {
            showToast(getString(R.string.xposedDetected));
            finish();
        } else if(envChecks.get(Launch_Screen.ENV_CHECK.CUSTOM_FIRMWARE.getValue())){
            showToast(getString(R.string.customFirmwareDetected));
            finish();
        } else if(envChecks.get(Launch_Screen.ENV_CHECK.INTEGRITY.getValue())){
            showToast(getString(R.string.intergrityFailed));
            finish();
        } else if(envChecks.get(Launch_Screen.ENV_CHECK.WIRELESS_SECURITY.getValue())){
            showToast(getString(R.string.wirelessSecurityCheckFailed));
            finish();
        } else if(envChecks.get(Launch_Screen.ENV_CHECK.ROOT.getValue())) {
            showToast(getString(R.string.onARooted));
            finish();
        } else if(Tazama()) {
            showToast(getString(R.string.suDetected));
            finish();
        } else if(isEmulator()) {
            showToast(getString(R.string.emulatorDetected));
            finish();
        } else {
            am = new AllMethods(this);
            am.disableScreenShot(this);

            //TODO: remove
            /*AppSignatureHelper signatureHelper = new AppSignatureHelper(this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                ArrayList<String> appSignatures  = signatureHelper.getAppSignatures();
                String hash = appSignatures.get(0);
                Log.d("hash", hash);
            }*/

            ProgressBar progress = findViewById(R.id.progress);
            progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.icon_orange), PorterDuff.Mode.MULTIPLY);
            if(am.getIfFirstDownload().equals("")){
                am.saveIsFirstDownload();
                if(!am.getCustomerIDLaunch().equals("")) am.saveCustomerID(am.getCustomerIDLaunch());
            }
            if(am.getCountry().equals("")) am.saveCountry("UGANDATEST");
            if(am.getIdleTime()==0) am.saveIdleTime("180000");
            if(am.getAppName().equals("")) am.saveAppName();
            if(am.getBankID().equals("")) am.saveBankID();
            // TEST 2021              459483989
//            am.saveCustomerID("4594839890");
//            am.saveUserPhone("256700146817");
            
            if(am.getIMEI().equals("")) {
                readDevice();
            } else {
                openUp();
            }
        }
    }

    public void doProbe(Context ctx) {
    }

    public static void positiveRootCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.ROOT.getValue());
    }
    public static void negativeRootCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.ROOT.getValue());
    }

    public static void positiveDebugCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.DEBUG.getValue());
    }
    public static void negativeDebugCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.DEBUG.getValue());
    }

    public static void positiveEmulatorCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.EMULATOR.getValue());
    }
    public static void negativeEmulatorCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.EMULATOR.getValue());
    }

    public static void positiveXposedCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.XPOSED.getValue());
    }
    public static void negativeXposedCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.XPOSED.getValue());
    }

    public static void positiveCustomFirmwareCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.CUSTOM_FIRMWARE.getValue());
    }
    public static void negativeCustomFirmwareCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.CUSTOM_FIRMWARE.getValue());
    }

    public static void positiveIntegrityCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.INTEGRITY.getValue());
    }
    public static void negativeIntegrityCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.INTEGRITY.getValue());
    }

    public static void positiveWirelessSecurityCheck(Object data) {
        envChecks.set(Launch_Screen.ENV_CHECK.WIRELESS_SECURITY.getValue());
    }
    public static void negativeWirelessSecurityCheck(Object data) {
        envChecks.clear(Launch_Screen.ENV_CHECK.WIRELESS_SECURITY.getValue());
    }

    private static boolean Tazama() {
        String[] paths = {"/system/su","/system/bin/.ext/.su","/system/usr/we-need-root/su-backup","/system/xbin/mu*/",
                "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su","/data/local/xbin/su",
                "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    private void showToast(String textShow){
        Toast.makeText(this,textShow,Toast.LENGTH_LONG).show();
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private void readDevice() {
        if (SDK_INT > P) {
            am.saveIMEI(am.putDeviceQ());
            openUp();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                dialog = new Dialog(this);
                //noinspection ConstantConditions
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_confirm);
                final TextView txtTitle = dialog.findViewById(R.id.dialog_title),
                        txtMessage = dialog.findViewById(R.id.dialog_message),
                        noBTN = dialog.findViewById(R.id.noBTN),
                        yesBTN = dialog.findViewById(R.id.yesBTN);
                noBTN.setVisibility(View.GONE);
                txtTitle.setText(getString(R.string.deviceId));
                txtMessage.setText(getString(R.string.permissionDevice));
                yesBTN.setText(getString(android.R.string.ok));
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(Launch_Screen.this, new String[]{Manifest.permission.READ_PHONE_STATE}, M_PHONE_STATE);
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            } else {
                try {
                    TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                    assert tm != null;
                    if (SDK_INT >= O) {
                        am.saveIMEI(tm.getImei(0));
                    } else if (SDK_INT >= M) {
                        am.saveIMEI(tm.getDeviceId(0));
                    } else {
                        am.saveIMEI(tm.getDeviceId());
                    }
                    openUp();
                } catch (Exception e) {
                    am.LogThis("DeviceId ► " + e.getMessage());
                    am.ToastMessageLong(this, getString(R.string.errorDevice));
                }
            }
        }
    }

    private void openUp(){
        int SPLASH_DISPLAY_LENGTH = 1900;
        new android.os.Handler().postDelayed(() -> {
            if(!am.getCustomerID().equals("")) {
                finish();
                startActivity(new Intent(Launch_Screen.this, Login.class));
            } else {
                finish();
                startActivity(new Intent(Launch_Screen.this, RecreateKey_Send.class));
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String [] permissions, @NonNull int [] grantResults) {
        if (requestCode == M_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readDevice();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", getPackageName(), null)));
                    am.ToastMessageLong(this, getString(R.string.enablePhone));
                    finish();
                } else {
                    readDevice();
                }
            }
        }
    }
}
