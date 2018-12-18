package com.ULock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.ULock.Prefhelper.Prefshelper;
import com.ULock.util.ViewAccessibilityCompat;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

public class SetPatternActivity extends BasePatternActivity
        implements PatternView.OnPatternListener{
    Prefshelper prefshelper;
    String confirmScreen, changeScreen;
    private enum LeftButtonState {

        Cancel(R.string.pl_cancel, true),
        CancelDisabled(R.string.pl_cancel, false),
        Redraw(R.string.pl_redraw, true),
        RedrawDisabled(R.string.pl_redraw, false);

        public final int textId;
        public final boolean enabled;

        private LeftButtonState(int textId, boolean enabled) {
            this.textId = textId;
            this.enabled = enabled;
        }
    }

    private enum RightButtonState {

        Continue(R.string.pl_continue, true),
        ContinueDisabled(R.string.pl_continue, false),
        Confirm(R.string.pl_confirm, true),
        ConfirmDisabled(R.string.pl_confirm, false);

        public final int textId;
        public final boolean enabled;

        private RightButtonState(int textId, boolean enabled) {
            this.textId = textId;
            this.enabled = enabled;
        }
    }

    private enum Stage {

        Draw(R.string.pl_draw_pattern, LeftButtonState.Cancel, RightButtonState.ContinueDisabled,
                true),
        DrawTooShort(R.string.pl_pattern_too_short, LeftButtonState.Redraw,
                RightButtonState.ContinueDisabled, true),
        DrawValid(R.string.pl_pattern_recorded, LeftButtonState.Redraw, RightButtonState.Continue,
                false),
        Confirm(R.string.pl_confirm_pattern, LeftButtonState.Cancel,
                RightButtonState.ConfirmDisabled, true),
        ConfirmWrong(R.string.pl_wrong_pattern, LeftButtonState.Cancel,
                RightButtonState.ConfirmDisabled, true),
        ConfirmCorrect(R.string.pl_pattern_confirmed, LeftButtonState.Cancel,
                RightButtonState.Confirm, false);

        public final int messageId;
        public final LeftButtonState leftButtonState;
        public RightButtonState rightButtonState;
        public final boolean patternEnabled;

        private Stage(int messageId, LeftButtonState leftButtonState,
                      RightButtonState rightButtonState, boolean patternEnabled) {
            this.messageId = messageId;
            this.leftButtonState = leftButtonState;
            this.rightButtonState = rightButtonState;
            this.patternEnabled = patternEnabled;
        }
    }

    private static final String KEY_STAGE = "stage";
    private static final String KEY_PATTERN = "pattern";
    private int minPatternSize;
    private List<PatternView.Cell> pattern;
    private Stage stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        minPatternSize = getMinPatternSize();
        prefshelper=new Prefshelper(this);
        confirmScreen=getIntent().getStringExtra("confirm");

        changeScreen=getIntent().getStringExtra("change");
        mPatternView.setOnPatternListener(this);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButtonClicked();
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButtonClicked();
            }
        });

        if (savedInstanceState == null) {
            updateStage(Stage.Draw);
        } else {
            String patternString = savedInstanceState.getString(KEY_PATTERN);
            if (patternString != null) {
                pattern = PatternUtils.stringToPattern(patternString);
            }
            updateStage(Stage.values()[savedInstanceState.getInt(KEY_STAGE)]);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_STAGE, stage.ordinal());
        if (pattern != null) {
            outState.putString(KEY_PATTERN, PatternUtils.patternToString(pattern));
        }
    }

    @Override
    public void onPatternStart() {

        removeClearPatternRunnable();

        mMessageText.setText(R.string.pl_recording_pattern);
        mPatternView.setDisplayMode(PatternView.DisplayMode.Correct);
        mLeftButton.setEnabled(false);
        mRightButton.setEnabled(false);
    }

    @Override
    public void onPatternCellAdded(List<PatternView.Cell> pattern) {}

    @Override
    public void onPatternDetected(List<PatternView.Cell> newPattern) {
        switch (stage) {
            case Draw:
            case DrawTooShort:
                if (newPattern.size() < minPatternSize) {
                    updateStage(Stage.DrawTooShort);
                } else {
                    pattern = new ArrayList<>(newPattern);
                    updateStage(Stage.DrawValid);
                }
                break;
            case Confirm:
                if (newPattern.equals(pattern)) {
                    prefshelper.storeLockType("pattern");
                    prefshelper.storePattern(PatternUtils.patternToSha1String(pattern));
                    if(confirmScreen.equalsIgnoreCase("confirm"))
                    {
                    if((prefshelper.getSecondaryLock()).equalsIgnoreCase("secondary"))
                    {
                        Intent i1 = new Intent(SetPatternActivity.this, AppListActivity.class);
                        startActivity(i1);
                        finish();
                    }
                    else
                    {
                        alert();
                    }
                }
                else if(changeScreen.equalsIgnoreCase("change"))
                {

                        Intent i1=new Intent(SetPatternActivity.this, LockSelectionActivity.class);
                        startActivity(i1);
                        finish();

                }
                else if((changeScreen.equalsIgnoreCase("")) || (confirmScreen.equalsIgnoreCase("")))
                {

                        if(prefshelper.getSecondaryLock().equalsIgnoreCase("secondary"))
                        {
                            Intent i1=new Intent(SetPatternActivity.this, AppListActivity.class);
                            startActivity(i1);
                            finish();
                        }
                        else
                        {
                            alert();
                        }
                }

                } else {
                    finish();
                    Intent intent = new Intent(SetPatternActivity.this, SetPatternActivity.class);
                    intent.putExtra("confirm", "confirm");
                    startActivity(intent);
                }

            case ConfirmWrong:
                if (newPattern.equals(pattern)) {
                    updateStage(Stage.ConfirmCorrect);
                } else {
                    updateStage(Stage.ConfirmWrong);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected stage " + stage + " when "
                        + "entering the pattern.");
        }
    }

    @Override
    public void onPatternCleared() {
        removeClearPatternRunnable();
    }
    public void alert() {
        new AlertDialog.Builder(SetPatternActivity.this)
                .setTitle("Please Note")
                .setMessage("You need to enter a PIN as secondary option to unlock the uLock app.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i1 = new Intent(SetPatternActivity.this, PinLockActivity.class);
                        startActivity(i1);
                        finish();
                    }
                }).create().show();
    }
    private void onLeftButtonClicked() {
        if (stage.leftButtonState == LeftButtonState.Redraw) {
            pattern = null;
            updateStage(Stage.Draw);
        } else if (stage.leftButtonState == LeftButtonState.Cancel) {
            setResult(RESULT_CANCELED);
            finish();
            Intent i1 = new Intent(SetPatternActivity.this, LockSelectionActivity.class);
            startActivity(i1);
            LockSelectionActivity.reset(LockSelectionActivity.root);
            LockSelectionActivity.addListener(LockSelectionActivity.root);
            LockSelectionActivity.mExplosionField.clear();
        } else {
            throw new IllegalStateException("left footer button pressed, but stage of " + stage
                    + " doesn't make sense");
        }
    }

    private void onRightButtonClicked() {
        if (stage.rightButtonState == RightButtonState.Continue) {
            if (stage != Stage.DrawValid) {
                throw new IllegalStateException("expected ui stage " + Stage.DrawValid
                        + " when button is " + RightButtonState.Continue);
            }
            updateStage(Stage.Confirm);
            //finish();
           /* Intent i1=new Intent(SetPatternActivity.this, ConfirmPatternActivity.class);
            i1.putExtra("change","");
            i1.putExtra("confirm", "confirm");
            startActivity(i1);*/
        } else if (stage.rightButtonState == RightButtonState.Confirm) {
            if (stage != Stage.ConfirmCorrect) {
                throw new IllegalStateException("expected ui stage " + Stage.ConfirmCorrect
                        + " when button is " + RightButtonState.Confirm);
            }
            onSetPattern(pattern);
            setResult(RESULT_OK);

        }
    }

    public void updateStage(Stage newStage) {

        Stage previousStage = stage;
        stage = newStage;

        if (stage == Stage.DrawTooShort) {
            mMessageText.setText(getString(stage.messageId, minPatternSize));
        } else {
            mMessageText.setText(stage.messageId);
        }

        mLeftButton.setText(stage.leftButtonState.textId);
        mLeftButton.setEnabled(stage.leftButtonState.enabled);

        mRightButton.setText(stage.rightButtonState.textId);
        mRightButton.setEnabled(stage.rightButtonState.enabled);

        mPatternView.setInputEnabled(stage.patternEnabled);

        switch (stage) {
            case Draw:
                // clearPattern() resets display mode to DisplayMode.Correct.
                mPatternView.clearPattern();
                break;
            case DrawTooShort:
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case DrawValid:
                break;
            case Confirm:
                mPatternView.clearPattern();
                break;
            case ConfirmWrong:
                mPatternView.setDisplayMode(PatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                break;
            case ConfirmCorrect:
                break;
        }

        // If the stage changed, announce the header for accessibility. This
        // is a no-op when accessibility is disabled.
        if (previousStage != stage) {
            ViewAccessibilityCompat.announceForAccessibility(mMessageText, mMessageText.getText());
        }
    }

    protected int getMinPatternSize() {
        return 4;
    }

    protected void onSetPattern(List<PatternView.Cell> pattern) {
        String patternSha1 = PatternUtils.patternToSha1String(pattern);


    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i1 = new Intent(SetPatternActivity.this, LockSelectionActivity.class);
        startActivity(i1);
        finish();
        LockSelectionActivity.reset(LockSelectionActivity.root);
        LockSelectionActivity.addListener(LockSelectionActivity.root);
        LockSelectionActivity.mExplosionField.clear();
    }
}