package com.example.cupcakes;

public class MainModel {

    String name,category,description,price,curl;

    MainModel(){}

    public MainModel(String name, String category, String description, String price, String curl) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.curl = curl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }
}
