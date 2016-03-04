package com.redbooth.demo.animators;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.redbooth.demo.R;

public class RocketAvatarsAnimator {
    private AnimatorSet animator;
    private final View rootView;

    public RocketAvatarsAnimator(View rootView) {
        this.rootView = rootView;
        initializeAnimator();
    }

    private void initializeAnimator() {
        final View avatar1 = rootView.findViewById(R.id.avatar1);
        final View avatar2 = rootView.findViewById(R.id.avatar2);
        final View avatar3 = rootView.findViewById(R.id.avatar3);
        final View avatar4 = rootView.findViewById(R.id.avatar4);
        Animator avatar1Animator = getAnimator(avatar1);
        Animator avatar2Animator = getAnimator(avatar2);
        Animator avatar3Animator = getAnimator(avatar3);
        Animator avatar4Animator = getAnimator(avatar4);
        animator = new AnimatorSet();
        animator.setStartDelay(1000);
        animator.play(avatar3Animator).after(avatar4Animator);
        animator.play(avatar2Animator).after(avatar3Animator);
        animator.play(avatar1Animator).after(avatar2Animator);
    }

    private AnimatorSet getAnimator(View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 1f);
        animator.playTogether(scaleXAnimator, scaleYAnimator);
        return animator;
    }

    public void play() {
        animator.start();
    }
}
