package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineDetailsActivity extends AppCompatActivity {

    TextView pageTitle;
    Medicina medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_medicina);

        pageTitle = findViewById(R.id.dettagli_nome_medicina);

        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable(UserHomeActivity.MEDICINA);
                if (medicine != null) {
                    pageTitle.setText(medicine.getNome());
                }
            }

        }
    }
}
