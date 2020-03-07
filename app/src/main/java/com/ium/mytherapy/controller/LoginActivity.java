package com.ium.mytherapy.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.IOException;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText usernameInput, passwordText;
    MaterialButton loginButton, supervisorButton;
    TextInputLayout passwordInputLayout;
    int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordInputLayout = findViewById(R.id.password_input_toggle);
        usernameInput = findViewById(R.id.login_username);
        passwordText = findViewById(R.id.login_password);
        supervisorButton = findViewById(R.id.login_supervisore_button);
        loginButton = findViewById(R.id.login_button);

        /* Listen per tasto di redirezione a supervisor login */
        supervisorButton.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), SupervisorLoginActivity.class);
            startActivity(newActivity);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
            finish();
        });

        /* Listener tasto di login */
        loginButton.setOnClickListener(view -> {
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /* Serve per togliere il simbolo di errore quando si riprende a scrivere dopo un input sbagliato */
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /* Per ricambiare il simbolo di errore a occhio per mostrare la password una volta che si riprende a scrivere */
            @SuppressWarnings("deprecation")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordInputLayout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void login() throws IOException {
        if (!validate()) {
            onLoginFailed();    // chiamo se il login non va a buon fine
            return;
        }

        loginButton.setEnabled(false);  // disabilito tasto

        /* Mostro finestrella di caricamento giusto per scena */
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login in corso");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String password = Objects.requireNonNull(passwordText.getText()).toString();
        String username = Objects.requireNonNull(usernameInput.getText()).toString();

        User validation = UserFactory.getInstance().verifyUser(username, password); // verifico validità utente

        /* Se non è valido */
        if (validation == null) {
            Toast.makeText(getBaseContext(), "Dati non validi", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(
                    progressDialog::dismiss, 1000); // simulo un mini delay

            new MaterialAlertDialogBuilder(this)
                    .setTitle("LOGIN ERROR")
                    .setMessage("Username o password errati")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                    })
                    .show();
            loginButton.setEnabled(true);   // riabilito tasto login
            return;
        } else {
            userId = validation.getUserId();
        }

        new android.os.Handler().postDelayed(
                () -> {
                    onLoginSuccess();   // chiamo se login OK
//                    onLoginFailed();
                    progressDialog.dismiss();
                    Intent userLogin = new Intent(getApplicationContext(), UserHomeActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(DefaultValues.USER_TYPE, "user");
                    editor.putInt(DefaultValues.USER_ID, userId);
                    editor.apply();
                    finish();
                    startActivity(userLogin);
                }, 1000);   // simulo un mini delay

    }

    @Override
    public void onBackPressed() {
        // Non faccio tornare a MainActivity visto che c'è solo la splash
        moveTaskToBack(true);
    }

    /* Da chiamare se il login va a buon fine */
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
    }

    /* Da chiamare se il login non va a buon fine */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /* Controllo validità dei dati inseriti e trigger del segnale di errore */
    @SuppressWarnings("deprecation")
    public boolean validate() {
        boolean valid = true;

        String email = Objects.requireNonNull(usernameInput.getText()).toString();
        String password = Objects.requireNonNull(passwordText.getText()).toString();

        if (email.isEmpty()) {
            usernameInput.setError("Inserisci username valido");
            valid = false;
        } else {
            usernameInput.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordInputLayout.setPasswordVisibilityToggleEnabled(false);
            passwordText.setError("Inserisci password valida");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


}