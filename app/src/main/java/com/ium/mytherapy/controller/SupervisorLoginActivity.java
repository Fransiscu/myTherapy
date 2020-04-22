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
        setContentView(R.layout.activity_login_supervisore);

        signup = findViewById(R.id.registrati);
        loginButton = findViewById(R.id.supervisore_login_button);
        usernameInput = findViewById(R.id.supervisore_login_username);
        passwordText = findViewById(R.id.supervisore_login_password);
        passwordInputLayout = findViewById(R.id.supervisore_password_input_toggle);

        /* Listener tasto login */
        loginButton.setOnClickListener(view -> {
            Log.d("loginSup", "click"); // ok
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        /* Listener tasto signup */
        signup.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), SupervisorSignupActivity.class);
            startActivity(newActivity);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        });
    }

    public void login() throws IOException {
        if (!validate()) {
            onLoginFailed();    // chiamo se il login non va a buon fine
            return;
        }

        loginButton.setEnabled(false);  // disabilito tasto

        Log.d("loginSup", "disabilitato button");

        /* Mostro finestrella di caricamento giusto per scena */
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

        Supervisor validation = SupervisorFactory.getInstance().verifySueprvisor(username, password); // verifico validità supervisore

        Log.d("loginSup", "dopo validation");

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
            supervisorId = validation.getSupervisorId();
        }

        Log.d("loginSup", "zero");

        new android.os.Handler().postDelayed(
                () -> {
                    Log.d("loginSup", "primo");
                    onLoginSuccess();   // chiamo se login OK
                    Log.d("loginSup", "secondo");
//                    onLoginFailed();
                    progressDialog.dismiss();
                    Intent supervisorHomeActivity = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                    supervisorHomeActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(DefaultValues.USER_TYPE, "supervisor"); // setto le sharedPreferences
                    editor.apply();
                    finish();
                    startActivity(supervisorHomeActivity);
                }, 1000);   // simulo un mini delay
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

    /* Da chiamare se il login va a buon fine */
    public void onLoginSuccess() {
        loginButton.setEnabled(true);
    }

    /* Da chiamare se il login non va a buon fine */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login fallito", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    /* Override pressione tasto back per cambiare l'animazione */
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
