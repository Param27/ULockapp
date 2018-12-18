package com.ULock;

import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.ULock.App_Database.ExitActivity;
import com.andexert.library.RippleView;
import com.ULock.Prefhelper.Prefshelper;
import com.ULock.util.PasswordGesturesLibrary;

import java.io.File;
import java.util.ArrayList;

public class ConfirmGestureActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener{

    private final File mStoreFile = new File(Environment.getExternalStorageDirectory(), "gestures");

    private Button btn_cancel, btn_confirm,mContinueButton;
    TextView txt_title;
    ImageView img_back,imgCart;
    Prefshelper prefshelper;
    RippleView rippleView_cnfrm, rippleView_contnue, rippleView_cancl;
    String confirmScreen, changeScreen;
    protected int numFailedAttempts=0;
    GestureLibrary store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_gesture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        store = PasswordGesturesLibrary.getStore(this);
        txt_title=(TextView)toolbar.findViewById(R.id.toolbar_title);
        txt_title.setText("Confirm Gesture");
        img_back=(ImageView)toolbar.findViewById(R.id.imageView_back);
        img_back.setVisibility(View.GONE);
        imgCart=(ImageView)toolbar.findViewById(R.id.image_cart);
        imgCart.setVisibility(View.GONE);

        prefshelper=new Prefshelper(this);
        rippleView_cnfrm=(RippleView)findViewById(R.id.confrm);
        rippleView_cnfrm.setVisibility(View.VISIBLE);
        rippleView_cancl=(RippleView)findViewById(R.id.ripple);
        rippleView_contnue=(RippleView)findViewById(R.id.more);
        rippleView_contnue.setVisibility(View.GONE);
        btn_cancel= (Button)findViewById(R.id.button_cancel);
        mContinueButton = (Button)findViewById(R.id.button_continue);
        mContinueButton.setVisibility(View.GONE);
        btn_confirm = (Button)findViewById(R.id.button_confirm);
        btn_confirm.setVisibility(View.VISIBLE);
        changeScreen=getIntent().getStringExtra("change");
        confirmScreen=getIntent().getStringExtra("confirm");
        Log.e("confirm screen",""+getIntent().getStringExtra("data1"));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefshelper.storeLockType("gesture");
                if(confirmScreen.equalsIgnoreCase("confirm"))
                {
                    if((prefshelper.getSecondaryLock()).equalsIgnoreCase("secondary")) {

                        if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                            prefshelper.storeservice("1");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAndRemoveTask();
                            } else {
                                ExitActivity.exitApplication(ConfirmGestureActivity.this);
//                        finish();
                            }
                        } else {

                            finish();
                            Intent i1 = new Intent(ConfirmGestureActivity.this, AppListActivity.class);
                            startActivity(i1);
                            Log.e("hiiiiiiii", "i am there");
                        }
                    }
                        else
                        {
                            Log.e("hiiiiiiii","i am here");

                            alert();
                        }
                }
                else if(changeScreen.equalsIgnoreCase("change"))
                {
                    finish();
                    Intent i1 = new Intent(ConfirmGestureActivity.this, LockSelectionActivity.class);
                    i1.putExtra("change","change");
                    startActivity(i1);
                }
                else if((changeScreen.equalsIgnoreCase(""))&& (confirmScreen.equalsIgnoreCase("")))
                {
                    if((prefshelper.getSecondaryLock()).equalsIgnoreCase("secondary"))
                    {
                        finish();
                        Intent i1 = new Intent(ConfirmGestureActivity.this, AppListActivity.class);
                        startActivity(i1);
                    }
                    else
                    {
                        finish();
                        Intent i1 = new Intent(ConfirmGestureActivity.this, PinLockActivity.class);
                        startActivity(i1);
                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmScreen.equalsIgnoreCase("confirm"))
                {
                    Log.e("check it","check it gesture cofirm");
                    if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
//                        prefshelper.storeservice("1");
                        onResume();
                    }

                        else if (getIntent().getStringExtra("ist")!=null) {
                        if (getIntent().getStringExtra("ist").equalsIgnoreCase("time"))
                            finish();
                        }
                        else
                        {
                            Intent i1 = new Intent(ConfirmGestureActivity.this, LockSelectionActivity.class);
                            startActivity(i1);
                            finish();
                            LockSelectionActivity.reset(LockSelectionActivity.root);
                            LockSelectionActivity.addListener(LockSelectionActivity.root);
                            LockSelectionActivity.mExplosionField.clear();
                        }

                    }

                else if(changeScreen.equalsIgnoreCase("change"))
                {
                    Intent i1 = new Intent(ConfirmGestureActivity.this, LockSelectionActivity.class);
                    startActivity(i1);
                    finish();
                }
                else if((changeScreen.equalsIgnoreCase(""))|| (confirmScreen.equalsIgnoreCase("")))
                {
                    Intent i1 = new Intent(ConfirmGestureActivity.this, LockSelectionActivity.class);
                    startActivity(i1);
                    finish();
                    LockSelectionActivity.reset(LockSelectionActivity.root);
                    LockSelectionActivity.addListener(LockSelectionActivity.root);
                    LockSelectionActivity.mExplosionField.clear();
                }
            }
        });
        btn_confirm.setEnabled(false);
        btn_confirm.setTextColor(ContextCompat.getColor(ConfirmGestureActivity.this, R.color.disabled_color));
        btn_confirm.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(ConfirmGestureActivity.this, R.drawable.right_black)), null);

        GestureOverlayView gesturesView = (GestureOverlayView) findViewById(R.id.g2);
                gesturesView.addOnGesturePerformedListener(this);
                store.load();

            }

            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                ArrayList<Prediction> predictions = store.recognize(gesture);
                if (predictions.size() > 0) {
                    Prediction prediction = predictions.get(0);
                       if (prediction.score > 7.0) {
                         btn_confirm.setEnabled(true);
                           txt_title.setText("Confirmed!!");
                           btn_confirm.setEnabled(true);
                           btn_confirm.setTextColor(ContextCompat.getColor(ConfirmGestureActivity.this, R.color.colorAccent));
                           btn_confirm.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(ConfirmGestureActivity.this, R.drawable.navigatenext)), null);

                       }
                       else {
                           Toast.makeText(this, "Please enter correct pattern", Toast.LENGTH_SHORT).show();
                           txt_title.setText("Error!!");
                           onWrongGesture();
                       }
                   }


            }
    protected void onWrongGesture() {
        ++numFailedAttempts;
        if(numFailedAttempts==1 && ((prefshelper.getSecondaryLock()).equalsIgnoreCase("")))
        {
            Intent i1=new Intent(ConfirmGestureActivity.this, GestureDetectorActivity.class);
            startActivity(i1);
            finish();
        }
        if(numFailedAttempts==3)
        {

            if(confirmScreen.equalsIgnoreCase("confirm"))
            {
                if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                    prefshelper.storeservice("1");
                    Log.e("saved data", "" + prefshelper.getservice());
                    Log.e("manja", "if");

                    Intent i1=new Intent(ConfirmGestureActivity.this, ConfirmPinLockActivity.class);
                    i1.putExtra("confirm", "confirm");
                    i1.putExtra("data1", ""+getIntent().getStringExtra("data1"));
                    i1.putExtra("change", "");
                    startActivity(i1);
                    finish();
                }
                else
                {
                    Intent i1=new Intent(ConfirmGestureActivity.this, ConfirmPinLockActivity.class);
                    i1.putExtra("confirm", "confirm");
                    i1.putExtra("change", "");
                    startActivity(i1);
                    finish();
                }


            }
            else if(changeScreen.equalsIgnoreCase("change"))
            {
                Intent i1=new Intent(ConfirmGestureActivity.this, ConfirmPinLockActivity.class);
                i1.putExtra("change", "change");
                i1.putExtra("confirm", "");
                startActivity(i1);
                finish();
            }
            else if((changeScreen.equalsIgnoreCase(null))&& (confirmScreen.equalsIgnoreCase(null)))
            {
                Intent i1=new Intent(ConfirmGestureActivity.this, ConfirmPinLockActivity.class);
                startActivity(i1);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
//        super.onBackPressed();
        if(confirmScreen.equalsIgnoreCase("confirm"))
        {

            if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                Log.e("check inside","back it gesture cofirm");
                onResume();
//                prefshelper.storeservice("1");
            }
            else if (getIntent().getStringExtra("ist")!=null) {
                if (getIntent().getStringExtra("ist").equalsIgnoreCase("time"))
                    finish();
            }
            else
            {
                Intent i1 = new Intent(ConfirmGestureActivity.this, LockSelectionActivity.class);
                startActivity(i1);
                finish();
                LockSelectionActivity.reset(LockSelectionActivity.root);
                LockSelectionActivity.addListener(LockSelectionActivity.root);
                LockSelectionActivity.mExplosionField.clear();
            }



        }
        else if(changeScreen.equalsIgnoreCase("change"))
        {
            Intent i1 = new Intent(ConfirmGestureActivity.this, AppListActivity.class);
            startActivity(i1);
            finish();
        }
        else if((changeScreen.equalsIgnoreCase(""))&& (confirmScreen.equalsIgnoreCase("")))
        {
            Intent i1 = new Intent(ConfirmGestureActivity.this, LockSelectionActivity.class);
            startActivity(i1);
            finish();
            LockSelectionActivity.reset(LockSelectionActivity.root);
            LockSelectionActivity.addListener(LockSelectionActivity.root);
            LockSelectionActivity.mExplosionField.clear();
        }
    }
    public void alert() {
        new AlertDialog.Builder(ConfirmGestureActivity.this)
                .setTitle("Please Note")
                .setMessage("You need to enter a PIN as secondary option to unlock the uLock app.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i1 = new Intent(ConfirmGestureActivity.this, PinLockActivity.class);
                        startActivity(i1);
                        finish();
                    }
                }).create().show();
    }
}
