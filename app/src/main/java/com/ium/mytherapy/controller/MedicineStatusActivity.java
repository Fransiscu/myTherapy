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
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.utils.Utility;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class MedicineStatusActivity extends AppCompatActivity {

    TextView medicineName, medicineHour, confirmText, medicineDetails, reminderCardTitle, doneCardTitle;
    ImageView doneCardDrawable, remindCardDrawable;
    MaterialCardView confirm, remindLater;
    MaterialButton medicineDetailsButton;
    Medicine medicine;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_status_activity);

        medicineName = findViewById(R.id.dettagli_nome_medicina);
        medicineHour = findViewById(R.id.orario_sotto_titolo_dettagli);
        medicineDetails = findViewById(R.id.appunti_medicina_da_supervisore);
        confirm = findViewById(R.id.carta_conferma_pagina_dettagli);
        remindLater = findViewById(R.id.carta_rimanda_pagina_dettagli);
        confirmText = findViewById(R.id.scritta_presa);
        doneCardDrawable = findViewById(R.id.stato_terapia_done);
        remindCardDrawable = findViewById(R.id.stato_terapia_notifica);
        reminderCardTitle = findViewById(R.id.titolo_carta_non_preso);
        doneCardTitle = findViewById(R.id.titolo_carta_preso);
        medicineDetailsButton = findViewById(R.id.dettagli_medicina_button);

        /* Getting medicine intent and filling the different fields */
        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable(DefaultValues.MEDICINA);
                if (medicine != null) {
                    medicineName.setText(Objects.requireNonNull(medicine).getName().toUpperCase());
                    medicineHour.setText(medicine.getTimeHour());
                    medicineDetails.setText(medicine.getSupervisorTips());
                }
            }
        }

        /* confirm button on click listener */
        confirm.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("Conferma")
                .setMessage("Sicuro di voler segnare la medicina come \"presa\"?")
                .setCancelable(false)
                .setPositiveButton("Continua", (dialogInterface, i) -> {
                    try {
                        setPresa(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(), "Salvato", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        /* remindLater notification on click listener */
        remindLater.setOnClickListener(view -> {
                Calendar mcurrentTime = Calendar.getInstance(); // opening datepicker
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
                            try {
                                setDelayed(true, selectedHour, selectedMinute);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

        /* medicineDetails on click listener */
        medicineDetailsButton.setOnClickListener(view -> {
            Intent medicineDetailsActivity = new Intent(getApplicationContext(), MedicineDetailsActivity.class);
            medicineDetailsActivity.putExtra(DefaultValues.MEDICINA, medicine);
            startActivity(medicineDetailsActivity);
        });

        /* Prefilling data if already taken */
        if (medicine.isTaken()) {
            try {
                setPresa(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setNonPresa();
        }

        if (medicine.isDelayed()) {
            try {
                setDelayed(false, 0, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Reminder activation button */
    private void setDelayed(boolean changed, int selectedHour, int selectedMinute) throws IOException {

        if (changed) {
            greyOutReminder(selectedHour, selectedMinute);
            String timestamp = selectedHour + ":" + selectedMinute;
            medicine.setDelayed(true);          // saving new notification time
            medicine.setReminder(timestamp);
            MedicineFactory.getInstance().setReminder(medicine, UserFactory.getInstance().getUser(Utility.getUserIdFromSharedPreferences(getApplicationContext())));
            Toast.makeText(getBaseContext(), "Notifica impostata per le " + timestamp, Toast.LENGTH_LONG).show();
        } else if (medicine.getReminder() == "none") {
            // do nothing
        } else {
            String[] strings = medicine.getReminder().split(":");
            greyOutReminder(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
        }

    }

    /* If medifine not taken, sett colors */
    private void setNonPresa() {
        final Drawable redBackground = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.lightRedBackground));
        final Drawable greenBackground = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.lightGreenBackground));

        confirm.setBackground(greenBackground);
        remindLater.setBackground(redBackground);
    }

    /* Medicine confirmation button and color changing after */
    public void setPresa(boolean changed) throws IOException {
        final Drawable colorAccentDrawable = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent));

        if (changed) {
            medicine.setTaken(true);
            MedicineFactory.getInstance().changeTaken(medicine, UserFactory.getInstance().getUser(Utility.getUserIdFromSharedPreferences(getApplicationContext())));
        }

        doneCardTitle.setText("Presa");
        doneCardTitle.setTextColor(getResources().getColor(R.color.greyText));
        confirm.setClickable(false);
        confirm.setFocusable(false);
        confirm.setStrokeWidth(0);
        confirm.setBackgroundColor(getResources().getColor(R.color.medicineTaken));
        doneCardDrawable.setImageDrawable(getDrawable(R.drawable.timeline_done_grey));

        reminderCardTitle.setText("Non rimandabile");   // if notification postponed
        reminderCardTitle.setTextColor(getResources().getColor(R.color.greyText));
        remindLater.setClickable(false);
        remindLater.setFocusable(false);
        remindLater.setStrokeWidth(0);
        remindLater.setBackgroundColor(getResources().getColor(R.color.lightRedBackground));
        remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey));

        confirmText.setVisibility(View.VISIBLE);
    }

    /* Override on back pressed in order to change the animation */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent userHome = new Intent(getApplicationContext(), UserHomeActivity.class);
        startActivity(userHome);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    /* Making postpone button not clickable - adding new time chosen by user */
    public void greyOutReminder(int hour, int minute) {
        final Drawable colorAccentDrawable = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorAccent));
        remindLater.setClickable(false);
        remindLater.setFocusable(false);
        remindLater.setForeground(colorAccentDrawable);
        remindLater.setBackgroundColor(getResources().getColor(R.color.white));
        remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey));
        reminderCardTitle.setText("Rimandato alle " + hour + ":" + minute);
    }
}
