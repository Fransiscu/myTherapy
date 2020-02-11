package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class MedicineStatusActivity extends AppCompatActivity {

    TextView medicineName, medicineHour, confirmText, medicineDetails, reminderCardTitle;
    ImageView doneCardDrawable, remindCardDrawable;
    MaterialCardView confirm, remindLater;
    LinearLayout home;
    Medicina medicine;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stato_terapia);

        medicineName = findViewById(R.id.dettagli_nome_medicina);
        medicineHour = findViewById(R.id.orario_sotto_titolo_dettagli);
        medicineDetails = findViewById(R.id.appunti_medicina_da_supervisore);
        confirm = findViewById(R.id.carta_conferma_pagina_dettagli);
        remindLater = findViewById(R.id.carta_rimanda_pagina_dettagli);
        confirmText = findViewById(R.id.scritta_presa);
        doneCardDrawable = findViewById(R.id.stato_terapia_done);
        remindCardDrawable = findViewById(R.id.stato_terapia_notifica);
        reminderCardTitle = findViewById(R.id.titolo_carta_non_preso);

        home = findViewById(R.id.home_user_view);

        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable(UserHomeActivity.MEDICINA);
            }
            medicineName.setText(Objects.requireNonNull(medicine).getNome().toUpperCase());
            medicineHour.setText(medicine.getOra());
            medicineDetails.setText(medicine.getConsigliSupervisore());
        }

        /* Listener tasto conferma medicina persa */
        confirm.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("Conferma")
                .setMessage("Sicuro di voler segnare la medicina come \"presa\"?")
                .setCancelable(false)
                .setPositiveButton("Continua", (dialogInterface, i) -> {
                    setPresa();
                    Toast.makeText(getBaseContext(), "Salvato", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        /* Listener tasto rimanda notifica */
        remindLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MedicineStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        MaterialAlertDialogBuilder confirmTime = new MaterialAlertDialogBuilder(MedicineStatusActivity.this);
                        confirmTime.setTitle("Conferma orario");
                        confirmTime.setMessage("Impostare la notifica per le " + selectedHour + ":" + selectedMinute + "?");
                        confirmTime.setCancelable(false);
                        confirmTime.setPositiveButton("Ok", ((dialogInterface, i) -> {
                            greyOutReminder(selectedHour, selectedMinute);
                            Toast.makeText(getBaseContext(), "Notifica impostata per le " + selectedHour + ":" + selectedMinute, Toast.LENGTH_LONG).show();
                        }));
                        confirmTime.setNegativeButton("Annulla", ((dialogInterface, i) -> {
                            return;
                        }));
                        confirmTime.show();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        if (medicine.isPresa()) {
            setPresa();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent userHome = new Intent(getApplicationContext(), UserHomeActivity.class);
        startActivity(userHome);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    public void setPresa() {
        final Drawable greyDrawable = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent));
        confirm.setClickable(false);
        confirm.setFocusable(false);
        confirm.setForeground(greyDrawable);
        confirm.setBackgroundColor(getResources().getColor(R.color.white));
        doneCardDrawable.setImageDrawable(getDrawable(R.drawable.timeline_done_grey));

        reminderCardTitle.setText("Rimanda");   // in caso abbia prima premuto su rimanda lo risetto normale
        remindLater.setClickable(false);
        remindLater.setFocusable(false);
        remindLater.setForeground(greyDrawable);
        remindLater.setBackgroundColor(getResources().getColor(R.color.white));
        remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey));

        confirmText.setVisibility(View.VISIBLE);
    }

    public void greyOutReminder(int hour, int minute) {
        final Drawable greyDrawable = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent));
        remindLater.setClickable(false);
        remindLater.setFocusable(false);
        remindLater.setForeground(greyDrawable);
        remindLater.setBackgroundColor(getResources().getColor(R.color.white));
        remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey));
        reminderCardTitle.setText("Rimandato alle " + hour + ":" + minute);
    }

}
