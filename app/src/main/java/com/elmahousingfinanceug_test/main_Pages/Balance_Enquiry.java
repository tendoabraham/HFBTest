package com.elmahousingfinanceug_test.main_Pages;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

public class Balance_Enquiry extends BaseAct implements ResponseListener, VolleyResponse {
    TextView  accountName,accountMobileNumber,accountTotal,accountCredit,accountCurrency,accountCurrency2;
    Spinner accountNumber;
    LinearLayout accountInfoLayout;
    String quest="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_enquiry);

        gToolBar(getString(R.string.balanceEnquiry));

        accountName = findViewById(R.id.accountName);
        accountMobileNumber = findViewById(R.id.accountMobileNumber);
        accountNumber = findViewById(R.id.accountNumber);
        accountCredit = findViewById(R.id.accountCredit);
        accountCurrency = findViewById(R.id.accountCurency);
        accountCurrency2 = findViewById(R.id.accountCurency2);
        accountTotal = findViewById(R.id.accountTotal);
        accountInfoLayout = findViewById(R.id.accountInfoLayout);
        accountInfoLayout.setVisibility(View.GONE);
        accountName.setText(am.getUserName());
        accountMobileNumber.setText(am.getUserPhone());
    }

    private void populateSpinners() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accountInfoLayout.setVisibility(View.GONE);
                } else {
                    quest = (
                            "FORMID:B-:" +
                                    "MERCHANTID:BALANCE:" +
                                    "BANKACCOUNTID:" + am.getBankAccountID(position) + ":"
                    );
                    //am.connectOldTwo(getString(R.string.processingReq),quest,Balance_Enquiry.this,"BAL");
                    am.get_(Balance_Enquiry.this,quest,getString(R.string.processingReq),"BAL");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        populateSpinners();
        super.onResume();
    }

    @Override
    public void onResponse(String response, String step) {
        String[] values = response.split(";");
        String currency = values[1].replace("Actual","");
        String total = values[2].replace("Available","");
        String credit = values[3].trim();
        accountCurrency.setText(currency);
        accountCurrency2.setText(currency);
        accountTotal.setText(am.Amount_Thousands(total));
        accountCredit.setText(am.Amount_Thousands(credit));
        accountInfoLayout.setVisibility(View.VISIBLE);
        
    }
}
