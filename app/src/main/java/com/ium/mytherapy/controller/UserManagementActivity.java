package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserManagementActivity extends AppCompatActivity {

    TextView nome;
    CircleImageView image;
    MaterialButton deleteUser, save;
    TextInputEditText birthdateInput;

    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_utente);

        datePickerFragment = new DatePickerFragment();

        nome = findViewById(R.id.titleProfile);
        image = findViewById(R.id.imageProfile);
        deleteUser = findViewById(R.id.deleteUser);
        save = findViewById(R.id.saveUserEdits);
        birthdateInput = findViewById(R.id.profile_date);

        Intent intent = getIntent();
        String nomeString = intent.getStringExtra("nome");

        byte[] nBytes = getIntent().getByteArrayExtra("avatar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(nBytes, 0, Objects.requireNonNull(nBytes).length);

        nome.setText(nomeString);
        image.setImageBitmap(bitmap);

        /* Listener tasto cancellazione utente */
        deleteUser.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("Conferma")
                .setMessage("Sei sicuro di voler cancellare l'utente?")
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
                    //TODO: implementazione cancellazione
                    Toast.makeText(getBaseContext(), "Utente cancellato", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        /* Listener tasto salvataggio dati utente */
        save.setOnClickListener(view -> {
            //TODO: implementazione salvataggio
            Toast.makeText(getBaseContext(), "Salvataggio effettuato", Toast.LENGTH_LONG).show();
            finish();
        });


        /* Calendario al tocco del campo data */
        birthdateInput.setOnClickListener(v -> datePickerFragment.show(getSupportFragmentManager(), "date picker"));

        birthdateInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                birthdateInput.setShowSoftInputOnFocus(false);
                birthdateInput.setInputType(InputType.TYPE_NULL);
                birthdateInput.setFocusable(false);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        datePickerFragment.setOnDatePickerFragmentChanged(new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDatePickerFragmentOkButton(DialogFragment dialog, Calendar date) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                birthdateInput.setText(format.format(date.getTime()));
            }

            @Override
            public void onDatePickerFragmentCancelButton(DialogFragment dialog) {

            }
        });
    }
}
