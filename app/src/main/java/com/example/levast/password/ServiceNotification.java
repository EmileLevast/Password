package com.example.levast.password;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.os.Message;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.levast.password.Database.AppDataBase;

/**
 * Created by Levast on 19.02.2019.
 */

public class ServiceNotification extends Service {

    private HandlerService mHandler;
    volatile private boolean isRunning;
    private boolean isAlive;
    private AppDataBase roomDB;
    volatile private User user;
    Thread thread;
    private Context context;

    private final static long TIME_SLEEP=3000;

    //the delay between each try of the password in millis
    private long[] timeOfRetry=new long[]{
/*
            1000,
            1000*5,
            1000*20,
            1000*30,
            1000*60,
            1000*90,*/
            1000*30,
            1000*60*10,//10 min first try
            1000*60*60*24,//24 h
            1000*60*60*24*4,//4 jours
            1000*60*60*24*7,//1 week
            1000*60*60*24*16,//16 jours
            18144000000L//1 month
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("msg","service created");
        isRunning=false;
        isAlive =false;

        mHandler=new HandlerService();
        roomDB=AppDataBase.getDataBase(this);
        context=this;

        thread=new Thread(new Runnable() {

            @Override
            public void run() {

                while(isAlive)
                {
                    Log.w("msg","is Alive > isRunning="+isRunning+ " user:"+user);
                    if (isRunning&& user!=null)
                    {
                        Log.w("msg","running");
                        if(System.currentTimeMillis() - user.getTimeFirstTry()>timeOfRetry[user.getNumOftry()])
                        {
                            Log.w("msg","sending");
                            Message message=mHandler.obtainMessage();
                            message.arg1= user.getNumOftry();
                            message.obj=context;
                            mHandler.sendMessage(message);


                            user.nextTry();
                            if(user.getNumOftry()>=timeOfRetry.length)
                            {
                                user.setNumOftry(0);
                                user.setTimeFirstTry(System.currentTimeMillis());
                            }

                            //We update only the field that we change here (else we remove the stat such as nbr of Failure)
                            roomDB.userDao().updateTryUser(user.getTimeFirstTry(),user.getNumOftry(),user.id);
                        }

                    }

                    try {
                        Thread.sleep(TIME_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("msg","service start");

        if(!thread.isAlive())
        {
            user=roomDB.userDao().loadAllById(intent.getIntExtra(MainActivity.INTENT_LEVAST_PASSWORD_ID_USER,-1));
            isAlive=true;
            isRunning=true;
            Log.w("msg","thread start");
            thread.start();
        }



        return START_REDELIVER_INTENT;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.w("msg","service destroy");
        super.onDestroy();
    }
}
