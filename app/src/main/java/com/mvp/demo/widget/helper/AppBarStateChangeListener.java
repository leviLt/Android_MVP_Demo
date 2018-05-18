package com.mvp.demo.widget.helper;

import android.support.design.widget.AppBarLayout;

/**
 * Created by luotao
 * 2018/5/2
 * emil:luotaosc@foxmail.com
 * qq:751423471
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener{

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    protected enum State {EXPANDED, COLLAPSED, IDLE}
}
