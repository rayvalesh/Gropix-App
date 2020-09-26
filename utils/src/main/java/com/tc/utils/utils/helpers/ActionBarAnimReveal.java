package com.tc.utils.utils.helpers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.tc.utils.R;

public class ActionBarAnimReveal {
    public static void circleReveal(Activity activity, int viewID, final boolean isShow, final int colorPrimary) {
        final View myView = activity.findViewById(viewID);
        final Resources resources = activity.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isShow) {
                myView.setBackgroundColor(resources.getColor(R.color.colorWhite));
            }
            int width = myView.getWidth();
            width -= (resources.getDimensionPixelSize(R.dimen.dimen_action_button_min_width_mat)) -
                    (resources.getDimensionPixelSize(R.dimen.dimen_action_button_min_width_mat) / 2);
            width -= resources.getDimensionPixelSize(R.dimen.dimen_action_button_min_width_overflow_mat);
            int cx = width;
            int cy = myView.getHeight() / 2;
            Animator anim;
            if (isShow)
                anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
            else
                anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);
            anim.setDuration((long) 400);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isShow) {
                        super.onAnimationEnd(animation);
                        myView.setBackgroundColor(resources.getColor(colorPrimary));
                    }
                }
            });
            anim.start();
        } else {
            if (isShow) {
                myView.setBackgroundColor(resources.getColor(R.color.colorWhite));
            } else {
                myView.setBackgroundColor(resources.getColor(colorPrimary));
            }
        }
    }
}
