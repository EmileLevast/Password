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
    private String theme;


    public final static String SUBJECT="SUBJECT";
    public final static String VERB="VERB";
    public final static String PLACE="PLACE";
    public final static String TIME="TIME";
    public final static String FORM="FORM";

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public ImageLegend(int idImage, String legend,String theme) {
        this.idImage = idImage;
        this.legend = legend;
        this.theme=theme;
    }

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
