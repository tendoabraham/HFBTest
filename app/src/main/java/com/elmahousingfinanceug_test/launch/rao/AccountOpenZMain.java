package com.elmahousingfinanceug_test.launch.rao;

import static com.elmahousingfinanceug_test.launch.rao.ocr.ImageResultProviderKt.getImageFromStorage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StatFs;
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
import android.widget.ImageView;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.chaos.view.PinView;
import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.launch.rao.ocr.ImageResultProviderKt;
import com.elmahousingfinanceug_test.launch.rao.ocr.OCRState;
import com.elmahousingfinanceug_test.launch.rao.ocr.OCRViewModel;
import com.elmahousingfinanceug_test.main_Pages.Contact_Us;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.example.icebergocr.IcebergSDK;
import com.example.icebergocr.utils.OCRData;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import mumayank.com.airlocationlibrary.AirLocation;

public class AccountOpenZMain extends AppCompatActivity implements ResponseListener, View.OnClickListener {
    TextView title, prev, next, otpcountdown, tv_resend_otp, show_response, textView2, textView3, textViewPEP, pepRelationshipTxt, isFamPE;
    ProgressBar determinateBar, progressBar1;
    private int TAKE_AVATAR_CAMERA_REQUEST = 1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS_THIRD = 0x4;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS_FOURTH = 0x5;
    Bitmap bitmapImageFront, bitmapImageBack, bitmapImageSelfie;
    ViewFlipper flipper;
    ImageView idFront, idBack;
    Bitmap bitmap;

    WebView webView;
    Uri imageUri;
    String nationality = "", sex, surName = "", CardNumber = "", DateOfExpiry = "", NIN = "", dob = "", Name = "", faceId1 = "", imageURL = "", faceId2 = "",
            FirstName = "", LastName = "", Relationship = "", Position = "";
    String status = "", message = "", fields = "", idNumberObject = "", fullName = "", issueDate = "", idSerial = "", birthdate = "";
    ImagePick front, backpick, selfie, signature;
    OCRViewModel ocrViewModel;
    Spinner currselect, branchselect, employmentType, political_spin, occupation_spin, proffession_status, regionselect, districtName,
            countyName, subCountyName, parishName, villageName, eAName, spinnerMulti, alternativeBank, pepPositionSpin, pepRelationshipSpin;
    LinearLayout ccg, currencyLayout, centeidsdetails, centeparentinfo, centesourceincome, centenextofkin, centepoliticallyexposedinfo, pepPeriod, pepDetails,
            centecontacts, centeextras, centehearusfrom, new_Lay, existing_lay, aMr, bMrs, cMiss, alternativeDeposite, PEPDet, otherRelationship, otherPosition;
    ScrollView pan_pin_in;
    cicEditText staffPhoneNumber, accountNumber, name, sname, otherNames, nationalID, nationalIDCardNo, DOBEdit, FatherFirstName, phoneregName, phoneregLastName,
            FatherMiddleName, FatherLastName, MotherFirstName, MotherMiddleName, MotherLastName, Address, YearsAtAddress,
            PoliticallyExposed, IncomeperAnnum, MonthlySalary, EmploymentType, Occupation, yearOfEmployment, PlaceofWork, NatureofBussiness,
            PeriodofEmployment, EmployerName, NatureofEmployment, NextofKinFirstName, NextofKinMiddleName,
            NextofKinLastName, NextofKinPhoneNumber, NextofKinAltPhoneNumber, NextofKinAddress, EmailAddress, PEPFirstName, PEPLastName, PEPInitial,
            PEPTitle, PEPCountry, PEPStartYear, PEPEndYear, PEPOtherPosition, PEPOtherRelationship, PhoneNumber, AlternatePhoneNumber, ActualAddress,
            country, zipCode, c4, c44, c45, c5, alternativeAccountNumber, bankName, branchName, accountName, PhoneNumberMobile;

    EditText et_acc, et_pan, et_pin, et_phone;
    RadioGroup addressPeriod, employPeriod, accountsGroup, radioGroup, genderGroup, RGroupM, RGroupCon, PEGroup, FPEGroup;
    RadioButton yearsButtonM, monthsButtonM, yearsButtonE, monthsButtonE, FaceBook, Twitter,
            Instagram, tv, rd, ss, bankstaff, HFBCustomer, Agent, male, female, mtn, airtel, yes, no, yesPE, noPE, yesFPE, noFPE;
    AppCompatCheckBox chkMobileBanking, chkPos, chkATM, chkChequeBook, chkInternetBanking, chkAgencyBanking, radiob;
    PinView otpPinView;
    HorizontalScrollView scrollAccounts;
    CustomerCat customer_cat_;
    joeSingleChoice singleTitle;
    RelativeLayout success_failed;
    String image_checker = "";

    private AllMethods am;
    private CustomJsonRequest customJsonRequest;

    int REQUEST_IMAGE = 100, REQUEST_IMAGEX = 0, step_ = 0;

    byte[] byteArray;
    byte[] byteArray1;
    private CountDownTimer countDownTimer;
    private long timeOut = 60000;


    String encodedImageFront = "", encodedImageBack = "", encodedImageSelfie = "", encodedImageSignature = "", Usertitle = "", CustomerCategory = "",
            step0 = "Customer Type & Product", step1 = "Personal details?", step2 = "Share with us your ID details", step3 = "Confirm these are your ID details",
            step4 = "Next Of Kin", step5 = "Source of Income",
            step6 = "Terms & Conditions", step7 = "Provide Alternative account number(Bank Account or Mobile Money)", step8 = "Political Exposure", step9 = "How did you come to know about us ?", step10 = "OTP Confirmation", step11 = "Other services",
            step12 = "Parent Information", raoOTP = "", currentTask = "", INFOFIELD1 = "", INFOFIELD2 = "", INFOFIELD3 = "",
            INFOFIELD4 = "", INFOFIELD5 = "", token = "", payload = "", Device = "", uri = "", extrauri = "", new_request = "",
            selectedAccount = "", selectedAccountID = "", currName = "", branchID = "", termsUrl = "", currencyURL, periodAddressString = "", periodWorkString = "", gender = "", mobileMoneyProvider = "", ProductDescription = "",
            StringPoliticallyExposed = "", occupationIDString = "", professionIDString = "", regionIDString = "", districtIDString = "", countyIDString = "",
            subcountyIDString = "", parishIdString = "", villageIdString = "", eAIdString = "", userEmploymentType, maritalStatus, alternativeSecurityDeposit, processID = "", processID2 = "", imageURL1 = "", PEPexposure, FPEPexposure;

    boolean done = false;
    private String[] FieldIDs, FieldValues;

    private AirLocation airLocation;
    Double latitude, longitude;
    String encStringfront, encStringBack;

    CountDownTimer cnt;
    private ActivityResultLauncher<Void> takePhotoLauncher;
    private ActivityResultLauncher<String> chooseFromGalleryLauncher;

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

    private final ActivityResultLauncher<CropImageContractOptions> cropImage
            = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Log.e("ImageURL","hapa") ;
            String uriFilePath = result.getUriFilePath(AccountOpenZMain.this, false);

            // Perform image processing on a background thread
            new Thread(() -> {
                Bitmap bitmap = getImageFromStorage(uriFilePath);

                if (bitmap != null) {
                    try {
                        if (image_checker.equals("selfie")) {
                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream1);
                            byteArray1 = stream1.toByteArray();

                            // Update UI components on the main thread
                            runOnUiThread(() -> {
                                selfie.setImage(bitmap);
                                encodedImageSelfie = ConvertImageToBase64(bitmap);
                                am.putSavedData("encodedImageSelfie", encodedImageSelfie);
                            });
                        } else if (image_checker.equals("signature")) {
                            // Update UI components on the main thread
                            runOnUiThread(() -> {
                                signature.setImage(bitmap);
                                encodedImageSignature = ConvertImageToBase64(bitmap);
                            });
                        }
                    } catch (Exception e) {
                        Log.e("Image3rrr", "thisISSUE:" + e);
                        e.printStackTrace();
                    }
                } else {
                    Exception exception = result.getError();
                    // Update UI components on the main thread
                    runOnUiThread(() -> {
                        Toast.makeText(AccountOpenZMain.this, "Kindly check your Image", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    });

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
        FPEGroup = findViewById(R.id.FPEGroup);
        PEGroup = findViewById(R.id.PEGroup);
        yesPE = findViewById(R.id.yesPE);
        yesFPE = findViewById(R.id.yesFPE);
        noPE = findViewById(R.id.noPE);
        noFPE = findViewById(R.id.noFPE);
        PEPDet = findViewById(R.id.PEPDet);
        pepRelationshipTxt = findViewById(R.id.pepRelationshipTxt);
        isFamPE = findViewById(R.id.isFamPE);
        otherRelationship = findViewById(R.id.otherRelationship);
        otherPosition = findViewById(R.id.otherPosition);

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
        pepPeriod = findViewById(R.id.pepPeriod);
        pepDetails = findViewById(R.id.pepDetails);
        pepPositionSpin = findViewById(R.id.pepPositionSpin);
        pepRelationshipSpin = findViewById(R.id.pepRelationshipSpin);

        ccg = findViewById(R.id.ccg);
        centeidsdetails = findViewById(R.id.centeidsdetails);
        alternativeDeposite = findViewById(R.id.alternativeDeposite);
        centeparentinfo = findViewById(R.id.centeparentinfo);
        centepoliticallyexposedinfo = findViewById(R.id.centepoliticallyexposedinfo);
        centesourceincome = findViewById(R.id.centesourceincome);
        centenextofkin = findViewById(R.id.centenextofkin);
        centecontacts = findViewById(R.id.centecontacts);
        centeextras = findViewById(R.id.centeextras);
        centehearusfrom = findViewById(R.id.centehearusfrom);
        textViewPEP = findViewById(R.id.textViewPEP);
        idFront = findViewById(R.id.frontId);
        idBack = findViewById(R.id.back_id);

        ocrViewModel = ViewModelProviders.of(this).get(OCRViewModel.class);
        ocrViewModel.getIsImageFront().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setImage(idFront, s, "front");
                setImage(idBack, s, "back");


            }
        });

        generateForms();

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
        updatePEPSpinners();

        pepPositionSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.equals("Other")) {
                    otherPosition.setVisibility(View.VISIBLE);
                } else {
                    Position = selectedItem;
                    otherPosition.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pepRelationshipSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                if (selectedItem.equals("Other")) {
                    otherRelationship.setVisibility(View.VISIBLE);
                } else {
                    Relationship = selectedItem;
                    otherRelationship.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void updatePEPSpinners() {
        ArrayAdapter<CharSequence> itemAdapter;
        itemAdapter = ArrayAdapter.createFromResource(this,
                R.array.PEP_Position, android.R.layout.simple_spinner_item);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pepPositionSpin.setAdapter(itemAdapter);

        ArrayAdapter<CharSequence> itemAdapter2;
        itemAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.PEP_Relationship, android.R.layout.simple_spinner_item);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pepRelationshipSpin.setAdapter(itemAdapter2);
    }

    private final BroadcastReceiver populateProducts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context cT, Intent iN) {
            try {

                String smsBody = iN.getStringExtra("Type");

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
                                || am.FindInArray(field_IDs, field_Values, "ProductID").equals("SALCA")
                                || am.FindInArray(field_IDs, field_Values, "ProductID").equals("USAVERS")
                                || am.FindInArray(field_IDs, field_Values, "ProductID").equals("PEAR1")
                                || am.FindInArray(field_IDs, field_Values, "ProductID").equals("PERC1"))) {
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

        EmailAddress = new cicEditText(this, VAR.EMAIL, "Email Address " + getString(R.string.mandatory_field), " acb@domain.com");
        PhoneNumber = new cicEditText(this, VAR.PHONENUMBER, "Phone Number ", " 722222222");
        AlternatePhoneNumber = new cicEditText(this, VAR.PHONENUMBER, " Alternative Phone Number ", " 7333333");
        ActualAddress = new cicEditText(this, VAR.TEXT, "Current Address ", " 123 Kampala");
        country = new cicEditText(this, VAR.TEXT, "Country of residence", " Uganda");
        zipCode = new cicEditText(this, VAR.TEXT, "Country code", " 256");

        centecontacts.addView(EmailAddress);
        centecontacts.addView(PhoneNumber);
        centecontacts.addView(AlternatePhoneNumber);

        centecontacts.addView(country);
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


        IncomeperAnnum = new cicEditText(this, VAR.AMOUNT, "Monthly  income ", "");
        NatureofBussiness = new cicEditText(this, VAR.TEXT, "Nature of Business/Activity Sector ", "");
        NatureofEmployment = new cicEditText(this, VAR.TEXT, "Business Address ", " 123 Kampala");
        PeriodofEmployment = new cicEditText(this, VAR.AMOUNT, "Kindly specify ", "Allowance");
        EmploymentType = new cicEditText(this, VAR.TEXT, "Employment Type ", "");
        MonthlySalary = new cicEditText(this, VAR.AMOUNT, "Monthly salary", "Amount");
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

        PEGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (yesPE.isChecked()) {
                PEPexposure = "Yes";
                Relationship = "Self";
                FirstName = name.getText();
                LastName = sname.getText();
                PEPDet.setVisibility(View.VISIBLE);
                centepoliticallyexposedinfo.setVisibility(View.GONE);
                textViewPEP.setVisibility(View.GONE);
                pepRelationshipSpin.setVisibility(View.GONE);
                pepRelationshipTxt.setVisibility(View.GONE);
                isFamPE.setVisibility(View.GONE);
                FPEGroup.setVisibility(View.GONE);
                FPEGroup.clearCheck();
            } else if (noPE.isChecked()) {
                PEPexposure = "No";
                PEPDet.setVisibility(View.GONE);
                isFamPE.setVisibility(View.VISIBLE);
                FPEGroup.setVisibility(View.VISIBLE);
            }
        });

        FPEGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (yesFPE.isChecked()) {
                FPEPexposure = "Yes";
                PEPexposure = "Yes";
                textViewPEP.setVisibility(View.VISIBLE);
                centepoliticallyexposedinfo.setVisibility(View.VISIBLE);
                PEPDet.setVisibility(View.VISIBLE);
                centepoliticallyexposedinfo.setVisibility(View.VISIBLE);
                textViewPEP.setVisibility(View.VISIBLE);
                pepRelationshipSpin.setVisibility(View.VISIBLE);
                pepRelationshipTxt.setVisibility(View.VISIBLE);
            } else if (noFPE.isChecked()) {
                FPEPexposure = "No";
                textViewPEP.setVisibility(View.GONE);
                centepoliticallyexposedinfo.setVisibility(View.GONE);
                PEPDet.setVisibility(View.GONE);
            }
        });

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

//                    Log.e("userEmployee", userEmploymentType);
                } else {
                    userEmploymentType = "";

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                userEmploymentType = "";
            }
        });

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

        //Politically exposed Details
        PEPFirstName = new cicEditText(this, VAR.TEXT, "First name ", " John");
        PEPLastName = new cicEditText(this, VAR.TEXT, "Last name ", " Kawooya");
        PEPInitial = new cicEditText(this, VAR.TEXT, "Initial", " Initial");
        PEPTitle = new cicEditText(this, VAR.TEXT, "Title of Position Held ", " Title");
        PEPCountry = new cicEditText(this, VAR.TEXT, "Country where the position was held? ", " Country");
        PEPStartYear = new cicEditText(this, VAR.NUMBER, "Starting year ", " Year");
        PEPEndYear = new cicEditText(this, VAR.NUMBER, "Ending year ", " Year");
        PEPOtherPosition = new cicEditText(this, VAR.TEXT, "Specify position held ", " Position");
        PEPOtherRelationship = new cicEditText(this, VAR.TEXT, "Specify your relationship with this person ", " Relationship");

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

        centepoliticallyexposedinfo.addView(PEPFirstName);
        centepoliticallyexposedinfo.addView(PEPLastName);
        centepoliticallyexposedinfo.addView(PEPInitial);

        pepDetails.addView(PEPTitle);
        pepDetails.addView(PEPCountry);

        pepPeriod.addView(PEPStartYear);
        pepPeriod.addView(PEPEndYear);

        otherPosition.addView(PEPOtherPosition);
        otherRelationship.addView(PEPOtherRelationship);

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
                next.setVisibility(View.VISIBLE);
                break;
            case 9:
                title.setText(step9);
                next.setVisibility(View.VISIBLE);
                break;
            case 10:
                title.setText(step10);
                otpPinView.setEnabled(true);
                next.setVisibility(View.VISIBLE);
                break;
            case 11:
                title.setText(step11);
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


    private void niraValidation(OCRData ocrData) {
        new_request = "FORMID:M-:" +
                "MERCHANTID:NIRA:" + "ACCOUNTID:" + nationalID.getText() + ":" +
                "INFOFIELD1:" + ocrData.getSurname() + ":" +
                "INFOFIELD2:" + ocrData.getGivenName() + ":" +
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

    private void removeDialogs() {
        am.progressDialog("0");
    }

    public void ErrorAlert(String Message) {
        am.myDialog(this, getString(R.string.alert), Message);
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

    private void getAddressParam(String paramName, String task, String paramValue) {
        currentTask = task;
        am.get(this, "FORMID:O-GETCUSTOMADDRESS:" +
                "PARAMETERNAME:" + paramName + ":" +
                "PARAMVALUE:" + paramValue + ":" +
                "BANKID:" + am.getBankID() + ":", getString(R.string.loading), currentTask);
    }

    public void RAO() {
        String test = "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";
        new_request =

                "FORMID:M-:" +
                        "MERCHANTID:SELFRAO:" + INFOFIELD1 + ":" + INFOFIELD2 + ":" + INFOFIELD3 + ":" + INFOFIELD4 + ":" + INFOFIELD5 + ":" +
                        "INFOFIELD6:" + encodedImageFront + ":" +
                        "INFOFIELD7:" + encodedImageBack + ":" +
                        "INFOFIELD8:" + encodedImageSelfie+ ":" +
                        "INFOFIELD9:" + encodedImageSignature + ":" +
                        "BANKACCOUNTID:" + am.getCustomerID() + ":" +
                        "ACTION:GETNAME:";
        new Handler().postDelayed(() -> am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), "RAO"), 800);
        Log.e("OCR", new_request);


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
                        "EMAILID:" + EmailAddress.getText().trim() + ":";

                am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), currentTask);
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
                "EMAILID:" + EmailAddress.getText().trim() + ":";
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
        Log.e("Message", message);
        Log.e("ocrpmResponse", response);
        if (step.equals("OCRCPMP")) {


            try {
                String jsonResponse = response;
                JSONObject json = new JSONObject(jsonResponse);

                // Now you can access individual fields like this:
                String status = json.getString("Status");
                String Message = json.getString("Message");

                Log.e("Message", Message);
                Log.e("ocrpmResponse", response);
                if (status.equals("000")) {

                    if (AllMethods.isNumeric(Message)) {
                        Double cs_score = Double.parseDouble(Message);
                        DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
                        //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");
                        if (cs_score > 0.4) {
                            flipper.showNext();
                            step_++;
                            flipViewIt(step_);
                        } else {
                            ErrorAlert("You did not Match the Image on your Id!");
                        }

                    } else {
                        ErrorAlert("Ensure the photo you take is close on hat is in your ID Card");
                    }

                } else if (status.equals("091")) {
                    am.progressDialog("0");
                    ErrorAlert(Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                // Handle the exception, such as invalid JSON.
            }
        } else if (step.equals("OCR-COMPARISON")) {

            try {
                String jsonResponse = response;
                JSONObject json = new JSONObject(jsonResponse);

                // Now you can access individual fields like this:
                String status = json.getString("Status");
                String Message = json.getString("Message");

                Log.e("Message", Message);
                Log.e("ocrpmResponse", response);
                if (status.equals("000")) {

                    if (AllMethods.isNumeric(Message)) {
                        Double cs_score = Double.parseDouble(Message);
                        DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
                        //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");
                        if (cs_score > 0.4) {
                            flipper.showNext();
                            step_++;
                            flipViewIt(step_);
                        } else {
                            ErrorAlert("You did not Match the Image on your Id!");
                        }

                    } else {
                        ErrorAlert("Ensure the photo you take is close on hat is in your ID Card");
                    }

                } else if (status.equals("091")) {
                    am.progressDialog("0");
                    ErrorAlert(Message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                // Handle the exception, such as invalid JSON.
            }

        } else {
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
                            case "OCR":
                                Log.e("Response", response);
                                Log.e("Message", message);
                                break;
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
                                    step_++;
                                    flipViewIt(step_);
                                    flipper.showNext();
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
                                RAO();

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
                    } else if (step_ == 3) {
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
                                    termsConditopnChoice();
                                    break;
                            }
                        }
                    }
                } else if (step_ == 1) {
                    if (TextUtils.isEmpty(EmailAddress.getText().trim())) {
                        ErrorAlert("Email Address required");
                    } else if (!TextUtils.isEmpty(EmailAddress.getText().trim()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(EmailAddress.getText().trim()).matches()) {
                        ErrorAlert("Invalid Email address format kindly avoid spacing at the end or any special characters");
                    } else if (!selectedAccountID.equals("32219") && PhoneNumber.getText().length() < 5) {
                        ErrorAlert("Invalid phone number");
                    } else if (!selectedAccountID.equals("32219") && !PhoneNumber.getCountryCode().equals("256") && TextUtils.isEmpty(EmailAddress.getText().trim())) {
                        ErrorAlert("Email address required outside Uganda");
                    } else if (maritalStatus.isEmpty()) {
                        ErrorAlert("You need to select marital status");
                    } else if (TextUtils.isEmpty(ActualAddress.getText())) {
                        ErrorAlert("Address required");
                    } else if(PhoneNumber.getText().length() != 9){
                        ErrorAlert("Please enter a valid phone number");
                    } else if(AlternatePhoneNumber.getText().length() != 9 && AlternatePhoneNumber.getText().length() != 0){
                        ErrorAlert("Please enter a valid alternative phone number");
                    }
                    else if (ActualAddress.getText().length() > 25) {
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

                        submitImages(new OCRState(this).ocrData());
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
                        // TODO: enable NIRA validation and comment out flipper
//                        niraValidation(new OCRState(this).ocrData());
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();
                    }
                } else if (step_ == 4) {
                    String phoneNum = NextofKinPhoneNumber.getText();
                    if (TextUtils.isEmpty(NextofKinFirstName.getText())) {
                        ErrorAlert("Next of Kin first name required");
                    } else if (TextUtils.isEmpty(NextofKinLastName.getText())) {
                        ErrorAlert("Next of Kin Last name required");
                    }
                    else if(TextUtils.isEmpty(NextofKinPhoneNumber.getText())){
                        ErrorAlert("Next of Kin phone number required");
                    }
                    else if(NextofKinPhoneNumber.getText().length() != 9){
                        ErrorAlert("Please enter a valid phone number");
                    }
                    else if(NextofKinAltPhoneNumber.getText().length() != 9 && NextofKinAltPhoneNumber.getText().length() != 0){
                        ErrorAlert("Please enter a valid alternative phone number");
                    }
                    else {
                        am.putSaveNextofKinCountry(NextofKinPhoneNumber.citizenship.getSelectedItemPosition());
                        am.putSaveAltNextofKinCountry(NextofKinAltPhoneNumber.citizenship.getSelectedItemPosition());
                        step_++;
                        flipViewIt(step_);
                        flipper.showNext();

                    }
                } else if (step_ == 5) {
                    if (userEmploymentType.equals("")) {
                        ErrorAlert("You need to select a source of income");
                    } else if (userEmploymentType.equals("Self-employed/Business") && TextUtils.isEmpty(IncomeperAnnum.getText())) {
                        ErrorAlert("Monthly Income is required");
                    } else if (userEmploymentType.equals("Self-employed/Business") && TextUtils.isEmpty(NatureofEmployment.getText())) {
                        ErrorAlert("Business Address is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(EmployerName.getText())) {
                        ErrorAlert(("EmployerName is required"));
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(Occupation.getText())) {
                        ErrorAlert("Occupation is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(PlaceofWork.getText())) {
                        ErrorAlert("PlaceofWork is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(MonthlySalary.getText())) {
                        ErrorAlert("Monthly Income is required");
                    } else if (userEmploymentType.equals("Employed/Salary") && TextUtils.isEmpty(yearOfEmployment.getText())) {
                        ErrorAlert("Period Of Employment is required");
                    } else if (userEmploymentType.equals("Others") && TextUtils.isEmpty(PeriodofEmployment.getText())) {
                        ErrorAlert("Allowance is required");
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

                    }
                }
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
                    }   else if(PhoneNumberMobile.getText().length() != 9 && PhoneNumberMobile.getText().length() != 0){
                        ErrorAlert("Please enter a valid phone number");
                    }   else if (alternativeSecurityDeposit.equals("Mobile Money") && no.isChecked() && TextUtils.isEmpty(phoneregName.getText())) {
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
                    otpPinView.setEnabled(true);
                    int selectedRadioButtonId = PEGroup.getCheckedRadioButtonId();
                    int selectedRadioButtonId2 = FPEGroup.getCheckedRadioButtonId();
                    String selectedPos = pepPositionSpin.getSelectedItem().toString();
                    String selectedRel = pepRelationshipSpin.getSelectedItem().toString();
                    if (selectedRadioButtonId == -1) {
                        ErrorAlert("Please state if you're politically exposed");
                    } else if (noPE.isChecked() && selectedRadioButtonId2 == -1) {
                        ErrorAlert("Please state if you are connected to someone who is politically exposed");
                    } else if (yesFPE.isChecked() && TextUtils.isEmpty(PEPFirstName.getText().trim())) {
                        ErrorAlert("Please fill in the first name of your politically exposed relative");
                    } else if (yesFPE.isChecked() && TextUtils.isEmpty(PEPLastName.getText().trim())) {
                        ErrorAlert("Please fill in the last name of your politically exposed relative");
                    } else if ((yesPE.isChecked() || yesFPE.isChecked()) && TextUtils.isEmpty(PEPTitle.getText().trim())) {
                        ErrorAlert("Please fill in the title of the senior government, political or military position held");
                    } else if ((yesPE.isChecked() || yesFPE.isChecked()) && TextUtils.isEmpty(PEPCountry.getText().trim())) {
                        ErrorAlert("Please fill in the country where the senior government, political or military position was held");
                    } else if ((yesPE.isChecked() || yesFPE.isChecked()) && TextUtils.isEmpty(PEPStartYear.getText().trim())) {
                        ErrorAlert("Please fill in the starting year ");
                    } else if ((yesPE.isChecked() || yesFPE.isChecked()) && TextUtils.isEmpty(PEPEndYear.getText().trim())) {
                        ErrorAlert("Please fill in the ending year");
                    }
                    else if (yesFPE.isChecked() && selectedRel.equals("Select One")) {
                        ErrorAlert("Please select your relationship with your senior government, political or military relative");
                    } else if (selectedPos.equals("Other") && TextUtils.isEmpty(PEPOtherPosition.getText().trim())) {
                        ErrorAlert("Please specify the senior government, political or military position held");
                    } else if (selectedRel.equals("Other") && TextUtils.isEmpty(PEPOtherRelationship.getText().trim())) {
                        ErrorAlert("Please specify your relationship with your senior government, political or military relative");
                    } else {
                        step_++;
                        prev.setVisibility(View.VISIBLE);
                        flipViewIt(step_);
                        flipper.showNext();
                    }
                } else if (step_ == 9) {
                    CustomerCategory = customer_cat_.getSelectedCategory();
                    int selectedRadioBtnId = radioGroup.getCheckedRadioButtonId();
                    otpPinView.setEnabled(true);
                    if (TextUtils.isEmpty(EmailAddress.getText().trim())) {
                        ErrorAlert("Email Address required");
                    } else if (!TextUtils.isEmpty(EmailAddress.getText().trim()) && !android.util.Patterns.EMAIL_ADDRESS.matcher(EmailAddress.getText().trim()).matches()) {
                        ErrorAlert("Invalid Email address format");
                    } else if (/*!selectedAccountID.equals("32219") &&*/ PhoneNumber.getText().length() < 5) {
                        ErrorAlert("Invalid phone number");
                    } else if (/*!selectedAccountID.equals("32219") &&*/ !PhoneNumber.getCountryCode().equals("256") && TextUtils.isEmpty(EmailAddress.getText().trim())) {
                        ErrorAlert("Email address required outside Uganda");
                    }
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
                    }   else if(staffPhoneNumber.getText().length() != 9 && staffPhoneNumber.getText().length() != 0){
                        ErrorAlert("Please enter a valid Staff phone number");
                    } else if (!radiob.isChecked()) {
                        ErrorAlert("Please accept terms and conditions to proceed");
                    } else if (selectedRadioBtnId == -1) {
                        ErrorAlert("Recommended by required");
                    } else if (tv.isChecked() && TextUtils.isEmpty(c4.getText())) {
                        ErrorAlert("TV station required");
                    } else if (rd.isChecked() && TextUtils.isEmpty(c4.getText())) {
                        ErrorAlert("Radio station required");
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
                    } else if(c45.getText().length() != 9 && c45.getText().length() != 0){
                        ErrorAlert("Please enter a valid Customer phone number");
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

                        if (yesFPE.isChecked()) {
                            FirstName = PEPFirstName.getText();
                            LastName = PEPLastName.getText();

                            String selectedRelationship = pepRelationshipSpin.getSelectedItem().toString();
                            if (selectedRelationship.equals("Other")) {
                                Relationship = PEPOtherRelationship.getText();
                            } else {
                                Relationship = selectedRelationship;
                            }

                        }

                        String selectedPosition = pepPositionSpin.getSelectedItem().toString();
                        if (selectedPosition.equals("Other")) {
                            Position = PEPOtherPosition.getText();
                        } else {
                            if (noPE.isChecked() && noFPE.isChecked()) {
                                Position = "";
                            } else {
                                Position = selectedPosition;
                            }
                        }
                        if (alternativeSecurityDeposit.equals("Bank Account")) {
                            INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + accountNumber.getText() + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + name.getText() +
                                    "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + sname.getText() + "|DOB|" + DOBEdit.getText() + "|NATIONALID|" + nationalID.getText() +
                                    "|PHONE_NUMBER|" + customerMobilenNumber + "|ALTERNATE_PHONE_NUMBER|" + alternatePhone + "|EMAIL_ADDRESS|" + EmailAddress.getText().trim() + "|GENDER|" + gender + "|TITLE|" + Usertitle + "|CURRENCY|" + currName + "|BRANCH|" + branchID + "|PRODUCTID|" + selectedAccountID + "|ALTERNATE_ACCOUNT_NUMBER|" + alternativeAccountNumber.getText() + "|ALTERNATE_ACCOUNT_NAME|" + accountName.getText() + "|ALTERNATE_BANKNAME|" + bankName.getText() + "|ALTERNATE_BRANCHNAME|" + branchName.getText() + "|MOBILE_MONEY_PROVIDER|" + "N/A" + "|MOBILE_MONEY_PHONE_OWNER|" + "N/A" + "|MOBILE_MONEY_PHONE_NUMBER|" + "N/A";
                        } else if (alternativeSecurityDeposit.equals("Mobile Money") && no.isChecked()) {
                            INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + accountNumber.getText() + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + name.getText() +
                                    "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + sname.getText() + "|DOB|" + DOBEdit.getText() + "|NATIONALID|" + nationalID.getText() +
                                    "|PHONE_NUMBER|" + customerMobilenNumber + "|ALTERNATE_PHONE_NUMBER|" + alternatePhone + "|EMAIL_ADDRESS|" + EmailAddress.getText().trim() + "|GENDER|" + gender + "|TITLE|" + Usertitle + "|CURRENCY|" + currName + "|BRANCH|" + branchID + "|PRODUCTID|" + selectedAccountID + "|ALTERNATE_ACCOUNT_NUMBER|" + "N/A" + "|ALTERNATE_ACCOUNT_NAME|" + "N/A" + "|ALTERNATE_BANKNAME|" + "N/A" + "|ALTERNATE_BRANCHNAME|" + "N/A" + "|MOBILE_MONEY_PROVIDER|" + mobileMoneyProvider + "|MOBILE_MONEY_PHONE_OWNER|" + phoneregName.getText() + "" + phoneregLastName.getText() + "|MOBILE_MONEY_PHONE_NUMBER|" + PhoneNumberMobile.getText();
                        } else {
                            INFOFIELD1 = "INFOFIELD1:ACCOUNTID|" + accountNumber.getText() + "|CUSTOMER_CATEGORY|" + CustomerCategory + "|ACCOUNT_TYPE|" + selectedAccount + "|FIRST_NAME|" + name.getText() +
                                    "|MIDDLE_NAME|" + otherNames.getText() + "|LAST_NAME|" + sname.getText() + "|DOB|" + DOBEdit.getText() + "|NATIONALID|" + nationalID.getText() +
                                    "|PHONE_NUMBER|" + customerMobilenNumber + "|ALTERNATE_PHONE_NUMBER|" + alternatePhone + "|EMAIL_ADDRESS|" + EmailAddress.getText().trim() + "|GENDER|" + gender + "|TITLE|" + Usertitle + "|CURRENCY|" + currName + "|BRANCH|" + branchID + "|PRODUCTID|" + selectedAccountID + "|ALTERNATE_ACCOUNT_NUMBER|" + "N/A" + "|ALTERNATE_ACCOUNT_NAME|" + "N/A" + "|ALTERNATE_BANKNAME|" + "N/A" + "|ALTERNATE_BRANCHNAME|" + "N/A" + "|MOBILE_MONEY_PROVIDER|" + mobileMoneyProvider + "|MOBILE_MONEY_PHONE_OWNER|" + "N/A" + "|MOBILE_MONEY_PHONE_NUMBER|" + PhoneNumberMobile.getText();
                        }

                        INFOFIELD2 = "INFOFIELD2:FATHER_FIRST_NAME|" + FatherFirstName.getText() + "|FATHER_MIDDLE_NAME|" + FatherMiddleName.getText() + "|FATHER_LAST_NAME|" + FatherLastName.getText() + "|MOTHER_FIRST_NAME|" + MotherFirstName.getText() + "|MOTHER_MIDDLE_NAME|" + MotherMiddleName.getText() + "|MOTHER_LAST_NAME|" + MotherLastName.getText();


                        INFOFIELD3 = "INFOFIELD3:CURRENT_LOCATION|" + c5.getText() + "|ADDRESS|" + ActualAddress.getText() + "|HOME_DISTRICT|" + Address.getText() + "|YEARS_AT_ADDRESS|" + YearsAtAddress.getText().concat(periodAddressString) +
                                "|POLITICALLY_EXPOSED|" + PEPexposure + "|POLITICALLY_EXPOSED_FIRSTNAME|" + FirstName + "|POLITICALLY_EXPOSED_LASTNAME|" + LastName + "|POLITICALLY_EXPOSED_POSITION|" + Position + "|POLITICALLY_EXPOSED_INITIAL|" + PEPInitial.getText() + "|POLITICALLY_EXPOSED_RELATIONSHIP|" + Relationship + "|POLITICALLY_EXPOSED_TITLE|" + PEPTitle.getText() + "|POLITICALLY_EXPOSED_COUNTRY|" + PEPCountry.getText() + "|POLITICALLY_EXPOSED_START_YEAR|" + PEPStartYear.getText() + "|POLITICALLY_EXPOSED_END_YEAR|" + PEPEndYear.getText() +
                                "|MARITALSTATUS|" + maritalStatus + "|ZIPCODE|" + zipCode.getText();

                        if (employmentType.getSelectedItem().equals("Self-employed/Business")) {
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|" + IncomeperAnnum.getText().toString() + "|EMPLOYMENT_TYPE|" + userEmploymentType + "|OCCUPATION|" + "N/A" + "|PLACE_OF_WORK|" + "N/A" + "|NATURE_OF_BUSINESS_SECTOR|" + NatureofBussiness.getText().toString() + "|PERIOD_OF_EMPLOYMENT|" + "N/A" + "|EMPLOYER_NAME|" + "N/A" + "|NATURE|" + "N/A" + "|BUSINESS_ADDRESS|" + NatureofEmployment.getText().toString();
                        } else if (employmentType.getSelectedItem().equals("Employed/Salary")) {
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|" + MonthlySalary.getText().toString() + "|EMPLOYMENT_TYPE|" + userEmploymentType + "|OCCUPATION|" + Occupation.getText().toString() + "|PLACE_OF_WORK|" + PlaceofWork.getText().toString() + "|NATURE_OF_BUSINESS_SECTOR|" + NatureofBussiness.getText().toString() + "|PERIOD_OF_EMPLOYMENT|" + yearOfEmployment.getText().concat(periodWorkString) + "|EMPLOYER_NAME|" + EmployerName.getText().toString() + "|NATURE|" + "N/A" + "|BUSINESS_ADDRESS|" + PlaceofWork.getText().toString();
                        } else {
                            INFOFIELD4 = "INFOFIELD4:INCOME_PER_ANUM|" + PeriodofEmployment.getText().toString() + "|EMPLOYMENT_TYPE|" + userEmploymentType + "|OCCUPATION|" + "N/A" + "|PLACE_OF_WORK|" + "N/A" + "|NATURE_OF_BUSINESS_SECTOR|" + "N/A" + "|PERIOD_OF_EMPLOYMENT|" + "N/A" + "|EMPLOYER_NAME|" + "N/A" + "|NATURE|" + "N/A" + "|BUSINESS_ADDRESS|" + "N/A";
                        }

                        INFOFIELD5 = "INFOFIELD5:NEXT_OF_KIN_FIRST_NAME|" + NextofKinFirstName.getText() + "|NEXT_OF_KIN_MIDDLE_NAME|" + NextofKinMiddleName.getText() + "|NEXT_OF_KIN_LAST_NAME|" + NextofKinLastName.getText() + "|NEXT_OF_KIN_PHONE_NUMBER|" + NextofKinPhoneNumber.getCountryCode() + NextofKinPhoneNumber.getText() + "|NEXT_OF_KIN_ALTERNATE_PHONE_NUMBER|" + NextofKinAltPhoneNumber.getCountryCode() + NextofKinAltPhoneNumber.getText() + "|NEXT_OF_KIN_ADDRESS|" + "null" + "|OTHER_SERVICES_REQUIRED|" + additional + "|RECOMMENDED_BY|" + recommendation;

                        currentTask = "RequestOTP";
                        createOTP();

                        step_++;
                        prev.setVisibility(View.VISIBLE);
                        flipViewIt(step_);
                        flipper.showNext();
                    }
                } else if (step_ == 10) {
                    if (TextUtils.isEmpty(raoOTP)) {
                        ErrorAlert("One time password required");
                    } else if (raoOTP.length() < 6) {
                        ErrorAlert("Otp can not be less than 6 digits");
                    } else {
                        verify();
                    }
                }

                break;
            case R.id.front:
                new IcebergSDK.Builder(AccountOpenZMain.this)
                        .ActionType("idFront")
                        .Country("UGANDA")
                        .ScanDoneClass(OcrResults.class)
                        .AppName(am.getAppName())
                        .init();
                break;
            case R.id.selfie: {
                image_checker = "selfie";
                cropImage.launch(ImageResultProviderKt.options());
                break;
            }
            case R.id.backpick:
                break;
            case R.id.signature:

                if (isStorageAvailable()) {
                    image_checker = "signature";
                    cropImage.launch(ImageResultProviderKt.options());
                } else {
                    // Display an error message
                    Toast.makeText(getApplicationContext(), "Not enough storage space.", Toast.LENGTH_SHORT).show();
                }
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

    private void submitImages(OCRData ocrData) {
        String customerMobilenNumber = "";
        if (selectedAccountID.equals("32219")) {
            customerMobilenNumber = staffPhoneNumber.getCountryCode() + staffPhoneNumber.getText();
        } else {
            customerMobilenNumber = PhoneNumber.getCountryCode() + PhoneNumber.getText();
        }

        // Parse the input date with the correct format
        LocalDate birthDate = LocalDate.parse(ocrData.getDateOfBirth(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Format the birth date to yyyy-MM-dd
        String formattedBirthDate = birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Set the updated birth date back to the Person object (replace "dob" with your variable)
        String dob = formattedBirthDate;




        sname.setText(ocrData.getSurname());
        name.setText(ocrData.getGivenName());
        nationalID.setText(ocrData.getNin());
        DOBEdit.setText(dob);
        nationalIDCardNo.setText(ocrData.getNin());
        otherNames.setText(ocrData.getGivenName());

        new_request = "FORMID:M-:" +
                "MERCHANTID:OCR:" +
                "INFOFIELD6:" + encodedImageSelfie + ":" +
                "INFOFIELD7:" + ocrData.getEncodedImageFront() + ":" +
                "ACTION:GETNAME:";
        writeToFile("ocrlogug.txt",new_request);




        am.get(AccountOpenZMain.this, new_request, getString(R.string.loading), "OCRCPMP");

    }




    private boolean isStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
            long megAvailable = bytesAvailable / (1024 * 1024);

            // You can set a threshold here, like 10MB
            long threshold = 10;

            return megAvailable > threshold;
        } else {
            // Storage is not available
            return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        OCRData ocrData = new OCRState(this).ocrData();
        if (ocrData != null) {
            // Decode and compress the front image
            Bitmap decodedFrontBitmap = decodeBase64ToBitmap(ocrData.getEncodedImageFront());
            Bitmap compressedFrontBitmap = compressBitmap(decodedFrontBitmap, 80);
            front.setImage(compressedFrontBitmap);
            ocrViewModel.setIsImageFront(ocrData.getEncodedImageFront());

            encodedImageFront = ConvertImageToBase64Ocr(compressAndResizeBitmap(decodedFrontBitmap, 400, 400,20));

            // Decode and compress the back image
            Bitmap decodedBackBitmap = decodeBase64ToBitmap(ocrData.getEncodedImageBack());
            Bitmap compressedBackBitmap = compressBitmap(decodedBackBitmap, 80);
            backpick.setImage(compressedBackBitmap);
            encodedImageBack = ConvertImageToBase64Ocr(compressAndResizeBitmap(decodedFrontBitmap, 400, 400,20));

            encodedImageBack = encodeBitmapToBase64(compressedBackBitmap);
            ocrViewModel.setIsImageFront(ocrData.getEncodedImageBack());
        }
    }

    private Bitmap compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Try to compress the bitmap with the given quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);

        // Keep compressing until the desired size is achieved
        while (byteArrayOutputStream.toByteArray().length / 1024 > 600 && quality > 0) {
            // Reset the stream for another compression attempt
            byteArrayOutputStream.reset();

            // Decrease the quality and try again
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        }

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


    public Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream); // Adjust the compression quality as needed
        byte[] compressedBytes = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.length);
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // You can adjust the compression quality here as needed
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void submitImageSelf(String processID, String imageURL1) {
        String base_URL2 = "https://imageai.azurewebsites.net/GetFaceFromImage.aspx";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestID", "GETFACE");
            jsonObject.put("ImageID", "NATIONALID");
            jsonObject.put("ProcessID", processID);
            jsonObject.put("ImageURL", imageURL1);
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
                        Log.d("IMEFIKAGATEFACE1", response.toString());
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");


                            String AnalyzeFaceDetails = jsonObject.getString("AnalyzeFaceDetails");

                            JSONArray jsonArrayFields = new JSONArray(AnalyzeFaceDetails);
                            JSONObject jsonFields = jsonArrayFields.getJSONObject(0);
                            if (jsonFields.has("faceId")) {
                                faceId1 = jsonFields.getString("faceId");
                            }


                            if (status.equals("000")) {
                                Log.d("responseSELF", faceId1);
                                faceId2 = "";
                                getNationalIdFaceID(processID, imageURL);

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
//                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
        Log.d("faceId1", jsonObject.toString());

    }

    private void getNationalIdFaceID(String processID, String imageURL) {

        String base_URL2 = "https://imageai.azurewebsites.net/GetFaceFromImage.aspx";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("RequestID", "GETFACE");
            jsonObject.put("ImageID", "NATIONALID");
            jsonObject.put("ProcessID", processID);
            jsonObject.put("ImageURL", imageURL);
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
                        Log.d("IMEFIKAGATEFACE2", response.toString());
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");
                            String AnalyzeFaceDetails = jsonObject.getString("AnalyzeFaceDetails");

                            JSONArray jsonArrayFields = new JSONArray(AnalyzeFaceDetails);
                            JSONObject jsonFields = jsonArrayFields.getJSONObject(0);
                            if (jsonFields.has("faceId")) {
                                faceId2 = jsonFields.getString("faceId");
                            }


                            if (status.equals("000")) {
                                if (faceId1 == null || faceId2 == null || faceId1.equals("") || faceId2.equals("")) {
                                    ErrorAlert("No Image face to compare kindly take a clear selfie!");
//                                    Toast.makeText(AccountOpenZMain.this, "No face to compare", Toast.LENGTH_SHORT).show();
                                } else {
                                    compareFaceId(faceId1, faceId2);
                                }
                            } else if (status.equals("091")) {
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
//                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
        Log.d("faceId2", jsonObject.toString());


    }

    private void compareFaceId(String faceId1, String faceId2) {
//        Log.e("faceID", faceId1 + "-" + faceId2);

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
//                        Log.e("COMPARE", response.toString());
                        am.progressDialog("0");
//                        Log.d("upload_compare_response", response.toString());
                        ;
//                        {"Status":"Accepted","Message":"Accepted","RequestID":"37236775-bf86-4240-a1db-8baafc33cbd1","ProcessID":"4C22913D-CDEA-480F-B4B1-2A1F703F110C"}
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            String status = jsonObject.getString("Status");
                            String message = jsonObject.getString("Message");

                            if (status.equals("000")) {
                                if (jsonObject.has("ConfidenceScore")) {
                                    String ConfidenceScore = jsonObject.getString("ConfidenceScore");
                                    if (AllMethods.isNumeric(ConfidenceScore)) {
                                        Double cs_score = Double.parseDouble(ConfidenceScore);
                                        DecimalFormat formatter = new DecimalFormat("#,###,##0.00");//here 0.00 instead #.##
                                        //txtSelfieText.setText(formatter.format(cs_score)+"\nmatch");
                                        if (cs_score > 0.5) {
                                            flipper.showNext();
                                            step_++;
                                            flipViewIt(step_);
                                        } else {
                                            ErrorAlert("You did not Match the Image on your Id!");
                                        }

                                    } else {
                                        ErrorAlert("Ensure the photo you take is close on hat is in your ID Card");
                                    }
                                }
                            } else if (status.equals("091")) {
                                am.progressDialog("0");
                                ErrorAlert(message);
                            }

                        } catch (JSONException e) {
//                            Log.e("ErrorV", response.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Test",error.toString()) ;
//                VolleyLog.e("Error: ", error.getMessage());
            }
        });

// add the request object to the queue to be executed
        RequestQueue queue = Volley.newRequestQueue(AccountOpenZMain.this);
        queue.add(req);
//        Log.d("", req.toString());

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

    public static byte[] getBitmapBytes(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, bytes);
        return bytes.toByteArray();
    }

    private void setImage(ImageView image, String encodedData, String type) {
        byte[] decodedString = Base64.decode(encodedData, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        //Using Glide
        Glide.with(this)
                .asBitmap()
                .load(decodedByte)
                .centerCrop()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (type.equalsIgnoreCase("front")) {

                            encStringfront = ConvertImageToBase64Ocr(getBitmapFromCanvas(resource));
                        } else {
                            encStringBack = ConvertImageToBase64Ocr(getBitmapFromCanvas(resource));

                        }


                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    private  Bitmap getBitmapFromCanvas(Bitmap srcBmp) {
        Bitmap dstBmp;

        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;

    }


    public static String ConvertImageToBase64Ocr(Bitmap bitmap) {
        String generatedBase64 = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream2);
            byte[] bytes2 = byteArrayOutputStream2.toByteArray();
            generatedBase64 = Base64.encodeToString(bytes2, Base64.NO_WRAP);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return generatedBase64;
    }
    public void appendLog(String text) {
        File logFile = new File("sdcard/ocrlogug.file");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void writeToFile(String filename, String data) {
        FileOutputStream outputStream;

        try {
            // Open the file stream for writing
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

            // Write the data to the file
            outputStream.write(data.getBytes());

            // Close the file stream
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate the scale factor
        float scaleWidth = ((float) targetWidth) / width;
        float scaleHeight = ((float) targetHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Resize the bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    private Bitmap compressAndResizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight, int quality) {
        // Resize the bitmap before compressing
        Bitmap resizedBitmap = resizeBitmap(bitmap, targetWidth, targetHeight);

        // Compress the resized bitmap
        return compressBitmap(resizedBitmap, quality);
    }
}