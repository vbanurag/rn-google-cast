package com.reactlibrary.api;

import androidx.annotation.VisibleForTesting;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class RNCastContext extends ReactContextBaseJavaModule implements LifecycleEventListener {
    @VisibleForTesting
    public static final String REACT_CLASS = "RNCastContext";


    private RNSessionManagerListener mSessionManagerListener;

    public RNCastContext(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(this);
        // TODO: add setupCastListener function
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
        return REACT_CLASS;
    }
}
