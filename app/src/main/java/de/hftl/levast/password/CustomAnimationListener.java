package de.hftl.levast.password;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Levast on 30.04.2019.
 */

public class CustomAnimationListener implements Animation.AnimationListener {

    private View viewToAnimate;

    public CustomAnimationListener(View viewToAnimate) {
        this.viewToAnimate = viewToAnimate;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        viewToAnimate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        viewToAnimate.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
