package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    View primo, secondo, terzo;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        primo = findViewById(R.id.primo_item);
        secondo = findViewById(R.id.secondo_item);
        terzo = findViewById(R.id.terzo_item);


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

        /* Listeners per medicine di esmepio */
        primo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primo.setSelected(true);    // coloro di grigio al tocco
                Intent therapy = new Intent(getApplicationContext(), MedicineDetailActivity.class);
                startActivity(therapy);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
                finish();
            }
        });

        secondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondo.setSelected(true);  // coloro di grigio al tocco
            }
        });

        terzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terzo.setSelected(true);    // coloro di grigio al tocco
            }
        });

    }

    public enum Mesi {Gennaio, Febbraio, Marzo, Aprile, Maggio, Giugno, Luglio, Agosto, Settembre, Ottobre, Novembre, Dicembre;}

    public enum Giorni {Domenica, Lunedì, Martedì, Mercoledì, Giovedì, Venerdì, Sabato;} //Sunday = primo della settimana per gli inglesi
}
