package com.ULock;


import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;

import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.ULock.Prefhelper.Prefshelper;
import com.andexert.library.RippleView;
import com.ULock.util.PasswordGesturesLibrary;
import com.ULock.R;


public class GestureDetectorActivity extends AppCompatActivity {

    GestureOverlayView gestures, overlay;
    private Gesture mGesture;
    private Button btn_cancel, mContinueButton,mbtnConfirm, mbtnRedraw;
    private static final float LENGTH_THRESHOLD = 120.0f;
    TextView txt_title;
    ImageView img_back, imgCart;
    Prefshelper prefshelper;
    RippleView rippleView_cnfrm, rippleView_contnue, rippleView_cancl,rippleView_redraw;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt_title=(TextView)toolbar.findViewById(R.id.toolbar_title);
        txt_title.setText("Draw Gesture");
        img_back=(ImageView)toolbar.findViewById(R.id.imageView_back);
        img_back.setVisibility(View.GONE);
        imgCart=(ImageView)toolbar.findViewById(R.id.image_cart);
        imgCart.setVisibility(View.GONE);
        prefshelper=new Prefshelper(this);
        gestures = (GestureOverlayView) findViewById(R.id.g2);
        overlay =   (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGestureListener(new GesturesProcessor());
        btn_cancel= (Button)findViewById(R.id.button_cancel);
        mContinueButton = (Button)findViewById(R.id.button_continue);
        mbtnConfirm = (Button)findViewById(R.id.button_confirm);
        mbtnRedraw= (Button)findViewById(R.id.button_redraw);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(v);
            }
        });
        rippleView_cnfrm=(RippleView)findViewById(R.id.confrm);
        rippleView_cancl=(RippleView)findViewById(R.id.ripple);
        rippleView_contnue=(RippleView)findViewById(R.id.more);
        rippleView_redraw=(RippleView)findViewById(R.id.redraw);
        rippleView_redraw.setVisibility(View.GONE);
        rippleView_cancl.setVisibility(View.VISIBLE);
        btn_cancel.setEnabled(true);
        String btnTxt=btn_cancel.getText().toString();


            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i1 = new Intent(GestureDetectorActivity.this,LockSelectionActivity.class);
                    startActivity(i1);
                    finish();
                   LockSelectionActivity.reset(LockSelectionActivity.root);
                   LockSelectionActivity.addListener(LockSelectionActivity.root);
                   LockSelectionActivity.mExplosionField.clear();

                }
            });
        mbtnRedraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overlay.cancelClearAnimation();
                overlay.clear(true);
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mGesture != null) {
            outState.putParcelable("gestures", mGesture);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mGesture = savedInstanceState.getParcelable("gestures");
        if (mGesture != null) {

            overlay.post(new Runnable() {
                public void run() {
                    overlay.setGesture(mGesture);

                }
            });


        }
    }

    public void confirm(View v) {
        if (mGesture != null) {

            final GestureLibrary store = PasswordGesturesLibrary.getStore(this);
            store.addGesture("gestures", mGesture);
            store.save();

            setResult(RESULT_OK);

            Intent intent = new Intent(this, ConfirmGestureActivity.class);
            intent.putExtra("change","");
            intent.putExtra("confirm", "confirm");
            intent.putExtra("data1","13");
            startActivity(intent);

        } else {
            setResult(RESULT_CANCELED);
        }

        finish();

    }

    public void cancelGesture(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
    private class GesturesProcessor implements GestureOverlayView.OnGestureListener {

        public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {


           /* mContinueButton.setEnabled(false);
            mContinueButton.setTextColor(ContextCompat.getColor(GestureDetectorActivity.this, R.color.disabled_color));
            mContinueButton.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(GestureDetectorActivity.this, R.drawable.right_black)), null);
         */

        }

        public void onGesture(GestureOverlayView overlay, MotionEvent event) {
        }

        public void onGestureEnded(final GestureOverlayView overlay, MotionEvent event) {
            mGesture = null;
            mGesture = overlay.getGesture();
            if (mGesture.getLength() < LENGTH_THRESHOLD) {
                overlay.clear(false);
            }
            mContinueButton.setEnabled(true);

            Log.e("first hereeeeeeeeeeeeee", "i am");
            mContinueButton.setEnabled(true);
            mContinueButton.setTextColor(ContextCompat.getColor(GestureDetectorActivity.this, R.color.colorAccent));
            mContinueButton.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(GestureDetectorActivity.this, R.drawable.navigatenext)), null);
            rippleView_cancl.setVisibility(View.GONE);
            btn_cancel.setEnabled(false);
            rippleView_redraw.setVisibility(View.VISIBLE);
            mbtnRedraw.setEnabled(true);


        }

        public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i1 = new Intent(GestureDetectorActivity.this,LockSelectionActivity.class);
        startActivity(i1);
        finish();
       LockSelectionActivity.reset(LockSelectionActivity.root);
       LockSelectionActivity.addListener(LockSelectionActivity.root);
       LockSelectionActivity.mExplosionField.clear();
    }
}
