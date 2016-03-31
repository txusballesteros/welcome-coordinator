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

import com.redbooth.WelcomeCoordinatorLayout;
import com.redbooth.WelcomePageBehavior;

@SuppressWarnings("unused")
public class AnimationFillWithColor extends WelcomePageBehavior {
    public static final long DURATION = 10000L;
    public static final int INIT_PLAY_TIME = 2;
    private ObjectAnimator objectAnimatorY;

    @Override
    protected void onCreate(WelcomeCoordinatorLayout coordinator) {
        configureTranslations();
    }

    protected void configureTranslations() {
        objectAnimatorY = ObjectAnimator.ofFloat(getTargetView(), View.TRANSLATION_Y, getTargetView().getHeight(), -getTargetView().getWidth()/2 - getPage().getPaddingTop());
        objectAnimatorY.setDuration(DURATION);
    }

    @Override
    protected void onPlaytimeChange(WelcomeCoordinatorLayout coordinator,
                                    float currentPlaytime,
                                    float newScrollPosition) {
        if (currentPlaytime <= INIT_PLAY_TIME) {
            objectAnimatorY.setCurrentPlayTime(0);
        } else {
            long playTime = (long) ((currentPlaytime - INIT_PLAY_TIME) * DURATION);
            objectAnimatorY.setCurrentPlayTime(playTime);
        }
    }
}
