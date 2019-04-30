package de.hftl.levast.password;

/**
 * Created by Levast on 07.03.2019.
 */

public class StatsTest {
    private int success;
    private int failure;

    //how many time you remember your password but reset for each failure
    private int currentSuite;

    public int getSuccess() {
        return success;
    }

    public int getFailure() {
        return failure;
    }

    public void addFailure()
    {
        failure++;
        currentSuite=0;
    }

    public void addSuccess()
    {
        success++;
        currentSuite++;
    }

    public int getCurrentSuite() {
        return currentSuite;
    }
}
