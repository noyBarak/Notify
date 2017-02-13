package com.example.david.notify_poc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.List;


public class SMSReceiver extends BroadcastReceiver {
    List<SmsObject> smsList;
    SMSReceiver(List<SmsObject> smsList){
        this.smsList = smsList;
    }

    public SMSReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = new SmsMessage[0];
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundleExtras = intent.getExtras();
            if (bundleExtras != null) {
                Object[] pdus = (Object[]) bundleExtras.get("pdus");
                messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
            }
        }
        String number = messages[0].getOriginatingAddress();
        Log.e("Rec Sms  ",number);
        /*
        for(SmsObject t : smsList){
            if(t.sms_number.equals(number) && (t.sms_in_out == 1 || t.sms_in_out == 3)){
                //should print t.sms_text;
                Log.e("sms Recived",t.sms_text);
            }
        }

        */
    }

}
