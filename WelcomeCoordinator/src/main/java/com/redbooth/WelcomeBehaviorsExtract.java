package com.redbooth;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WelcomeBehaviorsExtract {
    @NonNull
    public static List<WelcomePageBehavior> getWelcomePageBehaviors(WelcomePageView page, WelcomeCoordinatorLayout coordinatorLayout) {
        List<WelcomePageBehavior> result = new ArrayList<>();
        for (int index = 0; index < ((ViewGroup)page).getChildCount(); index++) {
            final View view = ((ViewGroup)page).getChildAt(index);
            if (view.getLayoutParams() instanceof WelcomeLayoutParams) {
                final WelcomeLayoutParams params = (WelcomeLayoutParams)view.getLayoutParams();
                final WelcomePageBehavior behavior = params.getBehavior();
                if (behavior != null) {
                    behavior.setCoordinator(coordinatorLayout);
                    behavior.setTarget(view);
                    behavior.setPage(page);
                    result.add(behavior);
                }
            }
        }
        return result;
    }
}
