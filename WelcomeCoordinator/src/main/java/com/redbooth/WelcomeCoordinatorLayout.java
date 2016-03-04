package com.redbooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.Log;
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
    private int pageSelected = 0;
    private Paint paintUnselected;
    private Paint paintSelected;
    private Paint paintPreSelected;

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
        configureCounter();
    }

    private void configureCounter() {
        paintUnselected = new Paint();
        paintUnselected.setColor(Color.WHITE);
        paintSelected = new Paint();
        paintSelected.setColor(Color.BLACK);
        paintPreSelected = new Paint();
        paintPreSelected.setColor(Color.GRAY);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        int radius = 20;
        int margin = 30;
        int centerX = (getWidth() - radius)/2 + radius/2;
        int indicatorWidth = radius * 2;
        int indicatorAndMargin = indicatorWidth + margin;
        int leftIndicators = centerX - ((getNumOfPages()-1) * indicatorAndMargin) / 2 ;
        int positionY = getHeight() - radius - margin;

        //all the points
        for (int i = 0; i < getNumOfPages(); i++) {
            int x = leftIndicators + indicatorAndMargin * i + getScrollX();
            canvas.drawCircle(x, positionY, radius, paintUnselected);
        }

        //Previous page
        float preSelectedXPosition = leftIndicators + getScrollX() + getPageSelected() * indicatorAndMargin;
        //canvas.drawCircle(preSelectedXPosition, positionY, radius, paintPreSelected);

        //Selected page with movement
        float width = (float) getWidth();
        float scrollProgress = getScrollX() / width;
        float selectedXPosition = leftIndicators + getScrollX() + scrollProgress * indicatorAndMargin;
        //canvas.drawCircle(selectedXPosition, positionY, radius, paintSelected);

        int top = getHeight() - indicatorAndMargin;
        int bottom = top + radius*2;
        /*Rectangle
        if (preSelectedXPosition <= selectedXPosition) {
            canvas.drawRoundRect(preSelectedXPosition - radius, top, selectedXPosition + radius, bottom, radius, radius, paintPreSelected);
        } else {
            canvas.drawRoundRect(selectedXPosition - radius, top, preSelectedXPosition + radius, bottom, radius, radius, paintPreSelected);
        }
        */

        if (preSelectedXPosition <= selectedXPosition) {
            int halfWide = radius * 4 / 2;
            float f2 = selectedXPosition - preSelectedXPosition;
            Log.d("DIFERENCE", "diference:" + f2);
            float leftSide = Math.min(halfWide, f2);
            float rightSide = Math.min(0, preSelectedXPosition - radius - selectedXPosition + radius);
            canvas.drawRoundRect(selectedXPosition - radius, top, selectedXPosition + radius, bottom, radius, radius, paintPreSelected);
        } else {
            canvas.drawRoundRect(selectedXPosition - radius, top, selectedXPosition + radius, bottom, radius, radius, paintPreSelected);
        }

    }

    public int getPageSelected() {
        return pageSelected;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean touchEventCaptured = touchController.onTouchEvent(event);
        if (!touchEventCaptured) {
            touchEventCaptured = super.onTouchEvent(event);
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

    public interface OnPageScrollListener {
        void onScrollPage(View v, float progress, float maximum);
        void onPageSelected(View v, int pageSelected);
    }
}
