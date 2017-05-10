package com.itgosolutions.tutorial.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itgosolutions.tutorial.R;

import java.util.ArrayList;

public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] PARAMS_TAKE_PHOTO = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int RESULT_PARAMS_TAKE_PHOTO = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        Button btnTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new PermissionFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_take_photo:
                takePhoto();
                break;
        }
    }

    private void takePhoto() {

        if(canTakePhoto()){

            Toast.makeText(this, "Can take pictures", Toast.LENGTH_SHORT).show();

        }else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            Toast.makeText(this, "You should give permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, netPermissions(PARAMS_TAKE_PHOTO), RESULT_PARAMS_TAKE_PHOTO);

        }else {

            ActivityCompat.requestPermissions(this, netPermissions(PARAMS_TAKE_PHOTO), RESULT_PARAMS_TAKE_PHOTO);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RESULT_PARAMS_TAKE_PHOTO){

            if(canTakePhoto()){

                Toast.makeText(this, "Can take pictures", Toast.LENGTH_SHORT).show();

            }else if(!(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))){

                Toast.makeText(this, "You should give permission From Settings", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private String[] netPermissions(String[] wantedPermissions){
        ArrayList<String> result = new ArrayList<>();

        for(String permission: wantedPermissions){
            if(!hasPermission(permission)){
                result.add(permission);
            }
        }

        return (result.toArray(new String[result.size()]));
    }

    private boolean canTakePhoto(){
        return (hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String permissionString){
        return (ContextCompat.checkSelfPermission(this, permissionString) == PackageManager.PERMISSION_GRANTED);
    }
}
