package com.example.postapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;

public class CreateAppointmentFragment extends Fragment {
    private DatePickerDialog datePickerDialog;
    private Button btnDate;
    private Button btnTime;
    private EditText etTrackingNumber;
    private int hour, minute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDatePicker();

        View view = inflater.inflate(R.layout.fragment_createappointment, container, false);

        btnDate = (Button) view.findViewById(R.id.btnDatePicker);
        //btnDate.setText(getTodaysDate());
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        btnTime = view.findViewById(R.id.btnHourPicker);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(view);
            }
        });

        etTrackingNumber = view.findViewById(R.id.etTrackingNb);



        // return inflater.inflate(R.layout.fragment_createappointment, container, false);
        return view;
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
                btnDate.setText(date);
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
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1) {
            return "JAN";
        }
        if(month == 2) {
            return "FEB";
        }
        if(month == 3) {
            return "MAR";
        }
        if(month == 4) {
            return "APR";
        }
        if(month == 5) {
            return "MAY";
        }
        if(month == 6) {
            return "JUN";
        }
        if(month == 7) {
            return "JUL";
        }
        if(month == 8) {
            return "AUG";
        }
        if(month == 9) {
            return "SEP";
        }
        if(month == 10) {
            return "OCT";
        }
        if(month == 11) {
            return "NOV";
        }
        if(month == 12) {
            return "DEC";
        }

        return "JAN";
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
                btnTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select hour");
        timePickerDialog.show();
    }
}
