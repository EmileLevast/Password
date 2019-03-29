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

/**
 * Created by Levast on 01.03.2019.
 */

public class AlarmReceiver extends BroadcastReceiver {

    //set to true when the get query has return
    //private AtomicBoolean isUserDownloaded;
    private FirebaseFirestore firestoreDb;
    private User userToNotify;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        firestoreDb=FirebaseFirestore.getInstance();
        firestoreDb.collection(MainActivity.COLLECTION_USERS).document(intent.getStringExtra(NotificationAlarm.INTENT_LEVAST_PASSWORD_ID_USER))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userToNotify=documentSnapshot.toObject(User.class);

                        if(userToNotify!=null)
                        {
                            String nameTest=intent.getStringExtra(MainActivity.INTENT_LEVAST_PASSWORD_NAME_TEST);
                            //we send a notif
                            sendNotification(context,nameTest,intent.getIntExtra(NotificationAlarm.INTENT_LEVAST_PASSWORD_ID_TEST_PENDINGINTENT,1));

                            firestoreDb.collection(MainActivity.COLLECTION_USERS).document(userToNotify.getDocumentName())
                                    .update("listTest",userToNotify.getListTest());
                        }

                    }
                });

        //intent.getIntExtra(NotificationAlarm.INTENT_LEVAST_PASSWORD_ID_USER,-1)
    }

    private void sendNotification(Context context,String nameTest,int idNotification)
    {
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_ID_PAGE,MainActivity.SHOW_LOGIN);
        resultIntent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_NAME_TEST,nameTest);

// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(userToNotify.getTestWithName(nameTest).getId(), PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context,MainActivity.CHANEl_ID);
        mBuilder.setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("Test n°"+ userToNotify.getTestWithName(nameTest).getNumOfTry()+" of \""+nameTest+"\"")
                .setSmallIcon(R.drawable.memory_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("do you remember your password?\ntest it now!");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(idNotification, mBuilder.build());
    }


}
