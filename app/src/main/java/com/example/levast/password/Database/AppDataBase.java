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
@Database(entities = {User.class, ImageLegend.class}, version = 6)
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
                new ImageLegend(R.drawable.auto,"Auto", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.ball,"Ball", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.bike,"Bike", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.bird,"Bird", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.boy,"Boy", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.bug,"Bug", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.builder,"Builder", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.bush,"Bush", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.cat,"Cat", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.cow,"Cow", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.dog,"dog", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.fireman,"Fireman", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.girl,"Girl", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.horse,"Horse", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.man,"Man", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.moto,"Moto", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.policeman,"Policeman", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.sheep,"Sheep", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.tree,"Tree", ImageLegend.SUBJECT),
                new ImageLegend(R.drawable.truck,"Truck", ImageLegend.SUBJECT),

                //FORM
                new ImageLegend(R.drawable.big,"Big", ImageLegend.FORM),
                new ImageLegend(R.drawable.broad,"Broad", ImageLegend.FORM),
                new ImageLegend(R.drawable.clean,"Clean", ImageLegend.FORM),
                new ImageLegend(R.drawable.colourful,"CoulourFul", ImageLegend.FORM),
                new ImageLegend(R.drawable.dirty,"Dirty", ImageLegend.FORM),
                new ImageLegend(R.drawable.flat,"Flat", ImageLegend.FORM),
                new ImageLegend(R.drawable.grey,"Grey", ImageLegend.FORM),
                new ImageLegend(R.drawable.high,"High", ImageLegend.FORM),
                new ImageLegend(R.drawable.huge,"Huge", ImageLegend.FORM),
                new ImageLegend(R.drawable.inclined,"Inclined", ImageLegend.FORM),
                new ImageLegend(R.drawable.little,"Little", ImageLegend.FORM),
                new ImageLegend(R.drawable.verylong,"Long", ImageLegend.FORM),
                new ImageLegend(R.drawable.square,"Square", ImageLegend.FORM),
                new ImageLegend(R.drawable.minuscule,"Tiny", ImageLegend.FORM),
                new ImageLegend(R.drawable.newtechnologies,"New", ImageLegend.FORM),
                new ImageLegend(R.drawable.old,"Old", ImageLegend.FORM),
                new ImageLegend(R.drawable.oval,"Oval", ImageLegend.FORM),
                new ImageLegend(R.drawable.pink,"Pink", ImageLegend.FORM),
                new ImageLegend(R.drawable.round,"Round", ImageLegend.FORM),
                new ImageLegend(R.drawable.purple,"Purple", ImageLegend.FORM)
        );
    }

    public abstract UserDao userDao();
    public abstract ImageLegendDao imageLegendDao();
}
