package com.ULock.HowToUseApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nazer on listen/14/2016.
 */
public class Intro_fragments_Refresh extends Fragment {

    final static String LAYOUT_ID = "layoutid";

    public static Intro_fragments_Refresh newInstance(int layoutId) {
        Intro_fragments_Refresh pane = new Intro_fragments_Refresh();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(args);
        return pane;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        return rootView;
    }
}