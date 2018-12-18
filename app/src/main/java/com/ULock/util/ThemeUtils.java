package com.ULock.util;

import android.app.Activity;
import android.content.Context;

import com.ULock.R;


public class ThemeUtils {

    private ThemeUtils() {
    }

    private static final int[] THEME_IDS = new int[]{
            R.style.AppTheme,
            R.style.AppTheme_Dark
    };

    public static int getThemeId(Context context) {
        int index = Integer.valueOf(PreferenceUtils.getString(PreferenceContract.KEY_THEME,
                PreferenceContract.DEFAULT_THEME, context));
        return THEME_IDS[index];
    }

    public static void applyTheme(Activity activity) {
        activity.setTheme(getThemeId(activity));
    }
}