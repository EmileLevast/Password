package com.example.levast.password;


import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Message;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by Levast on 19.02.2019.
 */

public class HandlerService extends Handler {



    public HandlerService() {
        super();
    }



    //msg.obj= Context for the notification
    //msg.arg1 =num of test
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);



        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent((Context) msg.obj, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_ID_PAGE,MainActivity.SHOW_LOGIN);

// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create((Context) msg.obj);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder((Context) msg.obj,MainActivity.CHANEl_ID);
        mBuilder.setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Reminder n°"+msg.arg1)
                .setSmallIcon(R.drawable.memory_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("do you remember your password?\ntest it now!");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from((Context) msg.obj);

        Log.w("msg","ok");
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());

    }


}
