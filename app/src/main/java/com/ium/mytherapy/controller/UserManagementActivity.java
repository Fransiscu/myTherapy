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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.views.recycleviews.adapters.MedicineTimelineCardAdapter;
import com.ium.mytherapy.views.recycleviews.adapters.MedicinelistCardAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UserManagementActivity extends AppCompatActivity {

    TextInputEditText profileName, profileSurname, profileUsername, profilePassword, birthdateInput;
    RecyclerView MedicineTimelineCardRecyclerView, MedicinelistCardRecyclerView;
    public MedicineTimelineCardAdapter medicineTimelineCardAdapter;
    public MedicinelistCardAdapter medicinelistCardAdapter;
    MaterialButton deleteUser, save, addTherapy;
    CircleImageView profileImage, editPicture;
    TextView nome;

    private int mYear, mMonth, mDay, userKey;
    List<Medicine> medicineList;
    boolean avatarChanged;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_management_activity);

        nome = findViewById(R.id.profileTitle);
        profileImage = findViewById(R.id.profile_image);
        editPicture = findViewById(R.id.edit_profile_image);
        deleteUser = findViewById(R.id.deleteUser);
        save = findViewById(R.id.saveUserEdits);
        addTherapy = findViewById(R.id.add_therapy_button_id);
        birthdateInput = findViewById(R.id.profile_date);
        profileName = findViewById(R.id.profile_name);
        profileSurname = findViewById(R.id.profile_surname);
        profileUsername = findViewById(R.id.profile_username);
        profilePassword = findViewById(R.id.profile_password);

        /* User intent */
        Intent usersIntent = getIntent();
        if (usersIntent != null) {
            Bundle bundle = usersIntent.getExtras();
            if (bundle != null) {
                userKey = bundle.getInt(DefaultValues.USER_KEY);
                Log.d("user id", String.valueOf(userKey));
            }
        }
        try {
            user = UserFactory.getInstance().getUser(userKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* List for recyclerview */
        try {
            medicineList = MedicineFactory.getInstance().getMedicinesForUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Medicine> medicineArrayList;
        medicineArrayList = new ArrayList<>(medicineList);

        /* Filling base fields */
        profileName.setText(user.getName());
        profileSurname.setText(user.getSurname());
        profileUsername.setText(user.getUsername());
        birthdateInput.setText(user.getBirthDate());
        profilePassword.setText(user.getPassword());
        nome.setText(String.format("%s %s", user.getName(), user.getSurname()));

        /* Profile picture */
        File profilePicture = new File(DefaultValues.dir + "/avatar_" + user.getUserId() + ".jpeg");
        File defaultAvatar = new File(DefaultValues.dir + "/default.jpg");
        if (profilePicture.exists()) {
            profileImage.setImageURI(Uri.fromFile(profilePicture));
        } else {
            profileImage.setImageURI(Uri.fromFile(defaultAvatar));

        }

        /* Filling timeline */
        MedicineTimelineCardRecyclerView = findViewById(R.id.userManagementMedicineTimelineRecycleView);
        MedicineTimelineCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineTimelineCardAdapter = new MedicineTimelineCardAdapter(this, user, medicineArrayList);
        MedicineTimelineCardRecyclerView.setAdapter(medicineTimelineCardAdapter);

        /* Filling therapy list */
        MedicinelistCardRecyclerView = findViewById(R.id.user_management_medicine_list_recycleview);
        MedicinelistCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicinelistCardAdapter = new MedicinelistCardAdapter(this, user, medicineArrayList, medicineTimelineCardAdapter);
        MedicinelistCardRecyclerView.setAdapter(medicinelistCardAdapter);

        /* save user data on click listener */
        save.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("SALVATAGGIO")
                .setMessage("Salvare i cambiamenti?")
                .setCancelable(false)
                .setPositiveButton("Salva", (dialogInterface, i) -> {
                    if (avatarChanged) {
                        UserFactory.getInstance().changeAvatar(userKey);
                    }
                    try {
                        User editedUser = updateUser(user);
                        UserFactory.getInstance().editUser(editedUser);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(), "Salvataggio effettuato", Toast.LENGTH_LONG).show();
                    Intent backToSupervisorHome = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                    backToSupervisorHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(backToSupervisorHome);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        /* addTherapy on click listener */
        addTherapy.setOnClickListener(view -> {
            Intent addTherapyIntent = new Intent(getApplicationContext(), AddMedicineActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            addTherapyIntent.putExtras(bundle);
            startActivity(addTherapyIntent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        });

        /* Open calendar on date field click */
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

        /* editPicture on click listener */
        editPicture.setOnClickListener(v -> {   // check if app has the permissions
            if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                    (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                pickImage();
            } else {    // if not, ask for them
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, DefaultValues.PERMISSION_REQUEST_CODE);
            }
        });

        /* deleteUser on click listener */
        deleteUser.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("CANCELLAZIONE")
                .setMessage("Sei sicuro di voler cancellare l'utente?")
                .setCancelable(false)
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
                    try {
                        UserFactory.getInstance().deleteUser(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(), "Utente cancellato", Toast.LENGTH_LONG).show();
                    Intent backToSupervisorHome = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
                    backToSupervisorHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(backToSupervisorHome);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_right);
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

    }

    /* Gather data from all fields */
    private User updateUser(User user) {
        User editedUser = new User();
        editedUser.setUserId(user.getUserId());
        editedUser.setName(Objects.requireNonNull(profileName.getText()).toString());
        editedUser.setSurname(Objects.requireNonNull(profileSurname.getText()).toString());
        editedUser.setUsername(Objects.requireNonNull(profileUsername.getText()).toString());
        editedUser.setEmail(Objects.requireNonNull(user.getEmail()));
        editedUser.setBirthDate(Objects.requireNonNull(birthdateInput.getText()).toString());
        editedUser.setPassword(Objects.requireNonNull(profilePassword.getText()).toString());
        return editedUser;
    }

    /* Get picture from user */
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

    /* Open image picker */
    @SuppressLint("IntentReset")
    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleziona la foto"), 1000);
    }

    /* Checking for permissions result */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == DefaultValues.PERMISSION_REQUEST_CODE) {
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

    /* Override on back pressed in order to change the animation */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent(getApplicationContext(), SupervisorHomeActivity.class);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        File file = new File(DefaultValues.dir, "avatar_" + userKey + "t.jpeg");
        file.delete();  // annullo eventuale cambiamento avatar non salvato
        startActivity(returnIntent);
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }
}
