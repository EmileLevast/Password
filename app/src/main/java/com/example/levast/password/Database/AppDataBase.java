package com.example.levast.password.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.levast.password.ImageLegend;

import com.example.levast.password.R;
import com.example.levast.password.User;

import java.util.concurrent.Executors;

/**
 * Created by Levast on 08.02.2019.
 */

@TypeConverters({Converter.class})
@Database(entities = {User.class, ImageLegend.class}, version = 5)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;
    private static final String NAME_DATABASE="NAME_DATABASE";

    public static AppDataBase getDataBase(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE= buildDataBase(context);

        }



        return INSTANCE;
    }

    private static AppDataBase buildDataBase(final Context context)
    {
        return Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, NAME_DATABASE)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Log.w("msg","create database");
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                prepopulateData(context);
                            }
                    });
                }}
                )
                .build();
    }

    private static void prepopulateData(final Context context)
    {
        getDataBase(context).imageLegendDao().insertAll(

                //Subject
                new ImageLegend(R.drawable.auto,"Auto"),
                new ImageLegend(R.drawable.ball,"Ball"),
                new ImageLegend(R.drawable.bike,"Bike"),
                new ImageLegend(R.drawable.bird,"Bird"),
                new ImageLegend(R.drawable.boy,"Boy"),
                new ImageLegend(R.drawable.bug,"Bug"),
                new ImageLegend(R.drawable.builder,"Builder"),
                new ImageLegend(R.drawable.bush,"Bush"),
                new ImageLegend(R.drawable.cat,"Cat"),
                new ImageLegend(R.drawable.cow,"Cow"),
                new ImageLegend(R.drawable.dog,"dog"),
                new ImageLegend(R.drawable.fireman,"Fireman"),
                new ImageLegend(R.drawable.girl,"Girl"),
                new ImageLegend(R.drawable.horse,"Horse"),
                new ImageLegend(R.drawable.man,"Man"),
                new ImageLegend(R.drawable.moto,"Moto"),
                new ImageLegend(R.drawable.policeman,"Policeman"),
                new ImageLegend(R.drawable.sheep,"Sheep"),
                new ImageLegend(R.drawable.tree,"Tree"),
                new ImageLegend(R.drawable.truck,"Truck")

        );
    }

    public abstract UserDao userDao();
    public abstract ImageLegendDao imageLegendDao();
}
