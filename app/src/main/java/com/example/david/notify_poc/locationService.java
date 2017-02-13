package com.example.david.notify_poc;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import static com.example.david.notify_poc.R.*;

public class locationService extends Service {
    private LocationManager locationManager = null;
    private Context context;
    private double address_lat ;              //target Longitude
    private double  address_lon ;              //target Latitude
    private int in_out ;
    private double distance = -1;                   //distance from target
    private int notNum = 0;                         //number of sent notifications.
    private LocationListener locationListener = new LocationListener() {
        boolean isInside = false;
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if ( distance == -1 ) {
                //do nothing, error
            } else {

                float[] results = new float[3];//distanceBetween will put his ans. here
                Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                        address_lat, address_lon, results);
                if (results[0] < distance) {
                    if( !isInside ){
                        isInside = true;
                        createNotification( true );
                    }

                }else{
                    //out of range from <<location>>
                    if(isInside){
                        isInside = false;
                        createNotification( false );
                    }
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get sent info
       // distance = Double.parseDouble(intent.getStringExtra(getString(string.ext_dist)));
       // address = intent.getParcelableExtra(getString(string.ext_addr));
        address_lat = intent.getDoubleExtra("address_lat",-1);
        address_lat = intent.getDoubleExtra("address_lat",-1);
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        //choose a provider.
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setCostAllowed(true);

        String best = locationManager.getBestProvider(criteria, false);
        //check permissions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return super.onStartCommand(intent, flags, startId);
        }
        Location lastKnown = locationManager.getLastKnownLocation( best );


        if ( distance == -1  || lastKnown == null) {
            // error
        } else {

            float[] results = new float[3];//distanceBetween will put his ans. here
            Location.distanceBetween(lastKnown.getLatitude(), lastKnown.getLongitude(),
                    address_lat,address_lon, results);
            //out of range from <<location>>
            if (results[0] < distance) {
                createNotification( true );
            }
        }
        long MIN_TIME_FOR_UPDATE = 1000;
        float MIN_DIS_FOR_UPDATE = 0.01f;
        //register to location service
        locationManager.requestLocationUpdates(best, MIN_TIME_FOR_UPDATE,
                MIN_DIS_FOR_UPDATE,
                locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void createNotification (boolean isInside){
        //build a new notification and send it
        NotificationCompat.Builder mBuilder = null;
        if ( isInside ) {
            /*mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.)
                            .setContentTitle(getString(string.notf_title_in))
                            .setContentText(getString(string.notf_text_in));
                            */
        }
        else{
        /*
            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(getString(string.notf_title_out))
                            .setContentText(getString(string.notf_text_out));
                            */
        }


        mBuilder.setColor(Color.argb(255,0, 125, 214));
        mBuilder.setLights(Color.argb(255,0, 125, 214) , 1000, 700);
        notNum++;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notNum, mBuilder.build());
    }
}
