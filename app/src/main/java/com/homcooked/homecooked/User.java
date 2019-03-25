package com.homcooked.homecooked;

public class User {
    private String username;
    private String email;
    private String password;
    private String userID;
    private int rating;
    private int numReviews;
    private int totalRating;

    public User(String name, String firstName, String lastName, String email,
                String password) {
        this.username = name;
        this.email = email;
        this.password = password;
    }
    public User (String name, String email) {
        this.username = name;
        this.email = email;
    }

    public int getRating() {return rating;}

    public int getNumReviews() {return numReviews;}

    public int getTotalRating() {return totalRating;}

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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTotalRating(int rating) {
        this.totalRating = rating;
    }

    public void setNumReviews(int num) {
        this.numReviews = num;
    }
}
