package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class SupervisorSignupActivity extends AppCompatActivity {

    TextInputEditText nameInput, surnameInput, emailInput, usernameInput, passwordInput, passwordConfirmationInput, birthdateInput;
    MaterialButton signup;
    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_supervisore);

        datePickerFragment = new DatePickerFragment();

        nameInput = findViewById(R.id.signup_name);
        surnameInput = findViewById(R.id.signup_surname);
        emailInput = findViewById(R.id.signup_email);
        usernameInput = findViewById(R.id.signup_username);
        passwordInput = findViewById(R.id.signup_password);
        passwordConfirmationInput = findViewById(R.id.signup_password_confirm);
        birthdateInput = findViewById(R.id.signup_birthdate);
        signup = findViewById(R.id.signup_button);


        /* Parte per l'input della data */
        birthdateInput.setOnClickListener(v -> datePickerFragment.show(getSupportFragmentManager(), "date picker"));

        birthdateInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                birthdateInput.setShowSoftInputOnFocus(false);
                birthdateInput.setInputType(InputType.TYPE_NULL);
                birthdateInput.setFocusable(false);
                datePickerFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        datePickerFragment.setOnDatePickerFragmentChanged(new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDatePickerFragmentOkButton(DialogFragment dialog, Calendar date) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                birthdateInput.setText(format.format(date.getTime()));
            }

            @Override
            public void onDatePickerFragmentCancelButton(DialogFragment dialog) {

            }
        });


    }
}
