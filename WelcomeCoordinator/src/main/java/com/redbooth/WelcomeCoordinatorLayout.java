package com.redbooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class WelcomeCoordinatorLayout extends HorizontalScrollView {
    public static final int WITHOUT_MARGIN = 0;
    private WelcomeCoordinatorTouchController touchController;
    private WelcomeCoordinatorPageInflater pageInflater;
    private FrameLayout mainContentView;
    private List<WelcomePageBehavior> behaviors = new ArrayList<>();
    private OnPageScrollListener onPageScrollListener;

    public WelcomeCoordinatorLayout(Context context) {
        super(context);
        initializeView();
    }

    public WelcomeCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public WelcomeCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WelcomeCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView();
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

    private void initializeView() {
        this.setHorizontalScrollBarEnabled(false);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        touchController = new WelcomeCoordinatorTouchController(this);
        pageInflater = new WelcomeCoordinatorPageInflater(this);
        buildMainContentView();
        attachMainContentView();
    }

    private void buildMainContentView() {
        mainContentView = new FrameLayout(this.getContext());
        mainContentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));
        mainContentView.setClipToPadding(false);
        mainContentView.setClipChildren(false);
    }

    private void attachMainContentView() {
        removeAllViews();
        setClipChildren(false);
        addView(mainContentView);
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
        FrameLayout.LayoutParams layoutParams = new FrameLayout
                .LayoutParams(coordinatorWidth, originalHeight);
        layoutParams.setMargins(pageMarginLeft, WITHOUT_MARGIN, WITHOUT_MARGIN, WITHOUT_MARGIN);
        pageView.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchEventCaptured = touchController.onTouchEvent(event);
        if (!touchEventCaptured) {
            touchEventCaptured = super.onTouchEvent(event);
        }
        return touchEventCaptured;
    }

    public void notifyProgressScroll(float progress) {
        for (WelcomePageBehavior welcomePageBehavior : behaviors) {
            welcomePageBehavior.onPlaytimeChange(this, progress);
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
                WelcomeCoordinatorLayout.this.onPageScrollListener.onPageSelected(WelcomeCoordinatorLayout.this, pageSelected);
            }
        });
    }

    public interface OnPageScrollListener {
        void onScrollPage(View v, float progress, float maximum);
        void onPageSelected(View v, int pageSelected);
    }
}
