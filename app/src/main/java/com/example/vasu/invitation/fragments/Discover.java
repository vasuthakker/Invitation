package com.example.vasu.invitation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vasu.invitation.R;


/**
 * Created by Aradh Pillai on 1/10/15.
 */
public class Discover extends Fragment {

    private String TAG = "fragment_discover";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        Log.i(TAG, "fragment_discover: onCreateView");

        return view;
    }
}
