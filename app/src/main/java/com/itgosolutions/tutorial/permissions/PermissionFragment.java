package com.itgosolutions.tutorial.permissions;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.itgosolutions.tutorial.R;

import java.util.ArrayList;


public class PermissionFragment extends Fragment implements View.OnClickListener {

    private static final String[] PARAMS_TAKE_PHOTO = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int RESULT_PARAMS_TAKE_PHOTO = 13;


    public PermissionFragment() {
        // Required empty public constructor'
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permission, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnTakePhoto = (Button) view.findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(this);
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

            Toast.makeText(getActivity(), "Can take pictures From Fragment", Toast.LENGTH_SHORT).show();

        }else if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            Toast.makeText(getActivity(), "You should give permission From Fragment", Toast.LENGTH_SHORT).show();
            requestPermissions(netPermissions(PARAMS_TAKE_PHOTO), RESULT_PARAMS_TAKE_PHOTO);

        }else {

            requestPermissions(netPermissions(PARAMS_TAKE_PHOTO), RESULT_PARAMS_TAKE_PHOTO);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RESULT_PARAMS_TAKE_PHOTO){

            if(canTakePhoto()){

                Toast.makeText(getActivity(), "Can take pictures From Fragment", Toast.LENGTH_SHORT).show();

            }else if(!(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))){

                Toast.makeText(getActivity(), "You should give permission From Settings From Fragment", Toast.LENGTH_SHORT).show();

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
        return (ContextCompat.checkSelfPermission(getContext(), permissionString) == PackageManager.PERMISSION_GRANTED);
    }

}
