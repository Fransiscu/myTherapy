package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddTherapyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText medicineName, medicineDetails, medicineStandardDosage, medicineLinks;
    MaterialSpinner spinnerNum, spinnerFreq;
    MaterialButton addTherapy;
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

        addTherapy = findViewById(R.id.add_therapy_button);

        spinnerNum.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(adapterInt);
        spinnerFreq.setAdapter(adapterString);

        /* Listener tasto per confermare l'aggiunta della terapia */
        addTherapy.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("AGGIUNTA TERAPIA")
                .setMessage("Stai per aggiungere la terapia, sicuro di voler procedere?")
                .setCancelable(false)
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
                    Intent backToManagement = new Intent(this, UserManagementActivity.class);
                    startActivity(backToManagement);
                    Toast.makeText(getBaseContext(), "Terapia aggiunta", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

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
        // niente, è solo per scena
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // niente, è solo per scena
    }
}
