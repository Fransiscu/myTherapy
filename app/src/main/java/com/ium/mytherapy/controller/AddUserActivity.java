package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Supervisor;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {

    TextInputEditText nameInput, surnameInput, dateInput, emailInput, usernameInput, passwordInput;
    TextInputLayout passwordInputLayout;
    MaterialButton addUserButton;
    private int mYear, mMonth, mDay;
    File path = Environment.getExternalStorageDirectory();
    File dir = new File(path.getAbsolutePath() + "/myTherapy/");

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_utente);

        nameInput = findViewById(R.id.user_name);
        surnameInput = findViewById(R.id.user_surname);
        dateInput = findViewById(R.id.user_data);
        emailInput = findViewById(R.id.user_email);
        usernameInput = findViewById(R.id.user_username);
        passwordInput = findViewById(R.id.user_password);
        addUserButton = findViewById(R.id.conferma_aggiunta_utente_button);

        passwordInputLayout = findViewById(R.id.user_password_toggle);

        /* Calendario al tocco del campo data */
        dateInput.setShowSoftInputOnFocus(false);
        dateInput.setInputType(InputType.TYPE_NULL);
        dateInput.setFocusable(false);

        /* Resetto errore per birthdate*/
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

        /* Popup calendario al tocco del campo */
        dateInput.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> dateInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        /* Aggiunta utente */
        addUserButton.setOnClickListener(view -> {
            boolean valid = true;
            String name = Objects.requireNonNull(nameInput.getText()).toString();
            String surname = Objects.requireNonNull(surnameInput.getText()).toString();
            String email = Objects.requireNonNull(emailInput.getText()).toString();
            String username = Objects.requireNonNull(usernameInput.getText()).toString();
            String password = Objects.requireNonNull(passwordInput.getText()).toString();
            String birthdate = Objects.requireNonNull(dateInput.getText()).toString();

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
                passwordInput.setError("Inserisci password valida");
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

            if (valid) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Conferma")
                        .setMessage("Aggiungere il nuovo account?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            addUser();
                            Intent newActivity = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                            startActivity(newActivity);
                            finish();
                        })
                        .setNegativeButton("Annulla", (dialogInterface, i) -> {
                        })
                        .show();
            }

        });


    }

    private void addUser() {
        Runnable signUp = () -> {
            User user = new User();
            Supervisor test = new Supervisor(); //TODO: aggiorna con vero valore di supervisore
            test.setSupervisorId(0);
            int max = 0;
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myTherapy/users/");

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
                user.setUserId(max);
            } else {
                user.setUserId(0);
            }

            user.setNome(Objects.requireNonNull(nameInput.getText()).toString());
            user.setCognome(Objects.requireNonNull(surnameInput.getText()).toString());
            user.setDataNascita(Objects.requireNonNull(dateInput.getText()).toString());
            user.setEmail(Objects.requireNonNull(emailInput.getText()).toString());
            user.setUsername(Objects.requireNonNull(usernameInput.getText()).toString());
            user.setPassword(Objects.requireNonNull(passwordInput.getText()).toString());

            File file = new File(dir + "/avatar_" + user.getUserId() + ".jpeg");
            File defaultAvatar = new File(dir + "/default.jpg");

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
