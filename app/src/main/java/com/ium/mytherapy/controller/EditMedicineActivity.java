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
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
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
    MaterialDayPicker materialDayPicker;
    MaterialCheckBox notifCheckbox;
    MaterialButton saveEdits;

    String[] itemsNumber = new String[]{"1", "2", "3"};
    String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", "Una tantum"};

    Medicine currentTherapy, newTherapy;
    int userId;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_medicine_activity);

        medicineStandardDosage = findViewById(R.id.add_edit_medicine_dosage);
        medicineDetails = findViewById(R.id.add_edit_medicine_details);
        medicineName = findViewById(R.id.add_edit_medicine_name);
        medicineTips = findViewById(R.id.consigli_paziente);
        medicineHour = findViewById(R.id.orario_medicina);
        medicineLinks = findViewById(R.id.link_utili);

        spinnerNum = findViewById(R.id.spinner_quantita);
        spinnerFreq = findViewById(R.id.spinner_freq);
        spinnerNum.setOnItemSelectedListener(this);

        notifCheckbox = findViewById(R.id.checkbox_notifiche);

        saveEdits = findViewById(R.id.save_therapy_edits);

        /* Week day picker */
        materialDayPicker = findViewById(R.id.day_picker);
        materialDayPicker.setLocale(Locale.ITALIAN);

        /* Setting spinners' values and adapters */
        ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNum.setAdapter(adapterInt);
        spinnerFreq.setAdapter(adapterString);

        /* Intent for therapy */
        Intent therapyIntent = getIntent();
        if (therapyIntent != null) {
            Bundle bundle = therapyIntent.getExtras();
            if (bundle != null) {
                currentTherapy = bundle.getParcelable("MEDICINA");
                if (currentTherapy != null) {
                    medicineName.setText(currentTherapy.getName());
                    medicineDetails.setText(currentTherapy.getDescription());
                    medicineStandardDosage.setText(currentTherapy.getDosage());
                    medicineLinks.setText(currentTherapy.getLink());
                    medicineHour.setText(currentTherapy.getTimeHour());
                    medicineTips.setText(currentTherapy.getSupervisorTips());
                    notifCheckbox.setChecked(currentTherapy.isNotificationEnabled());
                    newTherapy = currentTherapy;    // copying current therapy for future editing
                }
                User user = bundle.getParcelable("user");
                userId = Objects.requireNonNull(user).getUserId();
            }
        }

        /* medicineHour on click listener */
        medicineHour.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance(); // opening datepicker to set notification hour
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(EditMedicineActivity.this, (timePicker, selectedHour, selectedMinute) -> medicineHour.setText(String.format("%d:%d", selectedHour, selectedMinute)), hour, minute, true);//Yes 24 hour time
            mTimePicker.setMessage("Seleziona l'orario");
            mTimePicker.show();
        });

        /* saveEdits on click listener */
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

    private boolean editTherapy(Medicine newTherapy) throws IOException {
        newTherapy.setName(String.valueOf(medicineName.getText()));
        newTherapy.setDescription(String.valueOf(medicineDetails.getText()));
        newTherapy.setDosage(String.valueOf(medicineStandardDosage.getText()));
        newTherapy.setLink(String.valueOf(medicineLinks.getText()));
        newTherapy.setTimeHour(String.valueOf(medicineHour.getText()));
        newTherapy.setSupervisorTips(String.valueOf(medicineTips.getText()));
        newTherapy.setTaken(currentTherapy.isTaken());
        newTherapy.setNotificationEnabled(notifCheckbox.isChecked());

        return MedicineFactory.getInstance().deleteMedicineFromCode(UserFactory.getInstance().getUser(userId), newTherapy);
    }

    /* Override on back pressed in order to change the animation */
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
        // do nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
