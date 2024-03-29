package com.ium.mytherapy.model;

import com.ium.mytherapy.utils.DefaultValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SupervisorFactory {

    private static SupervisorFactory dummy;

    private SupervisorFactory() {
    }

    public static SupervisorFactory getInstance() {
        if (dummy == null) {
            dummy = new SupervisorFactory();
        }
        return dummy;
    }

    public void addSupervisor(Supervisor supervisor) throws IOException {
        File newSupervisor = new File(DefaultValues.supervisorDir + "/" + supervisor.getSupervisorId() + "/");
        boolean wasSuccessful = newSupervisor.mkdirs();

        if (!wasSuccessful) {
            System.out.println("was not successful.");
            return;
        }

        FileOutputStream fos = new FileOutputStream(newSupervisor + "/profile.txt");
        fos.flush();
        fos = new FileOutputStream(newSupervisor + "/utenti.txt");
        fos.flush();

        try {
            FileWriter fw = new FileWriter(newSupervisor + "/profile.txt", true);
            fw.write(supervisor.getSupervisorId() + "," + supervisor.getName() + "," + supervisor.getSurname() + "," + supervisor.getEmail() + "," +
                    supervisor.getUsername() + "," + supervisor.getPassword() + "," + supervisor.getBirthDate());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    /* Checking if supervisor exists */
    public Supervisor verifySueprvisor(String username, String password) throws IOException {
        ArrayList<Supervisor> supervisors = this.getSupervisors();

        if (supervisors == null) {
            return null;
        }

        for (Supervisor supervisor : Objects.requireNonNull(supervisors)) {

            if (supervisor.getUsername().equals(username) && supervisor.getPassword().equals(password)) {
                return supervisor;
            }
        }

        return null;
    }

    private Supervisor getSupervisorFromFile(String filePath) throws IOException {
        Supervisor supervisor = new Supervisor();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        /* Leggo dal file e assegno ogni valore al supervisore */
        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        supervisor.setSupervisorId(Integer.parseInt(strings.get(0)));
        supervisor.setName(strings.get(1));
        supervisor.setSurname(strings.get(2));
        supervisor.setEmail(strings.get(3));
        supervisor.setUsername(strings.get(4));
        supervisor.setPassword(strings.get(5));
        supervisor.setBirthDate(strings.get(6));

        return supervisor;
    }

    private ArrayList<Supervisor> getSupervisors() throws IOException {
        File f = new File(DefaultValues.supervisorDir.toString());
        ArrayList<Supervisor> supervisors = new ArrayList<>();

        File[] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    supervisors.add(getSupervisorFromFile(inFile.toString() + "/profile.txt"));
                }
            }
        } else {
            return null;
        }
        return supervisors;
    }
}
