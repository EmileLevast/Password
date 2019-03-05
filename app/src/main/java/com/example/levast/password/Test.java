package com.example.levast.password;

import java.util.ArrayList;

/**
 * Created by Levast on 05.03.2019.
 *
 * Contains all the data about a test
 * all the success and the failure
 * the size of the sequence
 * the policy
 */
public class Test {

    private int success;
    private int failure;
    private int numOfTry;
    private int nbrSentenceForSequence;
    private ArrayList<String> policy;
    //this list contains the password which allows the user to log in
    private ArrayList<Integer> passwordSaved;

    //seem to be useless but just for object deserialization with firestore
    public Test() {
    }

    public Test(ArrayList<String> policy, ArrayList<Integer> passwordSaved) {
        this.nbrSentenceForSequence = passwordSaved.size()/ImageLegend.listTheme.length;
        this.passwordSaved = new ArrayList<>(passwordSaved);
        this.policy = policy;
        success=0;
        failure=0;
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

    public int getNbrSentenceForSequence() {
        return nbrSentenceForSequence;
    }

    public void setNbrSentenceForSequence(int nbrSentenceForSequence) {
        this.nbrSentenceForSequence = nbrSentenceForSequence;
    }

    public ArrayList<String> getPolicy() {
        return policy;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void addSuccess()
    {
        success++;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public void addFailure()
    {
        failure++;
    }

    public ArrayList<Integer> getPasswordSaved() {
        return passwordSaved;
    }
}
