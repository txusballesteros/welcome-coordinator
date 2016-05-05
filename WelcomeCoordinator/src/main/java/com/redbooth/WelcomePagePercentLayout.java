/*
 * Copyright Francisco M Sirvent 2016 (@narfss)
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
 * Contact: Francico M Sirvent <narfss@gmail.com>
 */
package com.redbooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.percent.PercentRelativeLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.redbooth.welcomecoordinator.R;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class WelcomePagePercentLayout extends PercentRelativeLayout implements WelcomePageView {
    public WelcomePagePercentLayout(Context context) {
        super(context);
    }

    public WelcomePagePercentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WelcomePagePercentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //@SuppressWarnings("unused")
    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    //public WelcomePageViewLayout(Context context, AttributeSet attrs,
    //                         int defStyleAttr, int defStyleRes) {
    //    super(context, attrs, defStyleAttr, defStyleRes);
    //}

    @Override
    protected WelcomeLayoutParams generateDefaultLayoutParams() {
        return new WelcomeLayoutParams(WelcomeLayoutParams.WRAP_CONTENT, WelcomeLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams params) {
        return params instanceof WelcomeLayoutParams;
    }

    @Override
    protected WelcomeLayoutParams generateLayoutParams(ViewGroup.LayoutParams params) {
        return new WelcomeLayoutParams(params);
    }

    @Override
    public WelcomeLayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WelcomeLayoutParams(getContext(), attrs);
    }

    @Override
    public List<WelcomePageBehavior> getBehaviors(WelcomeCoordinatorLayout coordinatorLayout) {
        List<WelcomePageBehavior> result = new ArrayList<>();
        for (int index = 0; index < getChildCount(); index++) {
            final View view = getChildAt(index);
            if (view.getLayoutParams() instanceof WelcomeLayoutParams) {
                final WelcomeLayoutParams params = (WelcomeLayoutParams)view.getLayoutParams();
                final WelcomePageBehavior behavior = params.getBehavior();
                if (behavior != null) {
                    behavior.setCoordinator(coordinatorLayout);
                    behavior.setTarget(view);
                    behavior.setPage(this);
                    result.add(behavior);
                }
            }
        }
        return result;
    }

    public static class WelcomeLayoutParams extends PercentRelativeLayout.LayoutParams implements WelcomePageView.WelcomeLayoutParams {
        public final static int NO_DESTINY_VIEW = -1;
        private int destinyViewId = NO_DESTINY_VIEW;
        private WelcomePageBehavior behavior;

        public WelcomePageBehavior getBehavior() {
            return behavior;
        }

        @Override
        public int getDestinyViewId() {
            return destinyViewId;
        }

        public WelcomeLayoutParams(int width, int height) {
            super(width, height);
        }

        public WelcomeLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public WelcomeLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public WelcomeLayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            extractAttributes(context, attrs);
        }

        private void extractAttributes(Context context, AttributeSet attrs) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs,
                    R.styleable.WelcomePageLayout_LayoutParams);
            if (attributes.hasValue(R.styleable.WelcomePageLayout_LayoutParams_view_behavior)) {
                behavior = parseBehavior(context, attributes
                        .getString(R.styleable.WelcomePageLayout_LayoutParams_view_behavior));
            }
            if (attributes.hasValue(R.styleable.WelcomePageLayout_LayoutParams_destiny)) {
                destinyViewId = attributes
                        .getResourceId(R.styleable.WelcomePageLayout_LayoutParams_destiny, NO_DESTINY_VIEW);
            }
            attributes.recycle();
        }

        private WelcomePageBehavior parseBehavior(Context context, String name) {
            WelcomePageBehavior result = null;
            if (!TextUtils.isEmpty(name)) {
                final String fullName;
                if (name.startsWith(".")) {
                    fullName = context.getPackageName() + name;
                } else {
                    fullName = name;
                }
                try {
                    Class<WelcomePageBehavior> behaviorClazz
                            = (Class<WelcomePageBehavior>) Class.forName(fullName);
                    final Constructor<WelcomePageBehavior> mainConstructor
                            = behaviorClazz.getConstructor();
                    mainConstructor.setAccessible(true);
                    result = mainConstructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not inflate Behavior subclass " + fullName, e);
                }
            }
            return result;
        }

        @SuppressWarnings("unused")
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public WelcomeLayoutParams(PercentRelativeLayout.LayoutParams source) {
            super(source);
        }
    }
}