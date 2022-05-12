package com.example.postapp.fragments;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.postapp.Appointment;
import com.example.postapp.ParcelInfo;
import com.example.postapp.R;
import com.example.postapp.SpinnerNew;
import com.example.postapp.databinding.FragmentAppointmentBinding;
import com.example.postapp.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentAppointment extends Fragment {

    private FragmentAppointmentBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private Date pickupDate;
    private int trackingNb;
    private String pickupHour;
    private String postalOffice;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAppointmentBinding.inflate(inflater);
        calendar = Calendar.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.etTrackingNb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.etTrackingNb.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.Green));
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
                postalOffice = Constants.opList[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnCreateAppt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                trackingNb = Integer.parseInt(binding.etTrackingNb.getText().toString());

                if(binding.etTrackingNb.getText().toString().equals(""))
                    binding.etTrackingNb.setError("Fill in package code");
                else if(binding.btnDatePicker.getText().toString().isEmpty())
                    binding.btnDatePicker.setError("Date must be selected");
                else if(binding.btnHourPicker.getText().toString().isEmpty())
                    binding.btnHourPicker.setError("Hour must be selected");
                else
                {
                    trackingNb = Integer.parseInt(binding.etTrackingNb.getText().toString());
                    List<Integer> trackingNbList = new ArrayList<>();
                    List<Integer> usedTrackingNbList = new ArrayList<>();

                    DatabaseReference ref = firebaseDatabase.getReference().child("ParcelInfo");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                                ParcelInfo parcelInfo = infoSnapshot.getValue(ParcelInfo.class);
                                trackingNbList.add(parcelInfo.getTrackingNb());
                            }
                            if(trackingNbList.contains(trackingNb)) {
                                Appointment appointment = new Appointment(trackingNb, postalOffice, pickupDate, pickupHour);

                                DatabaseReference refAppt = firebaseDatabase.getReference().child("Appointments");
                                refAppt.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                                            Appointment apptInfo = infoSnapshot.getValue(Appointment.class);
                                            usedTrackingNbList.add(apptInfo.getTrackingNumber());

                                        }
                                        if(usedTrackingNbList.contains(trackingNb)) {
                                            Toast.makeText(getContext(), "There is an appointment created for this code!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            appointment.setUid(refAppt.child("Appointments").push().getKey());
                                            refAppt.child(appointment.getUid()).setValue(appointment);
                                            Toast.makeText(getContext(), "Appointment created successfully!", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        throw error.toException();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getContext(), "Package code does not exist in the system.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            throw databaseError.toException(); // NEVER ignore errors
                        }
                    });

                }
            }
        });

        return binding.getRoot();
    }

    private void showDateDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.calendar_view);
        ImageButton btnCloseCal = dialog.findViewById(R.id.btnCloseCalendar);

        CalendarView calendarView =
                dialog.findViewById(R.id.calendar_view);
        Calendar calendar1 = Calendar.getInstance();
        calendarView.setMinDate(calendar1.getTimeInMillis() - 1000);
        calendar1.set(Calendar.DAY_OF_YEAR, calendar1.get(Calendar.DAY_OF_YEAR) + Constants.appointmentDays);
        calendarView.setMaxDate(calendar1.getTimeInMillis());

        calendarView.setOnDateChangeListener((calendarView1, i, i1, i2) -> {
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - Constants.appointmentDays);
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
                Toast.makeText(getContext(), "Posta Romana does not work in the weekend", Toast.LENGTH_SHORT).show();
            } else {
                pickupDate = calendar.getTime();
                SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
                String dataString = data.format(new Date(calendar.getTimeInMillis()));

                binding.btnDatePicker.setText(dataString);
                binding.btnDatePicker.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.Green));
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
                    Toast.makeText(getContext(), "Working hours are between 08:00 - 19:00", Toast.LENGTH_SHORT).show();
                }
                else {
                    pickupHour = String.format(Locale.getDefault(), "%02d:%02d", i, i1);
                    calendar.set(Calendar.HOUR, i);
                    calendar.set(Calendar.MINUTE, i1);
                    binding.btnHourPicker.setText(pickupHour);
                    binding.btnHourPicker.getCompoundDrawables()[0].setTint(getResources().getColor(R.color.Green));
                    btnSetDate.setEnabled(true);
                }

            }
        });

        dialog.show();
    }
}