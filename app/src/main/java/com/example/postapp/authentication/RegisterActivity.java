package com.example.postapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.postapp.R;
import com.example.postapp.User;
import com.example.postapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(this);

        binding.tvAlreadyHaveAcc.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        listeners();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnRegister:
                registerUser();
                break;
            case R.id.tvAlreadyHaveAcc:
                startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void registerUser() {  // method for validating the fields

        String name = binding.etName.getText().toString().trim();
        String cnp = binding.etCNP.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if(name.isEmpty()) {
            binding.etName.setError("Full name is required");
            binding.etName.requestFocus();
            return;
        }

        if(cnp.isEmpty()) {
            binding.etCNP.setError("CNP is required");
            binding.etCNP.requestFocus();
            return;
        }


        if(address.isEmpty()) {
            binding.etAddress.setError("Residence address is required");
            binding.etAddress.requestFocus();
            return;
        }

        if(address.length() < 15) {
            binding.etAddress.setError("Residence address cannot be shorter than 15 characters");
            binding.etAddress.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Please provide a valid email address");
            binding.etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            binding.etPassword.setError("Password must be set");
            binding.etPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            binding.etPassword.setError("Password length is min 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        binding.progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(name, cnp, address, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        binding.progressBar2.setVisibility(View.GONE);

                                        // redirect to login layout
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        binding.progressBar2.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            binding.progressBar2.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void listeners() {
        binding.etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutName.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        binding.etCNP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutCNP.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        binding.etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutResidence.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        binding.etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutEmail.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        binding.etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutPassword.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });
    }
}