package de.hftl.levast.password;

/**
 * Created by Levast on 07.03.2019.
 */

public class Level {

    public static final int XP_BY_LEVEL=100;
    private final int BONUS_XP_SUITE =6;
    private final int BONUS_XP_NUM_OF_TRY=4;
    private final int BONUS_XP_NUMBER_OF_TESTS=2;
    private final int XP_WIN_FOR_SUCCESS=15;

    private int currentLevel;
    private int xp;

    public Level() {
        currentLevel=1;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getXp() {
        return xp;
    }

    private int addXp(int addXp)
    {
        addXp=Math.abs(addXp);
        int nbrLevelUp=0;
        xp+=addXp;
        if(xp>=currentLevel*XP_BY_LEVEL)
        {
            //we win a level

            //we calculate the xpSupp already won for the next level(and maybe we can get a second level
            int xpTempo=xp;
            xp=0;
            xpTempo-=currentLevel*XP_BY_LEVEL;
            currentLevel++;

            nbrLevelUp=1+addXp(xpTempo);
        }
        return nbrLevelUp;
    }

    private int loseXp(int loseXp)
    {
        loseXp=Math.abs(loseXp);
        int nbrLevelDown=0;
        xp-=loseXp;
        if(xp<=0)
        {
            if(currentLevel>1)
            {
                int xpTempo=xp;
                currentLevel--;
                xp=currentLevel*XP_BY_LEVEL;
                nbrLevelDown=1+loseXp(xpTempo);

            }else
            {
                xp=0;
            }
        }
        return nbrLevelDown;
    }

    /**
     * We calculate and add the xp
     * @param user
     * @param statsTest
     * @param success
     */
    public int calculateXp(User user, StatsTest statsTest, boolean success)
    {
        Test test=user.getCurrentTest();

        //Xp will be multiplicated By 2
        float factorXp=((float)(test.getPolicy().size()))/4f;
        factorXp+=test.getNbrSentenceForSequence();

        //if you make an error but your test was pretty hard you lose less points
        if(!success)
            factorXp=1/factorXp;

        int bonusXp= BONUS_XP_SUITE *statsTest.getCurrentSuite();
        bonusXp+=BONUS_XP_NUM_OF_TRY*test.getNumOfTry();
        bonusXp+=BONUS_XP_NUMBER_OF_TESTS*user.getListTest().size();

        int xpTotal= (int) ((XP_WIN_FOR_SUCCESS+bonusXp)*factorXp);

        if(success)
        {
            addXp(xpTotal);
        }
        else
        {
            loseXp(xpTotal);

            //just to return an negative number to print to the user
            xpTotal*=-1;
        }

        return xpTotal;
    }
}
