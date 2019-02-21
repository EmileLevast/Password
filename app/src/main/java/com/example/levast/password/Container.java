package com.example.levast.password;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levast on 05.02.2019.
 */

public class Container <T>{

    ArrayList<T> list;
    int index;
    int countloop;

    //true when all the data were read at least one time
    //work only for indexup
    boolean didLoop;

    /*public Container(T... item) {
        list=new ArrayList<>(Arrays.asList(item));
        index=0;
        init();
    }*/

    public Container(List<T> item)
    {
        list=new ArrayList<>(item);
        index=0;
        init();
    }

    public  void indexUp()
    {
        if(index<list.size()-1)
        {
            index++;
        }
        else
        {
            countloop++;
            if(countloop>=GeneratePassword.NBR_OF_LOOP)
                didLoop =true;
            index=0;
        }
    }

    public  void indexDown()
    {
        if(index>0)
        {
            index--;
        }
        else
        {
            index=list.size()-1;
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getCurrentItem()
    {
        return list.get(index);
    }

    /**
     * @return the current item and go to the next
     */
    public T getItemNext()
    {
        T var=getCurrentItem();
        indexUp();
        return var;
    }

    public boolean isDidLoop() {
        return didLoop;
    }

    public void init()
    {
        didLoop =false;
        countloop=0;
    }

    public int getSize()
    {
        return list.size();
    }
}
