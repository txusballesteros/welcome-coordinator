package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

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

    public AnimationFlyTo(@NonNull Context context, @NonNull AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onConfigure() {
        int[] viewLocation = new int[LENGTH_LOCATION_ARRAY];
        getLeftPositionFrom(getTargetView(), viewLocation);
        int[] destinyViewLocation = new int[LENGTH_LOCATION_ARRAY];
        getLeftPositionFrom(getDestinyView(), destinyViewLocation);
        objectAnimatorY = ObjectAnimator.ofFloat(getTargetView(), View.TRANSLATION_Y, 0, -(viewLocation[Y] - destinyViewLocation[Y]));
        objectAnimatorY.setDuration(DURATION);
        objectAnimatorY.setInterpolator(new LinearInterpolator());
        objectAnimatorX = ObjectAnimator.ofFloat(getTargetView(), View.TRANSLATION_X, 0, -(viewLocation[X] - destinyViewLocation[X]));
        objectAnimatorX.setDuration(DURATION);
        objectAnimatorX.setInterpolator(new LinearInterpolator());
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
        } else if (progress > INIT_TIME
                && progress <= FINAL_TIME) {
            long playTime = (long) ((progress - INIT_TIME) * DURATION);
            objectAnimatorY.setCurrentPlayTime(playTime);
            objectAnimatorX.setCurrentPlayTime(playTime);
        } else {
            objectAnimatorY.setCurrentPlayTime(DURATION);
            objectAnimatorX.setCurrentPlayTime(DURATION);
        }
    }
}
