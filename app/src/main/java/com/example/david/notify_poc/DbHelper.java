package com.example.david.notify_poc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


class DbHelper extends SQLiteOpenHelper {
    private static DbHelper sInstance;

    static synchronized DbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create CALL TABLE
        String CREATE_CALL_TABLE = "CREATE TABLE " + Constants.NotifyCall.TABLE_CALL +
                "(" +
                Constants.NotifyCall.KEY_Call_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.NotifyCall.KEY_CALL_NUMBER + " TEXT," +
                Constants.NotifyCall.KEY_CALL_DISPLAY_NAME + " TEXT," +
                Constants.NotifyCall.KEY_CALL_IN_OUT + " INTEGER, " +
                Constants.NotifyCall.KEY_CALL_TEXT + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(CREATE_CALL_TABLE);
        //create SMS TABLE
        String CREATE_SMS_TABLE = "CREATE TABLE " + Constants.NotifySms.TABLE_SMS +
                "(" +
                Constants.NotifySms.KEY_SMS_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.NotifySms.KEY_SMS_NUMBER + " TEXT," +
                Constants.NotifySms.KEY_SMS_DISPLAY_NAME+ " TEXT," +
                Constants.NotifySms.KEY_SMS_IN_OUT + " INTEGER, " +
                Constants.NotifySms.KEY_SMS_TEXT + " INTEGER" +
                ")";

        sqLiteDatabase.execSQL(CREATE_SMS_TABLE);
        //create TIME TABLE
        String CREATE_TIME_TABLE = "CREATE TABLE " + Constants.NotifyTime.TABLE_TIME +
                "(" +
                Constants.NotifyTime.KEY_TIME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.NotifyTime.KEY_TIME_AT + " TEXT," +
                Constants.NotifyTime.KEY_TIME_TEXT + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(CREATE_TIME_TABLE);
        //create LOCATION TABLE
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + Constants.NotifyLocation.TABLE_LOCATION +
                "(" +
                Constants.NotifyLocation.KEY_LOCATION_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constants.NotifyLocation.KEY_LOCATION_LAT + " TEXT," +
                Constants.NotifyLocation.KEY_LOCATION_LON+ " TEXT," +
                Constants.NotifyLocation.KEY_LOCATION_RAD + " INTEGER, " +
                Constants.NotifyLocation.KEY_LOCATION_IN_OUT + " INTEGER, " +
                Constants.NotifyLocation.KEY_LOCATION_TEXT + " INTEGER" +
                ")";
        sqLiteDatabase.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + Constants.NotifyCall.TABLE_CALL);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.NotifySms.TABLE_SMS);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.NotifyTime.TABLE_TIME);
            db.execSQL("DROP TABLE IF EXISTS " + Constants.NotifyLocation.TABLE_LOCATION);
            onCreate(db);
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    long addCall(CallObject callNotify,Context context) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        Log.d("add new","starting");
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifyCall.KEY_CALL_NUMBER, callNotify.call_number);
            values.put(Constants.NotifyCall.KEY_CALL_DISPLAY_NAME, callNotify.call_name);
            values.put(Constants.NotifyCall.KEY_CALL_IN_OUT, callNotify.call_in_out);
            values.put(Constants.NotifyCall.KEY_CALL_TEXT, callNotify.call_text);
            // user with this userName did not already exist, so insert new user
            userId = db.insertOrThrow(Constants.NotifyCall.TABLE_CALL, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long updateCall(CallObject callNotify,Context context){
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifyCall.KEY_CALL_NUMBER, callNotify.call_number);
            values.put(Constants.NotifyCall.KEY_CALL_DISPLAY_NAME, callNotify.call_name);
            values.put(Constants.NotifyCall.KEY_CALL_IN_OUT, callNotify.call_in_out);
            values.put(Constants.NotifyCall.KEY_CALL_TEXT, callNotify.call_text);
            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(Constants.NotifyCall.TABLE_CALL, values,
                    Constants.NotifyCall.KEY_Call_ID + "= ? ", new String[]{String.valueOf(callNotify.call_id)});
        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long addSms(SmsObject smsNotify,Context context) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifySms.KEY_SMS_NUMBER, smsNotify.sms_number);
            values.put(Constants.NotifySms.KEY_SMS_DISPLAY_NAME, smsNotify.sms_name);
            values.put(Constants.NotifySms.KEY_SMS_IN_OUT, smsNotify.sms_in_out);
            values.put(Constants.NotifySms.KEY_SMS_TEXT, smsNotify.sms_text);

            // user with this userName did not already exist, so insert new user
            userId = db.insertOrThrow(Constants.NotifySms.TABLE_SMS, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long updateSms(SmsObject smsNotify,Context context){
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifySms.KEY_SMS_NUMBER, smsNotify.sms_number);
            values.put(Constants.NotifySms.KEY_SMS_DISPLAY_NAME, smsNotify.sms_name);
            values.put(Constants.NotifySms.KEY_SMS_IN_OUT, smsNotify.sms_in_out);
            values.put(Constants.NotifySms.KEY_SMS_TEXT, smsNotify.sms_text);
            int rows = db.update(Constants.NotifySms.TABLE_SMS, values,
                    Constants.NotifySms.KEY_SMS_ID + "= ? ",
                    new String[]{String.valueOf(smsNotify.sms_id)});

        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long addTime(TimeObject timeNotify,Context context) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifyTime.KEY_TIME_AT, timeNotify.time_at_ms);
            values.put(Constants.NotifyTime.KEY_TIME_TEXT, timeNotify.time_text);

            userId = db.insertOrThrow(Constants.NotifyTime.TABLE_TIME, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT"+e.toString(),Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long updateTime(TimeObject timeNotify,Context context){
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifyTime.KEY_TIME_AT, timeNotify.time_at_ms);
            values.put(Constants.NotifyTime.KEY_TIME_TEXT, timeNotify.time_text);
            int rows = db.update(Constants.NotifyTime.TABLE_TIME, values,
                    Constants.NotifyTime.KEY_TIME_ID + "= ?",
                    new String[]{Long.toString(timeNotify.time_id)});
        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long addLocation(LocationObject locationNotify,Context context) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifyLocation.KEY_LOCATION_LAT, locationNotify.loc_lat);
            values.put(Constants.NotifyLocation.KEY_LOCATION_LON, locationNotify.loc_lon);
            values.put(Constants.NotifyLocation.KEY_LOCATION_IN_OUT, locationNotify.loc_in_out);
            values.put(Constants.NotifyLocation.KEY_LOCATION_RAD, locationNotify.loc_rad);
            values.put(Constants.NotifyLocation.KEY_LOCATION_TEXT, locationNotify.loc_text);
            userId = db.insertOrThrow(Constants.NotifyLocation.TABLE_LOCATION, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    long updateLocation(LocationObject locationNotify,Context context){
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Constants.NotifyLocation.KEY_LOCATION_LAT, locationNotify.loc_lat);
            values.put(Constants.NotifyLocation.KEY_LOCATION_LON, locationNotify.loc_lon);
            values.put(Constants.NotifyLocation.KEY_LOCATION_IN_OUT, locationNotify.loc_in_out);
            values.put(Constants.NotifyLocation.KEY_LOCATION_RAD, locationNotify.loc_rad);
            values.put(Constants.NotifyLocation.KEY_LOCATION_TEXT, locationNotify.loc_text);
            int rows = db.update(Constants.NotifyLocation.TABLE_LOCATION, values,
                    Constants.NotifyLocation.KEY_LOCATION_LAT + "= ? AND " +
                            Constants.NotifyLocation.KEY_LOCATION_LON+ "= ? AND "+
                            Constants.NotifyLocation.KEY_LOCATION_RAD+ "= ? AND "+
                            Constants.NotifyLocation.KEY_LOCATION_IN_OUT+ "= ?",new String[]{
                            String.valueOf(locationNotify.loc_lat),
                            String.valueOf(locationNotify.loc_lon),
                            String.valueOf(locationNotify.loc_rad),
                            String.valueOf(locationNotify.loc_in_out)
                    });
        } catch (Exception e) {
            Toast.makeText(context,"ERROR ADDING STUDENT",Toast.LENGTH_LONG).show();
        } finally {
            db.endTransaction();
        }
        return userId;
    }
    // Get all Notifications from the database
    List<CallObject> getAllCallNotification() {
        List<CallObject> callsList = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        Constants.NotifyCall.TABLE_CALL
                );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CallObject callobj = new CallObject();
                    callobj.call_id = cursor.getColumnIndex(Constants.NotifyCall.KEY_Call_ID);
                    callobj.call_number = cursor.getString(cursor.getColumnIndex(Constants.NotifyCall.KEY_CALL_NUMBER));
                    callobj.call_name = cursor.getString(cursor.getColumnIndex(Constants.NotifyCall.KEY_CALL_DISPLAY_NAME));
                    callobj.call_in_out = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.NotifyCall.KEY_CALL_IN_OUT)));
                    callobj.call_text = cursor.getString(cursor.getColumnIndex(Constants.NotifyCall.KEY_CALL_TEXT));
                    callsList.add(callobj);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return callsList;
    }

    List<TimeObject> getAllTimeNotification() {
        List<TimeObject> timeList = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        Constants.NotifyTime.TABLE_TIME
                );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TimeObject timeobj = new TimeObject();
                    timeobj.time_id = cursor.getInt(cursor.getColumnIndex(Constants.NotifyTime.KEY_TIME_ID));
                    timeobj.time_at_ms = Long.parseLong(cursor.getString(cursor.getColumnIndex(Constants.NotifyTime.KEY_TIME_AT)));
                    timeobj.time_text = cursor.getString(cursor.getColumnIndex(Constants.NotifyTime.KEY_TIME_TEXT));
                    timeList.add(timeobj);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return timeList;
    }

    List<SmsObject> getAllSmsNotification() {
        List<SmsObject> smsList = new ArrayList<>();
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        Constants.NotifySms.TABLE_SMS
                );

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    SmsObject smsobj = new SmsObject();
                    smsobj.sms_id = cursor.getColumnIndex(Constants.NotifySms.KEY_SMS_ID);
                    smsobj.sms_number = cursor.getString(cursor.getColumnIndex(Constants.NotifySms.KEY_SMS_NUMBER));
                    smsobj.sms_name = cursor.getString(cursor.getColumnIndex(Constants.NotifySms.KEY_SMS_DISPLAY_NAME));
                    smsobj.sms_in_out = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.NotifySms.KEY_SMS_IN_OUT)));
                    smsobj.sms_text = cursor.getString(cursor.getColumnIndex(Constants.NotifySms.KEY_SMS_TEXT));
                    smsList.add(smsobj);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return smsList;
    }

    List<LocationObject> getAllLocationNotification() {
        List<LocationObject> LocationList = new ArrayList<>();
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        Constants.NotifyLocation.TABLE_LOCATION
                );

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    LocationObject locationobj = new LocationObject();
                    locationobj.loc_id = cursor.getColumnIndex(Constants.NotifyLocation.KEY_LOCATION_ID);
                    locationobj.loc_lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constants.NotifyLocation.KEY_LOCATION_LAT)));
                    locationobj.loc_lon = Double.parseDouble(cursor.getString(cursor.getColumnIndex(Constants.NotifyLocation.KEY_LOCATION_LON)));
                    locationobj.loc_rad = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.NotifyLocation.KEY_LOCATION_RAD)));
                    locationobj.loc_in_out = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.NotifyLocation.KEY_LOCATION_IN_OUT)));
                    locationobj.loc_text = cursor.getString(cursor.getColumnIndex(Constants.NotifyLocation.KEY_LOCATION_TEXT));
                    LocationList.add(locationobj);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            //Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return LocationList;
    }

    // Delete all Calls from the database
    public void deleteAllCalls() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(Constants.NotifyCall.TABLE_CALL, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            // Log.d(TAG, "Error while trying to delete all posts and students");
        } finally {
            db.endTransaction();
        }
    }
    // Delete specific call in the database
    public void deleteSpecificCalls(CallObject callNotify) {
        SQLiteDatabase db = getWritableDatabase();
        String CALL_DELETE_QUERY =
                String.format("DELETE * FROM %s WHERE %s = ? ",
                        Constants.NotifyCall.TABLE_CALL ,Constants.NotifyCall.KEY_Call_ID
                );
        db.rawQuery(CALL_DELETE_QUERY,  new String[]{String.valueOf(callNotify.call_id)});
        db.beginTransaction();
    }
    public void deleteSpecificSms(SmsObject smsNotify) {
        SQLiteDatabase db = getWritableDatabase();
        String SMS_DELETE_QUERY =
                String.format("DELETE * FROM %s WHERE %s = ? ",
                        Constants.NotifySms.TABLE_SMS ,Constants.NotifySms.KEY_SMS_ID
                );
        db.rawQuery(SMS_DELETE_QUERY,  new String[]{String.valueOf(smsNotify.sms_id)});
        db.beginTransaction();
    }
    public void deleteSpecificLocation(LocationObject locNotify) {
        SQLiteDatabase db = getWritableDatabase();
        String LOCATION_DELETE_QUERY =
                String.format("DELETE * FROM %s WHERE %s = ? ",
                        Constants.NotifyLocation.TABLE_LOCATION ,Constants.NotifyLocation.KEY_LOCATION_ID
                );
        db.rawQuery(LOCATION_DELETE_QUERY,  new String[]{String.valueOf(locNotify.loc_id)});
        db.beginTransaction();
    }
    public void deleteSpecificTime(TimeObject timeNotify) {
        SQLiteDatabase db = getWritableDatabase();
        String TIME_DELETE_QUERY =
                String.format("DELETE * FROM %s WHERE %s = ? ",
                        Constants.NotifyTime.TABLE_TIME ,Constants.NotifyTime.KEY_TIME_ID
                );
        db.rawQuery(TIME_DELETE_QUERY,  new String[]{String.valueOf(timeNotify.time_id)});
        db.beginTransaction();
    }
}
