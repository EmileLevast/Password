package com.example.levast.password;

import android.content.res.Resources;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.example.levast.password.Database.AppDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int NBR_COLUMN=4;
    public final static int NBR_LINE=5;
    public final static int NBR_PAGE=2;

    /*public final static String KEY_SHARED_PREFERENCES="KEY_PASSWORD";// for the sharedPreferences
    public final static String KEY_PASSWORD="KEY_PASSWORD";// for the password in sharedPreferences
    public final static String KEY_NBR_SUCCESS="KEY_NBR_SUCCES";
    public final static String KEY_NBR_FAILURE="KEY_NBR_FAILURE";*/

    public static User user;
    //public static SharedPreferences sharedPreferences;
    public AppDataBase roomDB;

    private ContainerView containerView;
    private CustomView customView;
    private GridView gridView;



    //private Container<Integer[]> containerPagePictures;
    private Container<List<ImageLegend>> containerPagePictures;


    private int idHomePage;
    private int idPageImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomDB =AppDataBase.getDataBase(this);
        //on cree notre objet aui va contenir nos mot de passes
        user = roomDB.userDao().loadFirstRow();
        if(user==null)
            user=new User();
        //loadPassword();

        //we launch the container with all the pictures
        //containerPagePictures=new Container<>(getAllDrawables());
        containerPagePictures=new Container<>(loadAllImagefromDb());

        //our custom view to display the image in a imageView
        customView=new CustomView(this, containerPagePictures.getCurrentItem());

        //grid view to display the images in order
        gridView=findViewById(R.id.gridView);
        gridView.setNumColumns(NBR_COLUMN);
        gridView.setAdapter(customView);

        //what to do on click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user.addSymbolToPassword(position);

                //if we already have seen all the pages
                if(containerPagePictures.isDidLoop())
                {
                    String toPrint;
                    //SharedPreferences.Editor editor=sharedPreferences.edit();
                    //int nbrFailure=sharedPreferences.getInt(KEY_NBR_FAILURE,0);
                    //int nbrSuccess=sharedPreferences.getInt(KEY_NBR_SUCCESS,0);
                    if(user.isSavingPassword())
                    {

                        //editor.putString(KEY_PASSWORD, user.getPasswordSaved());
                        toPrint="New Password Saved";
                    }
                    else if(user.checkPassword())
                    {
                        toPrint="Correct Password";
                        user.addSuccess();
                        //nbrSuccess++;
                        //editor.putInt(KEY_NBRI_SUCCESS,nbrSuccess);
                    }
                    else
                    {
                        toPrint="Incorrect Password";
                        user.addFailure();
                        //nbrFailure++;
                        //editor.putInt(KEY_NBR_FAILURE,nbrFailure);
                    }
                    //editor.apply();

                    roomDB.userDao().insertAll(user);
                    toPrint+=" \nNbr Failure: "+user.getNbrFailure()+" \nNbr Success:"+user.getNbrSuccess();
                    //we check or save the password and go to the home page
                    Toast.makeText(getApplicationContext(),toPrint,Toast.LENGTH_LONG).show();
                    containerView.printView(R.id.homePage);
                    containerPagePictures.init();
                }else
                {
                    setCustomView();
                }
            }
        });

        //we add the view to the ContainerView in the order to organize the aparition of the different windows

        idHomePage=R.id.homePage;
        idPageImage=R.id.gridView;
        containerView=new ContainerView(this,idHomePage,idPageImage);
        containerView.printView(R.id.homePage);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void LogInClick(View v)
    {
        containerView.printView(idPageImage);
        user.logIn();
        setCustomView();
    }

    public void RegisterClick(View v)
    {
        containerView.printView(idPageImage);
        user.register();
        setCustomView();
    }

    //change the image of customView
    private void setCustomView()
    {
        customView.setImg(containerPagePictures.getItemNext());
        gridView.setAdapter(customView);
    }

    private Integer[][] getAllDrawables()
    {
        ArrayList<Integer[]> res=new ArrayList<>(0);

        int nbrElmtPage=NBR_COLUMN*NBR_LINE;
        LinkedList<Integer> listElmtPage=new LinkedList<>();

        int numImg=1;
        int nbrPage=0;
        Integer idDrawable=getResources().getIdentifier("test"+numImg,"drawable",this.getPackageName());

        while(idDrawable!=0 && nbrPage<NBR_PAGE)
        {
            listElmtPage.add(idDrawable);
            if(numImg%nbrElmtPage==0)
            {
                res.add( listElmtPage.toArray(new Integer[nbrElmtPage]));
                listElmtPage.clear();
                nbrPage++;
            }

            numImg++;
            idDrawable=getResources().getIdentifier("test"+numImg,"drawable",this.getPackageName());

            //we filled a page

        }


        return  res.toArray(new Integer[nbrPage][nbrElmtPage]);
    }


    private ArrayList<List<ImageLegend>> loadAllImagefromDb()
    {
        ArrayList<List<ImageLegend>> listPage=new ArrayList<>(0);

        listPage.add(roomDB.imageLegendDao().getAllImageTheme(ImageLegend.SUBJECT));
        listPage.add(roomDB.imageLegendDao().getAllImageTheme(ImageLegend.FORM));
        //listPage.add(roomDB.imageLegendDao().getAllImageTheme(ImageLegend.VERB));
        //listPage.add(roomDB.imageLegendDao().getAllImageTheme(ImageLegend.PLACE));
        //listPage.add(roomDB.imageLegendDao().getAllImageTheme(ImageLegend.TIME));



        return listPage;
    }

}
