package com.itgosolutions.tutorial.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itgosolutions.tutorial.R;

public class FragmentMap extends Fragment implements OnMapReadyCallback {

    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    public static FragmentMap newInstance() {

        Bundle args = new Bundle();

        FragmentMap fragment = new FragmentMap();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        /*
        * For zooming to particular location with animation

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(40.76793169992044, -73.98180484771729));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(cameraUpdate);
        googleMap.animateCamera(zoom);
        */

        Marker marker1 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.70686417491799, -74.01572942733765))
                .title("First Marker")
                .snippet("First Snippet"));

        Marker marker2 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.748963847316034, -73.96807193756104))
                .title("First Marker")
                .snippet("First Snippet"));

        Marker marker3 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.76866299974387, -73.98268461227417))
                .title("First Marker")
                .snippet("First Snippet"));

        Marker marker4 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.765136435316755, -73.97989511489868))
                .title("First Marker")
                .snippet("First Snippet"));

        builder.include(marker1.getPosition());
        builder.include(marker2.getPosition());
        builder.include(marker3.getPosition());
        builder.include(marker4.getPosition());

        getActivity().findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                CameraUpdate allMarkers = CameraUpdateFactory.newLatLngBounds(builder.build(), 32);
                googleMap.moveCamera(allMarkers);
            }
        });
    }
}
