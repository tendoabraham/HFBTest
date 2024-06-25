package com.elmahousingfinanceug_test.main_Pages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.AdapterKeyValue;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mobile_Money_Two extends BaseAct implements ResponseListener, VolleyResponse {
    TextView myNum, airtelResponse, validate, back, send;
    Spinner accNum, serviceProvider, benSpinner;
    EditText otherNum, ETAmount, ETPin;
    RadioGroup radioGroup, toOtherRadioGroup;
    RadioButton rMyAccount, rOtherAccount, enterNumber, savedBen;
    LinearLayout othNumLayout, numChoice, after, validateLayout,airtelLayout;
    ImageView contactsVw;
    RecyclerView valRecycler;
    String accNumString = "", sendPhoneStr = "", quest, utilityID, benAcc, merchant, valName="";
    int selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_money_two);

        gToolBar(getString(R.string.mobileMoney));

        accNum = findViewById(R.id.accno);
        myNum = findViewById(R.id.myNumber);
        myNum.setText(am.getUserPhone());
        otherNum = findViewById(R.id.otherNum);
        ETAmount = findViewById(R.id.ETAmount);
        ETPin = findViewById(R.id.ETPin);
        radioGroup = findViewById(R.id.radioGroup);
        rMyAccount = findViewById(R.id.rMyAccount);
        rOtherAccount = findViewById(R.id.rOtherAccount);
        serviceProvider = findViewById(R.id.serviceProvider);
        numChoice = findViewById(R.id.numChoice);
        othNumLayout = findViewById(R.id.otherNumLayout);
        contactsVw = findViewById(R.id.contacts);
        validate = findViewById(R.id.validate);
        after = findViewById(R.id.after);
        validateLayout = findViewById(R.id.validateLayout);
        valRecycler = findViewById(R.id.valRecycler);
        airtelResponse = findViewById(R.id.airtelResponse);
        airtelLayout = findViewById(R.id.airtelLayout);
        enterNumber = findViewById(R.id.enterNumber);
        savedBen = findViewById(R.id.savedBen);
        benSpinner = findViewById(R.id.benSpinner);
        toOtherRadioGroup = findViewById(R.id.toOtherRadioGroup);
        back = findViewById(R.id.back);
        send = findViewById(R.id.send);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accNum.setAdapter(dataAdapter);
        accNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accNumString = "";
                } else {
                    accNumString = am.getBankAccountID(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final List<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.selectOne));
        arrayList.add(getString(R.string.mtn));
        arrayList.add(getString(R.string.airtel));
        arrayList.add(getString(R.string.wendi));
        ArrayAdapter<String> dataAdapterSv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapterSv.setDropDownViewResource(R.layout.spinner_dropdown_item);
        serviceProvider.setAdapter(dataAdapterSv);
        serviceProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
//                    numChoice.setVisibility(View.GONE);
                    am.saveMerchantID("");
                    selection = 0;
                } else if (position == 1) {
                    after.setVisibility(View.GONE);
                    numChoice.setVisibility(View.VISIBLE);
                    rMyAccount.setChecked(true);
                    validate.setVisibility(View.VISIBLE);
                    am.saveMerchantID("007001017");
                    selection = 1;
                } else if (position == 2) {
                    after.setVisibility(View.GONE);
                    numChoice.setVisibility(View.VISIBLE);
                    rMyAccount.setChecked(true);
                    validate.setVisibility(View.VISIBLE);
                    am.saveMerchantID("007001016");
                    selection = 2;
                }else if (position == 3){
                    after.setVisibility(View.GONE);
                    numChoice.setVisibility(View.VISIBLE);
                    rMyAccount.setChecked(true);
                    validate.setVisibility(View.VISIBLE);
                    am.saveMerchantID("HFBWENDI");
                    selection = 3;
                }
                validateLayout.setVisibility(View.GONE);
                ETPin.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                am.saveMerchantID("");
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ETPin.setText("");
                validateLayout.setVisibility(View.GONE);
                if (rMyAccount.isChecked()) {
                    othNumLayout.setVisibility(View.GONE);
                    myNum.setVisibility(View.VISIBLE);
                    am.animate_View(myNum);
                    toOtherRadioGroup.setVisibility(View.GONE);
                    enterNumber.setChecked(true);
                    after.setVisibility(View.GONE);

                } else if (rOtherAccount.isChecked()) {
                    after.setVisibility(View.GONE);
                    othNumLayout.setVisibility(View.VISIBLE);
                    myNum.setVisibility(View.GONE);
                    am.animate_View(contactsVw);
                    toOtherRadioGroup.setVisibility(View.VISIBLE);
                    validate.setVisibility(View.VISIBLE);
                    otherNum.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                    otherNum.setBackgroundResource(R.drawable.bckgrd_text_fields);
                    otherNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }
        });

        rMyAccount.setChecked(true);

        toOtherRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                otherNum.setText("");
                if (enterNumber.isChecked()){
                    othNumLayout.setVisibility(View.VISIBLE);
                    benSpinner.setVisibility(View.GONE);
                    validate.setVisibility(View.VISIBLE);
                    after.setVisibility(View.GONE);
                    otherNum.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                    otherNum.setBackgroundResource(R.drawable.bckgrd_text_fields);
                    otherNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (savedBen.isChecked()){
                    validate.setVisibility(View.GONE);
                    othNumLayout.setVisibility(View.GONE);
                    validateLayout.setVisibility(View.GONE);
                    getBens();
                }
            }
        });

        ETPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after > 1)
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.copyPaste));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 1) ETPin.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        valRecycler.setLayoutManager(new LinearLayoutManager(this));

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(am.getMerchantID(), "007001017")) {
                    if (rOtherAccount.isChecked() && otherNum.getText().length() < 4) {
                        am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                        otherNum.setError(getString(R.string.enterValidPhoneAcc));
                    } else {
                        if (rMyAccount.isChecked()) {
                            sendPhoneStr = am.getUserPhone();
                        } else {
                            am.saveSendPhone(otherNum.getText().toString().trim());
                            sendPhoneStr = am.getSendPhone();
                        }
//                    String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
//                    String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
//                    String number = noSpecialXters.replaceFirst("0", "256");
//                    String strZipCodeRemovalPattern = "^256+(?!$)";
//                    String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "0");

                        quest = (
                                "FORMID:M-:" +
                                        "MERCHANTID:MMONEYUGMTN:" +
                                        "INFOFIELD1:VALIDATE:" +
                                        "INFOFIELD2:MTN:" +
//                                        "TOACCOUNTID:" + sendPhoneStr + ":" +
                                        "ACCOUNTID:" + sendPhoneStr + ":" +
                                        "BANKID:" + am.getBankID() + ":" +
                                        "ACTION:GETNAME:"
                        );
                        //am.connectOldTwo(getString(R.string.validating),quest,this,"MTN");
                        am.get_(Mobile_Money_Two.this, quest, getString(R.string.validating), "MTN");
                    }
                } else if (Objects.equals(am.getMerchantID(), "007001016")) {
                    if (rOtherAccount.isChecked() && otherNum.getText().length() < 4) {
                        am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                        otherNum.setError(getString(R.string.enterValidPhoneAcc));
                    } else {
                        if (rMyAccount.isChecked()) {
                            sendPhoneStr = am.getUserPhone();
                        } else {
                            am.saveSendPhone(otherNum.getText().toString().trim());
                            sendPhoneStr = am.getSendPhone();
                        }
                        String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
                        String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
                        String strZipCodeRemovalPattern = "^256+(?!$)";
                        String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "0");
//                    String strPattern = "^0+(?!$)";
//                    String mobilenumber = cleanMobile.replaceAll(strPattern, "");

                        quest = (
                                "FORMID:M-:" +
                                        "MERCHANTID:HFBMMONEY:" +
                                        "INFOFIELD1:VALIDATE:" +
                                        "INFOFIELD2:AIRTEL:" +
                                        "ACCOUNTID:" + cleanMobile + ":" +
//                                        "SERVICEACCOUNTID:" + cleanMobile + ":" +
                                        "COUNTRY:" + am.getCountry() + ":" +
                                        "BANKNAME:HFB:" +
                                        "BANKID:" + am.getBankID() + ":" +
                                        "ACTION:GETNAME:"
                        );
                        am.get_(Mobile_Money_Two.this, quest, getString(R.string.validating), "AIRTEL");
                    }
                } else if (Objects.equals(am.getMerchantID(), "HFBWENDI")) {
                    if (rOtherAccount.isChecked() && otherNum.getText().length() < 4) {
                        am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                        otherNum.setError(getString(R.string.enterValidPhoneAcc));
                    } else {
                        if (rMyAccount.isChecked()) {
                            sendPhoneStr = am.getUserPhone();
                        } else {
                            am.saveSendPhone(otherNum.getText().toString().trim());
                            sendPhoneStr = am.getSendPhone();
                        }
//                String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
//                String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
//                String strZipCodeRemovalPattern = "^256+(?!$)";
//                String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "0");
//                String strPattern = "^0+(?!$)";
//                String mobilenumber = cleanMobile.replaceAll(strPattern, "");


                        quest = (
                                "FORMID:M-:" +
                                        "MERCHANTID:HFBWENDY :" +
                                        "INFOFIELD1:CUSTINQ:" +
                                        "INFOFIELD3:" + sendPhoneStr + ":" +
                                        "COUNTRY:" + am.getCountry() + ":" +
                                        "BANKNAME:HFB:" +
                                        "BANKID:" + am.getBankID() + ":" +
                                        "ACTION:GETNAME:"
                        );
                        am.get_(Mobile_Money_Two.this, quest, getString(R.string.validating), "WENDI");
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETPin.setText("");
                validateLayout.setVisibility(View.GONE);
                after.setVisibility(View.GONE);
                airtelLayout.setVisibility(View.GONE);
                otherNum.setKeyListener(new EditText(getApplicationContext()).getKeyListener());
                validate.setVisibility(View.VISIBLE);
                otherNum.setBackgroundResource(R.drawable.bckgrd_text_fields);
                otherNum.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accNumString.equals("")) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (am.getMerchantID().equals("")) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.slctPrvdr));
                } else if (rMyAccount.isChecked() && accNumString.equals(myNum.getText().toString().trim())) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (rOtherAccount.isChecked() && enterNumber.isChecked() && otherNum.getText().length() < 4) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                    otherNum.setError(getString(R.string.enterValidPhoneAcc));
                } else if (rOtherAccount.isChecked() && accNumString.equals(otherNum.getText().toString().trim())) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (ETAmount.getText().toString().trim().isEmpty()) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (ETPin.getText().length() < 4) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.enterValidPin));
                    ETPin.setError(getString(R.string.enterValidPin));
                } else {
                    if (rMyAccount.isChecked()) {
                        sendPhoneStr = am.getUserPhone();
                    } else {
                        if (enterNumber.isChecked()){
                            am.saveSendPhone(otherNum.getText().toString().trim());
                            sendPhoneStr = am.getSendPhone();
                        } else if (savedBen.isChecked()){
                            sendPhoneStr = benAcc;
                        }
                    }
                    gDialog = new Dialog(Mobile_Money_Two.this);
                    //noinspection ConstantConditions
                    gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    gDialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
                    final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
                    final TextView txtNo = gDialog.findViewById(R.id.noBTN);

                    txtMessage.setText(String.format("%s %s %s %s %s %s.",
                            getText(R.string.sendMoneyto),
                            am.Amount_Thousands(ETAmount.getText().toString().trim()),
                            getText(R.string.fromAccNo),
                            accNumString,
                            getText(R.string.toAccNo),
                            sendPhoneStr));

                    txtOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String merchant = "";
                            String merchantID = am.getMerchantID();

                            String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
                            String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
                            String strZipCodeRemovalPattern = "^256+(?!$)";
                            String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "0");

                            if (Objects.equals(merchantID, "007001017")){
                                merchant = "MTN";

                                quest = (
                                        "FORMID:B-:" +
                                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                                "BANKACCOUNTID:" + accNumString + ":" +
                                                "TOACCOUNT:" + sendPhoneStr + ":" +
                                                "SERVICENAME:" + "MMONEYUGMTN:" +
                                                "INFOFIELD2:MTN:" +
                                                "INFOFIELD3:B2C:" +
                                                "INFOFIELD6:" + valName + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "MESSAGE:MOBILE MONEY:" +
                                                "ACTION:PAYBILL:"
                                );
                            }else if (Objects.equals(merchantID, "007001016")){
                                merchant = "AIRTEL";

                                quest = (
                                        "FORMID:B-:" +
                                                "MERCHANTID:HFBMMONEY:" +
                                                "BANKACCOUNTID:" + accNumString + ":" +
                                                "TOACCOUNT:" + cleanMobile + ":" +
                                                "INFOFIELD2:" + merchant + ":" +
                                                "INFOFIELD3:B2C:" +
                                                "INFOFIELD4:" + accNumString + ":" +
                                                "INFOFIELD5:" + ETAmount.getText().toString().trim() + ":" +
                                                "INFOFIELD6:" + valName + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "MESSAGE:MOBILE MONEY:" +
                                                "ACTION:PAYBILL:"
                                );
                            } else{

                                quest = (
                                        "FORMID:M-:" +
                                                "MERCHANTID:HFBWENDYPAYMENT:" +
                                                "BANKACCOUNTID:" + accNumString + ":" +
                                                "INFOFIELD1:TRANSFER_CUST:" +
                                                "INFOFIELD2:" + airtelResponse.getText().toString().trim() + ":" +
                                                "INFOFIELD3:" + cleanMobile + ":" +
                                                "INFOFIELD4:" + accNumString + ":" +
                                                "TOACCOUNTID:" + cleanMobile + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "BANKID:23:" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "MESSAGE:MOBILE MONEY:" +
                                                "ACTION:GETNAME:"
                                );
                            }

                            //am.connectOldTwo(getString(R.string.processingTrx),quest,Mobile_Money_Two.this,"TRX");

//                          TODO: Uncomment before upload
                            am.get_(Mobile_Money_Two.this, quest, getString(R.string.processingTrx), "TRX");
//                            startActivity(new Intent(Mobile_Money_Two.this, OTP.class).putExtra("Merchant", am.getMerchantID()));
                            gDialog.cancel();


                        }
                    });
                    txtNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gDialog.cancel();
                        }
                    });
                    gDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
//                            ETPin.setText("");
                            dialog.dismiss();
                        }
                    });
                    gDialog.show();
                }
            }
        });
    }

    private void getBens(){
        if(Objects.equals(am.getMerchantID(), "")){
            am.myDialog(this, getString(R.string.alert), getString(R.string.selectServiceProvider));
        } else {
            if (Objects.equals(am.getMerchantID(), "007001017")){
                utilityID = "MTN Money";
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:MMONEY:" +
                                "SERVICEID:" + "MMONEYUGMTN" + ":"
                );
            }else if (Objects.equals(am.getMerchantID(), "007001016")){
                utilityID = "Airtel Money";
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:MMONEY:" +
                                "SERVICEID:" + "MMONEYUGAIRTEL" + ":"
                );
            }else{
                utilityID = "Wendi";
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:MMONEY:" +
                                "SERVICEID:" + "HFBWENDI" + ":"
                );
            }
            am.get_(this,quest,getString(R.string.fetchingBeneficiaries) + " " + getString(R.string.forWord) + " " + utilityID,"BEN");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), Settings.class));
            return true;
        } else if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(), Contact_Us.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        String status = am.getOTPStatus();
//        Log.e("STAT", status);
        if (Objects.equals(status, "1")){
            otpTrxRequest();
            am.saveOTPStatus("0");
        }
//        ETPin.setText("");
        super.onResume();
    }

    private void otpTrxRequest(){
        String merchant = "";
        String merchantID = am.getMerchantID();

        String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
        String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
        String strZipCodeRemovalPattern = "^256+(?!$)";
        String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "0");

        if (Objects.equals(merchantID, "007001017")){
            quest = (
                    "FORMID:M-:" +
                            "MERCHANTID:" + am.getMerchantID() + ":" +
                            "BANKACCOUNTID:" + accNumString + ":" +
                            "ACCOUNTID:" + sendPhoneStr + ":" +
                            "SERVICEACCOUNTID:" + sendPhoneStr + ":" +
                            "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                            "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                            "INFOFIELD8:POSTOTPVALIDATE:" +
                            "MESSAGE:MOBILE MONEY:" +
                            "ACTION:PAYBILL:"
            );
        }else {
            quest = (
                    "FORMID:M-:" +
                            "MERCHANTID:HFBMMONEY:" +
                            "BANKACCOUNTID:" + accNumString + ":" +
                            "ACCOUNTID:" + cleanMobile + ":" +
                            "SERVICEACCOUNTID:" + cleanMobile + ":" +
                            "INFOFIELD1:PAYMENT:" +
                            "INFOFIELD2:" + merchant + ":" +
                            "INFOFIELD3:B2C:" +
                            "INFOFIELD4:" + accNumString + ":" +
                            "INFOFIELD5:" + ETAmount.getText().toString().trim() + ":" +
                            "INFOFIELD8:POSTOTPVALIDATE:" +
                            "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                            "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                            "MESSAGE:MOBILE MONEY:" +
                            "ACTION:PAYBILL:"
            );
        }
        //am.connectOldTwo(getString(R.string.processingTrx),quest,Mobile_Money_Two.this,"TRX");
        am.get_(Mobile_Money_Two.this, quest, getString(R.string.processingTrx), "TRX");
        ETPin.setText("");
        gDialog.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    try (Cursor c = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE},
                            null, null, null)) {
                        if (c != null && c.moveToFirst()) {
                            int type = c.getInt(1);
                            String number = c.getString(0).replace("+", "").trim();
                            otherNum.setText(PhoneNumberUtils.stripSeparators(number));
                        }
                    }
                } else {
                    am.ToastMessage(getApplicationContext(), getString(R.string.error) + " " + getString(R.string.plstryAgn));
                }
            } else {
                am.ToastMessage(getApplicationContext(), getString(R.string.errRetrievePhone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step) {
//            case "MTN":
//                Log.e("REEE", response);
//                String[] howLong = response.split("\\|");
//                String[] field_IDs = new String[howLong.length / 2];
//                String[] field_Values = new String[howLong.length / 2];
//                am.separate(response, "|", field_IDs, field_Values);
//                valRecycler.setVisibility(View.VISIBLE);
//                valRecycler.setAdapter(new AdapterKeyValue(field_IDs, field_Values));
//                airtelLayout.setVisibility(View.GONE);
//                validateMTN.setVisibility(View.GONE);
//                validateLayout.setVisibility(View.VISIBLE);
//                after.setVisibility(View.VISIBLE);
//                airtelLayout.setVisibility(View.GONE);

//                String[] parts = response.split("\\|");
//
//                // Iterate through the parts to find the name
//                String name = null;
//                for (int i = 0; i < parts.length; i += 2) {
//                    if (parts[i].equals("NAME")) {
//                        name = parts[i + 1];
//                        break;
//                    }
//                }
//                valName = name;
//                validate.setVisibility(View.GONE);
//                validateLayout.setVisibility(View.VISIBLE);
//                airtelLayout.setVisibility(View.VISIBLE);
//                airtelResponse.setText(name);
//                after.setVisibility(View.VISIBLE);
//                valRecycler.setVisibility(View.GONE);
//                otherNum.setKeyListener(null);
//                otherNum.setBackgroundColor(getResources().getColor(R.color.light_grey3));
//                break;
            case "AIRTEL":
            case "MTN":
            case "WENDI":
                validate.setVisibility(View.GONE);
                validateLayout.setVisibility(View.VISIBLE);
                airtelLayout.setVisibility(View.VISIBLE);
                String cleanName = response.replaceAll("[\".?]", "");
                airtelResponse.setText(cleanName);
                valName = cleanName;
                after.setVisibility(View.VISIBLE);
                valRecycler.setVisibility(View.GONE);
                otherNum.setKeyListener(null);
                otherNum.setBackgroundColor(getResources().getColor(R.color.light_grey3));
                break;
            case "TRX":
                am.saveDoneTrx(true);
                finish();
                startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
                break;
            case "OTPTRX":
                startActivity(new Intent(Mobile_Money_Two.this, OTP.class).putExtra("Merchant", am.getMerchantID()));
                break;
            case "BEN":
                validate.setVisibility(View.GONE);
                after.setVisibility(View.VISIBLE);
                String [] benZ  = response.split(";");
                final List <String> listMerchant = new ArrayList<>(),
                        listAccOrPhones = new ArrayList<>(),
                        listNames = new ArrayList<>();
                for (String aBenZ : benZ) {
                    String [] inside = aBenZ.split(",");
                    listMerchant.add(inside[0]);
                    listAccOrPhones.add(inside[1]);
                    listNames.add(inside[2]);
                }
                ArrayAdapter<String> benZAdapter = new ArrayAdapter<>(this ,R.layout.spinner_dropdown_item, listNames);
                benZAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                benSpinner.setAdapter(benZAdapter);
                benSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        benAcc = listAccOrPhones.get(position);
                        merchant = listMerchant.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                othNumLayout.setVisibility(View.GONE);
                benSpinner.setVisibility(View.VISIBLE);
                break;
        }
    }
}
