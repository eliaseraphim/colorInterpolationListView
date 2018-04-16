package com.example.elia.colorinterpolationlistview;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Elia on 4/2/2018.
 */

public class ListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] string_color;
    private final int[] int_color, int_colorComp;

    public ListAdapter(Activity context, String[] string_color, int[] int_color,
                       int[] int_colorComp) {
        super(context, R.layout.listview_row, string_color);

        this.context = context;
        this.string_color = string_color;
        this.int_color = int_color;
        this.int_colorComp = int_colorComp;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        TextView text = (TextView) rowView.findViewById(R.id.txt_color);

        text.setText(string_color[position]);
        text.setBackgroundColor(int_color[position]);
        text.setTextColor(int_colorComp[position]);

        return rowView;
    };
}
