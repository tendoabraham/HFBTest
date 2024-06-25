package com.elmahousingfinanceug_test.main_Pages.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Cheques;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug_test.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug_test.recursiveClasses.VolleyResponse;

public class ChequeRequest_Fragment extends Fragment implements ResponseListener, VolleyResponse {
    View RootView;
    Spinner srcAcc;
    RadioGroup leaves;
    RadioButton lv25,lv50,lv100;
    TextView submit;
    String srcAccStr="",leavesStr="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_cheque_req, container, false);

        srcAcc = RootView.findViewById(R.id.src_account);
        leaves = RootView.findViewById(R.id.choose_leaves);
        lv25 = RootView.findViewById(R.id.l_25);
        lv50 = RootView.findViewById(R.id.l_50);
        lv100 = RootView.findViewById(R.id.l_100);
        submit = RootView.findViewById(R.id.submit);

        assert getActivity() != null;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, ((Cheques)getActivity()).am.getAliases());
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        srcAcc.setAdapter(dataAdapter);
        srcAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    srcAccStr = "";
                } else {
                    assert getActivity() != null;
                    srcAccStr = ((Cheques)getActivity()).am.getBankAccountID(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        leaves.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (lv25.isChecked()) {
                    leavesStr = "25";
                } else if (lv50.isChecked()) {
                    leavesStr = "50";
                } else if (lv100.isChecked()) {
                    leavesStr = "100";
                }
            }
        });
        lv25.setChecked(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getActivity() != null;
                if(srcAccStr.equals("")){
                    ((Cheques)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),getString(R.string.selectAccount));
                } else {
                    String quest = (
                            "FORMID:B-:" +
                                "MERCHANTID:CHEQUEBOOK:" +
                                "BANKACCOUNTID:"+ srcAccStr +":" +
                                "INFOFIELD1:"+ leavesStr +":"
                    );
                    //((Cheques)getActivity()).am.connectOldTwo(getString(R.string.processingReq), quest,ChequeRequest_Fragment.this,"");
                    ((Cheques)getActivity()).am.get_(ChequeRequest_Fragment.this, quest, getString(R.string.processingReq),"");
                }
            }
        });

        return  RootView;
    }

    @Override
    public void onResponse(String response, String step) {
        assert getActivity() != null;
        ((Cheques)getActivity()).am.saveDoneTrx(true);
        getActivity().finish();
        startActivity(new Intent(getActivity(), SuccessDialogPage.class).putExtra("message", response));
    }
}
