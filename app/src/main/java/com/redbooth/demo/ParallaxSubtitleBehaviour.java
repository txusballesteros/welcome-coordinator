package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.redbooth.welcomecoordinator.WelcomeCoordinatorLayout;
import com.redbooth.welcomecoordinator.WelcomePageBehavior;

@SuppressWarnings("unused")
public class ParallaxSubtitleBehaviour extends WelcomePageBehavior {
    private final static int PARALLAX_FACTOR = 4;
    private ObjectAnimator parallaxAnimator;

    @Override
    protected void onCreate(WelcomeCoordinatorLayout coordinator) {
        final FrameLayout.LayoutParams params
                = (FrameLayout.LayoutParams)getPage().getLayoutParams();
        long startDelay;
        long duration;
        float rightTranslation;
        float leftTranslation;
        if (params.leftMargin == 0) {
            startDelay = 0;
            duration = getPage().getMeasuredWidth();
            rightTranslation = 0;
            leftTranslation = -(duration / PARALLAX_FACTOR);
        } else {
            startDelay = (params.leftMargin - coordinator.getMeasuredWidth());
            duration = (getPage().getMeasuredWidth() * 2);
            rightTranslation = (duration / PARALLAX_FACTOR);
            leftTranslation = -(duration / PARALLAX_FACTOR);
        }
        parallaxAnimator = ObjectAnimator
                .ofFloat(getTargetView(), View.TRANSLATION_X, rightTranslation, leftTranslation);
        parallaxAnimator.setInterpolator(new LinearInterpolator());
        parallaxAnimator.setTarget(getTargetView());
        parallaxAnimator.setStartDelay(startDelay);
        parallaxAnimator.setDuration(duration);
    }

    @Override
    protected void onPlaytimeChange(WelcomeCoordinatorLayout coordinator,
                                    float newPlaytime,
                                    float newScrollPosition) {
        long currentPlaytime = (long)newScrollPosition;
        if (newScrollPosition >= parallaxAnimator.getStartDelay()) {
            currentPlaytime = (long)(newScrollPosition - parallaxAnimator.getStartDelay());
        }
        parallaxAnimator.setCurrentPlayTime(currentPlaytime);
    }
}
