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
        userID = "1"; // change to equal usersRef.child("Users").numOfUsers;
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
}
