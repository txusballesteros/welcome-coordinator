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

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.lang.reflect.Constructor;

import com.redbooth.welcomecoordinator.R;

public class WelcomeBehaviorLayoutParams {
    public final static int NO_DESTINY_VIEW = -1;
    public int destinyViewId = NO_DESTINY_VIEW;
    public WelcomePageBehavior behavior;

    public void extractAttributes(Context context, AttributeSet attrs) {
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
}
