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
import com.ium.mytherapy.controller.EditMedicineActivity;
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.views.recycleviews.holders.MedicinelistCardHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("deprecation")
public class MedicinelistCardAdapter extends RecyclerView.Adapter<MedicinelistCardHolder> {

    private final Context context;
    private final ArrayList<Medicine> models;
    private final User user;
    private final MedicineTimelineCardAdapter medicineTimelineCardAdapter;  // adapter needed to be able to update the daily timeline

    public MedicinelistCardAdapter(Context context, User user, ArrayList<Medicine> models, MedicineTimelineCardAdapter medicineTimelineCardAdapter) {
        this.context = context;
        this.models = models;
        this.user = user;
        this.medicineTimelineCardAdapter = medicineTimelineCardAdapter;
    }

    @NonNull
    @Override
    public MedicinelistCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_supervisore_lista_medicine_card, null);
        return new MedicinelistCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicinelistCardHolder medicineListCardHolder, int position) {
        medicineListCardHolder.medicineName.setText(models.get(position).getName());

        medicineListCardHolder.delete.setBackgroundResource(0);
        medicineListCardHolder.delete.setImageDrawable(context.getResources().getDrawable(R.drawable.delete));

        medicineListCardHolder.edit.setBackgroundResource(0);
        medicineListCardHolder.edit.setImageDrawable(context.getResources().getDrawable(R.drawable.edit));

        medicineListCardHolder.delete.setOnClickListener(view -> new MaterialAlertDialogBuilder(context)
                .setTitle("Rimozione terapia")
                .setMessage("Sicuro di voler rimuovere la terapia associata all'utente?")
                .setCancelable(false)
                .setPositiveButton("Rimuovi", (dialogInterface, i) -> {
                    MedicineFactory.getInstance().removeMedicine(models.get(position), user);
                    models.remove(position);
                    this.notifyItemRemoved(position);
                    this.notifyItemRangeRemoved(0, models.size() - 1);
                    medicineTimelineCardAdapter.notifyItemRemoved(position);
                    medicineTimelineCardAdapter.notifyItemRangeChanged(0, models.size() - 1);
                    Toast.makeText(context, "Terapia rimossa", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .show());

        medicineListCardHolder.edit.setOnClickListener(view -> {
            Intent editTherapyIntent = new Intent(context, EditMedicineActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("MEDICINA", models.get(position));
            bundle.putParcelable("user", user);
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
