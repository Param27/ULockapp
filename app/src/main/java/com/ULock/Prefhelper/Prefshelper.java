package com.ULock.Prefhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Prefshelper {
    public static final String KEY_PREFS_USER_INFO = "user_info";
    private Context context;
    public static SharedPreferences preferences;

    public Prefshelper(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void storePin(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("pin", pin);
        edit.commit();

    }
    public String getPin() {
        return getPreferences().getString("pin", "");
    }

    public void storePattern(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("pattern", pin);
        edit.commit();

    }
    public String getPattern() {
        return getPreferences().getString("pattern", "");
    }



    public void storeConfirmPin(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("cpin", pin);
        edit.commit();

    }
    public void storePassword(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("pass", pin);
        edit.commit();

    }
    public String getPassword() {
        return getPreferences().getString("pass", "");
    }

    public void storeConfirmPass(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("cpass", pin);
        edit.commit();

    }
    public String getConfirmPin() {
        return getPreferences().getString("cpin", "");
    }

    public String getConfirmPass() {
        return getPreferences().getString("cpass", "");
    }

    public void storeLockType(String lock) {
        Editor edit = getPreferences().edit();
        edit.putString("lock_type", lock);
        edit.commit();

    }
    public String getLockType() {
        return getPreferences().getString("lock_type", "");
    }

    public void storeSecondaryLock(String lock) {
        Editor edit = getPreferences().edit();
        edit.putString("secondary_lock", lock);
        edit.commit();

    }
    public String getSecondaryLock() {
        return getPreferences().getString("secondary_lock", "");
    }

    public void appLocked(String locked) {
        Editor edit = getPreferences().edit();
        edit.putString("locked", locked);
        edit.commit();

    }
    public String isAppLocked() {
        return getPreferences().getString("locked","");
    }

    public void istImage(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("istImage", image);
        edit.commit();

    }
    public String getIstImage() {
        return getPreferences().getString("istImage","");
    }

    public void secondImage(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("secImage", image);
        edit.commit();

    }
    public String getSecondImage() {
        return getPreferences().getString("secImage","");
    }

    public void thirdImage(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("thirdImage", image);
        edit.commit();

    }
    public String getThirdImage() {
        return getPreferences().getString("thirdImage","");
    }

    public void positionOfIstApp(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("fposition", image);
        edit.commit();

    }
    public String getPositionOfIstApp() {
        return getPreferences().getString("fposition","");
    }

    public void positionOfSecApp(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("sposition", image);
        edit.commit();

    }
    public String getPositionOfSecApp() {
        return getPreferences().getString("sposition","");
    }
    public void positionOfThirdApp(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("tposition", image);
        edit.commit();

    }
    public String getPositionOfThirdApp() {
        return getPreferences().getString("tposition","");
    }

    public void storeStartTimeOfApp(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("start_time", image);
        edit.commit();

    }
    public String getStartTimeOfApp() {
        return getPreferences().getString("start_time","");
    }

    public void storeStopTimeOfApp(String time) {
        Editor edit = getPreferences().edit();
        edit.putString("stop_time", time);
        edit.commit();

    }
    public String getStopTimeOfApp() {
        return getPreferences().getString("stop_time","");
    }

    public boolean storeCheckBoxStates(Boolean[] array, String arrayName, Context mContext) {
        Editor edit = getPreferences().edit();
        edit.putInt(arrayName +"_size", array.length);

        for(int i=0;i<array.length;i++)
            edit.putBoolean(arrayName + "_" + i, array[i]);
        return edit.commit();

    }

    public Boolean[] getCheckBoxStates(String arrayName, Context mContext) {
        int size = getPreferences().getInt(arrayName + "_size", 0);
        Boolean array[] = new Boolean[size];
        for(int i=0;i<size;i++)
            array[i] = getPreferences().getBoolean(arrayName + "_" + i, false);
        return array;
    }

    public void isIInAppPurchased(String time) {
        Editor edit = getPreferences().edit();
        edit.putString("iinapppurchase", time);
        edit.commit();

    }
    public String getIInAppPurchased() {
        return getPreferences().getString("iinapppurchase","");
    }
    public void storeservice(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("service", pin);
        edit.commit();

    }
    public String getservice() {
        return getPreferences().getString("service", "");
    }


    public void storepkg(String pin) {
        Editor edit = getPreferences().edit();
        edit.putString("pkg", pin);
        edit.commit();

    }
    public String getpkg() {
        return getPreferences().getString("pkg", "");
    }

    public void storeTimer(String lock) {
        Editor edit = getPreferences().edit();
        edit.putString("timer", lock);
        edit.commit();

    }
    public String getTimer() {
        return getPreferences().getString("timer", "");
    }

}
