package com.example.levast.password;


import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

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

    //when true we register the new informations in "passwordSaved" array
    private boolean isSavingPassword;

    //true if the user is trying to remember his password (test with the notification)
    private boolean isTrying;

    //contain the name of the document that refer to the user in Firestore
    private String documentName;

    public User() {
    }

    public User(String documentName) {
        this.documentName=documentName;

        currentInput =new ArrayList<>(0);
        listTest=new ArrayList<>(0);
        isSavingPassword=true;
        isTrying=true;
    }

    /**
     * @return true if the two passwords are equals
     */
    public boolean checkPassword()
    {
        boolean check=false;
        ArrayList<Integer> passwordSaved= getCurrentTest().getPasswordSaved();
        if(passwordSaved.size()== currentInput.size() &&
                passwordSaved.equals(currentInput))
        {
            check=true;
            getCurrentTest().addSuccess();
        }
        else
        {
            getCurrentTest().addFailure();
        }
        return  check;
    }

    public void addSymbolToPassword(Integer symbol)
    {
        currentInput.add(symbol);
    }

    //When we finish to register a new Test This method is called
    public void addNewTest()
    {

        listTest.add(new Test(PasswordPolicyDialog.getChosenPolicy(),currentInput));

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
    public boolean isSavingPassword() {
        return isSavingPassword&&!isTrying;
    }

    @Exclude
    public ArrayList<Integer> getCurrentInput() {
        return currentInput;
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
}






























