package com.ium.mytherapy.views.recycleviews.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ium.mytherapy.R;
import com.ium.mytherapy.controller.UserManagementActivity;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.views.recycleviews.holders.UserlistCardHolder;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserlistCardAdapter extends RecyclerView.Adapter<UserlistCardHolder> {

    private Context context;
    private ArrayList<User> models;
    private static String USER_INTENT = "user";
    public static String USERS_INTENT = "userList";
    public static String USER_KEY = "userKey";
    private User user;

    public UserlistCardAdapter(Context context, ArrayList<User> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public UserlistCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisore_users_card, null);
        return new UserlistCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserlistCardHolder userlistCardHolder, int position) {
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path.getAbsolutePath() + "/myTherapy/");
        File file = new File(dir + "/avatar_" + models.get(position).getUserId() + ".jpeg");
        userlistCardHolder.mTitle.setText(String.format("%s %s", models.get(position).getNome(), models.get(position).getCognome()));

        /* Aggiorno l'immagine in caso sia cambiata dalla default */
        if (!file.exists()) {
            userlistCardHolder.avatar.setImageDrawable(Drawable.createFromPath(models.get(position).getAvatar()));
        } else {
            userlistCardHolder.avatar.setImageURI(Uri.fromFile(file));
        }

        userlistCardHolder.setUserlistItemClickListener((v, position1) -> {
            /* Mando a UserManagementActivity */
            user = models.get(position);
            Intent intent = new Intent(context, UserManagementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(USER_KEY, user.getUserId());
            bundle.putParcelable(USER_INTENT, user);
            intent.putExtras(bundle);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
//            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}