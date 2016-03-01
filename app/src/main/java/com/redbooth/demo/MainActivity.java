package com.redbooth.demo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.redbooth.WelcomeCoordinatorLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private boolean animationReady = false;
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
        final ValueAnimator backgroundAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                        0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff00ff);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int)animation.getAnimatedValue());
            }
        });
        final View androidSpin = findViewById(R.id.android_spin);
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long)maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long)progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                if (pageSelected == 3) {
                    RotateAnimation animation = new RotateAnimation(0, 360, androidSpin.getWidth()/2, androidSpin.getHeight()/2);
                    animation.setDuration(1000);
                    androidSpin.startAnimation(animation);
                }
            }
        });
    }
}
