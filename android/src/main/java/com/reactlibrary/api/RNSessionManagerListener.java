package com.reactlibrary.api;

import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.reactlibrary.RnGoogleCastModule;

public class RNSessionManagerListener implements SessionManagerListener<CastSession> {

    private CastSession castSession;
    private RnGoogleCastModule module;
    private RNRemoteMediaClientListener remoteMediaClientListener;

    @VisibleForTesting
    private static final String REACT_CLASS = "RNSessionManagerListener";

    public RNSessionManagerListener(RnGoogleCastModule module) {
        this.module = module;
    }


    @Override
    public void onSessionStarting(CastSession castSession) {
        //        onApplicationDisconnected();
        module.emitMessageToRN(RnGoogleCastModule.SESSION_STARTING, null);
    }

    @Override
    public void onSessionStarted(CastSession castSession, String s) {
        onApplicationConnected(castSession);
        module.emitMessageToRN(RnGoogleCastModule.SESSION_STARTED, null);
    }

    @Override
    public void onSessionStartFailed(CastSession castSession, int i) {
        onApplicationDisconnected();
        module.emitMessageToRN(RnGoogleCastModule.SESSION_START_FAILED, null);
    }

    @Override
    public void onSessionEnding(CastSession castSession) {
        module.emitMessageToRN(RnGoogleCastModule.SESSION_ENDING, null);
    }

    @Override
    public void onSessionEnded(CastSession castSession, int i) {
        onApplicationDisconnected();
        module.emitMessageToRN(RnGoogleCastModule.SESSION_ENDED, null);
    }

    @Override
    public void onSessionResuming(CastSession castSession, String s) {
        module.emitMessageToRN(RnGoogleCastModule.SESSION_RESUMING, null);
    }

    @Override
    public void onSessionResumed(CastSession castSession, boolean b) {
        onApplicationConnected(castSession);
        module.emitMessageToRN(RnGoogleCastModule.SESSION_RESUMED, null);
    }

    @Override
    public void onSessionResumeFailed(CastSession castSession, int i) {
        onApplicationDisconnected();
        module.emitMessageToRN(RnGoogleCastModule.SESSION_RESUME_FAILED, null);
    }

    @Override
    public void onSessionSuspended(CastSession castSession, int i) {
        module.emitMessageToRN(RnGoogleCastModule.SESSION_SUSPENDED, null);
    }

    private void onApplicationConnected(final CastSession castSession) {
        module.setCastSession(castSession);
        module.runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                RemoteMediaClient client = castSession.getRemoteMediaClient();
                if (client == null) {
                    return;
                }

                remoteMediaClientListener = new RNRemoteMediaClientListener(module);
                client.addListener(remoteMediaClientListener);
                client.addProgressListener(remoteMediaClientListener, 1000);
            }
        });
    }

    private void onApplicationDisconnected() {
        module.setCastSession(null);
    }
}
