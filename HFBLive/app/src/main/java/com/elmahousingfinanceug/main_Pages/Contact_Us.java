package com.elmahousingfinanceug.main_Pages;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;

public class Contact_Us extends BaseAct {
    TextView email,call,call2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        email = findViewById(R.id.email);
        call = findViewById(R.id.call);
        call2 = findViewById(R.id.call2);

        gToolBar(getString(R.string.contactUs));
    }

    public void contactBank(View c) {
        switch (c.getId()) {
            case R.id.email:
                try {
                    Intent eIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",email.getText().toString().trim(), null))
                            .putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString().trim()})
                            .putExtra(Intent.EXTRA_TEXT,"Greetings\n\n");
                    startActivity(Intent.createChooser(eIntent, getString(R.string.chooseClient)));
                   /* Intent eintent = new Intent(Intent.ACTION_SEND);
                    eintent.putExtra(Intent.EXTRA_EMAIL, new String[]{email.getText().toString().trim()});
                    eintent.putExtra(Intent.EXTRA_TEXT, "Greetings");
                    eintent.setType("message/rfc822");
                    startActivity(Intent.createChooser(eintent, getString(R.string.chooseClient)));*/
                } catch (Exception e) {
                    am.LogThis("Email failed ► " + e.getMessage());
                    am.myDialog(Contact_Us.this, getString(R.string.error), getString(R.string.errorEclient));
                }
                break;
            case R.id.call:
                gDialog = new Dialog(this);
                //noinspection ConstantConditions
                gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                gDialog.setContentView(R.layout.dialog_confirm);
                final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
                final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
                final TextView txtNo = gDialog.findViewById(R.id.noBTN);
                txtMessage.setText(String.format("%s  %s", getText(R.string.call), call.getText().toString().trim()));
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gDialog.dismiss();
                    }
                });
                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = call.getText().toString().trim();
                        callNumber();
                        gDialog.dismiss();
                    }
                });
                gDialog.show();
                break;
            case R.id.call2:
                gDialog = new Dialog(this);
                //noinspection ConstantConditions
                gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                gDialog.setContentView(R.layout.dialog_confirm);
                final TextView txtMessage2 = gDialog.findViewById(R.id.dialog_message);
                final TextView txtOk2 = gDialog.findViewById(R.id.yesBTN);
                final TextView txtNo2 = gDialog.findViewById(R.id.noBTN);
                txtMessage2.setText(String.format("%s  %s", getText(R.string.call), call2.getText().toString().trim()));
                txtNo2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gDialog.dismiss();
                    }
                });
                txtOk2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number = call.getText().toString().trim();
                        callNumber();
                        gDialog.dismiss();
                    }
                });
                gDialog.show();
                break;
            case R.id.facebook:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getFacebookPageURL(Contact_Us.this))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.twitter:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/housingfinanceU")));
                    //startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("twitter://user?user_id=312525914")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (Exception e){
                    e.printStackTrace();
                }
            case R.id.whatsapp:
                try{
                    PackageManager packageManager = getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone="+"256790542262" ;
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }else {
                        am.ToastMessageLong(this,getString(R.string.unexpectedError));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.website:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.housingfinance.co.ug")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private String getFacebookPageURL(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            boolean activated =  packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if(activated){
                if ((versionCode >= 3002850)) {
                    return "fb://facewebmodal/f?href=https://www.facebook.com/HousingFinanceBank";
                } else {
                    return "fb://page/HousingFinanceBank";
                }
            } else {
                return "https://www.facebook.com/HousingFinanceBank";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "https://www.facebook.com/HousingFinanceBank";
        }
    }
}
