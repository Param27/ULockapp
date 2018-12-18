package com.ULock;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.List;

import com.ULock.App_Database.ExitActivity;
import com.ULock.Prefhelper.Prefshelper;
import com.ULock.util.ViewAccessibilityCompat;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

public class ConfirmPatternActivity  extends BasePatternActivity
        implements PatternView.OnPatternListener {

    private static final String KEY_NUM_FAILED_ATTEMPTS = "num_failed_attempts";

    public static final int RESULT_FORGOT_PASSWORD = RESULT_FIRST_USER;

    protected int numFailedAttempts=0;
    String confirmScreen, changeScreen;
    Prefshelper prefshelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefshelper=new Prefshelper(this);
        confirmScreen=getIntent().getStringExtra("confirm");
        changeScreen=getIntent().getStringExtra("change");
        Log.e("confirm screen",""+getIntent().getStringExtra("data1"));
        mMessageText.setText(R.string.pl_draw_pattern_to_unlock);
        mPatternView.setInStealthMode(isStealthModeEnabled());
        mPatternView.setOnPatternListener(this);
        mLeftButton.setText(R.string.pl_cancel);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        mRightButton.setText(R.string.pl_forgot_pattern);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                    Log.e("saved data", "" + prefshelper.getservice());
                    Log.e("manja", "if");

                    Intent i1=new Intent(ConfirmPatternActivity.this, ConfirmPinLockActivity.class);
                    i1.putExtra("confirm", "confirm");
                    i1.putExtra("data1", ""+getIntent().getStringExtra("data1"));
                    i1.putExtra("change", "");
                    startActivity(i1);
                    finish();
                }
                else {
                    onForgotPassword();
                }
            }
        });
        ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());

        if (savedInstanceState == null) {
            numFailedAttempts = 0;
        } else {
            numFailedAttempts = savedInstanceState.getInt(KEY_NUM_FAILED_ATTEMPTS);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_NUM_FAILED_ATTEMPTS, numFailedAttempts);
    }

    @Override
    public void onPatternStart() {

        removeClearPatternRunnable();

        // Set display mode to correct to ensure that pattern can be in stealth mode.    public void onPatternCellAdded(List<PatternView.Cell> pattern) {}

        mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
    }

    @Override
    public void onPatternDetected(List<PatternView.Cell> pattern) {
        if(confirmScreen.equalsIgnoreCase("confirm"))
        {
            if (isPatternCorrect(pattern)) {

              onConfirmed();

            } else {
                Log.d("lllllllllllllllll", "confiem");
                mMessageText.setText(me.zhanghai.android.patternlock.R.string.pl_wrong_pattern);
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
                onWrongPattern();
            }

        }
        else if(changeScreen.equalsIgnoreCase("change"))
        {
            if (isPatternCorrect(pattern)) {
                Log.e("lllllllllllllllll", "confiem");
                Intent i1=new Intent(ConfirmPatternActivity.this, LockSelectionActivity.class);
                startActivity(i1);
                finish();
            } else {
                Log.d("lllllllllllllllll", "confiem");
                mMessageText.setText(me.zhanghai.android.patternlock.R.string.pl_wrong_pattern);
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
                onWrongPattern();
            }
        }
        else if((changeScreen.equalsIgnoreCase(""))&& (confirmScreen.equalsIgnoreCase("")))
        {
            if (isPatternCorrect(pattern)) {
                Log.e("lllllllllllllllll", "confiem");
                if(prefshelper.getSecondaryLock().equalsIgnoreCase("secondary"))
                {
                    Intent i1=new Intent(ConfirmPatternActivity.this, AppListActivity.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    alert();
                }


            } else {
                Log.d("lllllllllllllllll", "confiem");
                mMessageText.setText(me.zhanghai.android.patternlock.R.string.pl_wrong_pattern);
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
                onWrongPattern();
            }
        }

    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {

    }

    protected boolean isStealthModeEnabled() {
        return false;
    }

    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {

        String patternSha1 = prefshelper.getPattern();

        return TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
    }

   /* @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        return PatternLockUtils.isPatternCorrect(pattern, this);
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        onResume();
    }

    protected void onConfirmed() {
        setResult(RESULT_OK);
        prefshelper.storeLockType("pattern");
        if((prefshelper.getSecondaryLock()).equalsIgnoreCase("secondary"))
        {
            if(confirmScreen.equalsIgnoreCase("confirm")) {
                Log.e("Confirm method","confirm");
                if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                    prefshelper.storeservice("1");
                    Log.e("saved data", "" + prefshelper.getservice());
                    Log.e("manja", "if");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        finishAndRemoveTask ();
                    }
                    else
                    {
                        ExitActivity.exitApplication(ConfirmPatternActivity.this);
//                        finish();
                    }
                } else {
                    Intent i1 = new Intent(ConfirmPatternActivity.this, AppListActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        }
        else
        {
            alert();
        }
    }

    protected void onWrongPattern() {
        ++numFailedAttempts;
        if(numFailedAttempts==3)
        {

            if(confirmScreen.equalsIgnoreCase("confirm"))
            {
                if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                    prefshelper.storeservice("1");
                    Log.e("saved data", "" + prefshelper.getservice());
                    Log.e("manja", "if");

                    Intent i1=new Intent(ConfirmPatternActivity.this, ConfirmPinLockActivity.class);
                    i1.putExtra("confirm", "confirm");
                    i1.putExtra("data1", ""+getIntent().getStringExtra("data1"));
                    i1.putExtra("change", "");
                    startActivity(i1);
                    finish();
                }
                else {
                    Intent i1 = new Intent(ConfirmPatternActivity.this, ConfirmPinLockActivity.class);
                    i1.putExtra("confirm", "confirm");
                    i1.putExtra("change", "");
                    startActivity(i1);
                    finish();
                }
            }
            else if(changeScreen.equalsIgnoreCase("change"))
            {
                Intent i1=new Intent(ConfirmPatternActivity.this, ConfirmPinLockActivity.class);
                i1.putExtra("change", "change");
                i1.putExtra("confirm", "");
                startActivity(i1);
                finish();
            }
            else if((changeScreen.equalsIgnoreCase(null))&& (confirmScreen.equalsIgnoreCase(null)))
            {
                Intent i1=new Intent(ConfirmPatternActivity.this, ConfirmPinLockActivity.class);
                startActivity(i1);
                finish();
            }

        }
    }

    protected void onCancel() {
        setResult(RESULT_CANCELED);

        if(confirmScreen.equalsIgnoreCase("confirm"))
        {
            if (getIntent().getStringExtra("data1").equalsIgnoreCase("0")) {
                prefshelper.storeservice("1");
                onResume();
            }
            else {
              finish();
            }
        }
        else if(changeScreen.equalsIgnoreCase("change"))
        {
            Intent i1 = new Intent(ConfirmPatternActivity.this, AppListActivity.class);
            startActivity(i1);
            finish();
        }
        else if((changeScreen.equalsIgnoreCase(""))|| (confirmScreen.equalsIgnoreCase("")))
        {
            Intent i1 = new Intent(ConfirmPatternActivity.this, LockSelectionActivity.class);
            startActivity(i1);
            finish();
            LockSelectionActivity.reset(LockSelectionActivity.root);
            LockSelectionActivity.addListener(LockSelectionActivity.root);
            LockSelectionActivity.mExplosionField.clear();
        }

    }
    public void alert() {
        new AlertDialog.Builder(ConfirmPatternActivity.this)
                .setTitle("Please Note")
                .setMessage("You need to enter a PIN as secondary option to unlock the uLock app.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i1 = new Intent(ConfirmPatternActivity.this, PinLockActivity.class);
                        startActivity(i1);
                        finish();
                    }
                }).create().show();
    }
    protected void onForgotPassword() {

        setResult(RESULT_FORGOT_PASSWORD);
        //finish();
        Intent intent=new Intent(this, ConfirmPinLockActivity.class);
        intent.putExtra("confirm", "confirm");
        startActivity(intent);
    }
}
