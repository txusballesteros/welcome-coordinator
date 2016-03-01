package com.redbooth.demo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.redbooth.WelcomeCoordinatorLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.coordinator) WelcomeCoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializePages();
    }

    private void initializePages() {
        coordinatorLayout.addPage(R.layout.welcome_page_1,
                                  R.layout.welcome_page_2,
                                  R.layout.welcome_page_3,
                                  R.layout.welcome_page_4);
        
        coordinatorLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                coordinatorLayout.clearBehaviors();

                View androidView = findViewById(R.id.android_launch);
                AnimationLaunch animationLaunch = new AnimationLaunch(coordinatorLayout, androidView, AnimationLaunch.NON_DESTINY);
                coordinatorLayout.addBehavior(animationLaunch);

                View androidGoToPlaceView = findViewById(R.id.android_go_to_place);
                View androidDestinyView = findViewById(R.id.android_destiny);
                AnimationFlyTo animationFlyTo = new AnimationFlyTo(coordinatorLayout, androidGoToPlaceView, androidDestinyView);
                coordinatorLayout.addBehavior(animationFlyTo);

                coordinatorLayout.configureBehaviors();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    coordinatorLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    coordinatorLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
