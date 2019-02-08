package com.example.levast.password.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.levast.password.User;

import java.util.List;

/**
 * Created by Levast on 08.02.2019.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

}
