package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Supervisor;
import com.ium.mytherapy.model.SupervisorFactory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class SupervisorSignupActivity extends AppCompatActivity {

    TextInputEditText nameInput, surnameInput, emailInput, usernameInput, passwordInput, passwordConfirmationInput, birthdateInput;
    MaterialButton signupButton;
    TextInputLayout passwordInputLayout, passwordInputConfirmationLayout;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_supervisore);

        passwordInputLayout = findViewById(R.id.signup_password_toggle);
        passwordInputConfirmationLayout = findViewById(R.id.signup_password_confirm_toggle);

        nameInput = findViewById(R.id.signup_name);
        surnameInput = findViewById(R.id.signup_surname);
        emailInput = findViewById(R.id.signup_email);
        usernameInput = findViewById(R.id.signup_username);
        passwordInput = findViewById(R.id.signup_password);
        passwordConfirmationInput = findViewById(R.id.signup_password_confirm);
        birthdateInput = findViewById(R.id.signup_birthdate);
        signupButton = findViewById(R.id.signup_button);

        /* Calendario al tocco del campo data */
        birthdateInput.setShowSoftInputOnFocus(false);
        birthdateInput.setInputType(InputType.TYPE_NULL);
        birthdateInput.setFocusable(false);


        /* Resetto il punto esclamativo rosso qunado riprende a scrivere */
        passwordConfirmationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /* Per ricambiare il simbolo di errore a occhio per mostrare la password una volta che si riprende a scrivere */
            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
                passwordInputConfirmationLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* Resetto errore per birthdate*/
        birthdateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                birthdateInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* Resetto il punto esclamativo rosso qunado riprende a scrivere */
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /* Per ricambiare il simbolo di errore a occhio per mostrare la password una volta che si riprende a scrivere */
            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
                passwordInputConfirmationLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        signupButton.setOnClickListener(view -> {
            boolean valid = true;

            String name = Objects.requireNonNull(nameInput.getText()).toString();
            String surname = Objects.requireNonNull(surnameInput.getText()).toString();
            String email = Objects.requireNonNull(emailInput.getText()).toString();
            String username = Objects.requireNonNull(usernameInput.getText()).toString();
            String password = Objects.requireNonNull(passwordInput.getText()).toString();
            String passwordConfirmation = Objects.requireNonNull(passwordConfirmationInput.getText()).toString();
            String birthdate = Objects.requireNonNull(birthdateInput.getText()).toString();

            if (email.isEmpty()) {
                emailInput.setError("Inserisci una email valida");
                valid = false;
            } else {
                emailInput.setError(null);
            }
            if (name.isEmpty()) {
                nameInput.setError("Inserisci un nome valido");
                valid = false;
            } else {
                nameInput.setError(null);
            }
            if (surname.isEmpty()) {
                surnameInput.setError("Inserisci un cognome valido");
                valid = false;
            } else {
                surnameInput.setError(null);
            }
            if (username.isEmpty()) {
                usernameInput.setError("Inserisci uno username valido");
                valid = false;
            } else {
                usernameInput.setError(null);
            }
            if (password.isEmpty() || password.length() < 4 || password.length() > 10 || !password.equals(passwordConfirmation)) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                passwordInputConfirmationLayout.setPasswordVisibilityToggleEnabled(false);
                passwordInput.setError("Inserisci password valida");
                passwordConfirmationInput.setError("Inserisci una password valida");
                valid = false;
            } else {
                passwordInput.setError(null);
                passwordConfirmationInput.setError(null);
            }
            if (birthdate.isEmpty()) {
                birthdateInput.setError("Inserisci una data");
                valid = false;
            } else {
                birthdateInput.setError(null);
            }

            if (valid) {
                addSupervisor();
            }
        });


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

    public void addSupervisor() {
        Runnable signUp = () -> {
            Supervisor newSupervisor = new Supervisor();
            int max = 0;
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myTherapy/supervisors/");

            /* Setto id supervisore a seconda di quante cartelle ho */
            File[] files = f.listFiles();
            if (files != null) {
                for (File inFile : files) {
                    if (inFile.isDirectory()) {
                        String temp = inFile.getName();
                        if (Integer.parseInt(temp) >= max) {
                            max = Integer.parseInt(temp) + 1;
                        }
                    }
                }
                newSupervisor.setSupervisorId(max);
            } else {
                newSupervisor.setSupervisorId(0);
            }

            newSupervisor.setNome(Objects.requireNonNull(nameInput.getText()).toString());
            newSupervisor.setCognome(Objects.requireNonNull(surnameInput.getText()).toString());
            newSupervisor.setEmail(Objects.requireNonNull(emailInput.getText()).toString());
            newSupervisor.setUsername(Objects.requireNonNull(usernameInput.getText()).toString());
            newSupervisor.setPassword(Objects.requireNonNull(passwordInput.getText()).toString());
            newSupervisor.setDataNascita(Objects.requireNonNull(birthdateInput.getText()).toString());
            try {
                SupervisorFactory.getInstance().addSupervisor(newSupervisor);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getBaseContext(), "Utente registrato", Toast.LENGTH_LONG).show();
            finish();
        };

        signUp.run();

    }


}
