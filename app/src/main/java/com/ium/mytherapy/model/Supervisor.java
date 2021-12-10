package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class Supervisor implements Parcelable {

    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private String birthDate;
    private int supervisorId;
    private ArrayList<User> patients;

    public static final Creator<Supervisor> CREATOR = new Creator<Supervisor>() {
        @Override
        public Supervisor createFromParcel(Parcel in) {
            return new Supervisor(in);
        }

        @Override
        public Supervisor[] newArray(int size) {
            return new Supervisor[size];
        }
    };

    private Supervisor(Parcel in) {
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
        birthDate = in.readString();
        supervisorId = in.readInt();
        patients = in.createTypedArrayList(User.CREATOR);
    }

    public Supervisor() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(birthDate);
        dest.writeInt(supervisorId);
        dest.writeTypedList(patients);
    }

    @NonNull
    @Override
    public String toString() {
        return "Supervisor = " + this.username;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
