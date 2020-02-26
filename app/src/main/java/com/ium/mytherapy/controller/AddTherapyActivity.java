package com.ium.mytherapy.controller;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.ium.mytherapy.model.UserFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import androidx.appcompat.app.AppCompatActivity;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddTherapyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static String SHARED_PREFS = "com.ium.mytherapy.controller";
    public static SharedPreferences mPreferences;
    MaterialSpinner spinnerNum, spinnerFreq;
    MaterialButton addTherapy;
    String[] itemsNumber = new String[]{"1", "2", "3"};
    String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", "Una tantum"};
    public static String sharedPrefFile = SHARED_PREFS;
    TextInputEditText medicineName, medicineDetails, medicineStandardDosage, medicineLinks, medicineTips;
    MaterialCheckBox checkbox;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiunta_terapia);
        AtomicReference<Medicina> medicine = new AtomicReference<>(new Medicina());

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        userId = mPreferences.getInt(MainActivity.USER_ID, 0);

        /* TextInputEditText */
        medicineName = findViewById(R.id.add_edit_medicine_name);
        medicineDetails = findViewById(R.id.add_edit_medicine_details);
        medicineStandardDosage = findViewById(R.id.add_edit_medicine_dosage);
        medicineLinks = findViewById(R.id.link_utili);
        medicineTips = findViewById(R.id.consigli_paziente);

        /* Spinners */
        spinnerNum = findViewById(R.id.spinner_quantita);
        spinnerFreq = findViewById(R.id.spinner_freq);
        spinnerNum.setOnItemSelectedListener(this);

        /* Buttons */
        addTherapy = findViewById(R.id.add_therapy_button);

        /* Checkbox */
        checkbox = findViewById(R.id.checkbox_notifiche);

        /* Setto i valori degli spinners + adapters */
        ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(adapterInt);
        spinnerFreq.setAdapter(adapterString);

        addTherapy.setOnClickListener(view -> {
            if (checkInput()) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("AGGIUNTA TERAPIA")
                        .setMessage("Stai per aggiungere la terapia, sicuro di voler procedere?")
                        .setCancelable(false)
                        .setPositiveButton("Procedi", (dialogInterface, i) -> {
                            Intent backToManagement = new Intent(getApplicationContext(), UserManagementActivity.class);
                            medicine.set(getMedicine());
                            try {
                                MedicinaFactory.getInstance().addMedicine(UserFactory.getInstance().getUser(userId), medicine.get());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            startActivity(backToManagement);

                            Toast.makeText(getBaseContext(), "Terapia aggiunta", Toast.LENGTH_LONG).show();
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        })
                        .setNegativeButton("Annulla", (dialogInterface, i) -> {
                        })
                        .show();
            }
        });

    }

    private Medicina getMedicine() {
        Medicina medicine = new Medicina();
        medicine.setNome(Objects.requireNonNull(medicineName.getText()).toString());
        medicine.setDescrizione(Objects.requireNonNull(medicineDetails.getText()).toString());
        medicine.setDosaggio(Objects.requireNonNull(medicineStandardDosage.getText()).toString());
        medicine.setLink(Objects.requireNonNull(medicineLinks.getText()).toString());
        medicine.setConsigliSupervisore(Objects.requireNonNull(medicineTips.getText()).toString());
        medicine.setFrequenza(spinnerFreq.getSelectedItem().toString());
        medicine.setFrequenzaNum(Integer.parseInt(spinnerNum.getSelectedItem().toString()));
        medicine.setNotifEnabled(checkbox.isChecked());
        medicine.setPresa(false);
        return medicine;
    }

    /* Controllo che i valori inseriti siano validi */
    private boolean checkInput() {
        boolean valid = true;

        String medName = Objects.requireNonNull(medicineName.getText()).toString();
        String medDetails = Objects.requireNonNull(medicineDetails.getText()).toString();
        String medStandardDosage = Objects.requireNonNull(medicineStandardDosage.getText()).toString();

        if (medName.isEmpty()) {
            medicineName.setError("Inserisci una nome valido");
            valid = false;
        } else {
            medicineName.setError(null);
        }
        if (medDetails.isEmpty()) {
            medicineDetails.setError("Inserisci dei dettagli");
            valid = false;
        } else {
            medicineDetails.setError(null);
        }
        if (medStandardDosage.isEmpty()) {
            medicineStandardDosage.setError("Inserisci dosaggio");
            valid = false;
        } else {
            medicineStandardDosage.setError(null);
        }

        return valid;
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
        // niente, è solo per scena - da modificare in caso di tasti presenti sulle carte
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // niente, è solo per scena - da modificare in caso di tasti presenti sulle carte
    }
}
