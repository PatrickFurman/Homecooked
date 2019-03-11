package com.homcooked.homecooked;

public class Food_Item {
    private String date;
    private String description;
    private String foodName;
    private String postImage;
    private String time;
    private String seller;

    public Food_Item(String date, String description, String foodName, String photoKey, String time, String seller) {
        this.date = date;
        this.description = description;
        this.seller = seller;
        this.postImage = photoKey;
        this.foodName = foodName;
        this.time = time;
    }

    public String getName() { return foodName;}
    public String getDescription() {return description;}
    public String getPhotoKey() {return postImage;}
    public String getSeller() {return seller;}
}
