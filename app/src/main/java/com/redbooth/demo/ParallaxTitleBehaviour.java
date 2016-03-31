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
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.redbooth.WelcomeCoordinatorLayout;
import com.redbooth.WelcomePageBehavior;

@SuppressWarnings("unused")
public class ParallaxTitleBehaviour extends WelcomePageBehavior {
    private final static int PARALLAX_FACTOR = 2;
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
