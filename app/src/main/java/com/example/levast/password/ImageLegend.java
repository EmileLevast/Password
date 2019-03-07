package com.example.levast.password;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Levast on 13.02.2019.
 */


public class ImageLegend {

    private final static String SUBJECT="SUBJECT";
    private final static String VERB="VERB";
    private final static String PLACE="PLACE";
    private final static String TIME="TIME";
    private final static String FORM="FORM";

    public final static List<List<ImageLegend>> allPagesImages= (List<List<ImageLegend>>) Arrays.asList(
            Arrays.asList(new ImageLegend(R.drawable.auto,"Auto", ImageLegend.SUBJECT),
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
                    new ImageLegend(R.drawable.truck,"Truck", ImageLegend.SUBJECT)),

            Arrays.asList(new ImageLegend(R.drawable.big,"Big", ImageLegend.FORM),
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
                    new ImageLegend(R.drawable.purple,"Purple", ImageLegend.FORM)),

            Arrays.asList(new ImageLegend(R.drawable.climb,"Climb", ImageLegend.VERB),
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
                    new ImageLegend(R.drawable.sprint,"Sprint", ImageLegend.VERB)),

            Arrays.asList(new ImageLegend(R.drawable.beach,"Beach", ImageLegend.PLACE),
                    new ImageLegend(R.drawable.castle,"Castle", ImageLegend.PLACE),
                    new ImageLegend(R.drawable.earth,"Earth", ImageLegend.PLACE),
                    new ImageLegend(R.drawable.familyhouse,"House", ImageLegend.PLACE),
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
                    new ImageLegend(R.drawable.water,"Water", ImageLegend.PLACE)),

            Arrays.asList(new ImageLegend(R.drawable.afternoon,"Afternoon", ImageLegend.TIME),
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
                    new ImageLegend(R.drawable.winter,"Winter", ImageLegend.TIME))
    );






    private int idImage;
    private String legend;
    private String theme;





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
    public static List<ImageLegend> getElementWithId(List<List<ImageLegend>> list,List<Integer> listIndex)
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
