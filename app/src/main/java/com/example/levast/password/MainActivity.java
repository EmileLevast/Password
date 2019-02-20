package com.example.levast.password;

import android.content.Intent;
import android.content.res.Resources;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.example.levast.password.Database.AppDataBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int NBR_COLUMN=4;
    public final static int NBR_LINE=5;
    public static int NBR_PAGE;
    public final static char[] tableASCII=new char[]{
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        '\"','\'','!','^','#','$','%','&','/','{','(','[',')',']','=','}','?','*','~','+','-','_',',',';',':','.','<','>'
    };


    public static User user;
    public AppDataBase roomDB;

    private ContainerView containerView;
    private CustomView customView;
    private GridView gridView;

    private Container<List<ImageLegend>> containerPagePictures;
    private int idHomePage;
    private int idPageImage;

    public final static String CHANEl_ID="NOTIFICATION_TEST";
    //indicates to the service which user w want to load
    public final static String INTENT_LEVAST_PASSWORD_ID_USER ="INTENT_LEVAST_PASSWORD_ID_USER";
    //key:indicates to the activity which page we want to show
    public final static String INTENT_LEVAST_PASSWORD_ID_PAGE ="INTENT_LEVAST_PASSWORD_ID_PAGE";
    //value:used to know which page to show on an intent received
    public final static int SHOW_LOGIN =1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NBR_PAGE=ImageLegend.listTheme.length;




        /*
        Load data from database
         */
        roomDB =AppDataBase.getDataBase(this);
        //on cree notre objet aui va contenir nos mot de passes
        user = roomDB.userDao().loadFirstRow();
        if(user==null)
        {
            user=new User();
            //we insert here because the service may use some fields
            roomDB.userDao().insertAll(user);
        }


        /*
        Prepare All the graphics components
         */
        //we launch the container with all the pictures
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

                //if we have seen all the pages
                if(containerPagePictures.isDidLoop())
                {
                    String toPrint;

                    if(user.isSavingPassword())
                    {

                        toPrint="New Password Saved";
                        //we insert the id of the user to load him in the service
                        user.initStat();
                        Intent intent=new Intent(getApplicationContext(),ServiceNotification.class);
                        intent.putExtra(INTENT_LEVAST_PASSWORD_ID_USER,user.id);
                        startService(intent);
                        printDataAboutPassword();

                    }
                    else if(user.checkPassword())
                    {
                        toPrint="Correct Password";
                        user.addSuccess();
                    }
                    else
                    {
                        toPrint="Incorrect Password";
                        user.addFailure();
                    }

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

        updateWithIntent(getIntent());

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
        //sendNotif("Register a new password");
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

        for(String theme: ImageLegend.listTheme)
        {
            listPage.add(roomDB.imageLegendDao().getAllImageTheme(theme));
        }

        return listPage;
    }


    private void printDataAboutPassword()
    {
        Log.w("msg","=====Password Saved=====\n");
        for(int i=0;i<user.getPasswordSaved().size();i++)
        {
            Log.w("msg","e["+i+"]: "+user.getPasswordSaved().get(i));
        }

        long PN=GeneratePassword.seqceInputToInt(user.getPasswordSaved());
        Log.w("msg","PN:"+PN);

        List<Character> password=GeneratePassword.intToSqceSymbol(PN);
        for(int i=0;i<password.size();i++)
        {
            Log.w("msg","Z["+i+"]: "+password.get(i));
        }
        Log.w("msg","========================\n");
    }

    @Override
    protected void onNewIntent(Intent intent) {


        updateWithIntent(intent);
        super.onNewIntent(intent);
    }

    private void updateWithIntent(Intent intent)
    {
        int pageToLaunch=intent.getIntExtra(INTENT_LEVAST_PASSWORD_ID_PAGE,-1);
        if(pageToLaunch==SHOW_LOGIN)
        {
            LogInClick(null);
        }
    }
}
