package com.ium.mytherapy.model;

import com.ium.mytherapy.utils.DefaultValues;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserReportFactory {

    private static UserReportFactory dummy;

    private UserReportFactory() {
    }

    public static UserReportFactory getInstance() {
        if (dummy == null) {
            dummy = new UserReportFactory();
        }
        return dummy;
    }

    public static void addReport(UserReport report) {
        try {
            FileWriter fw = new FileWriter(DefaultValues.path + "/myTherapy/supervisors/report.txt", false);
            fw.write(report.isChecked() + "," + report.getMedicine() + "," + report.getErrorMessage() + "," + report.getUserId());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public void setChecked() throws IOException {
        UserReport userReport = new UserReport();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DefaultValues.defaultReportFile));

        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        userReport.setChecked(true);
        userReport.setMedicine(strings.get(1));
        userReport.setErrorMessage(strings.get(2));

        addReport(userReport);
    }

    public boolean checkRead() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DefaultValues.defaultReportFile));

        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        return Boolean.parseBoolean(strings.get(0));
    }

    public boolean checkReports() throws IOException {  // returns true if there are reports
        return this.countReports() != 0;
    }

    private int countReports() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(DefaultValues.defaultReportFile));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    public UserReport getReportFromFile() throws IOException {
        UserReport userReport = new UserReport();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(DefaultValues.defaultReportFile));

        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        userReport.setChecked(Boolean.getBoolean(strings.get(0)));
        userReport.setMedicine(strings.get(1));
        userReport.setErrorMessage(strings.get(2));
        userReport.setUserId(Integer.parseInt(strings.get(3)));

        return userReport;
    }
}
