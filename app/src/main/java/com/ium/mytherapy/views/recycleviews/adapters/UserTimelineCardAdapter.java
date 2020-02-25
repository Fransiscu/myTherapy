package com.ium.mytherapy.views.recycleviews.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.views.recycleviews.holders.UserTimelineCardHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class UserTimelineCardAdapter extends RecyclerView.Adapter<UserTimelineCardHolder> {

    private Context context;
    private ArrayList<Medicina> models;

    public UserTimelineCardAdapter(Context context, ArrayList<Medicina> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public UserTimelineCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_user_timeline_card, null);
        return new UserTimelineCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTimelineCardHolder userTimelineCardHolder, int position) {
        userTimelineCardHolder.medicineTime.setText(models.get(position).getOra());
        userTimelineCardHolder.medicineName.setText(models.get(position).getNome());

        /* Setto colori a seconda se prese o no */
        if (models.get(position).isPresa()) {
            setDone(userTimelineCardHolder);
        } else {
            setNotDone(userTimelineCardHolder);
        }

    }

    private void setDone(UserTimelineCardHolder userTimelineCardHolder) {
        userTimelineCardHolder.verticalLine.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));

        userTimelineCardHolder.timelineDot.setBackground(context.getResources().getDrawable(R.drawable.dot_ok));

        userTimelineCardHolder.medicineStatus.setImageDrawable(context.getDrawable(R.drawable.timeline_done));
    }

    private void setNotDone(UserTimelineCardHolder userTimelineCardHolder) {
        userTimelineCardHolder.verticalLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        userTimelineCardHolder.timelineDot.setBackground(context.getResources().getDrawable(R.drawable.dot));

        userTimelineCardHolder.medicineStatus.setImageDrawable(context.getDrawable(R.drawable.timeline_not_done));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
