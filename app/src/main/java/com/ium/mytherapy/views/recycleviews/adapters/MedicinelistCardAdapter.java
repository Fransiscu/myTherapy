package com.ium.mytherapy.views.recycleviews.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;
import com.ium.mytherapy.controller.EditTherapyActivity;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.views.recycleviews.holders.MedicinelistCardHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("deprecation")
public class MedicinelistCardAdapter extends RecyclerView.Adapter<MedicinelistCardHolder> {

    private Context context;
    private ArrayList<Medicina> models;

    public MedicinelistCardAdapter(Context context, ArrayList<Medicina> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MedicinelistCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_supervisore_lista_terapie_card, null);
        return new MedicinelistCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicinelistCardHolder medicineListCardHolder, int position) {
        medicineListCardHolder.medicineName.setText(models.get(position).getNome());
        /* Qui posso cambiare le icone se necessario */

        medicineListCardHolder.delete.setBackgroundResource(0);
        medicineListCardHolder.delete.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));

        medicineListCardHolder.edit.setBackgroundResource(0);
        medicineListCardHolder.edit.setImageDrawable(context.getResources().getDrawable(R.drawable.edit));

        medicineListCardHolder.delete.setOnClickListener(view -> new MaterialAlertDialogBuilder(context)
                .setTitle("Rimozione terapia")
                .setMessage("Sicuro di voler rimuovere la terapia associata all'utente?")
                .setCancelable(false)
                .setPositiveButton("Rimuovi", (dialogInterface, i) -> Toast.makeText(context, "Terapia rimossa", Toast.LENGTH_LONG).show())
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        medicineListCardHolder.edit.setOnClickListener(view -> {
            Intent editTherapyIntent = new Intent(context, EditTherapyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("MEDICINA", models.get(position));
            editTherapyIntent.putExtras(bundle);
            context.startActivity(editTherapyIntent);
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}