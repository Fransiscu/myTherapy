package com.ium.mytherapy.controller;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class SupervisorHomeActivity extends AppCompatActivity {

    public MaterialCardView cardview;
    TextView textview;
    LinearLayout linearLayout;
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_supervisore);
        linearLayout = findViewById(R.id.home_supervisore_view);

        List<User> list = new ArrayList<>();

        User user = new User();
        user.setNome("John");
        user.setCognome("Smith");

        User more = new User();
        more.setNome("Lorenzo");
        more.setCognome("Piana");

        list.add(user);
        list.add(more);

        cardview = new MaterialCardView(this);
        imageView = new CircleImageView(this);



    }

}
