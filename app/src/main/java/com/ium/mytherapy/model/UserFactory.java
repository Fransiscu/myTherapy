package com.ium.mytherapy.model;

import android.util.Log;

import com.ium.mytherapy.utils.DefaultValues;

import org.apache.commons.io.FileUtils;

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

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UserFactory {

    private static UserFactory dummy;

    private UserFactory() {
    }

    public static UserFactory getInstance() {
        if (dummy == null) {
            dummy = new UserFactory();
        }
        return dummy;
    }

    public User getUser(int userId) throws IOException {
        return getUserFromFile(DefaultValues.usersDir + "/" + userId + "/profile.txt");
    }

    public User verifyUser(String username, String password) throws IOException {
        ArrayList<User> users = this.getUsers();

        if (users == null) {
            return null;
        }

        for (User user : Objects.requireNonNull(users)) {

            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public ArrayList<User> getUsers() throws IOException {
        File f = new File(DefaultValues.usersDir.toString());
        ArrayList<User> users = new ArrayList<>();

        File[] files = f.listFiles();
        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    users.add(getUserFromFile(inFile.toString() + "/profile.txt"));
                }
            }
        } else {
            return null;
        }
        return users;
    }

    private User getUserFromFile(String filePath) throws IOException {
        User user = new User();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

        String line = bufferedReader.readLine();
        List<String> strings = Arrays.asList(line.split(","));
        user.setUserId(Integer.parseInt(strings.get(0)));
        user.setName(strings.get(1));
        user.setSurname(strings.get(2));
        user.setEmail(strings.get(3));
        user.setUsername(strings.get(4));
        user.setPassword(strings.get(5));
        user.setBirthDate(strings.get(6));

        return user;
    }

    public void addUser(User user, Supervisor supervisor) throws IOException {
        File newUser = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/");
        boolean wasSuccessful = newUser.mkdirs();

        if (!wasSuccessful) {
            System.out.println("was not successful.");
            return;
        }

        FileOutputStream fos = new FileOutputStream(newUser + "/profile.txt");
        fos.flush();    // flushing file stream

        try {
            FileWriter fw = new FileWriter(newUser + "/profile.txt", true);
            fw.write(user.getUserId() + "," + user.getName() + "," + user.getSurname() + "," + user.getEmail() + "," +
                    user.getUsername() + "," + user.getPassword() + "," + user.getBirthDate());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

        try {
            FileWriter fw = new FileWriter(DefaultValues.supervisorDir + "/" + supervisor.getSupervisorId() + "/utenti.txt");
            fw.write(user.getUserId() + ",");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

        File newMedicine = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/medicine");
        wasSuccessful = newMedicine.mkdirs();

        if (!wasSuccessful) {
            System.out.println("was not successful.");
        }
    }


    public void changeAvatar(int userKey) {
        File file = new File(DefaultValues.dir, "avatar_" + userKey + ".jpeg");
        file.delete();
        File from = new File(DefaultValues.dir, "avatar_" + userKey + "t.jpeg");
        File to = new File(DefaultValues.dir, "avatar_" + userKey + ".jpeg");
        from.renameTo(to);
    }

    public void deleteUser(User user) throws IOException {
        File toDelete = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId());
        Log.d("todelete", toDelete.toString());
        FileUtils.forceDelete(new File(String.valueOf(toDelete)));
    }

    public void editUser(User user) throws IOException {
        File userToEdit = new File(DefaultValues.usersDir.toString() + "/" + user.getUserId() + "/profile.txt");
        userToEdit.delete();

        FileOutputStream fos = new FileOutputStream(userToEdit);
        fos.flush();

        try {
            FileWriter fw = new FileWriter(userToEdit, true);
            fw.write(user.getUserId() + "," + user.getName() + "," + user.getSurname() + "," + user.getEmail() + "," +
                    user.getUsername() + "," + user.getPassword() + "," + user.getBirthDate());
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
