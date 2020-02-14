package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditTherapyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText medicineName, medicineDetails, medicineStandardDosage, medicineLinks;
    MaterialSpinner spinnerNum, spinnerFreq;
    String[] itemsNumber = new String[]{"1", "2", "3"};
    String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", "Una tantum"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_terapia);

        medicineName = findViewById(R.id.add_edit_medicine_name);
        medicineDetails = findViewById(R.id.add_edit_medicine_details);
        medicineStandardDosage = findViewById(R.id.add_edit_medicine_dosage);
        medicineLinks = findViewById(R.id.link_utili);

        spinnerNum = findViewById(R.id.spinner_quantita);
        spinnerFreq = findViewById(R.id.spinner_freq);

        spinnerNum.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(adapterInt);
        spinnerFreq.setAdapter(adapterString);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent userManagementHome = new Intent(getApplicationContext(), UserManagementActivity.class);
        userManagementHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(userManagementHome);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
