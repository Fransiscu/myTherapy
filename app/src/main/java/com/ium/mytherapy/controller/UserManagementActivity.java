package com.ium.mytherapy.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.views.recycleviews.adapters.MedicineTimelineCardAdapter;
import com.ium.mytherapy.views.recycleviews.adapters.MedicinelistCardAdapter;
import com.ium.mytherapy.views.recycleviews.adapters.UserlistCardAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UserManagementActivity extends AppCompatActivity {
    RecyclerView MedicineTimelineCardRecyclerView, MedicinelistCardRecyclerView;
    MedicineTimelineCardAdapter medicineTimelineCardAdapter;
    MedicinelistCardAdapter medicinelistCardAdapter;
    CircleImageView profileImage, editPicture;
    MaterialButton deleteUser, save, addTherapy;
    TextInputEditText profileName, profileSurname, profileUsername, profilePassword, birthdateInput;
    List<Medicina> medicineList;
    TextView nome;
    private int mYear, mMonth, mDay, userKey;
    boolean avatarChanged;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_utente);

        nome = findViewById(R.id.profileTitle);
        profileImage = findViewById(R.id.profileImage);
        editPicture = findViewById(R.id.editProfileImage);
        deleteUser = findViewById(R.id.deleteUser);
        save = findViewById(R.id.saveUserEdits);
        addTherapy = findViewById(R.id.aggiungi_terapia_button);
        birthdateInput = findViewById(R.id.profile_date);
        profileName = findViewById(R.id.profile_name);
        profileSurname = findViewById(R.id.profile_surname);
        profileUsername = findViewById(R.id.profile_username);
        profilePassword = findViewById(R.id.profile_password);

        /* Intent per user */
        Intent usersIntent = getIntent();
        if (usersIntent != null) {
            Bundle bundle = usersIntent.getExtras();
            if (bundle != null) {
                userKey = bundle.getInt(UserlistCardAdapter.USER_KEY);
            }
        }
        try {
            user = UserFactory.getInstance().getUser(userKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Lista per le recyclerviews */
        try {
            medicineList = MedicinaFactory.getInstance().getMedicinesForUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Medicina> medicineArrayList;
        medicineArrayList = new ArrayList<>(medicineList);

        /* Riempio i campi base */
        profileName.setText(user.getNome());
        profileSurname.setText(user.getCognome());
        profileUsername.setText(user.getUsername());
        birthdateInput.setText(user.getDataNascita());
        profilePassword.setText(user.getPassword());
        nome.setText(String.format("%s %s", user.getNome(), user.getCognome()));

        /* Immagine profilo */
        File profilePicture = new File(DefaultValues.dir + "/avatar_" + user.getUserId() + ".jpeg");
        File defaultAvatar = new File(DefaultValues.dir + "/default.jpg");
        if (profilePicture.exists()) {
            profileImage.setImageURI(Uri.fromFile(profilePicture));
        } else {
            profileImage.setImageURI(Uri.fromFile(defaultAvatar));

        }

        /* Riempio timeline */
        MedicineTimelineCardRecyclerView = findViewById(R.id.userManagementMedicineTimelineRecycleView);
        MedicineTimelineCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineTimelineCardAdapter = new MedicineTimelineCardAdapter(this, medicineArrayList);
        MedicineTimelineCardRecyclerView.setAdapter(medicineTimelineCardAdapter);
        /* Fine timeline */

        /* Riempio lista terapie */
        MedicinelistCardRecyclerView = findViewById(R.id.userManagementMedicineListRecycleView);
        MedicinelistCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicinelistCardAdapter = new MedicinelistCardAdapter(this, medicineArrayList);
        MedicinelistCardRecyclerView.setAdapter(medicinelistCardAdapter);
        /* Fine lista terapie */

        /* Listener tasto salvataggio dati utente */
        save.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("SALVATAGGIO")
                .setMessage("Salvare i cambiamenti?")
                .setCancelable(false)
                .setPositiveButton("Salva", (dialogInterface, i) -> {
                    if (avatarChanged) {
                        File file = new File(DefaultValues.dir, "avatar_" + userKey + ".jpeg");
                        file.delete();
                        File from = new File(DefaultValues.dir, "avatar_" + userKey + "t.jpeg");
                        File to = new File(DefaultValues.dir, "avatar_" + userKey + ".jpeg");
                        from.renameTo(to);
                    }
                    Toast.makeText(getBaseContext(), "Salvataggio effettuato", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        /* Listener per tasto aggiunta terapia */
        addTherapy.setOnClickListener(view -> {
            Intent addTherapyIntent = new Intent(getApplicationContext(), AddMedicineActivity.class);
            startActivity(addTherapyIntent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        });

        /* Calendario al tocco del campo data */
        birthdateInput.setShowSoftInputOnFocus(false);
        birthdateInput.setInputType(InputType.TYPE_NULL);
        birthdateInput.setFocusable(false);

        birthdateInput.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> birthdateInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        /* Listener tasto edit immagine */
        editPicture.setOnClickListener(v -> {
            if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                pickImage();
            } else {    // se non ho i permessi li chiedo
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            }
        });

        /* Listener tasto cancellazione utente */
        deleteUser.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("CANCELLAZIONE")
                .setMessage("Sei sicuro di voler cancellare l'utente?")
                .setCancelable(false)
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
//                    userList.remove(userKey);     //method per cancellare l'utente
                    Toast.makeText(getBaseContext(), "Utente cancellato", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());
    }

    /* Override pressione tasto back per cambiare l'animazione */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        File file = new File(DefaultValues.dir, "avatar_" + userKey + "t.jpeg");
        file.delete();  // annullo eventuale cambiamento avatar non salvato
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    /* Uso per ottenere l'immagine selezionata dall'utente */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                boolean success = true;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);

                if (!DefaultValues.dir.exists()) {
                    success = DefaultValues.dir.mkdir();
                }
                if (success) {
                    /* Salva nella cartella */
                    File file = new File(DefaultValues.dir, "avatar_" + userKey + "t.jpeg");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    avatarChanged = true;
                    Toast.makeText(getBaseContext(), "Immagine salvata", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Qualcosa è andato storto", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Apro image picker */
    @SuppressLint("IntentReset")
    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleziona la foto"), 1000);
    }

    /* Controllo permessi prima di aprire selector dell'immagine */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.PERMISSION_REQUEST_CODE) {
            if ((grantResults.length > 0)
                    && (grantResults[0] +
                    grantResults[1]
                    == PackageManager.PERMISSION_GRANTED)) {
                System.out.println(("Permssions granted"));
            } else {
                System.out.println(("Permssions not granted"));
            }
        }
    }

}
