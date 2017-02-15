package com.example.david.notify_poc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    public static String PACKAGE_NAME;
    //context :
    Context context;
    //db connection :
    DbHelper databaseHelper;
    //vars for alarm service :
    AlarmManager alarmManager;
    private PendingIntent pending_intent;
    Calendar calendar;
    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CREATE pop-up dialog to add new Notification
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.context = this;
        PACKAGE_NAME = getApplicationContext().getPackageName();


        //alarm service :
        this.myIntent = new Intent(this.context, TimeReciverService.class);
        // Get the alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // set the alarm to the time that you picked
        this.calendar = Calendar.getInstance();

    }

    @Override
    public void onBackPressed() {
        //return to mainActivity and close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        databaseHelper = DbHelper.getInstance(this);

        if (id == R.id.nav_camera) {
            // create sample post
            /*
            TimeObject temp = new TimeObject();
            temp.time_text = "text test _1_ ";
            temp.time_at_ms =  System.currentTimeMillis() + 2000;
            databaseHelper.addTime(temp,this);*/
            //start time service
            setAllTimeNotification();


        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {
            //start call Catching service
            Intent intent = new Intent(this, CallDetectService.class);
            List<CallObject> callsNotification = databaseHelper.getAllCallNotification();
            intent.putParcelableArrayListExtra("listCalls", (ArrayList<? extends Parcelable>) callsNotification);
            Log.e("Starting", "Call service");
            startService(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, AService.class);
            List<SmsObject> callsNotification = databaseHelper.getAllSmsNotification();
            intent.putParcelableArrayListExtra("listSms", (ArrayList<? extends Parcelable>) callsNotification);
            startService(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void setAllTimeNotification (){
        //read all Time Notify's
        List<TimeObject> allTimeNotify =  databaseHelper.getAllTimeNotification();
        if(allTimeNotify == null || allTimeNotify.isEmpty()){
            Toast.makeText(this,"No time Notifications",Toast.LENGTH_SHORT).show();
        }
        if (allTimeNotify != null) {
            for (TimeObject notification : allTimeNotify) {
                Intent intent = new Intent(this, TimeReciverService.class);
                intent.putExtra("time_text",notification.time_text);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this.getApplicationContext(),notification.time_id, intent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(notification.time_at_ms);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

}
