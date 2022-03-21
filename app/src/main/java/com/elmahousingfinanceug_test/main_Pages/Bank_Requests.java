package com.elmahousingfinanceug_test.main_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;

import java.util.ArrayList;
import java.util.List;

public class Bank_Requests extends BaseAct {
    LinearLayout a,b,c,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_requests);

        gToolBar(getString(R.string.bankRequests));

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
    }

    public void bRPage(View bR) {
        switch (bR.getId()){
            case R.id.a:
                startActivity(new Intent(this,Standing_Orders.class));
                break;
            case R.id.b:
                startActivity(new Intent(this,E_Statement.class));
                break;
            case R.id.c:
                startActivity(new Intent(this,Cheques.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        final List<LinearLayout> allLayouts = new ArrayList<>();
        allLayouts.add(a);
        allLayouts.add(b);
        allLayouts.add(c);
        allLayouts.add(d);
        am.Set_Items_2_Animate(allLayouts);
        super.onStart();
    }
}
