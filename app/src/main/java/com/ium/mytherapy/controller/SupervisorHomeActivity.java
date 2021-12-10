package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
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
import com.ium.mytherapy.views.fragments.EditedScrollView;
import com.ium.mytherapy.views.recycleviews.adapters.UserlistCardAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SupervisorHomeActivity extends AppCompatActivity {

    UserlistCardAdapter userlistCardAdapter;
    RecyclerView userCardRecyclerView;
    MaterialButton addUser, logout;
    ArrayList<User> list, check;
    EditedScrollView scrollView;
    ImageView notifications;
    UserReport report;
    TextView noUser;

    /* Grabbing user list */
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
        setContentView(R.layout.supervisor_home_activity);

        // Making top bar not scroll
        scrollView = findViewById(R.id.scrollView);
        scrollView.setScrolling(false);

        notifications = findViewById(R.id.supervisor_notifications);
        addUser = findViewById(R.id.add_users_button);
        logout = findViewById(R.id.supervisor_logout_button);

        try {
            check = getUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (check != null && check.size() != 0) {
            userCardRecyclerView = findViewById(R.id.usersListRecyclerView);
            userCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

            /* Notifications button on click listener */
            notifications.setOnClickListener(view -> {
                try {
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Richiesta di supporto")
                            .setMessage(UserFactory.getInstance().getUser(report.getUserId()).toString() + "\n\n" +
                                    report.getMedicine() + "\n\n" + report.getErrorMessage())
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
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            try {
                list = getUsers();
                userlistCardAdapter = new UserlistCardAdapter(this, list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            userCardRecyclerView.setAdapter(userlistCardAdapter);
        } else {
            noUser = findViewById(R.id.no_user_found);
            noUser.setVisibility(View.VISIBLE);
        }

        /* addUser on click listener */
        addUser.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivityForResult(newActivity, 11);
        });

        /* logout on click listener */
        logout.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("LOGOUT")
                .setMessage("Sei sicuro di voler fare il logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    Intent backToHome = new Intent(getApplicationContext(), UserLoginActivity.class);
                    startActivity(backToHome);
                    SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                    preferencesEditor.clear();
                    preferencesEditor.apply();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if (check != null && check.size() != 0) {
            userlistCardAdapter = new UserlistCardAdapter(this, list);
            userCardRecyclerView.setAdapter(userlistCardAdapter);
            userlistCardAdapter.notifyDataSetChanged();
        }
    }

    /* Updating user cards when back in AddUserACtivity */
    @SuppressLint("NotifyDataSetChanged")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            userlistCardAdapter = new UserlistCardAdapter(this, list);
            userCardRecyclerView.setAdapter(userlistCardAdapter);
            userlistCardAdapter.notifyDataSetChanged();
        }
    }

}
