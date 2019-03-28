package com.example.levast.password;


import android.content.Context;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Levast on 05.02.2019.
 */


public class User {


    //contain all the tests of the user and the last test in the list is the current one
    private HashMap<String,Test> listTest;

    //contains the input data of user
    //the password he is currently clicking
    private ArrayList<Integer> currentInput;
    private String currentPasswordGenerated;

    //when true we register the new informations in "passwordSaved" array
    private boolean isSavingPassword;

    //true if the user is trying to remember his password (test with the notification)
    private boolean isTrying;

    //contain the name of the document that refer to the user in Firestore
    private String documentName;
    private int numOfTest;//used to identify each test when schedule the alarm for the first time

    private Level level;
    private int rank;

    //each we do a try we get the name of the test we want via the intent in the notification and we save this test
    private String currentTestName;

    public User() {
        init();
    }

    public User(String documentName) {
        this.documentName=documentName;
        listTest=new HashMap<>(0);
        level=new Level();
        init();
    }

    //fields not created by firestore need to be instantiate
    private void init()
    {
        currentInput=new ArrayList<>(0);
        isSavingPassword=true;
        isTrying=true;
    }

    /**
     * Check wether the sequence of image selected is the same as registered in the test
     * @return true if the two sequence are equals
     */
    public int checkPassword(Context context)
    {
        boolean check=false;
        if(getCurrentTest().getPasswordSaved().equals(currentInput))
        {
            check=true;
            getCurrentTest().getStatsImagePassword().addSuccess();
            getCurrentTest().nextTry();
        }
        else
        {
            getCurrentTest().getStatsImagePassword().addFailure();

            //if we didn't fail the test for character (this mean alarm have not already been rescheduled) we reschedule alarm because we do an error here
            if(getCurrentTest().getStatCharacterPassword().getCurrentSuite()!=0) {
                getCurrentTest().previousTry();
                rescheduleAlarm(context);
            }

        }

        return level.calculateXp(this,getCurrentTest().getStatsImagePassword(),check);
    }


    /**
     * Compare the password generate with the policy to a input string by user in an editText
     * @param inputOfUser the input string to compare with
     * @return true if the password are equals
     */
    public int checkPassword(String inputOfUser,Context context)
    {
        boolean check=false;
        if((getCurrentTest().getPasswordGenerated()).equals(inputOfUser))
        {
            check=true;
            getCurrentTest().getStatCharacterPassword().addSuccess();
        }else
        {
            getCurrentTest().getStatCharacterPassword().addFailure();
            //we failed so we go back to the preivous try
            getCurrentTest().previousTry();
            rescheduleAlarm(context);
        }

        return level.calculateXp(this,getCurrentTest().getStatCharacterPassword(),check);
    }

    public void addSymbolToPassword(Integer symbol)
    {
        //we add +1 else the first image (index ==0) has no impact on the password
        currentInput.add(symbol);
    }

    //When we finish to register a new Test This method is called
    public boolean addNewTest(String nameTest)
    {
        boolean insertSucceed=false;

        if(isValidNameForNewTest(nameTest))
        {
            numOfTest++;//just after this method we schedule the alarm for this test so be sure we have a new id
            insertSucceed=true;
            Test testAdded=new Test(PasswordPolicyDialog.getChosenPolicy(),currentInput,currentPasswordGenerated,numOfTest);
            testAdded.nextTry();
            listTest.put(nameTest,testAdded);
            currentTestName=nameTest;


        }

        return insertSucceed;
    }

    public void logIn()
    {
        isSavingPassword=false;
        isTrying=false;
        //we are going to enter a new password so we clear the previous data
        currentInput.clear();
    }

    public void register()
    {
        isSavingPassword=true;
        isTrying=false;
        //reset the stats
        currentInput.clear();
    }

    //return true if the key is not already present and not null
    public boolean isValidNameForNewTest(String nameNewTest)
    {
        return !nameNewTest.isEmpty()&&!listTest.containsKey(nameNewTest);
    }

    /**
     * When the user click on the notification this method is called
     */
    public void doTry()
    {
        isSavingPassword=false;
        isTrying=true;
        currentInput.clear();
    }

    public String getDocumentName() {
        return documentName;
    }

    @Exclude
    public boolean isTrying() {
        return isTrying&&!isSavingPassword;
    }

    @Exclude
    public boolean isRegisteringTest() {
        return isSavingPassword&&!isTrying;
    }

    @Exclude
    public ArrayList<Integer> getCurrentInput() {
        return currentInput;
    }

    @Exclude
    public String getCurrentPasswordGenerated() {
        return currentPasswordGenerated;
    }

    public void setCurrentPasswordGenerated(String currentPasswordGenerated) {
        this.currentPasswordGenerated = currentPasswordGenerated;
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Do not call this method before the user registered at least one test
     * @return the currentTest
     */
    @Exclude
    public Test getCurrentTest()
    {
       return listTest.get(currentTestName);
    }

    @Exclude Test getTestWithName(String nameTest)
    {
        return listTest.get(nameTest);
    }

    @Exclude
    public String getCurrentTestName() {
        return currentTestName;
    }

    public boolean initCurrentTest(String name) {
        boolean testFound=false;
        if(listTest.containsKey(name))
        {
            testFound=true;
            currentTestName=name;
            currentPasswordGenerated=getCurrentTest().getPasswordGenerated();
        }
        return testFound;
    }

    //seem to be useless, but if yu want to save the listTest Field , firestore need to have an acess to it
    public HashMap<String,Test> getListTest() {
        return listTest;
    }

    public int getNumOfTest() {
        return numOfTest;
    }

    private void rescheduleAlarm(Context context)
    {
        //if failure we have to reschedule the alarms from the last one
        int newNumOfTry=getCurrentTest().getNumOfTry()-1;
        if(newNumOfTry>=0)
        {
            NotificationAlarm.scheduleAlarmFrom(context,documentName,newNumOfTry,currentTestName,getCurrentTest().getId());
        }
    }

    public void setCurrentInput(ArrayList<Integer> currentInput) {
        this.currentInput = new ArrayList<>(currentInput);
    }

    public int getRank() {
        return rank;
    }
}






























