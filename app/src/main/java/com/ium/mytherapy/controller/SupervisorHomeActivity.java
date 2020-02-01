package com.ium.mytherapy.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
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
    MaterialButton addUser, logout;
    ArrayList<User> list;
    ArrayList<User> check;

    public static ArrayList<User> getMyList() throws IOException {
        ArrayList<User> users;
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path.getAbsolutePath() + "/myTherapy/");

        /* Valori d'esempio */
/*
        User user = new User();
        user.setNome("John");
        user.setCognome("Smith");
        user.setUserId(0);
        users.add(user);

        user = new User();
        user.setNome("Lorenzo");
        user.setCognome("Piana");
        user.setUserId(1);
        users.add(user);

        user = new User();
        user.setNome("Francesco");
        user.setCognome("Soru");
        user.setUserId(2);
        users.add(user);

        user = new User();
        user.setNome("Chiara");
        user.setCognome("Soru");
        user.setUserId(3);
        users.add(user);

        user = new User();
        user.setNome("Alberto");
        user.setCognome("Usala");
        user.setUserId(4);
        users.add(user);

        user = new User();
        user.setNome("Michela");
        user.setCognome("Pinna");
        user.setUserId(5);
        users.add(user);

        user = new User();
        user.setNome("Laura");
        user.setCognome("Soru");
        user.setUserId(6);
        users.add(user);

        user = new User();
        user.setNome("Federico");
        user.setCognome("Carcangiu");
        user.setUserId(7);
        users.add(user);

        user = new User();
        user.setNome("Luca");
        user.setCognome("piddia");
        user.setUserId(8);
        users.add(user);

        user = new User();
        user.setNome("Gianluca");
        user.setCognome("Mei");
        user.setUserId(9);
        users.add(user);

        user = new User();
        user.setNome("Francesco");
        user.setCognome("Piredda");
        user.setUserId(10);
        users.add(user);

        for (int i = 0; i < users.size(); i++) {
            File file = new File(dir + "/avatar_" + users.get(i).getUserId() + ".jpeg");
            File defaultAvatar = new File(dir + "/default.jpg");
            if (file.exists()) {
                users.get(i).setAvatar(file.toString());
            } else {
                users.get(i).setAvatar(defaultAvatar.toString());
            }
        }*/

        users = UserFactory.getInstance().getUsers();
        Collections.sort(users);

        for (int i = 0; i < users.size(); i++) {
            File file = new File(dir + "/avatar_" + users.get(i).getUserId() + ".jpeg");
            File defaultAvatar = new File(dir + "/default.jpg");
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
            check = getMyList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (check != null) {
            setContentView(R.layout.activity_home_supervisore);
            cardRecyclerView = findViewById(R.id.usersListRecyclerView);
            cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            try {
                list = getMyList();
                cardAdapter = new CardAdapter(this, list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cardRecyclerView.setAdapter(cardAdapter);
        } else {
            noUser = findViewById(R.id.no_user_found);
            noUser.setVisibility(View.VISIBLE);
        }

        addUser = findViewById(R.id.aggiungi_utenti_button);
        addUser.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivityForResult(newActivity, 11);
//            finish();
        });

//        cardAdapter.notifyDataSetChanged();

        logout = findViewById(R.id.supervisore_logout_button);
        logout.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle("LOGOUT")
                .setMessage("Sei sicuro di voler fare il logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    Intent backToHome = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(backToHome);
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            cardAdapter = new CardAdapter(this, list);
            cardRecyclerView.setAdapter(cardAdapter);
            cardAdapter.notifyDataSetChanged();
        }
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

}
