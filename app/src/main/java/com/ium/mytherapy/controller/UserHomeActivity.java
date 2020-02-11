package com.ium.mytherapy.controller;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

@SuppressWarnings("ALL")
public class UserHomeActivity extends AppCompatActivity {

    MaterialButton logout;
    public final String CHANNEL_ID = "myThrapy";
    View primo, secondo, terzo;
    List<Medicina> therapy;
    public static final String MEDICINA = "MEDICINE_INTENT";
    public final int NOTIFICATION_ID = 001;
    TextView todaysDate, medName1, medName2, medName3, medTime1, medTime2, medTime3, notifTitolo;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_utente);

        primo = findViewById(R.id.primo_item);
        secondo = findViewById(R.id.secondo_item);
        terzo = findViewById(R.id.terzo_item);

        medName1 = findViewById(R.id.timeline_nome_medicina_uno);
        medName2 = findViewById(R.id.timeline_nome_medicina_due);
        medName3 = findViewById(R.id.timeline_nome_medicina_tre);

        medTime1 = findViewById(R.id.timeline_ora_medicina_uno);
        medTime2 = findViewById(R.id.timeline_ora_medicina_due);
        medTime3 = findViewById(R.id.timeline_ora_medicina_tre);

        notifTitolo = findViewById(R.id.titolo_home_utente);

        /* Prendo valori terapie e setto valori nella schermata */
        Runnable getTherapiesThread = this::setMedsValues;
        getTherapiesThread.run();

        /* Setto la data di oggi */
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

        /* Setto listener per pulsante di logout */
        logout = findViewById(R.id.user_logout_button);
        logout.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
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
                .show());

        /* Listeners per medicine di esmepio */
        primo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                primo.setSelected(true);    // coloro di grigio al tocco
                Bundle bundle = new Bundle();
                bundle.putParcelable(MEDICINA, therapy.get(0));
                Intent therapy1 = new Intent(getApplicationContext(), MedicineStatusActivity.class);
                therapy1.putExtras(bundle);
                startActivity(therapy1);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
                finish();
            }
        });
        secondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondo.setSelected(true);  // coloro di grigio al tocco
                Intent therapy2 = new Intent(getApplicationContext(), MedicineStatusActivity.class);
                therapy2.putExtra(MEDICINA, therapy.get(1));
                startActivity(therapy2);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
                finish();
            }
        });
        terzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terzo.setSelected(true);    // coloro di grigio al tocco
                Intent therapy3 = new Intent(getApplicationContext(), MedicineStatusActivity.class);
                therapy3.putExtra(MEDICINA, therapy.get(2));
                startActivity(therapy3);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
                finish();
            }
        });

        /* Listener per notifica test */

        notifTitolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable notificationExample = new Runnable() {
                    @Override
                    public void run() {
                        createNotificationChannel();
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                        builder.setSmallIcon(R.drawable.robot);
                        builder.setContentTitle(therapy.get(1).getNome());
                        builder.setContentText("Non dimenticare di prendere la tua medicina oggi alle " + therapy.get(1).getOra() + "!");
                        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

                        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                        Log.d("Notif", "test");

                    }
                };
                notificationExample.run();
            }
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myTherapy";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription("Notifiche dell'app myTherapy");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    /* In un thread separato in quanto in teoria potrebbe richiedere tempo facendo un feth dal server */
    private void setMedsValues() {
        therapy = new ArrayList<>();
        therapy = MedicinaFactory.getInstance().getMedicines();
        medName1.setText(therapy.get(0).getNome());
        medName2.setText(therapy.get(1).getNome());
        medName3.setText(therapy.get(2).getNome());

        medTime1.setText(therapy.get(0).getOra());
        medTime2.setText(therapy.get(1).getOra());
        medTime3.setText(therapy.get(2).getOra());
    }

    public enum Mesi {Gennaio, Febbraio, Marzo, Aprile, Maggio, Giugno, Luglio, Agosto, Settembre, Ottobre, Novembre, Dicembre;}

    public enum Giorni {Domenica, Lunedì, Martedì, Mercoledì, Giovedì, Venerdì, Sabato;} //Sunday = primo della settimana per gli inglesi, mi adatto
}
