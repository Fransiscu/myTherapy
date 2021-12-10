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

public class UserLoginActivity extends AppCompatActivity {

    TextInputEditText usernameInput, passwordText;
    MaterialButton loginButton, supervisorButton;
    TextInputLayout passwordInputLayout;

    int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_activity);

        passwordInputLayout = findViewById(R.id.password_input_toggle);
        usernameInput = findViewById(R.id.login_username);
        passwordText = findViewById(R.id.login_password);
        supervisorButton = findViewById(R.id.supervisor_login_page_button);
        loginButton = findViewById(R.id.login_button);

        /* supervisorLogin button on click listener */
        supervisorButton.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), SupervisorLoginActivity.class);
            startActivity(newActivity);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
            finish();
        });

        /* login key on click listener */
        loginButton.setOnClickListener(view -> {
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /* Removing error icon after fixing an error */
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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
            onLoginFailed();    // on login failed
            return;
        }

        loginButton.setEnabled(false);  // disabling button

        /* Showing loading fragment */
        final ProgressDialog progressDialog = new ProgressDialog(UserLoginActivity.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login in corso");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String password = Objects.requireNonNull(passwordText.getText()).toString();
        String username = Objects.requireNonNull(usernameInput.getText()).toString();

        User validation = UserFactory.getInstance().verifyUser(username, password); // validating user

        if (validation == null) {   // if not valid
            Toast.makeText(getBaseContext(), "Dati non validi", Toast.LENGTH_LONG).show();
            usernameInput.setError("Username o password non corretti");
            passwordText.setError("Username o password non corretti");
            new android.os.Handler().postDelayed(
                    progressDialog::dismiss, 1000); // fake a delay

            new MaterialAlertDialogBuilder(this)
                    .setTitle("LOGIN ERROR")
                    .setMessage("Username o password errati")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                    })
                    .show();
            loginButton.setEnabled(true);   // re enable login button
            return;
        } else {
            userId = validation.getUserId();
        }

        new android.os.Handler().postDelayed(
                () -> {
                    onLoginSuccess();   // on login successo
                    progressDialog.dismiss();
                    Intent userLogin = new Intent(getApplicationContext(), UserHomeActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(DefaultValues.USER_TYPE, "user");
                    editor.putInt(DefaultValues.USER_ID, userId);
                    editor.apply();
                    finish();
                    startActivity(userLogin);
                }, 1000);   // fake a delay

    }

    @Override
    public void onBackPressed() {
        // can't go back to main activity since it's just a splash
        moveTaskToBack(true);
    }

    /* on login success */
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
    }

    /* on login fail */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login fallito", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /* Check data input */
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