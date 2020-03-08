package com.ium.mytherapy.views.recycleviews.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.views.recycleviews.holders.MedicineTimelineCardHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("deprecation")
public class MedicineTimelineCardAdapter extends RecyclerView.Adapter<MedicineTimelineCardHolder> {

    private Context context;
    private ArrayList<Medicina> models;
    private User user;

    public MedicineTimelineCardAdapter(Context context, User user, ArrayList<Medicina> models) {
        this.context = context;
        this.models = models;
        this.user = user;
    }

    @NonNull
    @Override
    public MedicineTimelineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_supervisore_timeline_card, null);
        return new MedicineTimelineCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineTimelineCardHolder medicineTimelineCardHolder, int position) {

        medicineTimelineCardHolder.medicineTime.setText(models.get(position).getOra());
        medicineTimelineCardHolder.medicineName.setText(models.get(position).getNome());

        /* Setto le varie icone */
        setNotif(medicineTimelineCardHolder, position);
        setStatus(medicineTimelineCardHolder, position);

        /* Listeners icone */
        medicineTimelineCardHolder.notif.setOnClickListener(view -> changeNotif(medicineTimelineCardHolder, position));
        medicineTimelineCardHolder.checks.setOnClickListener(view -> changeStatus(medicineTimelineCardHolder, position));

    }

    private void setStatus(MedicineTimelineCardHolder medicineTimelineCardHolder, int position) {
        if (models.get(position).isPresa()) {
            medicineTimelineCardHolder.checks.setBackgroundResource(0);
            medicineTimelineCardHolder.checks.setImageDrawable(context.getResources().getDrawable(R.drawable.timeline_done));
        } else {
            medicineTimelineCardHolder.checks.setBackgroundResource(0);
            medicineTimelineCardHolder.checks.setImageDrawable(context.getResources().getDrawable(R.drawable.timeline_not_done));
        }
    }

    private void setNotif(MedicineTimelineCardHolder medicineTimelineCardHolder, int position) {
        if (models.get(position).isNotifEnabled()) {
            medicineTimelineCardHolder.notif.setBackgroundResource(0);
            medicineTimelineCardHolder.notif.setImageDrawable(context.getResources().getDrawable(R.drawable.notification_active));
        } else {
            medicineTimelineCardHolder.notif.setBackgroundResource(0);
            medicineTimelineCardHolder.notif.setImageDrawable(context.getResources().getDrawable(R.drawable.notification_inactive));
        }
    }

    private void changeNotif(MedicineTimelineCardHolder medicineTimelineCardHolder, int position) {
        models.get(position).setNotifEnabled(!models.get(position).isNotifEnabled());   // toggle notifiche
        setNotif(medicineTimelineCardHolder, position);
        MedicinaFactory.getInstance().changeNotif(models.get(position), user);
        Toast.makeText(context, "Notifiche modificate", Toast.LENGTH_LONG).show();
    }

    private void changeStatus(MedicineTimelineCardHolder medicineTimelineCardHolder, int position) {
        models.get(position).setPresa(!models.get(position).isPresa());     // toggle checks
        setStatus(medicineTimelineCardHolder, position);
        MedicinaFactory.getInstance().changePresa(models.get(position), user);
        Toast.makeText(context, "Stato modificato", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
