package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    private Context c;
    private ArrayList<User> models;

    MyAdapter(Context c, ArrayList<User> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        myHolder.mTitle.setText(models.get(position).getNome());
        myHolder.mDes.setText(models.get(position).getCognome());
        myHolder.mImaeView.setImageResource(models.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
