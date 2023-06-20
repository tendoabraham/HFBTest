package com.elmahousingfinanceug_test.launch;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.launch.rao.AccountOpenSplash;
import com.elmahousingfinanceug_test.main_Pages.ATM_Locations;
import com.elmahousingfinanceug_test.main_Pages.Branches_Page;
import com.elmahousingfinanceug_test.main_Pages.Contact_Us;
import com.elmahousingfinanceug_test.main_Pages.Main_Menu;
import com.elmahousingfinanceug_test.main_Pages.ZAtmBranch;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.FingerprintUiHelper;
import com.elmahousingfinanceug_test.recursiveClasses.ForegroundCheck;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.M;

public class Login extends AppCompatActivity implements ResponseListener, VolleyResponse, FingerprintUiHelper.Callback {
    LinearLayout loginLayout,buttonsLayout,fingerInput;
    ImageView bckGrd,fingerprint_icon;
    TextView fingerprint_status;
    EditText eTPin;
    Dialog dialog;
    AllMethods am;
    String quest="",ujumbe;
    private FusedLocationProviderClient mFusedLocationClient;
    private FingerprintUiHelper mFingerprintUiHelper;
    private FingerprintManager.CryptoObject mCryptoObject;
    private KeyStore keyStore = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        am = new AllMethods(this);
        am.disableScreenShot(this);
        setContentView(R.layout.activity_login);
        if(getIntent().getBooleanExtra("Hide_Login",false)) moveTaskToBack(true);
        bckGrd = findViewById(R.id.hfbBuild);
        loginLayout = findViewById(R.id.loginLayout);
        buttonsLayout = findViewById(R.id.buttonsLayout);
        fingerInput = findViewById(R.id.fingerInput);
        fingerprint_icon = findViewById(R.id.fingerprint_icon);
        fingerprint_status = findViewById(R.id.fingerprint_status);
        eTPin = findViewById(R.id.eTPin);
        buttonsLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);

        eTPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Login.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) eTPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void LoginPage(View l) {
        switch (l.getId()){
            case R.id.contactUs:
                startActivity(new Intent(this,Contact_Us.class));
                break;
            case R.id.toLogin:
                buttonsLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FlipInX).duration(800).playOn(loginLayout);
                break;
            case R.id.branches:
                am.saveViewBraches(true);
                am.saveViewAgent(false);
                startActivity(new Intent(this,Branches_Page.class));
                break;
            case R.id.atmLocations:
                am.saveViewBraches(false);
                am.saveViewAgent(false);
                startActivity(new Intent(this, ATM_Locations.class));
                break;
            case R.id.agent_locations:
                am.saveViewBraches(false);
                am.saveViewAgent(true);
                startActivity(new Intent(this, ZAtmBranch.class));
                break;
            case R.id.account_opening:
                startActivity(new Intent(this, AccountOpenSplash.class));
                break;
            case R.id.loginBn:
//                TODO: Comment out before sharing
//                startActivity(new Intent(this, Main_Menu.class));
                if (eTPin.getText().length() < 4) {
                    eTPin.setError(getString(R.string.enterPIn));
                } else {
                    quest = (
                            "FORMID:LOGIN:" +
                                    "LOGINMPIN:" + (eTPin.getText().toString().trim()) + ":" +
                                    "LOGINTYPE:PIN:"
                    );
                    am.get(this,quest,getString(R.string.loggingIn),"IN");
                    eTPin.setText("");
                }
                break;
            case R.id.backBn:
                eTPin.setText("");
                buttonsLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
                YoYo.with(Techniques.FlipInX).duration(800).playOn(buttonsLayout);
                break;
        }
    }

    private final BroadcastReceiver noConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            if (am.netDialog == null || !am.netDialog.isShowing()) am.promptUser(Login.this);
        }
    };

    private final BroadcastReceiver thereIsConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context cT, Intent iN) {
            if(am.netDialog!=null) am.netDialog.dismiss();
        }
    };

    private void getLoc() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    am.saveLatitude(String.valueOf(latitude));
                    am.saveLongitude(String.valueOf(longitude));
                }
            }
        });
    }

    private void producer() {
        try {
            if(SDK_INT >= M) {
                final FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                if(fingerprintManager !=null) {
                    mFingerprintUiHelper = new FingerprintUiHelper(fingerprintManager, fingerprint_icon, fingerprint_status,this);
                    if(am.getManLog() && am.getCount()<3 ) {
                        if(mFingerprintUiHelper.isFingerprintAuthAvailable()) {
                            fingerInput.setVisibility(View.VISIBLE);
                            Cipher cipher;
                            boolean isValue;
                            String KEY_NAME = am.D_T("NSloOLZ5ZdnrytsmUA1HUQ==");
                            try {
                                keyStore = KeyStore.getInstance(am.D_T("XX/b2sETMg+fqelJH4JnWg=="));
                                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, am.D_T("XX/b2sETMg+fqelJH4JnWg=="));
                                keyStore.load(null);
                                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                                        .setUserAuthenticationRequired(true)
                                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                                        .build());
                                keyGenerator.generateKey();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to get Cipher", e);
                            }
                            try {
                                assert keyStore != null;
                                keyStore.load(null);
                                SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
                                cipher.init(Cipher.ENCRYPT_MODE, key);
                                isValue = true;
                            } catch (KeyPermanentlyInvalidatedException e) {
                                isValue = false;
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to init Cipher", e);
                            }
                            if (isValue) {
                                mCryptoObject = new FingerprintManager.CryptoObject(cipher);
                                mFingerprintUiHelper.startListening(mCryptoObject);
                            }
                        } else {
                            am.saveManLog(false);
                            fingerInput.setVisibility(View.GONE);
                        }
                    } else {
                        fingerInput.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String mRestrict() {
        String RESULTS ="FAILED";
        try {
            byte [] iv = null, value_ = null;
            String iv_ = am.getMatchRule();
            if (iv_ != null) {
                String [] split = iv_.substring(1, iv_.length() - 1).split(", ");
                iv = new byte[split.length];
                for (int i = 0; i < split.length; i++) {
                    iv[i] = Byte.parseByte(split[i]);
                }
            }
            String val = am.getBufferQueue();
            if (val != null) {
                String [] split = val.substring(1, val.length() - 1).split(", ");
                value_ = new byte[split.length];
                for (int i = 0; i < split.length; i++) {
                    value_[i] = Byte.parseByte(split[i]);
                }
            }
            RESULTS = am.dumpsys(value_, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RESULTS;
    }

    private void onWake() {
        try {
            if(SDK_INT >= M) {
                if(am.getManLog() && mFingerprintUiHelper.isFingerprintAuthAvailable() && ForegroundCheck.get(this).isForeground()) {
                    mFingerprintUiHelper.reset();
                    fingerInput.setVisibility(View.VISIBLE);
                    mFingerprintUiHelper.startListening(mCryptoObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        eTPin.setText("");
        registerReceiver(noConnection,new IntentFilter("OFF"));
        registerReceiver(thereIsConnection,new IntentFilter("ON"));
        registerReceiver(am.Network,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getLoc();
        producer();
        super.onResume();
    }

    @Override
    public void onPause(){
        try {
            if(SDK_INT >= M) {
                if(am.getManLog() && mFingerprintUiHelper.isFingerprintAuthAvailable()){
                    mFingerprintUiHelper.stopListening();
                }
            }
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
    protected void onDestroy() {
        try {
            if(am.mDialog!=null) am.mDialog.dismiss();
            mFusedLocationClient.flushLocations();
            am.progressDialog("0");
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void userDetails(String response, final Boolean s) {
        String aliases="",accounts="";
        String[] howLong = response.split(":");
        String[] fieldIds =new String[howLong.length/2];
        String[] fieldValues =new String[howLong.length/2];
        am.separate(response,":",fieldIds,fieldValues);
        ujumbe = am.FindInArray(fieldIds, fieldValues,"MESSAGE");
        if(s){
            am.saveUserName(am.FindInArray(fieldIds, fieldValues,"FIRSTNAME") + " " + am.FindInArray(fieldIds, fieldValues,"LASTNAME"));
            am.saveBankName(am.FindInArray(fieldIds, fieldValues,"BANKNAME"));
            String[] accountsArray = am.FindInArray(fieldIds, fieldValues,"ACCOUNTS").trim().split(",");
            for (String anAccountArray : accountsArray) {
                String[] accountsSplit = anAccountArray.split("-");
                if (accountsSplit.length > 1) {
                    accounts = String.format("%s%s,", accounts, accountsSplit[0]);
                    aliases = String.format("%s%s:", aliases, accountsSplit[1]);
                } else {
                    accounts = accounts + anAccountArray + ",";
                    aliases = aliases + anAccountArray + ":";
                }
            }
            am.saveAccountIDS(accounts);
            am.saveAliases(aliases);
            am.saveUserPhone(am.FindInArray(fieldIds, fieldValues,"MOBILENUMBER"));
            am.saveUserEmail(am.FindInArray(fieldIds, fieldValues,"EMAIL"));
        }
    }

    @Override
    public void onAuthenticated() {
        quest = (
                "FORMID:LOGIN:" +
                        "LOGINMPIN:" + mRestrict() + ":" +
                        "LOGINTYPE:FINGERPRINT:"
        );
        am.get(this, quest,getString(R.string.loggingIn),"IN");
        fingerInput.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
        am.saveCount(String.valueOf(am.getCount()+1));
        if(am.getCount()==3) {
            mFingerprintUiHelper.stopListening();
            fingerInput.setVisibility(View.GONE);
            am.ToastMessageLong(this,getString(R.string.fingerprintMismatch));
        }
    }

    @Override
    public void onResponse(final String response, String step) {
        try {
            String [] splits = response.split(":");
            String status = splits[1], message = splits[3];
            switch (status) {
                /*case "102":
                case "108":
                    onWake();
                    userDetails(response,false);
                    am.myDialog(Login.this,getString(R.string.alert), ujumbe);
                    break;*/
                case "000":
                case "197":
                case "00":
                case "OK":
                case "102":
                case "108":
                    am.saveFirstTimeUser(false);
                    userDetails(response,true);
                    am.saveDoneTrx(false);
                    startActivity(new Intent(Login.this, Main_Menu.class));
                    break;
                case "101":
                    am.saveFirstTimeUser(true);
                    userDetails(response,false);
                    startActivity(new Intent(Login.this, SuccessDialogPage.class).putExtra("message",getText(R.string.welcome)+" " + am.getUserName() +". "+getText(R.string.firstLogin)));
                    break;
                case "201":
                    dialog = new Dialog(Login.this);
                    //noinspection ConstantConditions
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtTitle = dialog.findViewById(R.id.dialog_title),
                            txtMessage = dialog.findViewById(R.id.dialog_message),
                            noBTN = dialog.findViewById(R.id.noBTN),
                            yesBTN = dialog.findViewById(R.id.yesBTN);
                    noBTN.setVisibility(View.GONE);
                    txtTitle.setText(String.format("%s %s", getString(R.string.app_name), getString(R.string.update)));
                    userDetails(response,false);
                    txtMessage.setText(ujumbe);
                    yesBTN.setText(getString(R.string.goToGooglePlay));
                    yesBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            am.goToStore();
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                    break;
                case "202":
                    dialog = new Dialog(Login.this);
                    //noinspection ConstantConditions
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtTitle2 = dialog.findViewById(R.id.dialog_title),
                            txtMessage2 = dialog.findViewById(R.id.dialog_message),
                            cancel = dialog.findViewById(R.id.noBTN),
                            yes = dialog.findViewById(R.id.yesBTN);
                    txtTitle2.setText(String.format("%s %s", getString(R.string.app_name), getString(R.string.update)));
                    userDetails(response,false);
                    txtMessage2.setText(ujumbe);
                    cancel.setText(getString(R.string.later));
                    yes.setText(getString(R.string.goToGooglePlay));
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            userDetails(response,true);
                            am.saveFirstTimeUser(false);
                            am.saveDoneTrx(false);
                            startActivity(new Intent(Login.this, Main_Menu.class));
                            dialog.dismiss();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            am.goToStore();
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                    break;
                default:
                    onWake();
                    am.myDialog(Login.this,getString(R.string.alert), message);
                    break;
            }
        } catch (Exception e) {
            am.LogThis("FormatError ► "+e.getMessage());
            onWake();
            am.myDialog(Login.this,getString(R.string.alert),getString(R.string.tryAgain));
        }
    }
}
