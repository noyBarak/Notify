package com.example.david.notify_poc;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class AService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private SMSReceiver smsReceiver;
    final IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        smsFilter.setPriority(1000);
        this.smsReceiver = new SMSReceiver(intent.<SmsObject>getParcelableArrayListExtra("listSms"));
        Log.e("Starting","Sms service");
        this.registerReceiver(this.smsReceiver, smsFilter);
        return Service.START_STICKY;
    }
}
