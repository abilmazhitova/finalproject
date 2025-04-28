package com.example.finalproject;

import java.util.List;

public class Order {
    private List<Item> items;
    private int total;

    // Getter and Setter methods
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

