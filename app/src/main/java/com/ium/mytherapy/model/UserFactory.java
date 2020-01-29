package com.ium.mytherapy.model;

import java.util.ArrayList;

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

    public User verifyUser() {
        return null;
    }

    public User getUser(ArrayList<User> userArrayList) {
        return null;
    }


}
