package com.redbooth.welcomecoordinator;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class WelcomeCoordinatorPageInflater {
    private final WelcomeCoordinatorLayout view;
    private final LayoutInflater inflater;

    protected WelcomeCoordinatorPageInflater(@NonNull WelcomeCoordinatorLayout view) {
        this.view = view;
        this.inflater = LayoutInflater.from(view.getContext());
    }

    protected View inflate(@LayoutRes int layoutResourceId) {
        final ViewGroup pageView = (ViewGroup)inflater.inflate(layoutResourceId, view, false);
        return pageView;
    }
}
