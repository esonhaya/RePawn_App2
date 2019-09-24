package com.example.systemscoreinc.repawn.Orders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.systemscoreinc.repawn.R;

public class Fragment_Reservations extends Fragment {
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservations, container, false);
        ((Orders) getActivity()).getReservations(rootView);
        return rootView;
    }
}
