package com.elmahousingfinanceug_test.main_Pages;

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

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.AdapterKeyValue;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import java.text.DecimalFormat;

public class School_Fees extends BaseAct implements ResponseListener, VolleyResponse {
    Spinner debitAcc;
    EditText studentId,studentAmount,pin;
    LinearLayout validateLayout;
    TextView validate,schoolName;
    RecyclerView valRecycler;
    String accSend="", studentName="", schoolCode="", paymentCode="", outstandingAmount="",
            partialpayment="", returnMessage="",processTimeStamp="", quest="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_fees);

        gToolBar(getString(R.string.schoolFees));

        debitAcc = findViewById(R.id.debitAccountNumber);
        studentId = findViewById(R.id.studentId);
        validate = findViewById(R.id.validate);
        validateLayout = findViewById(R.id.validateLayout);
        valRecycler = findViewById(R.id.valRecycler);
        schoolName = findViewById(R.id.schoolName);
        studentAmount = findViewById(R.id.studentAmount);
        pin = findViewById(R.id.pin);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        debitAcc.setAdapter(dataAdapter);
        debitAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        studentAmount.addTextChangedListener(new TextWatcher() {
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
                    am.myDialog(School_Fees.this, getString(R.string.alert), getString(R.string.insufficient_funds));
                    pin.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(School_Fees.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) pin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        valRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void clicks(View c) {
        switch (c.getId()){
            case R.id.validate:
                if(accSend.equals("")) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if(studentId.getText().length()<5) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterStudentId));
                } else {
                    //1000027362  1000027366
                    quest = (
                            "FORMID:B-:" +
                                    "MERCHANTID:" + am.getMerchantID() + ":" +
                                    "BANKACCOUNTID:" + accSend + ":" +
                                    "ACCOUNTID:" +  studentId.getText().toString().trim() + ":" +
                                    "INFOFIELD1:" + studentId.getText().toString().trim() + ":" +
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
                }
                break;
            case R.id.backIn:
                validate.setVisibility(View.VISIBLE);
                validateLayout.setVisibility(View.GONE);
                break;
            case R.id.submit:
                if(accSend.equals("")) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
                } else if (studentId.getText().toString().trim().isEmpty()) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterStudentId));
                } else if (studentAmount.getText().toString().trim().isEmpty()) {
                    am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (pin.getText().length() < 4) {
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
                    txtMessage.setText(String.format("%s %s %s, %s %s %s %s %s %s",
                            getString(R.string.make),
                            getString(R.string.schoolFees),
                            getText(R.string.payFor),
                            studentName,
                            studentId.getText().toString().trim(),
                            getString(R.string.withAmount),
                            am.Amount_Thousands(studentAmount.getText().toString().trim()),
                            getText(R.string.fromAccNo),
                            accSend)
                    );
                    txtNo.setOnClickListener(v -> gDialog.cancel());
                    txtOk.setOnClickListener(v -> {
                        quest = (
                                "FORMID:B-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "BANKACCOUNTID:" + accSend + ":" +
                                        "INFOFIELD1:" + studentId.getText().toString().trim() + ":" +
                                        "INFOFIELD2:" + paymentCode + ":" +
                                        "INFOFIELD3:" + schoolCode + ":" +
                                        "INFOFIELD4:" + outstandingAmount + ":" +
                                        "INFOFIELD5:" + partialpayment + ":" +
                                        "INFOFIELD6:" + returnMessage + ":" +
                                        "INFOFIELD7:" + processTimeStamp + ":" +
                                        "INFOFIELD8:" + studentAmount.getText().toString().trim() + ":" +
                                        "INFOFIELD9:" + "PAYMENT" + ":" +
                                        "ACCOUNTID:" + studentId.getText().toString().trim() + ":" +
                                        "AMOUNT:" + studentAmount.getText().toString().trim() + ":" +
                                        "TMPIN:" + pin.getText().toString().trim() + ":" +
                                        "ACTION:GETNAME:"
                                /*"FORMID:M-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "BANKACCOUNTID:" + accSend + ":" +
                                        "INFOFILED1:" + paymentCode + ":" +
                                        "INFOFIELD2:" + schoolCode + ":" +
                                        "INFOFILED3:" + outstandingAmount + ":" +
                                        "INFOFIELD4:" + studentId.getText().toString().trim() + ":" +
                                        "INFOFILED5:" + partialpayment + ":" +
                                        "INFOFIELD6:" + returnMessage + ":" +
                                        "INFOFIELD7:" + processTimeStamp + ":" +
                                        "ACCOUNTID:" + studentId.getText().toString().trim() + ":" +
                                        "AMOUNT:" + studentAmount.getText().toString().trim() + ":" +
                                        "TMPIN:" + pin.getText().toString().trim() + ":" +
                                        "ACTION:GETNAME:"*/
                        );
                        am.get(School_Fees.this,quest,getString(R.string.processingTrx),"TRX");
                        gDialog.cancel();
                    });
                    gDialog.setOnCancelListener(dialog -> {
                        pin.setText("");
                        dialog.dismiss();
                    });
                    gDialog.show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        pin.setText("");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_menu) {
            finish();
            startActivity(new Intent(getApplicationContext(), Main_Menu.class));
            return true;
        } else if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(), Contact_Us.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step){
            case "VAL":
                // FIRST NAME|Judith|
                // MIDDLE NAME||
                // LAST NAME|Madnhawun|
                // RETURN MESSAGE|Judith Madnhawun|
                // REG NO||
                // SCHOOL CODE|209|
                // SCHOOL NAME|BLESSED FUTURE PRIMARY SCHOOL|
                // STUDENT CLASS|P1 - PRIMARY ONE|
                // DATE OF BIRTH|2013-03-09|
                // OUTSTANDING AMOUNT|0|
                // PAYMENT CODE|1000027362|
                // PARTIAL PAYMENT|false|
                // PROCESS TIMESTAMP|20211129162418
                String [] howLong =response.split("\\|");
                String [] field_IDs =new String[howLong.length/2];
                String [] field_Values =new String[howLong.length/2];
                am.separate(response,"|",field_IDs ,field_Values);
                paymentCode = am.FindInArray(field_IDs, field_Values,"PAYMENT CODE");
                schoolCode = am.FindInArray(field_IDs, field_Values,"SCHOOL CODE");
                studentName = am.FindInArray(field_IDs, field_Values,"FIRST NAME").concat(" ").concat(am.FindInArray(field_IDs, field_Values,"LAST NAME"));
                outstandingAmount = am.FindInArray(field_IDs, field_Values,"OUTSTANDING AMOUNT");
                partialpayment = am.FindInArray(field_IDs, field_Values,"PARTIAL PAYMENT");
                returnMessage = am.FindInArray(field_IDs, field_Values,"RETURN MESSAGE");
                processTimeStamp = am.FindInArray(field_IDs, field_Values,"PROCESS TIMESTAMP");
                valRecycler.setAdapter(new AdapterKeyValue(field_IDs,field_Values));
                validate.setVisibility(View.GONE);
                validateLayout.setVisibility(View.VISIBLE);
                break;
            case "TRX":
                am.saveDoneTrx(true);
                finish();
                startActivity(new Intent(getApplicationContext(),SuccessDialogPage.class).putExtra("message", response));
                break;
        }
    }
}
