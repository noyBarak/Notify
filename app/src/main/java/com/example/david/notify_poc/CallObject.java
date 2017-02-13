package com.example.david.notify_poc;

import android.os.Parcel;
import android.os.Parcelable;

class CallObject implements Parcelable {
    int call_id;
    String call_number;
    String call_name;
    int call_in_out;    //1 for incoming , 2 for outgoing , 3 for both
    String call_text;
    CallObject(){

    }

    protected CallObject(Parcel in) {
        call_id = in.readInt();
        call_number = in.readString();
        call_name = in.readString();
        call_in_out = in.readInt();
        call_text = in.readString();
    }

    public static final Creator<CallObject> CREATOR = new Creator<CallObject>() {
        @Override
        public CallObject createFromParcel(Parcel in) {
            return new CallObject(in);
        }

        @Override
        public CallObject[] newArray(int size) {
            return new CallObject[size];
        }
    };

    @Override
    public int describeContents() {
        return  call_id;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(call_id);
        parcel.writeString(call_number);
        parcel.writeString(call_name);
        parcel.writeInt(call_in_out);
        parcel.writeString(call_text);
    }
}
