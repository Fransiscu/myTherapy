package com.ium.mytherapy.views.recycleviews.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.views.recycleviews.itemclicklisteners.MedicineTimelineClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicineTimelineCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView medicineTime;
    public TextView medicineName;
    public ImageView notif;
    public ImageView checks;
    private MedicineTimelineClickListener medicineTimelineClickListener;

    public MedicineTimelineCardHolder(@NonNull View itemView) {
        super(itemView);
        this.medicineName = itemView.findViewById(R.id.nome_medicina);
        this.medicineTime = itemView.findViewById(R.id.orario_medicina);
        this.notif = itemView.findViewById(R.id.notifica);
        this.checks = itemView.findViewById(R.id.status_terapia);
    }

    @Override
    public void onClick(View view) {
        // do nothing
    }

    void setMedicineListClickListener(MedicineTimelineClickListener itemClick) {
        this.medicineTimelineClickListener = itemClick;
    }
}
