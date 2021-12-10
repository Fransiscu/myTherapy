package com.ium.mytherapy.views.recycleviews.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.controller.MedicineStatusActivity;
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.views.recycleviews.holders.UserTimelineCardHolder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("deprecation")
public class UserTimelineCardAdapter extends RecyclerView.Adapter<UserTimelineCardHolder> {

    private final Context context;
    private final ArrayList<Medicine> models;

    public UserTimelineCardAdapter(Context context, ArrayList<Medicine> models) {
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
        userTimelineCardHolder.medicineTime.setText(models.get(position).getTimeHour());
        userTimelineCardHolder.medicineName.setText(models.get(position).getName());

        /* Calculating screen size to pick best card size */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int baseHeight = displayMetrics.heightPixels;

        /* Setting colors for taken or not */
        if (models.get(position).isTaken()) {
            setDone(userTimelineCardHolder);
        } else {
            setNotDone(userTimelineCardHolder);
        }

        /* Changing card's height depending on how many medicines per day */
        if (models.size() >= 3) {
            userTimelineCardHolder.layout.getLayoutParams().height = baseHeight / (models.size() + 2);
        }

        /* timeline item click listener */
        userTimelineCardHolder.setUserTimelineClickListener((v, position1) -> {
            v.setSelected(true);
            Bundle bundle = new Bundle();
            bundle.putParcelable(DefaultValues.MEDICINA, models.get(position));
            Intent therapy = new Intent(context.getApplicationContext(), MedicineStatusActivity.class);
            therapy.putExtras(bundle);
            context.startActivity(therapy);
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        });

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
