package com.ium.mytherapy.utils;

import android.os.Environment;

import java.io.File;

public class DefaultValues {
    public static final File path = Environment.getExternalStorageDirectory();
    public static final File dir = new File(path.getAbsolutePath() + "/myTherapy/");
    public static final File defaultAvatar = new File(dir + "/default.jpg");
}
