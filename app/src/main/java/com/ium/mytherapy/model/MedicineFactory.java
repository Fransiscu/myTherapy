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
public class MedicineFactory {

    private static MedicineFactory dummy;

    private MedicineFactory() {
    }

    public static MedicineFactory getInstance() {
        if (dummy == null) {
            dummy = new MedicineFactory();
        }
        return dummy;
    }

    /* Getting medicine from file */
    private Medicine getMedicineFromFile(String filePath) throws IOException {
        Medicine medicine = new Medicine();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        /* Assigning value to supervisor */
        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));

        medicine.setCode(Integer.parseInt(strings.get(0)));
        medicine.setName(strings.get(1));
        medicine.setDescription(strings.get(2));
        medicine.setDosage(strings.get(3));
        medicine.setFrequency(strings.get(4));
        medicine.setFrequencyNumber(Integer.parseInt(strings.get(5)));
        medicine.setTimeHour(strings.get(6));
        medicine.setSupervisorTips(strings.get(7));
        medicine.setLink(strings.get(8));
        medicine.setTaken(Boolean.parseBoolean(strings.get(9)));
        medicine.setNotificationEnabled(Boolean.parseBoolean(strings.get(10)));
        medicine.setDelayed(Boolean.parseBoolean(strings.get(11)));
        medicine.setReminder(strings.get(12));

        return medicine;
    }

    public void addMedicine(User user, Medicine medicine) {
        File newMedicine = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/");

        /* Counting files and assigning id */
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
            medicine.setCode(max);
        } else {
            medicine.setCode(0);
        }

        File file = new File(newMedicine.toString() + "/" + medicine.getCode());

        /* Adding to user profile */
        try {
            FileWriter fw = new FileWriter(file.toString(), true);
            fw.write(medicine.getCode() + "," + medicine.getName() + "," + medicine.getDescription() + "," + medicine.getDosage() + "," +
                    medicine.getFrequency() + "," + medicine.getFrequencyNumber() + "," + medicine.getTimeHour() + "," + medicine.getSupervisorTips() + "," +
                    medicine.getLink() + "," + medicine.isTaken() + "," + medicine.isNotificationEnabled() + "," + medicine.isDelayed() + "," + medicine.getReminder());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /* Getting medicines for user */
    public List<Medicine> getMedicinesForUser(User user) throws IOException {
        ArrayList<Medicine> list = new ArrayList<>();

        File f = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/");

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

    public boolean deleteMedicineFromCode(User user, Medicine med) {
        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/" + med.getCode());
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

    public void removeMedicine(Medicine medicine, User user) {
        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/" + medicine.getCode());
        fileToDelete.delete();
    }

    public void changeTaken(Medicine medicine, User user) {

        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/" + medicine.getCode());
        fileToDelete.delete();
        medicine.setReminder("none");
        medicine.setDelayed(false);
        addMedicine(user, medicine);
    }

    /* Set time for new notification */
    public void setReminder(Medicine medicine, User user) {

        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/" + medicine.getCode());
        fileToDelete.delete();
        medicine.setDelayed(true);
        addMedicine(user, medicine);
    }

    /* Toggle enabled or disabled notifications */
    public void changeNotif(Medicine medicine, User user) {

        File fileToDelete = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine/" + medicine.getCode());
        fileToDelete.delete();
        addMedicine(user, medicine);
    }
}
