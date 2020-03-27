package it.chiarani.meteotrentino.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.views.HomeActivity;

public class FirebasePushNotificationService extends FirebaseMessagingService {
    @Override public void onMessageReceived(RemoteMessage remoteMessage) {
        // setNotification(32);
    }

    private void setNotification(long beaconsFoundSoFar) {
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, HomeActivity.class), 0);

        // Se il parametro della funzione è -1 visualizzo un messaggio di errore sulla notifica
        String notificationTitle = "Beacon detection";
        String notificationMessage = "collecting... ";

        if(beaconsFoundSoFar == 0) {
            notificationTitle = "Beacon detection";
            notificationMessage = "End.";
        }

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        Notification notification = new NotificationCompat.Builder(this, "misc")
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "misc",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
