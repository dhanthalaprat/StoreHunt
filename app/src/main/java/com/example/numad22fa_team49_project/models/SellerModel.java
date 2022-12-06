package com.example.numad22fa_team49_project.models;

import java.util.ArrayList;

public class SellerModel {
    public SellerModel() {
    }
    String name;
    ArrayList<GeneralProductHome> products;

    public SellerModel(String name, ArrayList<GeneralProductHome> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<GeneralProductHome> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<GeneralProductHome> products) {
        this.products = products;
    }
}
