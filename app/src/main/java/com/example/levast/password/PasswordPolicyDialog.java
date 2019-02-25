package com.example.levast.password;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.GnssClock;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

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
    private boolean[] itemChecked;





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        //load the sharedPreference
        sharedPreferences=getActivity().getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        //we search in the preference what is the current choice of the user, let display the item already checked
        itemChecked=new boolean[GeneratePassword.keyPasswordPolicy.length];
        for(int i=0;i<GeneratePassword.keyPasswordPolicy.length;i++)
        {
            itemChecked[i]=sharedPreferences.getBoolean(GeneratePassword.keyPasswordPolicy[i],false);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Password Policy")

                .setMultiChoiceItems(GeneratePassword.keyPasswordPolicy, itemChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        //we write the choice of the user in sharedPreference

                        itemChecked[i]=b;

                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //we check that he user selected at least one policy

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    for(int j=0;j<itemChecked.length;j++)
                    {
                        editor.putBoolean( GeneratePassword.keyPasswordPolicy[j],itemChecked[j]);
                    }
                    editor.apply();
                    dismiss();
                }
        });

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {



        if(checkAtLeastOneItemSelected())
        {
            super.onDismiss(dialog);
        }
        else
        {
            show(getFragmentManager(),"dialogPolicy");
            Toast.makeText(getContext(),"Error: choose at least 1 item",Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkAtLeastOneItemSelected()
    {
        for(boolean item : itemChecked)
        {
            if(item)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check that at least one policy is selected in the sharedPreference
     * If it's not the case we set one policy
     */
    public static void checkAtLeastOnePolicySharedPref(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

        for(int i=0;i<GeneratePassword.keyPasswordPolicy.length;i++)
        {
            //if we meet one policy as selected, that#s enough we return
            if(sharedPreferences.getBoolean(GeneratePassword.keyPasswordPolicy[i],false))
            {
                return;
            }
        }

        //if none of the policy was selected , we set the first one
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(GeneratePassword.keyPasswordPolicy[0],true);
        editor.apply();
    }
}
