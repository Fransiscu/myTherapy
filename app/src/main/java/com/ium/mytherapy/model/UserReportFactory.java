package com.ium.mytherapy.model;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserReportFactory {

    private static File path = Environment.getExternalStorageDirectory();
    private static File defaultReportFile = new File(path.getAbsolutePath() + "/myTherapy/supervisors/report.txt");

    private static UserReportFactory dummy;

    private UserReportFactory() {
    }

    public static UserReportFactory getInstance() {
        if (dummy == null) {
            dummy = new UserReportFactory();
        }
        return dummy;
    }

    /* Setto il report come già letto */
    public void setChecked() throws IOException {
        UserReport userReport = new UserReport();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(defaultReportFile));

        /* Leggo dal file e assegno ogni valore al report, per ora solo un report alla volta */
        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        userReport.setChecked(true);    // cambio a true
        userReport.setMedicina(strings.get(1));
        userReport.setErrorMessage(strings.get(2));

        addReport(userReport);
    }

    /* Aggiungo report al file di testo */
    public static void addReport(UserReport report) {
        try {
            FileWriter fw = new FileWriter(defaultReportFile, false);
            fw.write(report.isChecked() + "," + report.getMedicina() + "," + report.getErrorMessage());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public boolean checkReports() throws IOException {     // rende true se ci sono reports
        return this.countReports() != 0;
    }

    private int countReports() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(defaultReportFile));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    /* Prendo il report dal file di testo */
    public UserReport getReportFromFile() throws IOException {
        UserReport userReport = new UserReport();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(defaultReportFile));

        /* Leggo dal file e assegno ogni valore al report, per ora solo un report alla volta */
        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        userReport.setChecked(Boolean.getBoolean(strings.get(0)));
        userReport.setMedicina(strings.get(1));
        userReport.setErrorMessage(strings.get(2));

        return userReport;
    }
}
