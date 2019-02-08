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
    private String PasswordSaved;


    /**
     * FIELD ONLY NEEDED AT RUN TIME
     */
    @Ignore
    //contains the input data of user who wants to log in
    private String currentInput;

    @Ignore
    //when true we register the new informations in "PasswordSaved" array
    private boolean isSavingPassword;

    public User() {
        PasswordSaved ="";
        currentInput ="";
        isSavingPassword=true;
    }

    /**
     * @return true if the two passwords are equals
     */
    public boolean checkPassword()
    {
        return PasswordSaved.length()== currentInput.length() &&
                PasswordSaved.equals(currentInput);
    }

    public void setPasswordSaved(String passwordSaved) {
        this.PasswordSaved = passwordSaved;
    }

    public void addSymbolToPassword(Integer symbol)
    {
        if(isSavingPassword)
            PasswordSaved +=symbol.toString();
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
        PasswordSaved ="";
    }

    /*public String passwordInputEnd()
    {
        String print;

        //if we were saving a new password
        if(isSavingPassword)
        {
            print="New Password: "+PasswordSaved;
        }
        else
        {
            if(checkPassword())
                print="Correct Password";
            else
                print="Incorrect Password";
        }

        return print;
    }*/

    public boolean isSavingPassword() {
        return isSavingPassword;
    }

    public String getPasswordSaved() {
        return PasswordSaved;
    }
}
