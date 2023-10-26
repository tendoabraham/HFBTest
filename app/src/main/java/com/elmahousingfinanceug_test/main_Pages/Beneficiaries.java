package com.elmahousingfinanceug_test.main_Pages;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.AdapterKeyValue;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Beneficiaries extends BaseAct implements ResponseListener {
    Spinner service, serviceProvider, areas, benefit;
    LinearLayout providerLayout, numChoice, numChoice2, nwscAreas,addBen, viewBen, delBen;
    EditText otherNum;
    RadioButton add, viewBtn, delete;
    RadioGroup radioGroupType;
    TextInputEditText meter, phoneNum, benName;
    String areaString="",areaCode="",quest="", merchantName, serviceType, utilityID,benAcc,merchant;
    RecyclerView benRecords;
    TextView benTitle, benTitleDel, validateMeter, addBeneficiary, validate, validateMNO;
    public Dialog mDialog = null;
    private final ArrayList <String> benDataSet = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiaries);

        gToolBar(getString(R.string.benMgt));

//        TODO: Check all Loggings and comment them out

        serviceProvider = findViewById(R.id.serviceProvider);
        service = findViewById(R.id.service);
        providerLayout = findViewById(R.id.providerLayout);
        otherNum = findViewById(R.id.otherNum);
        numChoice = findViewById(R.id.numChoice);
        numChoice2 = findViewById(R.id.numChoice2);
        add = findViewById(R.id.add);
        viewBtn = findViewById(R.id.view);
        delete = findViewById(R.id.delete);
        meter = findViewById(R.id.meter);
        areas = findViewById(R.id.areas);
        nwscAreas = findViewById(R.id.nwscAreas);
        phoneNum = findViewById(R.id.phoneNum);
        benName = findViewById(R.id.benName);
        addBen = findViewById(R.id.addBen);
        benRecords = findViewById(R.id.benRecords);
        viewBen = findViewById(R.id.viewBen);
        delBen = findViewById(R.id.delBen);
        benTitle = findViewById(R.id.benTitle);
        benTitleDel = findViewById(R.id.benTitleDel);
        benefit = findViewById(R.id.benefit);
        radioGroupType = findViewById(R.id.radioGroupType);
        validateMeter = findViewById(R.id.validateMeter);
        addBeneficiary = findViewById(R.id.addBeneficiary);
        validate = findViewById(R.id.validate);
        validateMNO = findViewById(R.id.validateMNO);

        validateMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateMeters();
            }
        });

        addBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBen();
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delBen();
            }
        });

        validateMNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateMNO();
            }
        });

        add.setChecked(true);

        final List<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.selectOne));
        arrayList.add(getString(R.string.airtimeServ));
        arrayList.add(getString(R.string.mm));
        arrayList.add(getString(R.string.bills));
        arrayList.add(getString(R.string.ift));
//        arrayList.add(getString(R.string.schoolFees));


        ArrayAdapter<String> dataAdapterSv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapterSv.setDropDownViewResource(R.layout.spinner_dropdown_item);
        service.setAdapter(dataAdapterSv);
        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                benName.setText("");
                updateItemSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                am.saveMerchantID("");
            }
        });

        serviceProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (add.isChecked()){
                    add(selectedItem);
                    delBen.setVisibility(View.GONE);
                    viewBen.setVisibility(View.GONE);
                } else if (viewBtn.isChecked()) {
                    viewBens(selectedItem);
                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.GONE);
                    delBen.setVisibility(View.GONE);
                }else if (delete.isChecked()){
                    viewBens(selectedItem);
                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.GONE);
                    viewBen.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.add:
                        delBen.setVisibility(View.GONE);
                        viewBen.setVisibility(View.GONE);
                        break;
                    case R.id.view:
                        numChoice.setVisibility(View.GONE);
                        numChoice2.setVisibility(View.GONE);
                        delBen.setVisibility(View.GONE);
                        addBen.setVisibility(View.GONE);
                        break;
                    case R.id.delete:
                        numChoice.setVisibility(View.GONE);
                        numChoice2.setVisibility(View.GONE);
                        viewBen.setVisibility(View.GONE);
                        addBen.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void updateItemSpinner(int categoryPosition) {
        ArrayAdapter<CharSequence> itemAdapter;
        addBen.setVisibility(View.GONE);

        switch (categoryPosition) {
            case 1: // Airtime
                providerLayout.setVisibility(View.VISIBLE);
                itemAdapter = ArrayAdapter.createFromResource(this,
                        R.array.airtime_array, android.R.layout.simple_spinner_item);
                break;
            case 2: // Mobile Money
                providerLayout.setVisibility(View.VISIBLE);
                itemAdapter = ArrayAdapter.createFromResource(this,
                        R.array.mm_array, android.R.layout.simple_spinner_item);
                break;
            case 3: // Bill Payments
                providerLayout.setVisibility(View.VISIBLE);
                itemAdapter = ArrayAdapter.createFromResource(this,
                        R.array.bills_array, android.R.layout.simple_spinner_item);
                break;
            case 4: // Funds Transfers
                am.saveMerchantID("TRANSFER");
                serviceType = "INTERNAL";
                nwscAreas.setVisibility(View.GONE);
                providerLayout.setVisibility(View.GONE);
                itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

                if (add.isChecked()){
                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.VISIBLE);
                    meter.setHint("Account Number");
                }else   if (viewBtn.isChecked()){
                    quest = (
                            "FORMID:O-GETTRANSFERBENEFICIARY:" +
                                    "BENFTYPE:" + serviceType + ":" +
                                    "BANKID:" + am.getBankID() + ":"
                    );
                    am.get_(this,quest,getString(R.string.fetchingBeneficiaries),"BEN_GBI");

                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.GONE);
                }else if (delete.isChecked()){
                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.GONE);
                }
                break;
            case 5: // School fees
                am.saveMerchantID("SCHOOLFEES");
                utilityID = "School Fees";
                merchant = "SCHOOLFEES";
                nwscAreas.setVisibility(View.GONE);
                providerLayout.setVisibility(View.GONE);
                itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);

                if (add.isChecked()){
                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.VISIBLE);
                    meter.setHint("Student Id");
                }else   if (viewBtn.isChecked()){

                    quest = (
                            "FORMID:O-GetUtilityAlias:" +
                                    "SERVICETYPE:" + serviceType + ":" +
                                    "SERVICEID:" + utilityID + ":"
                    );
                    am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " " + utilityID,"BEN_UTS");

                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.GONE);
                }else if (delete.isChecked()){
                    numChoice.setVisibility(View.GONE);
                    numChoice2.setVisibility(View.GONE);
                }

                break;
            case 0: // Nothing selected
                nwscAreas.setVisibility(View.GONE);
                providerLayout.setVisibility(View.GONE);
                itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
                break;
            default:
                itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        }

        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceProvider.setAdapter(itemAdapter);
    }

    private void add (String selectedItem){
        benName.setText("");
        addBen.setVisibility(View.GONE);
        merchantName = selectedItem;

        switch(selectedItem){
            case "MTN":
                am.saveMerchantID("MTNUGAIRTIME");
                numChoice.setVisibility(View.VISIBLE);
                numChoice2.setVisibility(View.GONE);
                serviceType = "Airtime";
                utilityID = "MTN";
                break;
            case "Airtel":
                am.saveMerchantID("AIRTELUG");
                numChoice.setVisibility(View.VISIBLE);
                numChoice2.setVisibility(View.GONE);
                serviceType = "Airtime";
                utilityID = "Airtel";
                break;
            case "MTN Money":
                am.saveMerchantID("007001017");
                numChoice.setVisibility(View.VISIBLE);
                numChoice2.setVisibility(View.GONE);
                serviceType = "MMONEY";
                utilityID = "MMONEYUGMTN";
                break;
            case "Airtel Money":
                am.saveMerchantID("007001016");
                numChoice.setVisibility(View.VISIBLE);
                numChoice2.setVisibility(View.GONE);
                serviceType = "MMONEY";
                utilityID = "MMONEYUGAIRTEL";
                break;
            case "DSTV":
                am.saveMerchantID("007001001");
                numChoice.setVisibility(View.GONE);
                numChoice2.setVisibility(View.VISIBLE);
                nwscAreas.setVisibility(View.GONE);
                meter.setHint("SmartCard Number");
                serviceType = "Utility";
                utilityID = "DSTV";
                break;
            case "GOTV":
                am.saveMerchantID("007001014");
                numChoice.setVisibility(View.GONE);
                numChoice2.setVisibility(View.VISIBLE);
                nwscAreas.setVisibility(View.GONE);
                meter.setHint("SmartCard Number");
                utilityID = "GOTV";
                break;
            case "StarTimes":
                am.saveMerchantID("007001015");
                numChoice.setVisibility(View.GONE);
                numChoice2.setVisibility(View.VISIBLE);
                nwscAreas.setVisibility(View.GONE);
                meter.setHint("SmartCard Number");
                utilityID = "STAR TIMES";
                break;
            case "National Water":
                am.saveMerchantID("007001003");
                utilityID = "National Water";

                String quest = ("FORMID:O-NWATERUGAREA:");
                am.get(this,quest,getString(R.string.fetchingAreas),"NWA");

                numChoice.setVisibility(View.GONE);
                numChoice2.setVisibility(View.VISIBLE);
                nwscAreas.setVisibility(View.VISIBLE);
                meter.setHint("Meter Number");
                break;
            case "Umeme Yaka":
                am.saveMerchantID("007001012");
                utilityID = "UMEME YAKA";
                numChoice.setVisibility(View.GONE);
                numChoice2.setVisibility(View.VISIBLE);
                nwscAreas.setVisibility(View.GONE);
                break;
            case "Umeme Post-Paid":
                am.saveMerchantID("007001002");
                utilityID = "UMEME Power";
                numChoice.setVisibility(View.GONE);
                numChoice2.setVisibility(View.VISIBLE);
                nwscAreas.setVisibility(View.GONE);
                meter.setHint("Meter Number");
                break;
        }
    }

    private void viewBens(String selectedItem){
        switch (selectedItem){
            case "MTN":
                serviceType = "Airtime";
                utilityID = "MTN";
                benTitle.setText("MTN BENEFICIARIES");
                benTitleDel.setText("MTN BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:" + serviceType + ":" +
                                "SERVICEID:" +  utilityID + ":"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries) + " " + getString(R.string.forWord) + " " + utilityID,"BEN_AIR");
                break;

            case "Airtel":
                serviceType = "Airtime";
                utilityID = "Airtel";
                benTitle.setText("AIRTEL BENEFICIARIES");
                benTitleDel.setText("AIRTEL BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:" + serviceType + ":" +
                                "SERVICEID:" +  utilityID + ":"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries) + " " + getString(R.string.forWord) + " " + utilityID,"BEN_AIR");
                break;

            case "MTN Money":
                benTitle.setText("MTN MONEY BENEFICIARIES");
                benTitleDel.setText("MTN MONEY BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:" + serviceType + ":" +
                                "SERVICEID:" + "MMONEYUGMTN" + ":"
                );
                am.get_(this,quest,getString(R.string.fetchingBeneficiaries),"BEN_MTN");
                break;
            case "Airtel Money":
                benTitle.setText("AIRTEL MONEY BENEFICIARIES");
                benTitleDel.setText("AIRTEL MONEY BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:" + serviceType + ":" +
                                "SERVICEID:" + "MMONEYUGAIRTEL" + ":"
                );
                am.get_(this,quest,getString(R.string.fetchingBeneficiaries),"BEN_MTN");
                break;
            case "DSTV":
                benTitle.setText("DSTV BENEFICIARIES");
                benTitleDel.setText("DSTV BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:Utility:" +
                                "SERVICEID:DSTV:"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " DSTV","BEN_UTS");
                break;
            case "GOTV":
                benTitle.setText("GOTV BENEFICIARIES");
                benTitleDel.setText("GOTV BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:Utility:" +
                                "SERVICEID:GOTV:"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " GOTV","BEN_UTS");
                break;
            case "StarTimes":
                benTitle.setText("STAR TIMES BENEFICIARIES");
                benTitleDel.setText("STAR TIMES BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:Utility:" +
                                "SERVICEID:STAR TIMES:"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " Star Times","BEN_UTS");
                break;
            case "National Water":
                benTitle.setText("NATIONAL WATER BENEFICIARIES");
                benTitleDel.setText("NATIONAL WATER BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:Utility:" +
                                "SERVICEID:National Water:"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " National Water","BEN_UTS");
                break;
            case "Umeme Yaka":
                benTitle.setText("UMEME YAKA BENEFICIARIES");
                benTitleDel.setText("UMEME YAKA BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:Utility:" +
                                "SERVICEID:UMEME YAKA:"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " Umeme Yaka","BEN_UTS");
                break;
            case "Umeme Post-Paid":
                benTitle.setText("UMEME POST-PAID BENEFICIARIES");
                benTitleDel.setText("UMEME POST-PAID BENEFICIARIES");
                quest = (
                        "FORMID:O-GetUtilityAlias:" +
                                "SERVICETYPE:Utility:" +
                                "SERVICEID:UMEME Power:"
                );
                am.get_(Beneficiaries.this,quest,getString(R.string.fetchingBeneficiaries)  + " " + getString(R.string.forWord) + " Umeme Post-Paid","BEN_UTS");
                break;
        }
    }

    public void validateMeters(){
        String merchant = am.getMerchantID();
//        Log.e("Merchant", merchant);

        switch (merchant){
            case "007001003":
                if (areaString.isEmpty()){
                    Toast.makeText(this, "Please select your area", Toast.LENGTH_SHORT).show();
                }else {
                    quest = (
                            "FORMID:B-:" +
                                    "MERCHANTID:" + am.getMerchantID() + ":" +
                                    "BANKACCOUNTID:" + am.getBankAccountID(1) + ":" +
                                    "ACCOUNTID:" + meter.getText().toString().trim() + ":" +
                                    "INFOFIELD1:" + areaString + ":" +
                                    "INFOFIELD2:" + "VALIDATION" + ":" +
                                    "INFOFIELD3:" + meter.getText().toString().trim() + ":" +
                                    "INFOFIELD9:" + am.getUserPhone() + ":" +
                                    "AMOUNT:" + "501" + ":" +
                                    "ACTION:GETNAME:"
                    );
                    am.get(this,quest,getString(R.string.validating),"VAL");
                }
                break;
            case "TRANSFER":
                quest = (
                        "FORMID:M-:" +
                                "MERCHANTID:VALIDATEACCOUNTBANK:" +
                                "ACCOUNTID:" + meter.getText().toString().trim() + ":" +
                                "ACTION:GETNAME:"
                );
                am.get(this,quest,getString(R.string.validating),"VAL");
                break;
            case "SCHOOLFEES":
                quest = (
                        "FORMID:B-:" +
                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                "BANKACCOUNTID:" + am.getBankAccountID(1) + ":" +
                                "ACCOUNTID:" +  meter.getText().toString().trim() + ":" +
                                "INFOFIELD1:" + meter.getText().toString().trim() + ":" +
                                "INFOFIELD2:" + am.getUserPhone()  + ":" +
                                "INFOFIELD3:" + am.getUserName() + ":" +
                                "INFOFIELD9:" + "VALIDATION" + ":" +
                                "ACTION:GETNAME:"

                            /*"FORMID:M-:" +
                                    "MERCHANTID:" + am.getMerchantID() + ":" +
                                    "BANKACCOUNTID:" + accSend + ":" +
                                    "ACCOUNTID:" +  studentId.getText().toString().trim() + ":" +
                                    "INFOFIELD1:" + "VALIDATION" + ":" +
                                    "INFOFIELD2:" + am.getUserPhone()  + ":" +
                                    "INFOFIELD3:" + am.getUserName() + ":" +
                                    "ACTION:GETNAME:"*/
                );
                am.get(this,quest,getString(R.string.validating),"VAL");
                break;
            default:
                quest = (
                        "FORMID:M-:" +
                                "MERCHANTID:" + am.getMerchantID() + ":" +
                                "ACCOUNTID:" + meter.getText().toString().trim() + ":" +
                                "INFOFIELD1:" + merchantName + ":" +
                                "INFOFIELD9:" + am.getUserPhone() + ":" +
                                "ACTION:GETNAME:"
                );
                am.get(this,quest,getString(R.string.validating),"VAL");
                break;
        }
    }

    public void validateMNO(){
        String merchant = am.getMerchantID();
        String sendPhoneStr = phoneNum.getText().toString();

        switch (merchant){
            case "MTNUGAIRTIME":
            case "007001017":
                am.saveSendPhone(sendPhoneStr);
                quest = (
                        "FORMID:M-:" +
                                "MERCHANTID:MMONEYUGMTN:" +
                                "ACCOUNTID:" + sendPhoneStr + ":" +
                                "BANKID:" + am.getBankID() + ":" +
                                "ACTION:GETNAME:"
                );
                //am.connectOldTwo(getString(R.string.validating),quest,this,"MTN");
                am.get(this, quest, getString(R.string.validating), "MTN");

                break;

            case "AIRTELUG":
            case "007001016":
                String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
                String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
                String strZipCodeRemovalPattern = "^256+(?!$)";
                String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "0");
//                    String strPattern = "^0+(?!$)";
//                    String mobilenumber = cleanMobile.replaceAll(strPattern, "");
                am.saveSendPhone(cleanMobile);
                quest = (
                        "FORMID:M-:" +
                                "MERCHANTID:HFBMMONEY:" +
                                "INFOFIELD1:VALIDATE:" +
                                "INFOFIELD2:AIRTEL:" +
                                "ACCOUNTID:" + cleanMobile + ":" +
                                "SERVICEACCOUNTID:" + cleanMobile + ":" +
                                "COUNTRY:" + am.getCountry() + ":" +
                                "BANKNAME:HFB:" +
                                "BANKID:" + am.getBankID() + ":" +
                                "ACTION:GETNAME:"
                );
                am.get(this, quest, getString(R.string.validating), "AIRTEL");
                break;
        }
    }

    public void addBen(){
        String merchant = am.getMerchantID();

        switch (merchant){
            case "MTNUGAIRTIME":
            case "AIRTELUG":
                quest = (
                        "FORMID:O-AddUtilityAlias:" +
                                "SERVICETYPE:" + serviceType + ":" +
                                "UTILITYID:" + utilityID + ":" +
                                "UTILITYACCOUNTID:" + am.getSendPhone() + ":" +
                                "UTILITYALIAS:" + benName.getText().toString().trim().replace(":","").replace("|","").replace("~","").replace(",","").replace(";","") + ":"
                );
                am.get_(this,quest,getString(R.string.processingReq),"BEN_ADD");
                break;
            case "007001017":
                                am.saveSendPhone(phoneNum.getText().toString().trim());
                                quest = (
                                        "FORMID:O-AddUtilityAlias:" +
                                                "SERVICETYPE:" + serviceType + ":" +
                                                "UTILITYID:" + "MMONEYUGMTN" + ":" +
                                                "UTILITYACCOUNTID:" + am.getSendPhone() + ":" +
                                                "UTILITYALIAS:" + benName.getText().toString().trim().replace(":","").replace("|","").replace("~","").replace(",","").replace(";","") + ":"
                                );
                                am.get_(this,quest,getString(R.string.processingReq),"BEN_ADD");
                break;
            case "007001016":
                                am.saveSendPhone(phoneNum.getText().toString().trim());
                                quest = (
                                        "FORMID:O-AddUtilityAlias:" +
                                                "SERVICETYPE:" + serviceType + ":" +
                                                "UTILITYID:" + "MMONEYUGAIRTEL" + ":" +
                                                "UTILITYACCOUNTID:" + am.getSendPhone() + ":" +
                                                "UTILITYALIAS:" + benName.getText().toString().trim().replace(":","").replace("|","").replace("~","").replace(",","").replace(";","") + ":"
                                );
                                am.get_(this,quest,getString(R.string.processingReq),"BEN_ADD");
                break;
            case "TRANSFER":
                quest = (
                        "FORMID:O-ADDTRANSFERBENEFICIARY:" +
                                "BENFTYPE:" + serviceType + ":" +
                                "BENFACCOUNTID:" + meter.getText().toString().trim() + ":" +
                                "BENFALIAS:" + benName.getText().toString().trim().replace(":","").replace("|","").replace("~","").replace(",","").replace(";","") + ":" +
                                "BENFBANK:" +  am.getBankID() + ":" +
                                "BENFBRANCH:" +  am.getBankID() + ":" +
                                "BANKID:" + am.getBankID() + ":"
                );
                am.get_(this,quest,getString(R.string.processingReq),"BEN_ADD");
                break;
            case "007001001":
            case "007001014":
            case "007001015":
            case "007001003":
            case "007001012":
            case "007001002":
            case "SCHOOLFEES":
                quest = (
                        "FORMID:O-AddUtilityAlias:" +
                                "UTILITYID:" + utilityID + ":" +
                                "UTILITYACCOUNTID:" + meter.getText().toString().trim() + ":" +
                                "UTILITYALIAS:" + benName.getText().toString().trim().replace(":","").replace("|","").replace("~","").replace(",","").replace(";","") + ":" +
                                "SERVICETYPE:Utility:"
                );
                am.get_(this,quest,getString(R.string.processingReq),"BEN_ADD");
                break;
        }
    }

    public void delBen(){
        quest = (
                "FORMID:O-DeleteUtilityAlias:" +
                        "SERVICETYPE:" + serviceType + ":" +
                        "UTILITYID:" + merchant + ":" +
                        "UTILITYACCOUNTID:" + benAcc + ":" +
                        "UTILITYALIAS:" + benefit.getSelectedItem().toString().trim() + ":"
        );
        am.get_(this,quest,getString(R.string.processingReq),"BEN_DEL");
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step){
            case "NWA":

                final List<String> areaNames = new ArrayList<>(), idList = new ArrayList<>();
                areaNames.add(getString(R.string.selectArea));
                String [] areaIDS = response.split(",");
                for (String anAreaID : areaIDS) {
                    String[] insideData = anAreaID.split("\\|");
                    String code = insideData[0], area = insideData[1];
                    idList.add(code);
                    areaNames.add(area);
                }
                ArrayAdapter<String> dataAdapterA = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, areaNames);
                dataAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areas.setAdapter(dataAdapterA);
                areas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                areas.setVisibility(View.VISIBLE);
                addBen.setVisibility(View.GONE);
                break;

            case "MTN":
                int startIndex = response.indexOf("NAME|") + "NAME|".length();
                int endIndex = response.indexOf("|STATUS");

                // Extract the substring between the indices
                String extractedString = response.substring(startIndex, endIndex);
                benName.setText(extractedString);
                addBen.setVisibility(View.VISIBLE);
                break;

            case "AIRTEL":
                benName.setText(response);
                addBen.setVisibility(View.VISIBLE);
                break;

            case "VAL":
                addBen.setVisibility(View.VISIBLE);
//                Log.e("RESPONSE", response);
                if(response.contains("|")){
                    String[] howLong = response.split("\\|");
                    String[] field_IDs = new String[howLong.length/2];
                    String[] field_Values = new String[howLong.length/2];
                    am.separate(response,"|",field_IDs,field_Values);
                    switch (am.getMerchantID()){
                        case "007001012":
                            String beneficiaryName = am.FindInArray(field_IDs, field_Values,"Name");
                            benName.setText(beneficiaryName);
                            break;
                        case "007001002":
                            benName.setText(response);
                            break;
                        case "007001001":
                            int strtIndex = response.indexOf("Name|") + "Name|".length();
                            int lastIndex = response.indexOf("|DueAmount");

                            // Extract the substring between the indices
                            String extrctdString = response.substring(strtIndex, lastIndex);
                            benName.setText(extrctdString);
                    }
                } else {
                    benName.setText(response);
                }
                break;
            case "BEN_ADD":
            case "BEN_DEL":
                reset();
                startActivity(new Intent(this, SuccessDialogPage.class).putExtra("message",response));
                break;
            case "BEN_AIR":
            case "BEN_MTN":
            case "BEN_UTS":
            case "BEN_GBI":

                if (viewBtn.isChecked()){
                    show(response);
                }else if (delete.isChecked()){
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
                    ArrayAdapter<String> benZAdapter = new ArrayAdapter<>(Beneficiaries.this,R.layout.spinner_dropdown_item, listNames);
                    benZAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    benefit.setAdapter(benZAdapter);
                    benefit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            benAcc = listAccOrPhones.get(position);
                            merchant = listMerchant.get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                    delBen.setVisibility(View.VISIBLE);
                }
                break;

            case "RESET":
                myDialog(this,getString(R.string.alert), response);
                break;
        }

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
        txtOk.setOnClickListener(v -> {
            mDialog.dismiss(); // Close the dialog
            recreate(); // Recreate the activity
        });
        mDialog.setCancelable(true);
        mDialog.show();
    }

    private void reset() {
        phoneNum.setText("");
        benName.setText("");
        add.setChecked(true);
        addBen.setVisibility(View.GONE);
        delBen.setVisibility(View.GONE);

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
                            phoneNum.setText(PhoneNumberUtils.stripSeparators(number));
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

    private void show (String benFact) {
        try {
            benDataSet.clear();
            if(benFact.contains("~")){
                String [] data = benFact.split("~");
                Collections.addAll(benDataSet, data);
            } else {
                String [] util = benFact.split(";");
                Collections.addAll(benDataSet, util);
            }
            AdapterBen AdapterBen = new AdapterBen(benDataSet);
            benRecords.setLayoutManager(new LinearLayoutManager(this));
            benRecords.setAdapter(AdapterBen);
        } catch (final Exception e) {
            benRecords.setVisibility(View.GONE);
            am.ToastMessageLong(this,getString(R.string.responseError));
        }
        benRecords.setVisibility(View.VISIBLE);
        viewBen.setVisibility(View.VISIBLE);
    }

    private class AdapterBen extends RecyclerView.Adapter<AdapterBen.ViewHolder>  {
        private final ArrayList<String> dataSet;

        @NonNull
        @Override
        public AdapterBen.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_ben, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterBen.ViewHolder holder, final int position) {
            try {
                String data = dataSet.get(position);
                String [] splits;
                if (data.contains("|")) {
                    splits = data.split("\\|");
                    holder.benAccView.setText(splits[0]);
                    holder.benNameView.setText(splits[1]);
                } else if(data.contains(",")) {
                    splits = data.split(",");
                    holder.benAccView.setText(splits[1]);
                    holder.benNameView.setText(splits[2]);
                }
            } catch (Exception e) {
                benRecords.setVisibility(View.GONE);
                am.ToastMessageLong(Beneficiaries.this,getString(R.string.responseError));
            }
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        AdapterBen(ArrayList<String> mDataSet) {
            dataSet = mDataSet;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView benAccView, benNameView;

            ViewHolder(View v) {
                super(v);
                benAccView = v.findViewById(R.id.benAccView);
                benNameView = v.findViewById(R.id.benNameView);
            }
        }
    }
}