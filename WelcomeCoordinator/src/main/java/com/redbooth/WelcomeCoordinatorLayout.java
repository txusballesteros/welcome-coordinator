package com.redbooth;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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
        for (int layoutResourceId : layoutResourceIds) {
            final View pageView = pageInflater.inflate(layoutResourceId);
            extractBehaviors(pageView);
            mainContentView.addView(pageView);
        }
        requestLayout();
    }

    private void extractBehaviors(View view) {
        if (view instanceof WelcomePageLayout) {
            final WelcomePageLayout pageLayout = (WelcomePageLayout)view;
            for (int index = 0; index < pageLayout.getChildCount(); index++) {
                List<WelcomePageBehavior> behaviors = pageLayout.getBehaviors(this);
                if (!behaviors.isEmpty()) {
                    this.behaviors.addAll(behaviors);
                }
            }
        }
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

    public void addBehavior(WelcomePageBehavior welcomePageBehavior) {
        welcomePageBehavior.onConfigure();
        behaviors.add(welcomePageBehavior);
    }

    private void buildMainContentView() {
        mainContentView = new FrameLayout(this.getContext());
        mainContentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));
    }

    private void attachMainContentView() {
        removeAllViews();
        setClipChildren(false);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {
        int radius = 20;
        int margin = 10;
        int centerX = (getWidth() - radius)/2 + radius/2;
        int indicatorWidth = radius * 2;
        int indicatorAndMargin = indicatorWidth + margin;
        int leftIndicators = centerX - ((getNumOfPages()-1) * indicatorAndMargin) / 2 ;
        int positionY = getHeight() - radius - margin * 2;
        for (int i = 0; i < getNumOfPages(); i++) {
            int x = leftIndicators + indicatorAndMargin * i + getScrollX();
            canvas.drawCircle(x, positionY, radius, paintUnselected);
        }

        float preSelectedXPosition = leftIndicators + getScrollX() + getPageSelected() * indicatorAndMargin;
        //canvas.drawCircle(preSelectedXPosition, positionY, radius, paintPreSelected);

        float width = (float) getWidth();
        float scrollProgress = getScrollX() / width;
        float selectedXPosition = leftIndicators + getScrollX() + scrollProgress * indicatorAndMargin;
        //canvas.drawCircle(selectedXPosition, positionY, radius, paintSelected);

        int top = getHeight() - indicatorAndMargin - margin;
        int bottom = getHeight() - margin*2;
        if (preSelectedXPosition <= selectedXPosition) {
            canvas.drawRoundRect(preSelectedXPosition - radius, top, selectedXPosition + radius, bottom, radius, radius, paintPreSelected);
        } else {
            canvas.drawRoundRect(selectedXPosition - radius, top, preSelectedXPosition + radius, bottom, radius, radius, paintPreSelected);
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

    public void notifyProgressScroll(float progress) {
        for (WelcomePageBehavior welcomePageBehavior : behaviors) {
            welcomePageBehavior.setCurrentPlayTime(progress);
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
