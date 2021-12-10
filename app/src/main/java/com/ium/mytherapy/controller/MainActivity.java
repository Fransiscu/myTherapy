package com.ium.mytherapy.controller;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.ium.mytherapy.R;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    ArrayList<User> userList = new ArrayList<>();
    public static SharedPreferences mPreferences;
    String userValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* sharedPreferences initialization */
        mPreferences = getSharedPreferences(DefaultValues.sharedPrefFile, MODE_PRIVATE);

        /* loading last user data */
        Runnable loadData = this::loadData;
        loadData.run();

        /* Checking for app permissions */
        Runnable permissionsThread = this::permissions;
        permissionsThread.run();

        /* Gathering users from directories */
        Runnable getUsers = () -> {
            try {
                userList = UserFactory.getInstance().getUsers();
                Collections.sort(userList); // alphabetical order
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        getUsers.run();

        /* Setting user on app start */
        if (userValue.equals("user")) {
            Intent userIntent = new Intent(this, UserHomeActivity.class);
            startActivity(userIntent);
            finish();
        } else if (userValue.equals("supervisor")) {
            Intent supervisorIntent = new Intent(this, SupervisorHomeActivity.class);
            startActivity(supervisorIntent);
            finish();
        } else {
            Intent loginActivity = new Intent(this, UserLoginActivity.class);    // changing activity if no saved user
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(DefaultValues.USER_LIST, userList); // users list through intent
            loginActivity.putExtras(bundle);
            startActivity(loginActivity);
            finish();
        }
    }

    /* Method to load data from sharedPreferences */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(DefaultValues.SHARED_PREFS, MODE_PRIVATE);
        userValue = sharedPreferences.getString(DefaultValues.USER_TYPE, "");
    }

    /* Method to check for permissions */
    private void permissions() {
        if (!(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, DefaultValues.PERMISSION_REQUEST_CODE);
        }
        addDefaultItems();
    }

    public void addDefaultItems() {

        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            try {
                boolean wasSuccessful;
                DefaultValues.dir.mkdirs();
                wasSuccessful = DefaultValues.usersDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                wasSuccessful = DefaultValues.supervisorDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatardefault);
                wasSuccessful = DefaultValues.defaultAvatar.createNewFile();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                wasSuccessful = DefaultValues.defaultReportFile.createNewFile();
                if (!wasSuccessful) {
                    System.out.println("was not successful.");
                }
                FileOutputStream fos = new FileOutputStream(DefaultValues.defaultAvatar);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {    // recursively asking for permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, DefaultValues.PERMISSION_REQUEST_CODE);
            addDefaultItems();
        }
    }

    /* Checking if permissions have been granted */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == DefaultValues.PERMISSION_REQUEST_CODE) {
            if ((grantResults.length > 0)
                    && (grantResults[0] +
                    grantResults[1]
                    == PackageManager.PERMISSION_GRANTED)) {
                addDefaultItems();
            }
        }
    }
}
