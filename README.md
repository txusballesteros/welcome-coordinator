WelcomeCoordinator
==================

![](assets/welcome_demo.gif)  ![](assets/onboarding_demo.gif)

Welcome Coordinator is a library for Android that will help you create really awesome welcome wizards for your apps, but that's not all. You can also use the library to create form wizards really nicely. Take a look to how you would integrate Welcome Coordinator into your app. 

## Latest Version

[![Download](https://api.bintray.com/packages/txusballesteros/maven/WelcomeCoordinator/images/download.svg) ](https://bintray.com/txusballesteros/maven/WelcomeCoordinator/_latestVersion) ![](https://img.shields.io/badge/platform-android-green.svg) ![](https://img.shields.io/badge/Min%20SDK-14-green.svg) ![](https://img.shields.io/badge/Licence-Apache%20v2-green.svg) [![Build Status](https://travis-ci.org/txusballesteros/welcome-coordinator.svg?branch=master)](https://travis-ci.org/txusballesteros/welcome-coordinator) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Welcome%20Coordinator-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3351)


## How to use

### 1.- Configuring your Project Dependencies

Add the library dependency to your build.gradle file.

```groovy
dependencies {
    ...
    compile 'com.redbooth:WelcomeCoordinator:1.0.1'
}
```

### 2.- Adding and Customizing the View

Add the view to your xml layout file.

```xml
<com.redbooth.WelcomeCoordinatorLayout
        android:id="@+id/coordinator"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

### 3.- Modeling the Pages

Modeling your welcome pages is really easy. You only need to create a simple layout resource. Let me an appointment about _WelcomePageLayout_, the behavior of the layout is the same of the RelativeLayout. 

```xml
<com.redbooth.WelcomePageLayout
    xmlns:android="http://schemas.android.com/apk/res/android">
    
    ...
    
</com.redbooth.WelcomePageLayout>
```

**WARNING** Don't forget to create a _WelcomePageLayout_ as the root element of your page.

### 4.- Adding the Pages to Coordinator

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    final WelcomeCoordinatorLayout coordinatorLayout 
            = (WelcomeCoordinatorLayout)findViewById(R.id.coordinator); 
    coordinatorLayout.addPage(R.layout.welcome_page_1,
                              ...,
                              R.layout.welcome_page_4);
}
```

### 5.- Building your own Behaviors

If you want to have behavior on your page views when the user navigate inside of your welcome, you can create you own behaviors.

```java
public class ParallaxTitleBehaviour extends WelcomePageBehavior {
    @Override
    protected void onCreate(WelcomeCoordinatorLayout coordinator) {
        ...
    }
    
    @Override
    protected void onPlaytimeChange(WelcomeCoordinatorLayout coordinator,
                                    float newPlaytime,
                                    float newScrollPosition) {
        ...
    }
}
```

### 6.- Setting Up Your Own Behaviors

```xml

<com.redbooth.WelcomePageLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    
    <TextView
        ...
        app:view_behavior=".ParallaxTitleBehaviour" />
            
</com.redbooth.WelcomePageLayout>
```

## Developers

* Francisco Sirvent ([@Narfss](https://github.com/narfss))
* Txus Ballesteros ([@Txusballesteros](https://github.com/txusballesteros))

## Motivation

We created this view as a little piece of the [Redbooth](https://redbooth.com/) app for [Android](https://play.google.com/store/apps/details?id=com.redbooth).


## License

Copyright Txus Ballesteros & Francisco Sirvent 2016

This file is part of some open source application.

Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
