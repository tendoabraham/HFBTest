package com.elmahousingfinanceug.main_Pages.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elmahousingfinanceug.R;

import java.util.ArrayList;

public class Activity_Log_Adapter extends ArrayAdapter<String[]> {
    private final Activity context;
    private ArrayList<String[]> info;

    public Activity_Log_Adapter(Activity context, ArrayList<String[]> data) {
        super(context, 0, data);
        this.info = data;
        this.context = context;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_item_activity_log,null);
        final TextView ID = rowView.findViewById(R.id.ID);
        final TextView comment = rowView.findViewById(R.id.comment);
        final TextView date = rowView.findViewById(R.id.date);

        String[] data = info.get(position);
        ID.setText(data[0]);
        date.setText(data[1]);
        comment.setText(data[2]);

        return rowView;
    }
}