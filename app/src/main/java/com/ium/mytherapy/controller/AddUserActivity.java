package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Supervisor;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {

    TextInputEditText nameInput, surnameInput, dateInput, emailInput, usernameInput, passwordInput;
    TextInputLayout passwordInputLayout;
    TextView closeAlert;
    MaterialCardView alertCard;
    MaterialButton addUserButton;
    ScrollView addUserScrollView;

    private int mYear, mMonth, mDay;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_activity);

        addUserButton = findViewById(R.id.conferma_aggiunta_utente_button);

        usernameInput = findViewById(R.id.user_username);
        passwordInput = findViewById(R.id.user_password);
        surnameInput = findViewById(R.id.user_surname);
        emailInput = findViewById(R.id.user_email);
        nameInput = findViewById(R.id.user_name);
        dateInput = findViewById(R.id.user_data);

        alertCard = findViewById(R.id.adduser_alert_card);
        closeAlert = findViewById(R.id.x_chiudi_alert);

        addUserScrollView = findViewById(R.id.aggiunta_utente_scrollview);

        passwordInputLayout = findViewById(R.id.user_password_toggle);

        /* Hiding "ATTENZIONE" card */
        closeAlert.setOnClickListener(view -> alertCard.setVisibility(View.GONE));

        /* Showing calender on date input field tap */
        dateInput.setShowSoftInputOnFocus(false);
        dateInput.setInputType(InputType.TYPE_NULL);
        dateInput.setFocusable(false);

        /* Resetting errors in case of wrong fields during the previous attempt */
        dateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dateInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /* Calendar Popup */
        dateInput.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mMonth = c.get(Calendar.MONTH);
            mYear = c.get(Calendar.YEAR);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> dateInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        /* addUser on click listener */
        addUserButton.setOnClickListener(view -> {
            boolean valid = true;
            String name = Objects.requireNonNull(nameInput.getText()).toString();
            String surname = Objects.requireNonNull(surnameInput.getText()).toString();
            String email = Objects.requireNonNull(emailInput.getText()).toString();
            String username = Objects.requireNonNull(usernameInput.getText()).toString();
            String password = Objects.requireNonNull(passwordInput.getText()).toString();
            String birthdate = Objects.requireNonNull(dateInput.getText()).toString();

            /* Checking for mistakes in input fields */
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
            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
                passwordInput.setError("Inserisci una password valida");
                valid = false;
            } else {
                passwordInput.setError(null);
            }
            if (birthdate.isEmpty()) {
                dateInput.setError("Inserisci una data");
                valid = false;
            } else {
                dateInput.setError(null);
            }

            if (valid) {    // if everything is fine
                new MaterialAlertDialogBuilder(this)    // open this material alert for confirmation
                        .setTitle("AGGIUNTA UTENTE")
                        .setMessage("Aggiungere il nuovo account?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            addUser();
                            Intent backToSupervisorHome = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                            backToSupervisorHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(backToSupervisorHome);
                            finish();
                        })
                        .setNegativeButton("Annulla", (dialogInterface, i) -> {
                        })
                        .show();
            }
            else { // if not valid
                Toast.makeText(getBaseContext(), "Errore - Controlla i campi evidenziati", Toast.LENGTH_LONG).show();
            }
        });
    }

    /* addUser thread method */
    private void addUser() {
        Runnable signUp = () -> {
            User user = new User();
            Supervisor test = new Supervisor();
            test.setSupervisorId(0);
            int max = 0;
            File f = new File(DefaultValues.usersDir.toString());

            /* Setting supervisor id  */
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
                user.setUserId(max);
            } else {
                user.setUserId(0);
            }

            user.setName(Objects.requireNonNull(nameInput.getText()).toString());
            user.setSurname(Objects.requireNonNull(surnameInput.getText()).toString());
            user.setBirthDate(Objects.requireNonNull(dateInput.getText()).toString());
            user.setEmail(Objects.requireNonNull(emailInput.getText()).toString());
            user.setUsername(Objects.requireNonNull(usernameInput.getText()).toString());
            user.setPassword(Objects.requireNonNull(passwordInput.getText()).toString());

            /* Aggiungo avatar di default al nuovo utente */
            new File(DefaultValues.dir + "/avatar_" + user.getUserId() + ".jpeg");
            new File(String.valueOf(DefaultValues.defaultAvatar));

            try {
                UserFactory.getInstance().addUser(user, test);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getBaseContext(), "Utente registrato", Toast.LENGTH_LONG).show();
        };
        signUp.run();
    }

}
