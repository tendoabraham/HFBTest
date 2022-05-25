package com.elmahousingfinanceug.launch.rao;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.Looper;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chaos.view.PinView;
import com.elmahousingfinanceug.R;

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
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import mumayank.com.airlocationlibrary.AirLocation;

public class AccountOpenZMain extends AppCompatActivity implements ResponseListener, View.OnClickListener {
    TextView title, prev, next, otpcountdown, tv_resend_otp, show_response, textView2, textView3;
    ProgressBar determinateBar, progressBar1;
    Bitmap bitmapImageFront, bitmapImageBack, bitmapImageSelfie;
    ViewFlipper flipper;
    WebView webView;
    String  nationality = "", sex ,surName= "" , CardNumber= "",DateOfExpiry = "" , NIN = "", dob  = "" ,Name = "", faceId1 = "", imageURL = "",faceId2 = "";
    String status = "", message = "", fields = "", idNumberObject = "", fullName = "", issueDate = "", idSerial = "", birthdate = "";
    ImagePick front, backpick, selfie, signature;
    Spinner currselect, branchselect, employmentType, political_spin, occupation_spin, proffession_status, regionselect, districtName,
            countyName, subCountyName, parishName, villageName, eAName, spinnerMulti, alternativeBank;
    LinearLayout ccg, currencyLayout, centeidsdetails, centeparentinfo, centesourceincome, centenextofkin,
            centecontacts, centeextras, centehearusfrom, new_Lay, existing_lay, aMr, bMrs, cMiss, alternativeDeposite;
    ScrollView pan_pin_in;
    cicEditText staffPhoneNumber, accountNumber, name, sname, otherNames, nationalID, nationalIDCardNo, DOBEdit, FatherFirstName, phoneregName, phoneregLastName,
            FatherMiddleName, FatherLastName, MotherFirstName, MotherMiddleName, MotherLastName, Address, YearsAtAddress,
            PoliticallyExposed, IncomeperAnnum, MonthlySalary, EmploymentType, Occupation, yearOfEmployment, PlaceofWork, NatureofBussiness,
            PeriodofEmployment, EmployerName, NatureofEmployment, NextofKinFirstName, NextofKinMiddleName,
            NextofKinLastName, NextofKinPhoneNumber, NextofKinAltPhoneNumber, NextofKinAddress, EmailAddress,
            PhoneNumber, AlternatePhoneNumber, ActualAddress, country, zipCode, c4, c44, c45, c5, alternativeAccountNumber, bankName, branchName, accountName, PhoneNumberMobile;

    EditText et_acc, et_pan, et_pin, et_phone;
    RadioGroup addressPeriod, employPeriod, accountsGroup, radioGroup, genderGroup, RGroupM, RGroupCon;
    RadioButton yearsButtonM, monthsButtonM, yearsButtonE, monthsButtonE, FaceBook, Twitter,
            Instagram, tv, rd, ss, bankstaff, HFBCustomer, Agent, male, female, mtn, airtel, yes, no;
    AppCompatCheckBox chkMobileBanking, chkPos, chkATM, chkChequeBook, chkInternetBanking, chkAgencyBanking, radiob;
    PinView otpPinView;
    HorizontalScrollView scrollAccounts;
    CustomerCat customer_cat_;
    joeSingleChoice singleTitle;
    RelativeLayout success_failed;

    private AllMethods am;
    private  CustomJsonRequest customJsonRequest;  

    int REQUEST_IMAGE = 100, REQUEST_IMAGEX = 0, step_ = 0;

    byte[] byteArray;
    byte[] byteArray1;

    String encodedImageFront = "", encodedImageBack = "", encodedImageSelfie = "", encodedImageSignature = "", Usertitle = "", CustomerCategory = "",
            step0 = "Customer Type & Product", step1 = "Personal details?", step2 = "Share with us your ID details", step3 = "Confirm these are your ID details",
            step4 = "Next Of Kin", step5 = "Source of Income",
            step6 = "Terms & Conditions", step7 = "Provide Alternative account number(Bank Account or Mobile Money)", step8 = "How did you come to know about us ?", step9 = "Other services", step10 = "OTP Confirmation",
            step11 = "Parent Information", raoOTP = "", currentTask = "", INFOFIELD1 = "", INFOFIELD2 = "", INFOFIELD3 = "",
            INFOFIELD4 = "", INFOFIELD5 = "", token = "", payload = "", Device = "", uri = "", extrauri = "", new_request = "",
            selectedAccount = "", selectedAccountID = "", currName = "", branchID = "", termsUrl = "", currencyURL, periodAddressString = "", periodWorkString = "", gender = "", mobileMoneyProvider = "", ProductDescription = "",
            StringPoliticallyExposed = "", occupationIDString = "", professionIDString = "", regionIDString = "", districtIDString = "", countyIDString = "",
            subcountyIDString = "", parishIdString = "", villageIdString = "", eAIdString = "", userEmploymentType, maritalStatus, alternativeSecurityDeposit, processID = "",processID2 = "",imageURL1 = "";

    boolean done = false;
    private String[] FieldIDs, FieldValues;

    private AirLocation airLocation;
    Double latitude, longitude;

    CountDownTimer cnt;

    JSONArray accountProducts = null, branches = null;

    List<String> accountIDs = new ArrayList<>(), accountNames = new ArrayList<>(), currencyNames = new ArrayList<>(), productDescription = new ArrayList<>(),
            listBranchIDs = new ArrayList<>(), listBranchNames = new ArrayList<>(), listUrls = new ArrayList<>(), listCurrencyUrls = new ArrayList<>(),
            politicsIDs = new ArrayList<>(), politicsNames = new ArrayList<>(), employmentTypeDescription = new ArrayList<>(), maritalStatusList = new ArrayList<>(), alternativeAccounts = new ArrayList<>(),
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
        setContentView(R.layout.activity_main_open_account);

        am = new AllMethods(this);
        am.disableScreenShot(this);
        

        title = findViewById(R.id.title);
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
        signature = findViewById(R.id.signature);
        show_response = findViewById(R.id.show_response);

        currselect = findViewById(R.id.currselect);
        spinnerMulti = findViewById(R.id.spinnerMulti);
        alternativeBank = findViewById(R.id.alternativeBank);
        branchselect = findViewById(R.id.branchselect);
        employmentType = findViewById(R.id.employmentselect);
        determinateBar = findViewById(R.id.determinateBar);
        progressBar1 = findViewById(R.id.progressBar1);
        prev = findViewById(R.id.prev);
        prev.setVisibility(View.INVISIBLE);
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
        RGroupM = findViewById(R.id.RGroupM);
        RGroupCon = findViewById(R.id.RGroupCon);
        tv = findViewById(R.id.tv);
        rd = findViewById(R.id.rd);
        mtn = findViewById(R.id.mtn);
        airtel = findViewById(R.id.airtel);
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        ss = findViewById(R.id.ss);
        bankstaff = findViewById(R.id.bankstaff);
        HFBCustomer = findViewById(R.id.HFBCustomer);
        Agent = findViewById(R.id.Agent);

        ccg = findViewById(R.id.ccg);
        centeidsdetails = findViewById(R.id.centeidsdetails);
        alternativeDeposite = findViewById(R.id.alternativeDeposite);
        centeparentinfo = findViewById(R.id.centeparentinfo);
        centesourceincome = findViewById(R.id.centesourceincome);
        centenextofkin = findViewById(R.id.centenextofkin);
        centecontacts = findViewById(R.id.centecontacts);
        centeextras = findViewById(R.id.centeextras);
        centehearusfrom = findViewById(R.id.centehearusfrom);

        //Create Fields for user input
        generateForms();

        //Condition hide new for existing customers
        if (am.getCustomerID().length() < 10) {
            new_Lay.setVisibility(View.VISIBLE);
        } else {
            new_Lay.setVisibility(View.GONE);
        }

        title.setText(step0);


        branchselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    branchID = listBranchIDs.get(position - 1);
                } else {
                    branchID = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        backpick.setLabel("National\nIdentity Card\n(Back)");
        selfie.setLabel("\"Passport\\nPhoto\\n(Selfie");
        signature.setLabel("Signature\nPhoto\nNO Thumb print allowed\nSignature should not be in Upper case");

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
                productDescription.clear();
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
                                || am.FindInArray(field_IDs, field_Values, "ProductID").equals("SALCA") || am.FindInArray(field_IDs, field_Values, "ProductID").equals("PERC1"))) {
                            accountIDs.add(am.FindInArray(field_IDs, field_Values, "ProductID"));
                            accountNames.add(am.FindInArray(field_IDs, field_Values, "ProductName"));
                            productDescription.add(am.FindInArray(field_IDs, field_Values, "ProductDescription"));
                            listUrls.add(am.FindInArray(field_IDs, field_Values, "TermsUrl"));
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
                            productDescription.add(am.FindInArray(field_IDs, field_Values, "ProductDescription"));
                            listUrls.add(am.FindInArray(field_IDs, field_Values, "TermsUrl"));

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
                    RadioButton radioButton = new RadioButton(AccountOpenZMain.this);
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

                    selectedAccount = accountNames.get(checkedId);
                    selectedAccountID = accountIDs.get(checkedId);
                    ProductDescription = productDescription.get(checkedId);
                    termsUrl = listUrls.get(checkedId);
                    Log.e("product description", ProductDescription);
                    decriptionPopup();


                    currencyNames.clear();
                    listCurrencyUrls.clear();


                    try {

                        for (String aProduct : Products) {
                            String[] howLong = aProduct.split("\\|");
                            String[] field_IDs = new String[howLong.length / 2];
                            String[] field_Values = new String[howLong.length / 2];
                            am.separate(aProduct, "|", field_IDs, field_Values);
                            if (am.FindInArray(field_IDs, field_Values, "ProductID").matches(selectedAccountID)) {
                                currencyNames.add(am.FindInArray(field_IDs, field_Values, "CurrencyID"));
                                listCurrencyUrls.add(am.FindInArray(field_IDs, field_Values, "Urls"));


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
                        listCurrencyUrls.add(0, "Select Currency");
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(AccountOpenZMain.this, android.R.layout.simple_spinner_item, currencyNames);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.spiner_item);
                        currselect.setAdapter(spinnerArrayAdapter);
                        currencyLayout.setVisibility(View.VISIBLE);


                        currselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    currName = currselect.getSelectedItem().toString().trim();
                                    currencyURL = listCurrencyUrls.get(position);
                                    Log.e("CurrencyURL", currencyURL);
                                } else {
                                    currName = "";
                                    currencyURL = "";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


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
                        am.ToastMessageLong(AccountOpenZMain.this, getString(R.string.tryAgain));
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
                ArrayAdapter<String> spinnerArrayAdapterB = new ArrayAdapter<>(AccountOpenZMain.this, android.R.layout.simple_spinner_item, listBranchNames);
                spinnerArrayAdapterB.setDropDownViewResource(R.layout.spiner_item);
                branchselect.setAdapter(spinnerArrayAdapterB);

            } catch (Exception e) {
                am.ToastMessageLong(AccountOpenZMain.this, getString(R.string.tryAgain));
            }
        }
    };

    private void decriptionPopup() {

        final Dialog mDialog = new Dialog(AccountOpenZMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_info_customised);
        CheckBox check;
        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title),
                txtMessage = mDialog.findViewById(R.id.dialog_message),
                txtOk = mDialog.findViewById(R.id.dialog_btn);
        txtOk.setText(R.string.proceed);
        txtTitle.setText(selectedAccount);
        txtMessage.setText(ProductDescription);
        txtMessage.setOnClickListener(view1 -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currencyURL)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        txtOk.setOnClickListener(v1 -> {
            mDialog.dismiss();
        });
        mDialog.show();

    }

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
        signature.setOnClickListener(this);
        tv_resend_otp.setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);
    }

    @SuppressLint("ResourceType")
    private void generateForms() {


        //Contact Details  field inputs
        EmailAddress = new cicEditText(this, VAR.EMAIL, "Email Address " + getString(R.string.mandatory_field), " acb@domain.com");
        PhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Phone Number ", " 722222222");
        AlternatePhoneNumber = new cicEditText(this, VAR.PHONENUMBER, " Alternative Phone Number ", " 7333333");
        ActualAddress = new cicEditText(this, VAR.TEXT, "Current Address ", " 123 Kampala");
        country = new cicEditText(this, VAR.TEXT, "Country of residence", " Uganda");
        zipCode = new cicEditText(this, VAR.TEXT, "Country code", " 256");


        centecontacts.addView(EmailAddress);
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
        centecontacts.addView(ActualAddress);


        country.setEnabled(false);
        zipCode.setEnabled(false);

        PhoneNumber.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (PhoneNumber.editText.getText().length() > 5) {
                    zipCode.setText(PhoneNumber.getCountryCode());
                    country.setText(PhoneNumber.citizenship.getSelectedItem().toString());
                } else {
                    zipCode.setText("");
                    country.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        maritalStatusList.add(0, "Select MaritalStatus");
        maritalStatusList.add("Married");
        maritalStatusList.add("Single");
        maritalStatusList.add("Divorced");
        maritalStatusList.add("Separated");
        maritalStatusList.add("Widowed/Widower");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(AccountOpenZMain.this, android.R.layout.simple_spinner_item, maritalStatusList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spiner_item);
        spinnerMulti.setAdapter(spinnerArrayAdapter);
        spinnerMulti.setVisibility(View.VISIBLE);
        spinnerMulti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    maritalStatus = spinnerMulti.getSelectedItem().toString().trim();

                } else {
                    maritalStatus = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //ID Details
        name = new cicEditText(this, VAR.TEXT, "Given Name ", " John");
        sname = new cicEditText(this, VAR.TEXT, "Surname ", " Mwanza");
        otherNames = new cicEditText(this, VAR.TEXT, "Other Names " + getString(R.string.optional_field), " Mwanzo");
        nationalID = new cicEditText(this, VAR.TEXT, "National ID ", " CM1234568806LGB");
        DOBEdit = new cicEditText(this, VAR.TEXT, "Date of Birth  YYYY-MM-DD ", "  1990-12-31");
        nationalIDCardNo = new cicEditText(this, VAR.NUMBER, "Card No", "01041225");
        name.setEnabled(false);
        sname.setEnabled(false);
        nationalID.setEnabled(false);
        DOBEdit.setEnabled(false);
        nationalIDCardNo.setEnabled(false);

        centeidsdetails.addView(name);
        centeidsdetails.addView(sname);
        centeidsdetails.addView(otherNames);
        centeidsdetails.addView(nationalID);
        centeidsdetails.addView(DOBEdit);
        centeidsdetails.addView(nationalIDCardNo);

        //Next of kin details
        NextofKinFirstName = new cicEditText(this, VAR.TEXT, "Next of Kin First Name ", " John");
        NextofKinMiddleName = new cicEditText(this, VAR.TEXT, "Next of Kin Middle Name " + getString(R.string.optional_field), " Kawaooya");
        NextofKinLastName = new cicEditText(this, VAR.TEXT, "Next of Kin Last Name ", " Kawaooya");
        NextofKinPhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Next of Kin Phone Number " + getString(R.string.optional_field), " 72222222");
        NextofKinAltPhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Next of Kin Alternate Phone Number " + getString(R.string.optional_field), " 7333333");
//        NextofKinAddress = new cicEditText(this, VAR.TEXT,"Next of Kin Adress "," 123 Kampala");

        centenextofkin.addView(NextofKinFirstName);
        centenextofkin.addView(NextofKinMiddleName);
        centenextofkin.addView(NextofKinLastName);
        centenextofkin.addView(NextofKinPhoneNumber);
        centenextofkin.addView(NextofKinAltPhoneNumber);
//        centenextofkin.addView(NextofKinAddress);

        // occupation details


        IncomeperAnnum = new cicEditText(this, VAR.AMOUNT, "Annual income ", "");
        NatureofBussiness = new cicEditText(this, VAR.TEXT, "Nature of Business/Activity Sector ", "");
        NatureofEmployment = new cicEditText(this, VAR.TEXT, "Business Address ", " 123 Kampala");
        PeriodofEmployment = new cicEditText(this, VAR.TEXT, "Kindly specify ", "Allowance");
        EmploymentType = new cicEditText(this, VAR.TEXT, "Employment Type ", "");
        MonthlySalary = new cicEditText(this, VAR.TEXT, "Annual salary", "UGH4000000");
        EmployerName = new cicEditText(this, VAR.TEXT, "Employer's Name ", " NWSC");
        Occupation = new cicEditText(this, VAR.TEXT, "Occupation ", " Engineer");
        PlaceofWork = new cicEditText(this, VAR.TEXT, "Employer Address ", "");
        yearOfEmployment = new cicEditText(this, VAR.NUMBER, "Period Of Employment ", "5");


        RadioGroup.LayoutParams rp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT, 1);
        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (6 * density);
        rp.setMargins(margin, margin, margin, margin);

        employPeriod = new RadioGroup(this);
        employPeriod.setOrientation(RadioGroup.HORIZONTAL);
        genderGroup = new RadioGroup(this);
        genderGroup.setOrientation(RadioGroup.HORIZONTAL);


        yearsButtonE = new RadioButton(AccountOpenZMain.this);
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
        monthsButtonE = new RadioButton(AccountOpenZMain.this);
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
        male = new RadioButton(AccountOpenZMain.this);
        male.setButtonDrawable(getResources().getDrawable(R.drawable.back_radio_button_tick));
        male.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            male.setElevation(10f);
        }
        male.setText(getString(R.string.male));
        male.setId(0);
        male.setTextSize(14);
        male.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        male.setPadding(20, 20, 20, 20);
        genderGroup.addView(male, rp);

        female = new RadioButton(AccountOpenZMain.this);
        female.setButtonDrawable(getResources().getDrawable(R.drawable.back_radio_button_tick));
        female.setBackground(getResources().getDrawable(R.drawable.back_radio_button_select));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            female.setElevation(10f);
        }
        female.setText(getString(R.string.female));
        female.setTextSize(14);
        female.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        female.setPadding(20, 20, 20, 20);
        genderGroup.addView(female, rp);

        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (male.isChecked()) {
                gender = " - Male";
            } else {
                gender = " - Female";
            }
        });
        genderGroup.check(0);

        centecontacts.addView(genderGroup);
        employmentTypeDescription.add(0, "SelectIncomeType");
        employmentTypeDescription.add("Self-employed/Business");
        employmentTypeDescription.add("Employed/Salary");
        employmentTypeDescription.add("Others");
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(AccountOpenZMain.this, android.R.layout.simple_spinner_item, employmentTypeDescription);
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.spiner_item);
        employmentType.setAdapter(spinnerArrayAdapter1);
        employmentType.setVisibility(View.VISIBLE);

        employmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    userEmploymentType = employmentType.getSelectedItem().toString();
                    if (userEmploymentType.equals("Self-employed/Business")) {
                        centesourceincome.addView(IncomeperAnnum);
                        centesourceincome.addView(NatureofEmployment);
                        centesourceincome.addView(NatureofBussiness);
                        centesourceincome.removeView(EmployerName);
                        centesourceincome.removeView(Occupation);
                        centesourceincome.removeView(PlaceofWork);
                        centesourceincome.removeView(MonthlySalary);
                        centesourceincome.removeView(PeriodofEmployment);
                        centesourceincome.removeView(employPeriod);
                        centesourceincome.removeView(yearOfEmployment);
                    } else if (userEmploymentType.equals("Employed/Salary")) {
                        centesourceincome.addView(EmployerName);
                        centesourceincome.addView(PlaceofWork);
                        centesourceincome.removeView(NatureofEmployment);
                        centesourceincome.addView(MonthlySalary);
                        centesourceincome.addView(Occupation);
                        centesourceincome.addView(yearOfEmployment);
                        centesourceincome.addView(employPeriod);
                        centesourceincome.removeView(IncomeperAnnum);
                        centesourceincome.removeView(PeriodofEmployment);
                        centesourceincome.removeView(EmploymentType);
                        centesourceincome.removeView(NatureofBussiness);


                    } else {
                        centesourceincome.removeView(IncomeperAnnum);
                        centesourceincome.removeView(EmploymentType);
                        centesourceincome.removeView(EmployerName);
                        centesourceincome.removeView(Occupation);
                        centesourceincome.removeView(PlaceofWork);
                        centesourceincome.removeView(NatureofBussiness);
                        centesourceincome.removeView(NatureofEmployment);
                        centesourceincome.removeView(employPeriod);
                        centesourceincome.removeView(MonthlySalary);
                        centesourceincome.removeView(yearOfEmployment);
                        centesourceincome.addView(PeriodofEmployment);
                    }

                    Log.e("userEmployee", userEmploymentType);
                } else {
                    userEmploymentType = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userEmploymentType = "";
            }
        });


        //Occupation Dropdowns
        /*EmploymentType.setVisibility(View.GONE);
        Occupation.setVisibility(View.GONE);
        getLayoutInflater().inflate(R.layout.occuption, centesourceincome,true);
        occupation_spin = findViewById(R.id.occupation_spin);
        proffession_status = findViewById(R.id.proffession_status);*/


        // hear us
        final TextView sstext = findViewById(R.id.sstext);
        c4 = new cicEditText(this, VAR.TEXT, "HFB Staff Name", " James Kabaku");
        c4.setVisibility(View.GONE);
        centehearusfrom.addView(c4);
        c44 = new cicEditText(this, VAR.TEXT, "Bank Staff's Branch", " Kampala");
        c44.setVisibility(View.GONE);
        centehearusfrom.addView(c44);
        c45 = new cicEditText(this, VAR.PHONENUMBER, "Customer phone number", " 733333333");
        c45.setVisibility(View.GONE);
        centehearusfrom.addView(c45);
        c5 = new cicEditText(this, VAR.TEXT, "Current Location ", " Kampala");
//        centehearusfrom.addView(c5);


        final RadioGroup ssgroup = findViewById(R.id.ssgroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.tv) {
                ssgroup.setVisibility(View.GONE);
                c4.setVisibility(View.VISIBLE);
                sstext.setVisibility(View.GONE);
                c4.setLabel("TV/Radio station");
                c44.setVisibility(View.GONE);
                c45.setVisibility(View.GONE);
            } else if (checkedId == R.id.rd) {
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


        //Alternative bank dep detail

        alternativeAccountNumber = new cicEditText(this, VAR.NUMBER, "Alternative Account Number " + getString(R.string.mandatory_field), "00011100000");
        accountName = new cicEditText(this, VAR.TEXT, "Account Name " + getString(R.string.mandatory_field), "Saving plus");
        bankName = new cicEditText(this, VAR.TEXT, "Bank Name" + getString(R.string.mandatory_field), "Housing Finance Bank");
        branchName = new cicEditText(this, VAR.TEXT, "Branch Name" + getString(R.string.mandatory_field), " Uganda");
        PhoneNumberMobile = new cicEditText(this, VAR.PHONENUMBER, "  Phone Number " + getString(R.string.mandatory_field), " 7333333");
        phoneregName = new cicEditText(this, VAR.TEXT, "First Name" + getString(R.string.mandatory_field), "John");
        phoneregLastName = new cicEditText(this, VAR.TEXT, "Last Name" + getString(R.string.mandatory_field), "Smith");


        alternativeAccounts.add(0, "Select Alternative Accounts");
        alternativeAccounts.add("Bank Account");
        alternativeAccounts.add("Mobile Money");

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(AccountOpenZMain.this, android.R.layout.simple_spinner_item, alternativeAccounts);
        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spiner_item);
        alternativeBank.setAdapter(spinnerArrayAdapter2);
        alternativeBank.setVisibility(View.VISIBLE);

        alternativeBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    alternativeSecurityDeposit = alternativeBank.getSelectedItem().toString();
                    if (alternativeSecurityDeposit.equals("Bank Account")) {
                        alternativeDeposite.addView(alternativeAccountNumber);
                        alternativeDeposite.addView(accountName);
                        alternativeDeposite.addView(bankName);
                        alternativeDeposite.addView(branchName);
                        alternativeDeposite.removeView(PhoneNumberMobile);
                        alternativeDeposite.removeView(phoneregName);
                        alternativeDeposite.removeView(phoneregLastName);
                        textView2.setVisibility(View.GONE);
                        RGroupM.setVisibility(View.GONE);
                        textView3.setVisibility(View.GONE);
                        RGroupCon.setVisibility(View.GONE);
                    } else if (alternativeSecurityDeposit.equals("Mobile Money")) {
                        alternativeDeposite.removeView(alternativeAccountNumber);
                        alternativeDeposite.removeView(accountName);
                        alternativeDeposite.removeView(bankName);
                        alternativeDeposite.removeView(branchName);
                        alternativeDeposite.addView(PhoneNumberMobile);
                        textView2.setVisibility(View.VISIBLE);
                        RGroupM.setVisibility(View.VISIBLE);
                        textView3.setVisibility(View.VISIBLE);
                        RGroupCon.setVisibility(View.VISIBLE);
                        RGroupM.setOnCheckedChangeListener((group, checkedId) -> {
                            if (airtel.isChecked()) {
                                mobileMoneyProvider = "Airtel";
                            } else {
                                mobileMoneyProvider = "MTN";
                            }
                        });
                        RGroupM.check(0);
//                        alternativeDeposite.addView(phoneregName);
//                        alternativeDeposite.addView(phoneregLastName);
                        final RadioGroup mobile = findViewById(R.id.RGroupCon);
                        mobile.setOnCheckedChangeListener((group, checkedId) -> {
                            if (checkedId == R.id.yes) {
                                alternativeDeposite.removeView(phoneregName);
                                alternativeDeposite.removeView(phoneregLastName);
                            } else if (checkedId == R.id.no) {
                                alternativeDeposite.addView(phoneregName);
                                alternativeDeposite.addView(phoneregLastName);
                            }

                        });

                    } else {
                        alternativeDeposite.removeView(alternativeAccountNumber);
                        alternativeDeposite.removeView(accountName);
                        alternativeDeposite.removeView(bankName);
                        alternativeDeposite.removeView(branchName);
                        alternativeDeposite.removeView(PhoneNumberMobile);
                        alternativeDeposite.removeView(name);
                        alternativeDeposite.removeView(sname);
                        textView2.setVisibility(View.GONE);
                        RGroupM.setVisibility(View.GONE);
                        textView3.setVisibility(View.GONE);
                        RGroupCon.setVisibility(View.GONE);
                        alternativeDeposite.removeView(name);
                        alternativeDeposite.removeView(sname);
                    }
                } else {
                    alternativeSecurityDeposit = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alternativeSecurityDeposit = "";
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

        yearsButtonM = new RadioButton(AccountOpenZMain.this);
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
        monthsButtonM = new RadioButton(AccountOpenZMain.this);
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

        //Existing HFB Account
        accountNumber = new cicEditText(this, VAR.NUMBER, "Your Existing HFB Account Number", " 01001216399");
        accountNumber.setPadding(25, 0, 25, 0);
        ccg.addView(accountNumber);
        accountNumber.setVisibility(View.GONE);

        //STAFF  Details
        staffPhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Bank Staff Phone Number " + getString(R.string.mandatory_field), " 7000000001");
        staffPhoneNumber.setPadding(25, 0, 25, 0);
        ccg.addView(staffPhoneNumber);
        staffPhoneNumber.setVisibility(View.GONE);
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
                prev.setVisibility(View.VISIBLE);
                break;
            case 2:
                title.setText(step2);
                prev.setVisibility(View.VISIBLE);
                break;
            case 3:
                title.setText(step3);
                prev.setVisibility(View.VISIBLE);
                break;
            case 4:
                title.setText(step4);
                prev.setVisibility(View.VISIBLE);
                break;
            case 5:
                title.setText(step5);
                prev.setVisibility(View.VISIBLE);
                break;
            case 6:
                title.setText(step6);
                WebSettings webSetting = webView.getSettings();
                webSetting.setBuiltInZoomControls(true);
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(termsUrl);
                break;
            case 7:
                title.setText(step7);
                break;
            case 8:
                title.setText(step8);
                otpPinView.setEnabled(true);
                next.setVisibility(View.VISIBLE);
                break;
            case 9:
                title.setText(step9);
                next.setVisibility(View.VISIBLE);
                break;
            case 10:
                title.setText(step10);
                next.setVisibility(View.INVISIBLE);
                break;
//            case 11:
//                title.setText(step11);
//                break;
//            case 12:
//                title.setText(step11);
//                break;
//            case 13:
//                title.setText(step12);
//                startStimer();
//                break;*/
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
        Intent intent = new Intent(AccountOpenZMain.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(AccountOpenZMain.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountOpenZMain.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            AccountOpenZMain.this.openSettings();
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


    ///ImageUpload

    String base_URL = "https://imageuploadv1.azurewebsites.net/api/ImageUpload_V1/?JSONData=";
    String progressbar_title = "";
    String progressbar_message = "";
    String image_URL = "";

    private void uploadImage(byte[] byteArray, String document) { //IDFRONT

        String url_string = "{" + "\"FormID\":\"LITTLEBUSINESS\"," +
                "\"FileType\":\"jpg\"," +
                "\"ModuleID\":\"" + document + "\"," +
                "\"BankID\":\"LITTLE\"}";

        try {
            image_URL = base_URL + URLEncoder.encode(url_string, "UTF-8");

            Log.d("encoded_url_string", image_URL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            Log.d("encoded_url_error", e.getMessage());
        }


        Log.d("image_url_front", image_URL);


        int max = 100;

        //showProgressBar(0, max);
        //image_URL = base_URL + "%7B%22FormID%22%3A%22BANKIDFRONT%22%2C%22Key%22%3A%22PORTAL-FF7B-4CCA-B884-98346D5EC385%22%2C%22Country%22%3A%22"+preferenceHelper.getCountry()+"%22%2C%22FileType%22%3A%22jpg%22%2C%22MobileNumber%22%3A%22"+preferenceHelper.getDriverMobileNumber()+"%22%2C%22EMailID%22%3A%22"+preferenceHelper.getEmail()+"%22%2C%22ModuleID%22%3A%22CREATECUSTOMER%22%2C%22BankID%22%3A%22"+bank_id+"%22%7D";
        Ion.with(AccountOpenZMain.this)
                .load("POST", image_URL)
                .uploadProgressHandler((uploaded, total) -> runOnUiThread(() -> {
                    Log.d("mercedes_progress-1", uploaded + " of " + total);
                    Double progress = (double) uploaded / (double) total;
                    Log.d("mercedes_progress-2", progress + " / " + max);
                    progress = progress * max;
                    Log.d("mercedes_progress-3", progress + " / " + max);
                    //showProgressBar(progress.intValue(), max);

                    am.progressDialog("1");

                }))
                .setByteArrayBody(byteArray)
                .asString()
                .setCallback((e, result) -> {
                    //{"Status":"091","Message":"Invalid Form Details 1.0","ImageURL":null}
                    //dismisDialog();
                    if (result != null) {
                        //Upload Success
                        Log.d("mercedes-result", "1" + result);

                    } else {
                        //Upload Failed
                        Log.d("mercedes-else", "2");
                    }
                    if (e != null) {
                        Log.d("mercedes-error", "1" + e.getMessage());
                    }


                    //let me put it outside the result is null check so that when images fail they can still proceed
                    try {
                        removeDialogs();

                        // {"Status":"000","Message":"Data Saved","ImageURL":"https://littleimages.blob.core.windows.net/documents/6F005297-5883-4EAE-A30F-5C9F058CF3E7"}
                        Log.d("upload_id_response", result);

                        Log.d("response_", result);
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("Status");
                        String message = jsonObject.getString("Message");

                        if (status.equals("000")) {
                            
                            if (jsonObject.has("ImageURL")) {
                                imageURL = jsonObject.getString("ImageURL");

                                Log.d("response_idurl", result);
                                validateImageSubmited(imageURL);

//                                imgCheck.setVisibility(View.VISIBLE);
//
//                                BusinessDocumentsModel businessDocumentsModel = new BusinessDocumentsModel(imageURL, document);
//                                lstDocumentsUrl.add(businessDocumentsModel);

                            }

                        } else if (status.equals("091")) {
                            ErrorAlert(message);
                        }

                    } catch (Exception exception) {
                        Log.d("mercedes-result", "signature-> " + exception.getMessage());

                        am.progressDialog("0");

                    }
                });
    }

    private void validateImageSubmited(String imageURL) {

        String base_URL2 = "https://imageai.azurewebsites.net/ReadDocument.aspx";

        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("RequestID", "SUBMITIMAGE");
            jsonObject1.put("ImageID", "NATIONALID");
            jsonObject1.put("ImageURL", imageURL);
            jsonObject1.put("Country", "UGANDA");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        
        Log.d("upload_idval_call", jsonObject1.toString());


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, base_URL2, jsonObject1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        am.progressDialog("1");
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");

                            Log.d("upload_idval_resp", jsonObject1.toString());


                            if (status.equals("Accepted")) {

                                if (jsonObject.has("ProcessID")) {
                                    processID = jsonObject.getString("ProcessID");
                                    Log.d("response_ProcessID", processID);

                                    final Handler handler = new Handler(Looper.getMainLooper());
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something after 100ms
                                            submitImage(processID);
                                        }
                                    }, 10000);
                                   

//                                imgCheck.setVisibility(View.VISIBLE);
//
//                                BusinessDocumentsModel businessDocumentsModel = new BusinessDocumentsModel(imageURL, document);
//                                lstDocumentsUrl.add(businessDocumentsModel);

                                }

                            } else if (status.equals("091")) {
                                ErrorAlert(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
        Log.d("IMAGEURL", req.toString());
    }

    private void submitImage(String processID) {
       
        String base_URL2 = "https://imageai.azurewebsites.net/ReadDocument.aspx";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestID", "GETRESULT");
            jsonObject.put("ImageID", "NATIONALID");
            jsonObject.put("ProcessID", processID);
            jsonObject.put("Country", "UGANDA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        am.progressDialog("1");
        customJsonRequest = new CustomJsonRequest(Request.Method.POST, base_URL2, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                am.progressDialog("0");
//                Log.d("IMEFIKA",response.toString());
//                :[{"Nationality":[{"text":"UGA","confidence":"0.995"}],"Sex":[{"text":"M","confidence":"0.989"}],"Surname":[{"text":"BIGIRWA","confidence":"0.995"}],"CardNumber":[{"text":"010394857","confidence":"0.995"}],"DateOfExpiry":[{"text":"06.06.2025","confidence":"0.995"}],"NIN":[{"text":"CM86004106Z49G","confidence":"0.983"}],"DateOfBirth":[{"text":"10.08.1986","confidence":"0.995"}],"Name":[{"text":"ISAAC","confidence":"0.978"}]}]}]
                try {
                    JSONObject jsonObject1 = response.getJSONObject(0);
                    String status = jsonObject1.getString("Status");
                    String message = jsonObject1.getString("Message");
                    String fields = jsonObject1.getString("fields");

                    JSONArray jsonArrayFields = new JSONArray(fields);
                    for (int j = 0; j < jsonArrayFields.length(); j++) {
                        JSONObject jsonFields = jsonArrayFields.getJSONObject(j);

                        if (jsonFields.has("Nationality")){

                            String genderArray = jsonFields.getString("Nationality");

                            JSONArray jsonArrayGender = new JSONArray(genderArray);
                            for (int k = 0; k < jsonArrayGender.length(); k++) {
                                JSONObject jsonGender = jsonArrayGender.getJSONObject(k);

                                if (jsonGender.has("text")){
                                    nationality = jsonGender.getString("text");
                                }
                            }

                        }

                        if (jsonFields.has("Sex")){

                            String idNumberArray = jsonFields.getString("Sex");

                            JSONArray jsonArrayID = new JSONArray(idNumberArray);
                            for (int l = 0; l < jsonArrayID.length(); l++) {

                                JSONObject sex = jsonArrayID.getJSONObject(l);
                               
                            }

                        }

                        if (jsonFields.has("Surname")){

                            String issueDateArray   =   jsonFields.getString("Surname");

                            JSONArray jsonArrayIssueDate = new JSONArray(issueDateArray);
                            for (int m = 0; m < jsonArrayIssueDate.length(); m++) {
                                

                                JSONObject jsonIssueDate = jsonArrayIssueDate.getJSONObject(m);
                                if (jsonIssueDate.has("text")){
                                    surName = jsonIssueDate.getString("text");
                                }
                            }

                        }

                        if (jsonFields.has("CardNumber")){

                            String IdSerialArray = jsonFields.getString("CardNumber");

                            JSONArray jsonArrayIDSerial = new JSONArray(IdSerialArray);
                            for (int n = 0; n < jsonArrayIDSerial.length(); n++) {

                                JSONObject jsonSerialNumber = jsonArrayIDSerial.getJSONObject(n);
                                if (jsonSerialNumber.has("text")){
                                    CardNumber = jsonSerialNumber.getString("text");
                                }
                            }


                        }

                        if (jsonFields.has("DateOfExpiry")){

                            String idNumberArray = jsonFields.getString("DateOfExpiry");

                            JSONArray jsonArrayID = new JSONArray(idNumberArray);
                            for (int l = 0; l < jsonArrayID.length(); l++) {

                                JSONObject jsonIDNumber = jsonArrayID.getJSONObject(l);
                                if (jsonIDNumber.has("text")){
                                    DateOfExpiry = jsonIDNumber.getString("text");
                                }
                            }
                            

                        }
                        if (jsonFields.has("NIN")){

                            String idNumberArray = jsonFields.getString("NIN");

                            JSONArray jsonArrayID = new JSONArray(idNumberArray);
                            for (int l = 0; l < jsonArrayID.length(); l++) {

                                JSONObject jsonIDNumber = jsonArrayID.getJSONObject(l);
                                if (jsonIDNumber.has("text")){
                                    NIN = jsonIDNumber.getString("text");
                                }
                            }

                        }

                        if (jsonFields.has("DateOfBirth")){
                            String birthDateArray = jsonFields.getString("DateOfBirth");

                            JSONArray jsonArrayBirthDate = new JSONArray(birthDateArray);
                            for (int q = 0; q < jsonArrayBirthDate.length(); q++) {

                                JSONObject jsonBirthDateDate = jsonArrayBirthDate.getJSONObject(q);
                                if (jsonBirthDateDate.has("text")){
                                    birthdate = jsonBirthDateDate.getString("text");
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                                
                                try {
                                    Date date = sdf.parse(birthdate);
                                    dob = outFormat.format(date); 
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        if (jsonFields.has("Name")){

                            String idNumberArray = jsonFields.getString("Name");

                            JSONArray jsonArrayID = new JSONArray(idNumberArray);
                            for (int l = 0; l < jsonArrayID.length(); l++) {

                                JSONObject jsonIDNumber = jsonArrayID.getJSONObject(l);
                                if (jsonIDNumber.has("text")){
                                    Name = jsonIDNumber.getString("text");
                                }
                            }

                        }

                    }


                    



//                           
                    if(status.equals("000"))  {
                        

//                        String  nationality = getValues(fields,0,"Nationality");
//                        String  sex = getValues(fields,1,"Sex");
//                        Toast.makeText(AccountOpenZMain.this, "GENDER"+sex, Toast.LENGTH_SHORT).show();
//                        String  surName = getValues(fields,2,"Surname");
//                        String  CardNumber = getValues(fields,3,"CardNumber");
//                        String  DateOfExpiry = getValues(fields,4,"DateOfExpiry");
//                        String  NIN = getValues(fields,5,"NIN");
//                        String  DateOfBirth = getValues(fields,6,"DateOfBirth");
//                        String  Name = getValues(fields,7,"Name");
                        sname.setText(surName);
                        name.setText(Name);
                        nationalID.setText(NIN);
                        DOBEdit.setText(dob);
                        nationalIDCardNo.setText(CardNumber);
                        otherNames.setText(Name);
                        uploadSelfyImage(byteArray1,"SELF") ;
//                        flipper.showNext();
//                        step_++;
//                                    niraValidation() ;
                        flipViewIt(step_);
//                        Toast.makeText(AccountOpenZMain.this, "NATIONAL" + nationality, Toast.LENGTH_SHORT).show();
                    }   else{
                        am.myDialog(AccountOpenZMain.this, getString(R.string.unrecognized_ID), getString(R.string.sure_clear)); 
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
            }
        }){
            
        };


        MS.getInstance(AccountOpenZMain.this.getApplicationContext()).addToRequestQueue(customJsonRequest);

       
    }

    private String getValues(String fields,int index, String fieldName) {
        String text = "";
        try {
            JSONArray jsonArray = new JSONArray(fields);
            JSONObject jsonObject1 = jsonArray.getJSONObject(index);
            String nationality = jsonObject1.getString(fieldName);
            JSONArray jsonArray2 = new JSONArray(nationality);
            JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
            text = jsonObject2.getString("text");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return text;
    }



    private void niraValidation() {
        String surname  ;
//        if(sname.getText().contains("")) {
//           surname = sname.getText().replace(" ","-") ;
//        } else{
//            surname  = sname.getText().toString();
//            
//        }
        new_request = "FORMID:M-:" +
                "MERCHANTID:NIRA:" + "ACCOUNTID:" + nationalID.getText() + ":" +
                "INFOFIELD1:" + surName + ":" +
                "INFOFIELD2:" + Name + ":" +
                "INFOFIELD3:" + DOBEdit.getText() + ":" +
                "INFOFIELD4:" + nationalIDCardNo.getText() + ":" +
                "ACTION:GETNAME:";
        am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), "NIRA");


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

    private void callServer2(JsonObjectRequest jsonObjectRequest) {

        MS.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    public String Toa(String str) {
        str = str.substring(0, str.length() - 2);
        return str + "\"B-0\":\"RIGHT THUMB\"" + "}";
    }

    private void getLocationAddress(Location location) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + ","
                + location.getLongitude() + "&key=AIzaSyDhZlL-z0dTCANCHwHSHbNQYnG96phvQ0c";

        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
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
                    Toast.makeText(AccountOpenZMain.this, theSms, Toast.LENGTH_LONG).show();
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

//                            Try something like this:
//
                            //Bitmap bmp = ;
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();

//                            bmp.recycle()
                            front.setImage(bitmap);
                            bitmapImageFront = BitmapCompressionWithZ(this, mFile);
                            encodedImageFront = ConvertImageToBase64(bitmap);

                            am.putSavedData("encodedImageFront", encodedImageFront);
                            break;
                        case 2:
                            
                            //Bitmap bmp = ;
                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                            byteArray1 = stream1.toByteArray();

//                            bmp.recycle()
                            /*front.setImage(bitmap);
                            bitmapImageFront = BitmapCompressionWithZ(this, mFile);
                            encodedImageFront = ConvertImageToBase64(bitmap);

                            am.putSavedData("encodedImageFront", encodedImageFront);*/
                            
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
                        case 4:
                            signature.setImage(bitmap);
                            bitmapImageBack = BitmapCompressionWithZ(this, mFile);
                            encodedImageSignature = ConvertImageToBase64(bitmap);
                            am.putSavedData("encodedImageSignature", encodedImageSignature);
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
                        "INFOFIELD6:" + encodedImageFront + ":" +
                        "INFOFIELD7:" + encodedImageSelfie + ":" +
                        "INFOFIELD8:" + encodedImageSignature + ":" +
                        "INFOFIELD9:" + encodedImageBack + ":" +
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
                        "EMAILID:" + EmailAddress.getText() + ":";

                am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), currentTask);
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
                "EMAILID:" + EmailAddress.getText() + ":";
        am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), currentTask);
    }

    private void accountBranchChoice() {
        final Dialog mDialog = new Dialog(AccountOpenZMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_info);

        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title),
                txtMessage = mDialog.findViewById(R.id.dialog_message),
                txtOk = mDialog.findViewById(R.id.dialog_BTN);
        txtTitle.setText(selectedAccount);
        txtMessage.setText(String.format("Currency   %s\n\nBranch %s", currName, branchselect.getSelectedItem().toString().trim()));
        txtOk.setOnClickListener(v1 -> {
            step_++;
            flipViewIt(step_);
            flipper.showNext();
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
        String Status = am.FindInArray(FieldIDs, FieldValues, "STATUS");
        String Message = am.FindInArray(FieldIDs, FieldValues, "MESSAGE");
        if (TextUtils.isEmpty(Message)) {
            Message = am.FindInArray(FieldIDs, FieldValues, "DATA");
        }
        if (TextUtils.isEmpty(Message)) {
            Message = getString(R.string.tryAgain);
            Toast.makeText(AccountOpenZMain.this, Message, Toast.LENGTH_LONG).show();
        } else {
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

                        case "NIRA":
//                            STATUS:000:DATA:SURNAME|SHAFIQUE|GIVEN NAMES|KABALI|NIN|CM89105106DLMK|CARD NUMBER|010413271|DOB|7/11/1989 12 00 00 AM

                            if (step_ == 3/*&& tv_resend_otp.getVisibility()==View.GONE*/) {
//                                show_response.setText(Message);

                                depositMethodes(Message);
//                                step_++;
//                                flipViewIt(step_);
//                                flipper.showNext();
//                                startStimer();
                            }
                        case "RequestOTP":


                            if (step_ == 8 /*&& tv_resend_otp.getVisibility()==View.GONE*/) {
                                step_++;
                                flipViewIt(step_);
                                flipper.showNext();
                                startStimer();
                            }
                            Toast.makeText(AccountOpenZMain.this, Message, Toast.LENGTH_LONG).show();
                            break;
                        case "ResentRequestOTP":
                            startStimer();
                            Toast.makeText(AccountOpenZMain.this, Message, Toast.LENGTH_LONG).show();
                            break;
                        case "VerifyOTP": {
                            currentTask = "RAO";
                            new_request = RAO();
                            new Handler().postDelayed(() -> am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), "RAO"), 400);
                            Log.e("TEST", new_request);
                            break;
                        }
                        case "VerifyExisting":
                            am.myDialog(AccountOpenZMain.this, getString(R.string.alert), getString(R.string.exists));
                            break;
                        case "VerifyExistingAlt":
                            am.myDialog(AccountOpenZMain.this, getString(R.string.alert), getString(R.string.already_exists));
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
                            Toast.makeText(AccountOpenZMain.this, Message, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AccountOpenZMain.this, Message, Toast.LENGTH_LONG).show();
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
                                Glide.with(AccountOpenZMain.this).load(Base64.decode(encodedImageFront, Base64.DEFAULT)).into(front.imageView);
                            }
                            if (!am.getSavedData("encodedImageSelfie").equals("")) {
                                encodedImageSelfie = am.getSavedData("encodedImageSelfie");
                                Glide.with(AccountOpenZMain.this).load(Base64.decode(encodedImageSelfie, Base64.DEFAULT)).into(selfie.imageView);
                            }
                            if (!am.getSavedData("encodedImageBack").equals("")) {
                                encodedImageBack = am.getSavedData("encodedImageBack");
                                Glide.with(AccountOpenZMain.this).load(Base64.decode(encodedImageBack, Base64.DEFAULT)).into(backpick.imageView);
                            }
                            if (!am.getSavedData("encodedImageSignature").equals("")) {
                                encodedImageSignature = am.getSavedData("encodedImageSignature");
                                Glide.with(AccountOpenZMain.this).load(Base64.decode(encodedImageSignature, Base64.DEFAULT)).into(signature.imageView);
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
                        prev.setVisibility(View.INVISIBLE);
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
                    }
                    
                    else if (step_ == 3) {
                        name.setText("");
                        sname.setText("");
                        nationalID.setText("");
                        nationalIDCardNo.setText("");
                        DOBEdit.setText("");
                        otherNames.setText("");
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
                }
                break;
            case R.id.next:
                if (step_ == 0) {
                    CustomerCategory = customer_cat_.getSelectedCategory();
                    if (CustomerCategory.equals("")) {
                        ErrorAlert("Select customer type.");
                    } else if (selectedAccount.equals("")) {
                        ErrorAlert("Please select a preferred account");
                    } else if (currName.equals("")) {
                        ErrorAlert("Please select a preferred currency");
                    } else if (branchID.equals("")) {
                        ErrorAlert("Please select a preferred branch");
                    } else if (accountNumber.getVisibility() == View.VISIBLE && TextUtils.isEmpty(accountNumber.getText())) {
                        accountNumber.editText.requestFocus();
                        ErrorAlert("Existing HFB Account Number required");
                    } else if (/*selectedAccountID.equals("32219") &&*/ staffPhoneNumber.getVisibility() == View.VISIBLE && TextUtils.isEmpty(staffPhoneNumber.getText())) {
                        ErrorAlert("Bank Staff Phone Number required");
                    } else {
                        switch (CustomerCategory) {
                            case "New Customer":
                                accountNumber.editText.setText("");
                                accountNumber.setVisibility(View.GONE);
                                break;
                            case "Existing Customer":
                                accountNumber.setVisibility(View.VISIBLE);
                                break;
                        }
                        am.putSaveCustomerCategory(customer_cat_.getSelectedCatID());
                        am.putSaveSelectedBranchPosition(branchselect.getSelectedItemPosition());
                        am.putSaveSelectedCurrencyPosition(currselect.getSelectedItemPosition());
                        if (staffPhoneNumber.getVisibility() == View.VISIBLE)
                            am.putBankStaffCountryPosition(staffPhoneNumber.citizenship.getSelectedItemPosition());

                        if (staffPhoneNumber.getVisibility() == View.VISIBLE) {
                            checkCustomerExists(staffPhoneNumber.getCountryCode() + staffPhoneNumber.getText(), "3");
                        } else if (accountNumber.getVisibility() == View.VISIBLE && TextUtils.isEmpty(accountNumber.getText())) {
                            accountNumber.editText.requestFocus();
                            ErrorAlert("Existing HFB Account Number required");
                        } else {
                            switch (CustomerCategory) {
                                case "New Customer":
                                    termsConditopnChoiceNew();
                                    break;
                                case "Existing Customer":
//                                accountBranchChoice();
                                    termsConditopnChoice();
                                    //getPersonalDetailsExisting("GetCustomerPersonal");
                                    //0100121637
                                    //validateExisting(accountNumber.getText());
                                    break;
                            }
                        }
                    }
                } else if (step_ == 1) {
                    if (TextUtils.isEmpty(EmailAddress.getText())) {
                        ErrorAlert("Email Address required");
                    } else if (!TextUtils.isEmpty(EmailAddress.getText()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(EmailAddress.getText()).matches()) {
                        ErrorAlert("Invalid Email address format");
                    } else if (!selectedAccountID.equals("32219") && PhoneNumber.getText().length() < 5) {
                        ErrorAlert("Invalid phone number");
                    } else if (!selectedAccountID.equals("32219") && !PhoneNumber.getCountryCode().equals("256") && TextUtils.isEmpty(EmailAddress.getText())) {
                        ErrorAlert("Email address required outside Uganda");
                    } else if (maritalStatus.isEmpty()) {
                        ErrorAlert("You need to select marital status");
                    } else if (TextUtils.isEmpty(ActualAddress.getText())) {
                        ErrorAlert("Address required");
                    } else if (ActualAddress.getText().length() > 25) {
                        ErrorAlert("Address length should not be above 25 characters");
                    } else {
                        am.putSaveCustomerCountryPosition(PhoneNumber.citizenship.getSelectedItemPosition());
                        am.putSaveAlterCustomerCountryPosition(AlternatePhoneNumber.citizenship.getSelectedItemPosition());
                        step_++;
                        prev.setVisibility(View.VISIBLE);
                        flipViewIt(step_);
                        flipper.showNext();
                    }
                } else if (step_ == 2) {
                    Usertitle = singleTitle.getSelectedTitle();
                    if ("32112".equals(selectedAccountID) && Usertitle.equals("Mr")) {
                        ErrorAlert(selectedAccount + " reserved for women");
                    } else if (Usertitle.equals("Other") && singleTitle.otherEditText.getText().toString().trim().isEmpty()) {
                        singleTitle.otherEditText.requestFocus();
                        ErrorAlert("Kindly specify your Title");
                    } else if (TextUtils.isEmpty(encodedImageFront)) {
                        ErrorAlert("National id card photo required");
                    } else if (TextUtils.isEmpty(encodedImageBack)) {
                        ErrorAlert("National id card photo required");
                    } else if (TextUtils.isEmpty(encodedImageSelfie)) {
                        ErrorAlert("Selfie photo required");
                    } else if (TextUtils.isEmpty(encodedImageSignature)) {
                        ErrorAlert("Signature photo required");
                    } else {
                        if (Usertitle.equals("Other"))
                            Usertitle = singleTitle.otherEditText.getText().toString().trim();
                        am.putSaveTitlePosition(singleTitle.getViewTitleID());
//                        submitImages();
                        uploadImage(byteArray, "IDFRONT");
                       
                    }
                } else if (step_ == 3) {
                    if (TextUtils.isEmpty(name.getText())) {
                        ErrorAlert("First name required");
                    } else if (name.getText().length() > 20) {
                        ErrorAlert("Invalid First name length max 20 characters");
                    } else if (TextUtils.isEmpty(sname.getText())) {
                        ErrorAlert("Surname name required");
                    } else if (sname.getText().length() > 20) {
                        ErrorAlert("Invalid Surname name length max 20 characters");
                    } else if (TextUtils.isEmpty(nationalID.getText())) {
                        ErrorAlert("National ID required");
                    } else if (TextUtils.isEmpty(DOBEdit.getText())) {
                        ErrorAlert("Date of Birth required");
                    } else if (DOBEdit.getText().trim().contains(" ")) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (!DOBEdit.getText().contains("-")) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (!checkDateFormat(DOBEdit.getText())) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (!Pattern.compile(DATE_PATTERN).matcher(DOBEdit.getText()).matches()) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (TextUtils.isEmpty(nationalIDCardNo.getText())) {
                        ErrorAlert("Invalid Card Number.");
                    } else {
//                        step_++;
//                        flipViewIt(step_);
//                        flipper.showNext();
                        niraValidation();
                        //Was for political dropdown
                        /*if(politicsIDs.isEmpty()) {
                            if(am.getCountry().equals("UGANDATEST")) getPoliticalExposed();
                        }*/
                    }
                } else if (step_ == 4) {
                    if (TextUtils.isEmpty(NextofKinFirstName.getText())) {
                        ErrorAlert("Next of Kin first name required");
                    } else if (TextUtils.isEmpty(NextofKinLastName.getText())) {
                        ErrorAlert("Next of Kin Last name required");
                    }
//                    else if(TextUtils.isEmpty(NextofKinPhoneNumber.getText())){
//                        ErrorAlert("Next of Kin phone number required");
//                    }
//                    else if(TextUtils.isEmpty(NextofKinAltPhoneNumber.getText())){
//                        ErrorAlert("Next of Kin alternate phone number required");
//                    } 
                    else {
                        am.putSaveNextofKinCountry(NextofKinPhoneNumber.citizenship.getSelectedItemPosition());
                        am.putSaveAltNextofKinCountry(NextofKinAltPhoneNumber.citizenship.getSelectedItemPosition());
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();

                        //Was for Occupation drop down
                        /*if(occupationIds.isEmpty()) {
                            getCustomParam("OCCUPATION","FetchOccupation");
                        }*/

                        //Was for address drop down
                        /*if(regionsIds.isEmpty()){
                            if(am.getCountry().equals("UGANDATEST")) getAddressParam("REGION","FetchRegion", "");
                            
                              centesourceincome.addView(IncomeperAnnum);
                        centesourceincome.addView(NatureofEmployment);
                        centesourceincome.addView(NatureofBussiness);
                        centesourceincome.removeView(EmployerName);
                        centesourceincome.removeView(Occupation);
                        centesourceincome.removeView(PlaceofWork);
                        centesourceincome.removeView(MonthlySalary);
                        centesourceincome.removeView(PeriodofEmployment);
                        centesourceincome.removeView(employPeriod);
                        }*/
                    }
                } else if (step_ == 5) {
                    if (userEmploymentType.equals("")) {
                        ErrorAlert("You need to select a source of income");
                    } else if (userEmploymentType.equals("Self-employed/Business") && TextUtils.isEmpty(IncomeperAnnum.getText())) {
                        ErrorAlert("Income annually is required");
                    } else if (userEmploymentType.equals("Self-employed/Business") && TextUtils.isEmpty(NatureofEmployment.getText())) {
                        ErrorAlert("Business Address is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(EmployerName.getText())) {
                        ErrorAlert(("EmployerName is required"));
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(Occupation.getText())) {
                        ErrorAlert("Occupation is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(PlaceofWork.getText())) {
                        ErrorAlert("PlaceofWork is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(MonthlySalary.getText())) {
                        ErrorAlert("Annual Income is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(yearOfEmployment.getText())) {
                        ErrorAlert("Period Of Employment is required");
                    } else if (userEmploymentType.equals("Others") && TextUtils.isEmpty(PeriodofEmployment.getText())) {
                        ErrorAlert("Description is required");
                    } else if (userEmploymentType.equals("Self-employed/Business") && TextUtils.isEmpty(NatureofBussiness.getText())) {
                        ErrorAlert("Nature of Business is required");
                    } else {
                        am.putSavePeriodEmployment(employPeriod.getCheckedRadioButtonId());
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();

                    }

                } else if (step_ == 6) {
                    if (!radiob.isChecked()) {
                        ErrorAlert("Please accept terms and conditions to proceed");
                    } else {
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();
                    /*switch (CustomerCategory) {
                        case "New Customer":
                            step++;
                            switchIt(step);
                            flipper.showNext();
                            break;
                        case "Existing Customer":
                            webView.setVisibility(View.GONE);
                            radiob.setVisibility(View.GONE);
                            if(pan_pin_in.getVisibility()==View.VISIBLE) {
                                if (et_acc.getText().toString().trim().length() < 5) {
                                    ErrorAlert("Bank Account number required!");
                                } else if (et_pan.getText().toString().trim().length() < 16) {
                                    ErrorAlert("ATM Card Number required!");
                                } else if (et_pin.getText().toString().trim().length() < 1) {
                                    ErrorAlert("ATM Card Pin required!");
                                } else if (et_phone.getText().toString().length() < 6) {
                                    ErrorAlert("Phone Number required!");
                                } else {
                                    personalDetailsExisting(et_acc.getText().toString().trim(),
                                            et_pan.getText().toString().trim(),
                                            et_pin.getText().toString().trim(),
                                            et_phone.getText().toString().trim());
                                }
                            }
                            if(pan_pin_in.getVisibility()==View.GONE) pan_pin_in.setVisibility(View.VISIBLE);
                            break;
                    }*/
                    }
                }

                /*else if(step_ == 7){
                    if(!tv.isChecked() && !ss.isChecked() && !bankstaff.isChecked() && !HFBCustomer.isChecked() && !Agent.isChecked()){
                        ErrorAlert("Recommended by Mediums required");
                    } else if(tv.isChecked() && TextUtils.isEmpty(c4.getText())){
                        ErrorAlert("TV/Radio station required");
                    } else if(ss.isChecked() && !FaceBook.isChecked() && !Twitter.isChecked() && !Instagram.isChecked()){
                        ErrorAlert("Social media platform required");
                    } else if(bankstaff.isChecked() && TextUtils.isEmpty(c4.getText())){
                        ErrorAlert("Staff name required");
                    } else if(bankstaff.isChecked() && TextUtils.isEmpty(c44.getText())){
                        ErrorAlert("Staff's branch required");
                    } else if(HFBCustomer.isChecked() && TextUtils.isEmpty(c4.getText())){
                        ErrorAlert("Customer name required");
                    } else if(HFBCustomer.isChecked() && TextUtils.isEmpty(c45.getText())){
                        ErrorAlert("Customer phone number required");
                    } else if(Agent.isChecked() && TextUtils.isEmpty(c4.getText())){
                        ErrorAlert("Agent name and number required");
                    } else {
                        am.putMediumRadio(radioGroup.getCheckedRadioButtonId());
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();
                    }

                    *//*if(TextUtils.isEmpty(FatherFirstName.getText())){
                        ErrorAlert("Father's first name required");
                    }
                    else if(TextUtils.isEmpty(FatherLastName.getText())){
                        ErrorAlert("Father's last name required");
                    }
                    else if(TextUtils.isEmpty(MotherFirstName.getText())){
                        ErrorAlert("Mother's first name required");
                    }
                    else if(TextUtils.isEmpty(MotherLastName.getText())){
                        ErrorAlert("Mother's last name required");
                    }
                    else if(TextUtils.isEmpty(Address.getText())){
                        ErrorAlert("Home district required");
                    }
                    else if(Address.getText().length()>25){
                        ErrorAlert("Address length should not be above 25 characters");
                    }
                    else if(TextUtils.isEmpty(YearsAtAddress.getText())){
                        ErrorAlert("Duration of living at that Address required");
                    }
                    else if(!am.getCountry().equals("UGANDATEST")  &&  TextUtils.isEmpty(PoliticallyExposed.getText())){
                        ErrorAlert("Politically exposed required");
                    }
                    else if(am.getCountry().equals("UGANDATEST") && (StringPoliticallyExposed.isEmpty() || political_spin.getSelectedItemPosition()==0)) {
                        ErrorAlert("Politically exposed category required");
                    } else {
                        am.putSaveParentsDurationAddress(addressPeriod.getCheckedRadioButtonId());
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();
                    }*//*
                     alternativeDeposite.addView(alternativeAccountNumber);
                        alternativeDeposite.addView(accountName);
                        alternativeDeposite.addView(bankName);
                        alternativeDeposite.addView(branchName);
                        alternativeDeposite.removeView(AlternatePhoneNumber);
                        alternativeDeposite.removeView(phoneregName);
                        alternativeDeposite.removeView(phoneregLastName);
                }*/
                else if (step_ == 7) {
                    if (alternativeSecurityDeposit.isEmpty()) {
                        ErrorAlert("Select Alternative account");
                    } else if (alternativeSecurityDeposit.equals("Bank Account") && TextUtils.isEmpty(alternativeAccountNumber.getText())) {
                        ErrorAlert("AlternativeAccountNumber is required");
                    } else if (alternativeSecurityDeposit.equals("Bank Account") && TextUtils.isEmpty(accountName.getText())) {
                        ErrorAlert("AccountName is required");
                    } else if (alternativeSecurityDeposit.equals("Bank Account") && TextUtils.isEmpty(bankName.getText())) {
                        ErrorAlert("Bank name is required");
                    } else if (alternativeSecurityDeposit.equals("Bank Account") && TextUtils.isEmpty(branchName.getText())) {
                        ErrorAlert("Branch Name is required");
                    } else if (alternativeSecurityDeposit.equals("Mobile Money") && RGroupM.getCheckedRadioButtonId() == -1) {
                        ErrorAlert("You need to select Mobile money provider");
                    } else if (alternativeSecurityDeposit.equals("Mobile Money") && RGroupCon.getCheckedRadioButtonId() == -1) {
                        ErrorAlert("You need to let us kow if the number is registered to you");
                    } else if (alternativeSecurityDeposit.equals("Mobile Money") && no.isChecked() && TextUtils.isEmpty(phoneregName.getText())) {
                        ErrorAlert("Registered first name is required");

                    } else if (alternativeSecurityDeposit.equals("Mobile Money") && no.isChecked() && TextUtils.isEmpty(phoneregName.getText())) {
                        ErrorAlert("Registered last name is required");
                    } else if (alternativeSecurityDeposit.equals("Mobile Money") && TextUtils.isEmpty(PhoneNumberMobile.getText())) {
                        ErrorAlert("Mobile Number is required");
                    } else {
                        step_++;
                        prev.setVisibility(View.VISIBLE);
                        flipViewIt(step_);
                        flipper.showNext();
                    }
                } else if (step_ == 8) {
                    CustomerCategory = customer_cat_.getSelectedCategory();
                    otpPinView.setEnabled(true);
                    if (TextUtils.isEmpty(EmailAddress.getText())) {
                        ErrorAlert("Email Address required");
                    } else if (!TextUtils.isEmpty(EmailAddress.getText()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(EmailAddress.getText()).matches()) {
                        ErrorAlert("Invalid Email address format");
                    } else if (/*!selectedAccountID.equals("32219") &&*/ PhoneNumber.getText().length() < 5) {
                        ErrorAlert("Invalid phone number");
                    } else if (/*!selectedAccountID.equals("32219") &&*/ !PhoneNumber.getCountryCode().equals("256") && TextUtils.isEmpty(EmailAddress.getText())) {
                        ErrorAlert("Email address required outside Uganda");
                    }  /*else if(TextUtils.isEmpty(eAIdString)) {  //was for address dropdowna
                        ErrorAlert("EA Name required");}*/
//                    else if(TextUtils.isEmpty(maritalStatus.getText())) {
//                        ErrorAlert("Marital Status required");
//                    } 
                    else if (TextUtils.isEmpty(ActualAddress.getText())) {
                        ErrorAlert("Address required");
                    } else if (ActualAddress.getText().length() > 25) {
                        ErrorAlert("Address length should not be above 25 characters");
                    } else if ("32112".equals(selectedAccountID) && singleTitle.getSelectedTitle().equals("Mr")) {
                        ErrorAlert(selectedAccount + " reserved for women");
                    } else if (TextUtils.isEmpty(encodedImageFront)) {
                        ErrorAlert("National id card photo required");
                    } else if (TextUtils.isEmpty(encodedImageBack)) {
                        ErrorAlert("Passport photo required");
                    } else if (TextUtils.isEmpty(encodedImageSelfie)) {
                        ErrorAlert("Signature photo required");
                    } else if (TextUtils.isEmpty(name.getText())) {
                        ErrorAlert("First name required");
                    } else if (name.getText().length() > 20) {
                        ErrorAlert("Invalid First name length max 20 characters");
                    } else if (TextUtils.isEmpty(sname.getText())) {
                        ErrorAlert("Surname name required");
                    } else if (sname.getText().length() > 20) {
                        ErrorAlert("Invalid Surname name length max 20 characters");
                    } else if (TextUtils.isEmpty(nationalID.getText())) {
                        ErrorAlert("National ID required");
                    } else if (TextUtils.isEmpty(DOBEdit.getText())) {
                        ErrorAlert("Date of Birth required");
                    } else if (DOBEdit.getText().trim().contains(" ")) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (!DOBEdit.getText().contains("-")) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (!checkDateFormat(DOBEdit.getText())) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (!Pattern.compile(DATE_PATTERN).matcher(DOBEdit.getText()).matches()) {
                        ErrorAlert("Invalid Date of Birth format. YYYY-MM-DD required. Kindly capture again.");
                    } else if (TextUtils.isEmpty(NextofKinFirstName.getText())) {
                        ErrorAlert("Next of Kin first name required");
                    } else if (TextUtils.isEmpty(NextofKinLastName.getText())) {
                        ErrorAlert("Next of Kin Last name required");
                    } else if (CustomerCategory.equals("")) {
                        ErrorAlert("Select customer type.");
                    } else if (selectedAccount.equals("")) {
                        ErrorAlert("Please select a preferred account");
                    } else if (currName.equals("")) {
                        ErrorAlert("Please select a preferred currency");
                    } else if (branchID.equals("")) {
                        ErrorAlert("Please select a preferred branch");
                    } else if (accountNumber.getVisibility() == View.VISIBLE && TextUtils.isEmpty(accountNumber.getText())) {
                        ErrorAlert("Existing HFB Account Number required");
                    } else if (/*selectedAccountID.equals("32219") &&*/ staffPhoneNumber.getVisibility() == View.VISIBLE && TextUtils.isEmpty(staffPhoneNumber.getText())) {
                        ErrorAlert("Bank Staff Phone Number required");
                    } else if (!radiob.isChecked()) {
                        ErrorAlert("Please accept terms and conditions to proceed");
                    } else if (!tv.isChecked() && !ss.isChecked() && !bankstaff.isChecked() && !HFBCustomer.isChecked() && !Agent.isChecked()) {
                        ErrorAlert("Recommended by required");
                    } else if (tv.isChecked() && TextUtils.isEmpty(c4.getText())) {
                        ErrorAlert("TV/Radio station required");
                    } else if (ss.isChecked() && !FaceBook.isChecked() && !Twitter.isChecked() && !Instagram.isChecked()) {
                        ErrorAlert("Social media platform required");
                    } else if (bankstaff.isChecked() && TextUtils.isEmpty(c4.getText())) {
                        ErrorAlert("Staff name required");
                    } else if (bankstaff.isChecked() && TextUtils.isEmpty(c44.getText())) {
                        ErrorAlert("Staff's branch required");
                    } else if (HFBCustomer.isChecked() && TextUtils.isEmpty(c4.getText())) {
                        ErrorAlert("Customer name required");
                    } else if (HFBCustomer.isChecked() && TextUtils.isEmpty(c45.getText())) {
                        ErrorAlert("Customer phone number required");
                    } else if (Agent.isChecked() && TextUtils.isEmpty(c4.getText())) {
                        ErrorAlert("Agent name and number required");
                    } /*else if(!selectedAccountID.equals("32219") && (!chkMobileBanking.isChecked() || !chkAgencyBanking.isChecked())){
                        ErrorAlert("Kindly check Mobile Banking and Agency Banking");
                    }*/ else {

                        am.putMediumRadio(radioGroup.getCheckedRadioButtonId());

                        RegisterNewListener();

                        String customerMobilenNumber = PhoneNumber.getCountryCode() + PhoneNumber.getText();
                        String alternatePhone = AlternatePhoneNumber.getCountryCode() + AlternatePhoneNumber.getText();

                        String additional = "";
                        /*if(chkMobileBanking.isChecked()){
                            if(!TextUtils.isEmpty(additional)){
                                additional = additional +",";
                            }
                            additional = additional+"Mobile Banking";
                        }

                        if(chkATM.isChecked()){
                            if(!TextUtils.isEmpty(additional)){
                                additional = additional +",";
                            }
                            additional = additional+"Master Card";
                        }

                        if(chkPos.isChecked()){
                            if(!TextUtils.isEmpty(additional)){
                                additional = additional +",";
                            }
                            additional = additional+"Bank Assurance";
                        }
                        if(chkChequeBook.isChecked()){
                            if(!TextUtils.isEmpty(additional)){
                                additional = additional +",";
                            }
                            additional = additional+"Cheque Book";
                        }
                        if(chkInternetBanking.isChecked()){
                            if(!TextUtils.isEmpty(additional)){
                                additional = additional +",";
                            }
                            additional = additional+"Internet Banking";
                        }
                        if(chkAgencyBanking.isChecked()){
                            if(!TextUtils.isEmpty(additional)){
                                additional = additional +",";
                            }
                            additional = additional+"Agent Banking";
                        }*/

                        String recommendation = "";

                        if (ss.isChecked()) {
                            recommendation = "Social Media";
                        }
                        if (tv.isChecked()) {
                            recommendation = "TV/Radio-" + c4.getText();
                        }

                        if (bankstaff.isChecked()) {
                            recommendation = "Bank Staff-" + c4.getText() + "-" + c44.getText();
                        }

                        if (HFBCustomer.isChecked()) {
                            recommendation = "Cente Customer-" + c4.getText() + "-" + c45.getCountryCode() + c45.getText();
                        }

                        if (Agent.isChecked()) {
                            recommendation = "Agent-" + c4.getText();
                        }

                        String gender = "M";
                        if (Usertitle.equals("Mr")) {
                            gender = "M";
                        } else if (Usertitle.equals("Mrs") || Usertitle.equals("Miss")) {
                            gender = "F";
                        }
//                        if (alternativeSecurityDeposit.equals("Bank Account")) {
//                            alternativeDeposite.addView(alternativeAccountNumber);
//                            alternativeDeposite.addView(accountName);
//                            alternativeDeposite.addView(bankName);
//                            alternativeDeposite.addView(branchName);
//                            alternativeDeposite.removeView(PhoneNumberMobile);
//                            alternativeDeposite.removeView(phoneregName);
//                            alternativeDeposite.removeView(phoneregLastName);
//                            textView2.setVisibility(View.GONE);
//                            RGroupM.setVisibility(View.GONE);
//                            textView3.setVisibility(View.GONE);
//                            RGroupCon.setVisibility(View.GONE);
//                        } else if(alternativeSecurityDeposit.equals("Mobile Money")) {
//                            alternativeDeposite.removeView(alternativeAccountNumber);
//                            alternativeDeposite.removeView(accountName);
//                            alternativeDeposite.removeView(bankName);
//                            alternativeDeposite.removeView(branchName);
//                            alternativeDeposite.addView(PhoneNumberMobile);
                        if (alternativeSecurityDeposit.equals("Bank Account")) {
                            INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + accountNumber.getText() + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + name.getText() +
                                    "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + sname.getText() + "|DOB|" + DOBEdit.getText() + "|NATIONALID|" + nationalID.getText() +
                                    "|PHONE_NUMBER|" + customerMobilenNumber + "|ALTERNATE_PHONE_NUMBER|" + alternatePhone + "|EMAIL_ADDRESS|" + EmailAddress.getText() + "|GENDER|" + gender + "|TITLE|" + Usertitle + "|CURRENCY|" + currName + "|BRANCH|" + branchID + "|PRODUCTID|" + selectedAccountID + "|ALTERNATE_ACCOUNT_NUMBER|" + alternativeAccountNumber.getText() + "|ALTERNATE_ACCOUNT_NAME|" + accountName.getText() + "|ALTERNATE_BANKNAME|" + bankName.getText() + "|ALTERNATE_BRANCHNAME|" + branchName.getText() + "|MOBILE_MONEY_PROVIDER|" + "N/A" + "|MOBILE_MONEY_PHONE_OWNER|" + "N/A" + "|MOBILE_MONEY_PHONE_NUMBER|" + "N/A";
                        } else if (alternativeSecurityDeposit.equals("Mobile Money") && no.isChecked()) {
                            INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + accountNumber.getText() + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + name.getText() +
                                    "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + sname.getText() + "|DOB|" + DOBEdit.getText() + "|NATIONALID|" + nationalID.getText() +
                                    "|PHONE_NUMBER|" + customerMobilenNumber + "|ALTERNATE_PHONE_NUMBER|" + alternatePhone + "|EMAIL_ADDRESS|" + EmailAddress.getText() + "|GENDER|" + gender + "|TITLE|" + Usertitle + "|CURRENCY|" + currName + "|BRANCH|" + branchID + "|PRODUCTID|" + selectedAccountID + "|ALTERNATE_ACCOUNT_NUMBER|" + "N/A" + "|ALTERNATE_ACCOUNT_NAME|" + "N/A" + "|ALTERNATE_BANKNAME|" + "N/A" + "|ALTERNATE_BRANCHNAME|" + "N/A" + "|MOBILE_MONEY_PROVIDER|" + mobileMoneyProvider + "|MOBILE_MONEY_PHONE_OWNER|" + phoneregName.getText() + "" + phoneregLastName.getText() + "|MOBILE_MONEY_PHONE_NUMBER|" + PhoneNumberMobile.getText();
                        } else {
                            INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + accountNumber.getText() + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + name.getText() +
                                    "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + sname.getText() + "|DOB|" + DOBEdit.getText() + "|NATIONALID|" + nationalID.getText() +
                                    "|PHONE_NUMBER|" + customerMobilenNumber + "|ALTERNATE_PHONE_NUMBER|" + alternatePhone + "|EMAIL_ADDRESS|" + EmailAddress.getText() + "|GENDER|" + gender + "|TITLE|" + Usertitle + "|CURRENCY|" + currName + "|BRANCH|" + branchID + "|PRODUCTID|" + selectedAccountID + "|ALTERNATE_ACCOUNT_NUMBER|" + "N/A" + "|ALTERNATE_ACCOUNT_NAME|" + "N/A" + "|ALTERNATE_BANKNAME|" + "N/A" + "|ALTERNATE_BRANCHNAME|" + "N/A" + "|MOBILE_MONEY_PROVIDER|" + mobileMoneyProvider + "|MOBILE_MONEY_PHONE_OWNER|" + "N/A" + "|MOBILE_MONEY_PHONE_NUMBER|" + PhoneNumberMobile.getText();
                        }

//                        INFOFIELD1 = "INFOFIELD1:ACCOUNTID|"+ accountNumber.getText() +"|CUSTOMER_CATEGORY|"+CustomerCategory+"|ACCOUNT_TYPE|"+selectedAccount+"|FIRST_NAME|"+ name.getText() +
//                                "|MIDDLE_NAME|"+ otherNames.getText() +"|LAST_NAME|"+ sname.getText() +"|DOB|"+DOBEdit.getText()+"|NATIONALID|"+ nationalID.getText() +
//                                "|PHONE_NUMBER|"+customerMobilenNumber+"|ALTERNATE_PHONE_NUMBER|"+alternatePhone+"|EMAIL_ADDRESS|"+ EmailAddress.getText() +"|GENDER|"+gender+"|TITLE|"+Usertitle+"|CURRENCY|"+currName+"|BRANCH|"+branchID+"|PRODUCTID|"+selectedAccountID;
////                                "|PHONE_NUMBER|"+customerMobilenNumber+"|ALTERNATE_PHONE_NUMBER|"+alternatePhone+"|EMAIL_ADDRESS|"+ EmailAddress.getText() +"|GENDER|"+gender+"|TITLE|"+Usertitle+"|CURRENCY|"+currName+"|BRANCH|"+branchID+"|PRODUCTID|"+selectedAccountID+"|MARITALSTATUS|"+maritalStatus;

                        /*if(am.getCountry().equals("UGANDATEST")){
                            INFOFIELD2 = "INFOFIELD2:FATHER_FIRST_NAME|"+ FatherFirstName.getText() +"|FATHER_MIDDLE_NAME|"+ FatherMiddleName.getText() +"|FATHER_LAST_NAME|"+ FatherLastName.getText() +"|MOTHER_FIRST_NAME|"+ MotherFirstName.getText() +"|MOTHER_MIDDLE_NAME|"+ MotherMiddleName.getText() +"|MOTHER_LAST_NAME|"+ MotherLastName.getText() +"|ADDRESSCODE|"+ eAIdString ;
                        }*/
                        INFOFIELD2 = "INFOFIELD2:FATHER_FIRST_NAME|" + FatherFirstName.getText() + "|FATHER_MIDDLE_NAME|" + FatherMiddleName.getText() + "|FATHER_LAST_NAME|" + FatherLastName.getText() + "|MOTHER_FIRST_NAME|" + MotherFirstName.getText() + "|MOTHER_MIDDLE_NAME|" + MotherMiddleName.getText() + "|MOTHER_LAST_NAME|" + MotherLastName.getText();


                        /*if(am.getCountry().equals("UGANDATEST")){
                            INFOFIELD3 = "INFOFIELD3:CURRENT_LOCATION|"+ c5.getText() +"|ADDRESS|"+ ActualAddress.getText() +"|HOME_DISTRICT|"+ Address.getText() +"|YEARS_AT_ADDRESS|"+ YearsAtAddress.getText().concat(periodAddressString)+"|POLITICALLY_EXPOSED|"+StringPoliticallyExposed+"|CITY|"+city.getText()+"|ZIPCODE|"+zipCode.getText();
                        }*/
                        INFOFIELD3 = "INFOFIELD3:CURRENT_LOCATION|" + c5.getText() + "|ADDRESS|" + ActualAddress.getText() + "|HOME_DISTRICT|" + Address.getText() + "|YEARS_AT_ADDRESS|" + YearsAtAddress.getText().concat(periodAddressString) + "|POLITICALLY_EXPOSED|" + PoliticallyExposed.getText() + "|MARITALSTATUS|" + maritalStatus + "|ZIPCODE|" + zipCode.getText();

                        /*if(am.getCountry().equals("UGANDATEST")){
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|"+ IncomeperAnnum.getText() +"|EMPLOYMENT_TYPE|"+ professionIDString +"|OCCUPATION|"+ occupationIDString +"|PLACE_OF_WORK|"+ PlaceofWork.getText() +"|NATURE_OF_BUSINESS_SECTOR|"+ NatureofBussiness.getText() +"|PERIOD_OF_EMPLOYMENT|"+ PeriodofEmployment.getText().concat(periodWorkString) +"|EMPLOYER_NAME|"+ EmployerName.getText() +"|NATURE|"+ NatureofEmployment.getText();
                        }*/

                        if (employmentType.getSelectedItem().equals("Self-employed/Business")) {
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|" + IncomeperAnnum.getText().toString() + "|EMPLOYMENT_TYPE|" + userEmploymentType + "|OCCUPATION|" + "N/A" + "|PLACE_OF_WORK|" + "N/A" + "|NATURE_OF_BUSINESS_SECTOR|" + NatureofBussiness.getText().toString() + "|PERIOD_OF_EMPLOYMENT|" + "N/A" + "|EMPLOYER_NAME|" + "N/A" + "|NATURE|" + "N/A" + "|BUSINESS_ADDRESS|" + NatureofEmployment.getText().toString();
                        } else if (employmentType.getSelectedItem().equals("Employed/Salary")) {
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|" + MonthlySalary.getText().toString() + "|EMPLOYMENT_TYPE|" + userEmploymentType + "|OCCUPATION|" + Occupation.getText().toString() + "|PLACE_OF_WORK|" + PlaceofWork.getText().toString() + "|NATURE_OF_BUSINESS_SECTOR|" + NatureofBussiness.getText().toString() + "|PERIOD_OF_EMPLOYMENT|" + yearOfEmployment.getText().concat(periodWorkString) + "|EMPLOYER_NAME|" + EmployerName.getText().toString() + "|NATURE|" + "N/A" + "|BUSINESS_ADDRESS|" + PlaceofWork.getText().toString();
                        } else {
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|" + "N/A" + "|EMPLOYMENT_TYPE|" + userEmploymentType + "|OCCUPATION|" + "N/A" + "|PLACE_OF_WORK|" + "N/A" + "|NATURE_OF_BUSINESS_SECTOR|" + "N/A" + "|PERIOD_OF_EMPLOYMENT|" + "N/A" + "|EMPLOYER_NAME|" + "N/A" + "|NATURE|" + PeriodofEmployment.getText().toString() + "|BUSINESS_ADDRESS|" + "N/A";
                        }

                        INFOFIELD5 = "INFOFIELD5:NEXT_OF_KIN_FIRST_NAME|" + NextofKinFirstName.getText() + "|NEXT_OF_KIN_MIDDLE_NAME|" + NextofKinMiddleName.getText() + "|NEXT_OF_KIN_LAST_NAME|" + NextofKinLastName.getText() + "|NEXT_OF_KIN_PHONE_NUMBER|" + NextofKinPhoneNumber.getCountryCode() + NextofKinPhoneNumber.getText() + "|NEXT_OF_KIN_ALTERNATE_PHONE_NUMBER|" + NextofKinAltPhoneNumber.getCountryCode() + NextofKinAltPhoneNumber.getText() + "|NEXT_OF_KIN_ADDRESS|" + "null" + "|OTHER_SERVICES_REQUIRED|" + additional + "|RECOMMENDED_BY|" + recommendation;

                        currentTask = "RequestOTP";
                        createOTP();

                    }
                } else if (step_ == 9) {
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
                Dexter.withActivity(AccountOpenZMain.this)
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
                Dexter.withActivity(AccountOpenZMain.this)
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
                Dexter.withActivity(AccountOpenZMain.this)
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
            case R.id.signature:
                Dexter.withActivity(AccountOpenZMain.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                REQUEST_IMAGEX = 4;
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
//                popupDeposit() ;
                finish();
                startActivity(new Intent(this, DepositOptionsActivity.class));


                break;

        }

    }

    private void uploadSelfyImage(byte[] byteArray1, String self) {
        String url_string = "{" + "\"FormID\":\"LITTLEBUSINESS\"," +
                "\"FileType\":\"jpg\"," +
                "\"ModuleID\":\"" + self + "\"," +
                "\"BankID\":\"LITTLE\"}";

        try {
            image_URL = base_URL + URLEncoder.encode(url_string, "UTF-8");

            Log.d("encoded_url_string", image_URL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            Log.d("encoded_url_error", e.getMessage());
        }


        Log.d("image_url_front", image_URL);


        int max = 100;

        //showProgressBar(0, max);
        //image_URL = base_URL + "%7B%22FormID%22%3A%22BANKIDFRONT%22%2C%22Key%22%3A%22PORTAL-FF7B-4CCA-B884-98346D5EC385%22%2C%22Country%22%3A%22"+preferenceHelper.getCountry()+"%22%2C%22FileType%22%3A%22jpg%22%2C%22MobileNumber%22%3A%22"+preferenceHelper.getDriverMobileNumber()+"%22%2C%22EMailID%22%3A%22"+preferenceHelper.getEmail()+"%22%2C%22ModuleID%22%3A%22CREATECUSTOMER%22%2C%22BankID%22%3A%22"+bank_id+"%22%7D";
        Ion.with(AccountOpenZMain.this)
                .load("POST", image_URL)
                .uploadProgressHandler((uploaded, total) -> runOnUiThread(() -> {
                    Log.d("mercedes_progress-1", uploaded + " of " + total);
                    Double progress = (double) uploaded / (double) total;
                    Log.d("mercedes_progress-2", progress + " / " + max);
                    progress = progress * max;
                    Log.d("mercedes_progress-3", progress + " / " + max);
                    //showProgressBar(progress.intValue(), max);

                    am.progressDialog("1");

                }))
                .setByteArrayBody(byteArray1)
                .asString()
                .setCallback((e, result) -> {
                    //{"Status":"091","Message":"Invalid Form Details 1.0","ImageURL":null}
                    //dismisDialog();
                    if (result != null) {
                        //Upload Success
                        Log.d("mercedes-result", "1" + result);

                    } else {
                        //Upload Failed
                        Log.d("mercedes-else", "2");
                    }
                    if (e != null) {
                        Log.d("mercedes-error", "1" + e.getMessage());
                    }


                    //let me put it outside the result is null check so that when images fail they can still proceed
                    try {
                        removeDialogs();

                        // {"Status":"000","Message":"Data Saved","ImageURL":"https://littleimages.blob.core.windows.net/documents/6F005297-5883-4EAE-A30F-5C9F058CF3E7"}

                        Log.d("upload_selfie_response", result);
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("Status");
                        String message = jsonObject.getString("Message");

                        if (status.equals("000")) {
                            
                            if (jsonObject.has("ImageURL")) {
                                imageURL1 = jsonObject.getString("ImageURL");

                                Log.d("response_idurl", result);
                                validateImageSelfSubmited(imageURL1);

//                                imgCheck.setVisibility(View.VISIBLE);
//
//                                BusinessDocumentsModel businessDocumentsModel = new BusinessDocumentsModel(imageURL, document);
//                                lstDocumentsUrl.add(businessDocumentsModel);

                            }

                        } else if (status.equals("091")) {
                            ErrorAlert(message);
                        }

                    } catch (Exception exception) {
                        Log.d("mercedes-result", "signature-> " + exception.getMessage());

                        am.progressDialog("0");

                    }
                });
        
    }

    private void validateImageSelfSubmited(String imageURL1) {

        String base_URL2 = "https://imageai.azurewebsites.net/ReadDocument.aspx";

        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("RequestID", "SUBMITIMAGE");
            jsonObject1.put("ImageID", "NATIONALID");
            jsonObject1.put("ImageURL", imageURL1);
            jsonObject1.put("Country", "UGANDA");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, base_URL2, jsonObject1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        am.progressDialog("1");
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");

                            if (status.equals("Accepted")) {

                                if (jsonObject.has("ProcessID")) {
                                    processID2 = jsonObject.getString("ProcessID");
                                    Log.d("response_ProcessIDSELF", processID2);

                                    final Handler handler = new Handler(Looper.getMainLooper());
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something after 100ms
                                            faceId1 = "";
                                            submitImageSelf(processID2,imageURL1);
                                        }
                                    }, 10000);


//                                imgCheck.setVisibility(View.VISIBLE);
//
//                                BusinessDocumentsModel businessDocumentsModel = new BusinessDocumentsModel(imageURL, document);
//                                lstDocumentsUrl.add(businessDocumentsModel);

                                }

                            } else if (status.equals("091")) {
                                ErrorAlert(message);
                            }

                        } catch (JSONException e) {
//                            e.printStackTrace();
                            Toast.makeText(AccountOpenZMain.this, "Could not retrieve cliient image from selife", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
        Log.d("IMAGEURL", req.toString());
        
        
    }

    private void submitImageSelf(String processID, String imageURL1) {
       
//        {
//            "RequestID":"GETFACE",
//                "ImageID":"NATIONALID",
//                "ProcessID":"F56F08A7-C678-4B10-82C9-C8E27601AAD8",
//                "ImageURL":"https:\/\/littleimages.blob.core.windows.net\/bankdocuments\/\/0DBFAC18-7617-4627-8629-6A9E5DCC06D7",
//                "Country":"UGANDA"
//        }

        String base_URL2 = "https://imageai.azurewebsites.net/GetFaceFromImage.aspx";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestID", "GETFACE");
            jsonObject.put("ImageID", "NATIONALID");
            jsonObject.put("ProcessID", processID);
            jsonObject.put("ImageURL",imageURL1);
            jsonObject.put("Country", "UGANDA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        am.progressDialog("1");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, base_URL2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        am.progressDialog("0");
                        Log.d("IMEFIKAGATEFACE1",response.toString());
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");
                            
                            
                            String AnalyzeFaceDetails = jsonObject.getString("AnalyzeFaceDetails");
                            
                            JSONArray jsonArrayFields = new JSONArray(AnalyzeFaceDetails);
                            JSONObject jsonFields = jsonArrayFields.getJSONObject(0);
                            if (jsonFields.has("faceId")){
                                faceId1 = jsonFields.getString("faceId");
                            }


                            if (status.equals("000")) {
                                Log.d("responseSELF", faceId1)  ;
                                faceId2 = "";
                                getNationalIdFaceID(processID,imageURL) ;

                            } else if (status.equals("091")) {
                                ErrorAlert(message);
                            }

                        } catch (JSONException e) {
                            ErrorAlert("No client face to compare!!");
                            //e.printStackTrace();
                            
//                            Toast.makeText(AccountOpenZMain.this, "Could not retrieve cliient image from ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
        Log.d("faceId1", jsonObject.toString());

    }

    private void getNationalIdFaceID(String processID,String imageURL) {

//        {
//            "RequestID":"GETFACE",
//                "ImageID":"NATIONALID",
//                "ProcessID":"F56F08A7-C678-4B10-82C9-C8E27601AAD8",
//                "ImageURL":"https:\/\/littleimages.blob.core.windows.net\/bankdocuments\/\/0DBFAC18-7617-4627-8629-6A9E5DCC06D7",
//                "Country":"UGANDA"
//        }

        String base_URL2 = "https://imageai.azurewebsites.net/GetFaceFromImage.aspx";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestID", "GETFACE");
            jsonObject.put("ImageID", "NATIONALID");
            jsonObject.put("ProcessID", processID);
            jsonObject.put("ImageURL",imageURL);
            jsonObject.put("Country", "UGANDA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        am.progressDialog("1");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, base_URL2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        am.progressDialog("0");
                        Log.d("IMEFIKAGATEFACE2",response.toString());
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");
                            String AnalyzeFaceDetails = jsonObject.getString("AnalyzeFaceDetails");

                            JSONArray jsonArrayFields = new JSONArray(AnalyzeFaceDetails);
                            JSONObject jsonFields = jsonArrayFields.getJSONObject(0);
                            if (jsonFields.has("faceId")){
                                faceId2 = jsonFields.getString("faceId");
                            }


                            if (status.equals("000")){
                                if(faceId1==null || faceId2==null || faceId1.equals("")||faceId2.equals("")) {
                                    ErrorAlert("No Image face to compare kindly take a clear selfie!");
//                                    Toast.makeText(AccountOpenZMain.this, "No face to compare", Toast.LENGTH_SHORT).show();
                                } else{
                                    compareFaceId(faceId1,faceId2) ;
                                }
                            }else if (status.equals("091")) {
                                ErrorAlert(message);
                            }

                        } catch (JSONException e) {
                            ErrorAlert("No Image face and ID Card to compare kindly take a clear selfie and upload a valid ID Card!");
//                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
        Log.d("faceId2", jsonObject.toString());



    }

    private void compareFaceId(String faceId1, String faceId2) {
        Log.e("faceID",faceId1 + "-" + faceId2) ;

        String base_URL2 = "https://imageai.azurewebsites.net/CompareFace.aspx";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestID", "COMPAREFACE");
            jsonObject.put("FaceID1", faceId1);
            jsonObject.put("FaceID2", faceId2);
            jsonObject.put("Country", "UGANDA");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("upload_compare_call", jsonObject.toString());

        am.progressDialog("1");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, base_URL2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("COMPARE",response.toString())  ;
                        am.progressDialog("0")   ;
//                        Log.d("upload_compare_response", response.toString());
                        ;
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");
                           
                            if (status.equals("000")){
                                if(jsonObject.has("ConfidenceScore")){
                                    String ConfidenceScore = jsonObject.getString("ConfidenceScore");
                                    if(AllMethods.isNumeric(ConfidenceScore)){
                                        Double cs_score = Double.parseDouble(ConfidenceScore);
                                        DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
                                        //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");
                                        if(cs_score>0.5){
                                            flipper.showNext();
                                            step_++;
                                            flipViewIt(step_);
                                        }else{
                                          ErrorAlert("You did not Match the Image on your Id!"); 
                                        } 

                                    }else{
                                        ErrorAlert("Ensure the photo you take is close on hat is in your ID Card");
                                    }
                                }
                            } else if (status.equals("091")) {
                                am.progressDialog("0");
                                ErrorAlert(message);
                            }

                        } catch (JSONException e) {
                            Log.e("ErrorV",response.toString()) ;
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Test",error.toString()) ;
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
//        Log.d("", req.toString());
        
    }

//    private void popupDeposit() {
//        final Dialog mDialog = new Dialog(AccountOpenZMain.this);
//        //noinspection ConstantConditions
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(R.layout.deposit_options);
//        CheckBox check;
//        final TextView txtTitle = mDialog.findViewById(R.id.message),
//                txtMessage = mDialog.findViewById(R.id.show_response),
//                txtOk = mDialog.findViewById(R.id.done);
//        check = mDialog.findViewById(R.id.check) ;
//        txtTitle.setText(R.string.registerd_n_successfully);
//        txtMessage.setText(R.string.deposit_options);
//
//        txtOk.setOnClickListener(v1 -> {
//            finish();
//            mDialog.dismiss();
//
//
//        });
//        mDialog.show();
//        
//    }

    private void depositMethodes(String message) {

        final Dialog mDialog = new Dialog(AccountOpenZMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_info);
        CheckBox check;
        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title),
                txtMessage = mDialog.findViewById(R.id.dialog_message),
                txtOk = mDialog.findViewById(R.id.dialog_BTN);
        check = mDialog.findViewById(R.id.check);
        txtTitle.setText(R.string.ok);
        txtMessage.setText(message);

        txtOk.setOnClickListener(v1 -> {
            step_++;
            flipViewIt(step_);
            flipper.showNext();
            mDialog.dismiss();


        });
        mDialog.show();

    }

    private void termsConditopnChoiceNew() {
        final Dialog mDialog = new Dialog(AccountOpenZMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_inforao);
        CheckBox check;
        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title),
                txtMessage = mDialog.findViewById(R.id.dialog_message),
                txtOk = mDialog.findViewById(R.id.dialog_BTN);
        check = mDialog.findViewById(R.id.check);
        txtTitle.setText(selectedAccount);
        txtMessage.setText(String.format("Currency   %s\n\nBranch   %s\n\nProductURL %s", currName, branchselect.getSelectedItem().toString().trim(), Uri.parse(currencyURL)));
        txtMessage.setOnClickListener(view1 -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currencyURL)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        txtOk.setOnClickListener(v1 -> {
            if (!check.isChecked()) {
                check.setError("You need to accept terms and conditions");
                Toast.makeText(this, "Terms Can not be unchecked", Toast.LENGTH_SHORT).show();
            } else {
                flipper.showNext();
                step_++;
                flipViewIt(step_);
                mDialog.dismiss();

            }
        });
        mDialog.show();

    }

    private void termsConditopnChoice() {

        final Dialog mDialog = new Dialog(AccountOpenZMain.this);
        //noinspection ConstantConditions
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_inforao);
        CheckBox check;
        final TextView txtTitle = mDialog.findViewById(R.id.dialog_title),
                txtMessage = mDialog.findViewById(R.id.dialog_message),
                txtOk = mDialog.findViewById(R.id.dialog_BTN);
        check = mDialog.findViewById(R.id.check);
        txtTitle.setText(selectedAccount);
        txtMessage.setText(String.format("Currency   %s\n\nBranch   %s\n\nProductURL %s", currName, branchselect.getSelectedItem().toString().trim(), Uri.parse(currencyURL)));
        txtMessage.setOnClickListener(view1 -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currencyURL)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        txtOk.setOnClickListener(v1 -> {
            if (!check.isChecked()) {
                check.setError("You need to accept terms and conditions");
                Toast.makeText(this, "Terms Can not be unchecked", Toast.LENGTH_SHORT).show();
            } else {
//                    step_++;
                flipViewIt(step_);
                finish();
                startActivity(new Intent(this, AccountOpenZExistingCustomersMain.class)
                        .putExtra("accNo", accountNumber.getText().toString())
                        .putExtra("productID", selectedAccountID)
                        .putExtra("productName", selectedAccount)
                        .putExtra("termsURL", termsUrl)
                        .putExtra("currencyURL", currencyURL).putExtra("currency", currName).putExtra("branch", branchID));
//                    selectedAccount = accountNames.get(checkedId);
//                    selectedAccountID = accountIDs.get(checkedId);
//                    termsUrl = listUrls.get(checkedId);


                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            progressBar1.setVisibility(View.VISIBLE);
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