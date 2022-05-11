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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
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

        binding.etTrackingNb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.etTrackingNb.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.RomaniaBlue));
            }
        });

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        binding.btnHourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
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
        ImageButton btnCloseCal = dialog.findViewById(R.id.btnCloseCalendar);


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
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
                Toast.makeText(getContext(), "Posta romana nu lucreaza in weekend", Toast.LENGTH_SHORT).show();
            } else {
                SimpleDateFormat data = new SimpleDateFormat("EEEE-dd-MM");
                String dataString = data.format(new Date(calendar.getTimeInMillis()));

                binding.btnDatePicker.setText(dataString);
                binding.btnDatePicker.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.RomaniaBlue));
                dialog.dismiss();
            }

        });

        btnCloseCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showTimePickerDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.timepicker_view);
        Button btnSetDate = dialog.findViewById(R.id.btnSetTime);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        TimePicker timePickerView =
                dialog.findViewById(R.id.timepicker_view);

        timePickerView.setIs24HourView(true);

        btnSetDate.setEnabled(false);
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnHourPicker.setText("");
                dialog.dismiss();
            }
        });

        timePickerView.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                if(i < 8 || i > 19) {
                    Toast.makeText(getContext(), "Programul de lucru este intre 08:00 - 19:00", Toast.LENGTH_SHORT).show();
                }
                else {
                    binding.btnHourPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", i, i1));
                    binding.btnHourPicker.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.RomaniaBlue));
                    btnSetDate.setEnabled(true);
                }

            }
        });

        dialog.show();
    }







}