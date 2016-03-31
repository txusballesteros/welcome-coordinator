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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.redbooth.welcomecoordinator.R;

import java.util.ArrayList;
import java.util.List;

public class WelcomeCoordinatorLayout extends HorizontalScrollView {
    public static final boolean ANIMATED  = true;
    public static final boolean INANIMATED  = false;
    public static final int WITHOUT_MARGIN = 0;
    public static final int RADIUS = 12;
    public static final int RADIUS_MARGIN = 30;
    public static final int DEF_INDICATOR_UNSELECTED_COLOR = Color.WHITE;
    public static final int DEF_INDICATOR_SELECTED_COLOR = Color.BLACK;
    private WelcomeCoordinatorTouchController touchController;
    private WelcomeCoordinatorPageInflater pageInflater;
    private FrameLayout mainContentView;
    private List<WelcomePageBehavior> behaviors = new ArrayList<>();
    private OnPageScrollListener onPageScrollListener;
    private int pageSelected = 0;
    private int indicatorColorUnselected = DEF_INDICATOR_UNSELECTED_COLOR;
    private int indicatorColorSelected = DEF_INDICATOR_SELECTED_COLOR;
    private Paint indicatorPaintUnselected;
    private Paint indicatorPaintSelected;
    private boolean showIndicators = true;
    private boolean scrollingEnabled = true;

    public WelcomeCoordinatorLayout(Context context) {
        super(context);
        initializeView(context, null);
    }

    public WelcomeCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    public WelcomeCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context, attrs);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return true;
    }

    public void showIndicators(boolean show) {
        this.showIndicators = show;
    }

    public void setScrollingEnabled(boolean enabled) {
        this.scrollingEnabled = enabled;
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WelcomeCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView(context, attrs);
    }

    public void addPage(@LayoutRes int... layoutResourceIds) {
        for (int i = layoutResourceIds.length - 1; i >= 0; i--) {
            int layoutResourceId = layoutResourceIds[i];
            final View pageView = pageInflater.inflate(layoutResourceId);
            final List<WelcomePageBehavior> pageBehaviors = extractPageBehaviors(pageView);
            if (!pageBehaviors.isEmpty()) {
                this.behaviors.addAll(pageBehaviors);
            }
            mainContentView.addView(pageView);
        }
        if (onPageScrollListener != null) {
            onPageScrollListener.onPageSelected(this, 0);
        }
        requestLayout();
    }

    private List<WelcomePageBehavior> extractPageBehaviors(View view) {
        List<WelcomePageBehavior> behaviors = new ArrayList<>();
        if (view instanceof WelcomePageLayout) {
            final WelcomePageLayout pageLayout = (WelcomePageLayout)view;
            final List<WelcomePageBehavior> pageBehaviors = pageLayout.getBehaviors(this);
            if (!pageBehaviors.isEmpty()) {
                behaviors.addAll(pageBehaviors);
            }

        }
        return behaviors;
    }

    public int getNumOfPages() {
        int result = 0;
        if (mainContentView != null) {
            result = mainContentView.getChildCount();
        }
        return result;
    }

    private void initializeView(Context context, AttributeSet attrs) {
        this.setHorizontalScrollBarEnabled(false);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        touchController = new WelcomeCoordinatorTouchController(this);
        pageInflater = new WelcomeCoordinatorPageInflater(this);
        extractAttributes(context, attrs);
        buildMainContentView();
        attachMainContentView();
        configureIndicatorColors();
    }

    private void extractAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes
                = context.obtainStyledAttributes(attrs, R.styleable.WelcomeCoordinatorLayout);
        indicatorColorUnselected = attributes
                .getColor(R.styleable.WelcomeCoordinatorLayout_indicatorUnselected, DEF_INDICATOR_UNSELECTED_COLOR);
        indicatorColorSelected = attributes
                .getColor(R.styleable.WelcomeCoordinatorLayout_indicatorSelected, DEF_INDICATOR_SELECTED_COLOR);
        showIndicators = attributes
                .getBoolean(R.styleable.WelcomeCoordinatorLayout_showIndicators, showIndicators);
        scrollingEnabled = attributes
                .getBoolean(R.styleable.WelcomeCoordinatorLayout_scrollingEnabled, scrollingEnabled);
        attributes.recycle();
    }

    private void buildMainContentView() {
        mainContentView = new FrameLayout(this.getContext());
        mainContentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));
        mainContentView.setClipToPadding(false);
        mainContentView.setClipChildren(false);
    }

    private void attachMainContentView() {
        removeAllViews();
        setClipChildren(false);
        addView(mainContentView);
    }

    private void configureIndicatorColors() {
        indicatorPaintUnselected = new Paint();
        indicatorPaintUnselected.setColor(indicatorColorUnselected);
        indicatorPaintSelected = new Paint();
        indicatorPaintSelected.setColor(indicatorColorSelected);
    }

    public void setIndicatorColorSelected(int indicatorColorSelected) {
        this.indicatorColorSelected = indicatorColorSelected;
        indicatorPaintSelected.setColor(indicatorColorSelected);
    }

    public void setIndicatorColorUnselected(int indicatorColorUnselected) {
        this.indicatorColorUnselected = indicatorColorUnselected;
        indicatorPaintUnselected.setColor(indicatorColorUnselected);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int index = 0; index < getNumOfPages(); index++) {
            ViewGroup childAt = (ViewGroup) mainContentView.getChildAt(index);
            configurePageLayout(childAt, index);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void configurePageLayout(ViewGroup pageView, int position) {
        int coordinatorWidth = getMeasuredWidth();
        int reversePosition = getNumOfPages() - 1 - position;
        int pageMarginLeft = (coordinatorWidth * reversePosition);
        int originalHeight = pageView.getLayoutParams().height;
        LayoutParams layoutParams = new LayoutParams(coordinatorWidth, originalHeight);
        layoutParams.setMargins(pageMarginLeft, WITHOUT_MARGIN, WITHOUT_MARGIN, WITHOUT_MARGIN);
        pageView.setLayoutParams(layoutParams);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (showIndicators) {
            drawIndicator(canvas);
        }
    }

    private void drawIndicator(Canvas canvas) {
        int centerX = (getWidth() - RADIUS)/2 + RADIUS/2;
        int indicatorWidth = RADIUS * 2;
        int indicatorAndMargin = indicatorWidth + RADIUS_MARGIN;
        int leftIndicators = centerX - ((getNumOfPages()-1) * indicatorAndMargin) / 2 ;
        int positionY = getHeight() - RADIUS - RADIUS_MARGIN;
        for (int i = 0; i < getNumOfPages(); i++) {
            int x = leftIndicators + indicatorAndMargin * i + getScrollX();
            canvas.drawCircle(x, positionY, RADIUS, indicatorPaintUnselected);
        }
        float width = (float) getWidth();
        float scrollProgress = getScrollX() / width;
        float selectedXPosition = leftIndicators + getScrollX() + scrollProgress * indicatorAndMargin;
        canvas.drawCircle(selectedXPosition, positionY, RADIUS, indicatorPaintSelected);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchEventCaptured = false;
        if (scrollingEnabled) {
            touchEventCaptured = touchController.onTouchEvent(event);
            if (!touchEventCaptured) {
                touchEventCaptured = super.onTouchEvent(event);
            }
        }
        return touchEventCaptured;
    }

    public void notifyProgressScroll(float progress, float scroll) {
        for (WelcomePageBehavior welcomePageBehavior : behaviors) {
            welcomePageBehavior.onPlaytimeChange(this, progress, scroll);
        }
    }

    public void setOnPageScrollListener(OnPageScrollListener onPageScrollListener) {
        this.onPageScrollListener = onPageScrollListener;
        touchController.setOnPageScrollListener(new WelcomeCoordinatorTouchController.OnPageScrollListener() {
            @Override
            public void onScrollPage(float progress) {
                int numOfPages = (getNumOfPages() - 1);
                int maximumScroll = getMeasuredWidth() * numOfPages;
                WelcomeCoordinatorLayout.this.onPageScrollListener.onScrollPage(WelcomeCoordinatorLayout.this, progress, maximumScroll);
            }

            @Override
            public void onPageSelected(int pageSelected) {
                WelcomeCoordinatorLayout.this.pageSelected = pageSelected;
                WelcomeCoordinatorLayout.this.onPageScrollListener.onPageSelected(WelcomeCoordinatorLayout.this, pageSelected);
            }
        });
    }

    public int getPageSelected() {
        return pageSelected;
    }

    public void setCurrentPage(int newCurrentPage, boolean animated) {
        pageSelected = Math.max(0, Math.min(getNumOfPages() -1, newCurrentPage));
        touchController.scrollToPage(pageSelected, animated);
    }

    public interface OnPageScrollListener {
        void onScrollPage(View v, float progress, float maximum);
        void onPageSelected(View v, int pageSelected);
    }
}
