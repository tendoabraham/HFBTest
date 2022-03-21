package com.elmahousingfinanceug_test.main_Pages.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Standing_Orders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CreateStandingOrdersFr extends Fragment {
    View rootView;
    TextView startDate,endDate,create;
    Spinner accountNumber,frequency,whenExe,noBal;
    EditText ETAccount,ETAmount,OrderName;
    int currDay,currMonth,currYear;
    String accSend="",frequencyString="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_create_standing_orders, container, false);

        OrderName = rootView.findViewById(R.id.orderName);
        ETAccount = rootView.findViewById(R.id.ETAccount);
        ETAmount = rootView.findViewById(R.id.ETAmount);
        startDate = rootView.findViewById(R.id.startDate);
        endDate = rootView.findViewById(R.id.endDate);
        frequency = rootView.findViewById(R.id.frequency);
        create = rootView.findViewById(R.id.create);
        accountNumber = rootView.findViewById(R.id.accountNumber);
        whenExe = rootView.findViewById(R.id.timeofday);
        noBal = rootView.findViewById(R.id.ifNobalance);

        assert getActivity() != null;
        ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,((Standing_Orders)getActivity()).am.getAliases());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(adapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accSend = "";
                } else {
                    assert getActivity() != null;
                    accSend = ((Standing_Orders)getActivity()).am.getBankAccountID(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        setDates();

        final List<String> arrayList = new ArrayList<>();
        arrayList.add(rootView.getContext().getString(R.string.selectOne));
        arrayList.add(getString(R.string.daily));
        arrayList.add(getString(R.string.weekly));
        arrayList.add(getString(R.string.fotnight));
        arrayList.add(getString(R.string.monthly));
        arrayList.add(getString(R.string.bi_Month));
        arrayList.add(getString(R.string.qutrly));
        arrayList.add(getString(R.string.bi_annual));
        arrayList.add(getString(R.string.yearly));
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        frequency.setAdapter(dataAdapter2);
        frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    frequencyString = "";
                } else if (position == 1) {
                    frequencyString = "Daily";
                } else if (position == 2) {
                    frequencyString = "Weekly";
                } else if (position == 3) {
                    frequencyString = "Fortnightly";
                } else if (position == 4) {
                    frequencyString = "Monthly";
                } else if (position == 5) {
                    frequencyString = "Bi~Monthly";
                } else if (position == 6) {
                    frequencyString = "Quarterly";
                } else if (position == 7) {
                    frequencyString = "Half~Yearly";
                } else if (position == 8) {
                    frequencyString = "Yearly";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                frequencyString = "";
            }
        });

        final List<String> arrayListexe = new ArrayList<>();
        arrayListexe.add(rootView.getContext().getString(R.string.morning));
        arrayListexe.add(rootView.getContext().getString(R.string.afternoon));
        arrayListexe.add(rootView.getContext().getString(R.string.evening));
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, arrayListexe);
        dataAdapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
        whenExe.setAdapter(dataAdapter3);

        final List<String> arrayListifno = new ArrayList<>();
        arrayListifno.add(rootView.getContext().getString(R.string.ignore));
        arrayListifno.add(rootView.getContext().getString(R.string.keeptrying));
        ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, arrayListifno);
        dataAdapter4.setDropDownViewResource(R.layout.spinner_dropdown_item);
        noBal.setAdapter(dataAdapter4);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getActivity() != null;
                if (OrderName.getText().toString().trim().isEmpty()) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert), getString(R.string.enterOrderName));
                } else if (ETAccount.getText().toString().trim().length()< 5) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert), getString(R.string.enterAccCredited));
                } else if (ETAmount.getText().toString().trim().isEmpty()) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert), getString(R.string.enterValidAmount));
                } else if (accSend.equals("")) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),  getString(R.string.selectAccDebited));
                }  else if (accSend.equals(ETAccount.getText().toString().trim())) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),  getString(R.string.sameAccError));
                } else if (startDate.getText().toString().trim().length() < 1) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert), getString(R.string.startdate));
                }  else if (frequencyString.equals("")) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert), getString(R.string.entValidFreq));
                } else {
                    ((Standing_Orders)getActivity()).gDialog = new Dialog(getActivity());
                    //noinspection ConstantConditions
                    ((Standing_Orders)getActivity()).gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ((Standing_Orders)getActivity()).gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    ((Standing_Orders)getActivity()).gDialog.setContentView(R.layout.dialog_confirm);
                    final TextView txtMessage = ((Standing_Orders)getActivity()).gDialog.findViewById(R.id.dialog_message);
                    final TextView txtOk = ((Standing_Orders)getActivity()).gDialog.findViewById(R.id.yesBTN);
                    final TextView txtNo = ((Standing_Orders)getActivity()).gDialog.findViewById(R.id.noBTN);
                    txtMessage.setText(String.format("%s %s %s %s %s %s %s %s %s %s.",
                            getText(R.string.confirmCrtStOr),
                            ETAccount.getText().toString().trim(),
                            getText(R.string.withAmount),
                            ((Standing_Orders)getActivity()).am.Amount_Thousands(ETAmount.getText().toString().trim()),
                            getText(R.string.tobexec),
                            frequency.getSelectedItem().toString().trim(),
                            getText(R.string.every),
                            whenExe.getSelectedItem().toString().trim(),
                            getText(R.string.from),
                            startDate.getText().toString().trim()));
                    txtOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String quest = (
                                    "FORMID:O-StandingOrderSave:" +
                                            "MERCHANTNAME:TRANSFER:" +
                                            "TOACCOUNT:" + ETAccount.getText().toString().trim() + ":" +
                                            "AMOUNT:" + ETAmount.getText().toString().trim() + ":" +
                                            "PAYMENTS:" + accSend + ":" +
                                            "FIRSTRUNDATE:" + startDate.getText().toString().trim() + ":" +
                                            "EXECUTETIME:" + whenExe.getSelectedItem().toString().trim() + ":" +
                                            "RUNFREQUENCY:" + frequencyString + ":" +
                                            "CONDITION:" + noBal.getSelectedItem().toString().trim() + ":" +
                                            "ORDERNAME:" + OrderName.getText().toString().trim() + ":"
                            );
                            //((Standing_Orders)getActivity()).am.connectOldTwo(getString(R.string.processingReq),quest,((Standing_Orders)getActivity()),"");
                            ((Standing_Orders)getActivity()).am.get(((Standing_Orders)getActivity()), quest, getString(R.string.processingReq),"");
                            ((Standing_Orders)getActivity()).gDialog.cancel();
                        }
                    });
                    txtNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((Standing_Orders)getActivity()).gDialog.cancel();
                        }
                    });
                    assert getActivity() != null;
                    ((Standing_Orders)getActivity()).gDialog.show();
                }
            }
        });

        return rootView;
    }

    private void setDates() {
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                assert getActivity() != null;
                if(selectedYear == currYear) {
                    if(selectedMonth == currMonth && selectedDay<currDay){
                        ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),getString(R.string.errorPastDate));
                        startDate.setText("");
                    } else if (selectedMonth<currMonth){
                        ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),getString(R.string.errorPastDate));
                        startDate.setText("");
                    } else {
                        startDate.setText(String.valueOf(selectedDay).concat("/").concat(String.valueOf(selectedMonth + 1)).concat("/").concat(String.valueOf(selectedYear)));
                    }
                } else if (selectedYear<currYear) {
                    ((Standing_Orders)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),getString(R.string.errorPastDate));
                    startDate.setText("");
                } else {
                    startDate.setText(String.valueOf(selectedDay).concat("/").concat(String.valueOf(selectedMonth + 1)).concat("/").concat(String.valueOf(selectedYear)));
                }
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getActivity() != null;
                endDate.setText("");
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_DeviceDefault_Dialog, datePickerListener,
                        currYear=cal.get(Calendar.YEAR),
                        currMonth=cal.get(Calendar.MONTH),
                        currDay=cal.get(Calendar.DAY_OF_MONTH));
                //noinspection ConstantConditions
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.setCancelable(true);
                datePicker.setTitle(R.string.startdate);
                datePicker.show();
            }
        });
    }
}
