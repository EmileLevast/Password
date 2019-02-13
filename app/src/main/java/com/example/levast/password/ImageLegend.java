package com.example.levast.password;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Levast on 13.02.2019.
 */

@Entity
public class ImageLegend {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private int idImage;
    private String legend;

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }
}
