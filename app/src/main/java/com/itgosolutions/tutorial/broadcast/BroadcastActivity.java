package com.itgosolutions.tutorial.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.itgosolutions.tutorial.R;

public class BroadcastActivity extends AppCompatActivity {

    private static final String ACTION_DEMO_BROADCAST = "com.itgosolutions.com.broadcast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        findViewById(R.id.btn_send_broadcast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senBroadcast();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcast();
    }

    @Override
    protected void onPause() {
        unregisterBroadcast();
        super.onPause();
    }

    private void senBroadcast(){
        Intent intent = new Intent(ACTION_DEMO_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void registerBroadcast(){
        IntentFilter intentFilter = new IntentFilter(ACTION_DEMO_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(demoReceiver, intentFilter);
    }

    private void unregisterBroadcast(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(demoReceiver);
    }

    private BroadcastReceiver demoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Broadcast Received", Toast.LENGTH_SHORT).show();
        }
    };
}
