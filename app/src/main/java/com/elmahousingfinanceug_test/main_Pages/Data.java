package com.elmahousingfinanceug_test.main_Pages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.AdapterKeyValue;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Data extends BaseAct implements ResponseListener, VolleyResponse {
    EditText ETOtherNumber, ETAmount, eTPin;
    Spinner accountNumber, serviceProvider, selectCategory, selectBundle;
    RadioGroup radioGroup;
    RadioButton RBTNMyNumber, RBTNOtherNumber;
    TextView myNumber, validateMTN, back, airtelResponse, validate, send;
    LinearLayout mobileNos, othNumLayout, after, validateLayout, airtelLayout;
    ImageView contactsVw;
    RecyclerView valRecycler;
    private String provider="", categoryIDString="", bundleIDString="", bundleAmountString="";
    String accSend = "", phoneReceive = "", sendPhoneStr = "", quest = "";
    private ArrayList<String> cellCategotyIDs = new ArrayList<>(), cellcategotyName = new ArrayList<>(), mtnCategotyIDs = new ArrayList<>(),
            mtncategotyName = new ArrayList<>(), bundleName = new ArrayList<>(), bundleID = new ArrayList<>(), bundleAmount = new ArrayList<>();
    private ArrayAdapter<String> bundleAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        gToolBar(getString(R.string.data));

        ETAmount = findViewById(R.id.ETAmount);
        ETOtherNumber = findViewById(R.id.ETOtherNumber);
        eTPin = findViewById(R.id.eTPin);
        radioGroup = findViewById(R.id.radioGroup);
        RBTNMyNumber = findViewById(R.id.RBTNMyNumber);
        RBTNOtherNumber = findViewById(R.id.RBTNOtherNumber);
        radioGroup = findViewById(R.id.radioGroup);
        myNumber = findViewById(R.id.myNumber);
        myNumber.setText(am.getUserPhone());
        serviceProvider = findViewById(R.id.serviceProvider);
        accountNumber = findViewById(R.id.accountNumber);
        mobileNos = findViewById(R.id.mobileNos);
        othNumLayout = findViewById(R.id.othNumLyt);
        contactsVw = findViewById(R.id.contacts);
        validateLayout = findViewById(R.id.validateLayout);
        valRecycler = findViewById(R.id.valRecycler);
        airtelResponse = findViewById(R.id.airtelResponse);
        airtelLayout = findViewById(R.id.airtelLayout);
        after = findViewById(R.id.after);
        selectCategory = findViewById(R.id.selectCategory);
        selectBundle = findViewById(R.id.selectBundle);
        back = findViewById(R.id.back);
        validateMTN = findViewById(R.id.validateMTN);
        send = findViewById(R.id.send);

//        mobileNos.setVisibility(View.GONE);
        after.setVisibility(View.GONE);

        ArrayAdapter<String> dataAdapterAcc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapterAcc.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapterAcc);
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
            public void onNothingSelected(AdapterView<?> parent) {
                accSend = "";
            }
        });

        final List<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.selectOne));
        arrayList.add("MTN");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        serviceProvider.setAdapter(dataAdapter);
        am.saveMerchantID("MTNDB");

        serviceProvider.setSelection(1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (RBTNMyNumber.isChecked()) {
                    othNumLayout.setVisibility(View.GONE);
                    myNumber.setVisibility(View.VISIBLE);
                    am.animate_View(myNumber);
                } else if (RBTNOtherNumber.isChecked()) {
                    othNumLayout.setVisibility(View.VISIBLE);
                    myNumber.setVisibility(View.GONE);
                    am.animate_View(contactsVw);
                }
            }
        });
        RBTNMyNumber.setChecked(true);


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
                if (!inputedAmount.equals("") && (Double.parseDouble(inputedAmount)) >= balance) {
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.insufficient_funds));
                    eTPin.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eTPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after > 1)
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.copyPaste));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 1) eTPin.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        valRecycler.setLayoutManager(new LinearLayoutManager(this));

        bundleAdapt = new ArrayAdapter<>(this,R.layout.spinner_dropdown_item, bundleName);
        getStaticData();


        validateMTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RBTNOtherNumber.isChecked() && ETOtherNumber.getText().length() < 4) {
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
                    ETOtherNumber.setError(getString(R.string.enterValidPhoneAcc));
                } else {
                    if (RBTNMyNumber.isChecked()) {
                        sendPhoneStr = am.getUserPhone();
                    } else {
                        am.saveSendPhone(ETOtherNumber.getText().toString().trim());
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
                    am.get(Data.this, quest, getString(R.string.validating), "MTN2");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eTPin.setText("");
                validateLayout.setVisibility(View.GONE);
                after.setVisibility(View.GONE);
                airtelLayout.setVisibility(View.GONE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accSend.equals("")) {
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (am.getMerchantID().equals("")) {
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.slctPrvdr));
                } else if (RBTNOtherNumber.isChecked() && ETOtherNumber.getText().toString().trim().length() < 4) {
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.invalidMobileNo));
//                } else if (ETAmount.getText().toString().trim().isEmpty()) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (eTPin.getText().toString().trim().length() < 4) {
                    am.myDialog(Data.this, getString(R.string.alert), getString(R.string.enterValidPin));
                } else {
                    if (RBTNMyNumber.isChecked()) {
                        phoneReceive = am.getUserPhone();
                    } else {
                        am.saveSendPhone(ETOtherNumber.getText().toString().trim());
                        phoneReceive = am.getSendPhone();
                    }
                    getAmount();
                }
            }
        });
    }



//    public void air(View a) {
//        switch (a.getId()) {
//            case R.id.validateMTN:
//                if (RBTNOtherNumber.isChecked() && ETOtherNumber.getText().length() < 4) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
//                    ETOtherNumber.setError(getString(R.string.enterValidPhoneAcc));
//                } else {
//                    if (RBTNMyNumber.isChecked()) {
//                        sendPhoneStr = am.getUserPhone();
//                    } else {
//                        am.saveSendPhone(ETOtherNumber.getText().toString().trim());
//                        sendPhoneStr = am.getSendPhone();
//                    }
//                    quest = (
//                            "FORMID:M-:" +
//                                    "MERCHANTID:MMONEYUGMTN:" +
//                                    "ACCOUNTID:" + sendPhoneStr + ":" +
//                                    "BANKID:" + am.getBankID() + ":" +
//                                    "ACTION:GETNAME:"
//                    );
//                    //am.connectOldTwo(getString(R.string.validating),quest,this,"MTN");
//                    am.get(this, quest, getString(R.string.validating), "MTN2");
//                }
//                break;
//
//            case R.id.validateAirtel:
//                if (RBTNOtherNumber.isChecked() && ETOtherNumber.getText().length() < 4) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPhoneAcc));
//                    ETOtherNumber.setError(getString(R.string.enterValidPhoneAcc));
//                } else {
//                    if (RBTNMyNumber.isChecked()) {
//                        sendPhoneStr = am.getUserPhone();
//                    } else {
//                        am.saveSendPhone(ETOtherNumber.getText().toString().trim());
//                        sendPhoneStr = am.getSendPhone();
//                    }
//                    String noSpaceMobile = sendPhoneStr.replaceAll("\\s+", "");
//                    String noSpecialXters = noSpaceMobile.replaceAll("[^a-zA-Z0-9]", " ");
//                    String strZipCodeRemovalPattern = "^256+(?!$)";
//                    String cleanMobile = noSpecialXters.replaceAll(strZipCodeRemovalPattern, "");
//                    String strPattern = "^0+(?!$)";
//                    String mobilenumber = cleanMobile.replaceAll(strPattern, "");
//
//
//                    quest = (
//                            "FORMID:M-:" +
//                                    "MERCHANTID:HFBAIRTELKYC:" +
//                                    "ACCOUNTID:" + mobilenumber + ":" +
//                                    "SERVICEACCOUNTID:" + mobilenumber + ":" +
//                                    "COUNTRY:" + am.getCountry() + ":" +
//                                    "BANKNAME:HFB:" +
//                                    "BANKID:" + am.getBankID() + ":" +
//                                    "ACTION:GETNAME:"
//                    );
//                    am.get(this, quest, getString(R.string.validating), "AIRTEL");
//
//
//                    //am.connectOldTwo(getString(R.string.validating),quest,this,"MTN");
//                }
//                break;
//            case R.id.back:
//                eTPin.setText("");
//                validateLayout.setVisibility(View.GONE);
//                after.setVisibility(View.GONE);
//                airtelLayout.setVisibility(View.GONE);
////                if (serviceProvider.getSelectedItemPosition() == 1) {
////                    validateMTN.setVisibility(View.VISIBLE);
////                    validateAirtel.setVisibility(View.GONE);
////                } else {
////                    validateAirtel.setVisibility(View.VISIBLE);
////                    validateMTN.setVisibility(View.GONE);
////                }
//
//                break;
//            case R.id.send:
//                if (accSend.equals("")) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
//                } else if (am.getMerchantID().equals("")) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.slctPrvdr));
//                } else if (RBTNOtherNumber.isChecked() && ETOtherNumber.getText().toString().trim().length() < 4) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.invalidMobileNo));
////                } else if (ETAmount.getText().toString().trim().isEmpty()) {
////                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
//                } else if (eTPin.getText().toString().trim().length() < 4) {
//                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPin));
//                } else {
//                    if (RBTNMyNumber.isChecked()) {
//                        phoneReceive = am.getUserPhone();
//                    } else {
//                        am.saveSendPhone(ETOtherNumber.getText().toString().trim());
//                        phoneReceive = am.getSendPhone();
//                    }
//                    getAmount();
//                }
//                break;
//        }
//    }

    public void getAmount(){
        quest = (
                "FORMID:M-:" +
                        "MERCHANTID:" + "MTNDB:" +
                        "INFOFIELD1:" + bundleIDString+ ":" +
                        "INFOFIELD9:" + phoneReceive  + ":" +
                        "ACCOUNTID:" + phoneReceive  + ":" +
                        "BANKID:" + am.getBankID() + ":" +
                        "ACTION:GETNAME:"
        );
        am.get_(this, quest,getString(R.string.loading),"AMT");
    }

    public void getStaticData(){

        try {
            JSONObject staticStuff = new JSONObject((am.getStaticData()));
            JSONArray MTNDataFrequency = staticStuff.getJSONArray("MTNDataFrequency");
            for (int i = 0; i < MTNDataFrequency.length(); i++) {
                JSONObject dataFreak = MTNDataFrequency.getJSONObject(i);
                mtnCategotyIDs.add(dataFreak.getString("ID"));
                mtncategotyName.add(dataFreak.getString("Description"));

//                Log.e("CatID", mtnCategotyIDs.toString());
//                Log.e("CatName", mtncategotyName.toString());
//                Log.e("TEst", "TEST");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mtncategotyName);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        selectCategory.setAdapter(dataAdapter);
        selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bundleID.clear();
                bundleName.clear();
                bundleAmount.clear();
                bundleAdapt.notifyDataSetChanged();

                quest = (
                        "FORMID:O-DataBundleConfig:" +
                                "PROVIDER:" + "MTN:" +
                                "CATEGORY:" + mtnCategotyIDs.get(position) + ":"
                );
                am.get_(Data.this, quest,getString(R.string.fetchBundles),"DBC");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        eTPin.setText("");
        super.onResume();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor c = null;
                    try {
                        c = getContentResolver().query(uri, new String[]{
                                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                                        ContactsContract.CommonDataKinds.Phone.TYPE},
                                null, null, null);
                        if (c != null && c.moveToFirst()) {
                            String number = c.getString(0).replace("+", "");
                            int type = c.getInt(1);
                            ETOtherNumber.setText(PhoneNumberUtils.stripSeparators(number));
                        }
                    } finally {
                        if (c != null) c.close();
                    }
                } else {
                    am.ToastMessage(getApplicationContext(), getString(R.string.plstryAgn));
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
//        Log.e("Response11", response);
//
//        response = response.replace("STATUS:OK:DATA:","");
//
//        Log.e("Response11", response);
//        JSONObject staticStuff = null;
//        try {
////            staticStuff = new JSONObject(am.getStaticData());
//            staticStuff = new JSONObject(response);
//
//            JSONArray MTNDataFrequency = staticStuff.getJSONArray("MTNDataFrequency");
//            Log.e("MTN", MTNDataFrequency.toString());
//            for (int i = 0; i < MTNDataFrequency.length(); i++) {
//                JSONObject dataFreak = MTNDataFrequency.getJSONObject(i);
//                mtnCategotyIDs.add(dataFreak.getString("ID"));
//                mtncategotyName.add(dataFreak.getString("Description"));
//
//                Log.e("CatID", mtnCategotyIDs.toString());
//                Log.e("CatName", mtncategotyName.toString());
//                Log.e("TEst", "TEST");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        switch (step) {
            case "MTN":
                after.setVisibility(View.VISIBLE);
                eTPin.requestFocus();
//                TODO: Switch from MTN2 to MTN for live
                String[] howLong = response.split("\\|");
                String[] field_IDs = new String[howLong.length / 2];
                String[] field_Values = new String[howLong.length / 2];
                am.separate(response, "|", field_IDs, field_Values);
                valRecycler.setVisibility(View.VISIBLE);
                valRecycler.setAdapter(new AdapterKeyValue(field_IDs, field_Values));
                airtelLayout.setVisibility(View.GONE);
                validateMTN.setVisibility(View.GONE);
                validateLayout.setVisibility(View.VISIBLE);
                after.setVisibility(View.VISIBLE);
                airtelLayout.setVisibility(View.GONE);
                break;
            case "MTN2":
            case "AIRTEL":
                eTPin.requestFocus();
                after.setVisibility(View.VISIBLE);
//                validateAirtel.setVisibility(View.GONE);
                validateLayout.setVisibility(View.VISIBLE);
                airtelLayout.setVisibility(View.VISIBLE);
                airtelResponse.setText(response);
                after.setVisibility(View.VISIBLE);
                valRecycler.setVisibility(View.GONE);
                break;
            case "TRX":
                am.saveDoneTrx(true);
                finish();
                startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
                break;
            case "DBC":
                //STATUS:000:DATA:Daily|TEST_data_111|1GB for daily - 500 UGX|500~Daily|RACT_UG_Data_63|15MB for daily - 250 UGX|250.0~Daily|RACT_UG_Data_200|40MB for daily - 500 UGX|500.0~Daily|RACT_UG_Data_201|100MB for daily - 1,000 UGX|1000.0~Daily|RACT_UG_Data_202|300MB for daily - 2000 UGX|2000.0~Daily|RACT_UG_Data_67|1GB for daily - 5,000 UGX|5000.0~
                //STATUS:000:DATA:Daily|11day25MB|Daily, 25 MB, 250 UGX|250.0~Daily|11day50MB|Daily, 50 MB, 500 UGX|500.0~Daily|11day150MB|Daily, 150 MB, 1000 UGX|1000.0~Daily|11day600MB|Daily, 600 MB, 3000 UGX|3000.0~Daily|11day1200MB|Daily, 1.2 GB, 5000 UGX|5000.0~Daily|11day4000MB|Daily, 4 GB, 20000 UGX|20000.0~Daily|11day350MB|Daily, 350 MB, 2000 UGX|2000.0~
                String[] types = response.split("~");
                for (String aType : types){
                    String[] insideType = aType.split("\\|");
                    bundleID.add(insideType[1]);
                    bundleName.add(insideType[2]);
                    bundleAmount.add(insideType[3]);
                }
                bundleAdapt.setDropDownViewResource(R.layout.spinner_dropdown_item);
                selectBundle.setAdapter(bundleAdapt);
                selectBundle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        bundleIDString = bundleID.get(position);
                        bundleAmountString = bundleAmount.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                break;

            case "AMT":
//                Log.e("AMOUNT", response);

                String [] Length = response.split("\\|");
                String [] fieldIDs =new String[Length.length/2];
                String [] fieldValues =new String[Length.length/2];
                am.separate(response,"|",fieldIDs ,fieldValues);
                bundleAmountString = am.FindInArray(fieldIDs, fieldValues,"AMOUNT");

                gDialog = new Dialog(this);
                //noinspection ConstantConditions
                gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                gDialog.setContentView(R.layout.dialog_confirm);
                final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
                final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
                final TextView txtNo = gDialog.findViewById(R.id.noBTN);
                txtMessage.setText(String.format("%s %s %s %s %s %s.",
                        getText(R.string.purchaseTopup),
                        serviceProvider.getSelectedItem().toString().trim(),
                        getString(R.string.airtimeworthUGX),
                        am.Amount_Thousands(bundleAmountString),
                        getText(R.string.forNumber),
                        phoneReceive));
                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quest = (
                                "FORMID:M-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "BANKACCOUNTID:" + accSend + ":" +
                                        "ACCOUNTID:" + phoneReceive + ":" +
                                        "AMOUNT:" + bundleAmountString + ":" +
                                        "TMPIN:" + eTPin.getText().toString().trim() + ":" +
                                        "ACTION:PAYBILL:"
                        );
                        //am.connectOldTwo(getString(R.string.processingTrx),quest,Airtime_Services.this,"TRX");
                        am.get(Data.this, quest, getString(R.string.processingTrx), "TRX");
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
                        eTPin.setText("");
                        dialog.dismiss();
                    }
                });
                gDialog.show();
                break;
        }
    }

}