/*
 * Copyright Txus Ballesteros 2016 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.redbooth.demo;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.redbooth.WelcomeCoordinatorLayout;
import com.redbooth.WelcomePageBehavior;

@SuppressWarnings("unused")
public class AnimationFlightTo extends WelcomePageBehavior {
    public static final long DURATION = 10000L;
    public static final int INIT_TIME = 1;
    public static final int FINAL_TIME = 2;
    public static final int Y = 1;
    public static final int X = 0;
    public static final int LENGTH_LOCATION_ARRAY = 2;
    private ObjectAnimator alphaAnimator;
    private ObjectAnimator objectAnimatorY;
    private ObjectAnimator objectAnimatorX;
    private ObjectAnimator objectAnimatorScaleX;
    private ObjectAnimator objectAnimatorScaleY;

    @Override
    protected void onCreate(WelcomeCoordinatorLayout coordinator) {
        configureTranslation();
        configureScale();
    }

    private void configureTranslation() {
        final View targetView = getTargetView();
        final View shadowView = getTargetView().findViewById(R.id.star_shadow);
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
        alphaAnimator = ObjectAnimator.ofFloat(shadowView, View.ALPHA, 0, 0.4f);
        alphaAnimator.setInterpolator(new LinearInterpolator());
        alphaAnimator.setDuration(DURATION);
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
    protected void onPlaytimeChange(WelcomeCoordinatorLayout coordinator,
                                    float newPlaytime,
                                    float newScrollPosition) {
        if (newPlaytime <= INIT_TIME) {
            setCurrentTimeInAllAnimators(0);
        } else if (newPlaytime > INIT_TIME
                && newPlaytime <= FINAL_TIME) {
            long playTime = (long) ((newPlaytime - INIT_TIME) * DURATION);
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
        alphaAnimator.setCurrentPlayTime(playTime);
    }
}
