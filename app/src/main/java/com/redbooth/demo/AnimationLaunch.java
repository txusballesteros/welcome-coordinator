package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.redbooth.WelcomeCoordinatorLayout;
import com.redbooth.WelcomePageBehavior;

public class AnimationLaunch extends WelcomePageBehavior {
    public static final long DURATION = 10000L;
    private ObjectAnimator objectAnimatorY;
    private ObjectAnimator objectAnimatorX;

    public AnimationLaunch(@NonNull WelcomeCoordinatorLayout coordinatorLayout, @NonNull View view, @Nullable View destinyView) {
        super(coordinatorLayout, view, destinyView);
    }

    @Override
    public void configure() {
        objectAnimatorY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, -view.getTop() -view.getHeight());
        objectAnimatorY.setDuration(DURATION);
        objectAnimatorY.setInterpolator(new LinearInterpolator());
        objectAnimatorX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, coordinatorLayout.getWidth());
        objectAnimatorX.setDuration(DURATION);
        objectAnimatorX.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void setCurrentPlayTime(float progress) {
        if (progress < 1) {
            long playTime = (long) (progress * DURATION);
            objectAnimatorY.setCurrentPlayTime(playTime);
            objectAnimatorX.setCurrentPlayTime(playTime);
        } else {
            objectAnimatorY.setCurrentPlayTime(1);
            objectAnimatorX.setCurrentPlayTime(1);
        }
    }
}
