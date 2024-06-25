package com.elmahousingfinanceug.main_Pages.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.elmahousingfinanceug.R;
import com.elmahousingfinanceug.main_Pages.Cheques;
import com.elmahousingfinanceug.recursiveClasses.ResponseListener;
import com.elmahousingfinanceug.recursiveClasses.ResponseListenerTwo;
import com.elmahousingfinanceug.recursiveClasses.SuccessDialogPage;
import com.elmahousingfinanceug.recursiveClasses.VolleyResponse;

public class ChequeStop_Fragment extends Fragment implements ResponseListener, VolleyResponse {
    View RootView;
    Spinner srcAcc;
    EditText fromChq,toChq;
    TextView submit;
    String srcAccStr="";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_cheque_stop, container, false);

        srcAcc = RootView.findViewById(R.id.src_account);
        fromChq = RootView.findViewById(R.id.frmchq);
        toChq = RootView.findViewById(R.id.tochq);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getActivity() != null;
                if(srcAccStr.equals("")) {
                    ((Cheques)getActivity()).am.myDialog(getActivity(),getString(R.string.alert),getString(R.string.selectAccount));
                } else if (fromChq.getText().toString().trim().isEmpty()){
                    fromChq.setError(getString(R.string.invalidInput));
                } else if (toChq.getText().toString().trim().isEmpty()){
                    toChq.setError(getString(R.string.invalidInput));
                } else {
                    String quest = (
                            "FORMID:B-:" +
                                    "MERCHANTID:STOPCHEQUE:" +
                                    "BANKACCOUNTID:"+ srcAccStr +":" +
                                    "INFOFIELD1:"+ fromChq.getText().toString().trim() +":" +
                                    "INFOFIELD2:"+ toChq.getText().toString().trim() +":"
                    );
                    //((Cheques)getActivity()).am.connectOldTwo(getString(R.string.processingReq), quest,ChequeStop_Fragment.this,"");
                    ((Cheques)getActivity()).am.get_(ChequeStop_Fragment.this, quest, getString(R.string.processingReq),"");
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
