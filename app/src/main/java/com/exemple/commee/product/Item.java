package com.exemple.commee.product;

import android.os.StrictMode;

public class Item {

    private String name, category;
    private long ID;
    private float price;
    private String imageUrl, description;

    public Item(String name, String category, long ID, float price, String image, String description) {
        this.name = name;
        this.category = category;
        this.ID = ID;
        this.price = price;
        this.imageUrl = image;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public long getID() {
        return ID;
    }

    public float getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
