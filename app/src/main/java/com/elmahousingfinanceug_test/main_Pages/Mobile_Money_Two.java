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

public class Mobile_Money_Two extends BaseAct implements ResponseListener, VolleyResponse {
    TextView myNum, validateMTN;
    Spinner accNum, serviceProvider;
    EditText otherNum, ETAmount, ETPin;
    RadioGroup radioGroup;
    RadioButton rMyAccount, rOtherAccount;
    LinearLayout othNumLayout, numChoice, after, validateLayout;
    ImageView contactsVw;
    RecyclerView valRecycler;
    String accNumString = "", sendPhoneStr = "", quest;

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
        validateMTN = findViewById(R.id.validateMTN);
        after = findViewById(R.id.after);
        validateLayout = findViewById(R.id.validateLayout);
        valRecycler = findViewById(R.id.valRecycler);

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
        ArrayAdapter<String> dataAdapterSv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapterSv.setDropDownViewResource(R.layout.spinner_dropdown_item);
        serviceProvider.setAdapter(dataAdapterSv);
        serviceProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    numChoice.setVisibility(View.GONE);
                    am.saveMerchantID("");
                } else if (position == 1) {
                    after.setVisibility(View.GONE);
                    validateMTN.setVisibility(View.VISIBLE);
                    numChoice.setVisibility(View.VISIBLE);
                    rMyAccount.setChecked(true);
                    am.saveMerchantID("007001017");
                } else if (position == 2) {
                    after.setVisibility(View.VISIBLE);
                    validateMTN.setVisibility(View.GONE);
                    numChoice.setVisibility(View.VISIBLE);
                    am.saveMerchantID("007001016");
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
                if (serviceProvider.getSelectedItemPosition() == 1) {
                    ETPin.setText("");
                    validateLayout.setVisibility(View.GONE);
                    after.setVisibility(View.GONE);
                    validateMTN.setVisibility(View.VISIBLE);
                }
                if (rMyAccount.isChecked()) {
                    othNumLayout.setVisibility(View.GONE);
                    myNum.setVisibility(View.VISIBLE);
                    am.animate_View(myNum);
                } else if (rOtherAccount.isChecked()) {
                    othNumLayout.setVisibility(View.VISIBLE);
                    myNum.setVisibility(View.GONE);
                    am.animate_View(contactsVw);
                }
            }
        });

        rMyAccount.setChecked(true);
        ETAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String bal = am.getBal().replace(",", "");
                Double balance = Double.parseDouble(bal);

                DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
                //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");
                String inputedAmount = String.valueOf(s);
                if (!inputedAmount.equals("")&&(Double.parseDouble(inputedAmount)) >= balance) {
                    am.myDialog(Mobile_Money_Two.this, getString(R.string.alert), getString(R.string.insufficient_funds));
                    ETPin.setEnabled(false);
                }   


            }

            @Override
            public void afterTextChanged(Editable s) {

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
    }

    public void transferBTN(View view) {
        switch (view.getId()) {
            case R.id.validateMTN:
                if (rOtherAccount.isChecked() && otherNum.getText().length() < 4) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                    otherNum.setError(getString(R.string.enterValidPhoneAcc));
                } else {
                    if (rMyAccount.isChecked()) {
                        sendPhoneStr = am.getUserPhone();
                    } else {
                        am.saveSendPhone(otherNum.getText().toString().trim());
                        sendPhoneStr = am.getSendPhone();
                    }
                    quest = (
                            "FORMID:M-:" +
                                    "MERCHANTID:MMONEYUGMTN:" +
                                    "ACCOUNTID:" + sendPhoneStr + ":" +
                                    "BANKID:" + am.getBankID() + ":" +
                                    "ACTION:GETNAME:"
                    );
                    //am.connectOldTwo(getString(R.string.validating),quest,this,"MTN");
                    am.get(this, quest, getString(R.string.validating), "MTN");
                }
                break;
            case R.id.back:
                ETPin.setText("");
                validateLayout.setVisibility(View.GONE);
                after.setVisibility(View.GONE);
                validateMTN.setVisibility(View.VISIBLE);
                break;
            case R.id.send:
//              
                if (accNumString.equals("")) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (am.getMerchantID().equals("")) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.slctPrvdr));
                } else if (rMyAccount.isChecked() && accNumString.equals(myNum.getText().toString().trim())) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (rOtherAccount.isChecked() && otherNum.getText().length() < 4) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                    otherNum.setError(getString(R.string.enterValidPhoneAcc));
                } else if (rOtherAccount.isChecked() && accNumString.equals(otherNum.getText().toString().trim())) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (ETAmount.getText().toString().trim().isEmpty()) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (ETPin.getText().length() < 4) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPin));
                    ETPin.setError(getString(R.string.enterValidPin));
                } else {
                    if (rMyAccount.isChecked()) {
                        sendPhoneStr = am.getUserPhone();
                    } else {
                        am.saveSendPhone(otherNum.getText().toString().trim());
                        sendPhoneStr = am.getSendPhone();
                    }
                    gDialog = new Dialog(this);
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


                            quest = (
                                    "FORMID:M-:" +
                                            "MERCHANTID:" + am.getMerchantID() + ":" +
                                            "BANKACCOUNTID:" + accNumString + ":" +
                                            "ACCOUNTID:" + sendPhoneStr + ":" +
                                            "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                            "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                            "MESSAGE:MOBILE MONEY:" +
                                            "ACTION:PAYBILL:"
                            );
                            //am.connectOldTwo(getString(R.string.processingTrx),quest,Mobile_Money_Two.this,"TRX");
                            am.get(Mobile_Money_Two.this, quest, getString(R.string.processingTrx), "TRX");
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
                            ETPin.setText("");
                            dialog.dismiss();
                        }
                    });
                    gDialog.show();
                }
                break;
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
        ETPin.setText("");
        super.onResume();
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
            case "MTN":
                String[] howLong = response.split("\\|");
                String[] field_IDs = new String[howLong.length / 2];
                String[] field_Values = new String[howLong.length / 2];
                am.separate(response, "|", field_IDs, field_Values);
                valRecycler.setAdapter(new AdapterKeyValue(field_IDs, field_Values));
                validateMTN.setVisibility(View.GONE);
                validateLayout.setVisibility(View.VISIBLE);
                after.setVisibility(View.VISIBLE);
                break;
            case "TRX":
                am.saveDoneTrx(true);
                finish();
                startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
                break;
        }
    }
}
