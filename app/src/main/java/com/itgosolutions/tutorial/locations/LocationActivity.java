package com.itgosolutions.tutorial.locations;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itgosolutions.tutorial.R;

public class LocationActivity extends AbstractPermissionActivity {

    private static final int REQ_PENDING_INTENT = 0;
    private static final int REQ_LISTENER = 1;

    private static final int LOCATION_UPDATE_REQUEST = REQ_LISTENER;

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    private TextView tvGPSStatus, tvLastKnownLocation, tvCurrentLocation;

    private LocationManager locationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(LocationActivity.this, "Location Changed!", Toast.LENGTH_SHORT).show();
            String locationString = location.getLatitude() + " , " + location.getLongitude();
            tvCurrentLocation.setText(locationString);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(LocationActivity.this, "Status Changed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            tvGPSStatus.setText("Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            tvGPSStatus.setText("Disabled");
        }
    };

    @Override
    protected String[] getDesiredPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    protected void onPermissionDenied() {
        Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onReady() {
        setContentView(R.layout.activity_location);

        tvGPSStatus = (TextView) findViewById(R.id.tv_gps_status);
        tvLastKnownLocation = (TextView) findViewById(R.id.tv_last_known_location);
        tvCurrentLocation = (TextView) findViewById(R.id.tv_current_location);

        findViewById(R.id.btn_start_location_updates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    getLastKnownLocation();

                    if(LOCATION_UPDATE_REQUEST == REQ_LISTENER){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
                    }else {
                        Intent intent = new Intent(LocationActivity.this, LocationBroadcast.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(LocationActivity.this, 10, intent, 0);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, pendingIntent);
                    }

                }else {
                    Toast.makeText(LocationActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStop() {
        if (locationManager != null)
            locationManager.removeUpdates(mLocationListener);
        super.onStop();
    }

    @SuppressWarnings("MissingPermission")
    private void getLastKnownLocation(){
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null){
            String locationString = location.getLatitude() + " , " + location.getLongitude();
            tvLastKnownLocation.setText(locationString);
        }else {
            tvLastKnownLocation.setText("Unknown");
        }

    }
}
