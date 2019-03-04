package com.example.levast.password;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by Levast on 22.02.2019.
 */

public class NumberSentencesDialog extends DialogFragment {

    //indicate the number of sentence we want for one sequence
    public static int NBR_SENTENCES =2;
    public static final int DEFAULT_NUMBER_SENTENCES=1;
    private SharedPreferences sharedPreferences;

    //for the SharedPrefereces ,it is an int
    public static final String KEY_NUMBER_SENTENCES="KEY_NUMBER_SENTENCES";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //load the sharedPreference


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Number of sentences");

        builder.setSingleChoiceItems(new String[]{"1", "2", "3"},sharedPreferences.getInt(KEY_NUMBER_SENTENCES,DEFAULT_NUMBER_SENTENCES)-1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt(KEY_NUMBER_SENTENCES,i+1);
                editor.apply();
                NBR_SENTENCES=i+1;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        super.onCreate(savedInstanceState);
        return builder.create();
    }
}
