package com.ium.mytherapy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.RequiresApi;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;

// utility methods used throughout the implementation of the app
// some are not used anymore, will keep them here in case I ever need
// to use them again

public class Utility {
    /* Check date validity */
    public static boolean isDateValid(String date) {
        String[] numbers = date.split("-");
        // only digits allowed
        if (hasOnlyDigits(numbers[0]) || hasOnlyDigits(numbers[1]) || hasOnlyDigits(numbers[2])) {
            return false;
        }
        /* Forcing YYYY-MM-DD date format */
        return (numbers[0].length() == 4) && (numbers[1].length() == 2 && Integer.parseInt(numbers[1]) < 13) && (numbers[2].length() == 2 && Integer.parseInt(numbers[2]) < 32);
    }

    /* Check for only digits in string */
    private static boolean hasOnlyDigits(String input) {
        if (input == null || input.length() < 1) {
            return true;
        } else {
            for (int i = 0; i < input.length(); i++) {
                if (!Character.isDigit(input.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    /* userId from sharedPreferences */
    public static int getUserIdFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DefaultValues.SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DefaultValues.USER_ID, 0);
    }

    /* Checking url validity */
    public static boolean isUrlValid(String url) {
        if (url.length() < 4) {
            return false;
        }
        return (url.startsWith("www") || url.startsWith("http://") || url.startsWith("https://"));
    }

    /* Check if string not empty */
    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /* Split string into words, return the first one */
    public static String splitIntoWords(String words) {
        String[] word = words.split("\\s");
        return word[0];
    }
}