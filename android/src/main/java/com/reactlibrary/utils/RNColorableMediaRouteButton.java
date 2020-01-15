package com.reactlibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.mediarouter.app.MediaRouteButton;

public class RNColorableMediaRouteButton extends MediaRouteButton {
    protected Drawable mRemoteIndicatorDrawable;
    private Integer mColor;

    public RNColorableMediaRouteButton(Context context, Integer mColor) { super(context); }

    public RNColorableMediaRouteButton(Context context,Integer mColor, AttributeSet attrs) {
        super(context, attrs);
    }

    public RNColorableMediaRouteButton(Context context,Integer mColor, AttributeSet attrs,
                                     int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setRemoteIndicatorDrawable(Drawable d) {
        mRemoteIndicatorDrawable = d;
        super.setRemoteIndicatorDrawable(d);
        if (mColor != null)
            applyTint(mColor);
    }

    public void applyTint(Integer color) {
        if (mRemoteIndicatorDrawable == null)
            return;

        Drawable wrapDrawable = DrawableCompat.wrap(mRemoteIndicatorDrawable);
        DrawableCompat.setTint(wrapDrawable, color);
    }
}
