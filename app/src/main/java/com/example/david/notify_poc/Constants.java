package com.example.david.notify_poc;

import android.provider.BaseColumns;

 final class Constants implements BaseColumns {
    private Constants(){
        throw new AssertionError("Can't create constants class");
    }
    // Database Info
    static final String DATABASE_NAME = "NotifyDB";
     static final int DATABASE_VERSION = 1;

     static abstract class NotifyCall implements BaseColumns {

        // Table Names
         static final String TABLE_CALL = "NotifyCall";

        // User Table Columns
         static final String KEY_Call_ID = "id";
         static final String KEY_CALL_NUMBER = "call_number";
         static final String KEY_CALL_DISPLAY_NAME = "call_name";
         static final String KEY_CALL_IN_OUT = "call_inout";
         static final String KEY_CALL_TEXT = "call_text";
    }
     static abstract class NotifySms implements BaseColumns {
        // Table Names
         static final String TABLE_SMS= "NotifySms";

        // User Table Columns
         static final String KEY_SMS_ID = "id";
         static final String KEY_SMS_NUMBER = "sms_number";
         static final String KEY_SMS_DISPLAY_NAME = "sms_name";
         static final String KEY_SMS_IN_OUT = "sms_inout";
         static final String KEY_SMS_TEXT = "sms_text";
    }
     static abstract class NotifyTime implements BaseColumns {
        // Table Names
         static final String TABLE_TIME = "NotifyTime";

        // User Table Columns
         static final String KEY_TIME_ID = "id";
         static final String KEY_TIME_AT = "time_at";
         static final String KEY_TIME_TEXT = "time_text";
    }
     static abstract class NotifyLocation implements BaseColumns {
        // Table Names
         static final String TABLE_LOCATION = "NotifyLocation";

        // User Table Columns
         static final String KEY_LOCATION_ID = "id";
         static final String KEY_LOCATION_LAT = "location_lat";
         static final String KEY_LOCATION_LON = "location_lon";
         static final String KEY_LOCATION_RAD = "location_radius";
         static final String KEY_LOCATION_IN_OUT = "location_inout";
         static final String KEY_LOCATION_TEXT = "location_text";
    }
}
