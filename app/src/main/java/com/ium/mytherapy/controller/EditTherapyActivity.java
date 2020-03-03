package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditTherapyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText medicineName, medicineDetails, medicineStandardDosage, medicineLinks, medicineHour, medicineTips;
    MaterialSpinner spinnerNum, spinnerFreq;
    MaterialCheckBox notifCheckbox;
    Medicina currentTherapy, newTherapy;
    MaterialButton saveEdits;
    String[] itemsNumber = new String[]{"1", "2", "3"};
    String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", "Una tantum"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_terapia);

        medicineName = findViewById(R.id.add_edit_medicine_name);
        medicineDetails = findViewById(R.id.add_edit_medicine_details);
        medicineStandardDosage = findViewById(R.id.add_edit_medicine_dosage);
        medicineTips = findViewById(R.id.consigli_paziente);
        medicineLinks = findViewById(R.id.link_utili);
        medicineHour = findViewById(R.id.orario_medicina);

        spinnerNum = findViewById(R.id.spinner_quantita);
        spinnerFreq = findViewById(R.id.spinner_freq);
        spinnerNum.setOnItemSelectedListener(this);

        notifCheckbox = findViewById(R.id.checkbox_notifiche);

        saveEdits = findViewById(R.id.save_therapy_edits);

        /* Setto valori e adapter degli spinner */
        ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(adapterInt);
        spinnerFreq.setAdapter(adapterString);

        /* Intent per therapy */
        Intent therapyIntent = getIntent();
        if (therapyIntent != null) {
            Bundle bundle = therapyIntent.getExtras();
            if (bundle != null) {
                currentTherapy = bundle.getParcelable("MEDICINA");
                if (currentTherapy != null) {
                    medicineName.setText(currentTherapy.getNome());
                    medicineDetails.setText(currentTherapy.getDescrizione());
                    medicineStandardDosage.setText(currentTherapy.getDosaggio());
                    medicineLinks.setText(currentTherapy.getLink());
                    medicineHour.setText(currentTherapy.getOra());
                    medicineTips.setText(currentTherapy.getConsigliSupervisore());
                    notifCheckbox.setChecked(currentTherapy.isNotifEnabled()); // a quanto pare funziona
                    newTherapy = currentTherapy;    // creo una copia della terapia corrente e mi preparo a modificarla
                }
            }
        }

        /* Listener tasto per confermare l'aggiunta della terapia */
        saveEdits.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("MODIFICA TERAPIA")
                .setMessage("Stai per modificare la terapia, sicuro di voler procedere?")
                .setCancelable(false)
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
                    boolean succeded = editTherapy(newTherapy);
                    if (succeded) {
                        Toast.makeText(getBaseContext(), "Terapia modificata", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Errore durante la modifica della terapia", Toast.LENGTH_LONG).show();
                    }
                    Intent backToManagement = new Intent(this, UserManagementActivity.class);
                    startActivity(backToManagement);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

    }

    private boolean editTherapy(Medicina newTherapy) {
        newTherapy.setNome(String.valueOf(medicineName.getText()));
        newTherapy.setDescrizione(String.valueOf(medicineDetails.getText()));
        newTherapy.setDosaggio(String.valueOf(medicineStandardDosage.getText()));
        newTherapy.setLink(String.valueOf(medicineLinks.getText()));
        newTherapy.setOra(String.valueOf(medicineHour.getText()));
        newTherapy.setConsigliSupervisore(String.valueOf(medicineTips.getText()));
        newTherapy.setPresa(currentTherapy.isPresa());
        newTherapy.setNotifEnabled(notifCheckbox.isChecked());

        /* Sostituire primo campo con user */
        return MedicinaFactory.getInstance().deleteMedicineFromCode(null, newTherapy);
    }

    /* Override pressione tasto back per cambiare l'animazione */
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
        // non serve ancora
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // non serve ancora
    }
}
