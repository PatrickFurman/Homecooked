package com.homcooked.homecooked;

public class Pet_Item {
    private String date;
    private String description;
    private String petName;
    private String postImage;
    private String time;
    private String seller;

    public Pet_Item(String date, String description, String petName, String photoKey, String time, String seller) {
        this.date = date;
        this.description = description;
        this.seller = seller;
        this.postImage = photoKey;
        this.petName = petName;
        this.time = time;
    }

    public String getName() { return petName;}
    public String getDescription() {return description;}
    public String getPhotoKey() {return postImage;}
    public String getSeller() {return seller;}
}
