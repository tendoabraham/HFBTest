package com.elmahousingfinanceug_test.main_Pages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.AdapterKeyValue;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Funds_Transfer extends BaseAct implements ResponseListener, VolleyResponse {
    Spinner accountNumber,accountNumber2, benSpinner;
    EditText ETAccount,ETAmount,ETMessage,ETPin;
    TextView validate, sendFunds, valName;
    RadioGroup radioGroup, toOtherRadioGroup;
    RadioButton RBTNMyAccount,RBTNOtherAccount, enterAccount, savedAccount;
    LinearLayout validateLayout, input;
    private RecyclerView valRecycler;
    String accSend="",accReceive="",recipient="",quest="", benAcc="", bankCode="", branchCode="", sendMode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds_transfer);

        gToolBar(getString(R.string.fundsTransfer));

        accountNumber = findViewById(R.id.accountNumber);
        accountNumber2 = findViewById(R.id.accountNumber2);
        ETAccount = findViewById(R.id.ETAccount);
        radioGroup = findViewById(R.id.radioGroup);
        RBTNMyAccount = findViewById(R.id.RBTNMyAccount);
        RBTNOtherAccount = findViewById(R.id.RBTNOtherAccount);
        validateLayout = findViewById(R.id.validateLayout);
        valRecycler = findViewById(R.id.valRecycler);
        input =  findViewById(R.id.input);
        ETAmount = findViewById(R.id.ETAmount);
        ETMessage = findViewById(R.id.messaage);
        ETPin = findViewById(R.id.eTPin);
        validate = findViewById(R.id.validate);
        benSpinner = findViewById(R.id.benSpinner);
        toOtherRadioGroup = findViewById(R.id.toOtherRadioGroup);
        enterAccount = findViewById(R.id.enterAccount);
        savedAccount = findViewById(R.id.savedAccount);
        sendFunds = findViewById(R.id.sendFunds);
        valName = findViewById(R.id.valName);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                ETAccount.setText("");
                // TODO: 12/17/2020 same on LIVE
                if (RBTNMyAccount.isChecked()) {
                    validateLayout.setVisibility(View.GONE);
                    input.setVisibility(View.VISIBLE);
                    toOtherRadioGroup.setVisibility(View.GONE);
                    ETAccount.setVisibility(View.GONE);
                    validate.setVisibility(View.GONE);
                    populateSpinners2();
                    accountNumber2.setVisibility(View.VISIBLE);
                } else if (RBTNOtherAccount.isChecked()) {
                    toOtherRadioGroup.setVisibility(View.VISIBLE);
                    enterAccount.setChecked(true);
                    ETAccount.setVisibility(View.VISIBLE);
                    accountNumber2.setVisibility(View.GONE);
                    validate.setVisibility(View.VISIBLE);
                    input.setVisibility(View.GONE);
                }
            }
        });
        RBTNOtherAccount.setChecked(true);

        toOtherRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ETAccount.setText("");
                if (enterAccount.isChecked()){
                    ETAccount.setVisibility(View.VISIBLE);
                    benSpinner.setVisibility(View.GONE);
                } else if (savedAccount.isChecked()){
                    ETAccount.setVisibility(View.GONE);
                    getBens();
                }
            }
        });

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
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.insufficient_funds));
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
                if(after>1) am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        valRecycler.setLayoutManager(new LinearLayoutManager(this));

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RBTNOtherAccount.isChecked() && enterAccount.isChecked() && ETAccount.getText().toString().trim().length() < 5) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.enterValidAcc));
                } else if (RBTNOtherAccount.isChecked() && accSend.equals("")) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.selectAccDebited));
                }else if (RBTNOtherAccount.isChecked() && accSend.equals(ETAccount.getText().toString().trim())) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else {

                    String account = "";

                    if (savedAccount.isChecked()){
                        account = benAcc;
                    }else {
                        account = ETAccount.getText().toString().trim();
                    }

                    quest = (
                            "FORMID:B-:" +
                                    "MERCHANTID:ACCOUNTVALIDATION:" +
                                    "TOACCOUNT:" + account + ":"
                    );
                    //am.connectOldTwo(getString(R.string.validating),quest,Funds_Transfer.this,"VAL");
                    am.get_(Funds_Transfer.this,quest,getString(R.string.validating),"VAL");
                }
            }
        });

        sendFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accSend.equals("")) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (RBTNMyAccount.isChecked() && accReceive.equals("")) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.selectAccCredited));
                } else if (RBTNMyAccount.isChecked() && accSend.equals(accReceive)) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (RBTNOtherAccount.isChecked() && ETAccount.getText().toString().trim().length() < 5) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.enterValidAcc));
                } else if (RBTNOtherAccount.isChecked() && accSend.equals(ETAccount.getText().toString().trim())) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.sameAccError));
                } else if (ETAmount.getText().toString().trim().isEmpty()) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (ETMessage.getText().toString().trim().isEmpty()) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.descReq));
                } else if (ETPin.getText().toString().trim().isEmpty()) {
                    am.myDialog(Funds_Transfer.this, getString(R.string.alert), getString(R.string.enterValidPin));
                } else {
                    if (RBTNMyAccount.isChecked()) {
                        recipient = accReceive;
                        sendMode = "IFTOWN";
                    } else {
                        recipient = ETAccount.getText().toString().trim();
                        sendMode = "IFTOTHER";
                    }
                    gDialog = new Dialog(Funds_Transfer.this);
                    //noinspection ConstantConditions
                    gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    gDialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
                    final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
                    final TextView txtNo = gDialog.findViewById(R.id.noBTN);
                    txtMessage.setText(String.format("%s %s %s %s %s %s",
                            getText(R.string.doFundsTransfer),
                            am.Amount_Thousands(ETAmount.getText().toString().trim()),
                            getText(R.string.fromAccNo),
                            accSend,
                            getText(R.string.toAccNo),
                            recipient));
                    txtOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quest = (
                                    "FORMID:B-:" +
                                            "MERCHANTID:TRANSFER:" +
                                            "BANKACCOUNTID:" + accSend + ":" +
                                            "TOACCOUNT:" + recipient + ":" +
                                            "INFOFIELD3:" + sendMode + ":" +
                                            "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                            "MESSAGE:" + ETMessage.getText().toString().trim() + ":" +
                                            "TMPIN:" + ETPin.getText().toString().trim() + ":"
                            );
                            //am.connectOldTwo(getString(R.string.processingTrx), quest, Funds_Transfer.this, "TRX");
                            am.get_(Funds_Transfer.this,quest,getString(R.string.processingTrx),"TRX");
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
//                        ETPin.setText("");
                            dialog.dismiss();
                        }
                    });
                    gDialog.show();
                }
            }
        });
    }

    private void getBens(){
        quest = (
                "FORMID:O-GETTRANSFERBENEFICIARY:" +
                        "BENFTYPE:" + "INTERNAL" + ":" +
                        "BANKID:" + am.getBankID() + ":"
        );
        am.get_(this,quest,getString(R.string.fetchingBeneficiaries) ,"GETIN");
    }

    private void populateSpinners2() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber2.setAdapter(dataAdapter);
        accountNumber2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accReceive = "";
                } else {
                    accReceive = am.getBankAccountID(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
        quest = (
                "FORMID:B-:" +
                        "MERCHANTID:TRANSFER:" +
                        "BANKACCOUNTID:" + accSend + ":" +
                        "TOACCOUNT:" + recipient + ":" +
                        "INFOFIELD8:POSTOTPVALIDATE:" +
                        "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                        "MESSAGE:" + ETMessage.getText().toString().trim() + ":" +
                        "TMPIN:" + ETPin.getText().toString().trim() + ":"
        );
        //am.connectOldTwo(getString(R.string.processingTrx), quest, Funds_Transfer.this, "TRX");
        am.get_(Funds_Transfer.this,quest,getString(R.string.processingTrx),"TRX");
        ETPin.setText("");
        gDialog.cancel();
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
    public void onResponse(String response, String step) {
        switch (step) {
            case "GETIN":
            {
                benSpinner.setVisibility(View.VISIBLE);
                String [] benItems = response.split("~");
                final List<String> listNumbers = new ArrayList<>(), benName = new ArrayList<>()
                        , bankCodex = new ArrayList<>(), branchCodex = new ArrayList<>();
                for (String aBenItem : benItems) {
                    String [] inside = aBenItem.split("\\|");
                    listNumbers.add(inside[0]);
                    benName.add(inside[1]);
                    bankCodex.add(inside[2]);
                    branchCodex.add(inside[3]);
                }
                ArrayAdapter<String> benAdapter = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item, benName);
                benAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                benSpinner.setAdapter(benAdapter);
                benSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        benAcc = listNumbers.get(position);
                        bankCode = bankCodex.get(position);
                        branchCode = branchCodex.get(position);

                        toOtherRadioGroup.setVisibility(View.VISIBLE);
                        enterAccount.setChecked(false);
                        ETAccount.setVisibility(View.GONE);
                        accountNumber2.setVisibility(View.GONE);
                        validate.setVisibility(View.VISIBLE);
                        input.setVisibility(View.GONE);
                        validateLayout.setVisibility(View.GONE);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                ETAccount.setVisibility(View.GONE);
//                Log.e("RESSSPO", response);
//                String [] splits = response.split(":");
//                String status = splits[1], message = splits[3];
//                switch (status) {
//                    case "000":
//                    case "OK":
//                    case "00":
//                        benSpinner.setVisibility(View.VISIBLE);
//                        String [] benItems = response.split("~");
//                        final List<String> listNumbers = new ArrayList<>(), benName = new ArrayList<>()
//                                , bankCodex = new ArrayList<>(), branchCodex = new ArrayList<>();
//                        for (String aBenItem : benItems) {
//                            String [] inside = aBenItem.split("\\|");
//                            listNumbers.add(inside[0]);
//                            benName.add(inside[1]);
//                            bankCodex.add(inside[2]);
//                            branchCodex.add(inside[3]);
//                        }
//                        ArrayAdapter<String> benAdapter = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item, benName);
//                        benAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//                        benSpinner.setAdapter(benAdapter);
//                        benSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                benAcc = listNumbers.get(position);
//                                bankCode = bankCodex.get(position);
//                                branchCode = branchCodex.get(position);
//                            }
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {}
//                        });
//                        ETAccount.setVisibility(View.GONE);
//                        break;
//                    default:
//                        enterAccount.setChecked(true);
//                        am.ToastMessageLong(this, message);
//                        break;
//                }
            }
            break;
            case "VAL":
                String [] howLong =response.split("\\|");
                String [] field_IDs =new String[howLong.length/2];
                String [] field_Values =new String[howLong.length/2];
                am.separate(response,"|",field_IDs ,field_Values);
                valRecycler.setAdapter(new AdapterKeyValue(field_IDs,field_Values));
                validateLayout.setVisibility(View.VISIBLE);
                valName.setText(response);
                input.setVisibility(View.VISIBLE);
                validate.setVisibility(View.GONE);
                ETAccount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        validate.setVisibility(View.VISIBLE);
                        validateLayout.setVisibility(View.GONE);
                        input.setVisibility(View.GONE);
                    }
                    @Override
                    public void afterTextChanged(Editable s) {}
                });
                break;
            case "TRX":
                am.saveDoneTrx(true);
                finish();
                startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
                break;
            case "OTPTRX":
                startActivity(new Intent(Funds_Transfer.this, OTP.class).putExtra("Merchant", am.getMerchantID()));
                break;
        }
    }
}
