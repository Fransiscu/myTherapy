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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.model.UserReport;
import com.ium.mytherapy.model.UserReportFactory;
import com.ium.mytherapy.utils.DefaultValues;
import com.ium.mytherapy.utils.NotificationReceiver;
import com.ium.mytherapy.utils.Utility;
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

    TextView todaysDate, notifTitolo, medName1, medName2, medName3, medTime1, medTime2, medTime3;
    MaterialTextView noMedicine, topMidnight, bottomMidnight;
    UserTimelineCardAdapter userTimelineCardAdapter;
    RecyclerView userTimelineRecyclerView;
    ArrayList<Medicine> medicineArrayList;
    SharedPreferences mPreferences;
    MaterialButton logout, helpMe;
    List<Medicine> therapy = null;
    EditedScrollView scrollView;
    View topLine, bottomLine;

    int userId;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);

        mPreferences = getSharedPreferences(DefaultValues.sharedPrefFile, MODE_PRIVATE);
        userId = mPreferences.getInt(DefaultValues.USER_ID, 0);

        User user = new User();
        try {
            user = UserFactory.getInstance().getUser(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            therapy = MedicineFactory.getInstance().getMedicinesForUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        topLine = findViewById(R.id.horizontal_top_line);
        bottomLine = findViewById(R.id.horizontal_bottom_line);

        noMedicine = findViewById(R.id.no_medicine);
        topMidnight = findViewById(R.id.mezzanotte_top);
        bottomMidnight = findViewById(R.id.mezzanotte_bottom);

        /* Making the top bar not scroll */
        scrollView = findViewById(R.id.scrollView);
        scrollView.setScrolling(false);

        /* RecyclerView for medicines */
        userTimelineRecyclerView = findViewById(R.id.userHomeTimelineRecyclerView);
        userTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineArrayList = new ArrayList<>(therapy);
        userTimelineCardAdapter = new UserTimelineCardAdapter(this, medicineArrayList);
        userTimelineRecyclerView.setAdapter(userTimelineCardAdapter);

        /* Setting horizontal lines according to therapy*/
        if (!therapy.isEmpty() && therapy != null) {
            if (therapy.get(0).isTaken()) {
                topLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
            } else {
                topLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
            if (therapy.get(therapy.size() - 1).isTaken()) {
                bottomLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
            } else {
                bottomLine.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }
        } else {    // if no current therapies show this
            topLine.setVisibility(View.GONE);
            bottomLine.setVisibility(View.GONE);
            topMidnight.setVisibility(View.GONE);
            bottomMidnight.setVisibility(View.GONE);
            noMedicine.setVisibility(View.VISIBLE);
        }

        notifTitolo = findViewById(R.id.user_home_title);
        logout = findViewById(R.id.user_logout_button);
        helpMe = findViewById(R.id.user_help);

        /* Setting today's date */
        todaysDate = findViewById(R.id.data_oggi);
        Date date = new Date();
        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
            int month = localDate.getMonthValue();
            int day = localDate.getDayOfMonth();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            todaysDate.setText(String.format("%s %d %s %d", Giorni.values()[dayOfWeek - 1], day, Mesi.values()[month - 1].toString(), year));
        }

        /* logout button on click listener */
        logout.setOnClickListener(view -> {
            new MaterialAlertDialogBuilder(this)
                .setTitle("LOGOUT")
                .setMessage("Sei sicuro di voler fare il logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", (dialogInterface, i) -> {
                    Intent backToHome = new Intent(getApplicationContext(), UserLoginActivity.class);
                    startActivity(backToHome);
                    SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                    preferencesEditor.clear();
                    preferencesEditor.apply();
                    finish();
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                    .show();
        });

        /* test notification on click listener */
        notifTitolo.setOnClickListener(view -> {
            Runnable notificationExample = () -> {
                showNotificationExample();
            };
                notificationExample.run();
        });

        /* helpMe button on click listener*/
        helpMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHelpDialog();
            }
        });

    }

    /* Opens help dialog to send a message to the supervisor */
    public void openHelpDialog() {
        HelpDialogFragment helpDialogFragment = new HelpDialogFragment();
        Bundle bundle = new Bundle();
        helpDialogFragment.show(getSupportFragmentManager(), "help dialog");
    }

    /* Setting up notification parameters */
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

    /* Show test notification */
    private void showNotificationExample() {
        Medicine current;
        int rand;

        try {
            createNotificationChannel();

            if (noMoreUnfinishedTherapies(therapy)) {
                Toast.makeText(getApplicationContext(), "Non ci sono più medicine non prese", Toast.LENGTH_LONG).show();
                return;
            }

            do {        // cycling until I find a medicine not taken yet
                rand = (int) (Math.random() * therapy.size()) + 0;
                current = therapy.get(rand);
            } while (current.isTaken());

            Intent landingIntent = new Intent(getApplicationContext(), MedicineStatusActivity.class);
            landingIntent.putExtra(DefaultValues.MEDICINA, current);
            landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent landingPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, landingIntent, PendingIntent.FLAG_ONE_SHOT);

            Intent broadcastReminderActionIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
            broadcastReminderActionIntent.putExtra("toastMessage", "Rimandato di 10 minuti");
            broadcastReminderActionIntent.putExtra("NOTIFICATION_ID", DefaultValues.EXAMPLE_NOTIFICATION_ID);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", UserFactory.getInstance().getUser(Utility.getUserIdFromSharedPreferences(getApplicationContext())));
            bundle.putParcelable("medicine", therapy.get(rand));
            bundle.putInt("action", 1);
            broadcastReminderActionIntent.putExtras(bundle);
            PendingIntent remindActionIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, broadcastReminderActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent broadcastMarkdoneActionIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
            broadcastMarkdoneActionIntent.putExtra("toastMessage", "Segnato come preso!");
            broadcastMarkdoneActionIntent.putExtra("NOTIFICATION_ID", DefaultValues.EXAMPLE_NOTIFICATION_ID);
            bundle = new Bundle();
            bundle.putParcelable("user", UserFactory.getInstance().getUser(Utility.getUserIdFromSharedPreferences(getApplicationContext())));
            bundle.putParcelable("medicine", therapy.get(rand));
            bundle.putInt("action", 2);
            broadcastMarkdoneActionIntent.putExtras(bundle);
            PendingIntent markDoneActionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, broadcastMarkdoneActionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), DefaultValues.CHANNEL_ID);
            builder.setSmallIcon(R.drawable.robot);
            builder.setContentTitle("Promemoria");
            builder.setAutoCancel(true);
            builder.setContentText("Hey! Non dimenticare di prendere " + current.getName() + " oggi alle " + current.getTimeHour() + "!");
            builder.setSubText("Promemoria");
            builder.addAction(R.drawable.notification, "Rimanda di 10 minuti", remindActionIntent);
            builder.addAction(R.drawable.notification, "Segna come presa", markDoneActionIntent);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setContentIntent(landingPendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(DefaultValues.EXAMPLE_NOTIFICATION_ID, builder.build());
        } catch (RuntimeException | IOException e) { //
            throw new NoMedicinesFoundException(getApplicationContext());
        }
    }

    /* Checking if there is a non taken medicine */
    private boolean noMoreUnfinishedTherapies(List<Medicine> therapy) {
        for (Medicine medicine : therapy) {
            if (!medicine.isTaken()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void getDataFromFragment(UserReport report) {
        UserReportFactory.getInstance().addReport(report);
    }

    public enum Mesi {Gennaio, Febbraio, Marzo, Aprile, Maggio, Giugno, Luglio, Agosto, Settembre, Ottobre, Novembre, Dicembre;}

    public enum Giorni {Domenica, Lunedì, Martedì, Mercoledì, Giovedì, Venerdì, Sabato;} //Sunday = primo della settimana per gli inglesi, mi adatto
}
