package com.example.finalproject;

public class Product {
    private String name;
    private String img;
    private int price;
    private String category;

    public Product() {

    }
    public Product(String name, String img, int price, String category) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.category = category;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public String getImg() { return img; }
    public int getPrice() { return price; }
    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }
    public void setImg(String img) { this.img = img; }
    public void setPrice(int price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
}
