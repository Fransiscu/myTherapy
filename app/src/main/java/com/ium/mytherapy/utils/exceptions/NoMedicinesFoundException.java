package com.ium.mytherapy.utils.exceptions;

import android.content.Context;
import android.widget.Toast;

import com.ium.mytherapy.utils.DefaultValues;

public class NoMedicinesFoundException extends RuntimeException {
    public NoMedicinesFoundException(RuntimeException e, Context context) {
        Toast.makeText(context, DefaultValues.NO_MEDICINES_IN_LIST, Toast.LENGTH_LONG).show();
    }
}
