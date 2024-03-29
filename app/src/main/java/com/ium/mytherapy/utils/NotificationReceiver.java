package com.ium.mytherapy.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.exceptions.NoMedicinesFoundException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;

// class needed for test notifications
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0);
        String message = intent.getStringExtra("toastMessage");
        try {
            Bundle bundle = intent.getExtras();
            Medicine medicine = Objects.requireNonNull(bundle).getParcelable("medicine");
            User user = Objects.requireNonNull(bundle).getParcelable("user");
            int action = intent.getIntExtra("action", 0);

            switch (action) {
                case 0:
                    Toast.makeText(context, "Codice azione sbagliato", Toast.LENGTH_LONG).show();
                case 1:
                    Calendar now = getInstance();
                    now.add(Calendar.MINUTE, 10);
                    now.getTime();
                    Objects.requireNonNull(medicine).setReminder(now.get(HOUR_OF_DAY) + ":" + now.get(MINUTE));
                    MedicineFactory.getInstance().setReminder(medicine, Objects.requireNonNull(user));
                    break;
                case 2:
                    Objects.requireNonNull(medicine).setTaken(true);
                    MedicineFactory.getInstance().changeTaken(medicine, UserFactory.getInstance().getUser(Utility.getUserIdFromSharedPreferences(context)));
                    break;
                default:
                    break;
            }
        } catch (NoMedicinesFoundException | IOException e) {
            throw new NoMedicinesFoundException(context);
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(manager).cancel(notificationId);
    }
}
