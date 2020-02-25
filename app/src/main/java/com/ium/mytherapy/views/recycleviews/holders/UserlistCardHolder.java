package com.ium.mytherapy.views.recycleviews.holders;

import android.view.View;
import android.widget.TextView;

import com.ium.mytherapy.R;
import com.ium.mytherapy.views.recycleviews.itemclicklisteners.UserlistItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserlistCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView avatar;
    public TextView mTitle;
    private UserlistItemClickListener userlistItemClickListener;

    public UserlistCardHolder(@NonNull View itemView) {
        super(itemView);
        this.avatar = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.userlistItemClickListener.onItemClickListener(view, getLayoutPosition());
    }

    public void setUserlistItemClickListener(UserlistItemClickListener itemClick) {
        this.userlistItemClickListener = itemClick;
    }
}
