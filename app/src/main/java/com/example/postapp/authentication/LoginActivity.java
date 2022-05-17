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

import com.example.postapp.BottomNav;
import com.example.postapp.R;
import com.example.postapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityLoginBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        listeners();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textviewRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.textviewForgotPass:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        String email = binding.etLoginEmail.getText().toString().trim();
        String password = binding.etLoginPass.getText().toString().trim();

        if(email.isEmpty()) {
            binding.etLoginEmail.setError("Email is required!");
            binding.etLoginEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etLoginEmail.setError("Please enter a valid email");
            binding.etLoginEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            binding.etLoginPass.setError("Password is required!");
            binding.etLoginPass.requestFocus();
            return;
        }

        if(password.length() < 6) {
            binding.etLoginPass.setError("Password must be at least 6 characters");
            binding.etLoginPass.requestFocus();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        //progressBar.getProgressDrawable().setColorFilter(Color.rgb(0, 43, 127), android.graphics.PorterDuff.Mode.SRC_IN);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()) {
                        // redirect to user profile
                        startActivity(new Intent(LoginActivity.this, BottomNav.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void listeners() {
        binding.etLoginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutLoginUser.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        binding.etLoginPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? Color.rgb(0, 43, 127) : Color.GRAY;
                binding.textInputLayoutLoginPass.setStartIconTintList(ColorStateList.valueOf(color));
            }
        });

        binding.etLoginEmail.setText("nicolaiciucmaria19@stud.ase.ro");
        binding.etLoginPass.setText("123456");
    }
}