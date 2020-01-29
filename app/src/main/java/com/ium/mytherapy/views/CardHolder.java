package com.ium.mytherapy.views;

import android.view.View;
import android.widget.TextView;

import com.ium.mytherapy.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView avatar;
    TextView mTitle;
    private ItemClickListener itemClickListener;

    CardHolder(@NonNull View itemView) {
        super(itemView);
        this.avatar = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        this.itemClickListener.onItemClickListener(view, getLayoutPosition());
    }

    void setItemClickListener(ItemClickListener itemClick) {
        this.itemClickListener = itemClick;
    }
}
