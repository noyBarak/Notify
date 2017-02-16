package com.example.david.notify_poc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class TimeCursorAdaptor extends ArrayAdapter<TimeObject> {
    TimeCursorAdaptor(Context context, ArrayList<TimeObject> users) {
        super(context, 0, users);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        TimeObject to =  getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_time_layout, parent, false);
        }
        // Lookup view for data population
        TextView tv_text = (TextView) convertView.findViewById(R.id.time_text_tv);
        TextView tv_date = (TextView) convertView.findViewById(R.id.time_date_tv);
        // Populate the data into the template view using the data object

        tv_text.setText(to.time_text);
        tv_date.setText(String.valueOf( to.time_at_ms));
        // Return the completed view to render on screen
        return convertView;
    }
}