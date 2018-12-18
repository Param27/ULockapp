package com.ULock.App_Database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ULock.ConfirmGestureActivity;
import com.ULock.ConfirmPassLockActivity;
import com.ULock.ConfirmPatternActivity;
import com.ULock.LockSelectionActivity;
import com.ULock.Prefhelper.Prefshelper;

/**
 * Created by nazer on 8/3/2016.
 */
public class All_Password extends AppCompatActivity{
    Prefshelper prefshelper;
    String serv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefshelper=new Prefshelper(All_Password.this);
        serv=getIntent().getStringExtra("data");

        if((prefshelper.getLockType().equalsIgnoreCase("gesture"))&& (prefshelper.getSecondaryLock().equalsIgnoreCase("secondary"))&&
                (prefshelper.isAppLocked().equalsIgnoreCase("")))

        {
            Intent intent = new Intent(All_Password.this, ConfirmGestureActivity.class);
            intent.putExtra("confirm","confirm");
            intent.putExtra("ist","time");
            intent.putExtra("data1",""+serv);
            startActivity(intent);
            finish();
        }
        else if((prefshelper.getLockType().equalsIgnoreCase("pattern")) && (prefshelper.getSecondaryLock().equalsIgnoreCase("secondary"))
                && (prefshelper.isAppLocked().equalsIgnoreCase("")))
        {
            Log.e("mainActivity","main");
            Intent intent = new Intent(All_Password.this, ConfirmPatternActivity.class);
            intent.putExtra("confirm","confirm");
            intent.putExtra("data1",""+serv);
            startActivity(intent);
            finish();
        }

        else if((prefshelper.getLockType().equalsIgnoreCase("password"))&& (prefshelper.getSecondaryLock().equalsIgnoreCase("secondary"))
                && (prefshelper.isAppLocked().equalsIgnoreCase("")))

        {
            Intent intent = new Intent(All_Password.this, ConfirmPassLockActivity.class);
            intent.putExtra("confirm","confirm");
            intent.putExtra("ist","time");
            intent.putExtra("data1",""+serv);
            startActivity(intent);
            finish();
        }
        else if(prefshelper.isAppLocked().equalsIgnoreCase("permanent"))
        {
            Toast.makeText(All_Password.this, "Your App has been locked permanently, You need to reinstall the app", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            Intent intent = new Intent(All_Password.this, LockSelectionActivity.class);
            intent.putExtra("confirm","confirm");
            startActivity(intent);
            finish();
        }
    }

    public All_Password()
    {

    }

}
