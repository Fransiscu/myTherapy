package com.ium.mytherapy.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String dataNascita;
    private int avatar;

    public static List<User> getObjectList() {
        List<User> users = new ArrayList<>();
        users = getUsers();

        for (int i = 0; i < users.length; i++) {
            User user = new User();
            user.setNome(users.get(i).getNome());
            user.setCognome(users.get(i).getCognome());
            users.add(user);
        }

        return users;
    }

    private static List<User> getUsers() {

        List<User> list = new ArrayList<>();

        User user = new User();
        user.setNome("John");
        user.setCognome("Smith");

        User more = new User();
        more.setNome("Lorenzo");
        more.setCognome("Piana");

        list.add(user);
        list.add(more);

        return list;
    }

    //    private Terapia terapia;

    public User() {
    }

//    public Terapia getTerapia() {
//        return terapia;
//    }

//    public void setTerapia(Terapia terapia) {
//        this.terapia = terapia;
//    }

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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

}
