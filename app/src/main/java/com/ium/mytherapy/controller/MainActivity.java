package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newActivity = new Intent(this, LoginActivity.class);    // cambio subito activity
        startActivity(newActivity);
        finish();
    }
}
