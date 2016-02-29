package com.redbooth;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class BehaviorAnimation {
    public static final long DURATION = 10000L;
    private ObjectAnimator objectAnimatorY;
    private ObjectAnimator objectAnimatorX;
    private View view;
    private View parentView;

    private void configure() {
        objectAnimatorY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, -view.getTop() -view.getHeight());
        objectAnimatorY.setDuration(DURATION);
        objectAnimatorY.setInterpolator(new LinearInterpolator());
        objectAnimatorX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, parentView.getWidth());
        objectAnimatorX.setDuration(DURATION);
        objectAnimatorX.setInterpolator(new LinearInterpolator());
    }

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

    public static class Builder {
        private final BehaviorAnimation behaviorAnimation;

        public Builder() {
            behaviorAnimation = new BehaviorAnimation();
        }

        public Builder setView(View view) {
            behaviorAnimation.view = view;
            return this;
        }

        public Builder setParentView(View parentView) {
            behaviorAnimation.parentView = parentView;
            return this;
        }

        public BehaviorAnimation build() {
            behaviorAnimation.configure();
            return behaviorAnimation;
        }
    }
}
