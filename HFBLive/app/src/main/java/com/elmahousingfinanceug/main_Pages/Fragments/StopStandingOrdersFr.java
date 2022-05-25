package com.elmahousingfinanceug.main_Pages.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.main_Pages.Adapters.Standing_Order_Stop_Adapter;
import com.elmahousingfinanceug.main_Pages.Standing_Orders;

import java.util.ArrayList;

public class StopStandingOrdersFr extends Fragment {
    View rootView;
    ArrayList<String> myDataSet = new ArrayList<>();
    RecyclerView mRecyclerView;
    Standing_Order_Stop_Adapter mAdapter;
    TextView vS;
    public LinearLayout accInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_stop_standing_orders, container, false);

        vS = rootView.findViewById(R.id.listords);
        mRecyclerView = rootView.findViewById(R.id.recycler_standing_order);
        accInfo = rootView.findViewById(R.id.accInfo);
        accInfo.setVisibility(View.GONE);

        setupAdapter();

        vS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getActivity() != null;
                String quest = (
                        "FORMID:O-StandingOrdersList:"
                );
                //((Standing_Orders)getActivity()).am.connectOldTwo(getString(R.string.processingReq),quest,((Standing_Orders)getActivity()),"W");
                ((Standing_Orders)getActivity()).am.get(((Standing_Orders)getActivity()), quest, getString(R.string.processingReq),"");
            }
        });

        return rootView;
    }

    private void setupAdapter() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new Standing_Order_Stop_Adapter(myDataSet);
    }

    //STATUS:OK:DATA:
    // Name-Happy,Pay To-8052369874,First Run Date-2018-10-11(Afternoon),Execute Every-Weekly,If there is no Balance-Ignore,Amount-36.00,Pay via-1100115902,Service-TRANSFER
    // |Name-Maybe,Pay To-654123987412,First Run Date-2018-10-28(Morning),Execute Every-Bi~Monthly,If there is no Balance-Ignore,Amount-2580.00,Pay via-1100115902,Service-TRANSFER
    // |Name-Photo,Pay To-0855236981,First Run Date-2018-10-14(Afternoon),Execute Every-Year,If there is no Balance-Ignore,Amount-111.00,Pay via-1100115902,Service-TRANSFER|

    public void show(String display){
        vS.setVisibility(View.GONE);

        String orderName="",payTo="",startDate="",freq="",ifNb="",amount="",sourceAccount="";

        assert getActivity() != null;
        String[] orders = display.split("\\|");

        for (String anOrder : orders){
            String[] inside = anOrder.split(",");
            int insideLength = inside.length;
            for (int a = 0; a < insideLength; a++) {
                String[] value = inside[a].split("-");
                if (a == 0) {
                    orderName = value[1].trim();
                } else if (a == 1) {
                    payTo = value[1];
                } else if (a == 2) {
                    startDate = value[1]+"/"+ value[2]+"/"+ value[3].substring(0,2);
                } else if (a == 3) {
                    freq = value[1];
                } else if (a == 4) {
                    ifNb = value[1];
                } else if (a == 5) {
                    amount = ((Standing_Orders)getActivity()).am.Amount_Thousands(value[1].trim());
                } else if (a == 6) {
                    sourceAccount = value[1].trim();
                }
            }
            myDataSet.add(orderName + "|" + payTo + "|" +startDate + "|" +freq + "|" +ifNb + "|" +amount + "|" +sourceAccount);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
        }
        accInfo.setVisibility(View.VISIBLE);
    }
}
