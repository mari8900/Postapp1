package com.example.postapp;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        List<Appointment> appointmentList;
        Context context;

        // RecyclerView recyclerView;
        public RecyclerAdapter(List<Appointment> appointmentList, Context context) {
            this.appointmentList = appointmentList;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.recycler_appointments, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
            String dataString = data.format(appointmentList.get(position).getPickupDate());
            String stringPackageNb = context.getResources().getString(R.string.strTrackNb);
            stringPackageNb = String.format(stringPackageNb, appointmentList.get(position).getTrackingNumber());

            String stringDate = context.getResources().getString(R.string.strDate);
            stringDate = String.format(stringDate, dataString);

            String stringHour = context.getResources().getString(R.string.strHour);
            stringHour = String.format(stringHour, appointmentList.get(position).getPickupHour());

            String stringPO = context.getResources().getString(R.string.strPO);
            stringPO = String.format(stringPO, appointmentList.get(position).getCurrentPO());

            holder.tvTrack.setText(HtmlCompat.fromHtml(stringPackageNb, HtmlCompat.FROM_HTML_MODE_LEGACY));
            holder.tvDate.setText(HtmlCompat.fromHtml(stringDate, HtmlCompat.FROM_HTML_MODE_LEGACY));
            holder.tvHour.setText(HtmlCompat.fromHtml(stringHour, HtmlCompat.FROM_HTML_MODE_LEGACY));
            holder.tvPO.setText(HtmlCompat.fromHtml(stringPO, HtmlCompat.FROM_HTML_MODE_LEGACY));

        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvTrack, tvDate, tvHour, tvPO;

            public ViewHolder(View itemView) {
                super(itemView);
                this.tvTrack = (TextView) itemView.findViewById(R.id.tvTrack);
                this.tvDate = (TextView) itemView.findViewById(R.id.tvDate);
                this.tvHour = (TextView) itemView.findViewById(R.id.tvHour);
                this.tvPO = (TextView) itemView.findViewById(R.id.tvPO);
            }
        }
    }

