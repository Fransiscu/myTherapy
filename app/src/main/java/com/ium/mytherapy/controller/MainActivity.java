package com.ium.mytherapy.controller;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSION_REQUEST_CODE = 123;
    final static String USER_LIST = "DEFAULT_USER_LIST";
    ArrayList<User> userList = new ArrayList<>();
    public static SharedPreferences mPreferences;
    String userValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Inizializzo sharedPreferences */
        mPreferences = getSharedPreferences(DefaultValues.sharedPrefFile, MODE_PRIVATE);

        /* Riprendo stato passato dell'utente nell'app */
        Runnable loadData = this::loadData;
        loadData.run();

        /* Controllo i permessi */
        Runnable permissionsThread = this::permissions;
        permissionsThread.run();

        /* Thread che recupera gli users dalle directory */
        Runnable getUsers = () -> {
            try {
                userList = UserFactory.getInstance().getUsers();
                Collections.sort(userList); // ordino alfabeticamente
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        getUsers.run();

        /* Reimposto utente al riavvio dell'app */
        if (userValue.equals("user")) {
            Intent userIntent = new Intent(this, UserHomeActivity.class);
            startActivity(userIntent);
            finish();
        } else if (userValue.equals("supervisor")) {
            Intent supervisorIntent = new Intent(this, SupervisorHomeActivity.class);
            startActivity(supervisorIntent);
            finish();
        } else {
            Intent loginActivity = new Intent(this, LoginActivity.class);    // cambio subito activity alla login se non c'è alcun utente salvata
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(USER_LIST, userList); // passo la lista di utenti in intent
            loginActivity.putExtras(bundle);
            startActivity(loginActivity);
            finish();
        }
    }

    /* Metodo per il caricamento dei dati dalle sharedPreferences */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
        userValue = sharedPreferences.getString(DefaultValues.USER_TYPE, "");
    }

    /* Metodo di controllo dei permessi e aggiunta files di default */
    private void permissions() {
        if (!(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            addDefaultItems();
        } else {
            addDefaultItems();
        }
    }

    public void addDefaultItems() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            try {
                boolean wasSuccessful;
//                noinspection ResultOfMethodCallIgnored
                DefaultValues.dir.mkdirs();   // per evitare i warning zzz
                wasSuccessful = DefaultValues.usersDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                wasSuccessful = DefaultValues.supervisorDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatardefault);
                wasSuccessful = DefaultValues.defaultAvatar.createNewFile();
                if (!wasSuccessful) {   // per evitare i warnings
                    System.out.println("was not successful.");
                }
                wasSuccessful = DefaultValues.defaultReportFile.createNewFile();
                if (!wasSuccessful) { // per evitare i warnings
                    System.out.println("was not successful.");
                }
                FileOutputStream fos = new FileOutputStream(DefaultValues.defaultAvatar);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {    // altrimenti richiedo i permessi e continuo ricorsivamente :) - fuck you utente
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            addDefaultItems();
        }
    }

    /* Controllo che i permessi siano stati accettati */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.PERMISSION_REQUEST_CODE) {
            if ((grantResults.length > 0)
                    && (grantResults[0] +
                    grantResults[1]
                    == PackageManager.PERMISSION_GRANTED)) {
                addDefaultItems();
            }
        }
    }
}
