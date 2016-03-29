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
package com.redbooth.demo.animators;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.redbooth.demo.R;

public class RocketAvatarsAnimator {
    private AnimatorSet animator;
    private Animator rocketFlameAnimator;
    private final View rootView;

    public RocketAvatarsAnimator(View rootView) {
        this.rootView = rootView;
        initializeAnimator();
    }

    private void initializeAnimator() {
        final View rocketFlame = rootView.findViewById(R.id.rocket_flame);
        final View avatar1 = rootView.findViewById(R.id.avatar1);
        final View avatar2 = rootView.findViewById(R.id.avatar2);
        final View avatar3 = rootView.findViewById(R.id.avatar3);
        final View avatar4 = rootView.findViewById(R.id.avatar4);
        Animator avatar1Animator = getAnimator(avatar1);
        Animator avatar2Animator = getAnimator(avatar2);
        Animator avatar3Animator = getAnimator(avatar3);
        Animator avatar4Animator = getAnimator(avatar4);
        rocketFlameAnimator = getFlameAnimator(rocketFlame);
        animator = new AnimatorSet();
        animator.setStartDelay(500);
        animator.play(avatar3Animator).after(avatar4Animator);
        animator.play(avatar2Animator).after(avatar3Animator);
        animator.play(avatar1Animator).after(avatar2Animator);
    }

    private Animator getAnimator(View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 1f);
        animator.playTogether(scaleXAnimator, scaleYAnimator);
        return animator;
    }

    private Animator getFlameAnimator(View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(targetView, View.ALPHA, 0f, 1f, 0.5f, 1f, 0.8f, 1f);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 0.8f, 1f, 0.9f, 1f, 0.7f, 1f);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
        animator.playTogether(alphaAnimator, scaleAnimator);
        return animator;
    }

    public void play() {
        animator.start();
        rocketFlameAnimator.start();
    }
}
