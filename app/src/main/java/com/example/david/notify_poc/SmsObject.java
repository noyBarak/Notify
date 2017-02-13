package com.example.david.notify_poc;

import android.os.Parcel;
import android.os.Parcelable;

public class SmsObject implements Parcelable {
    int sms_id;
    String sms_number;
    String sms_name;
    int sms_in_out;//1 for incoming , 2 for outgoing , 3 for both
    String sms_text;

    protected SmsObject(Parcel in) {
        sms_id = in.readInt();
        sms_number = in.readString();
        sms_name = in.readString();
        sms_in_out = in.readInt();
        sms_text = in.readString();
    }

    public SmsObject() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sms_id);
        dest.writeString(sms_number);
        dest.writeString(sms_name);
        dest.writeInt(sms_in_out);
        dest.writeString(sms_text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmsObject> CREATOR = new Creator<SmsObject>() {
        @Override
        public SmsObject createFromParcel(Parcel in) {
            return new SmsObject(in);
        }

        @Override
        public SmsObject[] newArray(int size) {
            return new SmsObject[size];
        }
    };
}
