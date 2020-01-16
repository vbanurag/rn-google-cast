# React Native Google Cast

A React Native library to use Google Cast, based on [react-native-google-cast](https://github.com/react-native-google-cast/react-native-google-cast)

## Getting started

```
$ npm install rn-google-cast --save
```

### Installation

```
$ react-native link rn-google-cast
```

Notes:

- RN 0.60+ can use Autolinking
- tested only with RN 0.60+

### Setup

<details>
    <summary>Android</summary>

- Make sure the device you're using (also applies to emulators) has Google Play Services installed.

- Add this to your `android/build.gradle`:
  ```java
  ...
  ext{
  ...
   castFrameworkVersion = "18.0.0"
  }
  ...
  ```
- Add this dependencies to your `android/app/build.gradle`:

  ```java
  dependencies{
    ...
    implementation "com.google.android.gms:play-services-cast-framework:${rootProject.ext.castFrameworkVersion}"
  }
  ```

- Add to `androidmanifest.xml`:

  ```xml
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.yourappid">

    <!-- Add this permissions -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application ...>
    ...

    <!-- Add these lines after  <activity android:name="com.facebook.react.devsupport.DevSettingsActivity" /> -->
      <activity android:name="com.reactlibrary.components.RNExpandedCastControlsActivity" />

      <meta-data
        android:name="com.google.android.gms.cast.framework.OPTIONS_PROVIDER_CLASS_NAME"
        android:value="com.reactlibrary.RnGoogleCastOptionsProvider" />
    </application>

    </manifest>
  ```

- On your `MainActivity.java`:

  ```java
    // default imports
    ...

    // add these imports
    import android.util.Log;
    import android.view.View;
    import android.view.WindowManager;
    import android.os.Bundle;
    import android.os.Build;
    import com.google.android.gms.cast.framework.CastContext;
    import androidx.annotation.Nullable;

    public class MainActivity extends ReactActivity {
    // getMainComponentName function
    ...
        // Add this !
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // lazy load Google Cast context
            try{
                CastContext.getSharedInstance(this);
            } catch(Exception e){
                Log.e("ERROR", e.toString());
            }
        }
    }
  ```

- Done!

</details>

<details>
    <summary>iOS</summary>

- in your `AppDelegate.m` add:
  Google Cast SDK import

  ```obj-c
  #import <GoogleCast/GoogleCast.h>
  ```

  Aditional setup

  ```obj-c
  ...
  - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
  {
      ...
      // add this

      GCKDiscoveryCriteria *criteria = [[GCKDiscoveryCriteria alloc] initWithApplicationID:kGCKDefaultMediaReceiverApplicationID];
      GCKCastOptions* options = [[GCKCastOptions alloc] initWithDiscoveryCriteria:criteria];
      options.physicalVolumeButtonsWillControlDeviceVolume = YES; // add this row
      [GCKCastContext setSharedInstanceWithOptions:options];

      [GCKCastContext sharedInstance].useDefaultExpandedMediaControls = YES;
      return YES;
  }
  ```

- Done!

</details>

## Usage

See [example](https://github.com/b3coded/rn-google-cast/tree/master/example) application
