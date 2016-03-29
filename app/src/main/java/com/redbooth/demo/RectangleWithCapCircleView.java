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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RectangleWithCapCircleView extends View {
    private Paint paint;
    private RectF rectCap;
    private RectF rectBottom;

    public RectangleWithCapCircleView(Context context) {
        super(context);
    }

    public RectangleWithCapCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleWithCapCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        rectCap = null;
        super.onMeasure(widthMeasureSpec, (int) Math.ceil(heightMeasureSpec + widthMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectCap == null) {
            rectCap = new RectF(0f, 0f, getWidth(), getWidth() + 1);
            rectBottom = new RectF(0.0f, (getWidth()/2) - 1, getWidth(), getHeight());
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
        }
        canvas.drawRect(rectBottom, paint);
        canvas.drawArc(rectCap, 0f, -180f, true, paint);
    }
}
