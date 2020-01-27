package com.ium.mytherapy.controller;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.CardAdapter;
import com.ium.mytherapy.model.User;

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
        setContentView(R.layout.activity_home_supervisore);
        addUser = findViewById(R.id.aggiungi_utenti_button);

        cardRecyclerView = findViewById(R.id.recyclerView);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardAdapter = new CardAdapter(this, getMyList());
        cardRecyclerView.setAdapter(cardAdapter);

        addUser.setOnClickListener(view -> {

        });

    }

    private ArrayList<User> getMyList() {
        ArrayList<User> users = new ArrayList<>();

        User user = new User();
        user.setNome("John");
        user.setCognome("Smith");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Lorenzo");
        user.setCognome("Piana");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Francesco");
        user.setCognome("Soru");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Chiara");
        user.setCognome("Soru");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Alberto");
        user.setCognome("Usala");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Michela");
        user.setCognome("Pinna");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Laura");
        user.setCognome("Soru");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Federico");
        user.setCognome("Carcangiu");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Luca");
        user.setCognome("piddia");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);

        user = new User();
        user.setNome("Gianluca");
        user.setCognome("Mei");
        user.setAvatar(R.drawable.avatardefault);
        users.add(user);


        return users;
    }

}
