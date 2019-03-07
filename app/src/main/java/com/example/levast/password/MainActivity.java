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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

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
    public static View snackBar;
    public EditText rememberPasswordView;
    public TextView textPasswordGenerated;

    /*
    Manage the order of the views
     */
    private Container<List<ImageLegend>> containerPagePictures;
    private int idHomePage;
    private int idPageImage;
    private int idTestCharacterPage;
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
        NBR_PAGE=ImageLegend.allPagesImages.size();


        /*
        sharedPreference
         */
        sharedPreferences=getSharedPreferences(MainActivity.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);




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
        containerPagePictures=new Container<>(ImageLegend.allPagesImages);

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

        //get the view with their id
        snackBar=findViewById(R.id.scnackBar);
        rememberPasswordView=findViewById(R.id.rememberPassword);
        textPasswordGenerated=findViewById(R.id.passwordResult);

        //we add the view to the ContainerView in the order to organize the aparition of the different windows
        idHomePage=R.id.homePage;
        idPageImage=R.id.gridView;
        idResultPage=R.id.resultPage;
        idWaitPage=R.id.waitPage;
        idTestCharacterPage =R.id.pageTestCharacter;
        containerView=new ContainerView(this,idWaitPage,idHomePage,idPageImage,idResultPage,idTestCharacterPage);

        containerView.printView(idWaitPage);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void createPasswordClick(View v)
    {
        printPageImage();
        user.logIn();
    }

    public void generateTestClick(View view) {

        user.register();
        Random rand=new Random();
        for(int i=0;i<NumberSentencesDialog.NBR_SENTENCES*NBR_PAGE;i++)
        {
            user.addSymbolToPassword(rand.nextInt(NBR_COLUMN*NBR_LINE));
        }
        sequenceCompleted();
    }

    public void RegisterClick(View v)
    {
        printPageImage();
        user.register();
    }

    /**
     * Call when the user want to see if he can remember his password as a char sequence
     * @param view
     */
    public void checkPasswordCharOnClick(View view) {
        String inputCharPassword=rememberPasswordView.getText().toString();
        String toPrint=user.checkPassword(inputCharPassword)?"Correct":"Incorrect";
        toPrint+=" Password";

        Snackbar.make(snackBar,toPrint,Snackbar.LENGTH_SHORT).show();

        firestoreDB.collection(COLLECTION_USERS).document(user.getDocumentName())
                .update("listTest",user.getListTest());

        printPageImage();

    }

    /**
     * Called when the user built a sentence
     */
    private void sequenceCompleted()
    {
        String toPrint;

        user.setCurrentPasswordGenerated(GeneratePassword.getPasswordAsText(this,user.getCurrentInput()));
        //the user clicked on Register
        if(user.isRegisteringTest())
        {

            toPrint="New Test Regsitered";

            //we add a new test to his list of tests
            user.addNewTest();

            NotificationAlarm.createAlarm(this,user.getDocumentName());
        }
        //user clicked on the notification to test his memory
        else if(user.isTrying())
        {
            toPrint=user.checkPassword(this)?"Correct":"Incorrect";
            toPrint+=" Password";

            NumberSentencesDialog.NBR_SENTENCES=sharedPreferences.getInt(NumberSentencesDialog.KEY_NUMBER_SENTENCES,NumberSentencesDialog.DEFAULT_NUMBER_SENTENCES);
        }
        //user clicked on LogIn to get a password in Clipboard
        else
        {
            toPrint="Password Saved in the ClipBoard";

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", user.getCurrentPasswordGenerated());
            clipboard.setPrimaryClip(clip);
        }

        firestoreDB.collection(COLLECTION_USERS).document(user.getDocumentName())
                .set(user);

        printResultSequence();

        //we check or save the password and go to the home page
        Snackbar.make(snackBar,toPrint,Snackbar.LENGTH_SHORT).show();
        containerPagePictures.init();
    }

    private void printPageImage()
    {
        containerView.printView(idPageImage);
        setCustomView();
    }

    //change the image of customView
    private void setCustomView()
    {
        customView.setImg(containerPagePictures.getItemNext());
        gridView.setAdapter(customView);
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
        customViewResult=new CustomView(this,ImageLegend.getElementWithId(ImageLegend.allPagesImages,user.getCurrentInput()));
        gridViewResult.setAdapter(customViewResult);
        textPasswordGenerated.setText(user.getCurrentPasswordGenerated());
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
            NumberSentencesDialog.NBR_SENTENCES=user.getCurrentTest().getNbrSentenceForSequence();
            //printPageImage(idPageImage);
            containerView.printView(idTestCharacterPage);
            //the user is currently testing his memory
            user.doTry();

        }else
        {
            containerView.printView(idHomePage);
        }
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
        //we try to get the document corresponding to the user
        //I didn't understand why yet, but when there is no corresponding document sometimes it throw exception
        //sometimes user is null in OnSuccess Method
        try
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
                                CreateAndInsertUserInFirestore();
                            }
                            //if the activity is launched by an intent (thanks to our notification) we have to redirect the user on the right page
                            updateWithIntent(getIntent());
                        }
                    });

        }catch (IllegalArgumentException exception)
        {
            CreateAndInsertUserInFirestore();

            //if the activity is launched by an intent (thanks to our notification) we have to redirect the user on the right page
            updateWithIntent(getIntent());
        }
    }

    private void CreateAndInsertUserInFirestore()
    {
        //if there is no such document
        //we upload his document
        //we create it with an id auto-generated
        DocumentReference documentReference=firestoreDB.collection(COLLECTION_USERS).document();
        user=new User(documentReference.getId());
        documentReference.set(user);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_USER_DOCUMENT_NAME,user.getDocumentName());
        editor.apply();
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



    public void showNumberSentencesDialog(View view) {
        NumberSentencesDialog numberSentencesDialog=new NumberSentencesDialog();
        numberSentencesDialog.show(getFragmentManager(),"NumberSentencesDialog");
    }


}
