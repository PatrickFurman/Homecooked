package com.homcooked.homecooked;

public class Food_Item {
    private String name;
    private String description;
    private String seller;
    private int zipCode;

    public Food_Item(String name, String description, String seller, int zipCode) {
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.zipCode = zipCode;
    }

    public String getName() { return name;}
    public String getDescription() {return description;}
    public String getSeller() {return seller;}
    public int getzipCode() {return zipCode;}
}
