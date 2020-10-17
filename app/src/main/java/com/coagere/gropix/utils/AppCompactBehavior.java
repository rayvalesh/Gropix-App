package com.coagere.gropix.utils;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


public class AppCompactBehavior extends CoordinatorLayout.Behavior<CardView> {
    private CardView view;

    public AppCompactBehavior() {
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull CardView child, @NonNull View dependency) {
        view = child;
        return dependency instanceof FrameLayout;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull CardView child,
                                       @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes, int type) {
        view = child;
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull CardView child, @NonNull View target, float velocityX, float velocityY) {
        view = child;
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    public void hideCardView() {
        if (view != null)
            view.animate().translationY(view.getHeight());
    }

    public void showCardView() {
        if (view != null)
            view.animate().translationY(0);
    }
}