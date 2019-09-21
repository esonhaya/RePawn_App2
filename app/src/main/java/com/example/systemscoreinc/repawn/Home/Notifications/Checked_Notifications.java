package com.example.systemscoreinc.repawn.Home.Notifications;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.systemscoreinc.repawn.R;

public class Checked_Notifications extends Fragment {
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checked, container, false);
        ((Notifications) getActivity()).getOld_Notif(rootView);
        return rootView;
    }
}