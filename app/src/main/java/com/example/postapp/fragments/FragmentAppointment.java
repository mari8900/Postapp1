package com.example.postapp.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.example.postapp.TimepickerAdapterHour;
import com.example.postapp.TimepickerAdapterMinutes;
import com.example.postapp.classes.Appointment;
import com.example.postapp.classes.ParcelInfo;
import com.example.postapp.R;
import com.example.postapp.SpinnerNew;
import com.example.postapp.classes.User;
import com.example.postapp.databinding.FragmentAppointmentBinding;
import com.example.postapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAppointment extends Fragment {

    private FragmentAppointmentBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private Date pickupDate;
    private int trackingNb;
    private String pickupHour;
    private String postalOffice;
    private Calendar calendar;
    TimepickerAdapterHour timepickerAdapterHour;
    TimepickerAdapterMinutes timepickerAdapterMinutes;
    List<Appointment> appointmentList = new ArrayList<>();
    String selectedHour = "";
    String selectedMin = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAppointmentBinding.inflate(inflater);
        calendar = Calendar.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        listeners();

        return binding.getRoot();
    }

    private void searchTrackingNo() {
        trackingNb = Integer.parseInt(binding.etTrackingNb.getText().toString());
        List<Integer> trackingNbList = new ArrayList<>();
        List<String> poList = new ArrayList<>();

        reference.child("ParcelInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                    ParcelInfo parcelInfo = infoSnapshot.getValue(ParcelInfo.class);
                    trackingNbList.add(parcelInfo.getTrackingNb());
                    poList.add(parcelInfo.getPostalOffice());
                }

                if (trackingNbList.contains(trackingNb)) {
                    setViewCorrectNo();
                    String op = poList.get(trackingNbList.indexOf(trackingNb));
                    for (int i = 0; i < Constants.opList.length; i++) {
                        if (Constants.opList[i].equals(op)) {
                            binding.spinnerCreate.setSelection(i);
                            postalOffice = Constants.opList[i];
                            break;
                        }
                    }
                    getAppointmentsFromDb();
                } else {
                    binding.textViewNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // NEVER ignore errors
            }
        });
    }

    private void setViewCorrectNo() {
        binding.cardviewDATE.setVisibility(View.VISIBLE);
        binding.cardviewTIME.setVisibility(View.VISIBLE);
        binding.cardviewSPINNER.setVisibility(View.VISIBLE);
        binding.btnCreateAppt.setVisibility(View.VISIBLE);
        binding.imgLupa.setVisibility(View.GONE);
        binding.textViewNotFound.setVisibility(View.GONE);
        binding.tvCreateYourAppt.setText("Book your appointment");
    }

    private void listeners() {
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

        binding.btnDatePicker.setOnClickListener(view -> showDateDialog());

        binding.btnHourPicker.setOnClickListener(view -> {
            if(pickupDate == null) {
                Toast.makeText(getContext(), "Please select the date first!", Toast.LENGTH_LONG).show();
            } else {
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

        binding.imgLupa.setOnClickListener(view -> searchTrackingNo());

        binding.btnCreateAppt.setOnClickListener(view -> addAppointmentFirebase());
    }

    private void addAppointmentFirebase() {

        trackingNb = Integer.parseInt(binding.etTrackingNb.getText().toString());

        if (binding.etTrackingNb.getText().toString().equals(""))
            binding.etTrackingNb.setError("Fill in package code");
        else if (binding.btnDatePicker.getText().toString().isEmpty())
            binding.btnDatePicker.setError("Date must be selected");
        else if (binding.btnHourPicker.getText().toString().isEmpty())
            binding.btnHourPicker.setError("Hour must be selected");
        else {
            createAppointment();
        }

    }

    private void createAppointment() {

        trackingNb = Integer.parseInt(binding.etTrackingNb.getText().toString());
        List<Integer> trackingNbList = new ArrayList<>();
        List<Integer> usedTrackingNbList = new ArrayList<>();

        reference.child("ParcelInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                    ParcelInfo parcelInfo = infoSnapshot.getValue(ParcelInfo.class);
                    trackingNbList.add(parcelInfo.getTrackingNb());

                }
                if (trackingNbList.contains(trackingNb)) {
                    Appointment appointment = new Appointment(trackingNb, postalOffice, pickupDate, pickupHour);

                    reference.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                                Appointment apptInfo = infoSnapshot.getValue(Appointment.class);
                                usedTrackingNbList.add(apptInfo.getTrackingNumber());

                            }
                            if (usedTrackingNbList.contains(trackingNb)) {
                                Toast.makeText(getContext(), "There is an appointment created for this code!", Toast.LENGTH_SHORT).show();
                            } else {
                                appointment.setUid(reference.child("Appointments").push().getKey());
                                reference.child("Appointments").child(appointment.getUid()).setValue(appointment);
                                //Toast.makeText(getContext(), "Appointment created successfully!", Toast.LENGTH_SHORT).show();

                                binding.tvApptCreated.setVisibility(View.VISIBLE);
                                binding.groupCreateAppointment.setVisibility(View.GONE);
                                binding.lottieAppointmentCreated.setVisibility(View.VISIBLE);

                                addAppointmentsToUser(appointment);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            throw error.toException();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Package code does not exist in the system.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // NEVER ignore errors
            }
        });
    }

    private void addAppointmentsToUser(Appointment appointment) {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    List<Appointment> appointments = userProfile.getUserAppointments();

                    appointments.add(appointment);
                    firebaseDatabase.getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("userAppointments")
                            .setValue(appointments);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
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
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                Toast.makeText(getContext(), "Posta Romana does not work in the weekend", Toast.LENGTH_SHORT).show();
            } else {
                pickupDate = calendar.getTime();
                SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
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
        dialog.setContentView(R.layout.dialog_timepicker);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageButton btnClose;
        Button btnSetTime;
        btnClose = dialog.findViewById(R.id.btnClose);
        btnSetTime = dialog.findViewById(R.id.btnSetTime);

        btnClose.setOnClickListener(view -> dialog.dismiss());

        btnSetTime.setOnClickListener(view -> {
            if(!selectedHour.equals("") && !selectedMin.equals("")) {
                pickupHour = selectedHour + ":" + selectedMin;
                dialog.dismiss();
                binding.btnHourPicker.setText(pickupHour);
            } else {
                Toast.makeText(getContext(), "Please select a desired hour!", Toast.LENGTH_LONG).show();
            }

        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(pickupDate);
        Map<String, List<String>> appointmentsHours = new HashMap<>();


        for(Appointment a: appointmentList) {
            if(a.getCurrentPO().equals(postalOffice)) {
                String appointmentsDates = sdf.format(a.getPickupDate());
                if(appointmentsDates.equals(selectedDate)) {
                    String[] divided = a.getPickupHour().split(":");
                    if(appointmentsHours.get(divided[0]) == null) {
                        appointmentsHours.put(divided[0], new ArrayList<>());
                        List<String> usedHours = appointmentsHours.get(divided[0]);
                        usedHours.add(divided[1]);
                        appointmentsHours.put(divided[0], usedHours);
                    } else {
                        List<String> usedHours = appointmentsHours.get(divided[0]);
                        usedHours.add(divided[1]);
                        appointmentsHours.put(divided[0], usedHours);
                    }

                }
            }
        }

        RecyclerView rvHour = dialog.findViewById(R.id.rvTimepickerHour);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        timepickerAdapterHour = new TimepickerAdapterHour((hour, position) -> {
            timepickerAdapterHour.updatePos(position);
            selectedHour = hour;

            List<String> checkAvailableMinutes = appointmentsHours.get(hour);
            if(checkAvailableMinutes != null) {
                List<Boolean> isAvailable = new ArrayList<>(Collections.nCopies(6, true));

                for(String s : checkAvailableMinutes) {
                    int p = Constants.minutes.indexOf(s);
                    isAvailable.set(p, false);
                }
                timepickerAdapterMinutes.updateAvailability(isAvailable);
            }
        });
        rvHour.setAdapter(timepickerAdapterHour);
        rvHour.setLayoutManager(linearLayoutManager);


        RecyclerView rvMinutes = dialog.findViewById(R.id.rvTimepickerMinute);
        LinearLayoutManager linearLayoutManagerMin = new LinearLayoutManager(getContext());
        timepickerAdapterMinutes = new TimepickerAdapterMinutes((minutes, pos) -> {
            if(!selectedHour.equals("")) {
                timepickerAdapterMinutes.updatePos(pos);
                selectedMin = minutes;
            } else {
                Toast.makeText(getContext(), "Please select the hour first!", Toast.LENGTH_LONG).show();
            }

        });
        rvMinutes.setAdapter(timepickerAdapterMinutes);
        rvMinutes.setLayoutManager(linearLayoutManagerMin);



        dialog.show();
    }

    private void getAppointmentsFromDb() {


        reference.child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                    appointmentList.add(infoSnapshot.getValue(Appointment.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}