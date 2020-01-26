package com.ium.mytherapy.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

        ArrayList<User> list = new ArrayList<User>();

        User user = new User();
        user.setNome("John");
        user.setCognome("Smith");

        list.add(user);

        cardview = new MaterialCardView(this);
        imageView = new CircleImageView(this);

        ViewGroup.LayoutParams layoutparams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        cardview.setLayoutParams(layoutparams);

        cardview.setMinimumHeight(200);

        cardview.setRadius(8);

        cardview.setPadding(25, 25, 25, 25);

        cardview.setCardBackgroundColor(Color.WHITE);

        textview = new TextView(getApplicationContext());

        textview.setLayoutParams(layoutparams);

        textview.setText(String.format("%s %s", user.getNome(), user.getCognome()));

        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

        textview.setPadding(30, 40, 20, 0);

        textview.setGravity(Gravity.CENTER);

        cardview.addView(textview);

        linearLayout.addView(cardview);



    }


}
