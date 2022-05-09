package com.example.postapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.postapp.databinding.FragmentAppointmentBinding;
import com.example.postapp.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentAppointment extends Fragment {
    private DatePickerDialog datePickerDialog;

    private FragmentAppointmentBinding binding;

    private int hour, minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAppointmentBinding.inflate(inflater);


        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        binding.btnHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(view);
            }
        });

        SpinnerNew spinnerAdapter = new SpinnerNew(getContext());
        binding.spinnerCreate.setAdapter(spinnerAdapter);
        binding.spinnerCreate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Log.e("Spinner", "selected pos: " + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // return inflater.inflate(R.layout.fragment_appointment, container, false);
        // Log.e("jgjg", "Fragment create");
        return binding.getRoot();
    }

    private void showDateDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.calendar_view);


        CalendarView calendarView =
                dialog.findViewById(R.id.calendar_view);

        Calendar calendar = Calendar.getInstance();

        calendarView.setMinDate(calendar.getTimeInMillis() - 1000);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + Utils.appointmentDays);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - Utils.appointmentDays);
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
                Toast.makeText(getContext(), "Posta romana nu lucreaza in weekend", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat data = new SimpleDateFormat("EEEE-dd-MM");
                String dataString = data.format(new Date(calendar.getTimeInMillis()));

                binding.btnDatePicker.setText(dataString);
                dialog.dismiss();
            }

        });

        dialog.show();

    }

    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            binding.btnHourPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select hour");
        timePickerDialog.show();
    }





}