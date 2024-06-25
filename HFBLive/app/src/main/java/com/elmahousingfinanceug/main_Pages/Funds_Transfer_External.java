package com.elmahousingfinanceug.main_Pages;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.elmahousingfinanceug.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug.recursiveClasses.VolleyResponse;

import java.util.ArrayList;
import java.util.List;

public class Funds_Transfer_External extends BaseAct implements ResponseListener, VolleyResponse {
    Spinner accountNumber,bankName,branchName;
    EditText ETAccount,ETAddress,ETBenName,ETBenAddress,ETAmount,ETMessage,ETPin;
    RadioGroup radioGroupType;
    RadioButton eft,rtgs;
    ProgressBar load;
    LinearLayout branchNameIn, BenInLay;
    String  bankCode, bSwift, bIFSC, branchCode, selectedBranchName="", selectedShortCode="", srcAcc ="",quest="";
    Boolean bankSet = false;
    private ArrayAdapter<String> bankAdapter, branchAdapter;
    private final ArrayList<String> listBanks = new ArrayList<>(), listBranches = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds_transfer_external);

        gToolBar(getString(R.string.eftNrtgs));

        accountNumber = findViewById(R.id.accountNumber);
        ETAddress = findViewById(R.id.ETAddress);
        ETAccount = findViewById(R.id.ETAccount);
        ETBenName = findViewById(R.id.ETBenName);
        ETBenAddress = findViewById(R.id.ETBenAddress);
        radioGroupType = findViewById(R.id.radioGroupType);
        eft = findViewById(R.id.eft);
        rtgs = findViewById(R.id.rtgs);
        bankName = findViewById(R.id.bankName);
        branchNameIn = findViewById(R.id.branchNameIn);
        branchName = findViewById(R.id.branchName);
        BenInLay = findViewById(R.id.BenInLay);
        load = findViewById(R.id.load);
        ETAmount = findViewById(R.id.ETAmount);
        ETMessage = findViewById(R.id.messaage);
        ETPin = findViewById(R.id.eTPin);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    srcAcc = "";
                } else {
                    srcAcc = am.getBankAccountID(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (eft.isChecked()) {
                    am.saveMerchantID("EFT");
                    BenInLay.setVisibility(View.GONE);
                } else if (rtgs.isChecked()) {
                    am.saveMerchantID("RTGS");
                    BenInLay.setVisibility(View.VISIBLE);
                }
            }
        });
        eft.setChecked(true);

        bankAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listBanks);
        bankAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bankName.setAdapter(bankAdapter);

        branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listBranches);
        branchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        branchName.setAdapter(branchAdapter);

        ETPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(after>1) am.myDialog(Funds_Transfer_External.this, getString(R.string.alert), getString(R.string.copyPaste));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count>1) ETPin.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onResume() {
        ETPin.setText("");
        if(!bankSet) {
            quest = (
                "FORMID:O-GetCentralizedEFTRTGSBanks:" +
                        "MODULENAME:" + am.getMerchantID() + ":" +
                        "BANKID:" + am.getBankID() + ":"
            );
            am.get_(this,quest,getString(R.string.fetchingBanks),"CBK");
        }
        super.onResume();
    }

    public void fT(View t) {
        if (t.getId() == R.id.sendFunds) {
            if (srcAcc.equals("")) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.selectAccDebited));
            } else if (ETAccount.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.enterBen));
            } else if (ETBenName.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.enterBenName));
            } else if (rtgs.isChecked() && ETBenAddress.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.enterBenAddress));
            } else if (ETAmount.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
            } else if (ETMessage.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.descReq));
            } else if (ETPin.getText().toString().trim().isEmpty()) {
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
                txtMessage.setText(String.format("%s %s %s %s %s %s %s %s %s %s %s %s.",
                        getText(R.string.doEFT),
                        am.getMerchantID(),
                        getText(R.string.transfer_worth),
                        am.Amount_Thousands(ETAmount.getText().toString().trim()),
                        getText(R.string.fromAccNo),
                        srcAcc,
                        getText(R.string.to),
                        bankName.getSelectedItem().toString().trim(),
                        getText(R.string.accountNum),
                        ETAccount.getText().toString().trim(),
                        getText(R.string.branch),
                        selectedBranchName));
                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quest = (
                                "FORMID:B-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "BANKACCOUNTID:" + srcAcc + ":" +
                                        "INFOFIELD1:" + selectedShortCode + ":" +
                                        "INFOFIELD2:" + bankCode + ":" +
                                        "INFOFIELD3:" + branchCode + ":" +
                                        "INFOFIELD4:" + ETBenName.getText().toString().trim()  + ":" +
                                        "INFOFIELD5:" + selectedBranchName + ":" +
                                        "INFOFIELD6:" +  ETBenAddress.getText().toString().trim()  + ":" +
                                        "TOACCOUNT:" + ETAccount.getText().toString().trim() + ":" +
                                        "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                        "MESSAGE:" + ETMessage.getText().toString().trim() + ":" +
                                        "TMPIN:" + ETPin.getText().toString().trim() + ":"
                        );
                        am.get_(Funds_Transfer_External.this,quest,getString(R.string.processingTrx),"TRX");
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
        }
    }


    private void utilBank(final String threadBank) {
        final List<String> listBankCodes = new ArrayList<>(), listBranchCodes = new ArrayList<>(), shortCodes = new ArrayList<>();
        listBanks.clear();
        listBranches.clear();
        String[] bankItems = threadBank.split("~");
        for (String aBankItem : bankItems) {
            String[] inside = aBankItem.split("\\|");
            listBankCodes.add(inside[0]);
            listBranchCodes.add(inside[1]);
            listBanks.add(inside[2]);
            listBranches.add(inside[3]);
            shortCodes.add(inside[4]);
        }
        bankName.post(new Runnable() {
            public void run() {
                try {
                    bankAdapter.notifyDataSetChanged();
                    bankCode = listBankCodes.get(0);
                    branchCode= listBranchCodes.get(0);
                    selectedBranchName = listBranches.get(0);
                    selectedShortCode = shortCodes.get(0);
                    bankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            branchNameIn.setVisibility(View.GONE);
                            bankCode = listBankCodes.get(position);
                            branchCode= listBranchCodes.get(position);
                            selectedBranchName = listBranches.get(position);
                            selectedShortCode = shortCodes.get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    bankSet = true;
                    load.setVisibility(View.GONE);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
            case "CBK":
                utilBank(response);
                break;
            case "BRH":
                final List<String> listBranchCodes = new ArrayList<>(), swiftlist = new ArrayList<>(), iFSClist = new ArrayList<>();
                listBranches.clear();
                String[] branchItems = response.split(",");
                for (String aBranch : branchItems) {
                    String[] inside = aBranch.split("\\|");
                    listBranchCodes.add(inside[0]);
                    listBranches.add(inside[1]);
                    swiftlist.add(inside[2]);
                    iFSClist.add(inside[3]);
                }
                branchName.post(new Runnable() {
                    public void run() {
                        try {
                            branchAdapter.notifyDataSetChanged();
                            branchCode = listBranchCodes.get(0);
                            bSwift = swiftlist.get(0);
                            bIFSC = iFSClist.get(0);
                            branchName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    branchCode = listBranchCodes.get(position);
                                    bSwift = swiftlist.get(position);
                                    bIFSC = iFSClist.get(position);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}
                            });
                            branchNameIn.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case "TRX":
                finish();
                startActivity(new Intent(getApplicationContext(), SuccessDialogPage.class).putExtra("message", response));
                break;
        }
    }
}
