package com.example.postapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.postapp.TimepickerAdapterHour;
import com.example.postapp.TimepickerAdapterMinutes;
import com.example.postapp.authentication.LoginActivity;
import com.example.postapp.classes.Appointment;
import com.example.postapp.classes.ParcelInfo;
import com.example.postapp.R;
import com.example.postapp.SpinnerNew;
import com.example.postapp.classes.User;
import com.example.postapp.databinding.FragmentAppointmentBinding;
import com.example.postapp.utils.AlarmReceiver;
import com.example.postapp.utils.Constants;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FragmentAppointment extends Fragment {

    private FragmentAppointmentBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private Date pickupDate;
    private int trackingNb;
    private String pickupHour;
    private String postalOffice;
    private Calendar calendar;
    private TimepickerAdapterHour timepickerAdapterHour;
    private TimepickerAdapterMinutes timepickerAdapterMinutes;
    private List<Appointment> appointmentList = new ArrayList<>();
    private String selectedHour = "";
    private String selectedMin = "";
    private boolean checkedNotif, firstStart = true;
    private AnimatorSet mSetRightOut, mSetLeftIn, selectedRightOut, selectedLeftOut;
    int spinnerpos = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentAppointmentBinding.inflate(inflater);
        calendar = Calendar.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        listeners();
        loadAnimations();

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
                            spinnerpos = i;
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
        binding.notifLayout.setVisibility(View.VISIBLE);
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
            if (pickupDate == null) {
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
                binding.btnHourPicker.setText("");
                //binding.btnDatePicker.setText("");
                if(!firstStart) {
                    if(spinnerpos != i) {
                        new AlertDialog.Builder(new ContextThemeWrapper(requireContext(), R.style.Dialog))
                                .setTitle("Payment info")
                                .setMessage("In order to move the parcel from one Post Office to another, an additional fee of 15 RON is requested.")
                                .setPositiveButton("Agree", (dialog, which) -> unknown())
                                .setNegativeButton("Cancel", (dialog, which) -> binding.spinnerCreate.setSelection(spinnerpos))
                                .show();

                    }

                } else {
                    firstStart = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.imgLupa.setOnClickListener(view -> searchTrackingNo());

        binding.btnCreateAppt.setOnClickListener(view -> {
            addAppointmentFirebase();
        });

        binding.notifBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkedNotif = b;
            }
        });
    }

    private void unknown() {

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
            if(postalOffice.equals(Constants.opList[spinnerpos])) {
                createAppointment();
            } else {
                addCreditCard();
            }
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
                                binding.btnCreateAppt.setVisibility(View.GONE);
                                binding.notifLayout.setVisibility(View.GONE);

                                addAppointmentsToUser(appointment);
                                if(checkedNotif) {
                                    notifInit();
                                }

                                binding.lottieAppointmentCreated.addAnimatorListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        Navigation.findNavController(binding.lottieAppointmentCreated).navigate(R.id.action_fragmentAppointment_to_fragmentAccount);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });
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

                if (userProfile != null) {
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
            if (!selectedHour.equals("") && !selectedMin.equals("")) {
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

        for (Appointment a : appointmentList) {
            if (a.getCurrentPO().equals(postalOffice)) {
                String appointmentsDates = sdf.format(a.getPickupDate());
                if (appointmentsDates.equals(selectedDate)) {
                    String[] divided = a.getPickupHour().split(":");
                    if (appointmentsHours.get(divided[0]) == null) {
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
            if (checkAvailableMinutes != null) {
                List<Boolean> isAvailable = new ArrayList<>(Collections.nCopies(6, true));

                for (String s : checkAvailableMinutes) {
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
            if (!selectedHour.equals("")) {
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

    private void addCreditCard() {
        final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.credit_card_dialog);
        Button btnNext, btnSave, btnBack;
        ImageButton btnClose;
        ImageView imgCardFront, imgCardBack;
        EditText etCardNo, etCardholderName;
        Spinner spinnerM, spinnerY;
        TextInputLayout layoutNumber, layoutName, layoutCVV;

        btnNext = dialog.findViewById(R.id.btnNextStep);
        btnSave = dialog.findViewById(R.id.btnSaveCreditCard);
        imgCardFront = dialog.findViewById(R.id.imgCardFront);
        imgCardBack = dialog.findViewById(R.id.imgCardBack);
        etCardNo = dialog.findViewById(R.id.etCardNumber);
        etCardholderName = dialog.findViewById(R.id.etCardholderName);

        spinnerM = dialog.findViewById(R.id.spinnerMonth);
        spinnerY = dialog.findViewById(R.id.spinnerYear);
        layoutNumber = dialog.findViewById(R.id.textInputLayoutCreditCard);
        layoutName = dialog.findViewById(R.id.textInputLayoutCardholderName);
        layoutCVV = dialog.findViewById(R.id.textInputLayoutCVV);
        btnBack = dialog.findViewById(R.id.btnBackCard);
        btnClose = dialog.findViewById(R.id.btnCloseCard);

        btnNext.setOnClickListener(view -> {
            String cardNumber = etCardNo.getText().toString().trim();
            String cardholderName = etCardholderName.getText().toString().trim();

            if(cardNumber.isEmpty()) {
                etCardNo.setError("Card number is required!");
                etCardNo.requestFocus();

            } else if(cardNumber.length() != 16) {
                etCardNo.setError("Card number must be 16 digits");
                etCardNo.requestFocus();

            } else if(cardholderName.isEmpty()) {
                etCardholderName.setError("Cardholder name is required!");
                etCardholderName.requestFocus();
            } else {
                layoutNumber.setVisibility(View.GONE);
                layoutName.setVisibility(View.GONE);
                layoutCVV.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                spinnerM.setVisibility(View.GONE);
                spinnerY.setVisibility(View.GONE);
                flipCard(imgCardFront, imgCardBack);
            }

        });

        btnBack.setOnClickListener(view -> {
            layoutNumber.setVisibility(View.VISIBLE);
            layoutName.setVisibility(View.VISIBLE);
            layoutCVV.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            spinnerM.setVisibility(View.VISIBLE);
            spinnerY.setVisibility(View.VISIBLE);
            flipCard(imgCardBack, imgCardFront);
        });

        btnClose.setOnClickListener(view -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            EditText etCVV;
            TextView tvSuccess, tvAddCard;
            LottieAnimationView lottieCardSuccess;

            etCVV = dialog.findViewById(R.id.etCVV);
            tvSuccess = dialog.findViewById(R.id.tvPaySuccess);
            tvAddCard = dialog.findViewById(R.id.tvAddCreditCard);
            lottieCardSuccess = dialog.findViewById(R.id.lottie_card_success);
            String cvv = etCVV.getText().toString().trim();

            if(cvv.isEmpty()) {
                etCVV.setError("CVV is required!");
                etCVV.requestFocus();
            } else if(cvv.length() != 3) {
                etCVV.setError("CVV code must be 3 digits");
                etCVV.requestFocus();
            } else {
                //Toast.makeText(getContext(), "Payment successfull!", Toast.LENGTH_LONG).show();
                imgCardBack.setVisibility(View.GONE);
                layoutCVV.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                tvAddCard.setVisibility(View.GONE);
                tvSuccess.setVisibility(View.VISIBLE);
                lottieCardSuccess.setVisibility(View.VISIBLE);
                lottieCardSuccess.playAnimation();
                lottieCardSuccess.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        dialog.dismiss();
                        createAppointment();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

            }
        });

        List<String> monthsAdapter = Arrays.asList("01 JAN", "02 FEB", "03 MAR", "04 APR", "05 MAY", "06 JUN", "07 JUL", "08 AUG", "09 SEP", "10 OCT", "11 NOV", "12 DEC");
        List<Integer> yearsAdapter = Arrays.asList(2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029);
        ArrayAdapter<String> spinnerMonthsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, monthsAdapter);
        spinnerMonthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<Integer> spinnerYearsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yearsAdapter);
        spinnerYearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerM.setAdapter(spinnerMonthsAdapter);
        spinnerY.setAdapter(spinnerYearsAdapter);

        dialog.show();
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.in_animation);
        selectedRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_animation);
        selectedLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.in_animation);
        selectedRightOut.addListener(animatorListenerAdapter);
        selectedLeftOut.addListener(animatorListenerAdapter);
    }

    private AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
        }
    };

    public void flipCard(ImageView flipFront, ImageView flipBack) {
            mSetRightOut.setTarget(flipFront);
            mSetLeftIn.setTarget(flipBack);
            mSetRightOut.start();
            mSetLeftIn.start();
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

    public void notifInit() {
        AlarmManager alarmMgr;
        PendingIntent alarmIntentList;

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.setAction("NOTIFICATION");
        alarmMgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmIntentList = PendingIntent.getBroadcast(getContext(), 3, intent, 0);

        setDailyAlarm(alarmMgr, alarmIntentList);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("postapp", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }

    private void setDailyAlarm(AlarmManager alarmMgr, PendingIntent alarmIntentList) {
        createNotificationChannel();
        newAlarmService(alarmMgr, alarmIntentList);
    }

    private void newAlarmService(AlarmManager alarmManager, PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
        ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
        PackageManager pm = requireContext().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Calendar calendarAlarm = (Calendar) calendar.clone();
        calendarAlarm.set(Calendar.DAY_OF_YEAR, calendarAlarm.get(Calendar.DAY_OF_YEAR) - 1);


        calendarAlarm.set(Calendar.HOUR_OF_DAY, 22);
        calendarAlarm.set(Calendar.MINUTE, 55);

        String notifMessage = "Don't forget about your pickup appointment tomorrow at PO " + postalOffice + " at " + pickupHour;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.NOTIF_MESSAGE, notifMessage);
        editor.apply();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarAlarm.getTimeInMillis(), pendingIntent);
    }
}