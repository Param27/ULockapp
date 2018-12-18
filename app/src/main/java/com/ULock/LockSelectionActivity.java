package com.ULock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ULock.Prefhelper.Prefshelper;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import tyrantgit.explosionfield.ExplosionField;

public class LockSelectionActivity extends AppCompatActivity implements View.OnClickListener {
ImageView btn_gesture, btn_pin, btn_pwd, btn_pattern;
    private TextView txt_title;
    private ImageView img_back, imgCart;
    Shimmer shimmer;
    ShimmerTextView txt_shimmer;
    private static final int REQUEST_CODE_ENABLE = 11;
    public static ExplosionField mExplosionField;
    public static  View root ;
    Animation scale=null;
    Prefshelper prefshelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_selection);
        mExplosionField = ExplosionField.attach2Window(this);
        addListener(findViewById(R.id.root));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt_title=(TextView)toolbar.findViewById(R.id.toolbar_title);
        txt_title.setText("Choose Your Lock Type");
        img_back=(ImageView)toolbar.findViewById(R.id.imageView_back);
        img_back.setVisibility(View.GONE);
        imgCart=(ImageView)toolbar.findViewById(R.id.image_cart);
        imgCart.setVisibility(View.GONE);
        btn_gesture=(ImageView)findViewById(R.id.button_gesture);
        btn_pin=(ImageView)findViewById(R.id.button_pin);
        btn_pwd=(ImageView)findViewById(R.id.button_password);
        btn_pattern=(ImageView)findViewById(R.id.button_pattern);
        txt_shimmer=(ShimmerTextView)findViewById(R.id.textView2);
        scale = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        prefshelper=new Prefshelper(this);
        root=findViewById(R.id.root);
        shimmer = new Shimmer();
        shimmer.start(txt_shimmer);
        shimmer.setDuration(2000);
        btn_pattern.setEnabled(true);
        btn_pwd.setEnabled(true);
        btn_pin.setEnabled(true);
        btn_gesture.setEnabled(true);
        btn_gesture.setOnClickListener(this);
        btn_pattern.setOnClickListener(this);
        btn_pin.setOnClickListener(this);
        btn_pwd.setOnClickListener(this);


    }
    public  static void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            }) ;

        }
    }



    public static void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        reset(LockSelectionActivity.root);
        addListener(LockSelectionActivity.root);
        mExplosionField.clear();
    }

    @Override
    public void onClick(View v) {

        if(v==btn_gesture)
        {
            mExplosionField.explode(v);
           // btn_gesture.startAnimation(scale);
            btn_pattern.setEnabled(false);
            btn_pwd.setEnabled(false);
            btn_pin.setEnabled(false);
            Thread thread = new Thread( new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);

                            Intent i1 = new Intent(LockSelectionActivity.this, GestureDetectorActivity.class);
                            startActivity(i1);
                            finish();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        });

        thread.start();
         //   prefshelper.storeLockType("gesture");
        }
        if(v==btn_pattern)
        {
            mExplosionField.explode(v);
            btn_gesture.setEnabled(false);
            btn_pwd.setEnabled(false);
            btn_pin.setEnabled(false);
           // btn_pattern.startAnimation(scale);

            Thread thread = new Thread( new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        Intent i1 = new Intent(LockSelectionActivity.this, SetPatternActivity.class);
                        i1.putExtra("confirm", "confirm");
                        startActivity(i1);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
           // prefshelper.storeLockType("pattern");
        }
        if(v==btn_pin)
        {

            mExplosionField.explode(v);
           // btn_pin.startAnimation(scale);
            btn_pattern.setEnabled(false);
            btn_pwd.setEnabled(false);
            btn_gesture.setEnabled(false);
            Thread thread = new Thread( new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                            if((prefshelper.getSecondaryLock()).equalsIgnoreCase(""))
                            {
                                Intent i1 = new Intent(LockSelectionActivity.this, PinLockActivity.class);
                                startActivity(i1);
                                finish();
                            }
                            else
                            {
                                new Thread()
                                {
                                    public void run()
                                    {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                alert();
                                            }
                                        });
                                    }
                                }.start();
                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


        }
        if(v==btn_pwd)
        {
            mExplosionField.explode(v);
          //  btn_pwd.startAnimation(scale);
            btn_pattern.setEnabled(false);
            btn_gesture.setEnabled(false);
            btn_pin.setEnabled(false);
            Thread thread = new Thread( new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        Intent i1=new Intent(LockSelectionActivity.this, PasswordLockActivity.class);
                        startActivity(i1);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
          //  prefshelper.storeLockType("password");
        }

    }
    public void alert() {
        new AlertDialog.Builder(LockSelectionActivity.this)
                .setTitle("Please Note")
                .setMessage("You have already saved a PIN. Choose a different lock.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        Intent i1 = new Intent(LockSelectionActivity.this, LockSelectionActivity.class);
                        startActivity(i1);
                        overridePendingTransition(0,0);
                        dialog.dismiss();
                       }
                }).create().show();
    }
}
