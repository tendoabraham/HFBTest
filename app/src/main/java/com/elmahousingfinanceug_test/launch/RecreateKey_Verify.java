package com.elmahousingfinanceug_test.launch;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Contact_Us;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

public class RecreateKey_Verify extends AppCompatActivity implements ResponseListener, VolleyResponse {
    private AllMethods am;
    private EditText eTKey;
    private String quest="";
    TextView verify, contactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreate_key_v);
        am = new AllMethods(this);
        am.disableScreenShot(this);

        eTKey = findViewById(R.id.eTKey);
        verify = findViewById(R.id.verify);
        contactUs = findViewById(R.id.contactUs);

        eTKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eTKey.getText().length() >= 6) {
                    quest = (
                            "FORMID:VERIFYRECREATEKEY:" +
                                    "MOBILENUMBER:" + am.getUserPhone() + ":" +
                                    "KEY:"+ eTKey.getText().toString().trim() + ":"
                    );
                    //am.connectOldTwo(getString(R.string.verifying),quest,RecreateKey_Verify.this,"RV");
                    am.get_(RecreateKey_Verify.this,quest,getString(R.string.verifying),"RV");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eTKey.getText().length() < 6) {
                    eTKey.setError(getString(R.string.invalidInput));
                } else {
                    quest = (
                            "FORMID:VERIFYRECREATEKEY:" +
                                    "MOBILENUMBER:" + am.getUserPhone() + ":" +
                                    "KEY:"+ eTKey.getText().toString().trim()+ ":"
                    );
                    //am.connectOldTwo(getString(R.string.verifying),quest,RecreateKey_Verify.this,"RV");
                    am.get_(RecreateKey_Verify.this,quest,getString(R.string.verifying),"RV");
                }
            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecreateKey_Verify.this, Contact_Us.class));
            }
        });
    }

//    public void verification(View t) {
//        switch (t.getId()){
//            case R.id.verify:
//                if (eTKey.getText().length() < 6) {
//                    eTKey.setError(getString(R.string.invalidInput));
//                } else {
//                    quest = (
//                            "FORMID:VERIFYRECREATEKEY:" +
//                                    "MOBILENUMBER:" + am.getUserPhone() + ":" +
//                                    "KEY:"+ eTKey.getText().toString().trim()+ ":"
//                    );
//                    //am.connectOldTwo(getString(R.string.verifying),quest,RecreateKey_Verify.this,"RV");
//                    am.get_(RecreateKey_Verify.this,quest,getString(R.string.verifying),"RV");
//                }
//                break;
//            case R.id.contactUs:
//                startActivity(new Intent(this, Contact_Us.class));
//                break;
//        }
//    }

    @Override
    protected void onResume() {
        eTKey.setText("");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            if(am.mDialog!=null) am.mDialog.dismiss();
            am.progressDialog("0");
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResponse(String response, String step) {
        am.saveCustomerID(response.trim());
        finish();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}
