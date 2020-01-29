package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.button.MaterialButton;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.CardAdapter;
import com.ium.mytherapy.model.User;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SupervisorHomeActivity extends AppCompatActivity {
    RecyclerView cardRecyclerView;
    CardAdapter cardAdapter;
    MaterialButton addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<User> check = getMyList();

        if (check != null) {
            setContentView(R.layout.activity_home_supervisore);
            cardRecyclerView = findViewById(R.id.usersListRecyclerView);
            cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            cardAdapter = new CardAdapter(this, getMyList());
            cardRecyclerView.setAdapter(cardAdapter);
        } else {
            setContentView(R.layout.list_empty_view);
        }

        addUser = findViewById(R.id.aggiungi_utenti_button);
        addUser.setOnClickListener(view -> {
            Intent newActivity = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivity(newActivity);
        });

        cardAdapter.notifyDataSetChanged();

    }

    private ArrayList<User> getMyList() {
        ArrayList<User> users = new ArrayList<>();
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path.getAbsolutePath() + "/myTherapy/");

        User user = new User();
        user.setNome("John");
        user.setCognome("Smith");
        users.add(user);

        user = new User();
        user.setNome("Lorenzo");
        user.setCognome("Piana");
        users.add(user);

        user = new User();
        user.setNome("Francesco");
        user.setCognome("Soru");
        users.add(user);

        user = new User();
        user.setNome("Chiara");
        user.setCognome("Soru");
        users.add(user);

        user = new User();
        user.setNome("Alberto");
        user.setCognome("Usala");
        users.add(user);

        user = new User();
        user.setNome("Michela");
        user.setCognome("Pinna");
        users.add(user);

        user = new User();
        user.setNome("Laura");
        user.setCognome("Soru");
        users.add(user);

        user = new User();
        user.setNome("Federico");
        user.setCognome("Carcangiu");
        users.add(user);

        user = new User();
        user.setNome("Luca");
        user.setCognome("piddia");
        users.add(user);

        user = new User();
        user.setNome("Gianluca");
        user.setCognome("Mei");
        users.add(user);

        for (User item : users) {
            item.setId(users.indexOf(item));
            try {
                item.setAvatar(dir + "/avatar_" + item.getId() + ".jpeg");
//                Toast.makeText(getBaseContext(), item.getAvatar(), Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                item.setAvatar(null);
            }
        }

        return users;
    }

}
