package com.redbooth.demo.animators;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.redbooth.demo.R;

public class RocketFlightAwayAnimator {
    private AnimatorSet animator;
    private final View rootView;

    public RocketFlightAwayAnimator(View rootView) {
        this.rootView = rootView;
        initializeAnimator();
    }

    private void initializeAnimator() {
        final View rocket = rootView.findViewById(R.id.rocket_page4);
        Animator rocketScaleAnimator = getScaleAndVisibilityAnimator(rocket);
        Animator rocketRotationAnimator = getRotationAnimator(rocket);
        Animator rocketTranslationAnimator = getTranslationAnimator(rocket);
        animator = new AnimatorSet();
        animator.setStartDelay(600);
        animator.playTogether(rocketScaleAnimator, rocketRotationAnimator, rocketTranslationAnimator);
    }

    private AnimatorSet getScaleAndVisibilityAnimator(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(1000);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 1f, 0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 1f, 0f);
        animator.playTogether(scaleXAnimator, scaleYAnimator);
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                targetView.setVisibility(View.VISIBLE);
            }
        });
        return animator;
    }

    private AnimatorSet getRotationAnimator(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator(1.5f));
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targetView, View.ROTATION, 0f, 45f);
        animator.play(scaleXAnimator);
        return animator;
    }

    private AnimatorSet getTranslationAnimator(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(1000);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_X, -rootView.getWidth()/2, 0f);
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_Y, rootView.getWidth()/2, 0f);
        translationYAnimator.setInterpolator(new DecelerateInterpolator(1.5f));
        animator.playTogether(translationXAnimator, translationYAnimator);
        return animator;
    }

    public void play() {
        animator.start();
    }

    public abstract class AnimatorListener implements Animator.AnimatorListener {
        public abstract void onAnimationStart(Animator animation);
        public void onAnimationEnd(Animator animation) {}
        public void onAnimationCancel(Animator animation) {}
        public void onAnimationRepeat(Animator animation) {}
    }
}
