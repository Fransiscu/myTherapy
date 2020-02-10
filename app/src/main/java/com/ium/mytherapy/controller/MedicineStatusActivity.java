package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
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


        final Drawable greyDrawable = new ColorDrawable(getApplicationContext().getResources().getColor(R.color.statusBarColor));
        if (medicine.isPresa()) {
            confirm.setClickable(false);
            confirm.setFocusable(false);
            confirm.setFocusableInTouchMode(false);
            confirm.setBackgroundColor(getResources().getColor(R.color.statusBarColor));
            confirm.setForeground(greyDrawable);
            confirm.setBackgroundColor(R.color.statusBarColor);
            doneCardDrawable.setImageDrawable(getDrawable(R.drawable.timeline_done_grey));

            remindLater.setClickable(false);
            remindLater.setFocusable(false);
            remindLater.setFocusableInTouchMode(false);
            remindLater.setForeground(greyDrawable);
            remindLater.setBackgroundColor(getResources().getColor(R.color.statusBarColor));
            remindLater.setBackgroundColor(R.color.statusBarColor);
            remindCardDrawable.setImageDrawable(getDrawable(R.drawable.notification_grey));

            confirmText.setVisibility(View.VISIBLE);

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
}
