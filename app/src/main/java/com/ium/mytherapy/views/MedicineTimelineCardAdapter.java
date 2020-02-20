package com.ium.mytherapy.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineTimelineCardAdapter extends RecyclerView.Adapter<MedicineTimelineCardHolder> {

    private Context context;
    private ArrayList<Medicina> models;

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

        /* Setto le varie icone */
        if (models.get(position).isNotifEnabled()) {
            medicineTimelineCardHolder.notif.setBackgroundResource(0);
            medicineTimelineCardHolder.notif.setImageDrawable(context.getResources().getDrawable(R.drawable.notification_inactive));
            models.get(position).setNotifEnabled(!models.get(position).isNotifEnabled());   // toggle notifiche
//            Toast.makeText(context, "Notifiche disattivate", Toast.LENGTH_LONG).show();
        } else {
            medicineTimelineCardHolder.notif.setBackgroundResource(0);
            medicineTimelineCardHolder.notif.setImageDrawable(context.getResources().getDrawable(R.drawable.notification_active));
            models.get(position).setNotifEnabled(!models.get(position).isNotifEnabled());   // toggle notifiche
//            Toast.makeText(context, "Notifiche attivate", Toast.LENGTH_LONG).show();
        }

        if (models.get(position).isPresa()) {
            medicineTimelineCardHolder.checks.setBackgroundResource(0);
            medicineTimelineCardHolder.checks.setImageDrawable(context.getResources().getDrawable(R.drawable.timeline_not_done));
            models.get(position).setPresa(!models.get(position).isPresa());     // toggle checks
//            Toast.makeText(context, "Stato modificato", Toast.LENGTH_LONG).show();
        } else {
            medicineTimelineCardHolder.checks.setBackgroundResource(0);
            medicineTimelineCardHolder.checks.setImageDrawable(context.getResources().getDrawable(R.drawable.timeline_done));
            models.get(position).setPresa(!models.get(position).isPresa());     // toggle checks
//            Toast.makeText(context, "Stato modificato", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
