package com.reactlibrary.api;

import androidx.annotation.VisibleForTesting;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class RNCCastContext extends ReactContextBaseJavaModule implements LifecycleEventListener {
    @VisibleForTesting
    public static final String REACT_CLASS = "RNCastContext";

    private RNCSessionManagerListener mSessionManagerListener;


    public RNCCastContext(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
        // setupCastListener();
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }

    @Override
    public String getName() {
        return null;
    }
}
