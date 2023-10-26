package com.elmahousingfinanceug_test.main_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import java.util.ArrayList;
import java.util.List;

public class Bill_Pay_Options extends BaseAct implements ResponseListener, VolleyResponse {
    LinearLayout a, b, c, d, e, f, g, h, i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_options);

        gToolBar(getString(R.string.billsPay));

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        f = findViewById(R.id.f);
        g = findViewById(R.id.g);
        h = findViewById(R.id.h);
        i = findViewById(R.id.i);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001001");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "DSTV").putExtra("Code", "0"));
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001014");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "GOTV").putExtra("Code", "0"));
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001015");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "STARTIMES").putExtra("Code", "0"));
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001003");
                String quest = ("FORMID:O-NWATERUGAREA:");
                //am.connectOldTwo(getString(R.string.fetchingAreas),quest,this,"NWA");
                am.get(Bill_Pay_Options.this,quest,getString(R.string.fetchingAreas),"NWA");
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001012");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "UMEME YAKA").putExtra("Code", "1"));
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001002");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "UMEME").putExtra("Code", "1"));
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("021001001");
                startActivity(new Intent(getApplicationContext(),School_Fees.class));
            }
        });

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001021");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "NSSF").putExtra("Code", "0"));
            }
        });

        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.saveMerchantID("007001022");
                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "URA").putExtra("Code", "0"));
            }
        });
    }

    @Override
    protected void onStart() {
        final List<LinearLayout> allLayouts = new ArrayList<>();
        allLayouts.add(a);
        allLayouts.add(b);
        allLayouts.add(c);
        allLayouts.add(d);
        allLayouts.add(e);
        allLayouts.add(f);
        /*allLayouts.add(g);
        allLayouts.add(h);
        allLayouts.add(i);*/
        am.Set_Items_2_Animate(allLayouts);
        super.onStart();
    }

//    public void bills(View o) {
//        switch (o.getId()){
//            case R.id.a:
//                am.saveMerchantID("007001001");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "DSTV").putExtra("Code", "0"));
//                break;
//            case R.id.b:
//                am.saveMerchantID("007001014");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "GOTV").putExtra("Code", "0"));
//                break;
//            case R.id.c:
//                am.saveMerchantID("007001015");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "STARTIMES").putExtra("Code", "0"));
//                break;
//            case R.id.d:
//                am.saveMerchantID("007001003");
//                String quest = ("FORMID:O-NWATERUGAREA:");
//                //am.connectOldTwo(getString(R.string.fetchingAreas),quest,this,"NWA");
//                am.get(this,quest,getString(R.string.fetchingAreas),"NWA");
//                break;
//            case R.id.e:
//                am.saveMerchantID("007001012");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "UMEME YAKA").putExtra("Code", "1"));
//                break;
//            case R.id.f:
//                am.saveMerchantID("007001002");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "UMEME").putExtra("Code", "1"));
//                break;
//            case R.id.g:
//                am.saveMerchantID("021001001");
//                startActivity(new Intent(getApplicationContext(),School_Fees.class));
//                break;
//            case R.id.h:
//                am.saveMerchantID("007001021");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "NSSF").putExtra("Code", "0"));
//                break;
//            case R.id.i:
//                am.saveMerchantID("007001022");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "URA").putExtra("Code", "0"));
//                break;
//            default:
//                am.saveMerchantID("007001019");
//                startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "KCCA").putExtra("Code", "0"));
//                break;
//        }
//    }

    @Override
    public void onResponse(String response, String step) {
        am.saveAreas(response);
        startActivity(new Intent(getApplicationContext(), Bill_Payments.class).putExtra("pageTitle", "National Water").putExtra("Code", "1"));
    }
}
