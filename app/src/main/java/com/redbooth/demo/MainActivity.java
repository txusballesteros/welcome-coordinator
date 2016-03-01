package com.redbooth.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.RotateAnimation;

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
        final View androidSpin = findViewById(R.id.android_spin);
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float scrollProgress) {
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
