package com.ium.mytherapy.controller;

import android.content.Intent;
import android.os.Bundle;

import com.ium.mytherapy.R;

import androidx.appcompat.app.AppCompatActivity;

public class EditAddMedicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_terapia);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent userManagementHome = new Intent(getApplicationContext(), UserManagementActivity.class);
        userManagementHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(userManagementHome);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
