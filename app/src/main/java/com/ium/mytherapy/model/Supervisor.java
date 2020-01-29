package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Supervisor implements Parcelable {
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String password;
    private String dataNascita;
    private int supervisorId;
    private ArrayList<User> pazienti;

    public Supervisor() {
    }

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
        nome = in.readString();
        cognome = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
        dataNascita = in.readString();
        supervisorId = in.readInt();
        pazienti = in.createTypedArrayList(User.CREATOR);
    }

    int getSupervisorId() {
        return supervisorId;
    }

    public ArrayList<User> getPazienti() {
        return pazienti;
    }

    public void setPazienti(ArrayList<User> pazienti) {
        this.pazienti = pazienti;
    }

    public void setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
    }

    String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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

    String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(dataNascita);
        dest.writeInt(supervisorId);
        dest.writeTypedList(pazienti);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
