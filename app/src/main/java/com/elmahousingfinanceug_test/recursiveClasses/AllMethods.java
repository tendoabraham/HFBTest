package com.elmahousingfinanceug_test.recursiveClasses;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.launch.Login;
import com.elmahousingfinanceug_test.launch.rao.AccountOpenSplash;
import com.elmahousingfinanceug_test.launch.rao.AccountOpenZExistingCustomersMain;
import com.elmahousingfinanceug_test.launch.rao.AccountOpenZMain;
import com.elmahousingfinanceug_test.main_Pages.ATM_Locations;
import com.elmahousingfinanceug_test.main_Pages.About_Us;
import com.elmahousingfinanceug_test.main_Pages.Branches_Page;
import com.elmahousingfinanceug_test.main_Pages.Contact_Us;
import com.elmahousingfinanceug_test.main_Pages.Profile;
import com.elmahousingfinanceug_test.main_Pages.ZAtmBranch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.net.ssl.SSLSocketFactory;

public class AllMethods {
    private Context context;
    private static final String AreaCode = "256";
    private final String PREFS = D_T("CCqRYCZb0b6nD8RHb1ldkbG1tZz0abVYvrUQ/q5k6Z8="),
            TRANSFORMATION =D_T("GY0K1coapVtS45DU72FWzl3q0xcxyi8l6qGlw/ar4Kg="),
            ANDROID_KEY_STORE = D_T("XX/b2sETMg+fqelJH4JnWg==");
    private final Dialog dialogLoad;
    private final TextView cancelTrx;
    private final RequestQueue getQ;
    private final RequestQueue connectQ;
    private final RequestQueue vanishQ;
    private TextView progressMessage;
    private StringRequest stringRequest = null;
    private JsonObjectRequest jsonObjectRequest = null;
    private Handler cancelHandler =  new Handler();
    private Runnable cancelRunnable = new Runnable() {
        @Override
        public void run() {
            cancelTrx.setVisibility(View.VISIBLE);
            animate_Text(cancelTrx);
        }
    };

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public Dialog mDialog = null, netDialog = null;
    private KeyStore keyStore;

    public final Handler idleHandler = new Handler();
    public final Runnable idleRunnable = new Runnable() {
        @Override
        public void run() {
            nJe();
        }
    };

    ForegroundCheck.Listener fore_Back_Listener = new ForegroundCheck.Listener(){
        public void onBecameForeground(){
            idleHandler.removeCallbacks(idleRunnable);
        }
        public void onBecameBackground(){
            delayedIdle();
        }
    };

    public AllMethods(final Context context) {
        this.context = context;
        dialogLoad = new Dialog(context);
        //noinspection ConstantConditions
        dialogLoad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogLoad.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLoad.setContentView(R.layout.dialog_loading);
        dialogLoad.setCancelable(false);
        progressMessage = dialogLoad.findViewById(R.id.messageText);
        cancelTrx = dialogLoad.findViewById(R.id.cancelReq);
        cancelTrx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastMessageLong(context,context.getString(R.string.cancelRequest));
                dialogLoad.cancel();
            }
        }); cancelTrx.setVisibility(View.GONE);

        dialogLoad.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(stringRequest!=null) stringRequest.cancel();
                stringRequest = null;
                if(jsonObjectRequest!=null) jsonObjectRequest.cancel();
                jsonObjectRequest = null;
                AndroidNetworking.cancelAll();
                cancelHandler.removeCallbacks(cancelRunnable);
                cancelTrx.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        getQ = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
        connectQ = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
        vanishQ = Volley.newRequestQueue(context, new HurlStack(null, pinnedSSLSocketFactory()));
    }

    public void saveTheme(String themeName) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("theme", themeName);
        editor.apply();
    }

    public int getIdleTime() {
        int time = 0;
        try {
            time = Integer.parseInt(gSP("TIME"));
        } catch(NumberFormatException nfe) {
            LogThis("NumberFormatException ☼ " + nfe.getMessage());
        }
        return time;
    }
    public void saveIdleTime(String idleTime) {
        SP("TIME",idleTime);
    }

    public String getCountry() {
        return gSP("COUNTRY");
    }

    public void saveCountry(String Country) {
        SP("COUNTRY",Country);
    }

    public String getAppName() {
        return gSP("APPNAME");
    }

    public void saveAppName() {
        SP("APPNAME","HFBUG");
    }

    public String getBankID() {
        return gSP("BANKID");
    }

    public void saveBankID() {
        SP("BANKID","23");
    }

    public long getA() {
        long h = 0;
        try {
            h = Long.parseLong(gSP("AT"));
        } catch(Exception o) {
            LogThis("Long ☼ " + o.getMessage());
        }
        return h;
    }
    public void saveA(String t) {
        SP("AT", t);
    }

    public String getATMB() {
        return gSP("BTM");
    }
    public void saveATMB(String t) {
        SP("BTM",t);
    }

    public String getIfFirstDownload() {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        return D_T(mySharedPreferences.getString("ISFIRST", ""));
    }

    public void saveIsFirstDownload() {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("ISFIRST", E_P("NO"));
        editor.apply();
    }

    public String getCustomerIDLaunch() {
        String temp = "";
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        temp = mySharedPreferences.getString("CUSTOMERID", "");
        assert temp != null;
        if(!temp.equals("")) temp = decryptOldApp(temp);
        if(temp.equalsIgnoreCase("Cipher error")) temp = "";
        return temp;
    }

    public String getCustomerID() {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        return D_T(mySharedPreferences.getString("CUSTOMERID", ""));
    }

    public void saveCustomerID(String customerID) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("CUSTOMERID", E_P(customerID));
        editor.apply();
    }

    public String getIMEI() {
        return gSP("IMEI");
    }

    public void saveIMEI(String i) {
        SP("IMEI",i);
    }

    public String getLatitude() {
        return gSP("LAT");
    }

    public void saveLatitude(String lat) {
        SP("LAT",lat);
    }

    public String getLongitude() {
        return gSP("LON");
    }

    public void saveLongitude(String lon) {
        SP("LON",lon);
    }

    public Boolean getFirstTimeUser() {
        return gBool("FIRSTTIMEUSER");
    }

    public void saveFirstTimeUser(Boolean b) {
        SP_BOOL("FIRSTTIMEUSER", b);
    }

    public String getUserName() {
        return gSP("USERNAME");
    }

    public void saveUserName(String uN) {
        SP("USERNAME", uN);
    }

    public String getBankName() {
        return gSP("BANKNAME");
    }

    public void saveBankName(String bN) {
        SP("BANKNAME",bN);
    }

    public String getBankAccountID(int on) {
        String decAcc =  gSP("BANKACCOUNTID");
        String[] accountID = decAcc.split(",");
        return accountID[on - 1];
    }

    public String getDashBankAccountID(int on) {
        String decAcc =  gSP("BANKACCOUNTID");
        String[] accountID = decAcc.split(",");
        return accountID[on];
    }

    public void saveAccountIDS(String accIDs) {
        if (accIDs != null && accIDs.length() > 0 && accIDs.charAt(accIDs.length() - 1) == ',') {
            accIDs = accIDs.substring(0, accIDs.length() - 1);
        }
        SP("BANKACCOUNTID", accIDs);
    }

    public List<String> getAliases() {
        String decAcc =  gSP("ALIASIDS");
        String[] AccountIDS = decAcc.split(":");
        final List<String> allList = new ArrayList<>();
        allList.add(context.getString(R.string.selectOne));
        Collections.addAll(allList, AccountIDS);
        return allList;
    }

    public List<String> getDashAliases() {
        String decAcc =  gSP("ALIASIDS");
        String[] AccountIDS = decAcc.split(":");
        final List<String> allList = new ArrayList<>();
        Collections.addAll(allList, AccountIDS);
        return allList;
    }

    public void saveAliases(String all) {
        if (all != null && all.length() > 0 && all.charAt(all.length() - 1) == ':') {
            all = all.substring(0, all.length() - 1);
        }
        SP("ALIASIDS", all);
    }

    public String getUserPhone() {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        String y = D_T(mySharedPreferences.getString(E_P("PHONE"),""));
        if(y.equals("")){
            if(getCountry().equalsIgnoreCase("UGANDA")){
                y = "25600023";
            } else {
                y = "25600123";
            }
        }
        return y;
    }

    public void saveUserPhone(String tel) {
        tel = tel.replace("+","");
        if(tel.startsWith("0")) {
            tel = tel.substring(1);
            tel = AreaCode.concat(tel);
            SP("PHONE",tel);
        } else {
            SP("PHONE",tel);
        }
    }

    public String getUserEmail() {
        return gSP("USEREMAIL");
    }

    public void saveUserEmail(String uE) {
        SP("USEREMAIL", uE);
    }

    public String getUserPic() {
        return gSP("USERPIC");
    }

    public void saveUserPic(String uP) {
        SP("USERPIC", uP);
    }

    Boolean getChangePin() {
        return gBool("PINCHANGED");
    }

    public void saveChangePin(Boolean b) {
        SP_BOOL("PINCHANGED", b);
    }

    public Boolean getChangeTrxPin() {
        return gBool("TRXPINCHANGED");
    }

    public void saveChangeTrxPin(Boolean b) {
        SP_BOOL("TRXPINCHANGED", b);
    }

    public Boolean getViewBraches() {
        return gBool("VIEWBRANCH");
    }

    public void saveViewBraches(Boolean b) {
        SP_BOOL("VIEWBRANCH", b);
    }

    public Boolean getViewAgent() {
        return gBool("VIEWAGENT");
    }

    public void saveViewAgent(Boolean b) {
        SP_BOOL("VIEWAGENT", b);
    }

    public String getMerchantID() {
        return gSP("MERCHANTID");
    }

    public void saveMerchantID(String ant) {
        SP("MERCHANTID", ant);
    }

    public String getSendPhone() {
        return gSP("SPHONE");
    }

    public void saveSendPhone(String tel) {
        tel = tel.replace("+","");
        if(tel.startsWith("0")) {
            tel = tel.substring(1);
            tel = AreaCode.concat(tel);
            SP("SPHONE",tel);
        } else {
            SP("SPHONE",tel);
        }
    }

    public String getAreas() {
        return gSP("AREAS");
    }

    public void saveAreas(String aR) {
        if (aR != null && aR.length() > 0 && aR.charAt(aR.length() - 1) == ',') {
            aR = aR.substring(0, aR.length() - 1);
        }
        SP("AREAS",aR);
    }

    public String getMatchRule() {
        return gSP("RULE");
    }
    private void saveMatchRule(String flag) {
        SP("RULE", flag);
    }

    public String getBufferQueue() {
        return gSP("THREAD");
    }
    private void saveBufferQueue(String system) {
        SP("THREAD", system);
    }

    public String getSavedBundle() {
        return gSP("savedoptions");
    }
    public void putSavedBundle(String bundle) {
        SP("savedoptions", bundle);
    }

    public String getSavedBranch() {
        return gSP("savedbranch");
    }
    public void putSavedBranch(String bundle) {
        SP("savedbranch", bundle);
    }

    public String getSavedData(String label) {
        return gSP(label);
    }
    public void putSavedData(String label,String data) {
        SP(label, data);
    }

    public Integer getSavedPreviousStep() {
        return getSavedInt("PreviousStep");
    }
    public void putSavedPreviousStep(int data) {
        saveInt("PreviousStep", data);
    }

    public Integer getCustomerCategory() {
        return getSavedInt("CustomerCategory");
    }
    public void putSaveCustomerCategory(int CustomerCategory) {
        saveInt("CustomerCategory", CustomerCategory);
    }

    public Integer getAccountTypePosition() {
        return getSavedInt("AccountTypePosition");
    }
    public void putSaveSelectedAccountTypePosition(int AccountTypePosition) {
        saveInt("AccountTypePosition", AccountTypePosition);
    }

    public Integer getBranchPosition() {
        return getSavedInt("BranchPosition");
    }
    public void putSaveSelectedBranchPosition(int mPosition) {
        saveInt("BranchPosition", mPosition);
    }

    public Integer getCurrencyPosition() {
        return getSavedInt("CurrencyPosition");
    }
    public void putSaveSelectedCurrencyPosition(int mPosition) {
        saveInt("CurrencyPosition", mPosition);
    }

    public Integer getTitlePosition() {
        return getSavedInt("TitlePosition");
    }
    public void putSaveTitlePosition(int mPosition) {
        saveInt("TitlePosition", mPosition);
    }

    public Integer getParentsDurationAddress() {
        return getSavedInt("ParentsDurationAddress");
    }
    public void putSaveParentsDurationAddress(int mPosition) {
        saveInt("ParentsDurationAddress", mPosition);
    }

    public Integer getPeriodEmployment() {
        return getSavedInt("PeriodEmployment");
    }
    public void putSavePeriodEmployment(int mPosition) {
        saveInt("PeriodEmployment", mPosition);
    }

    public Integer getNextofKinCountryPosition() {
        return getSavedInt("NextofKinCountry");
    }
    public void putSaveNextofKinCountry(int mPosition) {
        saveInt("NextofKinCountry", mPosition);
    }

    public Integer getAltNextofKinCountryPosition() {
        return getSavedInt("AltNextofKinCountry");
    }
    public void putSaveAltNextofKinCountry(int mPosition) {
        saveInt("AltNextofKinCountry", mPosition);
    }

    public Integer getCustomerCountryPosition() {
        return getSavedInt("CustomerCountryPosition");
    }
    public void putSaveCustomerCountryPosition(int mPosition) {
        saveInt("CustomerCountryPosition", mPosition);
    }

    public Integer getAlterCustomerCountryPosition() {
        return getSavedInt("AltCustomerCountryPosition");
    }
    public void putSaveAlterCustomerCountryPosition(int mPosition) {
        saveInt("AltCustomerCountryPosition", mPosition);
    }

    public Integer getBankStaffCountryPosition() {
        return getSavedInt("BankStaffCountryPosition");
    }
    public void putBankStaffCountryPosition(int mPosition) {
        saveInt("BankStaffCountryPosition", mPosition);
    }

    public Integer getRegionPosition() {
        return getSavedInt("RegionPosition");
    }
    public void putRegionPosition(int mPosition) {
        saveInt("RegionPosition", mPosition);
    }

    public Integer getMediumRadio() {
        return getSavedInt("mediumRadio");
    }
    public void putMediumRadio(int medium) {
        saveInt("mediumRadio", medium);
    }

    public int getCount() {
        int c = 0;
        try {
            c = Integer.parseInt(gSP("COUNT"));
        } catch(NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return c;
    }
    public void saveCount(String t) {
        SP("COUNT", t);
    }

    public Boolean getManLog () {
        return gBool("FL");
    }
    public void saveManLog (Boolean bool) {
        SP_BOOL("FL",bool);
    }

    public Boolean getDoneTrx () {
        return gBool("DTRX");
    }
    public void saveDoneTrx (Boolean bool) {
        SP_BOOL("DTRX",bool);
    }

    public boolean getProceed() {
        return gBool("proceed");
    }
    public void putProceed(boolean proceed) {
        SP_BOOL("proceed",proceed);
    }

    private Boolean gBool(String z){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        z = E_P(z);
        return Boolean.valueOf(D_T(mySharedPreferences.getString(z, "false")));
    }

    private void SP_BOOL(String k,Boolean o){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(E_P(k), E_P(String.valueOf(o)));
        editor.apply();
    }

    private String gSP(String z){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        z = E_P(z);
        return D_T(mySharedPreferences.getString(z,""));
    }

    private void SP(String k,String v){
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(E_P(k), E_P(v));
        editor.apply();
    }

    private Integer getSavedInt(String z) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        z = E_P(z);
        return mySharedPreferences.getInt(z,0);
    }

    private void saveInt(String k,int v) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(E_P(k), v);
        editor.apply();
    }


    //============= Y ====================================================================================================================
    private String decryptOldApp(String DataToEncrypt) {
        byte[] EncryptedData;
        Encryption EncryptionEngine = new Encryption();
        try {
            EncryptedData = Base64.Base64Decode(DataToEncrypt.toCharArray());
            EncryptedData = EncryptionEngine.DESPasswdDecrypt(EncryptedData, "BG17D966".toCharArray());
            DataToEncrypt = (new String(EncryptedData));
            DataToEncrypt = DataToEncrypt.replace('\0', ' ');
            DataToEncrypt = DataToEncrypt.trim();
        } catch (Exception ex) {
            try {
                EncryptedData = Base64.Base64Decode(DataToEncrypt.toCharArray());
                DataToEncrypt = (new String(EncryptedData));
                DataToEncrypt = DataToEncrypt.replace('\0', ' ');
                DataToEncrypt = DataToEncrypt.trim();
            } catch (Exception exx) {
                exx.printStackTrace();
            }
            return DataToEncrypt;
        }
        return DataToEncrypt;
    }

    public String ENCODE(String t) {
        String at = "";
        try {
            CryptLib c = new CryptLib();
            String y = CryptLib.SHA256("KbPmng&1977dsfds%");
            at = c.encrypt(t, y);
            at = at.replaceAll("\\r\\n|\\r|\\n", "");

            try {
                at = URLEncoder.encode(at, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return at;
    }

    private String EncryptNoEncoding(String t, String y) {
        String at = "";
        try {
            CryptLib c = new CryptLib();
            String e = CryptLib.SHA256(y);
            at = c.encrypt(t, e);
            at = at.replaceAll("\\r\\n|\\r|\\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return at;
    }

    private String BIND (String x, String vice) {
        String a = "";
        try {
            CryptLib2 crypt = new CryptLib2();
            a = crypt.flip(x,CryptLib.SHA256(vice));
            a = a.replaceAll("\\r\\n|\\r|\\n", "");
            try {
                a = URLEncoder.encode(a, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    public String REVERSE(String t, String y) {
        String at = "";
        try {
            CryptLib c = new CryptLib();
            String e = CryptLib.SHA256(y);
            at = c.decrypt(t, e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return at;
    }

    private String SPAN(String x, String vice) {
        String a = "";
        try {
            CryptLib crypt = new CryptLib();
            a = crypt.decrypt(x, CryptLib.SHA256(vice));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    String E_P(String t) {
        String at = "";
        try {
            CryptLib c = new CryptLib();
            String e = CryptLib.SHA256("KbPmng&1977dsfds%");
            at = c.encrypt(t, e);
            at = at.replaceAll("\\r\\n|\\r|\\n", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return at;
    }

    public String D_T(String t) {
        String at = "";
        try {
            CryptLib c = new CryptLib();
            String e = CryptLib.SHA256("KbPmng&1977dsfds%");
            at = c.decrypt(t, e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return at;
    }

    //=================== O ===============================================================================================================
    public void Drawer_Item_Clicked(Context context, int id) {
        if (id == R.id.myProfile) {
            context.startActivity(new Intent(context, Profile.class));
        } else if (id == R.id.prepaidCard) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://portals.housingfinance.co.ug/consumerHFB/faces/consumerHFB.xhtml")));
        } else if (id == R.id.settings) {
            context.startActivity(new Intent(context, com.elmahousingfinanceug_test.main_Pages.Settings.class));
        } else if (id == R.id.aboutUs) {
            context.startActivity(new Intent(context, About_Us.class));
        } else if (id == R.id.contacUs) {
            context.startActivity(new Intent(context, Contact_Us.class));
        } else if (id == R.id.agents) {
            saveViewBraches(false);
            saveViewAgent(true);
            context.startActivity(new Intent(context, ZAtmBranch.class));
        } else if (id == R.id.branches) {
            saveViewBraches(true);
            saveViewAgent(false);
            context.startActivity(new Intent(context, Branches_Page.class));
        } else if (id == R.id.atmLocations) {
            saveViewBraches(false);
            saveViewAgent(false);
            context.startActivity(new Intent(context, ATM_Locations.class));
        } else if (id == R.id.logOut) {
            nJe();
        }
    }

    public void nJe () {
        SharedPreferences mySPrefs = context.getSharedPreferences(PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove("ALIASIDS");
        editor.remove("BANKACCOUNTID");
        editor.remove("BANKID");
        editor.remove("BANKNAME");
        editor.remove("COUNTRY");
        editor.remove("FIRSTTIMEUSER");
        editor.remove("MERCHANTID");
        editor.remove("PHONE");
        editor.remove("RANDOMKEY");
        editor.remove("USERNAME");
        editor.remove("USEREMAIL");
        editor.apply();

        Activity activity = (Activity)context;
        if(!activity.getClass().equals(Login.class)){
            Intent i = new Intent(activity, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(ForegroundCheck.get(activity).isBackground()) {
                i.putExtra("Hide_Login", true);
            } else {
                i.putExtra("Hide_Login", false);
            }
            activity.startActivity(i);
        }
        // TODO: on Q1
        //vanish();
    }

    public void LogThis(String data) {
        //TODO: comment
        Log.d("HFB...", data);
    }

    public void Set_Items_2_Animate(List<LinearLayout> arrayList) {
        int Length = arrayList.size();
        int delayTime = 100;
        for (int i = 0; i < Length; i++) {
            animate_Layout(arrayList.get(i), delayTime);
            delayTime = delayTime + 100;
        }
    }

    private void animate_Layout(LinearLayout ll, int time) {
        YoYo.with(Techniques.FlipInX)
                .duration(1100).delay(time)
                .playOn(ll);
    }

    public void animate_Text(TextView tv) {
        YoYo.with(Techniques.FadeInDown)
                .duration(500)
                .playOn(tv);
    }

    public void animate_View(View v) {
        YoYo.with(Techniques.Bounce)
                .duration(1000)
                .playOn(v);
    }

    public void animate_ViewZz(View v) {
        YoYo.with(Techniques.Tada)
                .duration(1000).delay(2)
                .playOn(v);
    }

    public void myDialog(Context DialogContext, String Title, String Message) {
        mDialog = new Dialog(DialogContext);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_info);
        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title);
        final TextView txtMessage = mDialog.findViewById(R.id.dialog_message);
        final TextView txtOk = mDialog.findViewById(R.id.dialog_BTN);
        txtTitle.setText(Title);
        txtMessage.setText(Message);
        txtOk.setOnClickListener(v -> mDialog.cancel());
        mDialog.setCancelable(true);
        mDialog.show();
        animate_Text(txtTitle);
        animate_Text(txtMessage);
        animate_Text(txtOk);
    }

    public void ToastMessage(Context x, String m) {
        Toast toast = Toast.makeText(x, m, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void ToastMessageLong(Context x, String m) {
        Toast toast = Toast.makeText(x, m, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    public void progressDialog(String status) {
        try {
            if (status.equals("1")) {
                idleHandler.removeCallbacks(idleRunnable);
                cancelHandler.postDelayed(cancelRunnable,45000);
                dialogLoad.show();
            } else {
                if(dialogLoad.isShowing()) dialogLoad.cancel();
            }
        } catch (Exception x){
            x.printStackTrace();
        }
    }

    public void setProgressMessage(String message) {
        progressMessage.setText(message);
        animate_Text(progressMessage);
    }

    public void separate(String chunks, String regex, String[] fieldIds, String[] fieldValues) {
        try {
            String output;
            int i, j = 0;
            while (chunks.length() > 0) {
                i = chunks.indexOf(regex);
                output = chunks.substring(0, i);
                fieldIds[j] = output;
                chunks = chunks.substring(i + 1);
                i = chunks.indexOf(regex);
                if (i < 0) {
                    output = chunks;
                    chunks = "";
                } else {
                    output = chunks.substring(0, i);
                    chunks = chunks.substring(i + 1);
                }
                fieldValues[j] = output;
                j++;
            }
        } catch (Exception e) {
            LogThis("SplitException ► " + e.getMessage());
        }
    }

    public String FindInArray (String[] ArrayID, String[] ArrayValue, String StringToFind) {
        int i;
        if (StringToFind.equals("")) return "";

        for (i = 0; i < ArrayID.length; i++) {
            try {
                if (ArrayID[i].equalsIgnoreCase(StringToFind)) return ArrayValue[i];
            } catch (Exception e) {
                LogThis("FindInArrayException "+e.getMessage());
            }
        }
        return "";
    }

    public String Amount_Thousands(String amount) {
        try {
            if (!amount.contains(",")) {
                NumberFormat formatter = new DecimalFormat("#,##0.00");
                amount = formatter.format(Double.parseDouble(amount));
                return amount;
            } else {
                return amount;
            }
        } catch (Exception e){
            e.printStackTrace();
            return amount;
        }
    }

    public final BroadcastReceiver Network = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contextNet, Intent intentNet) {
            boolean noConnectivity = intentNet.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intentNet.getStringExtra(ConnectivityManager.EXTRA_REASON);
            if(noConnectivity){
                Toast toast = Toast.makeText(contextNet, reason, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                if(reason!=null) toast.show();
                intentNet = new Intent("OFF");
                contextNet.sendBroadcast(intentNet);
            } else {
                intentNet = new Intent("ON");
                contextNet.sendBroadcast(intentNet);
            }
        }
    };

    public void disableScreenShot(Activity activity){
        if(getCountry().equals("UGANDA")) activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
    }

    void delayedIdle() {
        idleHandler.removeCallbacks(idleRunnable);
        idleHandler.postDelayed(idleRunnable,(getIdleTime()));
    }

    public void promptUser(final Activity alertActivity){
        AlertDialog.Builder builder = new AlertDialog.Builder(alertActivity);
        builder.setCancelable(true).setTitle(R.string.internetError).setMessage(R.string.internetEnable)
                .setPositiveButton(alertActivity.getString(R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alertActivity.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }})
                .setNeutralButton(alertActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        netDialog = builder.show();
    }

    public String Q(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String version() {
        NumberFormat value = new DecimalFormat("#,##0.0");
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pInfo != null;
        return value.format(pInfo.versionCode);
    }

    @SuppressLint("HardwareIds")
    public String putDeviceQ() {
        String RESULTS = Q();
        try {
            RESULTS = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            LogThis("SecureException ► " + e.getMessage());
        }
        return RESULTS;
    }

    public String HashLatest(String word) {
        String input = word + D_T("73RWvh50akerG5emdzSFkQ==");
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte [] encodedhash = new byte[0];
        try {
            assert digest != null;
            encodedhash = digest.digest(input.getBytes(context.getString(R.string.utf)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder hexString = new StringBuilder();
        for (byte anEncodedhash : encodedhash) {
            String hex = Integer.toHexString(0xff & anEncodedhash);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String E_P0117(String x) {
        // TODO: 1/30/2020 remove live
        //LOGINMPIN,TMPIN,OLDPIN,NEWPIN,KEY,DEVICEID,TRX1432019
        //PARAM0117
        String a = "";
        try {
            CryptLib crypt = new CryptLib();
            a = crypt.encrypt(x,CryptLib.SHA256("KBSB&er3bflx9%"));
            a = a.replaceAll("\\r\\n|\\r|\\n", "");
        } catch (Exception e) {
            LogThis("Cry ☼ "+e.getMessage());
        }
        return a;
    }

    public void goToStore() {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void Binder(final String textToEncrypt)
            throws NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeyZ());

        byte [] iv = cipher.getIV(),
                encryption = cipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));

        saveMatchRule(Arrays.toString(iv));
        saveBufferQueue(Arrays.toString(encryption));
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.M)
    private SecretKey getSecretKeyZ() throws
            NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(D_T("j/BvBBfNcgVfrGaWRBliEw=="),
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String dumpsys(final byte [] encryptedData, final byte [] encryptionIv)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchPaddingException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec;
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        try {
            keyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spec = new GCMParameterSpec(128, encryptionIv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeyX(), spec);
        return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
    }

    private SecretKey getSecretKeyX() throws
            NoSuchAlgorithmException,
            UnrecoverableEntryException,
            KeyStoreException {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(D_T("j/BvBBfNcgVfrGaWRBliEw=="), null)).getSecretKey();
    }

    private String getToken() {
        if (getCountry().equalsIgnoreCase("UGANDA"))
            return D_T("SK8jLvHib4OLFAuYb4Yfbp3s9KN48ShYNVmek1n1zlipEO3ByCb38QN+nsi7SPlr");
        else
            return D_T("ITcYFtXDh2esU+aOXoJr9ugd1yLhebnlFQJKUA6ulV0YcG1DUP99OfWWPTNCk9VoeDHVv5rd5C0QY0EGJ3SE3g==");
    }

    private String vanishing() {
        if (getCountry().equalsIgnoreCase("UGANDA"))
            return D_T("SK8jLvHib4OLFAuYb4Yfbp3s9KN48ShYNVmek1n1zliCJkhTEfUU43udJ93WOMeu");
        else
            return D_T("ITcYFtXDh2esU+aOXoJr9ugd1yLhebnlFQJKUA6ulV0YcG1DUP99OfWWPTNCk9VoDTUEMtqoDVh/ysPJVPD1zw==");
    }


    private SSLSocketFactory pinnedSSLSocketFactory() {
        try {
            //TODO: pins on Live
            return new TLSSocketFactory("30820122300d06092a864886f70d01010105000382010f003082010a0282010100a8ee6e5c4d60d03be2bb7e4b628c04c143d418fcb7d691cee5d3de0545e648e60f978c2a712a63413d14b3906e64318923584ec45c5d8737f44937d0b9e2bd978123a8326e0d340fa09709c71cf23ced907777d62e1e0c8757a8a93ee711b2b5d99bf1cbaeb7fb13f7a5bd15aa3560008f3023c080d861f384100216d00dbbb376cfa86c0ae74cf6de2d98563836d1e535b4814106b96e791c36ee7b4943e4db7570d449bfe96e9ae17f6bc72c51a84462660cb0b83be02d610e62df6b961041c0df2efa33a5c72a8fbb1fb238b4e9d33ea1d5f99cf6100434ec366a658d8e781a50fe9fea5994e0facd837d26b1bccd93d7f74ae4ee504b33e502c13866cbb90203010001");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String finale(String step) {
        String z;
        switch (step){
            case "RS":
            case "RV":
                z = (
                        "APPNAME:" + getAppName() + ":" +
                                //"DEVICEID:" + E_P0117(getIMEI()) + ":" +
                                "DEVICEID:" + getIMEI() + ":" +
                                "UNIQUEID:" + Q() + ":" +
                                "VERSION:" + version() + ":" +
                                "LAT:" + getLatitude() + ":" +
                                "LON:" + getLongitude() + ":" +
                                //"PARAM0117:YES:" +
                                "CODEBASE:ANDROID:" +
                                "TRXSOURCE:APP"
                );
                break;
            default:
                if(getCustomerID().equals("")) {
                    if (getCountry().equalsIgnoreCase("UGANDA")) {
                        z = "25600023";
                    } else {
                        z = "25600123";
                    }
                } else {
                    z = getCustomerID();
                }
                z = (
                        "CUSTOMERID:" + z + ":" +
                                "APPNAME:" + getAppName() + ":" +
                                //"DEVICEID:" + E_P0117(getIMEI()) + ":" +
                                "DEVICEID:" + getIMEI() + ":" +
                                "UNIQUEID:" + Q() + ":" +
                                "VERSION:" + version() + ":" +
                                "LAT:" + getLatitude() + ":" +
                                "LON:" + getLongitude() + ":" +
                                //"PARAM0117:YES:" +
                                "CODEBASE:ANDROID:" +
                                "TRXSOURCE:APP"
                );
                break;

        }
        return z;
    }

    public void get(final ResponseListener responseListener, String she, String task, final String step) {
        final String finalShe = she.concat(finale(step));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject
                    .put("MobileNumber", getUserPhone())
                    .put("Device", getIMEI())
                    .put("lat", getLatitude())
                    .put("longit", getLongitude())
                    .put("rashi", HashLatest(finalShe))
                    .put("appName", getAppName())
                    .put("codeBase", "ANDROID");
        } catch (final JSONException e) {
            LogThis("JSONException : " + e.getMessage());
        }
        setProgressMessage(task);
        progressDialog("1");
        AndroidNetworking
                .post(getToken())
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.IMMEDIATE)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("respCode");
                            switch (status) {
                                case "000":
                                case "OK":
                                case "00":
                                    String token = response.getString("token"),
                                            payLoad = response.getString("payload"),
                                            arrayData = response.getString("data");
                                    JSONObject jPayload = new JSONObject(payLoad);
                                    String machine = jPayload.getString("Device"),
                                            uri = jPayload.getString("Uri"),
                                            extrauri = jPayload.getString("ExtraUri");;
                                    arrayData = arrayData.replace("[", "");
                                    arrayData = arrayData.replace("]", "");
                                    String[] indexValues = arrayData.split(",");
                                    char[] viceArray = machine.toCharArray();
                                    machine = "";
                                    for (String anIndexValue : indexValues) {
                                        machine = String.format("%s%s", machine, viceArray[Integer.parseInt(anIndexValue)]);
                                    }
                                    uri = SPAN(uri, machine).concat("?c=S");
                                    extrauri = SPAN(extrauri, machine);

                                    if(step.contains("RAO")) {
                                        //Use ExtraUri to fetch and Post large data chunks
                                        RAOApiCall(finalShe, extrauri, machine, responseListener, token, step);
                                        LogThis("AGNESRAO"+ "AKIFIKA");
                                    } else {
                                        connect(finalShe, uri, machine, responseListener, token, step);
                                    }         LogThis("AGNESRAO"+ uri);
                                    break;
                                default:
                                    progressDialog("0");
                                    ToastMessageLong(context, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            LogThis("JSONResponse Error ► " + e.getMessage());
                            progressDialog("0");
                            ToastMessageLong(context, context.getString(R.string.tokenTryLater));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogThis("GetToken Error ► " + anError.getMessage());
                        progressDialog("0");
                        myDialog(context, context.getString(R.string.alert), context.getString(R.string.connectionError));
                    }
                });
    }

    private void connect (String request, String baseUrl, final String result, final ResponseListener responseListener, final String token, final String step){
        LogThis("send ► " + request);
        baseUrl = baseUrl + BIND(request,result);
        AndroidNetworking
                .get(baseUrl)
                .addHeaders("T", token)
                .setPriority(Priority.HIGH)
                .doNotCacheResponse()
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog("0");
                            response=response.replace("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">","");
                            response=response.replace("\r\n","");
                            response=REVERSE(response,result);
                            LogThis("response ◄§► "+response);

                            Activity activity = (Activity)context;
                            if (activity.getClass().equals(Login.class) || activity.getClass().equals(AccountOpenSplash.class) || activity.getClass().equals(AccountOpenZMain.class) || step.equals("CON")) {
                                responseListener.onResponse(response,step);
                            } else {
                                String [] splits = response.split(":");
                                String status = splits[1], message = splits[3];
                                switch (status) {
                                    case "000":
                                    case "OK":
                                    case "00":
                                        responseListener.onResponse(message,step);
                                        break;
                                    default:
                                        myDialog(context,context.getString(R.string.alert), message);
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            LogThis("FormatError ► "+e.getMessage());
                            myDialog(context, context.getString(R.string.alert),context.getString(R.string.tryAgain));
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        LogThis("ConnectError ► " + anError.getMessage());
                        progressDialog("0");
                        myDialog(context,context.getString(R.string.alert),context.getString(R.string.connectionError));
                    }
                });
    }

    private void RAOApiCall(String request, String BaseUrl, String machine, final ResponseListener responseListener, final String Token, String step) {
        Log.e("RAO",request)  ;
        request = EncryptNoEncoding(request,machine);
        JSONObject object = new JSONObject();
        try {
            object.put("Data", "")
                    .put("ExtraData", "S" + request);
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(BaseUrl)
                .addJSONObjectBody(object)
                .addHeaders("Content-Type","application/json")
                .addHeaders("T", Token)
                .setPriority(Priority.HIGH)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LogThis("RESPOSERAO"+response.toString());
                        try {
                            progressDialog("0");
                            String rao_response = response.getString("Response");
                            rao_response= REVERSE(rao_response,machine);
                            LogThis("response ◄§► "+rao_response);
                            responseListener.onResponse(rao_response,step);
                        } catch (Exception e) {
                            e.printStackTrace();
                            myDialog(context, context.getString(R.string.alert),context.getString(R.string.tryAgain));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        LogThis("ConnectError ► " + anError.getMessage());
                        progressDialog("0");
                        myDialog(context,context.getString(R.string.alert),context.getString(R.string.connectionError));
                    }
                });

        /*RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                BaseUrl, object,
                response -> {
                    progressDialog("0");
                    responseListener.onResponse(response.toString(),step);
                },
                error -> {
                    LogThis("ConnectError ► " + error.getMessage());
                    progressDialog("0");
                    myDialog(context,context.getString(R.string.alert),context.getString(R.string.connectionError));
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("T", Token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                180000,
                -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);*/
    }

    private void vanish() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject
                    .put("MobileNumber",getUserPhone())
                    .put("Device",getIMEI())
                    .put("lat",getLatitude())
                    .put("longit",getLongitude())
                    .put("rashi","")
                    .put("appName",getAppName())
                    .put("codeBase","ANDROID");
        } catch (final JSONException e) {
            LogThis("JSONException : "+ e.getMessage());
        }
        AndroidNetworking
                .post(vanishing())
                .addJSONObjectBody(jsonObject)
                .doNotCacheResponse()
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ToastMessageLong(context,context.getString(R.string.app_name)+" "+response.getString("message"));
                        } catch (JSONException e) {
                            LogThis("JSONVan Error : "+e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        LogThis("Networking Error : "+anError.getMessage());
                    }
                });
    }


    public void setBal(String bal) {
        SP("Bal", bal);
    }

    public String getBal() {
        return gSP("Bal");
    }
}