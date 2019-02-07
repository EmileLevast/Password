package com.example.levast.password;

import java.util.ArrayList;


/**
 * Created by Levast on 05.02.2019.
 */

public class UserPassword<T> {

    //this list contains the password which allows the user to log in
    private String saved;

    //contains the input data of user who wants to log in
    private String currentInput;

    //when true we register the new informations in "saved" array
    private boolean isSavingPassword;

    public UserPassword() {
        saved ="";
        currentInput ="";
        isSavingPassword=true;
    }

    /**
     * @return true if the two passwords are equals
     */
    public boolean checkPassword()
    {
        return saved.length()== currentInput.length() &&
                saved.equals(currentInput);
    }

    public void setSaved(String saved) {
        this.saved = saved;
    }

    public void addSymbolToPassword(T symbol)
    {
        if(isSavingPassword)
            saved+=symbol.toString();
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
        saved="";
    }

    /*public String passwordInputEnd()
    {
        String print;

        //if we were saving a new password
        if(isSavingPassword)
        {
            print="New Password: "+saved;
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

    public String getSavedPassword() {
        return saved;
    }
}
