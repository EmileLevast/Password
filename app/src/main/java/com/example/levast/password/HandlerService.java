package com.example.levast.password;

import android.content.Context;
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

        Log.w("msg","ok");
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder((Context) msg.obj,MainActivity.CHANEl_ID);
        mBuilder.setAutoCancel(true)
                .setContentTitle("Reminder n°"+msg.arg1)
                .setSmallIcon(R.drawable.memory_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("do you remember your password?\ntest it now!");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from((Context) msg.obj);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());

    }
}