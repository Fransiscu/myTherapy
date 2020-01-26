package com.ium.mytherapy.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ium.mytherapy.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {

    private List<User> userList;
    private LayoutInflater inflater;

    public CardViewAdapter(Context context, List<User> userList) {
        inflater = LayoutInflater.from(context);
        this.userList = userList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.material_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void onBindViewHolder(CardViewHolder holder, int position) {
        User current = userList.get(position);
        holder.setData(current, position);
//        holder.setListeners();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private CircleImageView avatar, delete;
        private int position;
        private User currentUser;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.card_textview);
            avatar = itemView.findViewById(R.id.card_image);
            delete = itemView.findViewById(R.id.card_delete);
        }

        public void setData(User current, int position) {
            this.name.setText(String.format("%s %s", currentUser.getNome(), currentUser.getCognome()));
            this.avatar.setImageResource(R.drawable.avatardefault);
            this.delete.setImageResource(android.R.drawable.ic_delete);
            this.position = position;
            this.currentUser = currentObject;
        }
    }
}
