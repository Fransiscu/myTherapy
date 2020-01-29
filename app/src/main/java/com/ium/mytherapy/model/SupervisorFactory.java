package com.ium.mytherapy.model;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SupervisorFactory {

    private static SupervisorFactory dummy;

    private File path = Environment.getExternalStorageDirectory();
    public File usersDir = new File(path.getAbsolutePath() + "/myTherapy/users/");
    public File supervisorDir = new File(path.getAbsolutePath() + "/myTherapy/supervisors/");
    private File baseDir = new File(path.getAbsolutePath() + "/myTherapy/");

    private SupervisorFactory() {
    }

    public static SupervisorFactory getInstance() {
        if (dummy == null) {
            dummy = new SupervisorFactory();
        }
        return dummy;
    }

    public void addSupervisor(Supervisor supervisor) throws IOException {
        File newSupervisor = new File(path.getAbsolutePath() + "/myTherapy/supervisors/" + supervisor.getSupervisorId() + "/");
        boolean wasSuccessful = newSupervisor.mkdirs();

        if (!wasSuccessful) {
            System.out.println("was not successful.");
        }

        FileOutputStream fos = new FileOutputStream(newSupervisor + "/profile.txt");

        fos.flush();
        try {
            FileWriter fw = new FileWriter(newSupervisor + "/profile.txt", true);
            fw.write(supervisor.getSupervisorId() + "," + supervisor.getNome() + "," + supervisor.getCognome() + "," + supervisor.getEmail() + "," +
                    supervisor.getUsername() + "," + supervisor.getPassword() + "," + supervisor.getDataNascita());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    public Supervisor verifySupervisor() {
        return null;
    }

    public ArrayList<Supervisor> getSupervisors() {
        return null;
    }

    public ArrayList<User> getUsersFromSupervisor(Supervisor supervisor) {
        return null;
    }


}
