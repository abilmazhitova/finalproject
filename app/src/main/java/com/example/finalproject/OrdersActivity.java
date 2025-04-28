package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setTitle("Мои заказы");

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, orderList);
        recyclerView.setAdapter(ordersAdapter);

        loadOrders();
    }

    private void loadOrders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Order> orders = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = document.toObject(Order.class);
                            orders.add(order);
                        }
                        // Set adapter to RecyclerView
                        OrdersAdapter ordersAdapter = new OrdersAdapter(this, orders);
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setAdapter(ordersAdapter);
                    } else {
                Log.w("OrdersActivity", "Error getting documents.", task.getException());
            }
        });
    }
}
