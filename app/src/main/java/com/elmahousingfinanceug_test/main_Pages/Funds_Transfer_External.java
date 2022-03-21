package com.elmahousingfinanceug_test.main_Pages;

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

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

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
                    /*"FORMID:O-GetCommercialBankIDAndName-" +
                            "MODULENAME-" + am.getMerchantID() + "-" +
                            "BANKID-" + am.getBankID() + ":"*/
            );
            //am.connectOldTwo(getString(R.string.fetchingBanks), quest,this, "CBK");
            am.get(this,quest,getString(R.string.fetchingBanks),"CBK");
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
            }  else if (ETAmount.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidAmount));
            } else if (ETMessage.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.descReq));
            } else if (ETPin.getText().toString().trim().isEmpty()) {
                am.myDialog(this, getString(R.string.alert), getString(R.string.enterValidPin));
            } else {
                /*if (ETAddress.getText().toString().trim().isEmpty()) {
                    ETAddress.setText(R.string.uganda);
                }*/
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
                        // TODO: 11/13/2020
                        //<RequestMessage><Header><MTI>9200</MTI>
                        //<PC>800030</PC><TDT>20170330055308</TDT>
                        //<SSIC>CBX</SSIC></Header><Payload><PC>800030</PC>
                        //<USERID>ELMAUSR</USERID><UserOption>A</UserOption>
                        //<BranchCode>1</BranchCode>
                        //<EntryDate>14-12-2016</EntryDate>
                        //<BatchDate>14-12-2016</BatchDate>
                        //<ServiceCode>9841</ServiceCode>
                        //<OrderIFSC>HFINUGKAXXX</OrderIFSC>
                        //<OrderName>HOUSING FINANCE BANK LTD</OrderName>
                        //<OrderAdd1>KAMPALA</OrderAdd1>
                        //<ValueDate>14-12-2016</ValueDate>
                        //<RemitMode>1</RemitMode>
                        //<CustAccountnumber>0100560494</CustAccountnumber>
                        //<CustAccountType>CORP1</CustAccountType>
                        //<PaymtCurr>UGX</PaymtCurr>
                        //<PaymtAmt>3710</PaymtAmt>
                        //<CustName>BHAS LIMITED</CustName>
                        //<CustAdd1>P.O BOX 4143 KAMP</CustAdd1>
                        //<ChequeNum></ChequeNum><PaymtOption>62</PaymtOption>
                        //<BenfIFSC>UGBAUGKAXXX</BenfIFSC>
                        //<EftType>2</EftType><BenBkCode>1</BenBkCode>
                        //<BenBrnCode>1</BenBrnCode>
                        //<BenfAcnum>0100482279</BenfAcnum>
                        //<BenfAcType>10</BenfAcType>
                        //<BenfName>COMMISSIONER FOR DOMESTIC REVENUE</BenfName>
                        //<BenfAdd1>UG</BenfAdd1><SendRecvInfo>/URA/</SendRecvInfo>
                        //<Remarks>paymnt transfer</Remarks>
                        //<ExternalRefNo>TR300317175240</ExternalRefNo>
                        //<Cross_con_rate>3710</Cross_con_rate>
                        //<TfrCurr>USD</TfrCurr><TfrAmt>1</TfrAmt>
                        //<PaymentDetail>MM</PaymentDetail>
                        //</Payload></RequestMessage>

                        //InfoField1
                        //Data to pass :
                        //InfoField1
                        //CustAccountnumber|""|PaymtAmt|""|PaymtCurr|""|CustName|""|CustAdd1|""|BenfIFSC|""|BenBkCode|""|BenBrnCode|""|BenfAcnum|""|BenfName|""| BenfAdd1|""| ExternalRefNo|""| Cross_con_rate|""| TfrCurr|""| TfrAmt|""| PaymentDetail|""| CustAccountType|""|
                        //CustAccountnumber|""|PaymtAmt|""|PaymtCurr|""|CustName|""|CustAdd1|""|BenfIFSC|""|BenBkCode|""|BenBrnCode|""|BenfAcnum|""|BenfName|""| BenfAdd1|""| ExternalRefNo|""| Cross_con_rate|""| TfrCurr|""| TfrAmt|""| PaymentDetail|""| CustAccountType|""|

                        //Account: 0100152002
                        //Bank: CITI Bank
                        //Beneficiary: UETCL
                        quest = (
                                "FORMID:B-:" +
                                        "MERCHANTID:" + am.getMerchantID() + ":" +
                                        "BANKACCOUNTID:" + srcAcc + ":" +
                                        /*"INFOFIELD2:" + "CustAccountnumber|" + srcAcc +
                                                        "|PaymtAmt|" + ETAmount.getText().toString().trim() +
                                                        "|PaymtCurr|UGX" +
                                                        "|CustName|" + am.getUserName() +
                                                        "|CustAdd1|" + ""*//*ETAddress.getText().toString().trim()*//* +
                                                        "|BenfIFSC|" + bSwift +
                                                        "|BenBkCode|" + bankCode +
                                                        "|BenBrnCode|" + branchCode +
                                                        "|BenfAcnum|" + ETAccount.getText().toString().trim() +
                                                        "|BenfName|" + ETBenName.getText().toString().trim() +
                                                        "|BenfAdd1|" + ETBenAddress.getText().toString().trim() +
                                                        "|ExternalRefNo|"+
                                                        "|Cross_con_rate|" + "1" +
                                                        "|TfrCurr|UGX" +
                                                        "|TfrAmt|"+ ETAmount.getText().toString().trim() +
                                                        "|PaymentDetail|" + ETMessage.getText().toString().trim() +
                                                        "|CustAccountType|" + ":" +*/
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
                        //am.connectOldTwo(getString(R.string.processingTrx), quest, Funds_Transfer_External.this, "TRX");
                        am.get(Funds_Transfer_External.this,quest,getString(R.string.processingTrx),"TRX");
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
        //BankID|BranchID|Bank Name|BranchName|Short Code
        // 31|01|ABC Capital Bank Limited|Colline House|31014700~
        // 01|20|ABSA Bank Uganda Limited|Hannington Road|01204700~
        // 13|01|Bank of Africa Uganda Limited|Jinja Road|13014700~
        // 02|01|Bank of Baroda Uganda Limited|Kampala Road|02014700~
        // 34|01|Bank of India Uganda Limited|Kampala Main|34014700~99|01|Bank Of Uganda|Kampala Road|99014700~63|01|BRAC BANK UGANDA LTD|Kampala|63014700~18|01|Cairo Bank Uganda Limited|Kampala Road|18014700~16|30|Centenary Bank Limited|Entebbe Road|16304700~22|01|CITI bank Uganda Limited|Head Office|22014700~05|01|DFCU Bank Limited|Impala Avenue|05014700~19|01|Diamond Trust Bank Uganda Limited|Kampala Road|19014700~29|01|Ecobank Uganda Limited|Parliament Avenue|29014700~30|01|Equity Bank Uganda Limited|Church House|30014700~32|01|Exim Bank Uganda Limited|Hannington Road|32014700~37|01|Finance Trust Bank Limited|Head Office|37014700~53|01|FINCA Uganda Ltd|Kampala|53014700~27|01|Guaranty Trust Bank Uganda Limited|Head Office|27014700~25|30|KCB Bank Uganda Limited|Kampala Road|25304700~55|01|Mercantile Credit Bank Ltd|Kampala|55014700~36|01|NCBA Bank Uganda|Nakasero|36014700~61|01|Opportunity Bank|Head Office|61014700~11|01|Orient Bank Limited|Main|11014700~56|01|POST BANK Uganda|Nkurumah Road|56014700~57|01|Pride Microfinance Ltd|Kampala|57014700~04|01|Stanbic Bank Uganda Limited|IBC|04014700~08|02|Standard Chartered Uganda Limited|Speke Road|08024700~62|01|Top Finance Bank|Kampala|62014700~06|01|Tropical Bank Limited|Kampala Road|06014700~60|01|UGAFODE|Kampala|60014700~26|01|United Bank for Africa Uganda Limited|Jinja Road|26014700
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

                            /*quest = (
                                    "FORMID:O-GetCommercialBankBranch:" +
                                            "BANKID:" + am.getBankID() + ":"+
                                            "BENEFICIARYBANKID:" + bankCode + ":"
                            );
                            am.connectOldTwo(getString(R.string.fetchingBranches)+"\n"+getString(R.string.forWord)+"\n"+bankName.getSelectedItem().toString().trim(), quest,Funds_Transfer_External.this, "BRH");*/
                            //am.get(Funds_Transfer_External.this,quest,getString(R.string.fetchingBranches)+"\n"+getString(R.string.forWord)+"\n"+bankName.getSelectedItem().toString().trim(),"BRH");
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
                // TODO: 12/19/2020
                //3547|ABAITA ABABIRI|BARCUGKXXXX|01204700,7547|AIRPORT|BARCUGKXXXX|01204700,
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
