package com.elmahousingfinanceug_test.main_Pages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

import java.util.Calendar;
import java.util.TimeZone;

public class E_Statement extends BaseAct implements ResponseListener, VolleyResponse {
    TextView startDate,endDate;
    Spinner accountNumber;
    String accSend = "";
    private int currDay,currMonth,currYear,startDay,startMonth,startYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_statement);

        gToolBar(getString(R.string.estmnt));

        accountNumber = findViewById(R.id.accountNumber);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accSend = "";
                } else {
                    accSend = am.getBankAccountID(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        setDates();
    }

    private void setDates() {
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
                final String concat = String.valueOf(selectedDay).concat("/")
                        .concat(String.valueOf(selectedMonth + 1)).concat("/").concat(String.valueOf(selectedYear));
                if(selectedYear == currYear) {
                    if(selectedMonth == currMonth && selectedDay>currDay){
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                        startDate.setText("");
                    } else if (selectedMonth>currMonth){
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                        startDate.setText("");
                    } else {
                        startDate.setText(concat);
                    }
                } else if (selectedYear>currYear) {
                    am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                    startDate.setText("");
                } else {
                    startDate.setText(concat);
                }

                startDay = selectedDay;
                startMonth = selectedMonth +1;
                startYear = selectedYear;
            }
        };


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate.setText("");
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(E_Statement.this,
                        android.R.style.Theme_DeviceDefault_Dialog, datePickerListener,
                        currYear=cal.get(Calendar.YEAR),
                        currMonth=cal.get(Calendar.MONTH),
                        currDay=cal.get(Calendar.DAY_OF_MONTH));
                //noinspection ConstantConditions
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.setCancelable(true);
                datePicker.setTitle(getString(R.string.slctStrtDt));
                datePicker.show();
            }
        });


        final DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear2, int selectedMonth2, int selectedDay2) {
                final String concat = String.valueOf(selectedDay2).concat("/")
                        .concat(String.valueOf(selectedMonth2 + 1)).concat("/").concat(String.valueOf(selectedYear2));
                if(selectedYear2 == startYear) {
                    if(selectedMonth2+1 == startMonth && selectedDay2<startDay){
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorStartDate));
                        endDate.setText("");
                    } else if (selectedMonth2+1<startMonth){
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorStartDate));
                        endDate.setText("");
                    } else if(selectedMonth2 == currMonth && selectedDay2 > currDay) {
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                        endDate.setText("");
                    } else if (selectedMonth2 > currMonth) {
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                        endDate.setText("");
                    } else {
                        endDate.setText(concat);
                    }
                } else if (selectedYear2 == currYear) {
                    if(selectedMonth2 == currMonth && selectedDay2 > currDay) {
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                        endDate.setText("");
                    } else if (selectedMonth2 > currMonth) {
                        am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                        endDate.setText("");
                    } else {
                        endDate.setText(concat);
                    }
                } else if (selectedYear2 < startYear) {
                    am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorStartDate));
                    endDate.setText("");
                } else if (selectedYear2 > currYear) {
                    am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.errorLater));
                    endDate.setText("");
                } else {
                    endDate.setText(concat);
                }
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startDate.getText().toString().trim().isEmpty()){
                    am.myDialog(E_Statement.this,getString(R.string.alert),getString(R.string.strartdate));
                    endDate.setText("");
                } else {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    DatePickerDialog datePicker2 = new DatePickerDialog(E_Statement.this,
                            android.R.style.Theme_DeviceDefault_Dialog, datePickerListener2,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    //noinspection ConstantConditions
                    datePicker2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePicker2.setCancelable(true);
                    datePicker2.setTitle(R.string.endDate);
                    datePicker2.show();
                }
            }
        });
    }

    public void createBTN(View view) {
        if (accSend.equals("")) {
            am.myDialog(this, getString(R.string.alert), getString(R.string.selectAcc));
        } else if (startDate.getText().toString().length() < 1) {
            am.myDialog(this, getString(R.string.alert), getString(R.string.setstrtDt));
        } else if (endDate.getText().toString().length() < 1) {
            am.myDialog(this, getString(R.string.alert), getString(R.string.setEnDt));
        } else {
            gDialog = new Dialog(this);
            //noinspection ConstantConditions
            gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            gDialog.setContentView(R.layout.dialog_confirm);
            final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
            final TextView txtNo = gDialog.findViewById(R.id.noBTN);
            final TextView txtOk = gDialog.findViewById(R.id.yesBTN);
            txtMessage.setText(String.format("%s %s  %s %s %s %s.",
                    getText(R.string.createEstmnt),
                    accSend,
                    getText(R.string.from),
                    startDate.getText().toString().trim(),
                    getText(R.string.to),
                    endDate.getText().toString().trim()));
            txtNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gDialog.dismiss();
                }
            });
            txtOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String quest = (
                            "FORMID:O-FullStatementBank:" +
                                    "FIELD1:" + startDate.getText().toString().trim() + ":" +
                                    "FIELD2:" + endDate.getText().toString().trim() + ":" +
                                    "FIELD3:" + accSend + ":"
                    );
                    //am.connectOldTwo(getString(R.string.processingReq),quest,E_Statement.this,"FSB");
                    am.get(E_Statement.this,quest,getString(R.string.processingReq),"FSB");
                    gDialog.dismiss();
                }
            });
            gDialog.setCancelable(true);
            gDialog.show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_menu) {
            finish();
            startActivity(new Intent(getApplicationContext(), Main_Menu.class));
            return true;
        } else if (id == R.id.contact) {
            startActivity(new Intent(getApplicationContext(), Contact_Us.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(String response, String step) {
        finish();
        startActivity(new Intent(getApplicationContext(),SuccessDialogPage.class).putExtra("message", response));
    }
}
