package com.ium.mytherapy.controller;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.CardViewAdapter;
import com.ium.mytherapy.model.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SupervisorHomeActivity extends AppCompatActivity {

    public MaterialCardView cardview;
    TextView textview;
    LinearLayout linearLayout;
    CircleImageView imageView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_supervisore);
        linearLayout = findViewById(R.id.home_supervisore_view);

        recyclerView = findViewById(R.id.recycler_material_card);
        CardViewAdapter adapter = new CardViewAdapter(this, User.getObjectList());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.setItemAnimator(new DefaultItemAnimator());


//        List<User> list = new ArrayList<>();
//
//        cardview = new MaterialCardView(this);
//        imageView = new CircleImageView(this);



    }

}
