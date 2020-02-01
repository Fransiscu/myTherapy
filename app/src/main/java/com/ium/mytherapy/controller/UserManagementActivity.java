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
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.views.CardAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UserManagementActivity extends AppCompatActivity {

    TextView nome;
    CircleImageView profileImage, editPicture;
    TextInputEditText profileName, profileSurname, profileUsername, profilePassword, birthdateInput;
    MaterialButton deleteUser, save;
    private int mYear, mMonth, mDay;
    int userKey;
    User user;
    File path = Environment.getExternalStorageDirectory();
    File dir = new File(path.getAbsolutePath() + "/myTherapy/");


    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_utente);

        nome = findViewById(R.id.profileTitle);
        profileImage = findViewById(R.id.profileImage);
        editPicture = findViewById(R.id.editProfileImage);
        deleteUser = findViewById(R.id.deleteUser);
        save = findViewById(R.id.saveUserEdits);
        birthdateInput = findViewById(R.id.profile_date);
        profileName = findViewById(R.id.profile_name);
        profileSurname = findViewById(R.id.profile_surname);
        profileUsername = findViewById(R.id.profile_username);
        profilePassword = findViewById(R.id.profile_password);

        Intent usersIntent = getIntent();

        if (usersIntent != null) {
            Bundle bundle = usersIntent.getExtras();
            if (bundle != null) {
                userKey = bundle.getInt(CardAdapter.USER_KEY);
            }
        }

        try {
            user = UserFactory.getInstance().getUser(userKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Riempio i campi */
        profileName.setText(user.getNome());
        profileSurname.setText(user.getCognome());
        profileUsername.setText(user.getUsername());
        birthdateInput.setText(user.getDataNascita());
        profilePassword.setText(user.getPassword());

        nome.setText(String.format("%s %s", user.getNome(), user.getCognome()));

        /* Immagine profilo */
        File profilePicture = new File(dir + "/avatar_" + user.getUserId() + ".jpeg");
        File defaultAvatar = new File(dir + "/default.jpg");
        if (profilePicture.exists()) {
            profileImage.setImageURI(Uri.fromFile(profilePicture));
        } else {
            profileImage.setImageURI(Uri.fromFile(defaultAvatar));

        }

        /* Listener tasto salvataggio dati utente */
        save.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("SALVATAGGIO")
                .setMessage("Salvare i cambiamenti?")
                .setCancelable(false)
                .setPositiveButton("Salva", (dialogInterface, i) -> {
                    File file = new File(dir, "avatar_" + userKey + ".jpeg");
                    file.delete();
                    File from = new File(dir, "avatar_" + userKey + "t.jpeg");
                    File to = new File(dir, "avatar_" + userKey + ".jpeg");
                    from.renameTo(to);
                    Toast.makeText(getBaseContext(), "Salvataggio effettuato", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

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
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            }
        });

        /* Listener tasto cancellazione utente */
        deleteUser.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("CANCELLAZIONE")
                .setMessage("Sei sicuro di voler cancellare l'utente?")
                .setCancelable(false)
                .setPositiveButton("Procedi", (dialogInterface, i) -> {
//                    userList.remove(userKey);   //TODO: risolvere con utenti veri
                    Toast.makeText(getBaseContext(), "Utente cancellato", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        File file = new File(dir, "avatar_" + userKey + "t.jpeg");
        file.delete();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                boolean success = true;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);

                if (!dir.exists()) {
                    success = dir.mkdir();
                }
                if (success) {
                    /* Salva nella cartella */
                    File file = new File(dir, "avatar_" + userKey + "t.jpeg");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    Toast.makeText(getBaseContext(), "Immagine salvata", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Qualcosa è andato storto", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("IntentReset")
    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleziona la foto"), 1000);
    }

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
