package com.redbooth;

import android.view.MotionEvent;

class WelcomeCoordinatorTouchController {
    private final WelcomeCoordinatorLayout view;

    public WelcomeCoordinatorTouchController(WelcomeCoordinatorLayout view) {
        this.view = view;
    }

    protected boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
