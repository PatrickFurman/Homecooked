package com.homcooked.homecooked;

public class Food_Item {
    private String name;
    private String description;
    private String nameOfUser;
    private int zipCode;

    public Food_Item(String name, String description, String nameOfUser, int zipCode) {
        this.name = name;
        this.description = description;
        this.nameOfUser = nameOfUser;
        this.zipCode = zipCode;
    }

    public String getName() { return name;}
    public String getDescription() {return description;}
    public String getNameOfUser() {return nameOfUser;}
    public int getzipCode() {return zipCode;}
}
