package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.redbooth.WelcomeCoordinatorLayout;
import com.redbooth.WelcomePageBehavior;

public class AnimationFlightTo extends WelcomePageBehavior {
    public static final long DURATION = 10000L;
    public static final int INIT_TIME = 1;
    public static final int FINAL_TIME = 2;
    public static final int Y = 1;
    public static final int X = 0;
    public static final int LENGTH_LOCATION_ARRAY = 2;
    private ObjectAnimator objectAnimatorY;
    private ObjectAnimator objectAnimatorX;
    private ObjectAnimator objectAnimatorScaleX;
    private ObjectAnimator objectAnimatorScaleY;
    private ObjectAnimator objectAnimatorRotation;

    @Override
    protected void onConfigure() {
        configureTranslation();
        configureScale();
        configureRotation();
    }

    private void configureTranslation() {
        View targetView = getTargetView();
        int[] viewLocation = new int[LENGTH_LOCATION_ARRAY];
        getLeftPositionFrom(targetView, viewLocation);
        int[] destinyViewLocation = new int[LENGTH_LOCATION_ARRAY];
        getLeftPositionFrom(getDestinyView(), destinyViewLocation);
        objectAnimatorY = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_Y, 0, -(viewLocation[Y] - destinyViewLocation[Y]));
        objectAnimatorY.setInterpolator(new AccelerateInterpolator());
        objectAnimatorY.setDuration(DURATION);
        objectAnimatorX = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_X, 0, -(viewLocation[X] - destinyViewLocation[X]));
        objectAnimatorX.setInterpolator(new LinearInterpolator());
        objectAnimatorX.setDuration(DURATION);
    }

    private void configureScale() {
        View targetView = getTargetView();
        float scaleXFactor = ((float) getDestinyView().getMeasuredWidth() / (float) targetView.getMeasuredWidth());
        objectAnimatorScaleX = ObjectAnimator.ofFloat(targetView, View.SCALE_X, scaleXFactor);
        objectAnimatorScaleX.setDuration(DURATION);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());
        float scaleYFactor = ((float) getDestinyView().getMeasuredHeight() / (float) targetView.getMeasuredHeight());
        objectAnimatorScaleY = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, scaleYFactor);
        objectAnimatorScaleY.setDuration(DURATION);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());
    }

    private void configureRotation() {
        View targetView = getTargetView();
        objectAnimatorRotation = ObjectAnimator.ofFloat(targetView, View.ROTATION, -360/5);
        objectAnimatorRotation.setDuration(DURATION);
        objectAnimatorRotation.setInterpolator(new LinearInterpolator());
    }

    private void getLeftPositionFrom(View view, int[] location) {
        int x = view.getLeft();
        int y = view.getTop();
        View parent = (View) view.getParent();
        while(parent != null
                && !(parent instanceof WelcomeCoordinatorLayout)){
            x += parent.getLeft();
            y += parent.getTop();
            parent = (View)parent.getParent();
        }
        location[X] = x;
        location[Y] = y;
    }

    @Override
    public void setCurrentPlayTime(float progress) {
        if (progress <= INIT_TIME) {
            setCurrentTimeInAllAnimators(0);
        } else if (progress > INIT_TIME
                && progress <= FINAL_TIME) {
            long playTime = (long) ((progress - INIT_TIME) * DURATION);
            setCurrentTimeInAllAnimators(playTime);
        } else {
            setCurrentTimeInAllAnimators(DURATION);
        }
    }

    private void setCurrentTimeInAllAnimators(long playTime) {
        objectAnimatorY.setCurrentPlayTime(playTime);
        objectAnimatorX.setCurrentPlayTime(playTime);
        objectAnimatorScaleX.setCurrentPlayTime(playTime);
        objectAnimatorScaleY.setCurrentPlayTime(playTime);
        objectAnimatorRotation.setCurrentPlayTime(playTime);
    }
}
