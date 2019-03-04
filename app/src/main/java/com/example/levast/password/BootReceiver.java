package com.example.levast.password;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaActionSound;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Levast on 04.03.2019.
 *
 * If the phone shut down we have to reschedule the current alarm
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        //we want to reschedule all the alarms
        //we prepare firestore
        FirebaseFirestore firestoreDB=FirebaseFirestore.getInstance();

        //we get the document name currently saved in sharedpreferences
        SharedPreferences sharedPreferences=context.getSharedPreferences(MainActivity.NAME_SHARED_PREFERENCE,Context.MODE_PRIVATE);
        final String documentName=sharedPreferences.getString(MainActivity.KEY_USER_DOCUMENT_NAME,"");


        //we download this document , to know which alarm we have to reschedule
        firestoreDB.collection(MainActivity.COLLECTION_USERS).document(documentName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user=documentSnapshot.toObject(User.class);

                        //if the user had a test begun
                        if(user!=null&&user.getNumOftry()>0)
                        {
                            NotificationAlarm.createAlarmFrom(context,user.getDocumentName(),user.getNumOftry()-1);
                        }
                    }
                });

    }

}
