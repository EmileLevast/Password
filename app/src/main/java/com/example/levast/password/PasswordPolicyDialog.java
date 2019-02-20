package com.example.levast.password;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.GnssClock;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.util.List;

/**
 * Created by Levast on 20.02.2019.
 */

public class PasswordPolicyDialog extends DialogFragment {

    /*
    Use with boolean , example if field corrsponding to KEY_NUMBERS = true then we use numbers to generate password
     */
    private SharedPreferences sharedPreferences;
    public final static String NAME_SHARED_PREFERENCE="NAME_SHARED_PREFERENCE_PASSWORD_POLICY";
    /*public final static String KEY_NUMBERS="KEY_NUMBERS";
    public final static String KEY_CAPITAL_LETTERS="KEY_CAPITAL_LETTERS";
    public final static String KEY_LOWERCASE_LETTERS="KEY_LOWERCASE_LETTERS";*/




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //load the sharedPreference
        sharedPreferences=getActivity().getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        //we search in the preference what is the current choice of the user, let display the item already checked
        boolean[] itemChecked=new boolean[GeneratePassword.keyPasswordPolicy.length];
        for(int i=0;i<GeneratePassword.keyPasswordPolicy.length;i++)
        {
            itemChecked[i]=sharedPreferences.getBoolean(GeneratePassword.keyPasswordPolicy[i],false);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Password Policy")

                .setMultiChoiceItems(GeneratePassword.keyPasswordPolicy, itemChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        //we write the choice of the user in sharedPreference
                        editor.putBoolean( GeneratePassword.keyPasswordPolicy[i],b);
                        editor.apply();
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });


        return builder.create();
    }
}
