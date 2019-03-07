package com.example.levast.password;

/**
 * Created by Levast on 07.03.2019.
 */

public class StatsTest {
    private int success;
    private int failure;

    public int getSuccess() {
        return success;
    }

    public int getFailure() {
        return failure;
    }

    public void addFailure()
    {
        failure++;
    }

    public void addSuccess()
    {
        success++;
    }
}
