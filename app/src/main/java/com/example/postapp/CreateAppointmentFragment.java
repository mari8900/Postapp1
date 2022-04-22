package com.example.postapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
    private int hour, minute;

    ListView listView;
    String[] postalOffices = {"Oficiul Postal 7", "Oficiul Postal 23", "Oficiul Postal 38", "Oficiul Postal 44", "Oficiul Postal 56", "Oficiul Postal 67", "Oficiul Postal 79", "Oficiul Postal 84"};
    String[] addresses = {"Sos. Giurgiului 119", "Str Romancierilor 1", "Str. Teiul Doamnei 19", "Str. Gheorghe Sincai 2", "Calea Crangasi 23", "Calea Plevnei 46-48", "Calea Mosilor 314", "Splaiul Independentei 290"};


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

        listView = view.findViewById(R.id.listview);
        ListViewAdapter listViewAdapter = new ListViewAdapter(container.getContext(), postalOffices, addresses);
        listView.setAdapter(listViewAdapter);

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
                btnTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select hour");
        timePickerDialog.show();
    }

    class ListViewAdapter extends ArrayAdapter<String> {
        Context context;
        String[] postalOffices;
        String[] addresses;

        ListViewAdapter(Context context, String[] postalOffices, String[] addresses) {
            super(context, R.layout.row, R.id.tvPoNumber, postalOffices);
            this.context = context;
            this.postalOffices = postalOffices;
            this.addresses = addresses;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView poNb = row.findViewById(R.id.tvPoNumber);
            TextView poAddress = row.findViewById(R.id.tvPoAddress);

            poNb.setText(postalOffices[position]);
            poAddress.setText(addresses[position]);

            return row;
        }
    }
}
