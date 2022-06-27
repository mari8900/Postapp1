package com.example.postapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.postapp.classes.Appointment;
import com.example.postapp.RecyclerAdapter;
import com.example.postapp.classes.User;
import com.example.postapp.authentication.LoginActivity;
import com.example.postapp.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentAccount extends Fragment {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private FragmentAccountBinding binding;
    private List<Appointment> appointmentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        displayInfoFromFirebase();
        listeners();

        return binding.getRoot();
    }


    private void listeners() {
        binding.imgEditMail.setOnClickListener(view -> {

        });

        binding.imgEditPass.setOnClickListener(view -> {

        });

        binding.imgDeleteAcc.setOnClickListener(view -> user.delete());

        binding.imgLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        binding.radioBtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rvAppointments.setVisibility(View.GONE);
                binding.accountView.setVisibility(View.VISIBLE);
                binding.tvNoAppt.setVisibility(View.GONE);
                binding.lottieNotFound.setVisibility(View.GONE);
            }
        });

        binding.radioBtnAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.rvAppointments.setVisibility(View.VISIBLE);
                binding.accountView.setVisibility(View.GONE);
                recycleMethod();
            }
        });
    }

    private void displayInfoFromFirebase() {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String fullName = userProfile.name;
                    String CNP = userProfile.CNP;
                    String rAddress = userProfile.address;
                    String email = userProfile.email;

                    appointmentList = new ArrayList<>();
                    appointmentList = userProfile.userAppointments;

                    binding.inputEtName.setText(fullName);
                    binding.inputEtCNP.setText(CNP);
                    binding.inputEtResAddr.setText(rAddress);
                    binding.inputEtEmailAddr.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recycleMethod() {
        if(appointmentList.isEmpty()) {
            binding.tvNoAppt.setVisibility(View.VISIBLE);
            binding.rvAppointments.setVisibility(View.GONE);
            binding.lottieNotFound.setVisibility(View.VISIBLE);

        }
        else {
            RecyclerAdapter adapter = new RecyclerAdapter(appointmentList, getContext());
            binding.rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
            // binding.rvAppointments.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.rvAppointments.setAdapter(adapter);
        }

    }
}