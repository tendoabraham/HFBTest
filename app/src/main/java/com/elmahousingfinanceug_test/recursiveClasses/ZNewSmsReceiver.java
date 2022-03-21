package com.elmahousingfinanceug_test.recursiveClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.Objects;

public class ZNewSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: 5/28/2021 remove before live
        //com.elmahousingfinanceug_test -- hash:  <#>
        // D/hash: 80/7SsvKf1O
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            try {
                Bundle extras = intent.getExtras();
                assert extras != null;
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                assert status != null;
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        String newmsgbody = message;
                        Log.d("LSO", Objects.requireNonNull(message));
                        String [] msgarray = newmsgbody.split(":");
                        if (msgarray.length > 1) {
                            newmsgbody = msgarray[1].substring(0, 6);
                            Intent k = new Intent("SMSReceived");
                            Log.d("LSO Code", newmsgbody);
                            k.putExtra("STATUS","000");
                            k.putExtra("SMSbody", newmsgbody);
                            Log.d("LSO","message body " + newmsgbody);
                            context.sendBroadcast(k);
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        Intent k = new Intent("SMSReceived");
                        k.putExtra("STATUS","091");
                        Log.d("LSO","SMS timeout");
                        k.putExtra("SMSbody", "Sorry. The SMS retrieval service timed out. Attempting to restart it.");
                        context.sendBroadcast(k);
                        break;
                }
            } catch (Exception chapa) {
                chapa.printStackTrace();
                Intent k = new Intent("SMSReceived");
                k.putExtra("STATUS", "092");
                Log.d("LSO", "Code fallen");
                k.putExtra("SMSbody","Sorry. Unable to retrieve the SMS.");
                context.sendBroadcast(k);
            }
        }
    }
}
