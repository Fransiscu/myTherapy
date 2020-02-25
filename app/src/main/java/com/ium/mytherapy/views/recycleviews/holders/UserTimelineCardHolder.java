package com.ium.mytherapy.views.recycleviews.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.views.recycleviews.itemclicklisteners.UserTimelineClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserTimelineCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public RelativeLayout layout;
    public View verticalLine, timelineDot;
    public TextView medicineTime, medicineName;
    public ImageView medicineStatus;
    private UserTimelineClickListener userTimelineClickListener;

    public UserTimelineCardHolder(@NonNull View itemView) {
        super(itemView);
        this.layout = itemView.findViewById(R.id.usertimeline_layout);
        this.medicineName = itemView.findViewById(R.id.timeline_nome_medicina);
        this.medicineTime = itemView.findViewById(R.id.timeline_ora_medicina);
        this.verticalLine = itemView.findViewById(R.id.timeline_vertical_line);
        this.timelineDot = itemView.findViewById(R.id.timeline_dot);
        this.medicineStatus = itemView.findViewById(R.id.timeline_status);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.userTimelineClickListener.onItemClickListener(view, getLayoutPosition());
    }

    public void setUserTimelineClickListener(UserTimelineClickListener itemClick) {
        this.userTimelineClickListener = itemClick;
    }
}
