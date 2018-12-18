package com.ULock;

import android.content.Intent;
import android.os.Bundle;

import com.andexert.library.RippleView;
import com.ULock.Prefhelper.Prefshelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PinLockActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView, txt_title;
    EditText edt_pin;
    ImageView img_back;
    String str_pin;
    Button btn_cancel, btn_continue, btn_confirm;
    Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven, btn_eight, btn_nine, btn_zero, btn_clear;
    int number;
   /* Animation fadeIn, fadeOut;
    AnimationSet animation;*/
    Prefshelper prefshelper;
    RippleView rippleView_cnfrm, rippleView_contnue, rippleView_cancl;
 

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


        if(v==btn_continue)
        {
         //   btn_continue.startAnimation(animation);
            rippleView_contnue.setVisibility(View.VISIBLE);
            Intent i1 = new Intent(PinLockActivity.this, ConfirmPinLockActivity.class);
            i1.putExtra("change","");
            i1.putExtra("confirm", "confirm");
            startActivity(i1);
            finish();
        }

        if(v==btn_cancel)
        {
                Intent i1 = new Intent(PinLockActivity.this, LockSelectionActivity.class);
                startActivity(i1);
                finish();
            LockSelectionActivity.reset(LockSelectionActivity.root);
            LockSelectionActivity.addListener(LockSelectionActivity.root);
            LockSelectionActivity.mExplosionField.clear();

        }
        edt_pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                str_pin = edt_pin.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edt_pin.setSelection(edt_pin.getText().length());


            }

            @Override
            public void afterTextChanged(Editable s) {
                str_pin = edt_pin.getText().toString();
                if (str_pin.length() < 4) {
                    textView.setText("PIN must be more than 4 in length");
                    btn_continue.setTextColor(ContextCompat.getColor(PinLockActivity.this, R.color.disabled_color));
                    btn_continue.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(PinLockActivity.this, R.drawable.right_black)), null);
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
                    btn_continue.setEnabled(false);
                } else if(str_pin.length()>=4){
                    textView.setText("Are you sure?");
                    btn_continue.setEnabled(true);
                    btn_continue.setTextColor(ContextCompat.getColor(PinLockActivity.this, R.color.colorAccent));
                    btn_continue.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(PinLockActivity.this, R.drawable.navigatenext)), null);
                    prefshelper.storePin(str_pin);
                    Log.e("pin.dsafrerwtrw4t.....", str_pin);
                 /*   btn_zero.setEnabled(false);
                    btn_one.setEnabled(false);
                    btn_two.setEnabled(false);
                    btn_three.setEnabled(false);
                    btn_four.setEnabled(false);
                    btn_five.setEnabled(false);
                    btn_six.setEnabled(false);
                    btn_seven.setEnabled(false);
                    btn_eight.setEnabled(false);
                    btn_nine.setEnabled(false);*/
                }

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i1 = new Intent(PinLockActivity.this, LockSelectionActivity.class);
        startActivity(i1);
        finish();
        LockSelectionActivity.reset(LockSelectionActivity.root);
        LockSelectionActivity.addListener(LockSelectionActivity.root);
        LockSelectionActivity.mExplosionField.clear();
    }
}

