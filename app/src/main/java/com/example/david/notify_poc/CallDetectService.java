package com.example.david.notify_poc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CallDetectService extends Service {
    private CallHelper callHelper;

    public CallDetectService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callHelper = new CallHelper(this, intent.<CallObject>getParcelableArrayListExtra("callList"));

        int res = super.onStartCommand(intent, flags, startId);
        Log.e("Starting","Call Helper");
        callHelper.start();
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        callHelper.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // not supporting binding
        return null;
    }
}
