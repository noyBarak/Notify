package com.example.david.notify_poc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimeReciverService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("reciver","Inside!"+ intent.getStringExtra("time_text")  );
    }
}