package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MedicineDetailsActivity extends AppCompatActivity {

    TextView pageTitle, medicineDetails, medicineDosage, medicineReccomandation, medicineLinks;
    MaterialSpinner spinnerNum, spinnerFreq;
    Medicina medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_medicina);

        /* Valori degli spinners */
        String[] itemsNumber = new String[]{"1", "2", "3"};
        String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", ""};

        pageTitle = findViewById(R.id.dettagli_nome_medicina);
        medicineDetails = findViewById(R.id.dettagli_medicina);
        medicineDosage = findViewById(R.id.assunzione_e_dosaggio);
        medicineReccomandation = findViewById(R.id.consigli_e_raccomandazioni);
        medicineLinks = findViewById(R.id.link_utili);
        spinnerNum = findViewById(R.id.spinnerInt);
        spinnerFreq = findViewById(R.id.spinnerString);

        /* Prendo la medicina in input e setto i vari campi dell'activity */
        Intent medicineIntent = getIntent();
        if (medicineIntent != null) {
            Bundle bundle = medicineIntent.getExtras();
            if (bundle != null) {
                medicine = bundle.getParcelable(UserHomeActivity.MEDICINA);
                if (medicine != null) {
                    pageTitle.setText(medicine.getNome());
                    medicineDetails.setText(medicine.getDescrizione());
                    medicineDosage.setText(medicine.getDosaggio());
                    medicineReccomandation.setText(medicine.getConsigliSupervisore());
                    medicineLinks.setText(medicine.getLink());
                    /* Setto spinners con dettagli vari */
                    ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
                    ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
                    spinnerNum.setAdapter(adapterInt);
                    spinnerFreq.setAdapter(adapterString);
                    spinnerNum.setEnabled(false);
                    spinnerFreq.setEnabled(false);
                    /* Setto valore iniziale */
                    spinnerNum.setSelection(medicine.getFrequenzaNum() - 1);

                    switch (medicine.getFrequenza()) {
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
