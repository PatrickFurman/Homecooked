package com.homcooked.homecooked;

import com.google.firebase.database.DatabaseReference;

public class User {
    private String name;
    private String email;
    private String password;
    private String userID;
    DatabaseReference usersRef;

    public User(String name, String email, String password, DatabaseReference usersRef) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.usersRef = usersRef;
    }

    public String getName() {
        return name;
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
