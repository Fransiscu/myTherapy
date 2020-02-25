package com.ium.mytherapy.views.recycleviews.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.views.recycleviews.itemclicklisteners.UserTimelineClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserTimelineCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View verticalLine;
    public View timelineDot;
    public TextView medicineTime;
    public TextView medicineName;
    public ImageView medicineStatus;
    private UserTimelineClickListener userTimelineClickListener;

    public UserTimelineCardHolder(@NonNull View itemView) {
        super(itemView);
        this.medicineName = itemView.findViewById(R.id.timeline_ora_medicina);
        this.medicineTime = itemView.findViewById(R.id.timeline_nome_medicina);
        this.verticalLine = itemView.findViewById(R.id.timeline_vertical_line);
        this.timelineDot = itemView.findViewById(R.id.timeline_dot);
        this.medicineStatus = itemView.findViewById(R.id.timeline_status);
    }

    @Override
    public void onClick(View view) {
//        do nothing
        this.userTimelineClickListener.onItemClickListener(view, getLayoutPosition());
    }

    void setUserTimelineListClickListener(UserTimelineClickListener itemClick) {
        this.userTimelineClickListener = itemClick;
    }
}
