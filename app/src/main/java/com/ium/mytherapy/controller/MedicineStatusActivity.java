package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineStatusActivity extends AppCompatActivity {

    Medicina medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stato_terapia);

        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            medicine = Objects.requireNonNull(medicineIntent.getExtras()).getParcelable("medicine");
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
