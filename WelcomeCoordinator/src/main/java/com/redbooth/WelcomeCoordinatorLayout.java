package com.redbooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class WelcomeCoordinatorLayout extends HorizontalScrollView {
    public static final int WITHOUT_MARGIN = 0;
    private WelcomeCoordinatorTouchController touchController;
    private WelcomeCoordinatorPageInflater pageInflater;
    private FrameLayout mainContentView;

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

    public void addPage(@LayoutRes int layoutResourceId) {
        final View pageView = pageInflater.inflate(layoutResourceId);
        mainContentView.addView(pageView);
        requestLayout();
    }

    protected int getNumOfPages() {
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
    }

    private void attachMainContentView() {
        removeAllViews();
        addView(mainContentView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int index = 0; index < getNumOfPages(); index++) {
            configurePageLayout(mainContentView.getChildAt(index), index);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void configurePageLayout(View pageView, int position) {
        int coordinatorWidth = getMeasuredWidth();
        int pageWidth = (coordinatorWidth * (getNumOfPages() - position));
        int pageMarginLeft = (coordinatorWidth * position);
        int originalHeight = pageView.getLayoutParams().height;
        FrameLayout.LayoutParams layoutParams = new FrameLayout
                .LayoutParams(pageWidth, originalHeight);
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
        Log.d("PROGRESS", "page progress: " + progress);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        private void extractAttributes(Context context, AttributeSet attrs) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs,
                    R.styleable.WelcomeCoordinatorLayout_LayoutParams);
            if (attributes.hasValue(R.styleable.WelcomePageLayout_LayoutParams_view_behavior)) {
                parseViewBehavior(context, attributes
                        .getString(R.styleable.WelcomePageLayout_LayoutParams_view_behavior));
            }
            attributes.recycle();
        }

        private void parseViewBehavior(Context context, String behaviorClassName) {
            if (TextUtils.isEmpty(behaviorClassName)) {
                return;
            }
            final String fullName;
            if (behaviorClassName.startsWith(".")) {
                fullName = context.getPackageName() + behaviorClassName;
            } else {
                fullName = behaviorClassName;
            }

            try {
                Class behaviorClazz = Class.forName(fullName, true, context.getClassLoader());

            } catch (Exception e) {
                throw new RuntimeException("Could not inflate View Behavior subclass " + fullName, e);
            }
        }
    }
}
