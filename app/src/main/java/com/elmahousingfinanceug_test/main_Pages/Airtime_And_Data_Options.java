package com.elmahousingfinanceug_test.main_Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.launch.rao.AccountOpenSplash;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

public class Airtime_And_Data_Options extends BaseAct implements ResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime_and_data_options);

        gToolBar(getString(R.string.airtimeanddata));
    }

    public void adSelection(View u) {
        switch (u.getId()){
            case R.id.a:
                startActivity(new Intent(getApplicationContext(), Airtime_Services.class));
                break;
            case R.id.b:
                if(am.getStaticData().equals("")) {
                    getBundles();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), Data.class));
                }
                break;
        }
    }

    public void getBundles(){
        am.get(this,
                "FORMID:O-GetMtnDataFrequency:" +
                        "BANKID:" + am.getBankID() + ":" +
                        "MOBILENUMBER:" + am.getUserPhone() + ":",
                getString(R.string.loading),
                "ABD");

//        new Handler().postDelayed(() -> am.get_(Airtime_And_Data_Options.this,
//                "FORMID:O-GetBankStaticData:" +
//                        "BANKID:" + am.getBankID() + ":" +
//                        "MOBILENUMBER:" + am.getUserPhone() + ":",
//                getString(R.string.loading),
//                "ABD"),400);
    }

    @Override
    public void onResponse(String response, String step) {
        switch (step) {
            case "ABD":
                try {
                    // TODO: 9/19/2020 remove
                    //STATUS:OK:DATA:{"Satatus":"00","LoanPeriod":[{"ID":"1","Description":"1 Month"},{"ID":"2","Description":"2 Month"},{"ID":"3","Description":"3 Month"}],"AfricelDataFrequency":[{"ID":"MONTHLY","Description":"Monthly"},{"ID":"WEEKLY","Description":"Weekly"},{"ID":"DAILY","Description":"Daily"}],"MTNDataFrequency":[{"ID":"DAILY","Description":"Daily"},{"ID":"WEEKLY","Description":"Weekly"},{"ID":"MONTHLY","Description":"Monthly"},{"ID":"Three_months","Description":"Three months"},{"ID":"Night_shift_Bundle","Description":"Night shift Bundle"},{"ID":"Tooti_Bundle","Description":"Tooti Bundle"},{"ID":"UNLIMITED","Description":"Unlimited"}]}
                    String [] splits = response.split(":");
                    switch (splits[1]) {
                        case "000":
                        case "00":
                        case "OK":
                            response = response.replace("STATUS:000:DATA:","");
                            response = response.replace("STATUS:00:DATA:","");
                            response = response.replace("STATUS:OK:DATA:","");
                            response = response.replace("STATUS:000:MESSAGE:","");
                            response = response.replace("STATUS:00:MESSAGE:","");
                            response = response.replace("STATUS:OK:MESSAGE:","");
                            am.saveStaticData(response);
//                            Log.e("RESPONSE111", response);
                            startActivity(new Intent(this,Data.class));
                            break;
                        default:
                            am.myDialog(this,getString(R.string.alert),splits[3]);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}