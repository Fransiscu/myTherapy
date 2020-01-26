package com.ium.mytherapy.controller;

import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.ium.mytherapy.R;

import androidx.appcompat.app.AppCompatActivity;

public class SupervisorHomeActivity extends AppCompatActivity {

    public MaterialCardView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_supervisore);

    }
}
