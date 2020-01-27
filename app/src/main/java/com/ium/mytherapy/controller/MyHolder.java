package com.ium.mytherapy.controller;

import android.view.View;
import android.widget.TextView;

import com.ium.mytherapy.R;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

class MyHolder extends RecyclerView.ViewHolder {

    CircleImageView mImaeView;
    TextView mTitle, mDes;

    MyHolder(@NonNull View itemView) {
        super(itemView);
        this.mImaeView = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);
    }
}
