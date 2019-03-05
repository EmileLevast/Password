package com.example.levast.password;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

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

    public final static String[] listTheme=new String[]{SUBJECT,VERB,TIME,FORM,PLACE};

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



    public String getLegend() {
        return legend;
    }



    /**
     * @param list all the pages of images
     * @param listIndex the index of the chosen image per page
     * @return the image chosen for each page
     */
    public static List<ImageLegend> getElementWithId(ArrayList<List<ImageLegend>> list,List<Integer> listIndex)
    {
        List<ImageLegend> res=new ArrayList<>(0);
        if(list.size()<=listIndex.size())
        {
            for(int i=0;i<listIndex.size();i++)
            {
                res.add(list.get(i%MainActivity.NBR_PAGE).get(listIndex.get(i)));
            }
        }

        return res;
    }

    public static List<Integer> getOnlyIdFrom(List<ImageLegend> list)
    {
        ArrayList<Integer> listId=new ArrayList<>(0);
        for(ImageLegend item:list)
        {
            listId.add(item.getIdImage());
        }
        return listId;
    }
}
