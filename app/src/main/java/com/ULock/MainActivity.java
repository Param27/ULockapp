package com.ULock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ULock.App_Database.All_Password;
import com.ULock.Prefhelper.Prefshelper;
import com.ULock.view.ContentView;
import com.ULock.view.MainView;
import com.ULock.view.SplashView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private static final boolean DO_XML = false;
  
  private ViewGroup mMainView;
  private SplashView mSplashView;
 
  private Handler mHandler = new Handler();
  private View mContentView;
  Prefshelper prefshelper;
  String serv;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prefshelper=new Prefshelper(this);
    Log.e("serv data",""+serv);
    serv=getIntent().getStringExtra("data");
    // change the DO_XML variable to switch between code and xml
    if(DO_XML){
      // inflate the view from XML and then get a reference to it
      setContentView(R.layout.main_activity);
      mMainView = (ViewGroup) findViewById(R.id.main_view);
      mSplashView = (SplashView) findViewById(R.id.splash_view);

    } else {
      // create the main view and it will handle the rest
      mMainView = new MainView(getApplicationContext());
      mSplashView = ((MainView) mMainView).getSplashView();
      setContentView(mMainView);

    }
    
    // pretend like we are loading data
    startLoadingData();


  }

  private void startLoadingData(){
    // finish "loading data" in a random time between 1 and 3 seconds
    Random random = new Random();
    mHandler.postDelayed(new Runnable(){
      @Override
      public void run(){
        onLoadingDataEnded();
      }
    }, 1000 + random.nextInt(10));
//    getBaseContext().getApplicationContext().sendBroadcast(
//            new Intent("StartupReceiver_Manual_Start"));
  }
  
  private void onLoadingDataEnded(){
    Context context = getApplicationContext();
    // now that our data is loaded we can initialize the content view
    mContentView = new ContentView(context);
    // add the content view to the background
    mMainView.addView(mContentView, 0);


    // start the splash animation
    mSplashView.splashAndDisappear(new SplashView.ISplashListener(){
      @Override
      public void onStart(){
        // log the animation start event
        if(BuildConfig.DEBUG){
          Log.d(TAG, "splash started");
        }
        
      }
      
      @Override
      public void onUpdate(float completionFraction){
        // log animation update events
        if(BuildConfig.DEBUG){
          Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
        }
      }
      
      @Override
      public void onEnd(){
        // log the animation end event
        if(BuildConfig.DEBUG){
          Log.d(TAG, "splash ended");
        }
        // free the view so that it turns into garbage
        mSplashView = null;
        if(!DO_XML){
          // if inflating from code we will also have to free the reference in MainView as well
          // otherwise we will leak the View, this could be done better but so far it will suffice
          ((MainView) mMainView).unsetSplashView();
        }
        //All_Password all_password=new All_Password(MainActivity.this,"all");

        Intent intent = new Intent(MainActivity.this,All_Password.class);
          startActivity(intent);
          finish();


      }
    });
  }
}