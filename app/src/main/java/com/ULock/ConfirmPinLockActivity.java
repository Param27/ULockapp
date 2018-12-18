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
import android.widget.Toast;

import com.ULock.App_Database.ExitActivity;
import com.andexert.library.RippleView;
import com.ULock.Prefhelper.Prefshelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ConfirmPinLockActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView, txt_title;
    EditText edt_pin;
    ImageView img_back;
    String str_pin;
    Button btn_cancel, btn_continue, btn_confirm;
    Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven, btn_eight, btn_nine, btn_zero, btn_clear;
    int number;
    String confirmScreen, changeScreen;
    LockSelectionActivity lockSelectionActivity;
    Prefshelper prefshelper;
    RippleView rippleView_cnfrm, rippleView_contnue, rippleView_cancl;
    protected int numFailedAttempts=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinlock);
        prefshelper=new Prefshelper(this);
        rippleView_cnfrm=(RippleView)findViewById(R.id.confrm);
        rippleView_cancl=(RippleView)findViewById(R.id.ripple);
        rippleView_contnue=(RippleView)findViewById(R.id.more);
        btn_cancel=(Button)findViewById(R.id.button_cancel);
        btn_continue=(Button)findViewById(R.id.button_continue);
        btn_confirm=(Button)findViewById(R.id.button_confirm);
        btn_one=(Button)findViewById(R.id.pin_code_button_1);
        btn_two=(Button)findViewById(R.id.pin_code_button_2);
        btn_three=(Button)findViewById(R.id.pin_code_button_3);
        btn_four=(Button)findViewById(R.id.pin_code_button_4);
        btn_five=(Button)findViewById(R.id.pin_code_button_5);
        btn_six=(Button)findViewById(R.id.pin_code_button_6);
        btn_seven=(Button)findViewById(R.id.pin_code_button_7);
        btn_eight=(Button)findViewById(R.id.pin_code_button_8);
        btn_nine=(Button)findViewById(R.id.pin_code_button_9);
        btn_zero=(Button)findViewById(R.id.pin_code_button_0);
        btn_clear=(Button)findViewById(R.id.pin_code_button_clear);
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);
        textView=(TextView)findViewById(R.id.textView);
        edt_pin=(EditText)findViewById(R.id.editText);
        lockSelectionActivity=new LockSelectionActivity();
        changeScreen=getIntent().getStringExtra("change");
        confirmScreen=getIntent().getStringExtra("confirm");
        Log.e("confirm screen",""+getIntent().getStringExtra("data1"));
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        rippleView_cnfrm.setVisibility(View.VISIBLE);
        rippleView_contnue.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        if(v==btn_one)
        {
            number= 1;


                edt_pin.setText(edt_pin.getText().toString()+number);


        }
        if(v==btn_two)
        {
            number= 2;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_three)
        {
            number= 3;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_four)
        {
            number= 4;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_five)
        {
            number= 5;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_six)
        {
            number= 6;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_seven)
        {
            number= 7;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_eight)
        {
            number= 8;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_nine)
        {
            number= 9;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_zero)
        {
            number= 0;
            edt_pin.setText(edt_pin.getText().toString()+number);
        }
        if(v==btn_clear)
        {
            if (!edt_pin.getText().toString().equalsIgnoreCase("")) {
                edt_pin.setText(edt_pin.getText().subSequence(0, edt_pin.getText().toString().length() - 1));
            }

        }

        if(v==btn_confirm)
        {

            if(confirmScreen.equalsIgnoreCase("confirm"))
            {
                Log.d("confirmmmmmmm",prefshelper.getConfirmPin()+"  "+prefshelper.getPin());
                if(prefshelper.getConfirmPin().equalsIgnoreCase(prefshelper.getPin())) {
                    if(((prefshelper.getLockType()).equalsIgnoreCase("gesture"))||((prefshelper.getLockType()).equalsIgnoreCase("pattern"))
                            ||((prefshelper.getLockType()).equalsIgnoreCase("password"))) {

                        if (getIntent().getStringExtra("data1")!=(null)) {
                            prefshelper.storeservice("1");
                            Log.e("saved data", "" + prefshelper.getservice());
                            Log.e("manja", "if");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAndRemoveTask();
                            } else {
                                ExitActivity.exitApplication(ConfirmPinLockActivity.this);
                            }
                        } else {
                            Intent i1 = new Intent(ConfirmPinLockActivity.this, AppListActivity.class);
                            startActivity(i1);
                            finish();
                            prefshelper.storeSecondaryLock("secondary");
                        }

                    }

                    else
                    {
                       alert();
                    }

                }
                else
                {
                    textView.setText("PINs do not match, Re-enter PIN.");
                    edt_pin.startAnimation(AnimationUtils.loadAnimation(ConfirmPinLockActivity.this, R.anim.shake));
                    onWrongPin();
                }
            }
            else if(changeScreen.equalsIgnoreCase("change"))
            {
                Log.d("changeee",prefshelper.getConfirmPin()+"  "+prefshelper.getPin());
                if(prefshelper.getConfirmPin().equalsIgnoreCase(prefshelper.getPin())) {
                    Intent i1 = new Intent(ConfirmPinLockActivity.this, LockSelectionActivity.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    textView.setText("PINs do not match, Re-enter PIN.");
                    edt_pin.startAnimation(AnimationUtils.loadAnimation(ConfirmPinLockActivity.this, R.anim.shake));
                    onWrongPin();
                }
            }
            else if((changeScreen.equalsIgnoreCase(null))&& (confirmScreen.equalsIgnoreCase(null)))
            {
                Log.d("nothingg",prefshelper.getConfirmPin()+"  "+prefshelper.getPin());
                if(prefshelper.getConfirmPin().equalsIgnoreCase(prefshelper.getPin())) {
                    Intent i1 = new Intent(ConfirmPinLockActivity.this, AppListActivity.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    textView.setText("PINs do not match, Re-enter PIN.");
                    edt_pin.startAnimation(AnimationUtils.loadAnimation(ConfirmPinLockActivity.this, R.anim.shake));
                    onWrongPin();
                }
            }

        }
        if(v==btn_cancel)
        {
            if(confirmScreen.equalsIgnoreCase("confirm"))
            {
                if (getIntent().getStringExtra("data1")!=(null)) {
                    onResume();
                }
               else
                {
                    Intent i1 = new Intent(ConfirmPinLockActivity.this, LockSelectionActivity.class);
                    startActivity(i1);
                    finish();
                    lockSelectionActivity.reset(LockSelectionActivity.root);
                    lockSelectionActivity.addListener(LockSelectionActivity.root);
                    lockSelectionActivity.mExplosionField.clear();
                }


            }
            else if(changeScreen.equalsIgnoreCase("change"))
            {
                Intent i1 = new Intent(ConfirmPinLockActivity.this, AppListActivity.class);
                startActivity(i1);
                finish();
            }
            else if((changeScreen.equalsIgnoreCase(""))|| (confirmScreen.equalsIgnoreCase("")))
            {
                Intent i1 = new Intent(ConfirmPinLockActivity.this, LockSelectionActivity.class);
                startActivity(i1);
                finish();
                lockSelectionActivity.reset(LockSelectionActivity.root);
                lockSelectionActivity.addListener(LockSelectionActivity.root);
                lockSelectionActivity.mExplosionField.clear();
            }


        }


         edt_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_pin.setSelection(edt_pin.getText().length());
                str_pin = edt_pin.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                str_pin = edt_pin.getText().toString();
                if (str_pin.length() < 4) {
                    textView.setText("PIN must be more than 4 in length");
                    btn_confirm.setTextColor(ContextCompat.getColor(ConfirmPinLockActivity.this, R.color.disabled_color));
                    btn_confirm.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(ConfirmPinLockActivity.this, R.drawable.right_black)), null);
                    btn_zero.setEnabled(true);
                    btn_one.setEnabled(true);
                    btn_two.setEnabled(true);
                    btn_three.setEnabled(true);
                    btn_four.setEnabled(true);
                    btn_five.setEnabled(true);
                    btn_six.setEnabled(true);
                    btn_seven.setEnabled(true);
                    btn_eight.setEnabled(true);
                    btn_nine.setEnabled(true);
                    btn_confirm.setEnabled(false);
                } else {
                    textView.setText("Confirm?");
                    btn_confirm.setEnabled(true);
                    btn_confirm.setTextColor(ContextCompat.getColor(ConfirmPinLockActivity.this, R.color.colorAccent));
                    btn_confirm.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(ConfirmPinLockActivity.this, R.drawable.navigatenext)), null);
                   /* btn_zero.setEnabled(false);
                    btn_one.setEnabled(false);
                    btn_two.setEnabled(false);
                    btn_three.setEnabled(false);
                    btn_four.setEnabled(false);
                    btn_five.setEnabled(false);
                    btn_six.setEnabled(false);
                    btn_seven.setEnabled(false);
                    btn_eight.setEnabled(false);
                    btn_nine.setEnabled(false);*/
                    prefshelper.storeConfirmPin(str_pin);
                }

            }
        });
    }
    protected void onWrongPin() {
        ++numFailedAttempts;
        if(numFailedAttempts==1 && ((prefshelper.getSecondaryLock()).equalsIgnoreCase("")))
        {
            Intent i1=new Intent(ConfirmPinLockActivity.this, PinLockActivity.class);
            startActivity(i1);
            finish();
        }
        if(numFailedAttempts==3)
        {
            prefshelper.appLocked("permanent");
            Toast.makeText(this, "Your App has been locked permanently, You need to reinstall the app", Toast.LENGTH_SHORT).show();
            finish();
           /* if(confirmScreen.equalsIgnoreCase("confirm"))
            {
                Intent i1=new Intent(ConfirmPinLockActivity.this, ConfirmPinLockActivity.class);
                i1.putExtra("confirm", "confirm");
                i1.putExtra("change", "");
                startActivity(i1);
                finish();
            }
            else if(changeScreen.equalsIgnoreCase("change"))
            {
                Intent i1=new Intent(ConfirmPinLockActivity.this, ConfirmPinLockActivity.class);
                i1.putExtra("change", "change");
                i1.putExtra("confirm", "");
                startActivity(i1);
                finish();
            }
            else if((changeScreen.equalsIgnoreCase(null))&& (confirmScreen.equalsIgnoreCase(null)))
            {
                Intent i1=new Intent(ConfirmPinLockActivity.this, ConfirmPinLockActivity.class);
                startActivity(i1);
                finish();
            }
*/
        }
    }

    @Override
    public void onBackPressed()
    {
        if(confirmScreen.equalsIgnoreCase("confirm"))
        {
            if (getIntent().getStringExtra("data1")!=(null)) {
                onResume();
            }
            else {
                finish();
            }
        }
        else if(changeScreen.equalsIgnoreCase("change"))
        {
            Intent i1 = new Intent(ConfirmPinLockActivity.this, AppListActivity.class);
            startActivity(i1);
            finish();
        }
        else if((changeScreen.equalsIgnoreCase(null))&& (confirmScreen.equalsIgnoreCase(null)))
        {
            Intent i1 = new Intent(ConfirmPinLockActivity.this, LockSelectionActivity.class);
            startActivity(i1);
            finish();
            lockSelectionActivity.reset(LockSelectionActivity.root);
            lockSelectionActivity.addListener(LockSelectionActivity.root);
            lockSelectionActivity.mExplosionField.clear();
        }
    }
    public void alert() {
        new AlertDialog.Builder(ConfirmPinLockActivity.this)
                .setTitle("Please Note")
                .setMessage("PIN you entered will be kept as a secondary option. Please choose another lock as well.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        prefshelper.storeSecondaryLock("secondary");
                        Intent i1 = new Intent(ConfirmPinLockActivity.this, LockSelectionActivity.class);
                        startActivity(i1);
                        finish();
                    }
                }).create().show();
    }
}



