package com.example.levast.password;


import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * Created by Levast on 05.02.2019.
 */


public class User {


    //contain all the tests of the user and the last test in the list is the current one
    private ArrayList<Test> listTest;

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

    private Level level;

    public User() {
        init();
    }

    public User(String documentName) {
        this.documentName=documentName;
        listTest=new ArrayList<>(0);
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

        }
        else
        {
            getCurrentTest().getStatsImagePassword().addFailure();

            rescheduleAlarm(context);
        }

        return level.calculateXp(getCurrentTest(),getCurrentTest().getStatsImagePassword(),check);
    }


    /**
     * Compare the password generate with the policy to a input string by user in an editText
     * @param inputOfUser the input string to compare with
     * @return true if the password are equals
     */
    public int checkPassword(String inputOfUser)
    {
        boolean check=false;
        if((getCurrentTest().getPasswordGenerated()).equals(inputOfUser))
        {
            check=true;
            getCurrentTest().getStatCharacterPassword().addSuccess();
        }else
        {
            getCurrentTest().getStatCharacterPassword().addFailure();
        }

        return level.calculateXp(getCurrentTest(),getCurrentTest().getStatCharacterPassword(),check);
    }

    public void addSymbolToPassword(Integer symbol)
    {
        //we add +1 else the first image (index ==0) has no impact on the password
        currentInput.add(symbol);
    }

    //When we finish to register a new Test This method is called
    public void addNewTest()
    {

        listTest.add(new Test(PasswordPolicyDialog.getChosenPolicy(),currentInput,currentPasswordGenerated));

        //we set tne number of try to 1, we know the tests begin for this user
        getCurrentTest().nextTry();
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
       //There are at least one Test
        if(listTest.isEmpty())
            return null;

        return listTest.get(listTest.size()-1);
    }

    //seem to be useless, but if yu want to save the listTest Field , firestore need to have an acess to it
    public ArrayList<Test> getListTest() {
        return listTest;
    }

    private void rescheduleAlarm(Context context)
    {
        //if failure we have to reschedule the alarms from the last one
        int newNumOfTry=getCurrentTest().getNumOfTry()-1;
        if(newNumOfTry>=0)
        {
            getCurrentTest().setNumOfTry(newNumOfTry);
            NotificationAlarm.createAlarmFrom(context,documentName,newNumOfTry);
        }
    }
}






























