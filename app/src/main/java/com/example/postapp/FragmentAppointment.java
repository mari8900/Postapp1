package com.example.postapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.postapp.databinding.FragmentAppointmentBinding;

import java.util.Calendar;
import java.util.Locale;

public class FragmentAppointment extends Fragment {
    private DatePickerDialog datePickerDialog;

    private FragmentAppointmentBinding binding;

    private int hour, minute;

    String[] postalOffices = {"Oficiul Postal 7", "Oficiul Postal 23", "Oficiul Postal 38", "Oficiul Postal 44", "Oficiul Postal 56", "Oficiul Postal 67", "Oficiul Postal 79", "Oficiul Postal 84"};
    String[] addresses = {"Sos. Giurgiului 119", "Str Romancierilor 1", "Str. Teiul Doamnei 19", "Str. Gheorghe Sincai 2", "Calea Crangasi 23", "Calea Plevnei 46-48", "Calea Mosilor 314", "Splaiul Independentei 290"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAppointmentBinding.inflate(inflater);

        initDatePicker();

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
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

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                binding.btnDatePicker.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1) {
            return "Ian";
        }
        if(month == 2) {
            return "Feb";
        }
        if(month == 3) {
            return "Mar";
        }
        if(month == 4) {
            return "Apr";
        }
        if(month == 5) {
            return "Mai";
        }
        if(month == 6) {
            return "Iun";
        }
        if(month == 7) {
            return "Iul";
        }
        if(month == 8) {
            return "Aug";
        }
        if(month == 9) {
            return "Sep";
        }
        if(month == 10) {
            return "Oct";
        }
        if(month == 11) {
            return "Noi";
        }
        if(month == 12) {
            return "Dec";
        }

        return "Ian";
    }

    public void openDatePicker() {
        datePickerDialog.show();
    }

    public void openTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                binding.btnHourPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select hour");
        timePickerDialog.show();
    }





}