[![cms-system-mobile](https://jitpack.io/v/needkirem/cms-system-mobile.svg)](https://jitpack.io/#needkirem/cms-system-mobile)

## Installation
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency:
```gradle
dependencies {
    implementation 'com.github.needkirem:cms-system-mobile:${latestVersion}'
    kapt 'com.github.needkirem:cms-system-mobile:${latestVersion}'
}
```