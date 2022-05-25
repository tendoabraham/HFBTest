package com.elmahousingfinanceug.main_Pages;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.main_Pages.Adapters.AdapterKeyValue;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.elmahousingfinanceug.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug.recursiveClasses.VolleyResponse;

import java.util.ArrayList;
import java.util.List;

public class Bill_Payments extends BaseAct implements ResponseListener, VolleyResponse {
    Spinner accountNumber,area;
    LinearLayout step1Layout,step2Layout,step3Layout;
    EditText ETMeterAccNumber,ETAmount,ETPin;
    TextView tVAccName,tVDisplay;
    RecyclerView valRecycler;
    String pageTitle="",code="",accSend="",areaString="",areaCode="",quest="",billAccName="",billDueAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payments);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            pageTitle = extras.getString("pageTitle");
            code = extras.getString("Code");
        }

        gToolBar(pageTitle);

        accountNumber = findViewById(R.id.accountNumber);
        area = findViewById(R.id.area);
        ETMeterAccNumber = findViewById(R.id.ETMeterAccNumber);
        ETAmount = findViewById(R.id.ETAmount);
        ETPin = findViewById(R.id.eTPin);
        valRecycler = findViewById(R.id.valRecycler);
        tVAccName = findViewById(R.id.tvAccName);
        tVDisplay = findViewById(R.id.TVDisplay);
        step1Layout = findViewById(R.id.step1Layout);
        step2Layout = findViewById(R.id.step2Layout);
        step3Layout = findViewById(R.id.step3Layout);

        step1Layout.setVisibility(View.VISIBLE);
        step2Layout.setVisibility(View.GONE);
        step3Layout.setVisibility(View.GONE);
        area.setVisibility(View.GONE);

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

        if(am.getMerchantID().equals("007001003")){
            final List<String> areaNames = new ArrayList<>(), idList = new ArrayList<>();
            areaNames.add(getString(R.string.selectArea));
            String [] areaIDS = am.getAreas().split(",");
            for (String anAreaID : areaIDS) {
                String[] insideData = anAreaID.split("\\|");
                String code = insideData[0], area = insideData[1];
                idList.add(code);
                areaNames.add(area);
            }
            ArrayAdapter<String> dataAdapterA = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, areaNames);
            dataAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(dataAdapterA);
            area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        areaCode="";
                        areaString="";
                    } else {
                        areaCode = idList.get(position-1);
                        areaString = areaNames.get(position);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
            area.setVisibility(View.VISIBLE);
        } else {
            area.setVisibility(View.GONE);
        }

        if (code.equals("1")) {
            ETMeterAccNumber.setHint(getString(R.string.meterNumber));
            tVDisplay.setText(getString(R.string.enterMeterNumber));
        } else {
            ETMeterAccNumber.setHint(getString(R.string.accountNumber));
            tVDisplay.setText(getString(R.string.enterAccountNumber));
        }

        ETPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Bill_Payments.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        valRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void payments(View y) {
        switch (y.getId()){
            case R.id.validate:
                if(am.getMerchantID().equals("007001003") && accSend.equals("")) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if(am.getMerchantID().equals("007001003") && areaCode.equals("")) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAreaOne));
                } else if (ETMeterAccNumber.getText().length() < 5) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAccMet));
                } else {
                    if ("007001003".equals(am.getMerchantID())) {
                        quest = (
                                "FORMID:B-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "BANKACCOUNTID:" + accSend + ":" +
                                        "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                        "INFOFIELD1:" + areaString + ":" +
                                        "INFOFIELD2:" + "VALIDATION" + ":" +
                                        "INFOFIELD3:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                        "INFOFIELD9:" + am.getUserPhone() + ":" +
                                        "AMOUNT:" + "501" + ":" +
                                        "ACTION:GETNAME:"

                                /*"FORMID:M-:" + old
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                        "INFOFIELD1:" + areaCode + ":" +
                                        "INFOFIELD9:" + am.getUserPhone() + ":" +
                                        "ACTION:GETNAME:"*/
                        );
                    } else {
                        quest = (
                                "FORMID:M-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                        "INFOFIELD1:" + pageTitle + ":" +
                                        "INFOFIELD9:" + am.getUserPhone() + ":" +
                                        "ACTION:GETNAME:"
                        );
                    }
                    am.get(this,quest,getString(R.string.validating),"VAL");
                }
                break;
            case R.id.backIn:
                step2Layout.setVisibility(View.GONE);
                step1Layout.setVisibility(View.VISIBLE);
                step3Layout.setVisibility(View.GONE);
                break;
            case R.id.okToPay:
                step3Layout.setVisibility(View.VISIBLE);
                break;
            case R.id.pay:
                if(accSend.equals("")){
                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (ETMeterAccNumber.getText().length() < 5) {
                    ETMeterAccNumber.setError(getString(R.string.invalidInput));
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAccMet));
                } else if (ETAmount.getText().toString().trim().isEmpty()) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (ETPin.getText().length() < 4) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPin));
                } else {
                    gDialog = new Dialog(this);
                    //noinspection ConstantConditions
                    gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    gDialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
                    final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
                    final TextView txtNo = gDialog.findViewById(R.id.noBTN);
                    txtMessage.setText(String.format("%s %s %s %s %s %s %s %s.",
                            getString(R.string.make),
                            pageTitle,
                            getText(R.string.payFor),
                            ETMeterAccNumber.getText().toString().trim(),
                            getString(R.string.withAmount),
                            am.Amount_Thousands(ETAmount.getText().toString().trim()),
                            getText(R.string.fromAccNo),
                            accSend));
                    txtOk.setOnClickListener(v -> {
                        switch (am.getMerchantID()){
                            case "007001003":
                                quest = (
                                        "FORMID:B-:" +
                                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                                "BANKACCOUNTID:" + accSend + ":" +
                                                "INFOFIELD1:" + areaString + ":" +
                                                "INFOFIELD2:" + "PAYMENT" + ":" +
                                                "INFOFIELD3:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                                "INFOFIELD9:" + am.getUserPhone() + ":"+
                                                "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "ACTION:GETNAME:"

                                        /*"FORMID:M-:" + old
                                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                                "BANKACCOUNTID:" + accSend + ":" +
                                                "INFOFIELD1:" + areaCode + ":" +
                                                "INFOFIELD9:" + am.getUserPhone() + ":"+
                                                "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "ACTION:PAYBILL:"*/
                                );
                                break;
                            case "007001002":
                            case "007001012":
                                quest = (
                                        "FORMID:M-:" +
                                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                                "BANKACCOUNTID:" + accSend + ":" +
                                                "INFOFIELD1:" + billAccName + ":" +
                                                "INFOFIELD2:" + billDueAmount + ":" +
                                                "INFOFIELD3:" + am.getUserPhone() + ":"+
                                                "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "ACTION:PAYBILL:"
                                );
                                break;
                            default:
                                quest = (
                                        "FORMID:M-:" +
                                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                                "BANKACCOUNTID:" + accSend + ":" +
                                                "INFOFIELD9:" + am.getUserPhone() + ":"+
                                                "ACCOUNTID:" + ETMeterAccNumber.getText().toString().trim() + ":" +
                                                "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                                "TMPIN:" + ETPin.getText().toString().trim() + ":" +
                                                "ACTION:PAYBILL:"
                                );
                                break;
                        }
                        am.get(Bill_Payments.this,quest,getString(R.string.processingTrx),"TRX");
                        gDialog.cancel();
                    });
                    txtNo.setOnClickListener(v -> gDialog.cancel());
                    gDialog.setOnCancelListener(dialog -> {
                        ETPin.setText("");
                        dialog.dismiss();
                    });
                    gDialog.show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        ETPin.setText("");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),Settings.class));
            return true;
        } else if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(),Contact_Us.class));
            return true;
        } else if (id == R.id.main_menu) {
            finish();
            startActivity(new Intent(getApplicationContext(),Main_Menu.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step){
            case "VAL":
                if(response.contains("|")){
                    String[] howLong = response.split("\\|");
                    String[] field_IDs = new String[howLong.length/2];
                    String[] field_Values = new String[howLong.length/2];
                    am.separate(response,"|",field_IDs,field_Values);
                    switch (am.getMerchantID()){
                        case "007001012":
                        case "007001002":
                            billAccName = am.FindInArray(field_IDs, field_Values,"Name");
                            billDueAmount = am.FindInArray(field_IDs, field_Values,"Amount Due");
                            break;
                    }
                    valRecycler.setAdapter(new AdapterKeyValue(field_IDs,field_Values));
                    valRecycler.setVisibility(View.VISIBLE);
                    tVAccName.setVisibility(View.GONE);
                } else {
                    tVAccName.setText(response);
                    valRecycler.setVisibility(View.GONE);
                    tVAccName.setVisibility(View.VISIBLE);
                }
                step1Layout.setVisibility(View.GONE);
                step2Layout.setVisibility(View.VISIBLE);
                step3Layout.setVisibility(View.GONE);
                break;
            case "TRX":
                am.saveDoneTrx(true);
                finish();
                startActivity(new Intent(getApplicationContext(),SuccessDialogPage.class).putExtra("message", response));
                break;
        }
    }
}
