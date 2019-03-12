package com.homcooked.homecooked;

public class Posts {

    public String uid, postimage, description, name, date, time, foodName, photoKey;


    public Posts(String uid, String time, String date, String postimage, String description, String name, String foodName, String photoKey) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
        this.name = name;
        this.foodName = foodName;
        this.photoKey = photoKey;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


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

    public String getFoodName() {
        return foodName;
    }
}
