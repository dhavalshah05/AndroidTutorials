package com.itgosolutions.tutorial.rxjava;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itgosolutions.tutorial.R;

public class FragmentRxJava extends Fragment {

    public static FragmentRxJava newInstance() {

        Bundle args = new Bundle();
        
        FragmentRxJava fragment = new FragmentRxJava();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rx_java, container, false);
    }

}
