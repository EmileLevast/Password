package com.example.levast.password;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Levast on 01.03.2019.
 */

public class NotificationAlarm {
    //indicates to the service which user w want to load
    public final static String INTENT_LEVAST_PASSWORD_ID_USER ="INTENT_LEVAST_PASSWORD_ID_USER";
    public final static String INTENT_LEVAST_PASSWORD_ID_TEST_PENDINGINTENT="INTENT_LEVAST_PASSWORD_ID_TEST_PENDINGINTENT";

    public static final long[] timeOfRetry=new long[]{

            //values for test only
            /*1000*30,
            1000*120,
            1000*300,
            1000*600,
            1000*1000,*/



            1000*60*10,//10 min first try
            1000*60*60*24,//24 h
            1000*60*60*24*4,//4 jours
            1000*60*60*24*7,//1 week
            1000*60*60*24*16,//16 jours
            18144000000L//1 month

    };



    public static void createAlarm(Context context, String documentName,String nameTest,int idTest)
    {
        scheduleAlarmFrom(context,documentName,0,nameTest,idTest);
    }

    public static void scheduleAlarmFrom(Context context, String documentName, int begin,String nameTest,int idTest)
    {
        Intent intent=new Intent(context,AlarmReceiver.class);
        intent.putExtra(INTENT_LEVAST_PASSWORD_ID_USER,documentName);
        intent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_NAME_TEST,nameTest);
        intent.putExtra(INTENT_LEVAST_PASSWORD_ID_TEST_PENDINGINTENT,idTest);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);




        //we create a different id for each pending intent in order to avoid update of the previous programmed intent
        for(int i=begin;i<timeOfRetry.length;i++)
        {
            //add the value of the test to make a difference between alarm and avoid updating previous one
            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,i+idTest*timeOfRetry.length,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC,System.currentTimeMillis()+timeOfRetry[i],pendingIntent);

        }
    }
}
