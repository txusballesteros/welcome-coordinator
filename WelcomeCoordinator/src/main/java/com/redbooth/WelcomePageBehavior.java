package com.redbooth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class WelcomePageBehavior {
    public static final View NON_DESTINY = null;
    protected final WelcomeCoordinatorLayout coordinatorLayout;
    protected final View view;
    protected final View destinyView;

    public WelcomePageBehavior(@NonNull WelcomeCoordinatorLayout coordinatorLayout,
                               @NonNull View view, @Nullable View destinyView) {
        this.coordinatorLayout = coordinatorLayout;
        this.view = view;
        this.destinyView = destinyView;
    }

    public abstract void configure();

    public abstract void setCurrentPlayTime(float progress);
}
