package com.example.levast.password;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levast on 05.02.2019.
 */

@Entity
public class User {

    /**
     * DATA TO SAVE IN THE DATABASE
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    //this list contains the password which allows the user to log in
    private ArrayList<Integer> passwordSaved;
    private int nbrFailure;
    private int nbrSuccess;

    //time since the user tried to log in
    //used to schedule callback with notification
    private long timeFirstTry;
    //indicates what is the nummer of this try
    private int numOftry;

    //save the choice of the user about the number of sentences
    private int nbrOfSentenceForTry;

    /**
     * FIELD ONLY NEEDED AT RUN TIME
     */
    @Ignore
    //contains the input data of user who wants to log in
    private ArrayList<Integer> currentInput;

    @Ignore
    //when true we register the new informations in "passwordSaved" array
    private boolean isSavingPassword;

    @Ignore
    //true if the user is trying to remember his password (test with the notification)
    private boolean isTrying;

    public User() {
        passwordSaved =new ArrayList<>(0);
        currentInput =new ArrayList<>(0);
        isSavingPassword=true;
        isTrying=true;
        nbrFailure=0;
        nbrSuccess=0;
        numOftry=0;
        timeFirstTry =0;
    }





/**
     * @return true if the two passwords are equals
     */
    public boolean checkPassword()
    {
        boolean check=false;
        if(passwordSaved.size()== currentInput.size() &&
                passwordSaved.equals(currentInput))
        {
            check=true;
            addSuccess();
        }
        else
        {
            addFailure();
        }
        return  check;
    }

    public void setPasswordSaved(ArrayList<Integer> passwordSaved) {
        this.passwordSaved = passwordSaved;
    }

    public void addSymbolToPassword(Integer symbol)
    {
        if(isSavingPassword)
            passwordSaved.add(symbol);
        else
            currentInput.add(symbol);
    }

    public void initStat()
    {
        numOftry=0;
        timeFirstTry=System.currentTimeMillis();
        nbrSuccess=0;
        nbrFailure=0;
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

        passwordSaved.clear();
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



    public int getNbrFailure() {
        return nbrFailure;
    }

    public void setNbrFailure(int nbrFailure) {
        this.nbrFailure = nbrFailure;
    }

    public int getNbrSuccess() {
        return nbrSuccess;
    }

    public void setNbrSuccess(int nbrSuccess) {
        this.nbrSuccess = nbrSuccess;
    }

    public int getNumOftry() {
        return numOftry;
    }

    public void nextTry() {
        this.numOftry ++;
    }

    public void addSuccess()
    {
        nbrSuccess++;
    }

    public void addFailure()
    {
        nbrFailure++;
    }

    public int getNbrOfSentenceForTry() {
        return nbrOfSentenceForTry;
    }

    public void setNbrOfSentenceForTry(int nbrOfSentenceForTry) {
        this.nbrOfSentenceForTry = nbrOfSentenceForTry;
    }

    public boolean isTrying() {
        return isTrying;
    }

    public void setTrying(boolean trying) {
        isTrying = trying;
    }

    public long getTimeFirstTry() {
        return timeFirstTry;
    }

    public void setNumOftry(int numOftry) {
        this.numOftry = numOftry;
    }

    public void setTimeFirstTry(long timeFirstTry) {
        this.timeFirstTry = timeFirstTry;
    }

    public boolean isSavingPassword() {
        return isSavingPassword;
    }

    public ArrayList<Integer> getPasswordSaved() {
        return passwordSaved;
    }

    public ArrayList<Integer> getCurrentInput() {
        return currentInput;
    }

    public ArrayList<Integer> getCurrentUsedPassword()
    {
        if(isSavingPassword)
        {
            return passwordSaved;
        }else
        {
            return currentInput;
        }
    }
}






























