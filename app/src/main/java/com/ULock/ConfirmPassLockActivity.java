package com.ULock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ULock.App_Database.ExitActivity;
import com.andexert.library.RippleView;
import com.ULock.Prefhelper.Prefshelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ConfirmPassLockActivity extends AppCompatActivity {

    TextView textView, txt_title;
    EditText edt_pin;
    ImageView img_back;
    String str_pin;
    Button btn_cancel, btn_continue, btn_confirm;
    Prefshelper prefshelper;
    RippleView rippleView_cnfrm, rippleView_contnue, rippleView_cancl;
    String confirmScreen, changeScreen;
    protected int numFailedAttempts=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passlock);
        btn_cancel=(Button)findViewById(R.id.button_cancel);
        btn_continue=(Button)findViewById(R.id.button_continue);
        btn_continue.setVisibility(View.GONE);
        prefshelper=new Prefshelper(this);
        rippleView_cnfrm=(RippleView)findViewById(R.id.confrm);
        rippleView_cancl=(RippleView)findViewById(R.id.ripple);
        rippleView_contnue=(RippleView)findViewById(R.id.more);
        btn_confirm=(Button)findViewById(R.id.button_confirm);
        btn_confirm.setVisibility(View.VISIBLE);
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);
        textView=(TextView)findViewById(R.id.textView);
        textView.setText("Confirm Password");
        edt_pin=(EditText)findViewById(R.id.editText);
        rippleView_cnfrm.setVisibility(View.VISIBLE);
        rippleView_contnue.setVisibility(View.GONE);
        changeScreen=getIntent().getStringExtra("change");
        confirmScreen=getIntent().getStringExtra("confirm");
        Log.e("confirm screen",""+getIntent().getStringExtra("data1"));
        edt_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str_pin = edt_pin.getText().toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                str_pin = edt_pin.getText().toString();
                if (str_pin.length() < 6) {
                    btn_confirm.setEnabled(false);
                    textView.setText("Password must be more than 6 in length");
                    btn_confirm.setTextColor(ContextCompat.getColor(ConfirmPassLockActivity.this, R.color.disabled_color));
                    btn_confirm.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(ConfirmPassLockActivity.this, R.drawable.right_black)), null);

                } else {
                    textView.setText("Confirm?");
                    btn_confirm.setEnabled(true);
                    btn_confirm.setTextColor(ContextCompat.getColor(ConfirmPassLockActivity.this, R.color.colorAccent));
                    btn_confirm.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(ConfirmPassLockActivity.this, R.drawable.navigatenext)), null);
                    prefshelper.storeConfirmPass(str_pin);

                }
               
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("cccccccccccccccccccccc", prefshelper.getPassword());
                prefshelper.storeLockType("password");
                if(confirmScreen.equalsIgnoreCase("confirm"))
                {
                    if (prefshelper.getConfirmPass().equalsIgnoreCase(prefshelper.getPassword())) {
                        if((prefshelper.getSecondaryLock()).equalsIgnoreCase("secondary"))
                        {
                            if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                                prefshelper.storeservice("1");
                                Log.e("saved data", "" + prefshelper.getservice());
                                Log.e("manja", "if");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    finishAndRemoveTask();
                                } else {
                                    ExitActivity.exitApplication(ConfirmPassLockActivity.this);
                                }
                            } else {

                                finish();
                                Intent i1 = new Intent(ConfirmPassLockActivity.this, AppListActivity.class);
                                startActivity(i1);
                            }

                        }
                        else
                        {
                            alert();
                        }
                    } else {

                        textView.setText("Passwords do not match, Re-enter Password.");
                        edt_pin.startAnimation(AnimationUtils.loadAnimation(ConfirmPassLockActivity.this, R.anim.shake));
                        onWrongPass();
                    }
                }
                else if(changeScreen.equalsIgnoreCase("change"))
                {
                    if (prefshelper.getConfirmPass().equalsIgnoreCase(prefshelper.getPassword())) {
                        Intent i1 = new Intent(ConfirmPassLockActivity.this, LockSelectionActivity.class);
                        startActivity(i1);
                        finish();
                    } else {
                        textView.setText("Passwords do not match, Re-enter Password.");
                        edt_pin.startAnimation(AnimationUtils.loadAnimation(ConfirmPassLockActivity.this, R.anim.shake));
                        onWrongPass();
                    }
                }
                else if((changeScreen.equalsIgnoreCase(""))&& (confirmScreen.equalsIgnoreCase("")))
                {
                    if (prefshelper.getConfirmPass().equalsIgnoreCase(prefshelper.getPassword())) {
                        Intent i1 = new Intent(ConfirmPassLockActivity.this, AppListActivity.class);
                        startActivity(i1);
                        finish();
                    } else {
                        textView.setText("Passwords do not match, Re-enter Password.");
                        edt_pin.startAnimation(AnimationUtils.loadAnimation(ConfirmPassLockActivity.this, R.anim.shake));
                        onWrongPass();
                    }
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmScreen.equalsIgnoreCase("confirm")) {
                    if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                        onResume();
                    } else if (getIntent().getStringExtra("ist") != null) {
                        if (getIntent().getStringExtra("ist").equalsIgnoreCase("time"))
                            finish();
                    } else {
                         if (getIntent().getStringExtra("ist") != null) {
                            if (getIntent().getStringExtra("ist").equalsIgnoreCase("time"))
                                finish();
                        }
                        Intent i1 = new Intent(ConfirmPassLockActivity.this, LockSelectionActivity.class);
                        startActivity(i1);
                        finish();

                        LockSelectionActivity.reset(LockSelectionActivity.root);
                        LockSelectionActivity.addListener(LockSelectionActivity.root);
                        LockSelectionActivity.mExplosionField.clear();
                    }
                }
                else if(changeScreen.equalsIgnoreCase("change"))
                {
                    Intent i1 = new Intent(ConfirmPassLockActivity.this, AppListActivity.class);
                    startActivity(i1);
                    finish();
                }
                else if((changeScreen.equalsIgnoreCase(""))|| (confirmScreen.equalsIgnoreCase("")))
                {
                    Intent i1 = new Intent(ConfirmPassLockActivity.this, LockSelectionActivity.class);
                    startActivity(i1);
                    finish();
                    LockSelectionActivity.reset(LockSelectionActivity.root);
                    LockSelectionActivity.addListener(LockSelectionActivity.root);
                    LockSelectionActivity.mExplosionField.clear();
                }

            }
        });
    }
    protected void onWrongPass() {
        ++numFailedAttempts;
        if(numFailedAttempts==1 && ((prefshelper.getSecondaryLock()).equalsIgnoreCase("")))
        {
            Intent i1=new Intent(ConfirmPassLockActivity.this, PasswordLockActivity.class);
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

                Intent i1=new Intent(ConfirmPassLockActivity.this, ConfirmPinLockActivity.class);
                i1.putExtra("confirm", "confirm");
                i1.putExtra("data1", ""+getIntent().getStringExtra("data1"));
                i1.putExtra("change", "");
                startActivity(i1);
                finish();
            }
            else {
                    Intent i1 = new Intent(ConfirmPassLockActivity.this, ConfirmPinLockActivity.class);
                    i1.putExtra("confirm", "confirm");
                    i1.putExtra("change", "");
                    startActivity(i1);
                    finish();
                }

            }
            else if(changeScreen.equalsIgnoreCase("change"))
            {
                Intent i1=new Intent(ConfirmPassLockActivity.this, ConfirmPinLockActivity.class);
                i1.putExtra("change", "change");
                i1.putExtra("confirm", "");
                startActivity(i1);
                finish();
            }
            else if((changeScreen.equalsIgnoreCase(null))&& (confirmScreen.equalsIgnoreCase(null)))
            {
                Intent i1=new Intent(ConfirmPassLockActivity.this, ConfirmPinLockActivity.class);
                startActivity(i1);
                finish();
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        if(confirmScreen.equalsIgnoreCase("confirm"))
        {
            if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                onResume();
            }
            else if (getIntent().getStringExtra("ist")!=null) {
                if (getIntent().getStringExtra("ist").equalsIgnoreCase("time"))
                    finish();
            }
            else
            {
                Intent i1 = new Intent(ConfirmPassLockActivity.this, LockSelectionActivity.class);
                startActivity(i1);
                finish();
                LockSelectionActivity.reset(LockSelectionActivity.root);
                LockSelectionActivity.addListener(LockSelectionActivity.root);
                LockSelectionActivity.mExplosionField.clear();
            }


        }
        else if(changeScreen.equalsIgnoreCase("change"))
        {
            Intent i1 = new Intent(ConfirmPassLockActivity.this, AppListActivity.class);
            startActivity(i1);
            finish();
        }
        else if((changeScreen.equalsIgnoreCase(""))&& (confirmScreen.equalsIgnoreCase("")))
        {
            Intent i1 = new Intent(ConfirmPassLockActivity.this, LockSelectionActivity.class);
            startActivity(i1);
            finish();
            LockSelectionActivity.reset(LockSelectionActivity.root);
            LockSelectionActivity.addListener(LockSelectionActivity.root);
            LockSelectionActivity.mExplosionField.clear();
        }
    }
    public void alert() {
        new AlertDialog.Builder(ConfirmPassLockActivity.this)
                .setTitle("Please Note")
                .setMessage("You need to enter a PIN as secondary option to unlock the uLock app.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i1 = new Intent(ConfirmPassLockActivity.this, PinLockActivity.class);
                        startActivity(i1);
                        finish();
                    }
                }).create().show();
    }
}
