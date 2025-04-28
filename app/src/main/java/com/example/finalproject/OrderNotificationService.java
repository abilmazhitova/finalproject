package com.example.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Service;
import android.os.IBinder;

public class OrderNotificationService extends Service {

    private static final String CHANNEL_ID = "order_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        // Создание канала уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Notifications";
            String description = "Notifications for Order Status";
            int importance = NotificationManager.IMPORTANCE_HIGH;  // Установите высокий приоритет
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification();
        return START_NOT_STICKY;
    }

    private void sendNotification() {
        // Устанавливаем URI для кастомного звука
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound);


        // Создание уведомления с кастомным звуком
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Ваш заказ принят!")
                .setContentText("Ваш заказ в пути или с вами свяжутся.")
                .setSmallIcon(R.drawable.ic_order)  // Используйте ваш ресурс иконки
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setSound(soundUri)  // Устанавливаем кастомный звук
                .build();

        // Отправка уведомления
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
