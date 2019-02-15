package com.example.levast.password;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.util.ArrayList;

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

    /**
     * FIELD ONLY NEEDED AT RUN TIME
     */
    @Ignore
    //contains the input data of user who wants to log in
    private ArrayList<Integer> currentInput;

    @Ignore
    //when true we register the new informations in "passwordSaved" array
    private boolean isSavingPassword;

    public User() {
        passwordSaved =new ArrayList<>(0);
        currentInput =new ArrayList<>(0);
        isSavingPassword=true;
        nbrFailure=0;
        nbrSuccess=0;
    }





/**
     * @return true if the two passwords are equals
     */
    public boolean checkPassword()
    {
        return passwordSaved.size()== currentInput.size() &&
                passwordSaved.equals(currentInput);
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

    public void logIn()
    {
        isSavingPassword=false;

        //we are going to enter a new password so we clear the previous data
        currentInput.clear();
    }

    public void register()
    {
        isSavingPassword=true;
        passwordSaved.clear();
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

    public void addSuccess()
    {
        nbrSuccess++;
    }

    public void addFailure()
    {
        nbrFailure++;
    }

    public boolean isSavingPassword() {
        return isSavingPassword;
    }

    public ArrayList<Integer> getPasswordSaved() {
        return passwordSaved;
    }
}






























