package com.redbooth;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

class WelcomeCoordinatorTouchController {
    public static final int MAX_VELOCITY = 300;
    public static final int CURRENT_VELOCITY = 1000;
    public static final long SMOOTH_SCROLL_DURATION = 600L;
    public static final String PROPERTY_SCROLL_X = "scrollX";
    private final WelcomeCoordinatorLayout view;
    private float iniEventX;
    private int currentScrollX;
    private int minScroll = 0;
    private int maxScroll = 0;
    private VelocityTracker velocityTracker;
    private ObjectAnimator smoothScrollAnimator;

    public WelcomeCoordinatorTouchController(WelcomeCoordinatorLayout view) {
        this.view = view;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WelcomeCoordinatorLayout welcomeCoordinatorLayout
                        = WelcomeCoordinatorTouchController.this.view;
                int pagesSteps = welcomeCoordinatorLayout.getNumOfPages() - 1;
                maxScroll = pagesSteps * welcomeCoordinatorLayout.getWidth();
                ViewTreeObserver viewTreeObserver
                        = welcomeCoordinatorLayout.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    protected boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(event);
                currentScrollX = view.getScrollX();
                iniEventX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                setScrollX(scrollLimited(event.getX()));
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(CURRENT_VELOCITY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                evaluateMoveToPage();
                break;
        }
        return true;
    }

    private void evaluateMoveToPage() {
        float xVelocity = velocityTracker.getXVelocity();
        currentScrollX = view.getScrollX();
        moveToPagePosition(xVelocity);
    }

    private int scrollLimited(float scrollX) {
        int newScrollPosition = (int) (currentScrollX + iniEventX - scrollX);
        return Math.max(minScroll, Math.min(newScrollPosition, maxScroll));
    }

    private void setScrollX(int value) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.setScrollX(value);
        } else {
            view.scrollTo(value, view.getScrollY());
        }
    }

    private void moveToPagePosition(float xVelocity) {
        float width = view.getWidth();
        int currentPage = (int) Math.floor(currentScrollX / width);
        float percentageOfNewPageVisible = (currentScrollX % width) / width;
        if (xVelocity < -MAX_VELOCITY) {
            scrollToPage(currentPage + 1);
        } else if (xVelocity > MAX_VELOCITY) {
            scrollToPage(currentPage);
        } else if (percentageOfNewPageVisible > 0.5f) {
            scrollToPage(currentPage + 1);
        } else {
            scrollToPage(currentPage);
        }
    }

    private void scrollToPage(int newCurrentPage) {
        int width = view.getWidth();
        int limitedNumPage = Math.max(0, Math.min(view.getNumOfPages() -1, newCurrentPage));
        int scrollX = limitedNumPage * width;
        smoothScrollX(scrollX);
    }

    private void smoothScrollX(int scrollX) {
        cancelScrollAnimationIfNecessary();
        smoothScrollAnimator = ObjectAnimator.ofInt(view, PROPERTY_SCROLL_X, view.getScrollX(), scrollX);
        smoothScrollAnimator.setDuration(SMOOTH_SCROLL_DURATION);
        smoothScrollAnimator.setInterpolator(new DecelerateInterpolator());
        smoothScrollAnimator.start();
    }

    private void cancelScrollAnimationIfNecessary() {
        if ((smoothScrollAnimator != null)
                && (smoothScrollAnimator.isRunning())) {
            smoothScrollAnimator.cancel();
            view.clearAnimation();
            smoothScrollAnimator = null;
        }
    }
}
