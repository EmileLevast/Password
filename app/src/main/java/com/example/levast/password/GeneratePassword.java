package com.example.levast.password;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Levast on 15.02.2019.
 */

public final class GeneratePassword {

    /*
    Shared Preferences for Password Policy
     */
    public static String[] keyPasswordPolicy;//=new String[]{"Numbers","Special characters","Uppercase letters","lowercase letters"};


    private final static HashMap<String,List<Character>> listAllChar;
    static {
        listAllChar=new HashMap<>();

        listAllChar.put("Numbers", Arrays.asList('0','1','2','3','4','5','6','7','8','9'));
        listAllChar.put("Lowercase letters", Arrays.asList('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'));
        listAllChar.put("Special characters", Arrays.asList('\"','\'','!','^','#','$','%','&','/','{','(','[',')',']','=','}','?',
                '*','~','+','-','_',',',';',':','.','<','>'));
        listAllChar.put("Uppercase letters",Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'));

        //we load all the key in the list that we will display on dialog
        keyPasswordPolicy=listAllChar.keySet().toArray(new String[0]);
    }

    /**
     * @param sequence contains the number of the position of the chosen images
     * @return an integer corresponding to this sqce
     */
    public static BigInteger seqceInputToInt(List<Integer> sequence)
    {

        //we  use a big integer because if the user choose
        BigInteger res=BigInteger.valueOf(0L);
        Log.w("msg","=======================");
        //NBR_PAGES== S
        for(int i=0;i<sequence.size();i++)
        {
            Log.w("msg","e["+i+"]="+(sequence.get(i)+1));
            //we add 1 else the first img (==0) has no impact on the password
            //res+=((double)(sequence.get(i)+1))*Math.pow(MainActivity.NBR_COLUMN*MainActivity.NBR_LINE,i);
            res=res.add(BigInteger.valueOf((long) ((sequence.get(i)+1)*Math.pow(MainActivity.NBR_COLUMN*MainActivity.NBR_LINE,i))));
            Log.w("msg","\tPN:"+res);
        }
        Log.w("msg","PN:"+res);
        return res;
    }

    /**
     * @param nbr PN calculated with seqceInputToInt
     *
     */
    public static List<Character> intToSqceSymbol(BigInteger nbr,Context context)
    {
        //Z
        ArrayList<Character> serieSymbol=new ArrayList<>(0);
        ArrayList<Character> symbolAvailable=loadAllUsedCharacters(context);

        //M
        int nbrCharAvailable= symbolAvailable.size();



        //if the bigInteger is positiv
        while (nbr.compareTo(BigInteger.ZERO)==1)
        {
            serieSymbol.add(symbolAvailable.get(nbr.mod(BigInteger.valueOf(nbrCharAvailable)).intValue()));
            nbr=nbr.divide(BigInteger.valueOf(nbrCharAvailable));

        }

        return serieSymbol;
    }

    /**
     * We load all the characters that the user used for his password Policy
     */
    private static ArrayList<Character> loadAllUsedCharacters(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(MainActivity.NAME_SHARED_PREFERENCE,Context.MODE_PRIVATE);
        ArrayList<Character> listSymbolAvailable=new ArrayList<>(0);
        for(String key:keyPasswordPolicy)
        {
            if(sharedPreferences.getBoolean(key,false))
            {
                listSymbolAvailable.addAll(listAllChar.get(key));
            }
        }
        return listSymbolAvailable;
    }

    static String getPasswordAsText(Context context,ArrayList<Integer> sequence)
    {
        String text="";
        List<Character> password= intToSqceSymbol(seqceInputToInt(sequence),context);
        for(Character elt: password)
        {
            text+=elt;
        }
        return text;
    }
}
