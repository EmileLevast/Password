package com.example.levast.password.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.levast.password.User;

import java.util.List;

/**
 * Created by Levast on 08.02.2019.
 */

@Dao
public interface UserDao extends genericDao<User> {

    @Query("SELECT * FROM user")
    List<User> getAll();//return a list with all the users currently registered in the database

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    //recuperer le premier element de la base de donnee
    @Query("SELECT * FROM user LIMIT 1")
    User loadFirstRow();



}
