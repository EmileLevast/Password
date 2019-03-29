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
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.core.RelationFilter;
import com.google.firebase.firestore.core.UserData;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /*
    Param of the Gridview
     */
    public final static int NBR_COLUMN = 4;
    public final static int NBR_LINE = 5;
    public static int NBR_PAGE;


    /*
        Use with boolean , example if field corrsponding to KEY_NUMBERS = true then we use numbers to generate password
         */
    public final static String NAME_SHARED_PREFERENCE = "NAME_SHARED_PREFERENCE_PASSWORD_POLICY";
    public final static String KEY_USER_DOCUMENT_NAME = "KEY_USER_DOCUMENT_NAME";
    public static SharedPreferences sharedPreferences;


    /*
    Database
     */
    private User user;
    //just used to memorize the tests displayed by list view
    private Test[] listTestCurrentDisplay;


    /*
    Firestore
     */
    FirebaseFirestore firestoreDB;
    public static final String COLLECTION_USERS = "COLLECTION_USERS";
    private final String COLLECTION_STATS = "stats";
    private final String DOCUMENT_STATS = "all_users";
    private final String KEY_FIRESTORE_NUMBER_USERS = "nbrUsers";

    /*
    The views
     */
    private ContainerView containerView;
    private CustomView customView;
    private CustomView customViewResult;
    private GridView gridView;
    private GridView gridViewResult;
    private ListView listViewTestDelete;
    public static View snackBar;
    private EditText rememberPasswordView;
    private TextView textPasswordGenerated;
    private ProgressBar expProgressBar;
    private TextView textViewLevel;
    private EditText editTextNametest;
    private TextView textviewTestName;

    /*
    Manage the order of the views
     */
    private Container<List<ImageLegend>> containerPagePictures;
    private int idHomePage;
    private int idPageImage;
    private int idTestCharacterPage;
    private int idResultPage;
    private int idWaitPage;
    private int idListTestPage;

    //used one time at the beginning to load the number of user
    private int numberOfUser;

    /*
    Intents
     */
    public final static String CHANEl_ID = "NOTIFICATION_TEST";
    //key:indicates the name of the test we are going to try
    public final static String INTENT_LEVAST_PASSWORD_NAME_TEST = "INTENT_LEVAST_PASSWORD_NAME_TEST";
    //key:indicates to the activity which page we want to show
    public final static String INTENT_LEVAST_PASSWORD_ID_PAGE = "INTENT_LEVAST_PASSWORD_ID_PAGE";
    //value:used to know which page to show on an intent received
    public final static int SHOW_LOGIN = 1;


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
        NBR_PAGE = ImageLegend.allPagesImages.size();


        /*
        sharedPreference
         */
        sharedPreferences = getSharedPreferences(MainActivity.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        /*
        intiate Firestore
         */
        firestoreDB = FirebaseFirestore.getInstance();
        initFirestore();


        /*
        We update the param with the sharedPreference
         */
        PasswordPolicyDialog.checkAtLeastOnePolicySharedPref(this);
        sharedPreferences = getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        NumberSentencesDialog.NBR_SENTENCES = sharedPreferences.getInt(NumberSentencesDialog.KEY_NUMBER_SENTENCES, NumberSentencesDialog.DEFAULT_NUMBER_SENTENCES);


        /*
        Prepare All the graphics components
         */
        containerPagePictures = new Container<>(ImageLegend.allPagesImages);

        //our custom view to display the image in a imageView
        customView = new CustomView(this, containerPagePictures.getCurrentItem());

        //grid view to display the images in order
        gridView = findViewById(R.id.gridView);
        gridView.setNumColumns(NBR_COLUMN);
        gridView.setAdapter(customView);

        //gridView to print the results
        gridViewResult = findViewById(R.id.gridViewResult);

        //what to do on click
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (getUser() != null) {
                    getUser().addSymbolToPassword(position);

                    //if we have seen all the pages
                    if (containerPagePictures.isDidLoop()) {
                        sequenceCompleted();
                    } else {
                        setCustomView();
                    }
                } else {
                    Snackbar.make(snackBar, "Error User is not valid", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        //get the view with their id
        snackBar = findViewById(R.id.scnackBar);
        rememberPasswordView = findViewById(R.id.rememberPassword);
        textPasswordGenerated = findViewById(R.id.passwordResult);
        textViewLevel = findViewById(R.id.textViewLevel);
        expProgressBar = findViewById(R.id.expProgressbar);
        editTextNametest = findViewById(R.id.editTextNameTest);
        textviewTestName = findViewById(R.id.textViewTestName);
        listViewTestDelete = findViewById(R.id.listViewTests);

        listViewTestDelete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Test testToDelete=listTestCurrentDisplay[i];
                NotificationAlarm.cancelAlarmForTest(getApplicationContext(),testToDelete);

                getUser().getListTest().remove(testToDelete.getName());
                firestoreDB.collection(COLLECTION_USERS).document(getUser().getDocumentName())
                        .update("listTest", getUser().getListTest());

                printPageListTest();
            }
        });


        expProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(snackBar, "Your rank :" + getUser().getRank() + " / " + numberOfUser, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

        //we add the view to the ContainerView in the order to organize the aparition of the different windows
        idHomePage = R.id.homePage;
        idPageImage = R.id.gridView;
        idResultPage = R.id.resultPage;
        idWaitPage = R.id.waitPage;
        idListTestPage = R.id.listTestPage;
        idTestCharacterPage = R.id.pageTestCharacter;
        containerView = new ContainerView(this, idWaitPage, idHomePage, idPageImage, idResultPage, idTestCharacterPage, idListTestPage);

        containerView.printView(idWaitPage);

    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public void createPasswordClick(View v) {
        printPageImage();
        getUser().logIn();
    }

    public void generateTestClick(View view) {
        if (checkNameNewTestValid()) {
            getUser().register();
            Random rand = new Random();
            for (int i = 0; i < NumberSentencesDialog.NBR_SENTENCES * NBR_PAGE; i++) {
                getUser().addSymbolToPassword(rand.nextInt(NBR_COLUMN * NBR_LINE));
            }
            sequenceCompleted();
        } else {
            Snackbar.make(snackBar, "Please choose a correct test name", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void RegisterClick(View v) {
        if (checkNameNewTestValid()) {
            printPageImage();
            getUser().register();
        } else {
            Snackbar.make(snackBar, "Please choose a correct test name", Snackbar.LENGTH_SHORT).show();
        }
    }

    //check if the current string entered in the edittext is valid to create a new test with
    private boolean checkNameNewTestValid() {
        boolean validName = true;
        if (!getUser().isValidNameForNewTest(editTextNametest.getText().toString())) {
            validName = false;
            Snackbar.make(snackBar, "Error: please enter valid name for your Test", Snackbar.LENGTH_SHORT);
        }

        return validName;
    }

    /**
     * Call when the user want to see if he can remember his password as a char sequence
     *
     * @param view
     */
    public void checkPasswordCharOnClick(View view) {

        if (getUser() != null) {
            String inputCharPassword = rememberPasswordView.getText().toString();
            String toPrint = constructStringXpWin(getUser().checkPassword(inputCharPassword, this));

            Snackbar.make(snackBar, toPrint, Snackbar.LENGTH_SHORT).show();

            firestoreDB.collection(COLLECTION_USERS).document(getUser().getDocumentName())
                    .update("listTest", getUser().getListTest());

            printPageImage();
        } else {
            Snackbar.make(snackBar, "Error User is not valid", Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * Called when the user built a sentence
     */
    private void sequenceCompleted() {
        String toPrint;


        //the user clicked on Register
        if (getUser().isRegisteringTest()) {
            getUser().setCurrentPasswordGenerated(GeneratePassword.getPasswordAsText(this, getUser().getCurrentInput()));

            //if we achieve to add our new test then we schedule the alarm
            if (getUser().addNewTest(editTextNametest.getText().toString())) {
                toPrint = "Learn this sequence and this password, first test in 10 min.";
                NotificationAlarm.createAlarm(this, getUser().getDocumentName(), getUser().getCurrentTestName(), getUser().getNumOfTest());
            } else {
                toPrint = "Error:Name for test is Not Valid";
            }


            //we add a new test to his list of tests
        }
        //user clicked on the notification to test his memory
        else if (getUser().isTrying()) {
            getUser().setCurrentPasswordGenerated(getUser().getCurrentTest().getPasswordGenerated());
            toPrint = constructStringXpWin(getUser().checkPassword(this));

            //we set the good sequence of images to print it in the page of results
            getUser().setCurrentInput(getUser().getCurrentTest().getPasswordSaved());
            NumberSentencesDialog.NBR_SENTENCES = sharedPreferences.getInt(NumberSentencesDialog.KEY_NUMBER_SENTENCES, NumberSentencesDialog.DEFAULT_NUMBER_SENTENCES);
        }
        //user clicked on LogIn to get a password in Clipboard
        else {

            getUser().setCurrentPasswordGenerated(GeneratePassword.getPasswordAsText(this, getUser().getCurrentInput()));
            toPrint = "Password Saved in the ClipBoard";

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", getUser().getCurrentPasswordGenerated());
            clipboard.setPrimaryClip(clip);
        }

        firestoreDB.collection(COLLECTION_USERS).document(getUser().getDocumentName())
                .set(getUser());

        printResultSequence();

        //we check or save the password and go to the home page
        Snackbar.make(snackBar, toPrint, Snackbar.LENGTH_SHORT).show();
        containerPagePictures.init();
    }

    private String constructStringXpWin(int xpWin) {
        String toPrint = xpWin >= 0 ? "You Win " : "You lose ";

        toPrint += xpWin + " XP";

        return toPrint;
    }

    private void printPageImage() {
        containerView.printView(idPageImage);
        setCustomView();
    }

    public void printPageListTest() {

        //we init the listTestCurrentDisplay variable
        listTestToArray();

        ArrayAdapter<Test> arrayAdapter = new ArrayAdapter<>(this, R.layout.layoutlisttest, R.id.textTestName, listTestCurrentDisplay);
        listViewTestDelete.setAdapter(arrayAdapter);
        containerView.printView(idListTestPage);

    }

    //change the image of customView
    private void setCustomView() {
        customView.setImg(containerPagePictures.getItemNext());
        gridView.setAdapter(customView);
    }


    public void showPolicyDialog(View v) {
        PasswordPolicyDialog passwordPolicyDialog = new PasswordPolicyDialog();
        passwordPolicyDialog.show(getFragmentManager(), "dialogPolicy");
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
        updateWithIntent(intent);
    }

    public void goToHomePage(View view) {
        containerView.printView(idHomePage);
        updateExpBarUser();
    }

    private void printResultSequence() {
        customViewResult = new CustomView(this, ImageLegend.getElementWithId(ImageLegend.allPagesImages, getUser().getCurrentInput()));
        gridViewResult.setAdapter(customViewResult);
        textPasswordGenerated.setText(getUser().getCurrentPasswordGenerated());
        containerView.printView(idResultPage);
    }

    /**
     * Used when app launch (even if she was already running) by an intent
     *
     * @param intent
     */
    private void updateWithIntent(Intent intent) {
        int pageToLaunch = intent.getIntExtra(INTENT_LEVAST_PASSWORD_ID_PAGE, -1);

        if (pageToLaunch == SHOW_LOGIN &&
                //we found the corresponding test and we add the test in currenttest
                getUser().initCurrentTest(intent.getStringExtra(INTENT_LEVAST_PASSWORD_NAME_TEST))) {
            //we set the number of sentence with the number of sentences for this test
            NumberSentencesDialog.NBR_SENTENCES = getUser().getCurrentTest().getNbrSentenceForSequence();
            containerView.printView(idTestCharacterPage);
            textviewTestName.setText(getUser().getCurrentTestName());
            //the user is currently testing his memory
            getUser().doTry();

            Snackbar.make(snackBar, "Sorry: can't find this test", Snackbar.LENGTH_SHORT);

        } else {
            goToHomePage(null);
        }
    }

    private void initFirestore() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //if the onCreate method is not already done , we report the download
                if (findViewById(idWaitPage).getVisibility() == View.GONE) {
                    initFirestore();
                } else {
                    downloadUser();
                }
            }
        }, 2000);
    }

    private void downloadUser() {
        //we try to get the document corresponding to the user
        //I didn't understand why yet, but when there is no corresponding document sometimes it throw exception
        //sometimes user is null in OnSuccess Method

        //we download the numberOfuser conained in the doc stat infirestore
        firestoreDB.collection(COLLECTION_STATS).document(DOCUMENT_STATS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            try {
                                numberOfUser = ((Long) documentSnapshot.getData().get(KEY_FIRESTORE_NUMBER_USERS)).intValue();
                            } catch (NullPointerException e) {
                                //can't find the field
                                numberOfUser = -1;
                            }
                        }

                    }
                });

        try {
            final DocumentReference docRef = firestoreDB.collection(COLLECTION_USERS).document(sharedPreferences.getString(KEY_USER_DOCUMENT_NAME, ""));

            //we retrieve the data about the document
            docRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            //we download the user
                            user = documentSnapshot.toObject(User.class);

                            if (getUser() == null) {
                                CreateAndInsertUserInFirestore();

                            }
                            setRealtimeUpdateRank();

                            //if the activity is launched by an intent (thanks to our notification) we have to redirect the user on the right page
                            updateWithIntent(getIntent());
                        }
                    });


        } catch (IllegalArgumentException exception) {
            CreateAndInsertUserInFirestore();
            setRealtimeUpdateRank();

            //if the activity is launched by an intent (thanks to our notification) we have to redirect the user on the right page
            updateWithIntent(getIntent());
        }
    }

    private void CreateAndInsertUserInFirestore() {
        //if there is no such document
        //we upload his document
        //we create it with an id auto-generated
        DocumentReference documentReference = firestoreDB.collection(COLLECTION_USERS).document();
        user = new User(documentReference.getId());
        documentReference.set(getUser());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_DOCUMENT_NAME, getUser().getDocumentName());
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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void showNumberSentencesDialog(View view) {
        NumberSentencesDialog numberSentencesDialog = new NumberSentencesDialog();
        numberSentencesDialog.show(getFragmentManager(), "NumberSentencesDialog");
    }

    //called when we arrive on the homePage occured for exp Player
    private void updateExpBarUser() {
        expProgressBar.setMax(getUser().getLevel().getCurrentLevel() * Level.XP_BY_LEVEL);
        expProgressBar.setProgress(getUser().getLevel().getXp());
        String str = "Level " + getUser().getLevel().getCurrentLevel();
        textViewLevel.setText(str);
    }

    private void setRealtimeUpdateRank() {
        final DocumentReference docRefUser = firestoreDB.collection(COLLECTION_USERS).document(sharedPreferences.getString(KEY_USER_DOCUMENT_NAME, ""));

        docRefUser.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("msg", "Listen failed.", e);
                    return;
                }

                //we observe a change on the server
                if (snapshot != null && snapshot.exists() && !snapshot.getMetadata().hasPendingWrites()) {

                    User userOnServer = snapshot.toObject(User.class);
                    if (getUser() != null && userOnServer.getRank() != getUser().getRank()) {
                        getUser().setRank(userOnServer.getRank());
                    }
                }
            }
        });

        final DocumentReference docRefStats = firestoreDB.collection(COLLECTION_STATS).document(DOCUMENT_STATS);
        docRefStats.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("msg", "Listen failed.", e);
                    return;
                }

                //we observe a change on the server
                if (snapshot != null && snapshot.exists() && !snapshot.getMetadata().hasPendingWrites()) {

                    int currentNumberOfUser = ((Long) snapshot.getData().get(KEY_FIRESTORE_NUMBER_USERS)).intValue();
                    if (numberOfUser != currentNumberOfUser) {
                        numberOfUser = currentNumberOfUser;
                    }
                }
            }
        });

    }

    private void listTestToArray()
    {
        listTestCurrentDisplay=(getUser().getListTest().values().toArray(new Test[getUser().getListTest().size()]));
    }

    private synchronized User getUser() {
        return user;
    }

    public void goToListTestPage(View view) {
        printPageListTest();
    }
}
