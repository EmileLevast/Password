package com.example.levast.password;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Levast on 20.02.2019.
 */

public class PasswordPolicyDialog extends DialogFragment {

    private boolean[] itemChecked;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //we search in the preference what is the current choice of the user, let display the item already checked
        itemChecked=new boolean[GeneratePassword.keyPasswordPolicy.length];
        for(int i=0;i<GeneratePassword.keyPasswordPolicy.length;i++)
        {
            itemChecked[i]=MainActivity.sharedPreferences.getBoolean(GeneratePassword.keyPasswordPolicy[i],false);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Password Policy")

                .setMultiChoiceItems(GeneratePassword.keyPasswordPolicy, itemChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        //we write the choice of the user
                        itemChecked[i]=b;

                        AlertDialog dialog= (AlertDialog) getDialog();

                        if(dialog!=null)
                        {
                            if(b && checkAtLeastOneItemSelected())
                            {
                                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                            }
                            else
                            {
                                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                            }
                        }


                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //we check that he user selected at least one policy

                    SharedPreferences.Editor editor=MainActivity.sharedPreferences.edit();
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


   /* @Override
    public void onDismiss(DialogInterface dialog) {



        if(checkAtLeastOneItemSelected())
        {
            super.onDismiss(dialog);
        }
        else
        {
            Toast.makeText(getContext(),"Error: choose at least 1 item",Toast.LENGTH_LONG).show();
            show(getFragmentManager(),"dialogPolicy");
        }
    }*/

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

    public static ArrayList<String> getChosenPolicy()
    {
        ArrayList<String> policyChosen=new ArrayList<>(0);
        for(int i=0;i<GeneratePassword.keyPasswordPolicy.length;i++)
        {
            if(MainActivity.sharedPreferences.getBoolean(GeneratePassword.keyPasswordPolicy[i],false))
            {
                policyChosen.add(GeneratePassword.keyPasswordPolicy[i]);
            }
        }
        return policyChosen;
    }

    /**
     * Check that at least one policy is selected in the sharedPreference
     * If it's not the case we set one policy
     */
    public static void checkAtLeastOnePolicySharedPref(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(MainActivity.NAME_SHARED_PREFERENCE, Context.MODE_PRIVATE);

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
