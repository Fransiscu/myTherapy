package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.model.UserReport;
import com.ium.mytherapy.model.UserReportFactory;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.utils.NotificationReceiver;
import com.ium.mytherapy.utils.exceptions.NoMedicinesFoundException;
import com.ium.mytherapy.views.fragments.EditedScrollView;
import com.ium.mytherapy.views.fragments.HelpDialogFragment;
import com.ium.mytherapy.views.recycleviews.adapters.UserTimelineCardAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("ALL")
public class UserHomeActivity extends AppCompatActivity implements HelpDialogFragment.HelpDialogListener {

    RecyclerView userTimelineRecyclerView;
    View topLine, bottomLine;
    UserTimelineCardAdapter userTimelineCardAdapter;
    MaterialButton logout, helpMe;
    List<Medicina> therapy = null;
    ArrayList<Medicina> medicineArrayList;
    EditedScrollView scrollView;
    MaterialTextView noMedicine, topMidnight, bottomMidnight;

    public static final String MEDICINA = "MEDICINE_INTENT";
    TextView todaysDate, notifTitolo,
            medName1, medName2, medName3,
            medTime1, medTime2, medTime3;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_utente);

        User user = new User();
        try {
            user = UserFactory.getInstance().getUser(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            therapy = MedicinaFactory.getInstance().getMedicinesForUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        topLine = findViewById(R.id.horizontal_top_line);
        bottomLine = findViewById(R.id.horizontal_bottom_line);

        noMedicine = findViewById(R.id.nessuna_medicina);
        topMidnight = findViewById(R.id.mezzanotte_top);
        bottomMidnight = findViewById(R.id.mezzanotte_bottom);

        // Per far si che la barra del titolo di sopra non scrolli con il resto
        scrollView = findViewById(R.id.scrollView);
        scrollView.setScrolling(false);

        /* RecyclerView per le medicine */
        userTimelineRecyclerView = findViewById(R.id.userHomeTimelineRecyclerView);
        userTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineArrayList = new ArrayList<>(therapy);
        userTimelineCardAdapter = new UserTimelineCardAdapter(this, medicineArrayList);
        userTimelineRecyclerView.setAdapter(userTimelineCardAdapter);

        /* Setto le linee orizzontali a seconda dello stato della terapia */

        if (!therapy.isEmpty() && therapy != null) {
            if (therapy.get(0).isPresa()) {
                topLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
            } else {
                topLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
            if (therapy.get(therapy.size() - 1).isPresa()) {
                bottomLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
            } else {
                bottomLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
        } else {        // eseguito se non ci sono terapie per evitare un crash & che non venga mostrato niente all'utente
            topLine.setVisibility(View.GONE);
            bottomLine.setVisibility(View.GONE);
            topMidnight.setVisibility(View.GONE);
            bottomMidnight.setVisibility(View.GONE);
            noMedicine.setVisibility(View.VISIBLE);
        }


        notifTitolo = findViewById(R.id.titolo_home_utente);

        logout = findViewById(R.id.user_logout_button);
        helpMe = findViewById(R.id.user_help);

        /* Setto la data di oggi */
        todaysDate = findViewById(R.id.data_oggi);
        Date date = new Date();
        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {     // controllo la versione di android perchè non compatibile prima di Oreo
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            todaysDate.setText(String.format("%s %d %s %d", Giorni.values()[dayOfWeek - 1], day, Mesi.values()[month - 1].toString(), year));
        }

        /* Setto listener per pulsante di logout */
        logout.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this)
                .setTitle("LOGOUT")
                .setMessage("Sei sicuro di voler fare il logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    Intent backToHome = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(backToHome);
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                    preferencesEditor.clear();
                    preferencesEditor.apply();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                    .show();
        });

        /* Listener per notifica test */
        notifTitolo.setOnClickListener(view -> {
            Runnable notificationExample = () -> {
                showNotificationExample();
            };
                notificationExample.run();
        });

        /* Listener per helpMe button */
        helpMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelpDialog();
            }
        });

    }

    /* Apre dialog di aiuto per mandare messaggio al supervisore */
    public void openHelpDialog() {
        HelpDialogFragment helpDialogFragment = new HelpDialogFragment();
        Bundle bundle = new Bundle();
        helpDialogFragment.show(getSupportFragmentManager(), "help dialog");
    }

    /* Creazione notification channel e parametri */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myTherapy";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(DefaultValues.CHANNEL_ID, name, importance);
            notificationChannel.setDescription("Notifiche dell'app myTherapy");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /* Mostra notifica di esempio */
    private void showNotificationExample() {

        try {
            createNotificationChannel();

            int rand = (int) (Math.random() * therapy.size()) + 0;
            Medicina current = therapy.get(rand);

            Intent landingIntent = new Intent(getApplicationContext(), MedicineStatusActivity.class);
            landingIntent.putExtra(MEDICINA, current);
            landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent landingPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);

            Intent broadcastReminderActionIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
            broadcastReminderActionIntent.putExtra("toastMessage", "Rimandato di 10 minuti");
            broadcastReminderActionIntent.putExtra("NOTIFICATION_ID", DefaultValues.EXAMPLE_NOTIFICATION_ID);
            PendingIntent remindActionIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, broadcastReminderActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent broadcastMarkdoneActionIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
            broadcastMarkdoneActionIntent.putExtra("toastMessage", "Segnato come preso!");
            broadcastMarkdoneActionIntent.putExtra("NOTIFICATION_ID", DefaultValues.EXAMPLE_NOTIFICATION_ID);
            Bundle bundle = new Bundle();
            bundle.putParcelable("medicine", therapy.get(rand));
            broadcastMarkdoneActionIntent.putExtras(bundle);
            PendingIntent markDoneActionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, broadcastMarkdoneActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), DefaultValues.CHANNEL_ID);
            builder.setSmallIcon(R.drawable.robot);
            builder.setContentTitle("Promemoria");
            builder.setAutoCancel(true);
            builder.setContentText("Hey! Non dimenticare di prendere " + current.getNome() + " oggi alle " + current.getOra() + "!");
            builder.setSubText("Promemoria");
            builder.addAction(R.drawable.notification, "Rimanda di 10 minuti", remindActionIntent);
            builder.addAction(R.drawable.notification, "Segna come presa", markDoneActionIntent);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setContentIntent(landingPendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(DefaultValues.EXAMPLE_NOTIFICATION_ID, builder.build());
        } catch (RuntimeException e) { //
            throw new NoMedicinesFoundException(e, getApplicationContext());
        }
    }

    @Override
    public void getDataFromFragment(UserReport report) {
        UserReportFactory.getInstance().addReport(report);
    }

    public enum Mesi {Gennaio, Febbraio, Marzo, Aprile, Maggio, Giugno, Luglio, Agosto, Settembre, Ottobre, Novembre, Dicembre;}

    public enum Giorni {Domenica, Lunedì, Martedì, Mercoledì, Giovedì, Venerdì, Sabato;} //Sunday = primo della settimana per gli inglesi, mi adatto
}
