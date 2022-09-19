OmniWallet
=======





Adding dependencies
--------

First, add plugin to your project's root build.gradle file:
```groovy
buildscript {
    ...
    dependencies {
        ...
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.5.2"
        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.40.5'
    }
}
```

Then, apply the Gradle plugin and add these dependencies in your app/build.gradle file:
```groovy
...
plugins {
  id 'kotlin-kapt'
  id 'dagger.hilt.android.plugin'
}

android {
    ...
}

dependencies {
    implementation 'com.github.IntelNet-JSC:Omni-Wallet:1.0.1'

    //Dagger Hilt
    implementation 'com.google.dagger:hilt-android:2.40.5'
    kapt 'com.google.dagger:hilt-compiler:2.40.5'
    kapt 'androidx.hilt:hilt-compiler:1.0.0'
}
```

OmniWallet requires at minimum Java 8 and API 23.


Usage
--------

Hilt application class:
```groovy
@HiltAndroidApp
class ExampleApplication : Application() { ... }
```

The OmniWallet configuration is created using the builder pattern.

    OmniWallet
        .with(this)
        .disableOpenNetwork()   // disable click open network
        .disableAddToken()  // disable item add token
        .ethTestnetIsDefault() // set network default is ETH Testnet
        .start()
        
