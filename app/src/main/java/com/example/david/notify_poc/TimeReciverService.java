package com.example.david.notify_poc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TimeReciverService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Time ","has come !");
        Toast.makeText(context,"This is an alarm",Toast.LENGTH_SHORT).show();

    }
}
