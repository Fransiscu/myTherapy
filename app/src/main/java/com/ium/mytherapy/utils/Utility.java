package com.ium.mytherapy.utils;

import android.os.Build;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.RequiresApi;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;

public class Utility {

    /* Classe usata solo per creare funzioni di supporto e testarle */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        System.out.println(LocalDateTime.now().getHour());
        System.out.println(LocalDateTime.now().getMinute());

        Calendar now = getInstance();
        System.out.println(now.get(HOUR_OF_DAY) + ":" + now.get(MINUTE));
        // Tests qui
    }

    /* Controllo che la data sia in formato valido in modo molto rustico */
    public static boolean isDateValid(String date) {
        String[] numbers = date.split("-");
        /* Se ci sono lettere in mezzo rendo false */
        if (hasOnlyDigits(numbers[0]) || hasOnlyDigits(numbers[1]) || hasOnlyDigits(numbers[2])) {
            return false;
        }
        /* Impongo la forma YYYY-MM-DD come segnato nel campo date del database */
        return (numbers[0].length() == 4) && (numbers[1].length() == 2 && Integer.parseInt(numbers[1]) < 13) && (numbers[2].length() == 2 && Integer.parseInt(numbers[2]) < 32);
    }

    /* Controllo che nella stringa in ingresso ci siano solo numeri */
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

    /* Controllo che url inserito sia valido */
    public static boolean isUrlValid(String url) {
        if (url.length() < 4) {
            return false;
        }
        return (url.substring(0, 3).equals("www") || url.substring(0, 4).equals("http"));
    }

    /* Controllo che la stringa in input non sia null o vuota */
    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /* Divido la stringa in parole separate da spazio e rendo la prima */
    public static String splitIntoWords(String words) {
        String[] word = words.split("\\s");
        return word[0];
    }

}