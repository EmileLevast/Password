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

    //each time we create a new test , this id increase itself in order to avoid updating the previous alarm
    private static int NUM_OF_CURRENT_ID=1;

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
        NUM_OF_CURRENT_ID++;
    }

    public static void scheduleAlarmFrom(Context context, String documentName, int begin,String nameTest)
    {
        Intent intent=new Intent(context,AlarmReceiver.class);
        intent.putExtra(INTENT_LEVAST_PASSWORD_ID_USER,documentName);
        intent.putExtra(MainActivity.INTENT_LEVAST_PASSWORD_NAME_TEST,nameTest);
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);




        //we create a different id for each pending intent in order to avoid update of the previous programmed intent
        for(int i=begin;i<timeOfRetry.length;i++)
        {
            //add the value of the test to make a difference between alarm and avoid updating previous one
            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,i+NUM_OF_CURRENT_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC,System.currentTimeMillis()+timeOfRetry[i],pendingIntent);

        }
    }
}
