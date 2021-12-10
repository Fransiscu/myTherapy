package com.ium.mytherapy.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.ium.mytherapy.controller.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}