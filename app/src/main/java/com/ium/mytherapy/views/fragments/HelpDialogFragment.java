package com.ium.mytherapy.views.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicine;
import com.ium.mytherapy.model.MedicineFactory;
import com.ium.mytherapy.model.User;
import com.ium.mytherapy.model.UserFactory;
import com.ium.mytherapy.model.UserReport;
import com.ium.mytherapy.utils.DefaultValues;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import fr.ganfra.materialspinner.MaterialSpinner;

public class HelpDialogFragment extends AppCompatDialogFragment {

    private HelpDialogListener listener;

    private final static String SHARED_PREFS = "com.ium.mytherapy.controller";

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        UserReport userReport = new UserReport();

        /* Prevent dialog from being closed or canceled */
        this.setCancelable(false);

        /* Getting user data */
        SharedPreferences mPreferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int userId = mPreferences.getInt(DefaultValues.USER_ID, 0);
        User user = new User();

        try {
            user = UserFactory.getInstance().getUser(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Grabbing medicine list */
        ArrayList<Medicine> list = null;
        try {
            list = (ArrayList<Medicine>) MedicineFactory.getInstance().getMedicinesForUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> spinnerItems = new ArrayList<>();

        if (list != null) {
            for (Medicine medicine : list)
                spinnerItems.add(medicine.getName());
        }
        spinnerItems.add("Richiesta di aiuto generica");     // if no medicines in list

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_help_dialog, null);

        MaterialSpinner spinnerPick = view.findViewById(R.id.help_dialog_spinner);
        TextInputEditText errorMessage = view.findViewById(R.id.problema_textfield);

        ArrayAdapter<String> adapterMedicines = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        adapterMedicines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setNegativeButton("Annulla", (dialogInterface, i) -> Toast.makeText(getActivity(), "REPORT CANCELLATO", Toast.LENGTH_LONG).show());
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            if (Objects.requireNonNull(errorMessage.getText()).toString().matches("")) {
                errorMessage.setText("Nessun messaggio di errore fornito");
            }
            userReport.setChecked(false);
            TextView textView = (TextView) spinnerPick.getSelectedView();
            String result = textView.getText().toString();
            userReport.setMedicine(result);
            userReport.setErrorMessage(Objects.requireNonNull(errorMessage.getText()).toString());
            listener.getDataFromFragment(userReport);
            Toast.makeText(getActivity(), "Report inviato", Toast.LENGTH_LONG).show();

        });
        spinnerPick.setAdapter(adapterMedicines);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (HelpDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "error");
        }
    }

    public interface HelpDialogListener {
        void getDataFromFragment(UserReport report);
    }
}
