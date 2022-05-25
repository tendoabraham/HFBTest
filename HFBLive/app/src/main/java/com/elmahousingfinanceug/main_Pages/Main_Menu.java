package com.elmahousingfinanceug.main_Pages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Menu extends BaseAct implements NavigationView.OnNavigationItemSelectedListener, ResponseListener {
    TextView username,userPhone,accountTotal,accountCredit;
    CircleImageView userProfilePicture;
    NavigationView navigationView;
    LinearLayout a, b, c, d, e, f, g, h, i, j;
    private DrawerLayout drawer;
    private String selectedAccount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.mainMenu);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(R.string.mainMenu);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.menuOptions);
            }
        };
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(getResources().getColorStateList(R.color.black));

        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.userName);
        userPhone = header.findViewById(R.id.userNumber);
        userProfilePicture = header.findViewById(R.id.user_profile_picture);
        username.setText(am.getUserName());
        userPhone.setText(am.getUserPhone());

        userProfilePicture.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Profile.class)));

        Spinner accountNumber = findViewById(R.id.accountNumber);
        accountTotal = findViewById(R.id.accountTotal);
        accountCredit = findViewById(R.id.accountCredit);
        a = findViewById(R.id.balanceEnquiry);
        b = findViewById(R.id.miniStatement);
        c = findViewById(R.id.fundsTransfer);
        d = findViewById(R.id.airTimeServices);
        e = findViewById(R.id.bankRequest);
        f = findViewById(R.id.mobileMoney);
        g = findViewById(R.id.billPayments);
        h = findViewById(R.id.extTransfer);
        i = findViewById(R.id.activityLog);
        j = findViewById(R.id.schoolFees);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, am.getDashAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccount = am.getDashBankAccountID(position);
                am.get(Main_Menu.this,
                        "FORMID:B-:" +
                                "MERCHANTID:BALANCE:" +
                                "BANKACCOUNTID:" + selectedAccount + ":",
                        getString(R.string.loading),"BAL");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        am.saveCount("0");
    }

    private void refreshPage() {
        final List<LinearLayout> allLayouts = new ArrayList<>();
        allLayouts.add(a);
        allLayouts.add(b);
        allLayouts.add(c);
        allLayouts.add(d);
        allLayouts.add(e);
        allLayouts.add(f);
        allLayouts.add(g);
        allLayouts.add(h);
        allLayouts.add(i);
        //allLayouts.add(j);
        am.Set_Items_2_Animate(allLayouts);
    }

    public void mnu(View u) {
        switch (u.getId()){
            case R.id.balanceEnquiry:
                startActivity(new Intent(getApplicationContext(), Balance_Enquiry.class));
                break;
            case R.id.miniStatement:
                startActivity(new Intent(getApplicationContext(), Mini_Statement.class));
                break;
            case R.id.fundsTransfer:
                startActivity(new Intent(getApplicationContext(), Funds_Transfer.class));
                break;
            case R.id.airTimeServices:
                startActivity(new Intent(getApplicationContext(), Airtime_Services.class));
                break;
            case R.id.mobileMoney:
                startActivity(new Intent(getApplicationContext(), Mobile_Money_Two.class));
                break;
            case R.id.bankRequest:
                startActivity(new Intent(getApplicationContext(), Bank_Requests.class));
                break;
            case R.id.billPayments:
                startActivity(new Intent(getApplicationContext(), Bill_Pay_Options.class));
                break;
            case R.id.schoolFees:
                am.saveMerchantID("SCHOOLFEES");
                startActivity(new Intent(getApplicationContext(), School_Fees.class));
                break;
            case R.id.extTransfer:
                startActivity(new Intent(getApplicationContext(), Funds_Transfer_External.class));
                break;
            case R.id.activityLog:
                startActivity(new Intent(getApplicationContext(), Activity_Log.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        refreshPage();
        try {
            if (!am.getUserPic().equals("")) {
                Glide.with(this).load(Base64.decode(am.getUserPic(), Base64.DEFAULT)).into(userProfilePicture);
            } else {
                userProfilePicture.setImageResource(R.drawable.roundaccount);
            }
        } catch (Exception e) {
            am.LogThis("picException : "+e.getMessage());
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        if(am.getDoneTrx()){
            am.get(Main_Menu.this,
                    "FORMID:B-:" +
                            "MERCHANTID:BALANCE:" +
                            "BANKACCOUNTID:" + selectedAccount + ":",
                    getString(R.string.loading),"BAL");
        }
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        am.Drawer_Item_Clicked(Main_Menu.this, id);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            escape();
        }
    }

    @Override
    public void onResponse(String response, String step) {
        String[] values = response.replace("/Unused limit","").split(";");
        String currency = values[1].replace("Actual","");
        String total = values[2].replace("Available","");
        String credit = values[3].trim();
        accountTotal.setText(am.Amount_Thousands(total));
        accountCredit.setText(am.Amount_Thousands(credit));
        am.saveDoneTrx(false);
    }
}
