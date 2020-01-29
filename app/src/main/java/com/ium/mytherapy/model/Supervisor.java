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
    private ArrayList<User> pazienti;

    public ArrayList<User> getPazienti() {
        return pazienti;
    }

    public void setPazienti(ArrayList<User> pazienti) {
        this.pazienti = pazienti;
    }

    private Supervisor(Parcel in) {
    }

    public static Creator<Supervisor> getCREATOR() {
        return CREATOR;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
