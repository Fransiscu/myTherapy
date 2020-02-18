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
import android.view.View;
import android.widget.ImageView;
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
import com.ium.mytherapy.views.CardAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UserManagementActivity extends AppCompatActivity {

    TextView nome, medicine1, medicine2, medicine3, medicine1Time, medicine2Time, medicine3Time;
    TextView editMedicineName1, editMedicineName2, editMedicineName3;
    CircleImageView profileImage, editPicture;
    ImageView notif1, notif2, notif3, completed1, completed2, completed3;
    ImageView deleteTherapy1, deleteTherapy2, deleteTherapy3, editTherapy1, editTherapy2, editTherapy3;
    TextInputEditText profileName, profileSurname, profileUsername, profilePassword, birthdateInput;
    ArrayList<Medicina> medicinesList = (ArrayList<Medicina>) MedicinaFactory.getInstance().getMedicines();
    MaterialButton deleteUser, save, addTherapy;
    private int mYear, mMonth, mDay;
    boolean avatarChanged;
    int userKey;
    boolean notifEnabled1 = true, completedEnabled1 = true;
    boolean notifEnabled2 = true, completedEnabled2 = false;
    boolean notifEnabled3 = false, completedEnabled3 = false;
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
        addTherapy = findViewById(R.id.aggiungi_terapia_button);
        birthdateInput = findViewById(R.id.profile_date);
        profileName = findViewById(R.id.profile_name);
        profileSurname = findViewById(R.id.profile_surname);
        profileUsername = findViewById(R.id.profile_username);
        profilePassword = findViewById(R.id.profile_password);

        /* Singole medicine mini timeline */
        medicine1 = findViewById(R.id.nome_medicina_uno);
        medicine2 = findViewById(R.id.nome_medicina_due);
        medicine3 = findViewById(R.id.nome_medicina_tre);

        medicine1Time = findViewById(R.id.orario_medicina_uno);
        medicine2Time = findViewById(R.id.orario_medicina_due);
        medicine3Time = findViewById(R.id.orario_medicina_tre);

        editMedicineName1 = findViewById(R.id.nome_modifica_medicina_uno);
        editMedicineName2 = findViewById(R.id.nome_modifica_medicina_due);
        editMedicineName3 = findViewById(R.id.nome_modifica_medicina_tre);

        /* Elementi della mini timeline */
        notif1 = findViewById(R.id.notifica_uno);
        notif2 = findViewById(R.id.notifica_due);
        notif3 = findViewById(R.id.notifica_tre);

        completed1 = findViewById(R.id.status_terapia_uno);
        completed2 = findViewById(R.id.status_terapia_due);
        completed3 = findViewById(R.id.status_terapia_tre);

        /* Elementi terapie associate all'utente */

        deleteTherapy1 = findViewById(R.id.cancella_terapia_uno);
        deleteTherapy2 = findViewById(R.id.cancella_terapia_due);
        deleteTherapy3 = findViewById(R.id.cancella_terapia_tre);

        editTherapy1 = findViewById(R.id.modifica_medicina_uno);
        editTherapy2 = findViewById(R.id.modifica_medicina_due);
        editTherapy3 = findViewById(R.id.modifica_medicina_tre);

        /* Intent per user */
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

        /* Listeners per tasti di modifica terapia */
        editTherapy1.setOnClickListener(view -> {
            Intent editTherapyIntent = new Intent(this, EditTherapyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("MEDICINA", medicinesList.get(0));
            editTherapyIntent.putExtras(bundle);
            startActivity(editTherapyIntent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        });

        editTherapy2.setOnClickListener(view -> {
            Intent editTherapyIntent = new Intent(this, EditTherapyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("MEDICINA", medicinesList.get(1));
            editTherapyIntent.putExtras(bundle);
            startActivity(editTherapyIntent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        });

        editTherapy3.setOnClickListener(view -> {
            Intent editTherapyIntent = new Intent(this, EditTherapyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("MEDICINA", medicinesList.get(2));
            editTherapyIntent.putExtras(bundle);
            startActivity(editTherapyIntent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        });

        /* Primo item timeline */
        notif1.setOnClickListener(view -> {
            if (notifEnabled1) {
                notif1.setBackgroundResource(0);
                notif1.setImageDrawable(getDrawable(R.drawable.notification_inactive));
                notifEnabled1 = !notifEnabled1;   // toggle
                Toast.makeText(getBaseContext(), "Notifiche disattivate", Toast.LENGTH_LONG).show();
            } else {
                notif1.setBackgroundResource(0);
                notif1.setImageDrawable(getDrawable(R.drawable.notification_active));
                notifEnabled1 = !notifEnabled1;   // toggle
                Toast.makeText(getBaseContext(), "Notifiche attivate", Toast.LENGTH_LONG).show();
            }
        });
        completed1.setOnClickListener(view -> {
            if (completedEnabled1) {
                completed1.setBackgroundResource(0);
                completed1.setImageDrawable(getDrawable(R.drawable.timeline_not_done));
                completedEnabled1 = !completedEnabled1;   // toggle
                Toast.makeText(getBaseContext(), "Stato modificato", Toast.LENGTH_LONG).show();
            } else {
                completed1.setBackgroundResource(0);
                completed1.setImageDrawable(getDrawable(R.drawable.timeline_done));
                completedEnabled1 = !completedEnabled1;   // toggle
                Toast.makeText(getBaseContext(), "Stato modificato", Toast.LENGTH_LONG).show();
            }
        });

        /* Secondo item timeline */
        notif2.setOnClickListener(view -> {
            if (notifEnabled2) {
                notif2.setBackgroundResource(0);
                notif2.setImageDrawable(getDrawable(R.drawable.notification_inactive));
                notifEnabled2 = !notifEnabled2;   // toggle
                Toast.makeText(getBaseContext(), "Notifiche disattivate", Toast.LENGTH_LONG).show();
            } else {
                notif2.setBackgroundResource(0);
                notif2.setImageDrawable(getDrawable(R.drawable.notification_active));
                notifEnabled2 = !notifEnabled2;   // toggle
                Toast.makeText(getBaseContext(), "Notifiche attivate", Toast.LENGTH_LONG).show();
            }
        });
        completed2.setOnClickListener(view -> {
            if (completedEnabled2) {
                completed2.setBackgroundResource(0);
                completed2.setImageDrawable(getDrawable(R.drawable.timeline_not_done));
                completedEnabled2 = !completedEnabled2;   // toggle
                Toast.makeText(getBaseContext(), "Stato modificato", Toast.LENGTH_LONG).show();
            } else {
                completed2.setBackgroundResource(0);
                completed2.setImageDrawable(getDrawable(R.drawable.timeline_done));
                completedEnabled2 = !completedEnabled2;   // toggle
                Toast.makeText(getBaseContext(), "Stato modificato", Toast.LENGTH_LONG).show();
            }
        });

        /* Terzo item timeline */
        notif3.setOnClickListener(view -> {
            if (notifEnabled3) {
                notif3.setBackgroundResource(0);
                notif3.setImageDrawable(getDrawable(R.drawable.notification_inactive));
                notifEnabled3 = !notifEnabled3;   // toggle
                Toast.makeText(getBaseContext(), "Notifiche disattivate", Toast.LENGTH_LONG).show();
            } else {
                notif3.setBackgroundResource(0);
                notif3.setImageDrawable(getDrawable(R.drawable.notification_active));
                notifEnabled3 = !notifEnabled3;   // toggle
                Toast.makeText(getBaseContext(), "Notifiche attivate", Toast.LENGTH_LONG).show();
            }
        });
        completed3.setOnClickListener(view -> {
            if (completedEnabled3) {
                completed3.setBackgroundResource(0);
                completed3.setImageDrawable(getDrawable(R.drawable.timeline_not_done));
                completedEnabled3 = !completedEnabled3;   // toggle
                Toast.makeText(getBaseContext(), "Stato modificato", Toast.LENGTH_LONG).show();
            } else {
                completed3.setBackgroundResource(0);
                completed3.setImageDrawable(getDrawable(R.drawable.timeline_done));
                completedEnabled3 = !completedEnabled3;   // toggle
                Toast.makeText(getBaseContext(), "Stato modificato", Toast.LENGTH_LONG).show();
            }
        });

        /* Fine elementi della mini timeline */

        /* Listeners terapie associate all'utente */
        View.OnClickListener onClickListener = view -> new MaterialAlertDialogBuilder(this)
                .setTitle("Rimozione terapia")
                .setMessage("Sicuro di voler rimuovere la terapia associata all'utente?")
                .setCancelable(false)
                .setPositiveButton("Rimuovi", (dialogInterface, i) -> Toast.makeText(getBaseContext(), "Terapia rimossa", Toast.LENGTH_LONG).show())
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show();

        deleteTherapy1.setOnClickListener(onClickListener);
        deleteTherapy2.setOnClickListener(onClickListener);
        deleteTherapy3.setOnClickListener(onClickListener);

        /* Fine listeners terapie associate all'utente */

        /* Riempio i campi */
        profileName.setText(user.getNome());
        profileSurname.setText(user.getCognome());
        profileUsername.setText(user.getUsername());
        birthdateInput.setText(user.getDataNascita());
        profilePassword.setText(user.getPassword());
        medicine1.setText(medicinesList.get(0).getNome());
        medicine2.setText(medicinesList.get(1).getNome());
        medicine3.setText(medicinesList.get(2).getNome());
        medicine1Time.setText(medicinesList.get(0).getOra());
        medicine2Time.setText(medicinesList.get(1).getOra());
        medicine3Time.setText(medicinesList.get(2).getOra());
        editMedicineName1.setText(medicinesList.get(0).getNome());
        editMedicineName2.setText(medicinesList.get(1).getNome());
        editMedicineName3.setText(medicinesList.get(2).getNome());

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
                    if (avatarChanged) {
                        File file = new File(dir, "avatar_" + userKey + ".jpeg");
                        file.delete();
                        File from = new File(dir, "avatar_" + userKey + "t.jpeg");
                        File to = new File(dir, "avatar_" + userKey + ".jpeg");
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
            Intent addTherapyIntent = new Intent(getApplicationContext(), AddTherapyActivity.class);
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
//                    userList.remove(userKey);
                    Toast.makeText(getBaseContext(), "Utente cancellato", Toast.LENGTH_LONG).show();
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        File file = new File(dir, "avatar_" + userKey + "t.jpeg");
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

                if (!dir.exists()) {
                    success = dir.mkdir();
                }
                if (success) {
                    /* Salva nella cartella */
                    File file = new File(dir, "avatar_" + userKey + "t.jpeg");
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
