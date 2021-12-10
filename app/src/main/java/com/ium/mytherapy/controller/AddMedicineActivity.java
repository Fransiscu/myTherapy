package com.ium.mytherapy.controller;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
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
import java.util.concurrent.atomic.AtomicReference;

import androidx.appcompat.app.AppCompatActivity;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddMedicineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static SharedPreferences mPreferences;
    public static String sharedPrefFile = DefaultValues.SHARED_PREFS;

    TextInputEditText medicineName, medicineDetails, medicineStandardDosage, medicineLinks, medicineTips, medicineTime;

    MaterialSpinner spinnerNum, spinnerFreq;
    MaterialDayPicker materialDayPicker;
    MaterialCheckBox checkbox;
    MaterialButton addTherapy;
    MaterialTextView title;

    String[] itemsNumber = new String[]{"1", "2", "3"};
    String[] itemsString = new String[]{"Giorno", "Settimana", "Mese", "Una tantum"};

    final static String test = "test";
    int tapsCount = 0;
    int userId;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine_activity);
        AtomicReference<Medicine> medicine = new AtomicReference<>(new Medicine());
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        /* Getting user sharedPreferences if they exist */
        Intent therapyIntent = getIntent();
        if (therapyIntent != null) {
            Bundle bundle = therapyIntent.getExtras();
            if (bundle != null) {
                User user = bundle.getParcelable("user");
                userId = Objects.requireNonNull(user).getUserId();
            }
        }

        /* TextInputEditText */
        title = findViewById(R.id.titolo_aggiunta_modifica_medicina);
        medicineName = findViewById(R.id.add_edit_medicine_name);
        medicineDetails = findViewById(R.id.add_edit_medicine_details);
        medicineStandardDosage = findViewById(R.id.add_edit_medicine_dosage);
        medicineLinks = findViewById(R.id.link_utili);
        medicineTips = findViewById(R.id.consigli_paziente);
        medicineTime = findViewById(R.id.orario_medicina);

        /* Spinners */
        spinnerNum = findViewById(R.id.spinner_quantita);
        spinnerFreq = findViewById(R.id.spinner_freq);
        spinnerNum.setOnItemSelectedListener(this);

        /* Buttons */
        addTherapy = findViewById(R.id.add_therapy_button);

        /* Checkboxes */
        checkbox = findViewById(R.id.checkbox_notifiche);

        /* Week day picker */
        materialDayPicker = findViewById(R.id.day_picker);
        materialDayPicker.setLocale(Locale.ITALIAN);

        /* Setting spinners + adapters values */
        ArrayAdapter<String> adapterInt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsNumber);
        adapterInt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapterString = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsString);
        adapterString.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNum.setAdapter(adapterInt);
        spinnerFreq.setAdapter(adapterString);

        medicineTime.setShowSoftInputOnFocus(false);
        medicineTime.setInputType(InputType.TYPE_NULL);
        medicineTime.setFocusable(false);

        /* Double tapping the title fills the interface with test values */
        title.setOnClickListener(view -> {
            tapsCount++;
            if (tapsCount > 1) {
                setTestFields();
            }
        });

        /* medicineTime on click listener*/
        medicineTime.setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance(); // Opening datepicker to select medicine time
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(AddMedicineActivity.this, (timePicker, selectedHour, selectedMinute) -> medicineTime.setText(format("%d:%d", selectedHour, selectedMinute)), hour, minute, true);    //Yes 24 hour time
            mTimePicker.setMessage("Seleziona l'orario");
            mTimePicker.show(); // Confirmation window + toast
        });

        /* addTherapy button on click listener */
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
                                MedicineFactory.getInstance().addMedicine(UserFactory.getInstance().getUser(userId), medicine.get());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putInt(DefaultValues.USER_KEY, userId);
                            try {
                                bundle.putParcelable(DefaultValues.USER_INTENT, UserFactory.getInstance().getUser(userId));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            backToManagement.putExtras(bundle);
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

    /* Setting automatic test fields */
    private void setTestFields() {
        medicineName.setText(test);
        medicineDetails.setText(test);
        medicineStandardDosage.setText("1");
        medicineLinks.setText(test);
        medicineTips.setText(test);
        medicineTime.setText(R.string.test_hour);
    }

    /* get new medicine from input fields*/
    private Medicine getMedicine() {
        Medicine medicine = new Medicine();
        medicine.setSupervisorTips(Objects.requireNonNull(medicineTips.getText()).toString());
        medicine.setDosage(Objects.requireNonNull(medicineStandardDosage.getText()).toString());
        medicine.setDescription(Objects.requireNonNull(medicineDetails.getText()).toString());
        medicine.setLink(Objects.requireNonNull(medicineLinks.getText()).toString());
        medicine.setName(Objects.requireNonNull(medicineName.getText()).toString());
        medicine.setTimeHour(Objects.requireNonNull(medicineTime.getText()).toString());
        medicine.setFrequency(String.valueOf(spinnerFreq.getSelectedItem()));
        TextView textView = (TextView) spinnerNum.getSelectedView();    // needed to select item from spinner
        medicine.setNotificationEnabled(checkbox.isChecked());
        medicine.setTaken(false);

        String result = textView.getText().toString();
        medicine.setFrequencyNumber(Integer.parseInt(result));

        return medicine;
    }

    /* Checking input values */
    private boolean checkInput() {
        String medName = Objects.requireNonNull(medicineName.getText()).toString();
        String medDetails = Objects.requireNonNull(medicineDetails.getText()).toString();
        String medStandardDosage = Objects.requireNonNull(medicineStandardDosage.getText()).toString();
        String timeOfDay = Objects.requireNonNull(medicineTime.getText()).toString();

        boolean valid = true;

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
        if (timeOfDay.isEmpty()) {
            medicineTime.setError("Inserisci un orario");
            valid = false;
        } else {
            medicineTime.setError(null);
        }

        return valid;
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

        Intent userManagementHome = new Intent(this, UserManagementActivity.class);
        userManagementHome.putExtras(bundle);
        userManagementHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(userManagementHome);
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
