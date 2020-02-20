package com.ium.mytherapy.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ium.mytherapy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MedicineTimelineCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView medicineTime, medicineName;
    ImageView notif, checks;
    MedicineTimelineClickListener medicineTimelineClickListener;

    MedicineTimelineCardHolder(@NonNull View itemView) {
        super(itemView);
        this.medicineName = itemView.findViewById(R.id.nome_medicina);
        this.medicineTime = itemView.findViewById(R.id.orario_medicina);
        this.notif = itemView.findViewById(R.id.notifica);
        this.checks = itemView.findViewById(R.id.status_terapia);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        this.medicineTimelineClickListener.onItemClickListener(view, getLayoutPosition());
    }

    void setMedicineListClickListener(MedicineTimelineClickListener itemClick) {
        this.medicineTimelineClickListener = itemClick;
    }
}
