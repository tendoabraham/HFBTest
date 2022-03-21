package com.elmahousingfinanceug_test.launch.rao;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;

import java.util.ArrayList;
import java.util.List;

public class cicEditText extends RelativeLayout {
    public EditText editText;
    LinearLayout phoneview,back;
    public Spinner citizenship;
    TextView textView,error,plusCode;
    TextView tag;
    int min, max;
    boolean required=  true;
    String regex;
    String minDate, maxDate;
    int val;
    Context c;
    boolean isDate = false;
    List<InputTypeItem> inputTypes;
    private int mYear, mMonth, mDay;
    int type;
    String label;
    String hint,selectedCountryCode="256";
    String [] Country = {"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Cote d\'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "North Korea", "South Korea", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands", "Nevis", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Marianas", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "São Tomé and Príncipe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu", "Venezuela", "Vatican City", "Vietnam", "Wallis and Futuna", "Yemen", "Zambia", "Zanzibar", "Zimbabwe"};
    String [] CountryCode = {"93", "355", "213", "1684", "376", "244", "1264", "1268", "54", "374", "297", "61", "43", "994", "973", "880", "1246", "375", "32", "501", "229", "1441", "975", "591", "387", "267", "55", "246", "1284", "673", "359", "226", "257", "855", "237", "1", "238", "1345", "236", "235", "56", "86", "61", "61", "57", "269", "242", "682", "506", "225", "385", "53", "357", "420", "243", "45", "253", "1767", "1809", "670", "593", "20", "503", "240", "291", "372", "251", "500", "298", "679", "358", "33", "594", "689", "241", "220", "995", "49", "233", "350", "30", "299", "1473", "590", "1671", "502", "224", "245", "592", "509", "504", "852", "36", "354", "91", "62", "98", "964", "353", "972", "39", "1876", "81", "44", "962", "76", "254", "686", "850", "82", "965", "996", "856", "371", "961", "266", "231", "218", "423", "370", "352", "853", "389", "261", "265", "60", "960", "223", "356", "692", "596", "222", "230", "262", "52", "691", "373", "377", "976", "382", "1664", "212", "258", "264", "674", "977", "31", "1869", "687", "64", "505", "227", "234", "683", "672", "1670", "47", "968", "92", "680", "970", "507", "675", "595", "51", "63", "64", "48", "351", "1787", "974", "262", "40", "7", "250", "290", "1869", "1758", "590", "508", "1784", "685", "378", "239", "966", "221", "381", "248", "232", "65", "421", "386", "677", "252", "27", "500", "211", "34", "94", "249", "597", "4779", "268", "46", "41", "963", "886", "992", "255", "66", "228", "690", "676", "1868", "216", "90", "993", "1649", "688", "256", "380", "971", "44", "1", "598", "1340", "998", "678", "58", "379", "84", "681", "967", "260", "255", "263"};


    public cicEditText(Context context, int type, String label, String hint) {
        super(context);
        this.c = context;
        this.label = label;
        this.type = type;
        this.hint = hint;
        init(context, null);
    }

    public cicEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public cicEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        inflate(getContext(), R.layout.joe_edittext, this);
        this.editText = findViewById(R.id.editText);
        this.textView = findViewById(R.id.textView);
        this.back = findViewById(R.id.back);
        this.tag = findViewById(R.id.textViewtag);
        this.phoneview = findViewById(R.id.phoneview);
        this.citizenship = findViewById(R.id.citizenship);
        this.plusCode = findViewById(R.id.plusCode);


        final AllMethods p = new AllMethods(c);

        textView.setText(label);
        editText.setHint(hint);
        if (p.getProceed()) {
         editText.setText(p.getSavedData(label));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, R.layout.spinner_item, Country);
        citizenship.setAdapter(adapter);
        citizenship.setSelection(208 + 11);
        citizenship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountryCode = CountryCode[position];
                plusCode.setText(String.format("+%s", selectedCountryCode));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //countrycode.setText("+"+CountryCode[106]);
            }
        });

        setType(type);

        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.cicEditText);
            try {
                if (typedArray.hasValue(R.styleable.cicEditText_label)) {
                    textView.setText(typedArray.getString(R.styleable.cicEditText_label));
                }
                if (typedArray.hasValue(R.styleable.cicEditText_text)) {
                    editText.setText(typedArray.getString(R.styleable.cicEditText_text));
                }
                if (typedArray.hasValue(R.styleable.cicEditText_android_inputType)) {
                    editText.setInputType(typedArray.getInt(typedArray.getIndex(R.styleable.cicEditText_android_inputType), EditorInfo.TYPE_TEXT_VARIATION_NORMAL));
                }
            } finally {
                typedArray.recycle();
            }
        }

        if(!isDate) {
            editText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        back.setBackground(getResources().getDrawable(R.drawable.edittextbackgroundfocused));
                    } else {
                        back.setBackground(getResources().getDrawable(R.drawable.edittextbackground));
                    }
                }
            });
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                p.putSavedData(label,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public String getText() {
        return editText.getText().toString().replace(",", "");
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public String getTag() {
        return tag.getText().toString();
    }

    public void setTag(String label) {
        tag.setText(label);
    }

    public void setType(int counter) {
        val = counter;
        initArray();
        if (counter == VAR.TEXT) {
            editText.setInputType(inputTypes.get(3).value);
        } else if (counter == VAR.AMOUNT) {
            setAmount();
            editText.setInputType(inputTypes.get(0).value);
        }
        else if (counter == VAR.PHONENUMBER) {
            phoneview.setVisibility(VISIBLE);
            editText.setInputType(inputTypes.get(1).value);
        }
        else {
            editText.setInputType(inputTypes.get(3).value);
        }
    }

    private void setAmount() {
        editText.addTextChangedListener(new NumberTextWatcherForThousand(editText));
    }

    private void initArray() {
        /*  */
        inputTypes = new ArrayList<>();
        /* 0*/
        inputTypes.add(new InputTypeItem("number", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL));
        /* 1*/
        inputTypes.add(new InputTypeItem("phone", InputType.TYPE_CLASS_PHONE));
        /* 2*/
        inputTypes.add(new InputTypeItem("textPassword", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
        /* 3*/
        inputTypes.add(new InputTypeItem("text", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS));
        /* 4*/
        inputTypes.add(new InputTypeItem("textEmailAddress", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
    }

    public boolean isValid() {
        boolean results = false;
        String s = editText.getText().toString();


        if (s.length() < 1) {
            editText.setError(getLabel() + " Cannot be empty");
            editText.setBackground(getResources().getDrawable(R.drawable.edittextbackgrounderror));
            return results;
        }
        switch (val) {
            case 0:
                if (s.length() > 0)
                    results = true;
                else {
                    error.setError(getLabel() + " is Required");
                }
                break;
            case 1:
                if (s.length() > 8)
                    results = true;
                else {
                    error.setError("a valid " + getLabel() + " is Required");
                }
                break;
            case 2:
                break;
            case VAR.TEXT:
                if (s.length() > 0)
                    results = true;
                else {
                    error.setError(getLabel() + " cannot be empty");
                }
                break;
            case 4:
                if (ValidateEmail(s))
                    results = true;
                else {
                    error.setError("Valid Email Address is required");
                }
                break;
            case VAR.AMOUNT:
                if (isAmountValid(s))
                    results = true;
                else {
                }
                break;
            case VAR.REG:
                if (s.length() < max && s.length() > min) {
                    if (s.matches(regex)) {
                        error.setText("Matches Regex");
                        return true;
                    } else {
                        error.setText("Does not Match Regex");
                        return false;
                    }
                } else {
                    error.setText( "lenght:" + s.length() + ":max:" + max + ":min:" + min);
                }
                break;
        }

        return results;

    }

    private String getLabel() {
        return textView.getText().toString();
    }

    public void setLabel(String label) {
        textView.setText(label);
        editText.setHint(label);
        //editText.setFloatingLabelText(label);
    }

    public String getCountryCode() {
        return selectedCountryCode;
    }

    public void setRequired(boolean label) {
        required = label;
    }
    public boolean getRequired() {
        return required;
    }

    public boolean ValidateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern) || email.matches(emailPattern2) || email.contains(".com.com");
    }

    private boolean isAmountValid(String amountEntered) {
        int amount = 0;
        try {
            amount = Integer.parseInt(amountEntered.replace(",", ""));
        } catch (Exception e) {
            editText.setError("Amount you entered is not valid " + max);
            error.setText( editText.getError());
        }
        if (amount < min) {
            error.setText("Amount cannot be less than " + min);
            return false;
        }
        if (amount > max) {
            error.setText("Amount cannot be more than " + max);
            return false;
        }
        return true;
    }


    private String dateFormat(String dateEntered) {
        String res = dateEntered;
        return res.replace("00:00:00 GMT+03:00","");
    }

    public void setRegex(String reg) {
        regex = reg;
    }


    class InputTypeItem {
        private String name;
        private int value;

        InputTypeItem(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    public void setEnabled(boolean choice)
    {
        editText.setEnabled(choice);
    }
}
