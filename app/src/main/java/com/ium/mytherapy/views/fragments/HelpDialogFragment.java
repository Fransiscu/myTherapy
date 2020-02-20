package com.ium.mytherapy.views.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ium.mytherapy.R;
import com.ium.mytherapy.model.Medicina;
import com.ium.mytherapy.model.MedicinaFactory;
import com.ium.mytherapy.model.UserReport;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import fr.ganfra.materialspinner.MaterialSpinner;

public class HelpDialogFragment extends AppCompatDialogFragment {

    private HelpDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        UserReport userReport = new UserReport();
        ArrayList<Medicina> list;
        list = (ArrayList<Medicina>) MedicinaFactory.getInstance().getMedicines();
        String[] spinnerItems = new String[]{list.get(0).getNome(), list.get(1).getNome(), list.get(2).getNome()};
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.layout_help_dialog, null);

        MaterialSpinner spinnerPick = view.findViewById(R.id.help_dialog_spinner);
        TextInputEditText errorMessage = view.findViewById(R.id.problema_textfield);

        ArrayAdapter<String> adapterMedicines = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        adapterMedicines.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        builder.setView(view)
                .setCancelable(false)
                .setNegativeButton("Annulla", (dialogInterface, i) -> {
                })
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    userReport.setChecked(false);
                    TextView textView = (TextView) spinnerPick.getSelectedView();
                    String result = textView.getText().toString();
                    userReport.setMedicina(result);
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
