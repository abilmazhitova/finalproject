package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class CartManager {
    private static List<CartItem> cart = new ArrayList<>();

    // Добавление товара в корзину
    public static void addItem(CartItem item) {
        for (CartItem cartItem : cart) {
            if (cartItem.getName().equals(item.getName())) {
                // Если товар уже есть в корзине, увеличиваем количество
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        // Если товара нет в корзине, добавляем новый
        cart.add(item);
    }

    // Получить все товары в корзине
    public static List<CartItem> getCartItems() {
        return cart;
    }

    // Получить общую сумму
    public static int getTotal() {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // Очистить корзину (удалить только выбранные товары)
    public static void clearSelectedItems() {
        Iterator<CartItem> iterator = cart.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.isSelected()) {  // Проверка на выбранный товар
                iterator.remove();  // Удаляем выбранный товар
            }
        }
    }

    // Очистить всю корзину
    public static void clearCart() {
        cart.clear();
    }
}
