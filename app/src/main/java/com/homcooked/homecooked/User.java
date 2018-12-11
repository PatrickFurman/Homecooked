package com.homcooked.homecooked;

import com.google.firebase.database.DatabaseReference;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userID;

    public User(String name, String firstName, String lastName, String email,
                String password) {
        this.username = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserID() {return userID; }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
