package com.ULock.util;

import android.content.Context;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;

import java.io.File;

public class PasswordGesturesLibrary {

    private static GestureLibrary sStore;

    public static GestureLibrary getStore(Context c) {

        if (sStore == null) {
            File storeFile = new File(c.getFilesDir(), "gestures");
            sStore = GestureLibraries.fromFile(storeFile);
            sStore.load();
        }

        return sStore;
    }

}