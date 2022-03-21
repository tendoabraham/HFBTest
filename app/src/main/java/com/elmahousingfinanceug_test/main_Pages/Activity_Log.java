package com.elmahousingfinanceug_test.main_Pages;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.Activity_Log_Adapter;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.elmahousingfinanceug_test.recursiveClasses.DataBaseClass;

import java.util.ArrayList;

public class Activity_Log extends BaseAct {
    ListView lv;
    Activity_Log_Adapter ala;
    LinearLayout noDataLayout;
    DataBaseClass db = new DataBaseClass(Activity_Log.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        gToolBar(getString(R.string.tranHistrory));

        lv = findViewById(R.id.lv);
        noDataLayout = findViewById(R.id.noDataLayout);

        Update_List();

        setClicks();
    }

    private void setClicks() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemID = view.findViewById(R.id.ID);
                gDialog = new Dialog(Activity_Log.this);
                //noinspection ConstantConditions
                gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                gDialog.setContentView(R.layout.dialog_confirm);
                final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
                final TextView noBTN = gDialog.findViewById(R.id.noBTN);
                final TextView yesBTN = gDialog.findViewById(R.id.yesBTN);
                txtMessage.setText(R.string.sureDelete);
                yesBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.deleteOneLog(Integer.valueOf(itemID.getText().toString()));
                        am.ToastMessage(Activity_Log.this,getString(R.string.logDeleted));
                        Update_List();
                        gDialog.dismiss();
                    }
                });
                noBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gDialog.dismiss();
                    }
                });
                gDialog.setCancelable(true);
                gDialog.show();
                am.animate_Text(noBTN);
                am.animate_Text(yesBTN);
                am.animate_Text(txtMessage);
            }
        });
    }

    public void Update_List() {
        final ArrayList<String[]> db_data = new ArrayList<>();
        ala = new Activity_Log_Adapter(this, db_data);
        Cursor cursor = db.retrieveAllLogs();
        int cur = cursor.getCount();
        if (cur > 0) {
            cursor.moveToLast();
            do {
                String ItemID = cursor.getString(cursor.getColumnIndex(DataBaseClass.LOG_ID_NUMBER));
                String ItemDate = cursor.getString(cursor.getColumnIndex(DataBaseClass.LOG_DATE));
                String ItemLabel = cursor.getString(cursor.getColumnIndex(DataBaseClass.LOG_LABEL));
                String aa =  am.D_T(ItemDate),bb = am.D_T(ItemLabel);
                if(aa.equalsIgnoreCase("") || bb.equalsIgnoreCase("")) {
                    String[] array_d = {ItemID, ItemDate, ItemLabel};
                    db_data.add(array_d);
                } else {
                    String[] array_db = {ItemID, aa, bb};
                    db_data.add(array_db);
                }
            } while (cursor.moveToPrevious());

            ala.notifyDataSetChanged();
            lv.setAdapter(ala);
            noDataLayout.setVisibility(View.GONE);
        } else {
            db_data.clear();
            ala.notifyDataSetChanged();
            lv.setAdapter(ala);
            noDataLayout.setVisibility(View.VISIBLE);
            am.animate_View(noDataLayout);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_logs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clearHistory) {
            gDialog = new Dialog(Activity_Log.this);
            //noinspection ConstantConditions
            gDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            gDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            gDialog.setContentView(R.layout.dialog_confirm);
            final TextView txtMessage = gDialog.findViewById(R.id.dialog_message);
            final TextView noBTN = gDialog.findViewById(R.id.noBTN);
            final TextView yesBTN = gDialog.findViewById(R.id.yesBTN);
            txtMessage.setText(R.string.sureDeleteAll);
            noBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gDialog.dismiss();
                }
            });
            yesBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.deleteAllLogs();
                    am.ToastMessageLong(Activity_Log.this, getString(R.string.allLogsDel));
                    Update_List();
                    gDialog.dismiss();
                }
            });
            gDialog.setCancelable(true);
            gDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
