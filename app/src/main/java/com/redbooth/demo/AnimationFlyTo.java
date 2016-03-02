package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.redbooth.WelcomeCoordinatorLayout;
import com.redbooth.WelcomePageBehavior;

public class AnimationFlyTo extends WelcomePageBehavior {
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

    @Override
    protected void onConfigure() {
        int[] viewLocation = new int[LENGTH_LOCATION_ARRAY];
        View targetView = getTargetView();
        getLeftPositionFrom(targetView, viewLocation);
        int[] destinyViewLocation = new int[LENGTH_LOCATION_ARRAY];
        getLeftPositionFrom(getDestinyView(), destinyViewLocation);
        objectAnimatorY = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_Y, 0, -(viewLocation[Y] - destinyViewLocation[Y]));
        objectAnimatorY.setDuration(DURATION);
        objectAnimatorY.setInterpolator(new LinearInterpolator());
        objectAnimatorX = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_X, 0, -(viewLocation[X] - destinyViewLocation[X]));
        objectAnimatorX.setDuration(DURATION);
        objectAnimatorX.setInterpolator(new LinearInterpolator());
        float scaleXFactor = ((float) getDestinyView().getMeasuredWidth() / (float) targetView.getMeasuredWidth());
        objectAnimatorScaleX = ObjectAnimator.ofFloat(targetView, View.SCALE_X, scaleXFactor);
        objectAnimatorScaleX.setDuration(DURATION);
        objectAnimatorScaleX.setInterpolator(new LinearInterpolator());
        targetView.setPivotX(0);
        float scaleYFactor = ((float) getDestinyView().getMeasuredHeight() / (float) targetView.getMeasuredHeight());
        objectAnimatorScaleY = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, scaleYFactor);
        objectAnimatorScaleY.setDuration(DURATION);
        objectAnimatorScaleY.setInterpolator(new LinearInterpolator());
        targetView.setPivotY(0);
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
            objectAnimatorY.setCurrentPlayTime(0);
            objectAnimatorX.setCurrentPlayTime(0);
            objectAnimatorScaleX.setCurrentPlayTime(0);
            objectAnimatorScaleY.setCurrentPlayTime(0);
        } else if (progress > INIT_TIME
                && progress <= FINAL_TIME) {
            long playTime = (long) ((progress - INIT_TIME) * DURATION);
            objectAnimatorY.setCurrentPlayTime(playTime);
            objectAnimatorX.setCurrentPlayTime(playTime);
            objectAnimatorScaleX.setCurrentPlayTime(playTime);
            objectAnimatorScaleY.setCurrentPlayTime(playTime);
        } else {
            objectAnimatorY.setCurrentPlayTime(DURATION);
            objectAnimatorX.setCurrentPlayTime(DURATION);
            objectAnimatorScaleX.setCurrentPlayTime(DURATION);
            objectAnimatorScaleY.setCurrentPlayTime(DURATION);
        }
    }
}
