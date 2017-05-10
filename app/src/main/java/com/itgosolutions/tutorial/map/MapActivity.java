package com.itgosolutions.tutorial.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itgosolutions.tutorial.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        /*
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

        findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
               CameraUpdate allMarkers = CameraUpdateFactory.newLatLngBounds(builder.build(), 32);
                googleMap.moveCamera(allMarkers);
            }
        });
    }
}
