package com.ium.mytherapy.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;

import java.io.IOException;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText usernameInput, passwordText;
    MaterialButton loginButton, supervisorButton;
    TextInputLayout passwordInputLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        passwordInputLayout = findViewById(R.id.password_input_toggle);
        usernameInput = findViewById(R.id.login_username);
        passwordText = findViewById(R.id.login_password);
        supervisorButton = findViewById(R.id.login_supervisore_button);
        loginButton = findViewById(R.id.login_button);

        supervisorButton.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), SupervisorLoginActivity.class);
            startActivity(newActivity);
        });

        loginButton.setOnClickListener(view -> {
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login in corso");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String password = Objects.requireNonNull(passwordText.getText()).toString();
        String username = Objects.requireNonNull(usernameInput.getText()).toString();

        User validation = UserFactory.getInstance().verifyUser(username, password);

        if (validation == null) {
            Toast.makeText(getBaseContext(), "Dati non validi", Toast.LENGTH_LONG).show(); //TODO: aggiungi finestrella migliore
        } else {
            Toast.makeText(getBaseContext(), "OK!", Toast.LENGTH_LONG).show(); //TODO: aggiungi finestrella migliore
        }

        new android.os.Handler().postDelayed(
                () -> {
                    onLoginSuccess();
//                    onLoginFailed();
                    progressDialog.dismiss();
                }, 2000);

//        Intent userLogin = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("USER", validation);
//        userLogin.putExtras(bundle);
//        startActivity(userLogin);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
//        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

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