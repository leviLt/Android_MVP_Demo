package com.mvp.demo.widget.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by luotao
 * 2018/1/22
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */

public class BottomBehavior extends CoordinatorLayout.Behavior<View> {
    private ObjectAnimator outAnimal, inAnimal;

    public BottomBehavior() {
    }

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        //垂直滑动的时候触发
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        /*
         * 上滑隐藏
         */
        if (dy > 0) {
            if (outAnimal == null) {
                outAnimal = ObjectAnimator.ofFloat(child, "translationY", 0, child.getHeight());
                outAnimal.setDuration(200);
                outAnimal.setInterpolator(new AccelerateInterpolator());
            }
            if (outAnimal != null && child.getTranslationY() <= 0) {
                outAnimal.start();
            }
        } else {
            if (inAnimal == null) {
                inAnimal = ObjectAnimator.ofFloat(child, "translationY", child.getHeight(), 0);
                inAnimal.setDuration(200);
                inAnimal.setInterpolator(new AccelerateInterpolator());
            }
            if (inAnimal != null && child.getTranslationY() >= child.getHeight()) {
                inAnimal.start();
            }
        }
    }
}
