package com.ium.mytherapy.controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    final static int PERMISSION_REQUEST_CODE = 123;
    final static String USER_LIST = "DEFAULT_USER_LIST";
    ArrayList<User> userList = new ArrayList<>();
    File path = Environment.getExternalStorageDirectory();
    File baseDir = new File(path.getAbsolutePath() + "/myTherapy/");
    File usersDir = new File(path.getAbsolutePath() + "/myTherapy/users/");
    File supervisorDir = new File(path.getAbsolutePath() + "/myTherapy/supervisors/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable permissionsThread = this::permissions;
        permissionsThread.run();

        Runnable getUsers = () -> {
            try {
                userList = UserFactory.getInstance().getUsers();
                Collections.sort(userList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        getUsers.run();

        Intent loginActivity = new Intent(this, LoginActivity.class);    // cambio subito activity
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(USER_LIST, userList);
        loginActivity.putExtras(bundle);
        startActivity(loginActivity);
        finish();
    }

    private void permissions() {
        if (!(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            addDefaultItems();
        } else {
            addDefaultItems();
        }
    }

    public void addDefaultItems() {
        File defaultAvatarPath = new File(path.getAbsolutePath() + "/myTherapy/default.jpg");

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            try {
                boolean wasSuccessful;
//                noinspection ResultOfMethodCallIgnored
                baseDir.mkdirs();   // per evitare i warning zzz
                wasSuccessful = usersDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                wasSuccessful = supervisorDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatardefault);
                wasSuccessful = defaultAvatarPath.createNewFile();
                if (!wasSuccessful) {   // per evitare i warnings
                    System.out.println("was not successful.");
                }
                FileOutputStream fos = new FileOutputStream(defaultAvatarPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUEST_CODE);
            addDefaultItems();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MainActivity.PERMISSION_REQUEST_CODE) {
            if ((grantResults.length > 0)
                    && (grantResults[0] +
                    grantResults[1]
                    == PackageManager.PERMISSION_GRANTED)) {
                addDefaultItems();
            } else {
                Log.d("Permessi", "not granted");
            }
        }
    }
}
