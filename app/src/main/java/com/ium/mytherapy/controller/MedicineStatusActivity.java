package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineStatusActivity extends AppCompatActivity {

    TextView medicineName, medicineHour, confirmText;
    MaterialCardView confirm, remindLater;
    Medicina medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stato_terapia);

        medicineName = findViewById(R.id.dettagli_nome_medicina);
        medicineHour = findViewById(R.id.orario_sotto_titolo_dettagli);
        confirm = findViewById(R.id.carta_conferma_pagina_dettagli);
        remindLater = findViewById(R.id.carta_rimanda_pagina_dettagli);
        confirmText = findViewById(R.id.scritta_presa);

        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable("medicine");
            }
            medicineName.setText(Objects.requireNonNull(medicine).getNome().toUpperCase());
            medicineHour.setText(medicine.getOra());
            Log.d("presa", String.valueOf(medicine.isPresa()));
        }

        if (medicine.isPresa()) {
            confirm.setClickable(false);
            confirm.setFocusable(false);
            confirm.setFocusableInTouchMode(false);
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
