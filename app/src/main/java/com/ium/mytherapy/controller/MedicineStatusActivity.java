package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("ALL")
public class MedicineStatusActivity extends AppCompatActivity {

    TextView medicineName, medicineHour, confirmText;
    ImageView doneCardDrawable, remindCardDrawable;
    MaterialCardView confirm, remindLater;
    Medicina medicine;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stato_terapia);

        medicineName = findViewById(R.id.dettagli_nome_medicina);
        medicineHour = findViewById(R.id.orario_sotto_titolo_dettagli);
        confirm = findViewById(R.id.carta_conferma_pagina_dettagli);
        remindLater = findViewById(R.id.carta_rimanda_pagina_dettagli);
        confirmText = findViewById(R.id.scritta_presa);
        doneCardDrawable = findViewById(R.id.stato_terapia_done);
        remindCardDrawable = findViewById(R.id.stato_terapia_notifica);

        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable(UserHomeActivity.MEDICINA);
            }
            medicineName.setText(Objects.requireNonNull(medicine).getNome().toUpperCase());
            medicineHour.setText(medicine.getOra());
        }


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

        remindLater.setClickable(false);
        remindLater.setFocusable(false);
        remindLater.setForeground(greyDrawable);
        remindLater.setBackgroundColor(getResources().getColor(R.color.white));
        remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey));

        confirmText.setVisibility(View.VISIBLE);
    }
}
