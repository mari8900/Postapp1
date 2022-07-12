package com.example.postapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimepickerAdapterMinutes extends RecyclerView.Adapter<TimepickerAdapterMinutes.ViewHolder>{

        ClickListener clickListener;
        public static List<String> minutes = Arrays.asList("00", "10", "20", "30", "40", "50");
        int selectedPos = -1;
        List<Boolean> isAvailable = new ArrayList<>(Collections.nCopies(6, true));


        public interface ClickListener {
            void onClick(String min, int pos);
        }

        public TimepickerAdapterMinutes(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.recycler_timepicker, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(view -> clickListener.onClick(minutes.get(position), position));
            holder.tvMinutes.setText(minutes.get(position));

            if(position == selectedPos) {
                holder.itemView.setBackgroundColor(Color.YELLOW);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
            if (isAvailable.get(position)) {
                holder.tvMinutes.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.RomaniaBlue));
                holder.itemView.setEnabled(true);
            } else {
                holder.tvMinutes.setTextColor(Color.GRAY);
                holder.itemView.setEnabled(false);
            }
        }

        public void updatePos(int newPosition) {
            selectedPos = newPosition;
            notifyDataSetChanged();
        }

        public void updateAvailability(List<Boolean> availableHours) {
            isAvailable = availableHours;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return minutes.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvMinutes;

            public ViewHolder(View itemView) {
                super(itemView);
                tvMinutes = itemView.findViewById(R.id.tvHourRecycler);
            }
        }
    }

