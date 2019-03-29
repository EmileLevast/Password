package com.example.levast.password;

import android.content.Context;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Levast on 05.03.2019.
 *
 * Contains all the data about a test
 * all the success and the failure
 * the size of the sequence
 * the policy
 */
public class Test {

    //used as key for the listStat
    private final String CHARACTER_PASSWORD="CHARACTER_PASSWORD";
    private final String IMAGE_PASSWORD="IMAGE_PASSWORD";

    //contain the stats for a password with image and the passyord with characters
    private HashMap<String,StatsTest> listStat;

    //indicates How many time did the user test his memory for this registered test
    private int numOfTry;

    //to schedule alarm without removing alarm for other tests
    private int id;

    private String name;

    //indicates the number of sentences contained in the sequence for this test
    private int nbrSentenceForSequence;

    //contains the password generated with the sequence of images and the password policy
    private String passwordGenerated;
    private ArrayList<String> policy;
    //this list contains the position of the images chosen for this test
    private ArrayList<Integer> passwordSaved;

    //seem to be useless but just for object deserialization with firestore
    public Test() {
    }

    public Test(ArrayList<String> policy, ArrayList<Integer> passwordSaved,String passwordGenerated,int id,String name) {
        this.nbrSentenceForSequence = passwordSaved.size()/ImageLegend.allPagesImages.size();
        this.passwordSaved = new ArrayList<>(passwordSaved);
        this.policy = policy;
        this.passwordGenerated=passwordGenerated;
        this.id=id;
        this.name=name;


        listStat=new HashMap<>(0);
        listStat.put(CHARACTER_PASSWORD,new StatsTest());
        listStat.put(IMAGE_PASSWORD,new StatsTest());
    }

    //useful to getBack from Firestore
    public String getPasswordGenerated() {
        return passwordGenerated;
    }

    public int getNumOfTry() {
        return numOfTry;
    }

    public void setNumOfTry(int numOfTry) {
        this.numOfTry = numOfTry;
    }

    public void nextTry() {
        this.numOfTry++;
    }

    public void previousTry()
    {
        //if we are at try 1 we don't want to go to test zero
        if(numOfTry>1)
        {
            numOfTry--;
        }
    }

    public int getNbrSentenceForSequence() {
        return nbrSentenceForSequence;
    }

    public void setNbrSentenceForSequence(int nbrSentenceForSequence) {
        this.nbrSentenceForSequence = nbrSentenceForSequence;
    }

    public ArrayList<String> getPolicy() {
        return policy;
    }

    public ArrayList<Integer> getPasswordSaved() {
        return passwordSaved;
    }

    @Exclude
    public StatsTest getStatCharacterPassword()
    {
        return listStat.get(CHARACTER_PASSWORD);
    }

    @Exclude
    public StatsTest getStatsImagePassword()
    {
        return listStat.get(IMAGE_PASSWORD);
    }

    //only used to retrieve data from firestore
    public HashMap<String, StatsTest> getListStat() {
        return listStat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
