package com.elmahousingfinanceug_test.main_Pages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Mobile_Money extends BaseAct implements ResponseListener, VolleyResponse {
    TextView myNum, sendMoney;
    Spinner accNum,serviceProvider;
    EditText ETOtherNum,ETAmount,ETPin;
    RadioGroup radioGroup;
    RadioButton rMyPhone,rOtherPhone;
    LinearLayout mobileNumbers,othNumLayout;
    ImageView contactsVw;
    String accSend = "",phoneReceive = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_money);

        gToolBar(getString(R.string.mobileMoney));

        accNum = findViewById(R.id.accno);
        myNum = findViewById(R.id.myNumber);
        myNum.setText(am.getUserPhone());
        ETOtherNum = findViewById(R.id.otherNum);
        ETAmount = findViewById(R.id.ETAmount);
        ETPin = findViewById(R.id.eTPin);
        radioGroup = findViewById(R.id.radioGroup);
        rMyPhone = findViewById(R.id.rMyPhone);
        rOtherPhone = findViewById(R.id.rOtherPhone);
        serviceProvider = findViewById(R.id.serviceProvider);
        mobileNumbers = findViewById(R.id.mNums);
        othNumLayout = findViewById(R.id.othNumLyt);
        contactsVw= findViewById(R.id.contacts);
        sendMoney = findViewById(R.id.sendMoney);

        mobileNumbers.setVisibility(View.GONE);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accNum.setAdapter(dataAdapter);
        accNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accSend = "";
                } else {
                    accSend = am.getBankAccountID(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rMyPhone.isChecked()) {
                    othNumLayout.setVisibility(View.GONE);
                    myNum.setVisibility(View.VISIBLE);
                    am.animate_View(myNum);
                } else if (rOtherPhone.isChecked()) {
                    othNumLayout.setVisibility(View.VISIBLE);
                    myNum.setVisibility(View.GONE);
                    am.animate_View(contactsVw);
                }
            }
        });
        rMyPhone.setChecked(true);

        final List<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.selectOne));
        arrayList.add(getString(R.string.mtn));
        arrayList.add(getString(R.string.airtel));
        ArrayAdapter<String> dataAdapterSv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapterSv.setDropDownViewResource(R.layout.spinner_dropdown_item);
        serviceProvider.setAdapter(dataAdapterSv);
        serviceProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mobileNumbers.setVisibility(View.GONE);
                    am.saveMerchantID("");
                } else if (position == 1) {
                    mobileNumbers.setVisibility(View.GONE);
                    rMyPhone.setChecked(true);
                    am.saveMerchantID("007001017");
                } else if (position == 2) {
                    mobileNumbers.setVisibility(View.VISIBLE);
                    am.saveMerchantID("007001016");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                am.saveMerchantID("");
            }
        });

        ETPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accSend.equals("")) {
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (am.getMerchantID().equals("")) {
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.slctPrvdr));
                } else if (rMyPhone.isChecked() && accSend.equals(myNum.getText().toString().trim())){
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (rOtherPhone.isChecked() && ETOtherNum.getText().length() < 4){
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                } else if (rOtherPhone.isChecked() && accSend.equals(ETOtherNum.getText().toString().trim())){
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (ETAmount.getText().toString().trim().isEmpty()) {
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (ETPin.getText().length() < 4) {
                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.enterValidPin));
                } else {
                    if (rMyPhone.isChecked()) {
                        phoneReceive = am.getUserPhone();
                    } else {
                        am.saveSendPhone(ETOtherNum.getText().toString().trim());
                        phoneReceive = am.getSendPhone();
                    }
                    gDialog = new Dialog(Mobile_Money.this);
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
                            accSend,
                            getText(R.string.toPhone),
                            phoneReceive));
                    txtOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AllMethods.isNumeric(am.getBal())) {
                                Double balance = Double.parseDouble(am.getBal());
                                DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
                                //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");

                                if (Double.parseDouble((ETAmount.getText().toString())) >= balance) {

                                    am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.insufficient_funds));
                                } else {
                                    String quest = (
                                            "FORMID:M-:" +
                                                    "MERCHANTID:" + am.getMerchantID() + ":" +
                                                    "BANKACCOUNTID:" + accSend + ":" +
                                                    "ACCOUNTID:" + phoneReceive + ":" +
                                                    "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                    "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                    "MESSAGE:MOBILE MONEY:" +
                                                    "ACTION:PAYBILL:"
                                    );
                                    //am.connectOldTwo(getString(R.string.processingTrx),quest,Mobile_Money.this,"TRX");
                                    am.get_(Mobile_Money.this, quest, getString(R.string.processingTrx), "TRX");
                                    gDialog.cancel();
                                }

                            }
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
                            ETPin.setText("");
                            dialog.dismiss();
                        }
                    });
                    gDialog.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        ETPin.setText("");
        super.onResume();
    }

//    public void sm$(View view) {
//        if (accSend.equals("")) {
//            am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
//        } else if (am.getMerchantID().equals("")) {
//            am.myDialog(this, getString(R.string.alert), getString(R.string.slctPrvdr));
//        } else if (rMyPhone.isChecked() && accSend.equals(myNum.getText().toString().trim())){
//            am.myDialog(this, getString(R.string.alert), getString(R.string.sameAccError));
//        } else if (rOtherPhone.isChecked() && ETOtherNum.getText().length() < 4){
//            am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
//        } else if (rOtherPhone.isChecked() && accSend.equals(ETOtherNum.getText().toString().trim())){
//            am.myDialog(this, getString(R.string.alert), getString(R.string.sameAccError));
//        } else if (ETAmount.getText().toString().trim().isEmpty()) {
//            am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
//        } else if (ETPin.getText().length() < 4) {
//            am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPin));
//        } else {
//            if (rMyPhone.isChecked()) {
//                phoneReceive = am.getUserPhone();
//            } else {
//                am.saveSendPhone(ETOtherNum.getText().toString().trim());
//                phoneReceive = am.getSendPhone();
//            }
//            gDialog = new Dialog(this);
//            //noinspection ConstantConditions
//            gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            gDialog.setContentView(R.layout.dialog_confirm);
//            final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
//            final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
//            final TextView txtNo = gDialog.findViewById(R.id.noBTN);
//            txtMessage.setText(String.format("%s %s %s %s %s %s.",
//                    getText(R.string.sendMoneyto),
//                    am.Amount_Thousands(ETAmount.getText().toString().trim()),
//                    getText(R.string.fromAccNo),
//                    accSend,
//                    getText(R.string.toPhone),
//                    phoneReceive));
//            txtOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (AllMethods.isNumeric(am.getBal())) {
//                        Double balance = Double.parseDouble(am.getBal());
//                        DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
//                        //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");
//
//                        if (Double.parseDouble((ETAmount.getText().toString())) >= balance) {
//
//                            am.myDialog(Mobile_Money.this, getString(R.string.alert), getString(R.string.insufficient_funds));
//                        } else {
//                            String quest = (
//                                    "FORMID:M-:" +
//                                            "MERCHANTID:" + am.getMerchantID() + ":" +
//                                            "BANKACCOUNTID:" + accSend + ":" +
//                                            "ACCOUNTID:" + phoneReceive + ":" +
//                                            "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
//                                            "TMPIN:" + ETPin.getText().toString().trim() + ":" +
//                                            "MESSAGE:MOBILE MONEY:" +
//                                            "ACTION:PAYBILL:"
//                            );
//                            //am.connectOldTwo(getString(R.string.processingTrx),quest,Mobile_Money.this,"TRX");
//                            am.get_(Mobile_Money.this, quest, getString(R.string.processingTrx), "TRX");
//                            gDialog.cancel();
//                        }
//
//                    }
//                }
//            });
//            txtNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    gDialog.cancel();
//                }
//            });
//            gDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    ETPin.setText("");
//                    dialog.dismiss();
//                }
//            });
//            gDialog.show();
//        }
//    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor c = null;
                    try {
                        c = getContentResolver().query(uri, new String[]{
                                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                                        ContactsContract.CommonDataKinds.Phone.TYPE },
                                null, null, null);
                        if (c != null && c.moveToFirst()) {
                            String number = c.getString(0).replace("+","");
                            int type = c.getInt(1);
                            ETOtherNum.setText(PhoneNumberUtils.stripSeparators(number));
                        }
                    } finally {
                        if (c != null) c.close();
                    }
                } else {
                    am.ToastMessage(getApplicationContext(),getString(R.string.plstryAgn));
                }
            } else {
                am.ToastMessage(getApplicationContext(),getString(R.string.errRetrievePhone));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResponse(String response, String step) {
        am.saveDoneTrx(true);
        finish();
        startActivity(new Intent(getApplicationContext(),SuccessDialogPage.class).putExtra("message", response));
    }
}
