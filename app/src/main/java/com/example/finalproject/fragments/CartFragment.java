package com.example.finalproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.CartAdapter;
import com.example.finalproject.CartItem;
import com.example.finalproject.CartManager;
import com.example.finalproject.OrderNotificationService;
import com.example.finalproject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItems = CartManager.getCartItems();
        cartAdapter = new CartAdapter(getContext(), cartItems);
        recyclerView.setAdapter(cartAdapter);

        // Обновление общей суммы
        TextView totalTextView = view.findViewById(R.id.total_price);
        totalTextView.setText("Общая сумма: " + CartManager.getTotal() + " KZT");

        // Кнопка оформления заказа
        Button checkoutButton = view.findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(v -> {
            addItemsToFirestore();  // Добавление товаров в Firestore
            startOrderNotificationService(); // Запуск уведомления
        });

        return view;
    }

    private void addItemsToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ordersRef = db.collection("orders");

        // Создаем список товаров для заказа
        List<Map<String, Object>> cartItems = new ArrayList<>();
        for (CartItem item : CartManager.getCartItems()) {
            Map<String, Object> cartItemData = new HashMap<>();
            cartItemData.put("name", item.getName());
            cartItemData.put("price", item.getPrice());
            cartItemData.put("quantity", item.getQuantity());
            cartItemData.put("img", item.getImg());
            cartItems.add(cartItemData);
        }

        // Добавляем данные заказа в коллекцию Firestore
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("items", cartItems);
        orderData.put("total", CartManager.getTotal());

        // Добавляем заказ в Firestore
        ordersRef.add(orderData).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Заказ успешно оформлен", Toast.LENGTH_SHORT).show();
            CartManager.clearCart();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Ошибка при оформлении заказа", Toast.LENGTH_SHORT).show();
        });
    }

    private void startOrderNotificationService() {
        // Запуск ForegroundService для отправки уведомления
        Intent intent = new Intent(getContext(), OrderNotificationService.class);
        getContext().startService(intent);
    }
}
