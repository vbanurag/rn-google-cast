package com.example;

import com.facebook.react.ReactActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.os.Bundle;
import android.os.Build;
import com.google.android.gms.cast.framework.CastContext;
import androidx.annotation.Nullable;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "example";
  }



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
