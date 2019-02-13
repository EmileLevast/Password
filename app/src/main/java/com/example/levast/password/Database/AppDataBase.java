package com.example.levast.password.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.levast.password.User;

/**
 * Created by Levast on 08.02.2019.
 */

@TypeConverters({Converter.class})
@Database(entities = {User.class}, version = 2)
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

    public abstract UserDao userDao();
}
