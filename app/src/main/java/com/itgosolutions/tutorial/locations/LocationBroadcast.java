package com.itgosolutions.tutorial.locations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Dhaval on 10-05-2017.
 */

public class LocationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Location Updated From Reciever", Toast.LENGTH_SHORT).show();
    }
}
