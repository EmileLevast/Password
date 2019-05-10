package de.hftl.levast.password;

import android.view.View;

/**
 * Created by Levast on 23.04.2019.
 */

public class ViewAvailableLevel {
    private View view;
    private int levelUnlock;//number to indicate which level the user need to unlock the view

    public ViewAvailableLevel(View view, int levelUnlock) {
        this.view = view;
        this.levelUnlock = levelUnlock;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getLevelUnlock() {
        return levelUnlock;
    }

    public void setLevelUnlock(int levelUnlock) {
        this.levelUnlock = levelUnlock;
    }

    public void unlockOrNotWith(int levelToTest)
    {
        if(levelToTest<levelUnlock)
        {
            view.setVisibility(View.INVISIBLE);
        }
        else
        {
            view.setVisibility(View.VISIBLE);
        }
    }
}
