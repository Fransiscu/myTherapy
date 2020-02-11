package com.ium.mytherapy.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        login.setOnClickListener(view -> {
            Intent supervisorHomeActivity = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
            supervisorHomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MainActivity.USER_TYPE, "supervisor");
            editor.apply();
            finish();
            startActivity(supervisorHomeActivity);
        });

        signup.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), SupervisorSignupActivity.class);
            startActivity(newActivity);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
        finish();
    }
}
