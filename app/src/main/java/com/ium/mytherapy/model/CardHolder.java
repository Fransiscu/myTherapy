package com.ium.mytherapy.model;

import android.view.View;
import android.widget.TextView;

import com.ium.mytherapy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

class CardHolder extends RecyclerView.ViewHolder {

    CircleImageView mImaeView;
    TextView mTitle, mDes;

    CardHolder(@NonNull View itemView) {
        super(itemView);
        this.mImaeView = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);
    }
}
