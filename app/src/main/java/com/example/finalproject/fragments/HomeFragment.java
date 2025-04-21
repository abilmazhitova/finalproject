package com.example.finalproject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.CartItem;
import com.example.finalproject.CartManager;
import com.example.finalproject.Product;
import com.example.finalproject.ProductAdapter;
import com.example.finalproject.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setHasFixedSize(true); // Ускоряет прокрутку
        recyclerView.setNestedScrollingEnabled(false); // Отключает вложенную прокрутку

        productList = new ArrayList<>();
        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);

        loadProducts(); // Загрузка продуктов из Firestore

        return view;
    }

    private void loadProducts() {
        // Получаем ссылку на коллекцию продуктов в Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("products");

        // Добавляем слушатель для обновления данных в реальном времени
        productsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                // Если произошла ошибка, выводим сообщение в логи
                Log.e("FirebaseError", "Ошибка при получении данных: " + error.getMessage());
                return;
            }

            // Очищаем список перед загрузкой новых данных
            productList.clear();

            // Проходим по всем документам в коллекции
            for (QueryDocumentSnapshot doc : value) {
                // Преобразуем документ в объект Product
                Product product = doc.toObject(Product.class);
                productList.add(product);
            }

            // Уведомляем адаптер, что данные обновились
            adapter.notifyDataSetChanged();
        });
    }
}
