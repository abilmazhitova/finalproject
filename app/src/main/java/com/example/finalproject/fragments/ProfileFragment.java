package com.example.finalproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.finalproject.LoginActivity;
import com.example.finalproject.OrdersActivity;
import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private TextView userNameText, userEmailText, userPhoneText, userRegistrationDate, changeAvatar;
    private Button logoutButton, myOrdersButton, loginButton;
    private ImageView profileAvatar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userNameText = view.findViewById(R.id.user_name);
        userEmailText = view.findViewById(R.id.user_email);
        userPhoneText = view.findViewById(R.id.user_phone);
        userRegistrationDate = view.findViewById(R.id.user_registration_date);
        logoutButton = view.findViewById(R.id.logout_button);
        myOrdersButton = view.findViewById(R.id.my_orders_button);
        loginButton = view.findViewById(R.id.login_button);
        profileAvatar = view.findViewById(R.id.profile_avatar);
        changeAvatar = view.findViewById(R.id.change_avatar);

        checkUserStatus();

        changeAvatar.setOnClickListener(v -> openFileChooser());

        myOrdersButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrdersActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            checkUserStatus();
        });

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        return view;
    }

    private void checkUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userEmailText.setText("Email: " + user.getEmail());

            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    userNameText.setText("Имя: " + documentSnapshot.getString("name"));
                    userPhoneText.setText("Телефон: " + documentSnapshot.getString("phone"));

                    long regDate = documentSnapshot.getLong("registration_date");
                    if (regDate > 0) {
                        String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(regDate));
                        userRegistrationDate.setText("Дата регистрации: " + formattedDate);
                    }
                }
            });

            logoutButton.setVisibility(View.VISIBLE);
            myOrdersButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);

            // Загружаем фото профиля
            storageReference.child("avatars/" + user.getUid() + ".jpg").getDownloadUrl()
                    .addOnSuccessListener(uri -> Glide.with(this).load(uri).into(profileAvatar))
                    .addOnFailureListener(e -> profileAvatar.setImageResource(R.drawable.ic_default_avatar));

        } else {
            userEmailText.setText("Email: -");
            userNameText.setText("Имя: -");
            userPhoneText.setText("Телефон: -");
            userRegistrationDate.setText("Дата регистрации: -");

            logoutButton.setVisibility(View.GONE);
            myOrdersButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileAvatar.setImageURI(imageUri);
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                StorageReference fileRef = storageReference.child("avatars/" + user.getUid() + ".jpg");
                fileRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(this)
                                    .load(uri)
                                    .into(profileAvatar);

                            // Обновляем ссылку на аватар в Firestore
                            db.collection("users").document(user.getUid())
                                    .update("photoUrl", uri.toString());

                            Toast.makeText(getContext(), "Фото обновлено!", Toast.LENGTH_SHORT).show();
                        }))
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Ошибка загрузки!", Toast.LENGTH_SHORT).show());
            }
        }
    }
}
