package com.example.levast.password.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import com.example.levast.password.User;

/**
 * Created by Levast on 13.02.2019.
 */


public interface genericDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(T... element);

    @Delete
    void delete(T element);

    @Update
    void update(T... element);
}
