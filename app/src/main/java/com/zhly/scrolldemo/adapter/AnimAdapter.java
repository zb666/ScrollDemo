package com.zhly.scrolldemo.adapter;

import android.view.animation.Animation;

/**
 * Created by zb666 on 2018/11/8.
 */

public abstract class AnimAdapter implements Animation.AnimationListener{
    @Override
    public void onAnimationStart(Animation animation) {
        onStart();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onEnd();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    protected abstract void onStart();

    protected abstract void onEnd();
}
