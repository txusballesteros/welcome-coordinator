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
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.redbooth.demo.R;

public class ChatAvatarsAnimator {
    private AnimatorSet animator;
    private final View rootView;

    public ChatAvatarsAnimator(View rootView) {
        this.rootView = rootView;
        initializeAnimator();
    }

    private void initializeAnimator() {
        final View avatar1 = rootView.findViewById(R.id.avatar1_page2);
        final View card1 = rootView.findViewById(R.id.card1_page2);
        final View avatar2 = rootView.findViewById(R.id.avatar2_page2);
        final View card2 = rootView.findViewById(R.id.card2_page2);
        final View star = rootView.findViewById(R.id.star);
        Animator avatar1Animator = getScaleAnimator(avatar1);
        Animator card1Animator = getFlightFromLeft(card1);
        Animator avatar2Animator = getScaleAnimator(avatar2);
        Animator card2Animator = getFlightFromRight(card2);
        Animator starAnimator = getScaleAndVisibilityAnimator(star);
        animator = new AnimatorSet();
        animator.play(starAnimator).after(card2Animator);
        animator.play(card2Animator).after(avatar2Animator);
        animator.play(avatar2Animator).after(card1Animator);
        animator.play(card1Animator).after(avatar1Animator);
    }

    private AnimatorSet getScaleAnimator(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 1f);
        animator.playTogether(scaleXAnimator, scaleYAnimator);
        return animator;
    }

    private AnimatorSet getScaleAndVisibilityAnimator(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(300);
        animator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_X, 0f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(targetView, View.SCALE_Y, 0f, 1f);
        animator.playTogether(scaleXAnimator, scaleYAnimator);
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                targetView.setVisibility(View.VISIBLE);
            }
        });
        return animator;
    }

    private AnimatorSet getFlightFromRight(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(300);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_X, 0f);
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                targetView.setVisibility(View.VISIBLE);
            }
        });
        animator.play(translationXAnimator);
        return animator;
    }

    private AnimatorSet getFlightFromLeft(final View targetView) {
        AnimatorSet animator = new AnimatorSet();
        animator.setDuration(600);
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(targetView, View.TRANSLATION_X, 0f);
        animator.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                targetView.setVisibility(View.VISIBLE);
            }
        });
        animator.play(translationXAnimator);
        return animator;
    }

    public void play() {
        animator.start();
    }

    public abstract class AnimatorListener implements Animator.AnimatorListener {
        public abstract void onAnimationStart(Animator animation);
        public void onAnimationEnd(Animator animation) {}
        public void onAnimationCancel(Animator animation) {}
        public void onAnimationRepeat(Animator animation) {}
    }
}
