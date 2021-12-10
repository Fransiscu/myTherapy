package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.utils.DefaultValues;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MedicineDetailsActivity extends AppCompatActivity {

    TextView pageTitle, medicineDetails, medicineDosage, medicineReccomandation, medicineLinks;
    MaterialSpinner spinnerNum, spinnerFreq;
    Medicine medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_details_activity);

        /* Spinners' values */
        String[] itemsNumber = new String[]{"1", "2", "3"};
        String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", ""};

        pageTitle = findViewById(R.id.dettagli_nome_medicina);
        medicineDetails = findViewById(R.id.dettagli_medicina);
        medicineDosage = findViewById(R.id.assunzione_e_dosaggio);
        medicineReccomandation = findViewById(R.id.consigli_e_raccomandazioni);
        medicineLinks = findViewById(R.id.link_utili);
        spinnerNum = findViewById(R.id.spinnerInt);
        spinnerFreq = findViewById(R.id.spinnerString);

        /* Getting a medicine as input and filling the different fields */
        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable(DefaultValues.MEDICINA);
                if (medicine != null) {
                    pageTitle.setText(medicine.getName());
                    medicineDetails.setText(medicine.getDescription());
                    medicineDosage.setText(medicine.getDosage());
                    medicineReccomandation.setText(medicine.getSupervisorTips());
                    medicineLinks.setText(medicine.getLink());
                    /* Setting spinners' values */
                    ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
                    ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
                    spinnerNum.setAdapter(adapterInt);
                    spinnerFreq.setAdapter(adapterString);
                    spinnerNum.setEnabled(false);
                    spinnerFreq.setEnabled(false);
                    spinnerNum.setSelection(medicine.getFrequencyNumber() - 1);    // initial value

                    switch (medicine.getFrequency()) {
                        case "Giorno":
                            spinnerFreq.setSelection(0);
                            break;
                        case "Settimana":
                            spinnerFreq.setSelection(1);
                            break;
                        case "Mese":
                            spinnerFreq.setSelection(2);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
