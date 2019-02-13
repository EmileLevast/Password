package com.example.levast.password.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.levast.password.User;

import java.util.concurrent.Executors;

/**
 * Created by Levast on 08.02.2019.
 */

@TypeConverters({Converter.class})
@Database(entities = {User.class}, version = 3)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;
    private static final String NAME_DATABASE="NAME_DATABASE";

    public static AppDataBase getDataBase(Context context)
    {
        if(INSTANCE==null)
        {
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, NAME_DATABASE)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
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
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO insert Image with legend
                            }
                    });
                }}
                )
                .build();
    }

    public abstract UserDao userDao();
}
