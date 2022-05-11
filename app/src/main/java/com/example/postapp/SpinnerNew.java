package com.example.postapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.postapp.utils.Constants;

public class SpinnerNew extends BaseAdapter {

    Context context;
    public SpinnerNew(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Constants.addressList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.spinner_settings, null);
        TextView textOP = view.findViewById(R.id.textSpinner);
        TextView textAddress = view.findViewById(R.id.textAddressSpinner);

        textOP.setText(Constants.opList[i]);
        textAddress.setText(Constants.addressList[i]);

        return view;
    }
}
