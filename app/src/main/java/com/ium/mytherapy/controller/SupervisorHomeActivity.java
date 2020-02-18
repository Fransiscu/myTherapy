package com.ium.mytherapy.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.model.UserReport;
import com.ium.mytherapy.model.UserReportFactory;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.views.CardAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SupervisorHomeActivity extends AppCompatActivity {
    RecyclerView cardRecyclerView;
    CardAdapter cardAdapter;
    TextView noUser;
    ImageView notifications;
    MaterialButton addUser, logout;
    ArrayList<User> list, check;
    UserReport report;

    /* Prendo lista degli utenti */
    public static ArrayList<User> getUsers() throws IOException {
        ArrayList<User> users;

        users = UserFactory.getInstance().getUsers();
        Collections.sort(users);

        for (int i = 0; i < users.size(); i++) {
            File file = new File(DefaultValues.dir + "/avatar_" + users.get(i).getUserId() + ".jpeg");
            File defaultAvatar = new File(DefaultValues.dir + "/default.jpg");
            if (file.exists()) {
                users.get(i).setAvatar(file.toString());
            } else {
                users.get(i).setAvatar(defaultAvatar.toString());
            }
        }

        return users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check = null;
        try {
            check = getUsers();    // controllo che la mia lista utenti presa da getUsers() non sia null
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (check != null) {
            setContentView(R.layout.activity_home_supervisore);
            cardRecyclerView = findViewById(R.id.usersListRecyclerView);
            cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            notifications = findViewById(R.id.supervisor_notification);
            addUser = findViewById(R.id.aggiungi_utenti_button);
            logout = findViewById(R.id.supervisore_logout_button);

            Runnable checkNotifications = () -> {
                try {
                    if (UserReportFactory.getInstance().checkReports() && !UserReportFactory.getInstance().checkRead()) {
                        notifications.setVisibility(View.VISIBLE);
                        report = UserReportFactory.getInstance().getReportFromFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            checkNotifications.run();

            /* Listener per l'icona delle notifiche */
            /* Apro una finestrella con l'ultima non letta */
            notifications.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                    .setTitle("Richiesta di supporto")
                    .setMessage("Terapia in questione:" + report.getMedicina())
                    .setMessage(report.getErrorMessage())
                    .setCancelable(false)
                    .setPositiveButton("Segna come letto", (dialogInterface, i) -> {
                        try {
                            UserReportFactory.getInstance().setChecked();
                            notifications.setVisibility(View.INVISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getBaseContext(), "Report segnato come letto", Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton("Annulla", (dialogInterface, i) -> {
                    })
                    .show());

            try {
                list = getUsers();
                cardAdapter = new CardAdapter(this, list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cardRecyclerView.setAdapter(cardAdapter);
        } else {
            noUser = findViewById(R.id.no_user_found);
            noUser.setVisibility(View.VISIBLE);
        }

        /* Listener tasto aggiunta utenti */
        addUser.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivityForResult(newActivity, 11);
        });

        /* Listener tasto logout */
        logout.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("LOGOUT")
                .setMessage("Sei sicuro di voler fare il logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    Intent backToHome = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(backToHome);
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                    preferencesEditor.clear();
                    preferencesEditor.apply();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

    }

    @Override     // non serve tornando indietro ma solo confermando
    public void onResume() {
        super.onResume();
        if (check != null) {
            cardAdapter = new CardAdapter(this, list);
            cardRecyclerView.setAdapter(cardAdapter);
            cardAdapter.notifyDataSetChanged();
        }
    }

    /* Aggiorno schede utenti quando torno da AddUserACtivity */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            cardAdapter = new CardAdapter(this, list);
            cardRecyclerView.setAdapter(cardAdapter);
            cardAdapter.notifyDataSetChanged(); // aggiorno con questa method
        }
    }

}
