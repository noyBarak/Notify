package com.example.david.notify_poc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 16/02/2017.
 */

public class locationTranslationService extends Service {
    private static Geocoder geocoder ;
    final int MAX_RES = 5;                      //maximum number of results from geocoder
    List<Address> list = null;                  //Response from Geocoder of sent <location name>
    String loc_input = "";
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public static void setContext (Context c){
        geocoder =  new Geocoder(c);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loc_input = intent.getStringExtra(getString(R.string.ext_location));
        //create a new Thread to run getFromLocationName func.
        MyThread myThread = new MyThread();
        myThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyThread extends Thread{
        @Override
        public void run() {
            Intent intent = new Intent();//return value
            intent.setAction(getString(R.string.action_code));
            ArrayList<MyAddress> myList = new ArrayList<>();
            try {
                list = geocoder.getFromLocationName(loc_input,MAX_RES);//actual work!
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i=0 ; i < list.size() ; i++) {
                MyAddress temp = new MyAddress(list.get(i));
                myList.add(  temp  );
            }
            intent.putExtra(getString(R.string.ext_list), myList); //save received info in intent
            sendBroadcast(intent);                  //send intent back to MainActivity
            stopSelf();
        }

    }
    public class MyAddress extends Address implements Serializable {
        MyAddress(Address address) {
            //copy constructor from Address obj to MyAddress
            super(address.getLocale());
            this.setLatitude(address.getLatitude());
            this.setLongitude(address.getLongitude());
            this.setAddressLine(0,address.getAddressLine(0));
            this.setAddressLine(1,address.getAddressLine(1));
            this.setAddressLine(2,address.getAddressLine(2));
        }
    }


}