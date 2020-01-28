package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class SupervisorSignupActivity extends AppCompatActivity {

    TextInputEditText nameInput, surnameInput, emailInput, usernameInput, passwordInput, passwordConfirmationInput, birthdateInput;
    MaterialButton signup;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_supervisore);

        nameInput = findViewById(R.id.signup_name);
        surnameInput = findViewById(R.id.signup_surname);
        emailInput = findViewById(R.id.signup_email);
        usernameInput = findViewById(R.id.signup_username);
        passwordInput = findViewById(R.id.signup_password);
        passwordConfirmationInput = findViewById(R.id.signup_password_confirm);
        birthdateInput = findViewById(R.id.signup_birthdate);
        signup = findViewById(R.id.signup_button);

        /* Calendario al tocco del campo data */
        birthdateInput.setShowSoftInputOnFocus(false);
        birthdateInput.setInputType(InputType.TYPE_NULL);
        birthdateInput.setFocusable(false);

        birthdateInput.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> birthdateInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

    }
}
