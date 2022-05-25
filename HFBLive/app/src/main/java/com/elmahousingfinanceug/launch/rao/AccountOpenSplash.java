package com.elmahousingfinanceug.launch.rao;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.AllMethods;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;


import mumayank.com.airlocationlibrary.AirLocation;

public class AccountOpenSplash extends AppCompatActivity implements ResponseListener {
    private AllMethods am;
   private AirLocation airLocation;
    private static final int REQUEST_ID_AIRLOCATION = 1235;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rao_splash);

        am = new AllMethods(this);
        am.disableScreenShot(this);

        TextView title = findViewById(R.id.title);

        //Combine words with different colors and fonts to appear in one sentence with SpannableStringBuilder
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String a = getString(R.string.open_an_account);
        SpannableString aSpannable = new SpannableString(a);
        aSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_grey)), 0, a.length(), 0);
        builder.append(aSpannable);

        String b = getString(R.string.digital_instantly);
        SpannableString bSpannable = new SpannableString(b);
        bSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),0, b.length(), 0);
        bSpannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,b.length(), 0);
        builder.append(bSpannable);
        title.setText(builder, TextView.BufferType.SPANNABLE);

        builder.clear();

        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NonNull Location location) {
                //Save location in prefs
                am.saveLatitude(String.valueOf(location.getLatitude()));
                am.saveLongitude(String.valueOf(location.getLongitude()));
            }
            @Override
            public void onFailed(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });

        findViewById(R.id.submit).setOnClickListener(v -> {
            am.get(this,
                    "FORMID:O-GetBankHFStaticData:" + //GetBankStaticData GetBankHFStaticData
                    "MOBILENUMBER:" + am.getUserPhone() + ":" +
                    "BANKID:" + am.getBankID() + ":",
                    getString(R.string.processingReq),"RAO_GSD");
        });
      
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ID_AIRLOCATION) {
            airLocation.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResponse(String response, String step) {
        String[] howLong = response.split(":");
        String[] fieldIds = new String[howLong.length/2];
        String[] fieldValues = new String[howLong.length/2];
        am.separate(response,":",fieldIds,fieldValues);
        String Status = am.FindInArray(fieldIds, fieldValues, "STATUS");
        String Message = am.FindInArray(fieldIds, fieldValues, "MESSAGE");
        if (TextUtils.isEmpty(Message)) {
            Message = am.FindInArray(fieldIds, fieldValues, "DATA");
        }
        if (TextUtils.isEmpty(Message)) {
            Toast.makeText(this, getString(R.string.tryAgain), Toast.LENGTH_LONG).show();
        } else {
            //GetBankStaticData response   STATUS:OK:DATA:{"Satatus":"00","LoanPeriod":[{"ID":"1","Description":"1 Month"},{"ID":"2","Description":"2 Month"},{"ID":"3","Description":"3 Month"},{"ID":"4","Description":"4 Month"},{"ID":"5","Description":"5 Month"},{"ID":"6","Description":"6 Month"},{"ID":"7","Description":"7 Month"},{"ID":"8","Description":"8 Month"},{"ID":"9","Description":"9 Month"},{"ID":"10","Description":"10 Month"},{"ID":"11","Description":"11 Month"},{"ID":"12","Description":"12 Month"}],"LoanTypes":[{"AccountCategory":"EB","MinLoanTerm":"1","MaxLoanTerm":"3","LoanTypeID":"IL","LoanTypes":"CenteLoan"},{"AccountCategory":"NS","MinLoanTerm":"1","MaxLoanTerm":"3","LoanTypeID":"IL","LoanTypes":"CenteLoan"},{"AccountCategory":"SE","MinLoanTerm":"1","MaxLoanTerm":"3","LoanTypeID":"IL","LoanTypes":"CenteLoan"},{"AccountCategory":"SS","MinLoanTerm":"4","MaxLoanTerm":"12","LoanTypeID":"IL","LoanTypes":"CenteLoan Staff - Unsecured"},{"AccountCategory":"SS","MinLoanTerm":"1","MaxLoanTerm":"3","LoanTypeID":"SA","LoanTypes":"Salary Advance"},{"AccountCategory":"SS","MinLoanTerm":"1","MaxLoanTerm":"3","LoanTypeID":"IL","LoanTypes":"CenteLoan - Staff"}],"AfricelDataFrequency":[{"ID":"MONTHLY","Description":"Monthly"},{"ID":"WEEKLY","Description":"Weekly"},{"ID":"DAILY","Description":"Daily"}],"MTNDataFrequency":[{"ID":"DAILY","Description":"Daily"},{"ID":"GAGATIME","Description":"Gaga Time"},{"ID":"Tooti","Description":"Tooti Bundle"},{"ID":"WEEKLY","Description":"Weekly"},{"ID":"MONTHLY","Description":"Monthly"},{"ID":"Three_months","Description":"Three months"},{"ID":"Night_shift_Bundle","Description":"Night shift Bundle"},{"ID":"Tooti_Bundle","Description":"Tooti Bundle"},{"ID":"Unlimited","Description":"Unlimited"}],"GasAfricaPaymentPlan":[{"ID":"CASH","Description":"Cash"},{"ID":"LOAN","Description":"Loan"}],"GasAfricaPaymentSubPlan":[{"ID":"Refill","Description":"Refill","RelationID":"CASH"},{"ID":"Outright","Description":"Outright","RelationID":"CASH"},{"ID":"Complete","Description":"Complete","RelationID":"CASH"},{"ID":"Complete","Description":"Complete","RelationID":"LOAN"},{"ID":"Refill","Description":"Refill","RelationID":"LOAN"}],"GasAfricaCity":[{"ID":"Kampala","Description":"Kampala"}],"GasAfricaDivision":[{"ID":"Central","Description":"Central Division","RelationID":"Kampala"},{"ID":"Kawempe","Description":"Kawempe Division","RelationID":"Kampala"},{"ID":"Lubaga","Description":"Lubaga Division","RelationID":"Kampala"},{"ID":"Makindye","Description":"Makindye Division","RelationID":"Kampala"},{"ID":"Nakawa","Description":"Nakawa Division","RelationID":"Kampala"}],"GasAfricaProductPrice":[{"ID":"6KGCBC","Description":"6kg Cylinder Burner Grill 0 Refill,0, 200000","RelationID":"Complete"},{"ID":"6KGCBO","Description":"6kg Cylinder 0 Refill,0,162000","RelationID":"Outright"},{"ID":"6KGCBR","Description":"Refill,1,55000","RelationID":"Refill"}],"GasAfricaLoanProductPrice":[{"ID":"6KG2","Description":"6kg Cylinder Burner Grill and 2 Refill,2,310000","RelationID":"Complete"},{"ID":"6KG1","Description":"1 Refill,1, 55000","RelationID":"Refill"}],"OnlineAccountProduct":[{"ID":"31111","Description":"Cente Current Accounts Ordinary","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31111","Description":"Cente Current Accounts Ordinary","RelationID":"USD","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31111","Description":"Cente Current Accounts Ordinary","RelationID":"GBP","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31111","Description":"Cente Current Accounts Ordinary","RelationID":"EUR","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31111","Description":"Cente Current Accounts Ordinary","RelationID":"KES","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31121","Description":"Cente Current Accounts Excecutive","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31121","Description":"Cente Current Accounts Excecutive","RelationID":"USD","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31121","Description":"Cente Current Accounts Excecutive","RelationID":"GBP","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31121","Description":"Cente Current Accounts Excecutive","RelationID":"EURO","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"31121","Description":"Cente Current Accounts Excecutive","RelationID":"KES","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Current-Accounts"},{"ID":"32111","Description":"Cente Ordinary Savings Account","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Ordinary-Savings-Account"},{"ID":"32111","Description":"Cente Ordinary Savings Account","RelationID":"USD","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Ordinary-Savings-Account"},{"ID":"32111","Description":"Cente Ordinary Savings Account","RelationID":"GBP","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Ordinary-Savings-Account"},{"ID":"32111","Description":"Cente Ordinary Savings Account","RelationID":"EURO","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Ordinary-Savings-Account"},{"ID":"32111","Description":"Cente Ordinary Savings Account","RelationID":"KES","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Ordinary-Savings-Account"},{"ID":"32112","Description":"CenteSupaWoman Account","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/CenteSupaWoman-Account"},{"ID":"32114","Description":"Cente Plus Account","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Plus-Account"},{"ID":"32115","Description":"CenteVolution Account","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/CenteVolution-Account"},{"ID":"32116","Description":"Cente Diaspora Account","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Diaspora-Account"},{"ID":"32116","Description":"Cente Diaspora Account","RelationID":"USD","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Diaspora-Account"},{"ID":"32116","Description":"Cente Diaspora Account","RelationID":"GBP","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Diaspora-Account"},{"ID":"32116","Description":"Cente Diaspora Account","RelationID":"EURO","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Diaspora-Account"},{"ID":"32116","Description":"Cente Diaspora Account","RelationID":"KES","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Diaspora-Account"},{"ID":"32219","Description":"Cente Save Account","RelationID":"UGX","Urls":"https:\/\/www.centenarybank.co.ug\/index.php\/product\/Cente-Save-Account"}],"BankBranch":[{"ID":"79","Description":"APAC","RelationID":""},{"ID":"20","Description":"ARUA","RelationID":""},{"ID":"46","Description":"BUGIRI","RelationID":""},{"ID":"24","Description":"BWAISE","RelationID":""},{"ID":"68","Description":"BWERA","RelationID":""},{"ID":"30","Description":"ENTEBBE ROAD","RelationID":""},{"ID":"55","Description":"FORT PORTAL","RelationID":""},{"ID":"75","Description":"GULU","RelationID":""},{"ID":"80","Description":"HOIMA","RelationID":""},{"ID":"52","Description":"IBANDA BRANCH","RelationID":""},{"ID":"48","Description":"IGANGA SERVICE CENTER","RelationID":""},{"ID":"65","Description":"ISHAKA","RelationID":""},{"ID":"57","Description":"ISINGIRO","RelationID":""},{"ID":"43","Description":"JINJA BRANCH","RelationID":""},{"ID":"33","Description":"KABALAGALA","RelationID":""},{"ID":"70","Description":"KABALE","RelationID":""},{"ID":"66","Description":"KAGADI","RelationID":""},{"ID":"64","Description":"KAMULI","RelationID":""},{"ID":"54","Description":"KANUNGU","RelationID":""},{"ID":"32","Description":"KAPCHORWA","RelationID":""},{"ID":"15","Description":"KASESE","RelationID":""},{"ID":"39","Description":"KAYABWE","RelationID":""},{"ID":"41","Description":"KAYUNGA","RelationID":""},{"ID":"91","Description":"KIBOGA SUB BRANCH","RelationID":""},{"ID":"27","Description":"KIKUUBO","RelationID":""},{"ID":"44","Description":"KIREKA","RelationID":""},{"ID":"58","Description":"KISORO","RelationID":""},{"ID":"74","Description":"KITGUM","RelationID":""},{"ID":"19","Description":"KOBOKO","RelationID":""},{"ID":"62","Description":"KOTIDO BRANCH","RelationID":""},{"ID":"47","Description":"KUMI","RelationID":""},{"ID":"56","Description":"KYENJOJO SUB-BRANCH","RelationID":""},{"ID":"60","Description":"KYOTERA","RelationID":""},{"ID":"10","Description":"LIRA","RelationID":""},{"ID":"29","Description":"LUGOGO BRANCH","RelationID":""},{"ID":"61","Description":"LYANTONDE","RelationID":""},{"ID":"49","Description":"MAKERERE","RelationID":""},{"ID":"37","Description":"MAPEERA","RelationID":""},{"ID":"40","Description":"MASAKA","RelationID":""},{"ID":"81","Description":"MASINDI","RelationID":""},{"ID":"31","Description":"MBALE","RelationID":""},{"ID":"50","Description":"MBARARA","RelationID":""},{"ID":"90","Description":"MITYANA","RelationID":""},{"ID":"63","Description":"MOROTO BRANCH","RelationID":""},{"ID":"59","Description":"MPIGI","RelationID":""},{"ID":"89","Description":"MUBENDE BRANCH","RelationID":""},{"ID":"42","Description":"MUKONO","RelationID":""},{"ID":"26","Description":"NAJJANANKUMBI","RelationID":""},{"ID":"21","Description":"NAKIVUBO","RelationID":""},{"ID":"25","Description":"NAMIREMBE RD","RelationID":""},{"ID":"28","Description":"NATEETE","RelationID":""},{"ID":"76","Description":"NEBBI BRANCH","RelationID":""},{"ID":"36","Description":"NTINDA SERVICE CENTER","RelationID":""},{"ID":"53","Description":"NTUNGAMO","RelationID":""},{"ID":"67","Description":"PAIDAH","RelationID":""},{"ID":"38","Description":"RUBAGA SERVICE CENTER","RelationID":""},{"ID":"51","Description":"RUKUNGIRI","RelationID":""},{"ID":"45","Description":"SOROTI","RelationID":""},{"ID":"95","Description":"TORORO","RelationID":""},{"ID":"34","Description":"WAKISO","RelationID":""},{"ID":"35","Description":"WOBULENZI","RelationID":""}]}
            switch(Status) {
                case "000":
                case "00":
                case "OK":
                    String replace = response
                            .replace("STATUS:OK:DATA:", "")
                            .replace("STATUS:000:DATA:", "")
                            .replace("STATUS:00:DATA:", "");
                    if(step.equals("RAO_GSD")) {
                        am.putSavedBundle(replace);
                        new Handler().postDelayed(() -> am.get(AccountOpenSplash.this,
                                "FORMID:O-GetBranchHFStaticData:" +  //GetBankStaticData  GetBranchHFStaticData
                                        "MOBILENUMBER:" + am.getUserPhone() + ":" +
                                        "BANKID:" + am.getBankID() + ":",
                                getString(R.string.processingReq),"RAO_BRH"),400);
                    } else {
                        am.putSavedBranch(replace);
                        finish();
                        startActivity(new Intent(this, AccountOpenZMain.class));
                    }
                    break;
                default:
                    am.myDialog(this,getString(R.string.alert), Message);
                    break;
            }
        }
    } 
}