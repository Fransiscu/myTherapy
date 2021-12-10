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

    /* Notifications */
    public static final String CHANNEL_ID = "myThrapy";
    public static final int EXAMPLE_NOTIFICATION_ID = 1;
    public static final String NO_MEDICINES_IN_LIST = "Non sono presenti alcune medicine, funzionalità disabilitata";

    /* SharedPreferences */
    public final static String SHARED_PREFS = "com.ium.mytherapy.controller";
    public final static String sharedPrefFile = SHARED_PREFS;
    public static final String USER_ID = "user_id";
    public final static String USER_TYPE = "user_type";     // key for user type, used on login to redirect to correct activity

    /* Intents */
    public static final String USER_INTENT = "user";
    public static final String USER_KEY = "userKey";
    public static final String USER_LIST = "DEFAULT_USER_LIST";
    public static final String MEDICINA = "MEDICINE_INTENT";

    /* Permissions */
    public static final int PERMISSION_REQUEST_CODE = 123;

}
