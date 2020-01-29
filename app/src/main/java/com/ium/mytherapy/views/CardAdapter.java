package com.ium.mytherapy.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.controller.UserManagementActivity;
import com.ium.mytherapy.model.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    private Context context;
    private ArrayList<User> models;
    public static String USER_INTENT = "user";
    public static String USERS_INTENT = "userList";
    public static String USER_KEY = "userKey";
    private User user;

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
        cardHolder.avatar.setImageDrawable(Drawable.createFromPath(models.get(position).getAvatar()));

        cardHolder.setItemClickListener((v, position1) -> {
            /* Mando a UserManagementActivity */
            user = models.get(position);
            Intent intent = new Intent(context, UserManagementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(USER_KEY, user.getUserId());
            bundle.putParcelableArrayList(USERS_INTENT, models);
            bundle.putParcelable(USER_INTENT, user);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
