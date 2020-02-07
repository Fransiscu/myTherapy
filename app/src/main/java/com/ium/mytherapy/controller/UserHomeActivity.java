package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class UserHomeActivity extends AppCompatActivity {

    MaterialButton logout;
    TextView todaysDate;
    View primo;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        /* Setto la data di oggi */
        todaysDate = findViewById(R.id.data_oggi);
        Date date = new Date();
        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            Log.d("data", dayOfWeek + " " + year + " " + month + " " + day);
            todaysDate.setText(String.format("%s %d %s %d", Giorni.values()[dayOfWeek - 1], day, Mesi.values()[month - 1].toString(), year));
        }

        /* Setto listener per pulsante di logout */
        logout = findViewById(R.id.user_logout_button);
        logout.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("LOGOUT")
                .setMessage("Sei sicuro di voler fare il logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    Intent backToHome = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(backToHome);
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());


        primo = findViewById(R.id.primo_item);
        primo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Primo item", Toast.LENGTH_LONG).show();
            }
        });

    }

    public enum Mesi {Gennaio, Febbraio, Marzo, Aprile, Maggio, Giugno, Luglio, Agosto, Settembre, Ottobre, Novembre, Dicembre;}

    public enum Giorni {Domenica, Lunedì, Martedì, Mercoledì, Giovedì, Venerdì, Sabato;} //Sunday = primo della settimana per gli inglesi
}
