package com.example.postapp.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.postapp.R;
import com.example.postapp.authentication.LoginActivity;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "NOTIFICATION")) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            Intent newIntent = new Intent(context, LoginActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);
            String notMessage = sharedPreferences.getString(Constants.NOTIF_MESSAGE, "You have an appointment tomorrow.");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "postapp")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(notMessage)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notMessage))
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(100, builder.build());
        }
    }
}



