package com.ium.mytherapy.views.recycleviews.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.views.recycleviews.itemclicklisteners.MedicineListClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MedicinelistCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView medicineName;
    public ImageView delete, edit;
    private MedicineListClickListener medicineListClickListener;

    public MedicinelistCardHolder(@NonNull View itemView) {
        super(itemView);
        this.medicineName = itemView.findViewById(R.id.nome_modifica_medicina);
        this.delete = itemView.findViewById(R.id.cancella_terapia);
        this.edit = itemView.findViewById(R.id.modifica_medicina);
    }

    @Override
    public void onClick(View view) {
        // do nothing
    }

    void setMedicineListClickListener(MedicineListClickListener itemClick) {
        this.medicineListClickListener = itemClick;
    }

    public MedicineListClickListener getMedicineListClickListener() {
        return medicineListClickListener;
    }
}
