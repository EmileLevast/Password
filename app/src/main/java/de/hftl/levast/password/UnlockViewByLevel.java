package de.hftl.levast.password;

import android.os.Build;
import android.view.View;

/**
 * Created by Levast on 23.04.2019.
 */

public class UnlockViewByLevel {
    //GenerateTest
    //RegisterButton
    //loginButton
    //PolicyButton
    //SentencesButton

    private final int UNLOCK_GENERATE_TEST=0;
    private final int UNLOCK_AUTOGENERATE_TEST=5;
    private final int UNLOCK_POLICY_PASSWORD=10;
    private final int UNLOCK_SENTENCES_NUMBER=15;
    private final int UNLOCK_GENERATE_PASSWORD=20;

    /*private View generateTest;
    private View autoGenerateTest;
    private View generatePassword;
    private View policyPassword;
    private View numberSentences;
*/
    private ViewAvailableLevel[] listView;

    public UnlockViewByLevel(MainActivity activity)
    {
        View generatePassword=activity.findViewById(R.id.loginButton);
        View generateTest=activity.findViewById(R.id.GenerateTest);
        View autoGenerateTest=activity.findViewById(R.id.RegisterButton);
        View policyPassword=activity.findViewById(R.id.PolicyButton);
        View numberSentences=activity.findViewById(R.id.SentencesButton);

        if (Build.VERSION.SDK_INT >= 26) {
            generatePassword.setTooltipText("");
        }

        listView= new ViewAvailableLevel[]{
        new ViewAvailableLevel(generatePassword,UNLOCK_GENERATE_PASSWORD),
                new ViewAvailableLevel(generateTest,UNLOCK_GENERATE_TEST),
                new ViewAvailableLevel(autoGenerateTest,UNLOCK_AUTOGENERATE_TEST),
                new ViewAvailableLevel(policyPassword,UNLOCK_POLICY_PASSWORD),
                new ViewAvailableLevel(numberSentences,UNLOCK_SENTENCES_NUMBER)};
    }

    public void checkIfViewToUnlock(int levelUser)
    {
        for(int i=0;i<listView.length;i++)
        {
            listView[i].unlockOrNotWith(levelUser);
        }
    }

}
