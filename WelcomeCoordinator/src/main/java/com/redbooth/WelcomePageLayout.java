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
package com.redbooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.redbooth.welcomecoordinator.R;

import java.util.List;

public class WelcomePageLayout extends RelativeLayout implements WelcomePageView {
    public WelcomePageLayout(Context context) {
        super(context);
    }

    public WelcomePageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WelcomePageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WelcomePageLayout(Context context, AttributeSet attrs,
                             int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams params) {
        return params instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams params) {
        return new LayoutParams(params);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public List<WelcomePageBehavior> getBehaviors(WelcomeCoordinatorLayout coordinatorLayout) {
        return WelcomeBehaviorsExtract.getWelcomePageBehaviors(this, coordinatorLayout);
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams implements WelcomeLayoutParams {
        private WelcomeBehaviorLayoutParams welcomeBehaviorLayoutParams = new WelcomeBehaviorLayoutParams();

        @Override
        public WelcomePageBehavior getBehavior() {
            return welcomeBehaviorLayoutParams.behavior;
        }

        @Override
        public int getDestinyViewId() {
            return welcomeBehaviorLayoutParams.destinyViewId;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            welcomeBehaviorLayoutParams.extractAttributes(context, attrs);
        }

        @SuppressWarnings("unused")
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }
    }
}