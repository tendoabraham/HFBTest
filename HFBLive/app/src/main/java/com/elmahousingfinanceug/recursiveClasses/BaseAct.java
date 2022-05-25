package com.elmahousingfinanceug.recursiveClasses;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.launch.App_Init;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;

public class BaseAct extends AppCompatActivity {
    public AllMethods am;
    public Dialog gDialog;
    public static String number;
    public static final int M_PERMISSION_READ_CONTACTS =732,PICK_CONTACT=742,M_PERMISSION_CALL_PHONE=114;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(App_Init.getThemeId());
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        am = new AllMethods(this);
        am.disableScreenShot(this);
        setContentView(R.layout.activity_base);
        ForegroundCheck.get(this).addListener(am.fore_Back_Listener);
        registerReceiver(screenReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    public void gToolBar(String title){
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getContact(View vct){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true)
                        .setTitle(R.string.alert)
                        .setMessage(R.string.readContacts)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(BaseAct.this,new String[]{Manifest.permission.READ_CONTACTS}, M_PERMISSION_READ_CONTACTS);
                            }})
                        .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                gDialog = builder.show();
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, M_PERMISSION_READ_CONTACTS);
            }
        } else {
            startActivityForResult(new Intent(Intent.ACTION_PICK).setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE),PICK_CONTACT);
        }
    }

    public void callNumber(){
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, M_PERMISSION_CALL_PHONE);
            } else {
                startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+number)));
            }
        } catch (ActivityNotFoundException aE) {
            am.LogThis("Call failed : "+aE.getMessage());
            am.ToastMessageLong(this,getString(R.string.error)+" "+getString(R.string.callfailed));
        }
    }

    public void escape(){
        gDialog = new Dialog(this);
        //noinspection ConstantConditions
        gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gDialog.setContentView(R.layout.dialog_confirm);
        final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
        final TextView noBTN = gDialog.findViewById(R.id.noBTN);
        final TextView yesBTN = gDialog.findViewById(R.id.yesBTN);
        txtMessage.setText(getString(R.string.sureExit));
        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gDialog.dismiss();
            }
        });
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.nJe();
                gDialog.dismiss();
            }
        });
        gDialog.setCancelable(true);
        gDialog.show();
        am.animate_Text(txtMessage);
        am.animate_Text(noBTN);
        am.animate_Text(yesBTN);
    }

    private final BroadcastReceiver noConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            if (am.netDialog == null || !am.netDialog.isShowing()) am.promptUser(BaseAct.this);
        }
    };

    private final BroadcastReceiver thereIsConnection = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            if (am.netDialog != null) am.netDialog.dismiss();
        }
    };

    public final BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            try {
                if (Objects.requireNonNull(i.getAction()).equals(Intent.ACTION_SCREEN_OFF)) {
                    am.idleHandler.removeCallbacks(am.idleRunnable);
                    am.nJe();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void getLoc() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    am.saveLatitude(String.valueOf(latitude));
                    am.saveLongitude(String.valueOf(longitude));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        am.delayedIdle();
        registerReceiver(noConnection,new IntentFilter("OFF"));
        registerReceiver(thereIsConnection,new IntentFilter("ON"));
        registerReceiver(am.Network,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getLoc();
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        am.delayedIdle();
        super.onUserInteraction();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case M_PERMISSION_READ_CONTACTS:
                    startActivityForResult(new Intent(Intent.ACTION_PICK).setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE),PICK_CONTACT);
                    break;
                case M_PERMISSION_CALL_PHONE:
                    startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ number)));
                    break;
            }
        } else {
            am.myDialog(this, getString(R.string.alert), getString(R.string.noPermission));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause(){
        try {
            am.idleHandler.removeCallbacks(am.idleRunnable);
            if(gDialog !=null) gDialog.dismiss();
            unregisterReceiver(am.Network);
            unregisterReceiver(thereIsConnection);
            unregisterReceiver(noConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(screenReceiver);
            ForegroundCheck.get(this).removeListener(am.fore_Back_Listener);
            if(am.mDialog!=null) am.mDialog.dismiss();
            mFusedLocationClient.flushLocations();
            am.progressDialog("0");
        } catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
