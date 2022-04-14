package com.example.postapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                }
                catch (Exception e) {
                    Log.d("SplashScreen", e.getMessage());
                }
                finally {
                    startActivity(new Intent(getApplicationContext(),
                            LoginActivity.class));
                    finish(); //close the activity
                }
            };
        };
        splashTread.start();
    }
}