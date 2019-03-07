package com.example.levast.password;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.levast.password.MainActivity.user;


/**
 * Created by Levast on 01.03.2019.
 */

public class AlarmReceiver extends BroadcastReceiver {

    //set to true when the get query has return
    //private AtomicBoolean isUserDownloaded;
    private FirebaseFirestore firestoreDb;
    private User userToNotify;

    @Override
    public void onReceive(final Context context, Intent intent) {

        firestoreDb=FirebaseFirestore.getInstance();
        firestoreDb.collection(MainActivity.COLLECTION_USERS).document(intent.getStringExtra(NotificationAlarm.INTENT_LEVAST_PASSWORD_ID_USER))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userToNotify=documentSnapshot.toObject(User.class);

                        if(user!=null && user.getCurrentTest()!=null)
                        {
                            //we send a notif
                            sendNotification(context);

                            //user go to nextTry
                            userToNotify.getCurrentTest().nextTry();

                            firestoreDb.collection(MainActivity.COLLECTION_USERS).document(userToNotify.getDocumentName())
                                    .update("listTest",userToNotify.getListTest());
                        }

                    }
                });

        //intent.getIntExtra(NotificationAlarm.INTENT_LEVAST_PASSWORD_ID_USER,-1)
    }

    private void sendNotification(Context context)
    {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_ID_PAGE,MainActivity.SHOW_LOGIN);

// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context,MainActivity.CHANEl_ID);
        mBuilder.setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Reminder n°"+ userToNotify.getCurrentTest().getNumOfTry())
                .setSmallIcon(R.drawable.memory_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("do you remember your password?\ntest it now!");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, mBuilder.build());
    }


}
