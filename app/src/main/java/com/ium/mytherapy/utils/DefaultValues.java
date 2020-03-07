package com.ium.mytherapy.utils;

import android.os.Environment;

import java.io.File;

public class DefaultValues {
    public static final File path = Environment.getExternalStorageDirectory();
    public static final File dir = new File(path.getAbsolutePath() + "/myTherapy/");
    public static final File usersDir = new File(path.getAbsolutePath() + "/myTherapy/users/");
    public static final File defaultAvatar = new File(dir + "/default.jpg");
    public static final File supervisorDir = new File(path.getAbsolutePath() + "/myTherapy/supervisors/");
    public static final File defaultReportFile = new File(path + "/myTherapy/supervisors/report.txt");

    /* Per le notifiche */
    public static final String CHANNEL_ID = "myThrapy";
    public static final int EXAMPLE_NOTIFICATION_ID = 1;
    public static final String NO_MEDICINES_IN_LIST = "Non sono presenti alcune medicine, funzionalità disabilitata";

    /* Per sharedPreferences */
    public final static String SHARED_PREFS = "com.ium.mytherapy.controller";   // key per le shared preferences
    public final static String sharedPrefFile = SHARED_PREFS;
    public static final String USER_ID = "user_id";
    public final static String USER_TYPE = "user_type";     // key per il tipo di utente salvato nelle shared preferences

}
