package com.redbooth.demo.animators;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.redbooth.demo.R;

public class InSyncAnimator {
    private AnimatorSet animator;
    private final View rootView;

    public InSyncAnimator(View rootView) {
        this.rootView = rootView;
        initializeAnimator();
    }

    private void initializeAnimator() {
        final View avatarView = rootView.findViewById(R.id.avatar5);
        final View arrowChartMaskView = rootView.findViewById(R.id.arrow_chart_mask);
        final ObjectAnimator scaleXAnimator = ObjectAnimator
                .ofFloat(avatarView, View.SCALE_X, 0f, 1f);
        scaleXAnimator.setDuration(300);
        scaleXAnimator.setInterpolator(new OvershootInterpolator());
        final ObjectAnimator scaleYAnimator = ObjectAnimator
                .ofFloat(avatarView, View.SCALE_Y, 0f, 1f);
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new OvershootInterpolator());
        final ObjectAnimator maskScaleXAnimator = ObjectAnimator
                .ofFloat(arrowChartMaskView, View.SCALE_X, 1f, 0f);
        maskScaleXAnimator.setDuration(500);
        maskScaleXAnimator.setInterpolator(new LinearInterpolator());
        animator = new AnimatorSet();
        animator.play(scaleXAnimator).with(scaleYAnimator).before(maskScaleXAnimator);
    }

    public void play() {
        animator.start();
    }
}
