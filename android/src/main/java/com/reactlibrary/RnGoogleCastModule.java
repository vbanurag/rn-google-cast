package com.reactlibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.annotations.VisibleForTesting;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.reactlibrary.api.RNSessionManagerListener;
import com.reactlibrary.components.RNExpandedCastControlsActivity;
import com.reactlibrary.components.RNGoogleCastButton;
import com.reactlibrary.utils.RNMediaInfoBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RnGoogleCastModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    @VisibleForTesting
    public static final String REACT_CLASS = "RnGoogleCast";

    // Constants
    public static final String SESSION_STARTING = "GoogleCast:SessionStarting";
    public static final String SESSION_STARTED = "GoogleCast:SessionStarted";
    public static final String SESSION_START_FAILED =
            "GoogleCast:SessionStartFailed";
    public static final String SESSION_SUSPENDED =
            "GoogleCast:SessionSuspended";
    public static final String SESSION_RESUMING = "GoogleCast:SessionResuming";
    public static final String SESSION_RESUMED = "GoogleCast:SessionResumed";
    public static final String SESSION_RESUME_FAILED = "GoogleCast:SessionResumeFailed";
    public static final String SESSION_ENDING = "GoogleCast:SessionEnding";
    public static final String SESSION_ENDED = "GoogleCast:SessionEnded";

    public static final String MEDIA_STATUS_UPDATED =
            "GoogleCast:MediaStatusUpdated";
    public static final String MEDIA_PLAYBACK_STARTED =
            "GoogleCast:MediaPlaybackStarted";
    public static final String MEDIA_PLAYBACK_ENDED =
            "GoogleCast:MediaPlaybackEnded";
    public static final String MEDIA_PROGRESS_UPDATED =
            "GoogleCast:MediaProgressUpdated";

    public static final String CHANNEL_MESSAGE_RECEIVED = "GoogleCast:ChannelMessageReceived";

    public static final String E_CAST_NOT_AVAILABLE = "E_CAST_NOT_AVAILABLE";
    public static final String GOOGLE_CAST_NOT_AVAILABLE_MESSAGE = "Google Cast not available";


    public static final String CAST_STATE_CHANGED =
            "GoogleCast:CastStateChanged";
    public static final String DEFAULT_SUBTITLES_LANGUAGE = Locale.ENGLISH.getLanguage();

    private CastSession mCastSession = null;
    private SessionManagerListener<CastSession> mSessionManagerListener;

    private CastStateListener mCastStateListener;
    private CastContext mCastContext;


    /*
        'CAST_AVAILABLE' is volatile because 'initializeCast' is called on the main thread, but
        react-native modules may be initialized on any thread.
        */
    private static volatile boolean CAST_AVAILABLE = true;


    @Override
    public String getName() {
        return REACT_CLASS;
    }

    private final ReactApplicationContext reactContext;

    public RnGoogleCastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        if (CAST_AVAILABLE) {
            reactContext.addLifecycleEventListener(this);
            setupCastListener();
            setupCastStateListener();
        }
    }

    public void emitMessageToRN(String eventName,
                                @Nullable WritableMap params) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void castMedia(final ReadableMap params) {
        Log.v(REACT_CLASS, "Casting media... ");
        if (mCastSession == null) {
            return;
        }

        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();


                if (remoteMediaClient == null) {
                    return;
                }

                Integer seconds = null;
                if (params.hasKey("playPosition")) {
                    seconds = params.getInt("playPosition");
                }
                if (seconds == null) {
                    seconds = 0;
                }

                MediaInfo mediaInfo = RNMediaInfoBuilder.buildMediaInfo(params);
                remoteMediaClient.load(mediaInfo, true, seconds * 1000);

            }
        });
    }

    @ReactMethod
    public void showCastPicker() {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                RNGoogleCastButton.getGoogleCastButtonManagerInstance().performClick();
                Log.e(REACT_CLASS, "showCastPicker... ");
            }
        });
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void getCastDevice(final Promise promise) {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                if (mCastSession == null) {
                    promise.resolve(null);
                    return;
                }

                WritableMap map = Arguments.createMap();
                map.putString("id", mCastSession.getCastDevice().getDeviceId());
                map.putString("version", mCastSession.getCastDevice().getDeviceVersion());
                map.putString("name", mCastSession.getCastDevice().getFriendlyName());
                map.putString("model", mCastSession.getCastDevice().getModelName());
                promise.resolve(map);
            }
        });
    }

    @ReactMethod
    public void getCastState(final Promise promise) {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                if (CAST_AVAILABLE) {
                    CastContext castContext =
                            CastContext.getSharedInstance(reactContext);
                    promise.resolve(castContext.getCastState() - 1);
                } else {
                    promise.reject(E_CAST_NOT_AVAILABLE, GOOGLE_CAST_NOT_AVAILABLE_MESSAGE);
                }
            }
        });
    }

    @ReactMethod
    public void initChannel(final String namespace, final Promise promise) {
        if (mCastSession != null) {
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mCastSession.setMessageReceivedCallbacks(namespace, new Cast.MessageReceivedCallback() {
                            @Override
                            public void onMessageReceived(CastDevice castDevice, String channelNameSpace, String message) {
                                WritableMap map = Arguments.createMap();
                                map.putString("channel", channelNameSpace);
                                map.putString("message", message);
                                emitMessageToRN(CHANNEL_MESSAGE_RECEIVED, map);
                            }
                        });
                        promise.resolve(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        promise.reject(e);
                    }
                }
            });
        }
    }


    @ReactMethod
    public void sendMessage(final String message, final String namespace, final Promise promise) {
        if(mCastSession != null){
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mCastSession.sendMessage(namespace, message)
                                .setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        if (!status.isSuccess()) {
                                            Log.i(REACT_CLASS, "Error :> Sending message failed");
                                            promise.reject(String.valueOf(status.getStatusCode()), status.getStatusMessage());
                                        } else {
                                            Log.i(REACT_CLASS, "Message sent Successfully");
                                            promise.resolve(true);
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @ReactMethod
    public void play() {
        if (mCastSession != null) {
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    RemoteMediaClient client = mCastSession.getRemoteMediaClient();
                    if (client == null) {
                        return;
                    }

                    client.play();
                }
            });
        }
    }

    @ReactMethod
    public void pause() {
        if (mCastSession != null) {
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    RemoteMediaClient client = mCastSession.getRemoteMediaClient();
                    if (client == null) {
                        return;
                    }

                    client.pause();
                }
            });
        }
    }

    @ReactMethod
    public void stop() {
        if (mCastSession != null) {
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    RemoteMediaClient client = mCastSession.getRemoteMediaClient();
                    if (client == null) {
                        return;
                    }

                    client.stop();
                }
            });
        }
    }

    @ReactMethod
    public void seek(final int position) {
        if (mCastSession != null) {
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    RemoteMediaClient client = mCastSession.getRemoteMediaClient();
                    if (client == null) {
                        return;
                    }

                    client.seek(position * 1000);
                }
            });
        }
    }

    @ReactMethod
    public void setVolume(final double volume) {
        if (mCastSession != null) {
            getReactApplicationContext().runOnUiQueueThread(new Runnable() {
                @Override
                public void run() {
                    RemoteMediaClient client = mCastSession.getRemoteMediaClient();
                    if (client == null) {
                        return;
                    }

                    client.setStreamVolume(volume);
                }
            });
        }
    }

    @ReactMethod
    public void endSession(final boolean stopCasting, final Promise promise) {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                if (CAST_AVAILABLE) {
                    SessionManager sessionManager =
                            CastContext.getSharedInstance(getReactApplicationContext())
                                    .getSessionManager();
                    sessionManager.endCurrentSession(stopCasting);
                    promise.resolve(true);
                } else {
                    promise.reject(E_CAST_NOT_AVAILABLE, GOOGLE_CAST_NOT_AVAILABLE_MESSAGE);
                }
            }
        });
    }


    @ReactMethod
    public void launchExpandedControls() {
        if (CAST_AVAILABLE) {
            Intent intent = new Intent(reactContext, RNExpandedCastControlsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            reactContext.startActivity(intent);
        } else {
            Log.e(REACT_CLASS, "Error :> " + GOOGLE_CAST_NOT_AVAILABLE_MESSAGE);
        }

    }

    public static void initializeCast(Context context) {
        try {
            CastContext.getSharedInstance(context);
        } catch (RuntimeException e) {
            CAST_AVAILABLE = false;
        }
    }


    private void setupCastListener() {
        mSessionManagerListener = new RNSessionManagerListener(this);
    }

    private void setupCastStateListener() {
        mCastStateListener = new CastStateListener() {
            @Override
            public void onCastStateChanged(int newState) {
                // TODO: Implement onCastStateChanged
                Log.w("CAST_STATE_CHANGED:", "" + (newState - 1));
                emitMessageToRN(CAST_STATE_CHANGED, transformValue(newState - 1));
            }
        };

        try{
            mCastContext.addCastStateListener(mCastStateListener);
        }catch (Exception e){
            Log.e(REACT_CLASS, "Could not add listener to CastContext");
            Log.e(REACT_CLASS, e.toString());
        }
    }

    private WritableMap transformValue(int value) {
        WritableMap map = Arguments.createMap();
        map.putInt("playerState", value);
        return map;
    }


    @Override
    public void onHostResume() {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                mCastContext = CastContext.getSharedInstance(reactContext);
                SessionManager sessionManager = mCastContext.getSessionManager();
                mCastContext.addCastStateListener(mCastStateListener);
                sessionManager.addSessionManagerListener(mSessionManagerListener,
                        CastSession.class);
            }
        });

    }

    @Override
    public void onHostPause() {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                mCastContext = CastContext.getSharedInstance(reactContext);
                SessionManager sessionManager = mCastContext.getSessionManager();
                sessionManager.removeSessionManagerListener(mSessionManagerListener,
                        CastSession.class);
            }
        });
    }

    @Override
    public void onHostDestroy() {
        getReactApplicationContext().runOnUiQueueThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mCastContext = CastContext.getSharedInstance(reactContext);
                    SessionManager sessionManager = mCastContext.getSessionManager();
                    mCastContext.removeCastStateListener(mCastStateListener);
                    sessionManager.removeSessionManagerListener(mSessionManagerListener,
                            CastSession.class);
                } catch (Exception e) {
                    Log.w(REACT_CLASS, "No listeners to remove");
                }
            }
        });
    }

    public void setCastSession(CastSession castSession) {
        this.mCastSession = castSession;
    }

    public CastSession getCastSession() {
        return mCastSession;
    }

    public @Nullable
    MediaStatus getMediaStatus() {
        if (mCastSession == null) {
            return null;
        }

        RemoteMediaClient client = mCastSession.getRemoteMediaClient();
        if (client == null) {
            return null;
        }

        return client.getMediaStatus();
    }

    public void runOnUiQueueThread(Runnable runnable) {
        getReactApplicationContext().runOnUiQueueThread(runnable);
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        constants.put("SESSION_STARTING", SESSION_STARTING);
        constants.put("SESSION_STARTED", SESSION_STARTED);
        constants.put("SESSION_START_FAILED", SESSION_START_FAILED);
        constants.put("SESSION_SUSPENDED", SESSION_SUSPENDED);
        constants.put("SESSION_RESUMING", SESSION_RESUMING);
        constants.put("SESSION_RESUMED", SESSION_RESUMED);
        constants.put("SESSION_RESUME_FAILED", SESSION_RESUME_FAILED);
        constants.put("SESSION_ENDING", SESSION_ENDING);
        constants.put("SESSION_ENDED", SESSION_ENDED);

        constants.put("MEDIA_STATUS_UPDATED", MEDIA_STATUS_UPDATED);
        constants.put("MEDIA_PLAYBACK_STARTED", MEDIA_PLAYBACK_STARTED);
        constants.put("MEDIA_PLAYBACK_ENDED", MEDIA_PLAYBACK_ENDED);
        constants.put("MEDIA_PROGRESS_UPDATED", MEDIA_PROGRESS_UPDATED);

        constants.put("CAST_STATE_CHANGED", CAST_STATE_CHANGED);
        constants.put("CAST_AVAILABLE", CAST_AVAILABLE);

        constants.put("CHANNEL_MESSAGE_RECEIVED", CHANNEL_MESSAGE_RECEIVED);
        return constants;
    }

}
