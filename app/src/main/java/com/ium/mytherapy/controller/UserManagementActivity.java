package com.ium.mytherapy.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import com.ium.mytherapy.R;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserManagementActivity extends AppCompatActivity {

    TextView nome;
    CircleImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws NullPointerException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_utente);

        nome = findViewById(R.id.titleProfile);
        image = findViewById(R.id.imageProfile);

        Intent intent = getIntent();
        String nomeString = intent.getStringExtra("nome");

        byte[] nBytes = getIntent().getByteArrayExtra("avatar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(nBytes, 0, Objects.requireNonNull(nBytes).length);

        nome.setText(nomeString);
        image.setImageBitmap(bitmap);
    }
}
