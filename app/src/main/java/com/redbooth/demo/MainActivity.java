package com.redbooth.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
}
