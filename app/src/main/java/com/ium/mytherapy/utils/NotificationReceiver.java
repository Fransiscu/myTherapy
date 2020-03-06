package com.ium.mytherapy.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;
import com.ium.mytherapy.utils.exceptions.NoMedicinesFoundException;

import java.util.Calendar;
import java.util.Objects;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);
        String message = intent.getStringExtra("toastMessage");
        try {
            Bundle bundle = intent.getExtras();
            Medicina medicina = Objects.requireNonNull(bundle).getParcelable("medicine");
            int action = intent.getIntExtra("action", 0);

            switch (action) {
                case 0:
                    Toast.makeText(context, "Codice azione sbagliato", Toast.LENGTH_LONG).show();
                case 1:
                    Calendar now = getInstance();
                    now.add(Calendar.MINUTE, 10);
                    now.getTime();
                    Objects.requireNonNull(medicina).setReminder(now.get(HOUR_OF_DAY) + ":" + now.get(MINUTE));
                    Log.d("ora", medicina.getReminder());
                    MedicinaFactory.getInstance().setReminder(medicina);
                    break;
                case 2:
                    Objects.requireNonNull(medicina).setPresa(true);
                    MedicinaFactory.getInstance().changePresa(medicina);
                    break;
                default:
                    break;
            }
        } catch (NoMedicinesFoundException e) {
            throw new NoMedicinesFoundException(e, context);
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(manager).cancel(notificationId);
    }
}
