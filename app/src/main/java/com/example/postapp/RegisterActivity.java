package com.example.postapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView logo;
    private Button btnRegister;
    private EditText etName, etCNP, etAddress, etEmail, etPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        logo = findViewById(R.id.imageView);
        logo.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etCNP = findViewById(R.id.etCNP);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.logo:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    private void registerUser() {  // method for validating the fields

        String name = etName.getText().toString().trim();
        String cnp = etCNP.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(name.isEmpty()) {
            etName.setError("Full name is required");
            etName.requestFocus();
            return;
        }

        if(cnp.isEmpty()) {
            etCNP.setError("CNP is required");
            etCNP.requestFocus();
            return;
        }


        if(address.isEmpty()) {
            etAddress.setError("Residence address is required");
            etAddress.requestFocus();
            return;
        }

        if(address.length() < 15) {
            etAddress.setError("Residence address cannot be shorter than 15 characters");
            etAddress.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please provide a valid email address");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            etPassword.setError("Password must be set");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            etPassword.setError("Password length is min 6 characters");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
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
                                        progressBar.setVisibility(View.GONE);

                                        // redirect to login layout
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}