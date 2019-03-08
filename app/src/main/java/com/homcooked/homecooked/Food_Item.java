package com.homcooked.homecooked;

public class Food_Item {
    private String name;
    private String description;
    private String seller;
    private String photoKey;
    private double longitude;
    private double latitude;

    public Food_Item(String name, String description, String seller, String photoKey, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.photoKey = photoKey;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name;}
    public String getDescription() {return description;}
    public String getPhotoKey() {return photoKey;}
    public String getSeller() {return seller;}
}
