package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
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
    MaterialButton medicineDetailsButton;
    Medicina medicina;

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
        medicineDetailsButton = findViewById(R.id.dettagli_medicina_button);

        /* Recupero intent della medicina e setto i vari campi */
        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicina = bundle.getParcelable(UserHomeActivity.MEDICINA);
                if (medicina != null) {
                    medicineName.setText(Objects.requireNonNull(medicina).getNome().toUpperCase());
                    medicineHour.setText(medicina.getOra());
                    medicineDetails.setText(medicina.getConsigliSupervisore());
                }
            }
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
        remindLater.setOnClickListener(view -> {
            /* Apro datePicker per selezionare l'ora della notifica */
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MedicineStatusActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    /* Alla selezione del tempo faccio comparire una finestra di conferma + toast */
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
            mTimePicker.setMessage("Seleziona l'orario");
                mTimePicker.show();
        });

        /* Listener tasto dettagli medicina */
        medicineDetailsButton.setOnClickListener(view -> {
            Intent medicineDetailsActivity = new Intent(getApplicationContext(), MedicineDetailsActivity.class);
            medicineDetailsActivity.putExtra(UserHomeActivity.MEDICINA, medicina);
            startActivity(medicineDetailsActivity);
        });

        /* Preimposto i tasti se è già presa */
        if (medicina.isPresa()) {
            setPresa();
        }

    }

    /* Override pressione tasto back per cambiare l'animazione */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent userHome = new Intent(getApplicationContext(), UserHomeActivity.class);
        startActivity(userHome);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    /* Tasto per confermare medicina presa + cambio colore dei tasti ad un grigiastro */
    public void setPresa() {
        final Drawable greyDrawable = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent));
        confirm.setClickable(false);
        confirm.setFocusable(false);
        confirm.setForeground(greyDrawable);
        confirm.setBackgroundColor(getResources().getColor(R.color.white));
        doneCardDrawable.setImageDrawable(getDrawable(R.drawable.timeline_done_grey));  // non funziona come vorrei, il colore viene sbiadito e non cambia completamente

        reminderCardTitle.setText("Rimanda");   // in caso abbia prima premuto su rimanda lo risetto normale
        remindLater.setClickable(false);
        remindLater.setFocusable(false);
        remindLater.setForeground(greyDrawable);
        remindLater.setBackgroundColor(getResources().getColor(R.color.white));
        remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey)); // non funziona come vorrei, il colore viene sbiadito e non cambia completamente

        confirmText.setVisibility(View.VISIBLE);
    }

    /* Rendo non clickabile il tasto per rimandare la notifica e aggiungo l'orario appena scelto dall'utente */
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
