package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.view.View;

import com.redbooth.welcomecoordinator.WelcomeCoordinatorLayout;
import com.redbooth.welcomecoordinator.WelcomePageBehavior;

@SuppressWarnings("unused")
public class AnimationFillWithColor extends WelcomePageBehavior {
    public static final long DURATION = 10000L;
    public static final int INIT_PLAY_TIME = 2;
    private ObjectAnimator objectAnimatorY;

    @Override
    protected void onCreate(WelcomeCoordinatorLayout coordinator) {
        configureTranslations();
    }

    protected void configureTranslations() {
        objectAnimatorY = ObjectAnimator.ofFloat(getTargetView(), View.TRANSLATION_Y, getTargetView().getHeight(), -getTargetView().getWidth()/2 - getPage().getPaddingTop());
        objectAnimatorY.setDuration(DURATION);
    }

    @Override
    protected void onPlaytimeChange(WelcomeCoordinatorLayout coordinator,
                                    float currentPlaytime,
                                    float newScrollPosition) {
        if (currentPlaytime <= INIT_PLAY_TIME) {
            objectAnimatorY.setCurrentPlayTime(0);
        } else {
            long playTime = (long) ((currentPlaytime - INIT_PLAY_TIME) * DURATION);
            objectAnimatorY.setCurrentPlayTime(playTime);
        }
    }
}
