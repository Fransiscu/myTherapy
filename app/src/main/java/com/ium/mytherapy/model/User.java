package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private int userId;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String dataNascita;
    private String avatar;

    public User() {
    }

//    public Terapia getTerapia() {
//        return terapia;
//    }

//    public void setTerapia(Terapia terapia) {
//        this.terapia = terapia;
//    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        nome = in.readString();
        cognome = in.readString();
        email = in.readString();
        password = in.readString();
        dataNascita = in.readString();
        avatar = in.readString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nome);
        parcel.writeString(cognome);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(dataNascita);
        parcel.writeString(avatar);
    }
}
