package com.example.levast.password.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.levast.password.ImageLegend;

import java.util.List;

/**
 * Created by Levast on 13.02.2019.
 */

@Dao
public interface ImageLegendDao extends genericDao<ImageLegend>{
    @Query("SELECT * FROM ImageLegend")
    List<ImageLegend> getAll();//return a list with all the users currently registered in the database

    @Query("SELECT idImage FROM ImageLegend")
    List<Integer> getAllId();

    @Query("SELECT * FROM ImageLegend WHERE theme = (:theme)")
    List<ImageLegend> getAllImageTheme(String theme);
}
