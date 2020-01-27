package com.ium.mytherapy.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.controller.UserManagementActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    private Context context;
    private ArrayList<User> models;

    public CardAdapter(Context context, ArrayList<User> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder cardHolder, int position) {
        cardHolder.mTitle.setText(String.format("%s %s", models.get(position).getNome(), models.get(position).getCognome()));
        cardHolder.avatar.setImageResource(models.get(position).getAvatar());

        cardHolder.setItemClickListener((v, position1) -> {
            String name = models.get(position1).getNome() + " " + models.get(position1).getCognome();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) cardHolder.avatar.getDrawable();

            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] bytes = stream.toByteArray();

            Intent intent = new Intent(context, UserManagementActivity.class);
            intent.putExtra("nome", name);
            intent.putExtra("avatar", bytes);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
