package com.ium.mytherapy.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;
import com.ium.mytherapy.utils.exceptions.NoMedicinesFoundException;

import java.util.Objects;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);
        String message = intent.getStringExtra("toastMessage");
        try {
            Bundle bundle = intent.getExtras();
            Medicina medicina = Objects.requireNonNull(bundle).getParcelable("medicine");
            Objects.requireNonNull(medicina).setPresa(true);
            MedicinaFactory.getInstance().changePresa(medicina);
        } catch (NoMedicinesFoundException e) {
            throw new NoMedicinesFoundException(e, context);
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(manager).cancel(notificationId);
    }
}
