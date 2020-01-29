package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean success = true;

        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path.getAbsolutePath() + "/myTherapy/");

        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdirs();
        }
        if (isDirectoryCreated) {
            System.out.println("Already present");
        }

        Intent newActivity = new Intent(this, LoginActivity.class);    // cambio subito activity
        startActivity(newActivity);
        finish();
    }


}
