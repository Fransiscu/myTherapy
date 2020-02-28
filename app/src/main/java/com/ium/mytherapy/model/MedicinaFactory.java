package com.ium.mytherapy.model;

import com.ium.mytherapy.utils.DefaultValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("ALL")
public class MedicinaFactory {

    private static MedicinaFactory dummy;

    private MedicinaFactory() {
    }

    public static MedicinaFactory getInstance() {
        if (dummy == null) {
            dummy = new MedicinaFactory();
        }
        return dummy;
    }

    /* Prendo medicina da file */
    private Medicina getMedicineFromFile(String filePath) throws IOException {
        Medicina medicina = new Medicina();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        /* Leggo dal file e assegno ogni valore al supervisore */
        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));

        medicina.setCode(Integer.parseInt(strings.get(0)));
        medicina.setNome(strings.get(1));
        medicina.setDescrizione(strings.get(2));
        medicina.setDosaggio(strings.get(3));
        medicina.setFrequenza(strings.get(4));
        medicina.setFrequenzaNum(Integer.parseInt(strings.get(5)));
        medicina.setOra(strings.get(6));
        medicina.setConsigliSupervisore(strings.get(7));
        medicina.setLink(strings.get(8));
        medicina.setPresa(Boolean.parseBoolean(strings.get(9)));
        medicina.setNotifEnabled(Boolean.parseBoolean(strings.get(10)));

        return medicina;
    }

    /* Aggiunta medicina */
    public void addMedicine(User user, Medicina medicina) {
        File newMedicine = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/");

        /* Setto id medicina a seconda di quanti file ho */
        File[] files = newMedicine.listFiles();
        if (files != null) {
            int max = 0;
            for (File inFile : files) {
                if (inFile.isFile()) {
                    String temp = inFile.getName();
                    if (Integer.parseInt(temp) >= max) {
                        max = Integer.parseInt(temp) + 1;
                    }
                }
            }
            medicina.setCode(max);
        } else {
            medicina.setCode(0);
        }

        File file = new File(newMedicine.toString() + "/" + medicina.getCode());

        /* Aggiungo medicine al profilo utente */
        try {
            FileWriter fw = new FileWriter(file.toString(), true);
            fw.write(medicina.getCode() + "," + medicina.getNome() + "," + medicina.getDescrizione() + "," + medicina.getDosaggio() + "," +
                    medicina.getFrequenza() + "," + medicina.getFrequenzaNum() + "," + medicina.getOra() + "," + medicina.getConsigliSupervisore() + "," +
                    medicina.getLink() + "," + medicina.isPresa() + "," + medicina.isNotifEnabled());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    /* Raccolgo medicine per utente */
    public List<Medicina> getMedicinesForUser(User user) throws IOException {
        ArrayList<Medicina> list = new ArrayList<>();

        File f = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/");

        /* Scorro tutte le cartelle */
        File[] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isFile()) {
                    list.add(getMedicineFromFile(inFile.toString()));
                }
            }
        } else {
            return null;
        }

        Collections.sort(list);
        return list;
    }

    public boolean deleteMedicineFromCode(User user, Medicina med) {

        user = new User();
        user.setUserId(0);

        // impongo userid == 0
        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + 0 + "/medicine/" + med.getCode());
        boolean succeded = fileToDelete.delete();

        if (!succeded) {
            return false;
        }

        try {
            this.addMedicine(user, med);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /* Cambio status presa o meno */
    public void changePresa(Medicina medicina) {
        User user = new User();
        user.setUserId(0);

        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + 0 + "/medicine/" + medicina.getCode());
        fileToDelete.delete();
        addMedicine(user, medicina);
    }

    /* Cambio notifiche abilitate o meno */
    public void changeNotif(Medicina medicina) {
        User user = new User();
        user.setUserId(0);

        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + 0 + "/medicine/" + medicina.getCode());
        fileToDelete.delete();
        addMedicine(user, medicina);
    }
}
