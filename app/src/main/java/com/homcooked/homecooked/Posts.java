package com.homcooked.homecooked;

public class Posts {

    public String uid, postimage, description, name, date, time, petName, photoKey;
    public double latitude, longitude;


    public Posts(String uid, String time, String date, String postimage, String description, String name, String petName, String photoKey, double latitude, double longitude) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
        this.name = name;
        this.petName = petName;
        this.photoKey = photoKey;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpetName() {
        return petName;
    }
}
