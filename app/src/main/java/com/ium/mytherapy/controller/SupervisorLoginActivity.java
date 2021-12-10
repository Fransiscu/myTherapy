package com.ium.mytherapy.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Supervisor;
import com.ium.mytherapy.model.SupervisorFactory;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.IOException;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class SupervisorLoginActivity extends AppCompatActivity {

    TextInputEditText usernameInput, passwordText;
    TextInputLayout passwordInputLayout;
    TextView signup;
    MaterialButton loginButton;
    int supervisorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervisor_login_activity);

        signup = findViewById(R.id.signup);
        loginButton = findViewById(R.id.supervisor_login_button);
        usernameInput = findViewById(R.id.supervisor_login_username_input);
        passwordText = findViewById(R.id.supervisor_login_password_input);
        passwordInputLayout = findViewById(R.id.supervisor_password_input_toggle);

        /* login on click listener */
        loginButton.setOnClickListener(view -> {
            Log.d("loginSup", "click"); // ok
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /* signup on click listener */
        signup.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), SupervisorSignupActivity.class);
            startActivity(newActivity);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        });
    }

    public void login() throws IOException {
        if (!validate()) {
            onLoginFailed();    // on login failed
            return;
        }

        loginButton.setEnabled(false);  // disable key

        Log.d("loginSup", "disabilitato button");

        /* show fake loading */
        final ProgressDialog progressDialog = new ProgressDialog(SupervisorLoginActivity.this,
                R.style.AppCompatAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login in corso");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String password = Objects.requireNonNull(passwordText.getText()).toString();
        String username = Objects.requireNonNull(usernameInput.getText()).toString();

        Log.d("loginSup", "prima di validation");

        Supervisor validation = SupervisorFactory.getInstance().verifySueprvisor(username, password); // check supervisor legitimacy

        Log.d("loginSup", "dopo validation");

        if (validation == null) {   // if not valid
            Toast.makeText(getBaseContext(), "Dati non validi", Toast.LENGTH_LONG).show();
            usernameInput.setError("Username o password non corretti");
            passwordText.setError("Username o password non corretti");
            new android.os.Handler().postDelayed(
                    progressDialog::dismiss, 1000); // simulo un mini delay

            new MaterialAlertDialogBuilder(this)
                    .setTitle("LOGIN ERROR")
                    .setMessage("Username o password errati")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                    })
                    .show();
            loginButton.setEnabled(true);   // enable login key
            return;
        } else {
            supervisorId = validation.getSupervisorId();
        }

        Log.d("loginSup", "zero");

        new android.os.Handler().postDelayed(
                () -> {
                    Log.d("loginSup", "primo");
                    onLoginSuccess();   // if login successful
                    Log.d("loginSup", "secondo");
                    progressDialog.dismiss();
                    Intent supervisorHomeActivity = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                    supervisorHomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(DefaultValues.USER_TYPE, "supervisor");
                    editor.apply();
                    finish();
                    startActivity(supervisorHomeActivity);
                }, 1000);   // simulo un mini delay
    }

    /* Validate input and add errors */
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

    /* Method for on login success */
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
    }

    /* Method for on login fail */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login fallito", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /* Override on back pressed in order to change the animation */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginActivity = new Intent(getApplicationContext(), UserLoginActivity.class);
        startActivity(loginActivity);
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
        finish();
    }
}
