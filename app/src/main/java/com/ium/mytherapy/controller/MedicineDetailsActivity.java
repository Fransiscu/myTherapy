package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineDetailsActivity extends AppCompatActivity {

    TextView pageTitle, medicineDetails, medicineDosage, medicineReccomandation, medicineLinks;
    Spinner spinnerInt, spinnerString;
    Medicina medicine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_medicina);

        String[] itemsNumber = new String[]{"1", "2", "3"};
        String[] itemsString = new String[]{"Giorno", "Settimana", "Mese"};

        pageTitle = findViewById(R.id.dettagli_nome_medicina);
        medicineDetails = findViewById(R.id.dettagli_medicina);
        medicineDosage = findViewById(R.id.assunzione_e_dosaggio);
        medicineReccomandation = findViewById(R.id.consigli_e_raccomandazioni);
        medicineLinks = findViewById(R.id.link_utili);
        spinnerInt = findViewById(R.id.spinnerInt);
        spinnerString = findViewById(R.id.spinnerString);

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
                    spinnerInt.setAdapter(adapterInt);
                    spinnerString.setAdapter(adapterString);
                    spinnerInt.setEnabled(false);
                    spinnerString.setEnabled(false);
                    /* Setto valore iniziale */
                    spinnerInt.setSelection(medicine.getFrequenzaNum() - 1);

                    switch (medicine.getFrequenza()) {
                        case "Giorno":
                            spinnerString.setSelection(0);
                            break;
                        case "Settimana":
                            spinnerString.setSelection(1);
                            break;
                        case "Mese":
                            spinnerString.setSelection(2);
                            break;
                        case "Anno":
                            spinnerString.setSelection(3);
                            break;
                        default:
                            break;
                    }

                }
            }

        }
    }
}
