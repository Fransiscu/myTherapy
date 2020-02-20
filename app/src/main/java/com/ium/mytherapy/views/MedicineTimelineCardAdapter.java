package com.ium.mytherapy.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.controller.UserManagementActivity;
import com.ium.mytherapy.model.Medicina;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineTimelineCardAdapter extends RecyclerView.Adapter<MedicineTimelineCardHolder> {

    private Context context;
    private ArrayList<Medicina> models;
    private Medicina medicina;

    public MedicineTimelineCardAdapter(Context context, ArrayList<Medicina> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MedicineTimelineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisore_timeline_card, null);
        return new MedicineTimelineCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineTimelineCardHolder medicineTimelineCardHolder, int position) {

        medicineTimelineCardHolder.medicineTime.setText(models.get(position).getOra());
        medicineTimelineCardHolder.medicineName.setText(models.get(position).getNome());


        medicineTimelineCardHolder.setMedicineListClickListener((v, position1) -> {
            /* Mando a UserManagementActivity */
            user = models.get(position);
            Intent intent = new Intent(context, UserManagementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(USER_KEY, user.getUserId());
            bundle.putParcelable(USER_INTENT, user);
            intent.putExtras(bundle);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
//            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
