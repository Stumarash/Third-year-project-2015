package com.innovative.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.innovative.emergencyapp.R;

/**
 * class handling a custom adapter for my emergency list
 */
public class IncidentAdapter extends ArrayAdapter<String> {

    public IncidentAdapter(Context context,String[] values) {
        super(context, R.layout.row_layout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theinflater = LayoutInflater.from(getContext());
        View theView =theinflater.inflate(R.layout.row_layout,parent,false);
        String list = getItem(position);
        TextView text = (TextView)theView.findViewById(R.id.row_text);
        text.setText(list);

        return theView;
    }
}
