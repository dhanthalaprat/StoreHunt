package com.example.numad22fa_team49_project.models;

import java.io.Serializable;

public class GeneralProductHome implements Serializable {
    String name, description, price, rating, date, time, image_uri, category, seller_id;

    public GeneralProductHome() {
    }
    public GeneralProductHome(String name, String description, String price, String rating, String date, String time, String image_uri, String category, String seller_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.date = date;
        this.time = time;
        this.image_uri = image_uri;
        this.category = category;
        this.seller_id = seller_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }
}
