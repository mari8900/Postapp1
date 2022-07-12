package com.example.postapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class TimepickerAdapterHour extends RecyclerView.Adapter<TimepickerAdapterHour.ViewHolder>{

        ClickListener clickListener;
        public static List<String> hours = Arrays.asList("08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18");
        int selectedPos = -1;

        public interface ClickListener {
            void onClick(String hour, int position);
        }

        public TimepickerAdapterHour(ClickListener clickListener) {
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
            holder.itemView.setOnClickListener(view -> clickListener.onClick(hours.get(position), position));
            holder.tvHour.setText(hours.get(position));
            if(position == selectedPos) {
                holder.itemView.setBackgroundColor(Color.YELLOW);
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        public void updatePos(int newPosition) {
            selectedPos = newPosition;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return hours.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvHour;

            public ViewHolder(View itemView) {
                super(itemView);
                tvHour = itemView.findViewById(R.id.tvHourRecycler);

            }
        }
    }

