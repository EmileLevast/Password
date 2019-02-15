package com.example.levast.password;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levast on 15.02.2019.
 */

public final class GeneratePassword {

    /**
     * @param sequence contains the number of the position of the chosen images
     * @return an integer corresponding to this sqce
     */
    public static long seqceInputToInt(List<Integer> sequence)
    {

        long res=0;

        //NBR_PAGES== S
        for(int i=0;i<MainActivity.NBR_PAGE;i++)
        {
            res+=sequence.get(i)*Math.pow(MainActivity.NBR_COLUMN*MainActivity.NBR_LINE,i);
        }
        //res == PN
        return res;
    }

    /**
     * @param nbr PN calculated with seqceInputToInt
     * @return
     */
    public static List<Character> intToSqceSymbol(long nbr)
    {
        //Z
        ArrayList<Character> serieSymbol=new ArrayList<>(0);

        //Q
        long varTempo=nbr;

        //M
        int nbrCharAvailable=MainActivity.tableASCII.length;


        while (varTempo>0)
        {
            serieSymbol.add(MainActivity.tableASCII[(int) (varTempo%nbrCharAvailable)]);
            varTempo/=nbrCharAvailable;
        }

        return serieSymbol;
    }
}
