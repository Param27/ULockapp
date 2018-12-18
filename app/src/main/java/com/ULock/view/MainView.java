package com.ULock.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

import com.ULock.R;

public class MainView extends FrameLayout {
  
  public MainView(Context context){
    super(context);
    initialize();
  }
  
  private SplashView mSplashView;
  
  private void initialize(){
    Context context = getContext();
    
    // initialize the view with all default values
    // you don't need to set these default values, they are already set, except for setIconResource
    // this is only for demonstration purposes
    mSplashView = new SplashView(context);
    mSplashView.setDuration(1000); // the animation will last 0.5 seconds
  //  mSplashView.setBackground(getResources().getDrawable(R.drawable.new_bg)); // transparent hole will look white before the animation
    mSplashView.setIconColor(Color.rgb(0, 150, 136)); // this is the Twitter blue color
    mSplashView.setIconResource(R.drawable.new_bg); // a Twitter icon with transparent hole in it
    mSplashView.setRemoveFromParentOnEnd(true); // remove the SplashView from MainView once animation is completed
    
    // add the view
    addView(mSplashView);
  }
  
  public void unsetSplashView(){
    mSplashView = null;
  }
  
  public SplashView getSplashView(){
    return mSplashView;
  }
}