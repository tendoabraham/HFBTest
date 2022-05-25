package com.elmahousingfinanceug.launch.rao;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chaos.view.PinView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.launch.Login;
import com.elmahousingfinanceug.main_Pages.Contact_Us;
import com.elmahousingfinanceug.recursiveClasses.AllMethods;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import mumayank.com.airlocationlibrary.AirLocation;

public class AccountOpenZExistingCustomersMain extends AppCompatActivity implements ResponseListener, View.OnClickListener {
    TextView title, prev, next, otpcountdown, tv_resend_otp, show_response;
    ProgressBar determinateBar, progressBar1;
    String AccoutNo, ProductID, ProductAccout, CurrencyURL, TermsURL, Currency, BranchID;
    Bitmap bitmapImageFront, bitmapImageBack, bitmapImageSelfie;
    ViewFlipper flipper;
    WebView webView;
    ImagePick front, backpick, selfie;
    Spinner currselect, branchselect, political_spin, occupation_spin, proffession_status, regionselect, districtName,
            countyName, subCountyName, parishName, villageName, eAName;
    LinearLayout ccg, currencyLayout, centeidsdetails, centeparentinfo, centesourceincome, centenextofkin,
            centecontacts, centeextras, centehearusfrom, new_Lay, existing_lay, aMr, bMrs, cMiss;
    ScrollView pan_pin_in;
    cicEditText staffPhoneNumber, accountNumber, name, sname, otherNames, nationalID, DOBEdit, FatherFirstName,
            FatherMiddleName, FatherLastName, MotherFirstName, MotherMiddleName, MotherLastName, Address, YearsAtAddress,
            PoliticallyExposed, IncomeperAnnum, EmploymentType, Occupation, PlaceofWork, NatureofBussiness,
            PeriodofEmployment, EmployerName, NatureofEmployment, NextofKinFirstName, NextofKinMiddleName,
            NextofKinLastName, NextofKinPhoneNumber, NextofKinAltPhoneNumber, NextofKinAddress, EmailAddress,
            PhoneNumber, AlternatePhoneNumber, ActualAddress, country, city, zipCode, c4, c44, c45, c5;

    EditText et_acc, et_pan, et_pin, et_phone;
    RadioGroup addressPeriod, employPeriod, accountsGroup, radioGroup;
    RadioButton yearsButtonM, monthsButtonM, yearsButtonE, monthsButtonE, FaceBook, Twitter,
            Instagram, tv, ss, bankstaff, HFBCustomer, Agent;
    AppCompatCheckBox chkMobileBanking, chkPos, chkATM, chkChequeBook, chkInternetBanking, chkAgencyBanking, radiob;
    PinView otpPinView;
    HorizontalScrollView scrollAccounts;
    CustomerCat customer_cat_;
    joeSingleChoice singleTitle;
    RelativeLayout success_failed;


    private AllMethods am;

    int REQUEST_IMAGE = 100, REQUEST_IMAGEX = 0, step_ = 0;

    String encodedImageFront = "", encodedImageBack = "", encodedImageSelfie = "", Usertitle = "", CustomerCategory = "",
            step0 = "Personal details?", step1 = "Terms & Conditions", step2 = "Other services", step3 = "OTP Confirmation", raoOTP = "", currentTask = "", INFOFIELD1 = "", INFOFIELD2 = "", INFOFIELD3 = "",
            INFOFIELD4 = "", INFOFIELD5 = "", token = "", payload = "", Device = "", uri = "", extrauri = "", new_request = "",
            selectedAccount = "", selectedAccountID = "", currName = "", branchID = "", termsUrl = "", periodAddressString = "", periodWorkString = "",
            StringPoliticallyExposed = "", occupationIDString = "", professionIDString = "", regionIDString = "", districtIDString = "", countyIDString = "",
            subcountyIDString = "", parishIdString = "", villageIdString = "", eAIdString = "";

    boolean done = false;
    private String[] FieldIDs, FieldValues;

    private AirLocation airLocation;
    Double latitude, longitude;

    CountDownTimer cnt;

    JSONArray accountProducts = null, branches = null;

    List<String> accountIDs = new ArrayList<>(), accountNames = new ArrayList<>(), currencyNames = new ArrayList<>(),
            listBranchIDs = new ArrayList<>(), listBranchNames = new ArrayList<>(), listUrls = new ArrayList<>(),
            politicsIDs = new ArrayList<>(), politicsNames = new ArrayList<>(),
            occupationIds = new ArrayList<>(), occupationNames = new ArrayList<>(), professionIds = new ArrayList<>(),
            professionNames = new ArrayList<>(), regionsIds = new ArrayList<>(), regionNames = new ArrayList<>(),
            districtIds = new ArrayList<>(), districtNames = new ArrayList<>(), countyIds = new ArrayList<>(),
            countylistNames = new ArrayList<>(), subcountyIds = new ArrayList<>(), subcountylistNames = new ArrayList<>(),
            parishIds = new ArrayList<>(), parishlistNames = new ArrayList<>(), villageIds = new ArrayList<>(),
            villagelistNames = new ArrayList<>(), eAIds = new ArrayList<>(), eAlistNames = new ArrayList<>();

    private final static int REQUEST_ID_AIRLOCATION = 1235;
    private static final String DATE_PATTERN = "((19|20)\\d\\d)[/-](0?[1-9]|1[012])[/-](0?[1-9]|[12][0-9]|3[01])";
    public static final SimpleDateFormat BIRTHDAY_FORMAT_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    public Boolean checkDateFormat(String date) {
        if (date == null)
            return false;
        try {
            BIRTHDAY_FORMAT_PARSER.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_open_account_existing);

        am = new AllMethods(this);
        am.disableScreenShot(this);
        Intent intent = getIntent();
        AccoutNo = intent.getStringExtra("accNo");
        ProductID = intent.getStringExtra("productID");
        ProductAccout = intent.getStringExtra("productName");
        CurrencyURL = intent.getStringExtra("currencyURL");
        TermsURL = intent.getStringExtra("termsURL");
        Currency = intent.getStringExtra("currency");
        BranchID = intent.getStringExtra("branch");


        title = findViewById(R.id.title);
        progressBar1 = findViewById(R.id.progressBar1);
        customer_cat_ = findViewById(R.id.customer_cat_);
        new_Lay = findViewById(R.id.new_Lay);
        existing_lay = findViewById(R.id.existing_lay);
        next = findViewById(R.id.next);
        scrollAccounts = findViewById(R.id.scrollAccounts);
        accountsGroup = findViewById(R.id.accountsGroup);
        currencyLayout = findViewById(R.id.currencyLayout);
        pan_pin_in = findViewById(R.id.pan_pin_in);
        et_acc = findViewById(R.id.et_acc);
        et_pan = findViewById(R.id.et_pan);
        et_pin = findViewById(R.id.et_pin);
        et_phone = findViewById(R.id.et_phone);
        singleTitle = findViewById(R.id.singleTitle);
        aMr = findViewById(R.id.aMr);
        bMrs = findViewById(R.id.bMrs);
        cMiss = findViewById(R.id.cMiss);
        success_failed = findViewById(R.id.success_failed);
        front = findViewById(R.id.front);
        backpick = findViewById(R.id.backpick);
        selfie = findViewById(R.id.selfie);
        show_response = findViewById(R.id.show_response);

        currselect = findViewById(R.id.currselect);
        branchselect = findViewById(R.id.branchselect);
        determinateBar = findViewById(R.id.determinateBar);
        prev = findViewById(R.id.prev);
        prev.setVisibility(View.VISIBLE);
        webView = findViewById(R.id.webview);
        flipper = findViewById(R.id.flipper);
        radiob = findViewById(R.id.radiobb);
        otpPinView = findViewById(R.id.otpPinView);
        otpcountdown = findViewById(R.id.otpcountdown);

        chkPos = findViewById(R.id.chkPos);
        chkMobileBanking = findViewById(R.id.chkMobileBanking);
        chkChequeBook = findViewById(R.id.chkChequeBook);
        chkATM = findViewById(R.id.chkATM);
        chkInternetBanking = findViewById(R.id.chkInternetBanking);
        chkAgencyBanking = findViewById(R.id.chkAgencyBanking);

        FaceBook = findViewById(R.id.FaceBook);
        Twitter = findViewById(R.id.Twitter);
        Instagram = findViewById(R.id.Instagram);

        radioGroup = findViewById(R.id.RGroup);
        tv = findViewById(R.id.tv);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        ss = findViewById(R.id.ss);
        bankstaff = findViewById(R.id.bankstaff);
        HFBCustomer = findViewById(R.id.HFBCustomer);
        Agent = findViewById(R.id.Agent);

        ccg = findViewById(R.id.ccg);
        centeidsdetails = findViewById(R.id.centeidsdetails);
        centeparentinfo = findViewById(R.id.centeparentinfo);
        centesourceincome = findViewById(R.id.centesourceincome);
        centenextofkin = findViewById(R.id.centenextofkin);
        centecontacts = findViewById(R.id.centecontacts);
        centeextras = findViewById(R.id.centeextras);
        centehearusfrom = findViewById(R.id.centehearusfrom);


        //Create Fields for user input
        generateForms();

        //Condition hide new for existing customers
//        if (am.getCustomerID().length() <10) {
//            new_Lay.setVisibility(View.VISIBLE);
//        } else {
//            new_Lay.setVisibility(View.GONE);
//        }

        title.setText(step0);


//        currselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position!=0) {
//                    currName = currselect.getSelectedItem().toString().trim();
//                } else {
//                    currName = "";
//                }
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });                                                                                           previous

//        branchselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position!=0) {
//                    branchID = listBranchIDs.get(position-1);
//                } else {
//                    branchID = "";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        otpPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                raoOTP = charSequence.toString();
                if (raoOTP.length() >= 6) {
                    verify();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        front.setLabel("National\nIdentity Card\n(Front)");
        backpick.setLabel("Passport\nPhoto\n(Selfie)");
        selfie.setLabel("Signature\nPhoto\nNO Thumb print allowed\nSignature should not be in Upper case");

        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NonNull Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getLocationAddress(location);
                //new GetAddressFromLocation().execute(location);
            }

            @Override
            public void onFailed(@NonNull AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });

        if (am.getUserPhone().length() > 8 && am.getUserPhone().startsWith("256")) {
            PhoneNumber.editText.setText(am.getUserPhone().replace("256", ""));
        }

        if (am.getUserEmail().length() > 3) {
            EmailAddress.editText.setText(am.getUserEmail());
        }

        if (am.getProceed()) {
            proceedFromWhereYouLeftOff();
        }
        registerReceiver(populateProducts, new IntentFilter("populate"));
    }

    private final BroadcastReceiver populateProducts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context cT, Intent iN) {
            try {

                String smsBody = iN.getStringExtra("Type");
                //Get account details from StaticData response

                //ProductID|2024|ProductName|Save plus Account|CurrencyID|UGX|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/save-plus-account/~
                // ProductID|2024|ProductName|Save plus Account|CurrencyID|USD|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/save-plus-account/~
                // ProductID|2024|ProductName|Save plus Account|CurrencyID|GBP|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/save-plus-account/~ProductID|2024|ProductName|Save plus Account|CurrencyID|EUR|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/save-plus-account/~ProductID|2024|ProductName|Easy Savings Account|CurrencyID|UGX|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/easy-savings-account/~ProductID|2024|ProductName|Easy Savings Account|CurrencyID|USD|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/easy-savings-account/~ProductID|2024|ProductName|Easy Savings Account|CurrencyID|GBP|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/easy-savings-account/~ProductID|2024|ProductName|Easy Savings Account|CurrencyID|EUR|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/easy-savings-account/~ProductID|2024|ProductName|U-Savers Account|CurrencyID|UGX|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/u-savers-account-student-account/~ProductID|2025|ProductName|Toto’s Treasure Account|CurrencyID|UGX|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/totos-treasure-account/~ProductID|2025|ProductName|Toto’s Treasure Account|CurrencyID|USD|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/totos-treasure-account/~ProductID|2025|ProductName|Toto’s Treasure Account|CurrencyID|GBP|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/totos-treasure-account/~ProductID|2025|ProductName|Toto’s Treasure Account|CurrencyID|EUR|Urls|https://www.housingfinance.co.ug/retail-banking/savings-account/totos-treasure-account/~ProductID|2030|ProductName|Salary Current Account|CurrencyID|UGX|Urls|https://www.housingfinance.co.ug/retail-banking/current-accounts/salary-current-account/~ProductID|2030|ProductName|Pearl Current Account|CurrencyID|UGX|Urls|https://www.housingfinance.co.ug/retail-banking/current-accounts/pearl-current-account/~ProductID|2030|ProductName|Pearl Current Account|CurrencyID|USD|Urls|https://www.housingfinance.co.ug/retail-banking/current-accounts/pearl-current-account/~ProductID|2030|ProductName|Pearl Current Account|CurrencyID|GBP|Urls|https://www.housingfinance.co.ug/retail-banking/current-accounts/pearl-current-account/~ProductID|2030|ProductName|Pearl Current Account|CurrencyID|EURO|Urls|https://www.housingfinance.co.ug/retail-banking/current-accounts/pearl-current-a

                String[] Products = am.getSavedBundle().split("~");
                accountIDs.clear();
                accountNames.clear();
                listUrls.clear();
                accountsGroup.invalidate();
                accountsGroup.removeAllViews();
                if (smsBody.equals("New Customer")) {
                    for (String aProduct : Products) {
                        String[] howLong = aProduct.split("\\|");
                        String[] field_IDs = new String[howLong.length / 2];
                        String[] field_Values = new String[howLong.length / 2];
                        am.separate(aProduct, "|", field_IDs, field_Values);
                        if (!accountIDs.contains(am.FindInArray(field_IDs, field_Values, "ProductID"))
                                && (am.FindInArray(field_IDs, field_Values, "ProductID").equals("EASY")
                                || am.FindInArray(field_IDs, field_Values, "ProductID").equals("SPLUS"))) {
                            accountIDs.add(am.FindInArray(field_IDs, field_Values, "ProductID"));
                            accountNames.add(am.FindInArray(field_IDs, field_Values, "ProductName"));
                            listUrls.add(am.FindInArray(field_IDs, field_Values, "Urls"));
                        }

                    }
                } else {

                    for (String aProduct : Products) {
                        String[] howLong = aProduct.split("\\|");
                        String[] field_IDs = new String[howLong.length / 2];
                        String[] field_Values = new String[howLong.length / 2];
                        am.separate(aProduct, "|", field_IDs, field_Values);
                        if (!accountIDs.contains(am.FindInArray(field_IDs, field_Values, "ProductID"))) {
                            accountIDs.add(am.FindInArray(field_IDs, field_Values, "ProductID"));
                            accountNames.add(am.FindInArray(field_IDs, field_Values, "ProductName"));
                            listUrls.add(am.FindInArray(field_IDs, field_Values, "Urls"));
                        }
                    }
                }

               

            /*JSONObject tact = new JSONObject(am.getSavedBundle());
            accountProducts = tact.getJSONArray("OnlineAccountProduct");
            branches = tact.getJSONArray("BankBranch");
            for (int i = 0; i < accountProducts.length(); i++) {
                JSONObject actor = accountProducts.getJSONObject(i);
                if (!accountIDs.contains(actor.getString("ID"))) {
                    accountIDs.add(actor.getString("ID"));
                    accountNames.add(actor.getString("Description"));
                    listUrls.add(actor.getString("Urls").replaceAll("\\\\",""));
                }
            }*/

                float density = getResources().getDisplayMetrics().density;
                int margin = (int) (6 * density);
                RadioGroup.LayoutParams rp = new RadioGroup.LayoutParams((int) (115 * density), RadioGroup.LayoutParams.MATCH_PARENT, 1);
                rp.setMargins(margin, margin, margin, margin);
                for (String buttonItem : accountIDs) {
                    RadioButton radioButton = new RadioButton(AccountOpenZExistingCustomersMain.this);
                    radioButton.setButtonDrawable(/*null*/ getResources().getDrawable(R.drawable.back_radio_button_tick));
                    radioButton.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
                    radioButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.online_rao), null, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        radioButton.setElevation(10f);
                    }
                    radioButton.setText(accountNames.get(accountIDs.indexOf(buttonItem)));
                    radioButton.setTextSize(10);
                    radioButton.setId(accountIDs.indexOf(buttonItem));
                    radioButton.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    radioButton.setPadding(20, 20, 20, 20);
                    accountsGroup.addView(radioButton, rp);
                }
                accountsGroup.setOnCheckedChangeListener((group, checkedId) -> {

                    int selectedIndex = group.getCheckedRadioButtonId();

                    if (selectedIndex != -1) {
                        int buttonId = group.getCheckedRadioButtonId();
                        RadioButton selectedButton = findViewById(buttonId);
                        selectedButton.toggle();
                    }

                    selectedAccount = ProductAccout;
                    selectedAccountID = ProductID;
                    termsUrl = TermsURL;
                    Log.e("URLTE", TermsURL);

//                    webView.loadUrl(termsUrl);
//                    webView.setOnClickListener(view1-> {
//                        try {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl)));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    });

                    currencyNames.clear();

                    try {

                        for (String aProduct : Products) {
                            String[] howLong = aProduct.split("\\|");
                            String[] field_IDs = new String[howLong.length / 2];
                            String[] field_Values = new String[howLong.length / 2];
                            am.separate(aProduct, "|", field_IDs, field_Values);
                            if (am.FindInArray(field_IDs, field_Values, "ProductID").matches(selectedAccountID)) {
                                currencyNames.add(am.FindInArray(field_IDs, field_Values, "CurrencyID"));
                            }
                        }

                    /*accountProducts = tact.getJSONArray("OnlineAccountProduct");
                    for (int i = 0; i < accountProducts.length(); i++) {
                        JSONObject actor = accountProducts.getJSONObject(i);
                        if(actor.getString("ID").matches(selectedAccountID)) {
                            currencyNames.add(actor.getString("RelationID"));
                        }
                    }*/

                        currencyNames.add(0, "Select Currency");
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(AccountOpenZExistingCustomersMain.this, android.R.layout.simple_spinner_item, currencyNames);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spiner_item);
                        currselect.setAdapter(spinnerArrayAdapter);
                        currencyLayout.setVisibility(View.VISIBLE);

                        if (customer_cat_.getSelectedCategory().equals("")) {
                            ErrorAlert("Select customer type.");
                        }

                    /*if(selectedAccountID.equals("32219")){
                        new_Lay.setVisibility(View.VISIBLE);
                        staffPhoneNumber.setVisibility(View.VISIBLE);
                    } else  staffPhoneNumber.setVisibility(View.GONE);*/

                        if (am.getCustomerID().length() < 10) {
                            new_Lay.setVisibility(View.VISIBLE);
                            accountNumber.setVisibility(View.GONE);
                        } else {
                            new_Lay.setVisibility(View.GONE);
                            if (customer_cat_.getSelectedCategory().equals("Existing Customer")) {
                                accountNumber.setVisibility(View.VISIBLE);
                            }
                        }

                    } catch (Exception e) {
                        am.ToastMessageLong(AccountOpenZExistingCustomersMain.this, getString(R.string.tryAgain));
                    }

                    am.putSaveSelectedAccountTypePosition(checkedId);
                });

                //BranchCode|07|BranchName|Arua~
                // BranchCode|17|BranchName|Bank Operations~
                // BranchCode|13|BranchName|Fortportal~BranchCode|10|BranchName|Gardencity~
                // BranchCode|14|BranchName|Gulu~BranchCode|99|BranchName|HeadOffice~BranchCode|16|BranchName|Jinja~BranchCode|06|BranchName|Kikuubo~BranchCode|11|BranchName|Kololo~BranchCode|12|BranchName|Lira~BranchCode|20|BranchName|Malaba~BranchCode|08|BranchName|Mbale~BranchCode|05|BranchName|Mbarara~BranchCode|18|BranchName|Najjanankumbi~BranchCode|02|BranchName|Nakasero~BranchCode|03|BranchName|Namuwongo~BranchCode|15|BranchName|Ndeeba~BranchCode|04|BranchName|Ntinda~BranchCode|09|BranchName|Ovino~BranchCode|19|BranchName|Tororo~

                String[] Branches = am.getSavedBranch().split("~");

                for (String aBranch : Branches) {
                    String[] howLong = aBranch.split("\\|");
                    String[] field_IDs = new String[howLong.length / 2];
                    String[] field_Values = new String[howLong.length / 2];
                    am.separate(aBranch, "|", field_IDs, field_Values);
                    if (!listBranchIDs.contains(am.FindInArray(field_IDs, field_Values, "BranchCode"))) {
                        listBranchIDs.add(am.FindInArray(field_IDs, field_Values, "BranchCode"));
                        listBranchNames.add(am.FindInArray(field_IDs, field_Values, "BranchName"));
                    }
                }

            /*for (int i = 0; i < branches.length(); i++) {
                JSONObject actor = branches.getJSONObject(i);
                if (!listBranchIDs.contains(actor.getString("ID"))) {
                    listBranchIDs.add(actor.getString("ID"));
                    listBranchNames.add(actor.getString("Description"));
                }
            }*/

                listBranchNames.add(0, "Select Branch");
                ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(AccountOpenZExistingCustomersMain.this, android.R.layout.simple_spinner_item, listBranchNames);
                spinnerArrayAdapterB.setDropDownViewResource(R.layout.spiner_item);
                branchselect.setAdapter(spinnerArrayAdapterB);

            } catch (Exception e) {
                am.ToastMessageLong(AccountOpenZExistingCustomersMain.this, getString(R.string.tryAgain));
            }
        }
    };

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        findViewById(R.id.contact_rao).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        front.setOnClickListener(this);
        selfie.setOnClickListener(this);
        backpick.setOnClickListener(this);
        tv_resend_otp.setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);
    }

    private void generateForms() {
        //Contact Details  field inputs
        EmailAddress = new cicEditText(this, VAR.EMAIL, "Email Address " + getString(R.string.mandatory_field), " acb@domain.com");
        PhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Phone Number ", " 722222222");
        AlternatePhoneNumber = new cicEditText(this, VAR.PHONENUMBER, " Alternative Phone Number ", " 7333333");
        ActualAddress = new cicEditText(this, VAR.TEXT, "Current Address ", " 123 Kampala");
        city = new cicEditText(this, VAR.TEXT, "City", " Kampala");
        country = new cicEditText(this, VAR.TEXT, "Name", " John Smith");
        zipCode = new cicEditText(this, VAR.TEXT, "Country code", " 256");

//        centecontacts.addView(EmailAddress);
        centecontacts.addView(PhoneNumber);
        centecontacts.addView(AlternatePhoneNumber);

        //Was for address Drop down
        /*if(am.getCountry().equals("UGANDATEST")) {
            //ActualAddress.setVisibility(View.GONE);
            getLayoutInflater().inflate(R.layout.address, centecontacts,true);
        }
        if(am.getCountry().equals("UGANDATEST")) {
            regionselect = findViewById(R.id.regionselect);
            districtName = findViewById(R.id.districtName);
            countyName = findViewById(R.id.countyName);
            subCountyName = findViewById(R.id.subCountyName);
            parishName = findViewById(R.id.parishName);
            villageName = findViewById(R.id.villageName);
            eAName = findViewById(R.id.eAName);
        }*/

        centecontacts.addView(country);
//        centecontacts.addView(zipCode);
//        centecontacts.addView(city);
//        centecontacts.addView(ActualAddress);

        country.setEnabled(true);
        zipCode.setEnabled(false);

        PhoneNumber.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (PhoneNumber.editText.getText().length() > 5) {
                    zipCode.setText(PhoneNumber.getCountryCode());

                } else {
                    zipCode.setText("");
                    country.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //ID Details
        name = new cicEditText(this, VAR.TEXT, "Given Name ", " John");
        sname = new cicEditText(this, VAR.TEXT, "Surname ", " Mwanza");
        otherNames = new cicEditText(this, VAR.TEXT, "Other Names " + getString(R.string.optional_field), " Mwanzo");
        nationalID = new cicEditText(this, VAR.TEXT, "National ID ", " CM1234568806LGB");
        DOBEdit = new cicEditText(this, VAR.TEXT, "Date of Birth  YYYY-MM-DD ", "  1990-12-31");

        name.setEnabled(false);
        sname.setEnabled(false);
        nationalID.setEnabled(false);
        DOBEdit.setEnabled(false);

        centeidsdetails.addView(name);
        centeidsdetails.addView(sname);
        centeidsdetails.addView(otherNames);
        centeidsdetails.addView(nationalID);
        centeidsdetails.addView(DOBEdit);

        //Next of kin details
        NextofKinFirstName = new cicEditText(this, VAR.TEXT, "Next of Kin First Name ", " John");
        NextofKinMiddleName = new cicEditText(this, VAR.TEXT, "Next of Kin Middle Name " + getString(R.string.optional_field), " Kawaooya");
        NextofKinLastName = new cicEditText(this, VAR.TEXT, "Next of Kin Last Name ", " Kawaooya");
        NextofKinPhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Next of Kin Phone Number " + getString(R.string.optional_field), " 72222222");
        NextofKinAltPhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Next of Kin Alternate Phone Number " + getString(R.string.optional_field), " 7333333");
        NextofKinAddress = new cicEditText(this, VAR.TEXT, "Next of Kin Adress ", " 123 Kampala");

        centenextofkin.addView(NextofKinFirstName);
        centenextofkin.addView(NextofKinMiddleName);
        centenextofkin.addView(NextofKinLastName);
        centenextofkin.addView(NextofKinPhoneNumber);
        centenextofkin.addView(NextofKinAltPhoneNumber);
        centenextofkin.addView(NextofKinAddress);

        // occupation details
        IncomeperAnnum = new cicEditText(this, VAR.AMOUNT, "Income per Annum ", "");
        EmploymentType = new cicEditText(this, VAR.TEXT, "Employment Type ", "");
        EmployerName = new cicEditText(this, VAR.TEXT, "Employer's Name ", " NWSC");
        Occupation = new cicEditText(this, VAR.TEXT, "Occupation ", " Engineer");
        PlaceofWork = new cicEditText(this, VAR.TEXT, "Place of Work ", "");
        NatureofBussiness = new cicEditText(this, VAR.TEXT, "Nature of Business/Activity Sector ", "");
        PeriodofEmployment = new cicEditText(this, VAR.AMOUNT, "Period of Employment ", " 5");

        RadioGroup.LayoutParams rp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, 1);
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (6 * density);
        rp.setMargins(margin, margin, margin, margin);

        employPeriod = new RadioGroup(this);
        employPeriod.setOrientation(RadioGroup.HORIZONTAL);
        yearsButtonE = new RadioButton(AccountOpenZExistingCustomersMain.this);
        yearsButtonE.setButtonDrawable(getResources().getDrawable(R.drawable.back_radio_button_tick));
        yearsButtonE.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            yearsButtonE.setElevation(10f);
        }
        yearsButtonE.setText(getString(R.string.years));
        yearsButtonE.setId(0);
        yearsButtonE.setTextSize(14);
        yearsButtonE.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        yearsButtonE.setPadding(20, 20, 20, 20);
        employPeriod.addView(yearsButtonE, rp);
        monthsButtonE = new RadioButton(AccountOpenZExistingCustomersMain.this);
        monthsButtonE.setButtonDrawable(getResources().getDrawable(R.drawable.back_radio_button_tick));
        monthsButtonE.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            monthsButtonE.setElevation(10f);
        }
        monthsButtonE.setText(getString(R.string.months));
        monthsButtonE.setTextSize(14);
        monthsButtonE.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        monthsButtonE.setPadding(20, 20, 20, 20);
        employPeriod.addView(monthsButtonE, rp);
        employPeriod.setOnCheckedChangeListener((group, checkedId) -> {
            if (yearsButtonE.isChecked()) {
                periodWorkString = " - Years";
            } else {
                periodWorkString = " - Months";
            }
        });
        employPeriod.check(0);

        NatureofEmployment = new cicEditText(this, VAR.TEXT, "Nature of employment ", " Contract");

        centesourceincome.addView(IncomeperAnnum);
        centesourceincome.addView(EmploymentType);
        centesourceincome.addView(EmployerName);
        centesourceincome.addView(Occupation);

        //Occupation Dropdowns
        /*EmploymentType.setVisibility(View.GONE);
        Occupation.setVisibility(View.GONE);
        getLayoutInflater().inflate(R.layout.occuption, centesourceincome,true);
        occupation_spin = findViewById(R.id.occupation_spin);
        proffession_status = findViewById(R.id.proffession_status);*/

        centesourceincome.addView(PlaceofWork);
        centesourceincome.addView(NatureofBussiness);
        centesourceincome.addView(PeriodofEmployment);
        centesourceincome.addView(employPeriod);
        centesourceincome.addView(NatureofEmployment);

        // hear us
        final TextView sstext = findViewById(R.id.sstext);
        c4 = new cicEditText(this, VAR.TEXT, "Bank Staff Name", " James Kabaku");
        c4.setVisibility(View.GONE);
        centehearusfrom.addView(c4);
        c44 = new cicEditText(this, VAR.TEXT, "Bank Staff's Branch", " Kampala");
        c44.setVisibility(View.GONE);
        centehearusfrom.addView(c44);
        c45 = new cicEditText(this, VAR.PHONENUMBER, "Customer phone number", " 733333333");
        c45.setVisibility(View.GONE);
        centehearusfrom.addView(c45);
        c5 = new cicEditText(this, VAR.TEXT, "Current Location ", " Kampala");
        centehearusfrom.addView(c5);


        final RadioGroup ssgroup = findViewById(R.id.ssgroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.tv) {
                ssgroup.setVisibility(View.GONE);
                c4.setVisibility(View.VISIBLE);
                sstext.setVisibility(View.GONE);
                c4.setLabel("TV/Radio station");
                c44.setVisibility(View.GONE);
                c45.setVisibility(View.GONE);
            } else if (checkedId == R.id.ss) {
                ssgroup.setVisibility(View.VISIBLE);
                sstext.setVisibility(View.VISIBLE);
                c4.setVisibility(View.GONE);
                c44.setVisibility(View.GONE);
                c45.setVisibility(View.GONE);
            } else if (checkedId == R.id.bankstaff) {
                ssgroup.setVisibility(View.GONE);
                sstext.setVisibility(View.GONE);
                c4.setVisibility(View.VISIBLE);
                c4.setLabel("Bank Staff Name");
                c44.setVisibility(View.VISIBLE);
                c45.setVisibility(View.GONE);
            } else if (checkedId == R.id.HFBCustomer) {
                ssgroup.setVisibility(View.GONE);
                sstext.setVisibility(View.GONE);
                c4.setVisibility(View.VISIBLE);
                c4.setLabel("Customer Name");
                c44.setVisibility(View.GONE);
                c45.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.Agent) {
                ssgroup.setVisibility(View.GONE);
                sstext.setVisibility(View.GONE);
                c4.setVisibility(View.VISIBLE);
                c4.setLabel("Agent name and number  (Name-Number)");
                c44.setVisibility(View.GONE);
                c45.setVisibility(View.GONE);
            }
        });

        //Parents details
        FatherFirstName = new cicEditText(this, VAR.TEXT, "Father's first name ", " John");
        FatherMiddleName = new cicEditText(this, VAR.TEXT, "Father's middle name ", " Kawooya");
        FatherLastName = new cicEditText(this, VAR.TEXT, "Father's last name ", " Kawooya");

        MotherFirstName = new cicEditText(this, VAR.TEXT, "Mother's first name ", " Mary");
        MotherMiddleName = new cicEditText(this, VAR.TEXT, "Mother's middle name ", " Kawooya");
        MotherLastName = new cicEditText(this, VAR.TEXT, "Mother's last name ", " Kawooya");

        Address = new cicEditText(this, VAR.TEXT, "Home District ", " Mwanza");
        YearsAtAddress = new cicEditText(this, VAR.AMOUNT, "Duration of living at that Address ", " 1");

        addressPeriod = new RadioGroup(this);
        addressPeriod.setOrientation(RadioGroup.HORIZONTAL);

        yearsButtonM = new RadioButton(AccountOpenZExistingCustomersMain.this);
        yearsButtonM.setButtonDrawable(getResources().getDrawable(R.drawable.back_radio_button_tick));
        yearsButtonM.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            yearsButtonM.setElevation(10f);
        }
        yearsButtonM.setText(getString(R.string.years));
        yearsButtonM.setId(0);
        yearsButtonM.setTextSize(14);
        yearsButtonM.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        yearsButtonM.setPadding(20, 20, 20, 20);
        addressPeriod.addView(yearsButtonM, rp);
        monthsButtonM = new RadioButton(AccountOpenZExistingCustomersMain.this);
        monthsButtonM.setButtonDrawable(getResources().getDrawable(R.drawable.back_radio_button_tick));
        monthsButtonM.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            monthsButtonM.setElevation(10f);
        }
        monthsButtonM.setText(getString(R.string.months));
        monthsButtonM.setTextSize(14);
        monthsButtonM.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        monthsButtonM.setPadding(20, 20, 20, 20);
        addressPeriod.addView(monthsButtonM, rp);
        addressPeriod.setOnCheckedChangeListener((group, checkedId) -> {
            if (yearsButtonM.isChecked()) {
                periodAddressString = " - Years";
            } else {
                periodAddressString = " - Months";
            }
        });
        addressPeriod.check(0);

        PoliticallyExposed = new cicEditText(this, VAR.TEXT, "Politically Exposed ", " Yes/No");

        centeparentinfo.addView(FatherFirstName);
        centeparentinfo.addView(FatherMiddleName);
        centeparentinfo.addView(FatherLastName);
        centeparentinfo.addView(MotherFirstName);
        centeparentinfo.addView(MotherMiddleName);
        centeparentinfo.addView(MotherLastName);
        centeparentinfo.addView(Address);
        centeparentinfo.addView(YearsAtAddress);
        centeparentinfo.addView(addressPeriod);
        centeparentinfo.addView(PoliticallyExposed);

        //Political  stuff dropdown
        /*PoliticallyExposed.setVisibility(View.GONE);
        getLayoutInflater().inflate(R.layout.political, centeparentinfo,true);
        political_spin = findViewById(R.id.political_spin);*/

//        //Existing HFB Account
//        accountNumber= new cicEditText(this, VAR.NUMBER,"Your Existing HFB Account Number" ," 01001216399");
//        accountNumber.setPadding(25,0,25,0);
//        ccg.addView(accountNumber);
//        accountNumber.setVisibility(View.GONE);

//        //STAFF  Details
//        staffPhoneNumber =  new cicEditText(this, VAR.PHONENUMBER,"Bank Staff Phone Number "+getString(R.string.mandatory_field)," 7000000001");
//        staffPhoneNumber.setPadding(25,0,25,0);
//        ccg.addView(staffPhoneNumber);
//        staffPhoneNumber.setVisibility(View.GONE);
    }


    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void flipViewIt(int i) {
        float prs = ((float) i / (float) 11) * (float) 100;
        determinateBar.setProgress((int) prs);
        switch (i) {
            case 0:
                title.setText(step0);
                break;
            case 1:
                title.setText(step1);
                WebSettings webSetting = webView.getSettings();
                webSetting.setBuiltInZoomControls(true);
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(TermsURL);
                webView.setVerticalScrollBarEnabled(true);
                webView.setHorizontalScrollBarEnabled(true);

                break;
            case 2:
                title.setText(step2);
                otpPinView.setEnabled(true);
                break;
            case 3:
                title.setText(step3);
                next.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void startStimer() {
        if (cnt != null) {
            cnt.cancel();
        }
        cnt = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                otpcountdown.setText("Resend in  " + millisUntilFinished / 1000 + " Seconds");
            }

            public void onFinish() {
                otpPinView.setText("");
                tv_resend_otp.setVisibility(View.VISIBLE);
                otpcountdown.setText("Tap to resend");
            }
        };
        cnt.start();
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(AccountOpenZExistingCustomersMain.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(AccountOpenZExistingCustomersMain.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountOpenZExistingCustomersMain.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            AccountOpenZExistingCustomersMain.this.openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    //OCR request with ID image
    private void submitImages() {
        alertLoading("");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://craftsiliconai.azurewebsites.net/IdImages.aspx",
                response -> {
                    removeDialogs();
                    ImagePickerActivity.clearCache(AccountOpenZExistingCustomersMain.this);
                    if (response.contains("{")) {
                        int spacesToIndentEachLevel = 2;
                        String formattedJson = "";
                        try {
                            formattedJson = new JSONObject(response).toString(spacesToIndentEachLevel);
                        } catch (Exception e) {
                            formattedJson = response;
                        }

                        try {
                            JSONObject json = new JSONObject(Toa(formattedJson));
                            ugandanID ug = new ugandanID(json);

                            if (ug.isUgandanID()) {
                                // TODO: 26/11/2021 added condirions to check empty
                                String[] nameSplit = ug.getGivenNameNew().split(" ");
                                if (nameSplit.length > 1) {
                                    if (nameSplit[0].isEmpty()) {
                                        am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.unrecognized_ID), getString(R.string.sure_clear));
                                    } else {
                                        name.setText(nameSplit[0]);
                                        otherNames.setText(nameSplit[1]);
                                        // TODO: 1/10/2022
                                        otherNames.setEnabled(false);
                                    }
                                } else {
                                    if (ug.getGivenNameNew().isEmpty()) {
                                        am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.unrecognized_ID), getString(R.string.sure_clear));
                                    } else {
                                        name.setText(ug.getGivenNameNew());
                                    }
                                }

                                if (ug.getNIN().isEmpty() || ug.getSurNameNew().isEmpty() || ug.getDOBNew().isEmpty()) {
                                    am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.unrecognized_ID), getString(R.string.sure_clear));
                                } else {
                                    sname.setText(ug.GetSurName());
                                    
                                    nationalID.setText(ug.getNIN());
                                    DOBEdit.setText(ug.getDOBNew());
                                    otherNames.setText(ug.getGivenName());

                                    flipper.showNext();
                                    step_++;
                                    flipViewIt(step_);
                                }
                            } else {
                                am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.unrecognized_ID), getString(R.string.sure_clear));
                            }
                        } catch (Exception e) {
                            am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.alert), getString(R.string.sure_clear));
                        }
                    } else {
                        am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.alert), getString(R.string.tryAgain));
                    }

                },
                error -> {
                    removeDialogs();
                    am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.alert), getString(R.string.connectionError));
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("IDType", "");
                params.put("IDNumber", "");
                params.put("IDFront", "" + encodedImageFront);
                params.put("IDBack", "" + encodedImageBack);
                params.put("Selfie", "" + encodedImageSelfie);
                params.put("UserID", "SC");
                params.put("Password", "R00PK1RANI");
                //IDFront,IDBack,Selfie
                //contants.LogThis(params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        callServer(stringRequest);
    }

    public static String ConvertImageToBase64(Bitmap bitmap) {
        String generatedBase64 = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream2);
            byte[] bytes2 = byteArrayOutputStream2.toByteArray();
            generatedBase64 = Base64.encodeToString(bytes2, Base64.DEFAULT);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return generatedBase64;
    }

    public static int getBatteryPercentage(Context context) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
        float batteryPct = level / (float) scale;
        return (int) (batteryPct * 100);
    }

    static public Bitmap BitmapCompressionWithZ(Activity activity, File mFile) {
        Bitmap bmp = null;
        try {
            bmp = new Compressor(activity)
                    /*.setMaxWidth(291)
                    .setMaxHeight(218)*/
                    .setMaxWidth(150)
                    .setMaxHeight(112)
                    .setQuality(20)
                    .compressToBitmap(mFile);
        } catch (Exception e) {
            Log.e("error-whenusinglib", "" + e);
        }

        if (bmp == null) {
            Log.e("", "Bitmap not found");
        }

        return bmp;
    }

    public static String sizeOf(Bitmap bitmap) {
        double mbs = 0;
        if (bitmap == null) {
            mbs = 0;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mbs = bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            mbs = bitmap.getByteCount();
        } else {
            mbs = bitmap.getRowBytes() * bitmap.getHeight();
        }
        //return android.text.format.Formatter.formatFileSize(getActivity(), mbs);
        return Double.toString(mbs / 1024);
    }

    private void removeDialogs() {
        am.progressDialog("0");
    }

    private void alertLoading(String message) {
        am.progressDialog("1");
    }

    public void ErrorAlert(String Message) {
        am.myDialog(this, getString(R.string.alert), Message);
    }

    private void callServer(StringRequest stringRequest) {

        MS.getInstance(this).addToRequestQueue(stringRequest);

    }

    public String Toa(String str) {
        str = str.substring(0, str.length() - 2);
        return str + "\"B-0\":\"RIGHT THUMB\"" + "}";
    }

    private void getLocationAddress(Location location) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + ","
                + location.getLongitude() + "&key=AIzaSyDhZlL-z0dTCANCHwHSHbNQYnG96phvQ0c";

        RequestQueue queue = Volley.newRequestQueue(AccountOpenZExistingCustomersMain.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (!TextUtils.isEmpty(response)) {
                        //c5.setText(result);
                        String formatted_address = null;
                        try {
                            JSONObject json = new JSONObject(response);
                            //G1.log("googlemapsurl:json1", "" + json.toString());
                            JSONArray jArray = json.getJSONArray("results");
                            formatted_address = jArray.getJSONObject(0).getString("formatted_address");


                            //get neighborhood
                            JSONArray results = json.getJSONArray("results");
                            JSONObject rec = results.getJSONObject(0);
                            JSONArray address_components = rec.getJSONArray("address_components");
                            for (int i = 0; i < address_components.length(); i++) {
                                JSONObject rec1 = address_components.getJSONObject(i);
                                //trace(rec1.getString("long_name"));
                                JSONArray types = rec1.getJSONArray("types");
                                String comp = types.getString(0);
                            }

                            c5.setText(formatted_address);

                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }


                    }
                }, error -> {

        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    public void RegisterNewListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(aVoid -> registerReceiver(onBroadcastSMSReceived, new IntentFilter("SMSReceived")));
    }

    private final BroadcastReceiver onBroadcastSMSReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            try {
                String theSms = i.getStringExtra("SMSbody");
                String status = i.getStringExtra("STATUS");
                assert status != null;
                if ("000".equals(status)) {
                    assert theSms != null;
                    otpPinView.setText(theSms.trim());
                } else {
                    Toast.makeText(AccountOpenZExistingCustomersMain.this, theSms, Toast.LENGTH_LONG).show();
                    unregisterReceiver(onBroadcastSMSReceived);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    File mFile = new File(uri.getPath());
                    switch (REQUEST_IMAGEX) {
                        case 1:
                            front.setImage(bitmap);
                            bitmapImageFront = BitmapCompressionWithZ(this, mFile);
                            encodedImageFront = ConvertImageToBase64(bitmap);
                            am.putSavedData("encodedImageFront", encodedImageFront);
                            break;
                        case 2:
                            selfie.setImage(bitmap);
                            bitmapImageSelfie = BitmapCompressionWithZ(this, mFile);
                            encodedImageSelfie = ConvertImageToBase64(bitmap);
                            am.putSavedData("encodedImageSelfie", encodedImageSelfie);
                            break;
                        case 3:
                            backpick.setImage(bitmap);
                            bitmapImageBack = BitmapCompressionWithZ(this, mFile);
                            encodedImageBack = ConvertImageToBase64(bitmap);
                            am.putSavedData("encodedImageBack", encodedImageBack);
                            break;
                        default:
                            selfie.setImage(bitmap);
                            break;
                    }
                    ImagePickerActivity.clearCache(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_ID_AIRLOCATION) {
            airLocation.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void getCustomParam(String paramName, String task) {
        am.get(this, "FORMID:O-GETCUSTOMPARAMS:" +
                "PARAMETERNAME:" + paramName + ":" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), task);

    }

    private void checkCustomerExists(String checkNum, String nom) {
        if (nom.equals("1")) {
            currentTask = "VerifyExisting";
        } else if (nom.equals("2")) {
            currentTask = "VerifyExistingAlt";
        } else {
            currentTask = "ValidateBankStaff";
        }
        am.get(this, "FORMID:O-CheckCustomerExists:" +
                "CUSTOMERMOBILENUMBER:" + checkNum + ":" +
                "PRODUCTID:" + selectedAccountID + ":" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), currentTask);
    }

    private void getPersonalDetailsExisting(String task) {
        am.get(this, "FORMID:O-GetCustomerPersonalDetails:" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), task);
    }

    private void validateExisting(String existingAccount) {
        am.get(this, "FORMID:M-:" +
                // TODO: 2/2/2022 on Live
                //"MERCHANTID:VALIDATEACCOUNTBANK:" +
                "MERCHANTID:VALIDATEACCOUNT:" +
                "ACCOUNTID:" + existingAccount + ":" +
                "ACTION:GETNAME:", getString(R.string.validating), "VAL_EXIST");
    }

    private void cardDetailsExisting(String accNum, String cardNum, String auth, String checkNum) {
        currentTask = "cardDetailsExisting";
        am.get(this, "FORMID:B-:MERCHANTID:SELFREG:" +
                "BANKACCOUNTID:" + accNum + ":" +
                "INFOFIELD1:" + cardNum.replaceAll("\\s+", "") + ":" +
                "INFOFIELD2:" + auth + ":" +
                "INFOFIELD3:" + checkNum + ":" +
                //"INFOFIELD5:DEVICEMISMATCH:" +
                "INFOFIELD6:" + am.getIMEI() + ":" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), currentTask);
    }

    private void getPoliticalExposed() {
        currentTask = "FetchPolitics";
        am.get(this, "FORMID:O-GetStaticData:" +
                "SYSTEMCODE:" + "CSPEP" + ":" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), currentTask);
    }

    private void getAddressParam(String paramName, String task, String paramValue) {
        currentTask = task;
        am.get(this, "FORMID:O-GETCUSTOMADDRESS:" +
                "PARAMETERNAME:" + paramName + ":" +
                "PARAMVALUE:" + paramValue + ":" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), currentTask);
    }

    public String RAO() {
        return (
                "FORMID:M-:" +
                        "MERCHANTID:SELFRAO:" + INFOFIELD1 + ":" + INFOFIELD2 + ":" + INFOFIELD3 + ":" + INFOFIELD4 + ":" + INFOFIELD5 + ":" +
                        "INFOFIELD6:" + "N/A" + ":" +
                        "INFOFIELD7:" + "N/A" + ":" +
                        "INFOFIELD8:" + "N/A" + ":" +
                        "INFOFIELD9:" + "N/A" + ":" +
                        "BANKACCOUNTID:" + am.getCustomerID() + ":" +
                        "ACTION:GETNAME:"
        );
    }

    private void createOTP() {
        try {
            new Handler().postDelayed(() -> {
                raoOTP = "";
                String customerMobilenNumber = "";
                if (selectedAccountID.equals("32219")) {
                    customerMobilenNumber = staffPhoneNumber.getCountryCode() + staffPhoneNumber.getText();
                } else {
                    customerMobilenNumber = PhoneNumber.getCountryCode() + PhoneNumber.getText();
                }
                new_request = "FORMID:O-OTPCREATE:" +
                        "SERVICENAME:SELFRAO:" +
                        "BANKID:" + am.getBankID() + ":" +
                        "CUSTOMERMOBILENUMBER:" + customerMobilenNumber + ":" +
                        "EMAILID:" + "test@test.com" + ":";

                am.get(AccountOpenZExistingCustomersMain.this, new_request, getString(R.string.loading), currentTask);
                /*if(PhoneNumber.getCountryCode().equals("256")){
                    otpPinView.setEnabled(false);
                } else {
                    otpPinView.requestFocus();
                }*/
                otpPinView.setEnabled(true);
                otpPinView.requestFocus();
            }, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verify() {
        if (cnt != null) {
            cnt.cancel();
        }
        currentTask = "VerifyOTP";
        String customerMobilenNumber = "";
        if (selectedAccountID.equals("32219")) {
            customerMobilenNumber = staffPhoneNumber.getCountryCode() + staffPhoneNumber.getText();
        } else {
            customerMobilenNumber = PhoneNumber.getCountryCode() + PhoneNumber.getText();
        }
        new_request = "FORMID:O-OTPVERIFY:" +
                "SERVICENAME:SELFRAO:" +
                "BANKID:" + am.getBankID() + ":" +
                "CUSTOMERMOBILENUMBER:" + customerMobilenNumber + ":" +
                "OTPKEY:" + raoOTP + ":" +
                "EMAILID:" + "test@gmail.com" + ":";
        am.get(AccountOpenZExistingCustomersMain.this, new_request, getString(R.string.loading), currentTask);
    }

    private void accountBranchChoice() {
        final Dialog mDialog = new Dialog(AccountOpenZExistingCustomersMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_info);
        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title),
                txtMessage = mDialog.findViewById(R.id.dialog_message),
                txtOk = mDialog.findViewById(R.id.dialog_BTN);
        txtTitle.setText(selectedAccount);
        txtMessage.setText(String.format("Currency   %s\n\nBranch   %s", currName, branchselect.getSelectedItem().toString().trim()));
        txtOk.setOnClickListener(v1 -> {
            step_++;
            flipViewIt(step_);
            startActivity(new Intent(this, Login.class));
//            flipper.showNext();
            mDialog.dismiss();
        });
        mDialog.show();
    }

    private void processData(String response) {
        String[] howlong = response.split(":");
        String RequestDetails = response;
        FieldIDs = new String[howlong.length / 2];
        FieldValues = new String[howlong.length / 2];
        try {
            String Output = "";
            int i = 0, j = 0;

            while (RequestDetails.length() > 0) {
                i = RequestDetails.indexOf(':');
                Output = RequestDetails.substring(0, i);
                FieldIDs[j] = Output;
                RequestDetails = RequestDetails.substring(i + 1);
                i = RequestDetails.indexOf(':');
                if (i < 0) {
                    Output = RequestDetails;
                    RequestDetails = "";
                } else {
                    Output = RequestDetails.substring(0, i);
                    RequestDetails = RequestDetails.substring(i + 1);
                }
                FieldValues[j] = Output;
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processDataPipe(String response) {
        String[] howlong = response.split("\\|");
        String RequestDetails = response;
        FieldIDs = new String[howlong.length / 2];
        FieldValues = new String[howlong.length / 2];
        try {
            String Output = "";
            int i = 0, j = 0;
            while (RequestDetails.length() > 0) {
                i = RequestDetails.indexOf('|');
                Output = RequestDetails.substring(0, i);
                FieldIDs[j] = Output;
                RequestDetails = RequestDetails.substring(i + 1);
                i = RequestDetails.indexOf('|');
                if (i < 0) {
                    Output = RequestDetails;
                    RequestDetails = "";
                } else {
                    Output = RequestDetails.substring(0, i);
                    RequestDetails = RequestDetails.substring(i + 1);
                }
                FieldValues[j] = Output;
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(onBroadcastSMSReceived);
            unregisterReceiver(populateProducts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onResponse(String response, String step) {
        processData(response);
        Log.e("Agnes", response);
        String Status = am.FindInArray(FieldIDs, FieldValues, "STATUS");
        String Message = am.FindInArray(FieldIDs, FieldValues, "MESSAGE");
        if (TextUtils.isEmpty(Message)) {
            Message = am.FindInArray(FieldIDs, FieldValues, "DATA");
        }
        if (TextUtils.isEmpty(Status) && step.equals("RequestOTP")) {
            if (response.equals("OTP Created Successfully.")) {

                if (step_ == 1 /*&& tv_resend_otp.getVisibility()==View.GONE*/) {
                    step_++;
                    flipViewIt(step_);
                    flipper.showNext();
                    startStimer();
                }
                Toast.makeText(AccountOpenZExistingCustomersMain.this, response, Toast.LENGTH_LONG).show();
              
            }
        }
        if (TextUtils.isEmpty(Status) && step.equals("ResentRequestOTP")) {
            if (response.equals("OTP Created Successfully.")) {
                    startStimer();
                    Toast.makeText(AccountOpenZExistingCustomersMain.this, response, Toast.LENGTH_LONG).show();

            }
        }
        if (TextUtils.isEmpty(Status) && step.equals("VerifyOTP")) {
            if (response.equals("SUCCESS|NAME|")) {
                currentTask = "RAO";
                new_request = RAO();
                new Handler().postDelayed(() -> am.get(AccountOpenZExistingCustomersMain.this, new_request, getString(R.string.loading), currentTask), 400);


            }
        }
        ///TOA PLZ
        
        if (TextUtils.isEmpty(Message)) {
            if(step.equals("RequestOTP")||step.equals("ResentRequestOTP")||step.equals("VerifyOTP")) {
                   Log.d("Condition","OK") ;
            }else{
                Message = getString(R.string.tryAgain);
                Toast.makeText(AccountOpenZExistingCustomersMain.this, Message, Toast.LENGTH_LONG).show();
            }
         
        }
       
        else {
            switch (Status) {
                case "000":
                case "OK":
                case "00":
                    switch (step) {
                        case "ValidateBankStaff":
                            accountBranchChoice();
                            break;
                        case "FetchPolitics": {
                            //STATUS:OK:DATA:10-MBR OF COURT OF AUDITORS OF CENTRAL BANK,11-MEMBER OF BOARD OF A CENTRAL BANK,12-AMBASSADORS,13-CHARGES Dâ€™AFFAIRES,14-HIGH-RANKING OFFICER IN ARMED FORCES,15-HIGH-RANKING OFFICER IN POLICE FORCES,16-BOARD MEMBER OF STATE-OWNED ENTERPRISE,17-BOARD MEMBER OF GOVT PARASTATAL,18-SPOUSE/PARTNER OF PEP,19-BENEFICIARY OF PEP,1-NO,20-SON OR DAUGHTER OF PEP,21-FATHER OR MOTHER OF PEP,22-SPOUSE/PARTNER OF SON/DAUGHTER OF PEP,23-PARTNER/DIRECTOR OF A LEGAL ENTERPRISE,24-CLOSE BUSINESS RELATION WITH PEP,2-HEADS OF STATE,3-HEAD OF GOVT ENTITY,4-CABINET MINISTER,5-DEPUTY OR ASSISTANT CABINET MINISTER,6-MEMBER OF PARLIAMENT,7-MEMBER OF SUPREME COURT,8-MEMBER OF CONSTITUTIONAL COURT,9-MEMBER OTHER HIGH LEVEL JUDICIAL
                            String[] politicsCategories = Message.split(",");
                            for (String oneCategory : politicsCategories) {
                                politicsIDs.add(oneCategory.split("-")[0]);
                                politicsNames.add(oneCategory.split("-")[1]);
                            }
                            politicsIDs.add(0, "Select One Category");
                            politicsNames.add(0, "Select One Category");
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, politicsNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            political_spin.setAdapter(spinnerArrayAdapterB);
                            political_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    StringPoliticallyExposed = politicsIDs.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchOccupation": {
                            String[] regions = Message.split("~");
                            for (String aRegion : regions) {
                                processDataPipe(aRegion);
                                occupationIds.add(am.FindInArray(FieldIDs, FieldValues, "CODE"));
                                occupationNames.add(am.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, occupationNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            occupation_spin.setAdapter(spinnerArrayAdapterB);
                            occupation_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    occupationIDString = occupationIds.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            new Handler().postDelayed(() -> {
                                if (am.getCountry().equals("UGANDATEST"))
                                    getCustomParam("PROFFESSIONSTATUS", "FetchProfession");
                            }, 400);
                            break;
                        }
                        case "FetchProfession": {
                            String[] regions = Message.split("~");
                            for (String aRegion : regions) {
                                processDataPipe(aRegion);
                                professionIds.add(am.FindInArray(FieldIDs, FieldValues, "CODE"));
                                professionNames.add(am.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, professionNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            proffession_status.setAdapter(spinnerArrayAdapterB);
                            proffession_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    professionIDString = professionIds.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchRegion": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"Region":"NORTH"},{"Region":"EAST"},{"Region":"WEST"},{"Region":"CENTRAL"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                regionsIds.add(0, "Select One");
                                regionNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    regionsIds.add(String.valueOf(i));
                                    regionNames.add(actor.getString("Region"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*String [] regions = Message.split("~");
                            regionsIds.add(0,"Select One");
                            regionNames.add(0,"Select One");
                            for (String aRegion : regions) {
                                processDataPipe(aRegion);
                                regionsIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                regionNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, regionNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            regionselect.setAdapter(spinnerArrayAdapterB);
                            regionselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    regionIDString = regionsIds.get(position);
                                    if (position != 0) {
                                        districtIds.clear();
                                        districtNames.clear();
                                        getAddressParam("DISTRICTNAME", "FetchDistrict", regionselect.getSelectedItem().toString().trim());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchDistrict": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"DistrictName":"AGAGO"},{"DistrictName":"MOROTO"},{"DistrictName":"AMOLATAR"},{"DistrictName":"OTUKE"},{"DistrictName":"DOKOLO"},{"DistrictName":"NAKAPIRIPIRIT"},{"DistrictName":"AMUDAT"},{"DistrictName":"LUUKA"},{"DistrictName":"LIRA"},{"DistrictName":"NWOYA"},{"DistrictName":"PADER"},{"DistrictName":"LAMWO"},{"DistrictName":"KAABONG"},{"DistrictName":"AMURIA"},{"DistrictName":"KITGUM"},{"DistrictName":"KOLE"},{"DistrictName":"KOBOKO"},{"DistrictName":"ALEBTONG"},{"DistrictName":"ABIM"},{"DistrictName":"GULU"},{"DistrictName":"ARUA"},{"DistrictName":"OYAM"},{"DistrictName":"APAC"},{"DistrictName":"MARACHA"},{"DistrictName":"KOTIDO"},{"DistrictName":"ADJUMANI"},{"DistrictName":"NAPAK"},{"DistrictName":"ZOMBO"},{"DistrictName":"YUMBE"},{"DistrictName":"MOYO"},{"DistrictName":"AMURU"},{"DistrictName":"NEBBI"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                districtIds.add(0, "Select One");
                                districtNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    if (!districtNames.contains(actor.getString("DistrictName"))) {
                                        districtIds.add(String.valueOf(i));
                                        districtNames.add(actor.getString("DistrictName"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*String [] districts = Message.split("~");
                            districtIds.add(0,"Select One");
                            districtNames.add(0,"Select One");
                            for (String aDistrict : districts) {
                                processDataPipe(aDistrict);
                                districtIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                districtNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, districtNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            districtName.setAdapter(spinnerArrayAdapterB);
                            districtName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    districtIDString = districtIds.get(position);
                                    if (position != 0) {
                                        countyIds.clear();
                                        countylistNames.clear();
                                        getAddressParam("COUNTYNAME", "FetchCounty", districtName.getSelectedItem().toString().trim());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchCounty": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"CountyName":"BUKOTO"},{"CountyName":"MASAKA MUNICIPALITY"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                countyIds.add(0, "Select One");
                                countylistNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    if (!countylistNames.contains(actor.getString("CountyName"))) {
                                        countyIds.add(String.valueOf(i));
                                        countylistNames.add(actor.getString("CountyName"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*String [] counties = Message.split("~");
                            countyIds.add(0,"Select One");
                            countylistNames.add(0,"Select One");
                            for (String aCounty : counties) {
                                processDataPipe(aCounty);
                                countyIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                countylistNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, countylistNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            countyName.setAdapter(spinnerArrayAdapterB);
                            countyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    countyIDString = countyIds.get(position);
                                    if (position != 0) {
                                        subcountyIds.clear();
                                        subcountylistNames.clear();
                                        getAddressParam("SUBCOUNTYNAME", "FetchSubCounty", countyName.getSelectedItem().toString().trim());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchSubCounty": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"CountyName":"BUKOTO"},{"CountyName":"MASAKA MUNICIPALITY"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                subcountyIds.add(0, "Select One");
                                subcountylistNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    if (!subcountylistNames.contains(actor.getString("SubCountyName"))) {
                                        subcountyIds.add(String.valueOf(i));
                                        subcountylistNames.add(actor.getString("SubCountyName"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           /* String [] counties = Message.split("~");
                            for (String aCounty : counties) {
                                processDataPipe(aCounty);
                                subcountyIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                subcountylistNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, subcountylistNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            subCountyName.setAdapter(spinnerArrayAdapterB);
                            subCountyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    subcountyIDString = subcountyIds.get(position);
                                    if (position != 0) {
                                        parishIds.clear();
                                        parishlistNames.clear();
                                        getAddressParam("PARISHNAME", "FetchParishName", subCountyName.getSelectedItem().toString().trim());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchParishName": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"ParishName":"KAMATURU"},{"ParishName":"NARISAE"},{"ParishName":"NATHINYONOIT"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                parishIds.add(0, "Select One");
                                parishlistNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    if (!parishlistNames.contains(actor.getString("ParishName"))) {
                                        parishIds.add(String.valueOf(i));
                                        parishlistNames.add(actor.getString("ParishName"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*String [] parish = Message.split("~");
                            for (String oneParish : parish) {
                                processDataPipe(oneParish);
                                parishIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                parishlistNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, parishlistNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            parishName.setAdapter(spinnerArrayAdapterB);
                            parishName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    parishIdString = parishIds.get(position);
                                    if (position != 0) {
                                        villageIds.clear();
                                        villagelistNames.clear();
                                        getAddressParam("VILLAGENAME", "FetchVillageName", parishName.getSelectedItem().toString().trim());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchVillageName": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"VillageName":"LOKWAMORU"},{"VillageName":"NANGAMIT"},{"VillageName":"NAOI"},{"VillageName":"NAOI SPECIAL AREA"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                villageIds.add(0, "Select One");
                                villagelistNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    if (!villagelistNames.contains(actor.getString("VillageName"))) {
                                        villageIds.add(String.valueOf(i));
                                        villagelistNames.add(actor.getString("VillageName"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*String [] villages = Message.split("~");
                            for (String aVillage : villages) {
                                processDataPipe(aVillage);
                                villageIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                villagelistNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, villagelistNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            villageName.setAdapter(spinnerArrayAdapterB);
                            villageName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    villageIdString = villageIds.get(position);
                                    if (position != 0) {
                                        eAIds.clear();
                                        eAlistNames.clear();
                                        getAddressParam("EANAME", "FetchEaName", villageName.getSelectedItem().toString().trim());
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
                        case "FetchEaName": {
                            try {
                                //STATUS:OK:DATA:{"Satatus":"00","CenteAddressCodes":[{"AddressCode":"25134","EAName":"OLYERAI"}]}
                                JSONObject res = new JSONObject(response.replace("STATUS:OK:DATA:", ""));
                                JSONArray addressRegionArray = res.getJSONArray("CenteAddressCodes");
                                eAIds.add(0, "Select One");
                                eAlistNames.add(0, "Select One");
                                for (int i = 0; i < addressRegionArray.length(); i++) {
                                    JSONObject actor = addressRegionArray.getJSONObject(i);
                                    if (!eAIds.contains(actor.getString("AddressCode"))) {
                                        eAIds.add(actor.getString("AddressCode"));
                                        eAlistNames.add(actor.getString("EAName"));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*String [] villages = Message.split("~");
                            for (String aVillage : villages) {
                                processDataPipe(aVillage);
                                eAIds.add(constants.FindInArray(FieldIDs, FieldValues, "CODE"));
                                eAlistNames.add(constants.FindInArray(FieldIDs, FieldValues, "NAME"));
                            }*/
                            ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(this, R.layout.spinner_item, eAlistNames);
                            spinnerArrayAdapterB.setDropDownViewResource(R.layout.spinner_item);
                            eAName.setAdapter(spinnerArrayAdapterB);
                            eAName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position != 0) {
                                        eAIdString = eAIds.get(position);
                                    } else {
                                        eAIdString = "";
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            break;
                        }
//                        case "RequestOTP":
//                            if (step_ == 2 /*&& tv_resend_otp.getVisibility()==View.GONE*/) {
//                                step_++;
//                                flipViewIt(step_);
//                                flipper.showNext();
//                                startStimer();
//                            }
//                            Toast.makeText(AccountOpenZExistingCustomersMain.this, Message, Toast.LENGTH_LONG).show();
//                            break;
//                        case "ResentRequestOTP":
//                            startStimer();
//                            Toast.makeText(AccountOpenZExistingCustomersMain.this, Message, Toast.LENGTH_LONG).show();
//                            break;
//                        case "VerifyOTP": {
//                            currentTask = "RAO";
//                            new_request = RAO();
//                            new Handler().postDelayed(() -> am.get(AccountOpenZExistingCustomersMain.this, new_request, getString(R.string.loading), currentTask), 400);
//                            break;
//                        }
                        case "VerifyExisting":
                            am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.alert), getString(R.string.exists));
                            break;
                        case "VerifyExistingAlt":
                            am.myDialog(AccountOpenZExistingCustomersMain.this, getString(R.string.alert), getString(R.string.already_exists));
                            break;
                        case "VAL_EXIST":

                            break;
                        case "GetCustomerPersonal":
                            //STATUS:OK:DATA:IDNumber|22637574|EmailID|samuel.mutua@craftsilicon.com|TypeOfID|National ID
                            step_++;
                            flipViewIt(step_);
                            flipper.showNext();
                            break;
                        case "RAO":
                            show_response.setText(Message);
                            success_failed.setVisibility(View.VISIBLE);
                            //STATUS:000:MESSAGE:Dear Customer We have received your application, we shall respond with the status of your Application with in 24hrs call 0800200555/0800335344 for more info or send an email to cente_mobile@centenarybank.co.ug.~You can Deposit to this Account Using;~
                            //Wallet to Bank Transactions (Momo and Airtel Money)| url~ ATM Deposit| url~ Agent Banking| url~T.Ts| url~Wire Transfer| url~Western Union| url~At any HFB Bank Branch countrywide (Uganda).| url
                            break;
                        default:
                            Toast.makeText(AccountOpenZExistingCustomersMain.this, Message, Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
                default:
                    switch (step  /*currentTask*/) {
                        case "ValidateBankStaff":
                            ErrorAlert("This service is not Available, Please contact the Bank");
                            break;
                        case "FetchPolitics":
                        case "FetchOccupation":
                        case "FetchProfession":
                        case "FetchRegion":
                        case "FetchDistrict":
                        case "FetchCounty":
                        case "FetchSubCounty":
                        case "FetchParishName":
                        case "FetchVillageName":
                        case "FetchEaName":
                            Toast.makeText(AccountOpenZExistingCustomersMain.this, Message, Toast.LENGTH_LONG).show();
                            break;
                        case "VerifyExisting":
                            new Handler().postDelayed(() -> checkCustomerExists(AlternatePhoneNumber.getCountryCode() + AlternatePhoneNumber.getText(), "2"), 300);
                            break;
                        case "VerifyExistingAlt":
                            step_++;
                            flipViewIt(step_);
                            flipper.showNext();
                            break;
                        case "GetCustomerPersonal":
                            am.myDialog(this, getString(R.string.alert), Message);
                            break;
                        default:
                            am.myDialog(this, getString(R.string.alert), Message);
                            break;
                    }
                    break;
            }
        }
    }

    public void proceedFromWhereYouLeftOff() {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.exitapp);

        Button yes = dialog.findViewById(R.id.yes);
        TextView title = dialog.findViewById(R.id.title);
        Button no = dialog.findViewById(R.id.no);
        TextView phonelabel = dialog.findViewById(R.id.phonelabel);

        title.setText("Procceed From where you left off ?");
        phonelabel.setText(" tap Yes to proceed or No to start again");

        yes.setOnClickListener(v -> {
            step_ = am.getSavedPreviousStep();
            if (step_ > 0) {
                prev.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < step_; i++) {
                flipper.showNext();
            }

            for (int i = 0; i <= step_; i++) {
                try {
                    switch (i) {
                        case 0:
                            if (am.getCustomerCategory() == R.id.new_Lay) {
                                new_Lay.performClick();
                            } else {
                                existing_lay.performClick();
                            }
                            accountsGroup.check(am.getAccountTypePosition());
                            branchselect.setSelection(am.getBranchPosition());
                            currselect.setSelection(am.getCurrencyPosition());
                            scrollAccounts.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (am.getAccountTypePosition() > 2 && am.getAccountTypePosition() < 4) {
                                        scrollAccounts.scrollTo(accountsGroup.getWidth() / 2, 0);
                                    } else if (am.getAccountTypePosition() >= 4) {
                                        scrollAccounts.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                                    }
                                }
                            });
                            currName = currselect.getSelectedItem().toString().trim();
                            branchID = listBranchIDs.get(branchselect.getSelectedItemPosition());
                            staffPhoneNumber.citizenship.setSelection(am.getBankStaffCountryPosition());
                            break;
                        case 1:
                            PhoneNumber.citizenship.setSelection(am.getCustomerCountryPosition());
                            AlternatePhoneNumber.citizenship.setSelection(am.getAlterCustomerCountryPosition());
                            break;
                        case 2:
                            Integer titlePosition = am.getTitlePosition();
                            switch (titlePosition) {
                                case R.id.aMr:
                                    aMr.performClick();
                                    break;
                                case R.id.bMrs:
                                    bMrs.performClick();
                                    break;
                                case R.id.cMiss:
                                    cMiss.performClick();
                                    break;
                            }
                            if (!am.getSavedData("encodedImageFront").equals("")) {
                                encodedImageFront = am.getSavedData("encodedImageFront");
                                Glide.with(AccountOpenZExistingCustomersMain.this).load(Base64.decode(encodedImageFront, Base64.DEFAULT)).into(front.imageView);
                            }
                            if (!am.getSavedData("encodedImageSelfie").equals("")) {
                                encodedImageSelfie = am.getSavedData("encodedImageSelfie");
                                Glide.with(AccountOpenZExistingCustomersMain.this).load(Base64.decode(encodedImageSelfie, Base64.DEFAULT)).into(selfie.imageView);
                            }
                            if (!am.getSavedData("encodedImageBack").equals("")) {
                                encodedImageBack = am.getSavedData("encodedImageBack");
                                Glide.with(AccountOpenZExistingCustomersMain.this).load(Base64.decode(encodedImageBack, Base64.DEFAULT)).into(backpick.imageView);
                            }
                            break;
                        case 3:
                            radiob.setChecked(true);
                            break;
                        case 4:
                            NextofKinPhoneNumber.citizenship.setSelection(am.getNextofKinCountryPosition());
                            NextofKinAltPhoneNumber.citizenship.setSelection(am.getAltNextofKinCountryPosition());
                            break;
                        case 5:
                            if (am.getPeriodEmployment() == 0) {
                                yearsButtonE.setChecked(true);
                            } else {
                                monthsButtonE.setChecked(true);
                            }
                            break;

                        case 6:
                            try {
                                radioGroup.check(am.getMediumRadio());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        /*case 8:
                            if(!selectedAccountID.equals("32219")){
                                chkMobileBanking.setChecked(true);
                                chkAgencyBanking.setChecked(true);
                            }
                            break;*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /*if (politicsIDs.isEmpty()) {
                if(am.getCountry().equals("UGANDATEST")) getPoliticalExposed();
            }*/

            /*new Handler().postDelayed(() -> {
                if (occupationIds.isEmpty()) {
                    if(am.getCountry().equals("UGANDATEST")) getCustomParam("OCCUPATION","FetchOccupation");
                }
            },5000);*/

            //Was Address dropdown
            /* new Handler().postDelayed(() -> {
                if (regionsIds.isEmpty()) {
                    if(am.getCountry().equals("UGANDATEST")) getAddressParam("REGION","FetchRegion", "");
                }
            },10000);*/

            flipViewIt(step_);

            dialog.dismiss();
        });

        no.setOnClickListener(v -> {
            am.putProceed(false);
            radiob.setChecked(false);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (done) {
            super.onBackPressed();
        } else {
            final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.exitapp);

            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);

            yes.setOnClickListener(v -> {

                if (step_ == 0) {
                    am.putSaveCustomerCategory(customer_cat_.getSelectedCatID());
                    am.putSaveSelectedBranchPosition(branchselect.getSelectedItemPosition());
                    am.putSaveSelectedCurrencyPosition(currselect.getSelectedItemPosition());
                    am.putBankStaffCountryPosition(staffPhoneNumber.citizenship.getSelectedItemPosition());
                } else if (step_ == 1) {
                    am.putSaveCustomerCountryPosition(PhoneNumber.citizenship.getSelectedItemPosition());
                    am.putSaveAlterCustomerCountryPosition(AlternatePhoneNumber.citizenship.getSelectedItemPosition());
                } else if (step_ == 2) {
                    am.putSaveTitlePosition(singleTitle.getViewTitleID());
                } else if (step_ == 3) {
                    am.putSaveNextofKinCountry(NextofKinPhoneNumber.citizenship.getSelectedItemPosition());
                    am.putSaveAltNextofKinCountry(NextofKinAltPhoneNumber.citizenship.getSelectedItemPosition());
                } else if (step_ == 4) {
                    am.putSavePeriodEmployment(employPeriod.getCheckedRadioButtonId());
                } else if (step_ == 5) {
                    am.putMediumRadio(radioGroup.getCheckedRadioButtonId());
                }
                am.putSavedPreviousStep(step_);
                am.putProceed(true);
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            });

            no.setOnClickListener(v -> {
                am.putProceed(false);
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            });

            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact_rao:
                startActivity(new Intent(this, Contact_Us.class));
                break;
            case R.id.close:
                if (done) {
                    finish();
                } else {
                    final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.exitapp);

                    Button yes = dialog.findViewById(R.id.yes);
                    Button no = dialog.findViewById(R.id.no);

                    yes.setOnClickListener(v1 -> {
                        am.putProceed(true);
                        am.putSavedPreviousStep(step_);

                        if (step_ == 0) {
                            am.putSaveCustomerCountryPosition(PhoneNumber.citizenship.getSelectedItemPosition());
                            am.putSaveAlterCustomerCountryPosition(AlternatePhoneNumber.citizenship.getSelectedItemPosition());
                        } else if (step_ == 1) {
                            am.putSaveTitlePosition(singleTitle.getViewTitleID());
                        } else if (step_ == 3) {
                            am.putSaveNextofKinCountry(NextofKinPhoneNumber.citizenship.getSelectedItemPosition());
                            am.putSaveAltNextofKinCountry(NextofKinAltPhoneNumber.citizenship.getSelectedItemPosition());
                        } else if (step_ == 4) {
                            am.putSavePeriodEmployment(employPeriod.getCheckedRadioButtonId());
                        } else if (step_ == 5) {
                            am.putSaveCustomerCategory(customer_cat_.getSelectedCatID());
                            am.putSaveSelectedBranchPosition(branchselect.getSelectedItemPosition());
                            am.putSaveSelectedCurrencyPosition(currselect.getSelectedItemPosition());
                            am.putBankStaffCountryPosition(staffPhoneNumber.citizenship.getSelectedItemPosition());
                        } else if (step_ == 6) {
                            am.putMediumRadio(radioGroup.getCheckedRadioButtonId());
                        }

                        dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                    });

                    no.setOnClickListener(v -> {
                        am.putProceed(false);
                        dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                    });

                    dialog.show();
                }
                break;
            case R.id.prev:
                if (step_ != 0) {
                    step_--;
                    flipViewIt(step_);
                    flipper.showPrevious();
                    if (step_ == 0) {
                        prev.setVisibility(View.VISIBLE);
                        if (pan_pin_in.getVisibility() == View.VISIBLE) {
                            et_pin.setText("");
                            pan_pin_in.setVisibility(View.GONE);
                            webView.setVisibility(View.VISIBLE);
                            radiob.setVisibility(View.VISIBLE);

                        }
                    } else if (step_ == 1) {
                        if (pan_pin_in.getVisibility() == View.VISIBLE) {
                            et_pin.setText("");
                            pan_pin_in.setVisibility(View.GONE);
                            webView.setVisibility(View.VISIBLE);
                            radiob.setVisibility(View.VISIBLE);
                        }
                    } else if (step_ == 3) {
                        occupationIds.clear();
                        occupationNames.clear();
                        professionIds.clear();
                        professionNames.clear();
                    } else if (step_ == 4) {
                        politicsIDs.clear();
                        politicsNames.clear();
                        StringPoliticallyExposed = "";
                    } else if (step_ == 6) {
                        regionsIds.clear();
                        regionNames.clear();
                        districtIds.clear();
                        districtNames.clear();
                        countyIds.clear();
                        countylistNames.clear();
                        subcountyIds.clear();
                        subcountylistNames.clear();
                        parishIds.clear();
                        parishlistNames.clear();
                        villageIds.clear();
                        villagelistNames.clear();
                        eAIds.clear();
                        eAlistNames.clear();
                    }
                } else if (step_ == 0) {
                    finish();
                }
                break;
            case R.id.next:

                if (step_ == 0) {
                    if (TextUtils.isEmpty(country.getText())) {
                        ErrorAlert("Name required");
                    } else if (!selectedAccountID.equals("32219") && PhoneNumber.getText().length() < 5) {
                        ErrorAlert("Invalid phone number");
                    } /*else if(TextUtils.isEmpty(eAIdString)) {  //was for address drop downa
                        ErrorAlert("EA Name required");}*/ else {
                        am.putSaveCustomerCountryPosition(PhoneNumber.citizenship.getSelectedItemPosition());
                        am.putSaveAlterCustomerCountryPosition(AlternatePhoneNumber.citizenship.getSelectedItemPosition());
                        step_++;
                        prev.setVisibility(View.VISIBLE);
                        flipViewIt(step_);
                        flipper.showNext();
                    }
                } else if (step_ == 1) {
                    if (!radiob.isChecked()) {
                        ErrorAlert("Please accept terms and conditions to proceed");
                    } else {
                        step_++;
                        prev.setVisibility(View.VISIBLE);
                        flipViewIt(step_);
                        flipper.showNext();
                        RegisterNewListener();
                        CustomerCategory = "Existing Customer";
                        otpPinView.setEnabled(true);
                        startStimer();
//                        INFOFIELD1 = "INFOFIELD1:ACCOUNTID|"+ accountNumber.getText() +"|CUSTOMER_CATEGORY|"+CustomerCategory+"|ACCOUNT_TYPE|"+selectedAccount+"|FIRST_NAME|"+ name.getText() +
//                                "|MIDDLE_NAME|"+ otherNames.getText() +"|LAST_NAME|"+ sname.getText() +"|DOB|"+DOBEdit.getText()+"|NATIONALID|"+ nationalID.getText() +
//                                "|PHONE_NUMBER|"+customerMobilenNumber+"|ALTERNATE_PHONE_NUMBER|"+alternatePhone+"|EMAIL_ADDRESS|"+ EmailAddress.getText() +"|GENDER|"+gender+"|TITLE|"+Usertitle+"|CURRENCY|"+currName+"|BRANCH|"+branchID+"|PRODUCTID|"+selectedAccountID+"|ALTERNATE_ACCOUNT_NUMBER|"+"N/A"+"|ALTERNATE_ACCOUNT_NAME|"+"N/A"+"|ALTERNATE_BANKNAME|"+ "N/A"+"|ALTERNATE_BRANCHNAME|"+"N/A"+"|MOBILE_MONEY_PROVIDER|"+mobileMoneyProvider+"|MOBILE_MONEY_PHONE_OWNER|"+"N/A"+"|MOBILE_MONEY_PHONE_NUMBER|"+PhoneNumberMobile.getText();

                        INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + AccoutNo + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + country.getText() +
                                "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + country.getText() + "|DOB|" + "N/A" + "|NATIONALID|" + "N/A" +
                                "|PHONE_NUMBER|" + PhoneNumber.getCountryCode() + PhoneNumber.getText() + "|ALTERNATE_PHONE_NUMBER|" + AlternatePhoneNumber.getCountryCode() + AlternatePhoneNumber.getText() + "|EMAIL_ADDRESS|" + "N/A"+"|GENDER|"+"N/A"+"|TITLE|" + "N/A"+"|CURRENCY|"+ Currency + "|BRANCH|" + BranchID + "|PRODUCTID|" + ProductID+"|ALTERNATE_ACCOUNT_NUMBER|"+"N/A"+"|ALTERNATE_ACCOUNT_NAME|"+"N/A"+"|ALTERNATE_BANKNAME|"+ "N/A"+"|ALTERNATE_BRANCHNAME|"+"N/A"+"|MOBILE_MONEY_PROVIDER|"+"N/A"+"|MOBILE_MONEY_PHONE_OWNER|"+"N/A"+"|MOBILE_MONEY_PHONE_NUMBER|"+"N/A";

                        /*if(am.getCountry().equals("UGANDATEST")){
                            INFOFIELD2 = "INFOFIELD2:FATHER_FIRST_NAME|"+ FatherFirstName.getText() +"|FATHER_MIDDLE_NAME|"+ FatherMiddleName.getText() +"|FATHER_LAST_NAME|"+ FatherLastName.getText() +"|MOTHER_FIRST_NAME|"+ MotherFirstName.getText() +"|MOTHER_MIDDLE_NAME|"+ MotherMiddleName.getText() +"|MOTHER_LAST_NAME|"+ MotherLastName.getText() +"|ADDRESSCODE|"+ eAIdString ;
                        }*/
                        INFOFIELD2 = "INFOFIELD2:FATHER_FIRST_NAME|" + FatherFirstName.getText() + "|FATHER_MIDDLE_NAME|" + FatherMiddleName.getText() + "|FATHER_LAST_NAME|" + FatherLastName.getText() + "|MOTHER_FIRST_NAME|" + MotherFirstName.getText() + "|MOTHER_MIDDLE_NAME|" + MotherMiddleName.getText() + "|MOTHER_LAST_NAME|" + MotherLastName.getText();


                        /*if(am.getCountry().equals("UGANDATEST")){
                            INFOFIELD3 = "INFOFIELD3:CURRENT_LOCATION|"+ c5.getText() +"|ADDRESS|"+ ActualAddress.getText() +"|HOME_DISTRICT|"+ Address.getText() +"|YEARS_AT_ADDRESS|"+ YearsAtAddress.getText().concat(periodAddressString)+"|POLITICALLY_EXPOSED|"+StringPoliticallyExposed+"|CITY|"+city.getText()+"|ZIPCODE|"+zipCode.getText();
                        }*/
                        INFOFIELD3 = "INFOFIELD3:CURRENT_LOCATION|"+"N/A"+"|ADDRESS|"+"N/A"+"|HOME_DISTRICT|"+"N/A"+"|YEARS_AT_ADDRESS|"+"N/A"+"|POLITICALLY_EXPOSED|"+"N/A"+"|MARITALSTATUS|"+"N/A"+"|ZIPCODE|"+"N/A";

                        /*if(am.getCountry().equals("UGANDATEST")){
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|"+ IncomeperAnnum.getText() +"|EMPLOYMENT_TYPE|"+ professionIDString +"|OCCUPATION|"+ occupationIDString +"|PLACE_OF_WORK|"+ PlaceofWork.getText() +"|NATURE_OF_BUSINESS_SECTOR|"+ NatureofBussiness.getText() +"|PERIOD_OF_EMPLOYMENT|"+ PeriodofEmployment.getText().concat(periodWorkString) +"|EMPLOYER_NAME|"+ EmployerName.getText() +"|NATURE|"+ NatureofEmployment.getText();
                        }*/
                        INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|"+ "N/A" +"|EMPLOYMENT_TYPE|"+"N/A"+"|OCCUPATION|"+ "N/A" +"|PLACE_OF_WORK|"+"N/A" +"|NATURE_OF_BUSINESS_SECTOR|"+ "N/A" +"|PERIOD_OF_EMPLOYMENT|"+ "N/A"+"|EMPLOYER_NAME|"+ "N/A"+"|NATURE|"+"N/A"+"|BUSINESS_ADDRESS|"+ "N/A";

                        INFOFIELD5 = "INFOFIELD5:NEXT_OF_KIN_FIRST_NAME|" + "N/A" + "|NEXT_OF_KIN_MIDDLE_NAME|" +"N/A" +"|NEXT_OF_KIN_LAST_NAME|" +"N/A"+"|NEXT_OF_KIN_PHONE_NUMBER|" + "N/A"+"|NEXT_OF_KIN_ALTERNATE_PHONE_NUMBER|"+"N/A"+"|NEXT_OF_KIN_ADDRESS|" +"N/A"+"|OTHER_SERVICES_REQUIRED|"+"N/A"+"|RECOMMENDED_BY|"+"N/A";

                        currentTask = "RequestOTP";
                        createOTP();

                    }
                } else if (step_ == 2) {
                    if (TextUtils.isEmpty(raoOTP)) {
                        ErrorAlert("One time password required");
                    } else if (raoOTP.length() < 6) {
                        ErrorAlert("Otp can not be less than 6 digits");
                    } else {
                        verify();
                    }
                }
            
            
               

              

                       

                    
                
               

                /*else if(step_ == 10){
                }*/
                break;
            case R.id.front:
                Dexter.withActivity(AccountOpenZExistingCustomersMain.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                REQUEST_IMAGEX = 1;
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.selfie:
                Dexter.withActivity(AccountOpenZExistingCustomersMain.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                REQUEST_IMAGEX = 2;
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.backpick:
                Dexter.withActivity(AccountOpenZExistingCustomersMain.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                REQUEST_IMAGEX = 3;
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.tv_resend_otp:
                try {
                    unregisterReceiver(onBroadcastSMSReceived);
                    RegisterNewListener();
                    currentTask = "ResentRequestOTP";
                    createOTP();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.done:
                am.putProceed(false);
                finish();
                startActivity(new Intent(this, DepositOptionsActivity.class));
                break;

        }
        class WebViewClient extends android.webkit.WebViewClient {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                progressBar1.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar1.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        }
    }

    private void depositMethodes() {

        final Dialog mDialog = new Dialog(AccountOpenZExistingCustomersMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.deposit_options);
        CheckBox check;
        final TextView txtTitle = mDialog.findViewById(R.id.message),
                txtMessage = mDialog.findViewById(R.id.show_response),
                txtOk = mDialog.findViewById(R.id.done);
        check = mDialog.findViewById(R.id.check) ;
        txtTitle.setText(R.string.registerd_n_successfully);
        txtMessage.setText(R.string.deposit_options);

        txtOk.setOnClickListener(v1 -> {
            finish();
            mDialog.dismiss();


        });
        mDialog.show();

    }
}