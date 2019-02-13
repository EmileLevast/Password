package com.example.levast.password;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

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
    private String passwordSaved;
    private int nbrFailure;
    private int nbrSuccess;

    /**
     * FIELD ONLY NEEDED AT RUN TIME
     */
    @Ignore
    //contains the input data of user who wants to log in
    private String currentInput;

    @Ignore
    //when true we register the new informations in "passwordSaved" array
    private boolean isSavingPassword;

    public User() {
        passwordSaved ="";
        currentInput ="";
        isSavingPassword=true;
        nbrFailure=0;
        nbrSuccess=0;
    }





/**
     * @return true if the two passwords are equals
     */
    public boolean checkPassword()
    {
        return passwordSaved.length()== currentInput.length() &&
                passwordSaved.equals(currentInput);
    }

    public void setPasswordSaved(String passwordSaved) {
        this.passwordSaved = passwordSaved;
    }

    public void addSymbolToPassword(Integer symbol)
    {
        if(isSavingPassword)
            passwordSaved +=symbol.toString();
        else
            currentInput+=symbol.toString();
    }

    public void logIn()
    {
        isSavingPassword=false;

        //we are going to enter a new password so we clear the previous data
        currentInput="";
    }

    public void register()
    {
        isSavingPassword=true;
        passwordSaved ="";
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

    public String getPasswordSaved() {
        return passwordSaved;
    }
}






























