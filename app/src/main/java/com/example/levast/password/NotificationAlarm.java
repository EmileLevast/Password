package com.example.levast.password;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Levast on 01.03.2019.
 */

public class NotificationAlarm {
    //indicates to the service which user w want to load
    public final static String INTENT_LEVAST_PASSWORD_ID_USER ="INTENT_LEVAST_PASSWORD_ID_USER";

    public static final long[] timeOfRetry=new long[]{

            //values for test only
            1000*30,
            1000*120,
            1000*300,
            1000*600,
            1000*1000,



            /*1000*60*10,//10 min first try
            1000*60*60*24,//24 h
            1000*60*60*24*4,//4 jours
            1000*60*60*24*7,//1 week
            1000*60*60*24*16,//16 jours
            18144000000L//1 month*/

    };



    public static void createAlarm(Context context, String documentName,String nameTest)
    {
       scheduleAlarmFrom(context,documentName,0,nameTest);
    }

    public static void scheduleAlarmFrom(Context context, String documentName, int begin,String nameTest)
    {
        Intent intent=new Intent(context,AlarmReceiver.class);
        intent.putExtra(INTENT_LEVAST_PASSWORD_ID_USER,documentName);
        intent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_NAME_TEST,nameTest);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for(int i=begin;i<timeOfRetry.length;i++)
        {
            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,i,intent,PendingIntent.FLAG_ONE_SHOT);
            alarmManager.cancel(pendingIntent);

            alarmManager.set(AlarmManager.RTC,System.currentTimeMillis()+timeOfRetry[i],pendingIntent);

        }
    }
}
