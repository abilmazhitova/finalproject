package com.example.finalproject;

public class CartItem {
    private String name;
    private String img;
    private int price;
    private int quantity;

    // Конструктор с четырьмя параметрами
    public CartItem(String name, int price, String img, int quantity) {
        this.name = name;
        this.img = img;
        this.price = price;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public String getName() { return name; }
    public String getImg() { return img; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setName(String name) { this.name = name; }
    public void setImg(String img) { this.img = img; }
    public void setPrice(int price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
