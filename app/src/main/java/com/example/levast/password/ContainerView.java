package com.example.levast.password;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Levast on 05.02.2019.
 */

public class ContainerView {

    private HashMap<Integer,View> listView;// key correspond to the id of the view
    private int index;

    public ContainerView(MainActivity activity, Integer... item) {


        listView=new HashMap<>(0);

        for (Integer viewId : item) {
            View view=(activity.findViewById(viewId));

            view.setVisibility(View.INVISIBLE);
            listView.put(viewId,view);
        }

        index=0;


    }

    public void printView(Integer viewId)
    {
        if(index!=0)
            listView.get(index).setVisibility(View.INVISIBLE);
        listView.get(viewId).setVisibility(View.VISIBLE);
        index=viewId;
    }
}
