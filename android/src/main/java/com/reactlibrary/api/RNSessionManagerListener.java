package com.reactlibrary.api;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

public class RNSessionManagerListener implements SessionManagerListener<CastSession> {

    @VisibleForTesting
    private static final String REACT_CLASS = "RNSessionManagerListener";


    private CastSession castSession;

    @Override
    public void onSessionStarting(CastSession castSession) {

    }

    @Override
    public void onSessionStarted(CastSession castSession, String s) {

    }

    @Override
    public void onSessionStartFailed(CastSession castSession, int i) {

    }

    @Override
    public void onSessionEnding(CastSession castSession) {

    }

    @Override
    public void onSessionEnded(CastSession castSession, int i) {

    }

    @Override
    public void onSessionResuming(CastSession castSession, String s) {

    }

    @Override
    public void onSessionResumed(CastSession castSession, boolean b) {

    }

    @Override
    public void onSessionResumeFailed(CastSession castSession, int i) {

    }

    @Override
    public void onSessionSuspended(CastSession castSession, int i) {

    }


    private void onConnected(final CastSession castSession) {
        this.castSession = castSession;

        //  remoteMediaClientListener = new RNGCRemoteMediaClientListener(module);
        //  castSession.getRemoteMediaClient().registerCallback(
        //  remoteMediaClientListener);
    }

    private void onDisconnected() {
        this.castSession = null;
    }
}
