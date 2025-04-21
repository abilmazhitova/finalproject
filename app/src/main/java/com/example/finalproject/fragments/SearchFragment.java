package com.example.finalproject.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Product;
import com.example.finalproject.ProductAdapter;
import com.example.finalproject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText searchInput;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> fullProductList = new ArrayList<>();
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.search_input);
        recyclerView = view.findViewById(R.id.search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProductAdapter(getContext(), fullProductList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadProducts();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadProducts() {
        db.collection("products").get().addOnSuccessListener(queryDocumentSnapshots -> {
            fullProductList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Product product = doc.toObject(Product.class);
                fullProductList.add(product);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : fullProductList) {
            String name = product.getName().toLowerCase();
            String category = product.getCategory().toLowerCase();
            String lowerQuery = query.toLowerCase();

            if (name.contains(lowerQuery) || category.contains(lowerQuery)) {
                filteredList.add(product);
            }
        }
        adapter = new ProductAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);
    }

}
