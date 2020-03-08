package com.ium.mytherapy.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable, Comparable<User> {
    private int userId;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private String dataNascita;
    private String avatar;

    public User(Parcel in) {
        userId = in.readInt();
        nome = in.readString();
        cognome = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        dataNascita = in.readString();
        avatar = in.readString();
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

    public User() {
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userId);
        parcel.writeString(nome);
        parcel.writeString(cognome);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(dataNascita);
        parcel.writeString(avatar);
    }

    public int getUserId() {
        return userId;
    }

    @NonNull
    @Override
    public String toString() {
        return userId + " " + nome;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    @Override
    public int compareTo(User user) {
        return this.getNome().equals(user.getNome()) ? this.getCognome().compareTo(user.getCognome()) : this.getNome().compareTo(user.getNome());
    }
}
