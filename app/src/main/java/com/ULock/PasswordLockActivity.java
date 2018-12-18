package com.ULock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.ULock.Prefhelper.Prefshelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class PasswordLockActivity extends AppCompatActivity {
    TextView textView, txt_title;
    EditText edt_pin;
    ImageView img_back;
    String str_pin;
    Button btn_cancel, btn_continue, btn_confirm;
    RippleView rippleView_cnfrm, rippleView_contnue, rippleView_cancl;
    Prefshelper prefshelper;
   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passlock);
       
        btn_cancel=(Button)findViewById(R.id.button_cancel);
        btn_continue=(Button)findViewById(R.id.button_continue);
        btn_confirm=(Button)findViewById(R.id.button_confirm);
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);
        textView=(TextView)findViewById(R.id.textView);
        edt_pin=(EditText)findViewById(R.id.editText);
        prefshelper=new Prefshelper(this);
        rippleView_cnfrm=(RippleView)findViewById(R.id.confrm);
        rippleView_cancl=(RippleView)findViewById(R.id.ripple);
        rippleView_contnue=(RippleView)findViewById(R.id.more);
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
                    textView.setText("Password must be more than 6 in length");
                    btn_continue.setEnabled(false);
                    btn_continue.setTextColor(ContextCompat.getColor(PasswordLockActivity.this, R.color.disabled_color));
                    btn_continue.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(PasswordLockActivity.this, R.drawable.right_black)), null);

                } else {
                    textView.setText("Are you sure?");
                    btn_continue.setEnabled(true);
                    prefshelper.storePassword(str_pin);
                    btn_continue.setTextColor(ContextCompat.getColor(PasswordLockActivity.this, R.color.colorAccent));
                    btn_continue.setCompoundDrawablesWithIntrinsicBounds(null, null, (ContextCompat.getDrawable(PasswordLockActivity.this, R.drawable.navigatenext)), null);


                }
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rippleView_contnue.setVisibility(View.VISIBLE);
                Intent intent = new Intent(PasswordLockActivity.this, ConfirmPassLockActivity.class);
                intent.putExtra("change","");
                intent.putExtra("confirm", "confirm");
                intent.putExtra("data1","13");
                intent.putExtra("pin", prefshelper.getPassword());
                startActivity(intent);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(PasswordLockActivity.this, LockSelectionActivity.class);
                startActivity(i1);
                finish();
                LockSelectionActivity.reset(LockSelectionActivity.root);
                LockSelectionActivity.addListener(LockSelectionActivity.root);
                LockSelectionActivity.mExplosionField.clear();
            }
        });
        
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i1 = new Intent(PasswordLockActivity.this, LockSelectionActivity.class);
        startActivity(i1);
        finish();
        LockSelectionActivity.reset(LockSelectionActivity.root);
        LockSelectionActivity.addListener(LockSelectionActivity.root);
        LockSelectionActivity.mExplosionField.clear();
    }
}
