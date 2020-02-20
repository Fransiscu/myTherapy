package com.ium.mytherapy.views.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private Calendar date;

    private DatePickerFragmentListener listener;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (date == null) {
            date = Calendar.getInstance();
            date.set(Calendar.YEAR, 1995);
            date.set(Calendar.MONTH, Calendar.JANUARY);
            date.set(Calendar.DAY_OF_MONTH, 1);
        }

        final DatePicker datePicker = new DatePicker(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(datePicker);

        builder.setPositiveButton("Ok", (dialog, which) -> {
            date.set(Calendar.YEAR, datePicker.getYear());
            date.set(Calendar.MONTH, datePicker.getMonth());
            date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

            if (listener != null) {
                listener.onDatePickerFragmentOkButton(DatePickerFragment.this, date);
            }
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> {
                    if (listener != null) {
                        listener.onDatePickerFragmentCancelButton(DatePickerFragment.this);
                    }
                }
        );

        return builder.create();
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setOnDatePickerFragmentChanged(DatePickerFragmentListener l) {
        this.listener = l;
    }

    public interface DatePickerFragmentListener {
        void onDatePickerFragmentOkButton(DialogFragment dialog, Calendar date);
        void onDatePickerFragmentCancelButton(DialogFragment dialog);
    }
}
