package com.ULock.Prefhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceDialogFragmentCompat;

import com.ULock.R;
import com.ULock.util.PatternLockUtils;
import com.ULock.util.ToastUtils;

public class ClearPatternPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    // PreferenceDialogFragmentCompat needs a key to find its preference.
    public static ClearPatternPreferenceDialogFragment newInstance(String key) {
        ClearPatternPreferenceDialogFragment dialogFragment =
                new ClearPatternPreferenceDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_KEY, key);
        dialogFragment.setArguments(arguments);
        return dialogFragment;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            Activity activity = getActivity();
            PatternLockUtils.clearPattern(activity);
            ToastUtils.show(R.string.pattern_cleared, activity);
        }
    }
}