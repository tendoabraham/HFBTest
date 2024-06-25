package com.elmahousingfinanceug.main_Pages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.recursiveClasses.BaseAct;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.elmahousingfinanceug.recursiveClasses.VolleyResponse;

import java.util.ArrayList;

public class Mini_Statement extends BaseAct implements ResponseListener, VolleyResponse {
    Spinner accountNumber;
    public LinearLayout accountInfoLayout;
    RecyclerView mRecyclerView;
    Mini_Statement_Adapter mAdapter;
    String  quest="";
    final ArrayList<String> myDataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);

        gToolBar(getString(R.string.miniStatement));

        mRecyclerView = findViewById(R.id.recycler_mini_statement);
        accountNumber = findViewById(R.id.accountNumber);
        accountInfoLayout = findViewById(R.id.accountInfoLayout);
        accountInfoLayout.setVisibility(View.GONE);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Mini_Statement.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Mini_Statement_Adapter(myDataSet);
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh(){
        myDataSet.clear();
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        populateSpinners();
    }

    private void populateSpinners() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        accountNumber.setAdapter(dataAdapter);
        accountNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    accountInfoLayout.setVisibility(View.GONE);
                } else {
                    quest = (
                            "FORMID:B-:" +
                                    "MERCHANTID:STATEMENT:" +
                                    "BANKACCOUNTID:" + am.getBankAccountID(position) + ":"
                    );
                    //am.connectOldTwo(getString(R.string.processingReq),quest,Mini_Statement.this,"TRX");
                    am.get_(Mini_Statement.this,quest,getString(R.string.processingReq),"TRX");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onResponse(String response, String step) {
        myDataSet.clear();
        response = response.replace("\r","~");
        String[] Maindata = response.split("~");
        int i = 0;
        for (String s: Maindata){
            try {
                String date = Maindata[i].substring(0,8),
                        desc = Maindata[i].substring(9,16),
                        type = Maindata[i].substring(17,19),
                        amount =  Maindata[i].substring(19,36);
                if(date.trim().isEmpty()) date = "N/A";
                if(desc.trim().isEmpty()) desc = "N/A";
                if(type.trim().isEmpty()) type = "N/A";
                if(amount.trim().isEmpty()) {
                    amount = "N/A";
                } else {
                    amount = am.Amount_Thousands(amount.trim());
                }
                s = date + "|" + desc + "|" + type + "|" + amount ;
            } catch (Exception sE){
                am.LogThis("StringIndex Error : "+sE.getMessage());
            }
            myDataSet.add(s);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
            i++;
        }
        accountInfoLayout.setVisibility(View.VISIBLE);
    }

    class Mini_Statement_Adapter extends RecyclerView.Adapter<Mini_Statement_Adapter.ViewHolder> {
        private final ArrayList<String> mDataSet;
        private Context context;

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView date;
            final TextView particulars;
            final TextView type;
            final TextView amount;

            ViewHolder(View v) {
                super(v);
                date = v.findViewById(R.id.date);
                particulars = v.findViewById(R.id.particulars);
                type = v.findViewById(R.id.type);
                amount = v.findViewById(R.id.amount);
            }
        }

        public void add(int position, String item) {
            mDataSet.add(position, item);
            notifyItemInserted(position);
        }

        Mini_Statement_Adapter(ArrayList<String> myDataSet) {
            mDataSet = myDataSet;
        }

        @Override
        public Mini_Statement_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ministatement, parent, false);
            context = v.getContext();
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder( final ViewHolder holder, final int position) {
            try {
                String data = mDataSet.get(position);
                String[] splits = data.split("\\|");
                holder.date.setText(splits[0]);
                holder.particulars.setText(splits[1]);
                if(splits[2].contains("C")){
                    holder.type.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                    holder.type.setText(R.string.credit);
                } else if(splits[2].contains("D")){
                    holder.type.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.logo_red));
                    holder.type.setText(R.string.debit);
                } else {
                    holder.type.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
                    holder.type.setText(splits[2]);
                }
                holder.amount.setText(splits[3]);

                setScaleAnimation(holder.itemView);

            } catch (Exception a){
                am.LogThis("ViewHolderException ► "+a.getMessage());
                am.myDialog(context,context.getString(R.string.alert),context.getString(R.string.unexpectedError));
                accountInfoLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        private void setScaleAnimation(View view) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(200);
            view.startAnimation(anim);
        }
    }
}
