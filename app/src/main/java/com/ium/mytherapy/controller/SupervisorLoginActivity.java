package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ium.mytherapy.R;

import androidx.appcompat.app.AppCompatActivity;

public class SupervisorLoginActivity extends AppCompatActivity {

    TextView signup;
    MaterialButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_supervisore);

        signup = findViewById(R.id.registrati);
        login = findViewById(R.id.supervisore_login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                startActivity(newActivity);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(getApplicationContext(), SupervisorSignupActivity.class);
                startActivity(newActivity);
            }
        });
    }
}
