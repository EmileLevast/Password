package com.example.levast.password;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.example.levast.password.Database.AppDataBase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
    Param of the Gridview
     */
    public final static int NBR_COLUMN=4;
    public final static int NBR_LINE=5;
    public static int NBR_PAGE;


    /*
        Use with boolean , example if field corrsponding to KEY_NUMBERS = true then we use numbers to generate password
         */
    public final static String NAME_SHARED_PREFERENCE="NAME_SHARED_PREFERENCE_PASSWORD_POLICY";
    public final static String KEY_USER_DOCUMENT_NAME ="KEY_USER_DOCUMENT_NAME";
    public static SharedPreferences sharedPreferences;


    /*
    Database
     */
    public static User user;
    public AppDataBase roomDB;

    /*
    Firestore
     */
    FirebaseFirestore firestoreDB;
    public static final String COLLECTION_USERS="COLLECTION_USERS";

    /*
    The views
     */
    private ContainerView containerView;
    private CustomView customView;
    private CustomView customViewResult;
    private GridView gridView;
    private GridView gridViewResult;

    /*
    Manage the order of the views
     */
    private Container<List<ImageLegend>> containerPagePictures;
    private ArrayList<List<ImageLegend>> allPagesImages;
    private int idHomePage;
    private int idPageImage;
    private int idResultPage;
    private int idWaitPage;

    /*
    Intents
     */
    public final static String CHANEl_ID="NOTIFICATION_TEST";
    //key:indicates to the activity which page we want to show
    public final static String INTENT_LEVAST_PASSWORD_ID_PAGE ="INTENT_LEVAST_PASSWORD_ID_PAGE";
    //value:used to know which page to show on an intent received
    public final static int SHOW_LOGIN =1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Instantiate Notification Parameters
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        createNotificationChannel();

        setContentView(R.layout.activity_main);
        NBR_PAGE=ImageLegend.listTheme.length;


        /*
        sharedPreference
         */
        sharedPreferences=getSharedPreferences(MainActivity.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        /*
        Load data from database
         */
        roomDB =AppDataBase.getDataBase(this);
        //on cree notre objet aui va contenir nos mot de passes
        /*user = roomDB.userDao().loadFirstRow();
        if(user==null)
        {
            user=new User();
            //we insert here because the service may use some fields
            roomDB.userDao().insertAll(user);
        }*/

        /*
        intiate Firestore
         */
        firestoreDB = FirebaseFirestore.getInstance();
        initFirestore();


        /*
        We update the param with the sharedPreference
         */
        PasswordPolicyDialog.checkAtLeastOnePolicySharedPref(this);
        sharedPreferences=getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        NumberSentencesDialog.NBR_SENTENCES=sharedPreferences.getInt(NumberSentencesDialog.KEY_NUMBER_SENTENCES,NumberSentencesDialog.DEFAULT_NUMBER_SENTENCES);


        /*
        Prepare All the graphics components
         */
        //we launch the container with all the pictures
        allPagesImages=loadAllImagefromDb();
        containerPagePictures=new Container<>(allPagesImages);

        //our custom view to display the image in a imageView
        customView=new CustomView(this, containerPagePictures.getCurrentItem());

        //grid view to display the images in order
        gridView=findViewById(R.id.gridView);
        gridView.setNumColumns(NBR_COLUMN);
        gridView.setAdapter(customView);

        //gridView to print the results
        gridViewResult=findViewById(R.id.gridViewResult);

        //what to do on click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user.addSymbolToPassword(position);

                //if we have seen all the pages
                if(containerPagePictures.isDidLoop())
                {
                    sequenceCompleted();
                }else
                {
                    setCustomView();
                }
            }
        });

        //we add the view to the ContainerView in the order to organize the aparition of the different windows
        idHomePage=R.id.homePage;
        idPageImage=R.id.gridView;
        idResultPage=R.id.resultPage;
        idWaitPage=R.id.waitPage;
        containerView=new ContainerView(this,idWaitPage,idHomePage,idPageImage,idResultPage);

        containerView.printView(idWaitPage);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void GenerateClick(View v)
    {
        printPageImage(idPageImage);
        user.logIn();
    }

    public void RegisterClick(View v)
    {
        printPageImage(idPageImage);
        user.register();
    }

    /**
     * Called when the user built a sentence
     */
    private void sequenceCompleted()
    {
        String toPrint;

        //the user clicked on Register
        if(user.isSavingPassword())
        {

            toPrint="New Password Saved";
            //we insert the id of the user to load him in the service
            user.initStat();

            //we save the number of sentences for this test
            user.setNbrOfSentenceForTry(NumberSentencesDialog.NBR_SENTENCES);

            //we say to the service to begin the tests
            user.nextTry();//his field nextTry is set to 1, thanks to that we know the tests begin
            NotificationAlarm.createAlarm(this,user.getDocumentName());
            //printDataAboutPassword();

        }
        //user clicked on the notification to test his memory
        else if(user.isTrying())
        {
            if(user.checkPassword())
            {
                toPrint="Correct Password";
            }
            else
            {
                toPrint="Incorrect Password";

            }
            NumberSentencesDialog.NBR_SENTENCES=sharedPreferences.getInt(NumberSentencesDialog.KEY_NUMBER_SENTENCES,NumberSentencesDialog.DEFAULT_NUMBER_SENTENCES);

            toPrint+=" \nNbr Failure: "+user.getNbrFailure()+" \nNbr Success:"+user.getNbrSuccess();
        }
        //user clicked on LogIn to get a password in Clipboard
        else
        {
            toPrint="Password Saved in the ClipBoard";

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", getPasswordAsText());
            clipboard.setPrimaryClip(clip);
        }

        //roomDB.userDao().insertAll(user);
        firestoreDB.collection(COLLECTION_USERS).document(user.getDocumentName())
                .set(user);

        printResultSequence();

        //we check or save the password and go to the home page
        Toast.makeText(getApplicationContext(),toPrint,Toast.LENGTH_LONG).show();
        //containerView.printView(R.id.homePage);
        containerPagePictures.init();
    }

    private void printPageImage(int idPageToPrint)
    {
        containerView.printView(idPageToPrint);
        setCustomView();
    }

    //change the image of customView
    private void setCustomView()
    {
        customView.setImg(containerPagePictures.getItemNext());
        gridView.setAdapter(customView);
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


    public void showPolicyDialog(View v)
    {
        PasswordPolicyDialog passwordPolicyDialog=new PasswordPolicyDialog();
        passwordPolicyDialog.show(getFragmentManager(),"dialogPolicy");
    }


    @Override
    protected void onNewIntent(Intent intent) {

        updateWithIntent(intent);
        super.onNewIntent(intent);
    }

    public void goToHomePage(View view) {
        containerView.printView(idHomePage);
    }

    private void printResultSequence()
    {
        customViewResult=new CustomView(this,ImageLegend.getElementWithId(allPagesImages,user.getCurrentUsedPassword()));
        gridViewResult.setAdapter(customViewResult);
        containerView.printView(idResultPage);
    }

    /**
     * Used when app launch (even if she was already running) by an intent
     * @param intent
     */
    private void updateWithIntent(Intent intent)
    {
        int pageToLaunch=intent.getIntExtra(INTENT_LEVAST_PASSWORD_ID_PAGE,-1);
        if(pageToLaunch==SHOW_LOGIN)
        {
            NumberSentencesDialog.NBR_SENTENCES=user.getNbrOfSentenceForTry();
            printPageImage(idPageImage);

            //the user is currently testing his memory
            user.doTry();

        }else
        {
            containerView.printView(idHomePage);
        }
    }

    private String getPasswordAsText()
    {
        String text="";
        List<Character> password=GeneratePassword.intToSqceSymbol(GeneratePassword.seqceInputToInt(user.getCurrentInput()),this);
        for(Character elt: password)
        {
            text+=elt;
        }
        return text;
    }

    private void initFirestore()
    {


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //if the onCreate method is not already done , we report the download
                if(findViewById(idWaitPage).getVisibility()==View.GONE)
                {
                    initFirestore();
                }else
                {
                    downloadUser();
                }
            }
        },2000);
    }

    private void downloadUser()
    {
        firestoreDB.collection(COLLECTION_USERS).document(sharedPreferences.getString(KEY_USER_DOCUMENT_NAME,""))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        //we download the user
                        user=documentSnapshot.toObject(User.class);
                        if(user==null)
                        {
                            DocumentReference documentReference=firestoreDB.collection(COLLECTION_USERS).document();
                            user=new User(documentReference.getId());
                            documentReference.set(user);

                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(KEY_USER_DOCUMENT_NAME,user.getDocumentName());
                            editor.apply();
                        }

                        //if the activity is launched by an intent (thanks to our notification) we have to redirect the user on the right page
                        updateWithIntent(getIntent());


                    }
                });
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= 26) {
            CharSequence name = "Reminder";
            String description = "The user must test his memory about his Password";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MainActivity.CHANEl_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

        List<Character> password=GeneratePassword.intToSqceSymbol(PN,this);
        for(int i=0;i<password.size();i++)
        {
            Log.w("msg","Z["+i+"]: "+password.get(i));
        }
        Log.w("msg","========================\n");
    }

    public void showNumberSentencesDialog(View view) {
        NumberSentencesDialog numberSentencesDialog=new NumberSentencesDialog();
        numberSentencesDialog.show(getFragmentManager(),"NumberSentencesDialog");
    }


}
