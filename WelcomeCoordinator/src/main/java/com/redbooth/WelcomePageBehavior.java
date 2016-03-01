package com.redbooth;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public abstract class WelcomePageBehavior {
    static final Class<?>[] CONSTRUCTOR_PARAMS = new Class<?>[] {
            Context.class,
            AttributeSet.class
    };

    private final static int NO_DESTINY_VIEW = -1;
    public static final View NON_DESTINY = null;
    protected WelcomeCoordinatorLayout coordinatorLayout;
    private View targetView;
    private View destinyView;
    private Context context;
    private AttributeSet attributes;

    protected View getTargetView() {
        return targetView;
    }

    protected View getDestinyView() {
        if (targetView != null && destinyView == null && coordinatorLayout != null) {
            int destinyViewId = ((WelcomePageLayout.LayoutParams)targetView.getLayoutParams()).getDestinyViewId();
            if (destinyViewId != NO_DESTINY_VIEW) {
                destinyView = coordinatorLayout.findViewById(destinyViewId);
            }
        }
        return destinyView;
    }

    void setCoordinator(WelcomeCoordinatorLayout coordinator) {
        this.coordinatorLayout = coordinator;
        this.coordinatorLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onConfigure();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            coordinatorLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            coordinatorLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }

    void setTarget(View target) {
        this.targetView = target;
    }

    public WelcomePageBehavior(@NonNull Context context, @NonNull AttributeSet attributes) {
        this.context = context;
        this.attributes = attributes;
    }

    public WelcomePageBehavior(@NonNull WelcomeCoordinatorLayout coordinatorLayout,
                               @NonNull View targetView, @Nullable View destinyView) {
        this.coordinatorLayout = coordinatorLayout;
        this.targetView = targetView;
        this.destinyView = destinyView;
    }

    protected abstract void onConfigure();

    public abstract void setCurrentPlayTime(float progress);
}
