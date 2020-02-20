package com.ium.mytherapy.views;

import android.view.View;
import android.widget.TextView;

import com.ium.mytherapy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

class UserlistCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView avatar;
    TextView mTitle;
    private UserlistItemClickListener userlistItemClickListener;

    UserlistCardHolder(@NonNull View itemView) {
        super(itemView);
        this.avatar = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        this.userlistItemClickListener.onItemClickListener(view, getLayoutPosition());
    }

    void setUserlistItemClickListener(UserlistItemClickListener itemClick) {
        this.userlistItemClickListener = itemClick;
    }
}
