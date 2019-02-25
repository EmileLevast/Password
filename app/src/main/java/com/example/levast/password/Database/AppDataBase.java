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
@Database(entities = {User.class, ImageLegend.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;
    private static final String NAME_DATABASE="NAME_DATABASE";

    public static AppDataBase getDataBase(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE= buildDataBase(context);

            //TODO not sure about that
            //we do a first call to create the database if it's not the case
            INSTANCE.userDao().loadFirstRow();

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
                new ImageLegend(R.drawable.purple,"Purple", ImageLegend.FORM),


                //VERB
                new ImageLegend(R.drawable.climb,"Climb", ImageLegend.VERB),
                new ImageLegend(R.drawable.dance,"Dance", ImageLegend.VERB),
                new ImageLegend(R.drawable.dream,"Dream", ImageLegend.VERB),
                new ImageLegend(R.drawable.drink,"Drink", ImageLegend.VERB),
                new ImageLegend(R.drawable.eat,"Eat", ImageLegend.VERB),
                new ImageLegend(R.drawable.fall,"Fall", ImageLegend.VERB),
                new ImageLegend(R.drawable.go,"go", ImageLegend.VERB),
                new ImageLegend(R.drawable.itsmells,"It smells", ImageLegend.VERB),
                new ImageLegend(R.drawable.jump,"Jump", ImageLegend.VERB),
                new ImageLegend(R.drawable.lie,"Lie", ImageLegend.VERB),
                new ImageLegend(R.drawable.race,"Race", ImageLegend.VERB),
                new ImageLegend(R.drawable.roll,"Roll", ImageLegend.VERB),
                new ImageLegend(R.drawable.run,"Run", ImageLegend.VERB),
                new ImageLegend(R.drawable.show,"Show", ImageLegend.VERB),
                new ImageLegend(R.drawable.sleep,"Sleep", ImageLegend.VERB),
                new ImageLegend(R.drawable.smell,"smell", ImageLegend.VERB),
                new ImageLegend(R.drawable.stand,"Stand", ImageLegend.VERB),
                new ImageLegend(R.drawable.stay,"Stay", ImageLegend.VERB),
                new ImageLegend(R.drawable.descend,"Descend", ImageLegend.VERB),
                new ImageLegend(R.drawable.sprint,"Sprint", ImageLegend.VERB),


                //PLACE
                new ImageLegend(R.drawable.beach,"Beach", ImageLegend.PLACE),
                new ImageLegend(R.drawable.castle,"Castle", ImageLegend.PLACE),
                new ImageLegend(R.drawable.earth,"Earth", ImageLegend.PLACE),
                new ImageLegend(R.drawable.familyhouse,"Field", ImageLegend.PLACE),
                new ImageLegend(R.drawable.field,"Field", ImageLegend.PLACE),
                new ImageLegend(R.drawable.footpath,"Footpath", ImageLegend.PLACE),
                new ImageLegend(R.drawable.forest,"Forest", ImageLegend.PLACE),
                new ImageLegend(R.drawable.garden,"Garden", ImageLegend.PLACE),
                new ImageLegend(R.drawable.heaven,"Heaven", ImageLegend.PLACE),
                new ImageLegend(R.drawable.highway,"Highway", ImageLegend.PLACE),
                new ImageLegend(R.drawable.lake,"Lake", ImageLegend.PLACE),
                new ImageLegend(R.drawable.pasture,"Pasture", ImageLegend.PLACE),
                new ImageLegend(R.drawable.pond,"Pond", ImageLegend.PLACE),
                new ImageLegend(R.drawable.river,"River", ImageLegend.PLACE),
                new ImageLegend(R.drawable.sea,"Sea", ImageLegend.PLACE),
                new ImageLegend(R.drawable.street,"Street", ImageLegend.PLACE),
                new ImageLegend(R.drawable.supermarket,"Supermarket", ImageLegend.PLACE),
                new ImageLegend(R.drawable.trainstation,"TrainStation", ImageLegend.PLACE),
                new ImageLegend(R.drawable.villa,"Villa", ImageLegend.PLACE),
                new ImageLegend(R.drawable.water,"Water", ImageLegend.PLACE),

                new ImageLegend(R.drawable.afternoon,"Afternoon", ImageLegend.TIME),
                new ImageLegend(R.drawable.always,"Always", ImageLegend.TIME),
                new ImageLegend(R.drawable.autumn,"Autumn", ImageLegend.TIME),
                new ImageLegend(R.drawable.evening,"Evening", ImageLegend.TIME),
                new ImageLegend(R.drawable.fog,"Fog", ImageLegend.TIME),
                new ImageLegend(R.drawable.midday,"Midday", ImageLegend.TIME),
                new ImageLegend(R.drawable.morning,"Morning", ImageLegend.TIME),
                new ImageLegend(R.drawable.never,"Never", ImageLegend.TIME),
                new ImageLegend(R.drawable.night,"Night", ImageLegend.TIME),
                new ImageLegend(R.drawable.often,"Often", ImageLegend.TIME),
                new ImageLegend(R.drawable.rain,"Rain", ImageLegend.TIME),
                new ImageLegend(R.drawable.rare,"Rare", ImageLegend.TIME),
                new ImageLegend(R.drawable.snow,"Snow", ImageLegend.TIME),
                new ImageLegend(R.drawable.sometimes,"Sometimes", ImageLegend.TIME),
                new ImageLegend(R.drawable.storm,"Storm", ImageLegend.TIME),
                new ImageLegend(R.drawable.summer,"Summer", ImageLegend.TIME),
                new ImageLegend(R.drawable.sunshine,"Sunshine", ImageLegend.TIME),
                new ImageLegend(R.drawable.weekend,"Week-end", ImageLegend.TIME),
                new ImageLegend(R.drawable.wind,"Wind", ImageLegend.TIME),
                new ImageLegend(R.drawable.winter,"Winter", ImageLegend.TIME)


                );


    }

    public abstract UserDao userDao();
    public abstract ImageLegendDao imageLegendDao();
}
