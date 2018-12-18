package com.ULock.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.ULock.R;

public class ContentView extends ImageView {
  
  public ContentView(Context context){
    super(context);
    initialize();
  }
  
  @TargetApi(Build.VERSION_CODES.M)
  private void initialize(){
    // set the dummy content image here
   // setImageResource(R.drawable.splash_bg);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
    }
    else
    {
      setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }
  }
}