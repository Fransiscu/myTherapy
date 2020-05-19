package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
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
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditMedicineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText medicineName, medicineDetails, medicineStandardDosage, medicineLinks, medicineHour, medicineTips;
    MaterialSpinner spinnerNum, spinnerFreq;
    MaterialCheckBox notifCheckbox;
    Medicina currentTherapy, newTherapy;
    MaterialDayPicker materialDayPicker;
    MaterialButton saveEdits;
    String[] itemsNumber = new String[]{"1", "2", "3"};
    String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", "Una tantum"};
    int userId;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_medicina);

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

        /* Week day picker */
        materialDayPicker = findViewById(R.id.day_picker);
        materialDayPicker.setLocale(Locale.ITALIAN);

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
                User user = bundle.getParcelable("user");
                userId = Objects.requireNonNull(user).getUserId();
            }
        }

        /* Listener per campo orario */
        medicineHour.setOnClickListener(view -> {
            /* Apro datePicker per selezionare l'ora della notifica */
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            /* Alla selezione del tempo faccio comparire una finestra di conferma + toast */
            mTimePicker = new TimePickerDialog(EditMedicineActivity.this, (timePicker, selectedHour, selectedMinute) -> medicineHour.setText(String.format("%d:%d", selectedHour, selectedMinute)), hour, minute, true);//Yes 24 hour time
            mTimePicker.setMessage("Seleziona l'orario");
            mTimePicker.show();
        });

        /* Listener tasto per confermare l'aggiunta della terapia */
        saveEdits.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("MODIFICA TERAPIA")
                .setMessage("Stai per modificare la terapia, sicuro di voler procedere?")
                .setCancelable(false)
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
                    boolean succeded = false;
                    try {
                        succeded = editTherapy(newTherapy);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (succeded) {
                        Toast.makeText(getBaseContext(), "Terapia modificata", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Errore durante la modifica della terapia", Toast.LENGTH_LONG).show();
                    }
                    Intent backToManagement = new Intent(this, UserManagementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(DefaultValues.USER_KEY, userId);
                    try {
                        bundle.putParcelable(DefaultValues.USER_INTENT, UserFactory.getInstance().getUser(userId));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    backToManagement.putExtras(bundle);
                    startActivity(backToManagement);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

    }

    private boolean editTherapy(Medicina newTherapy) throws IOException {
        newTherapy.setNome(String.valueOf(medicineName.getText()));
        newTherapy.setDescrizione(String.valueOf(medicineDetails.getText()));
        newTherapy.setDosaggio(String.valueOf(medicineStandardDosage.getText()));
        newTherapy.setLink(String.valueOf(medicineLinks.getText()));
        newTherapy.setOra(String.valueOf(medicineHour.getText()));
        newTherapy.setConsigliSupervisore(String.valueOf(medicineTips.getText()));
        newTherapy.setPresa(currentTherapy.isPresa());
        newTherapy.setNotifEnabled(notifCheckbox.isChecked());

        /* Sostituire primo campo con user */
        return MedicinaFactory.getInstance().deleteMedicineFromCode(UserFactory.getInstance().getUser(userId), newTherapy);
    }

    /* Override pressione tasto back per cambiare l'animazione e tornare indietro alla schermata corretta */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putInt(DefaultValues.USER_KEY, userId);
        try {
            bundle.putParcelable(DefaultValues.USER_INTENT, UserFactory.getInstance().getUser(userId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent backToManagement = new Intent(this, UserManagementActivity.class);
        bundle.putInt(DefaultValues.USER_KEY, userId);
        backToManagement.putExtras(bundle);
        startActivity(backToManagement);
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
