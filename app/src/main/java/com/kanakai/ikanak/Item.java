package com.kanakai.ikanak;

public class Item {
    private String name;
    private String category;
    private double price;
    private String imageUrl;

    public Item() {} // Needed for Firebase

    public Item(String name, String category, double price, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
