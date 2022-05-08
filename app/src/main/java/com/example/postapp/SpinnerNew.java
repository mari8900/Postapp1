package com.example.postapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class SpinnerNew extends BaseAdapter {
    private String[] opList = new String[] {"Oficiul Postal 1",
            "Oficiul Postal 2",
            "Oficiul Postal 3",
            "Oficiul Postal 4",
            "Oficiul Postal 5",
            "Oficiul Postal 6",
            "Oficiul Postal 7",
            "Oficiul Postal 8",
            "Oficiul Postal 9",
            "Oficiul Postal 10",
            "Oficiul Postal 11",
            "Oficiul Postal 12",
            "Oficiul Postal 13"};

    private String[] addressList = new String[] {"Adresa 1",
            "Adresa 2",
            "Adresa 3",
            "Adresa 4",
            "Adresa 5",
            "Adresa 6",
            "Adresa 7",
            "Adresa 8",
            "Adresa 9",
            "Adresa 10",
            "Adresa 11",
            "Adresa 12",
            "Adresa 13"};

    Context context;
    public SpinnerNew(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return addressList.length;
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

        textOP.setText(opList[i]);
        textAddress.setText(addressList[i]);

        return view;
    }
}
