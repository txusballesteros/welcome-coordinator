WelcomeCoordinator
==================

![](assets/welcome_demo.gif) ![](assets/onboarding_demo.gif)

Welcome Coordinator is a library for Android it will helps you to create really awesome welcome wizards for your Apps, but this is not all, also you can use the library to create form wizards really nice. Take a look to how you would integrate Welcome Coordinator into your App. 

## Latest Version

![](https://img.shields.io/badge/platform-android-green.svg) ![](https://img.shields.io/badge/Min%20SDK-14-green.svg) ![](https://img.shields.io/badge/Licence-Apache%20v2-green.svg)


## How to use

### 1.- Configuring your project dependencies

Add the library dependency to your build.gradle file.

```groovy
dependencies {
    ...
    compile 'com.redbooth:WelcomeCoordinator:1.0.0'
}
```

### 2.- Adding and Customizing the View

Add the view to your xml layout file.

```xml
<com.redbooth.WelcomeCoordinator
        android:id="@+id/coordinator"
        android:layout_width="match_parent"
        android:layout_height="match_parent' />
```

### 3.- Adding and Customizing the View

## Developers

* Francisco Sirvent ([@Narfss](https://github.com/narfss))
* Txus Ballesteros ([@Txusballesteros](https://github.com/txusballesteros))

## Motivation

We created this view as a little piece of the [Redbooth](https://redbooth.com/) App for [Android](https://play.google.com/store/apps/details?id=com.redbooth).


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