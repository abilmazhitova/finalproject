package com.example.finalproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.CartAdapter;
import com.example.finalproject.CartItem;
import com.example.finalproject.CartManager;
import com.example.finalproject.R;

import java.util.List;

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
            // Логика оформления заказа
            CartManager.clearCart();
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            totalTextView.setText("Общая сумма: 0 KZT");
        });

        return view;
    }
}
