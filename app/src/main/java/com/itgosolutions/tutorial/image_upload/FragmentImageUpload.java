package com.itgosolutions.tutorial.image_upload;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itgosolutions.tutorial.AppConstants;
import com.itgosolutions.tutorial.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FragmentImageUpload extends Fragment {

    private static final String[] PERMISSIONS_CHOOSE_IMAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int RESULT_PERMISSIONS_CHOOSE_IMAGE = 14;
    private static final int PICK_IMAGE_REQUEST = 101;

    private ImageView mImageView;
    private TextView mImageTitleTV;
    private ProgressBar mProgress;

    private byte[] mByteArray;

    public FragmentImageUpload() {
    }

    public static FragmentImageUpload newInstance() {
        return new FragmentImageUpload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_upload, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mImageView = view.findViewById(R.id.fragment_image_upload_iv);
        mImageTitleTV = view.findViewById(R.id.fragment_image_upload_tv_name);
        Button mChooseImageBtn = view.findViewById(R.id.fragment_image_upload_btn_choose_image);
        Button mUploadImageBtn = view.findViewById(R.id.fragment_image_upload_btn_upload_image);
        mProgress = view.findViewById(R.id.fragment_image_upload_progress);

        initView();

        mChooseImageBtn.setOnClickListener(view1 -> selectImage());
        mUploadImageBtn.setOnClickListener(view1 -> uploadImage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RESULT_PERMISSIONS_CHOOSE_IMAGE) {
            if (canChooseImage()) {
                showFileChooser();
            } else if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Please give permission from settings", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            /*
            Uri uri = data.getData();
            File mFile = new File(uri.getPath());

            if (!mFile.exists()) {
                Toast.makeText(getActivity(), "Image is corrupted. Please select from gallary", Toast.LENGTH_SHORT).show();
                mFile = null;
                return;
            }

            Log.i(TAG, "URI: " + uri);
            Log.i(TAG, "URI STRING : " + uri.toString());
            Log.i(TAG, "URI PATH : " + uri.getPath());
            Log.i(TAG, "File Exist: " + mFile.exists());
            Log.i(TAG, "File Absolute Path: " + mFile.getAbsolutePath());
            */

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                mImageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mByteArray = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void initView() {
        mProgress.setVisibility(View.INVISIBLE);
        mImageTitleTV.setText("");
    }

    private void selectImage() {
        if (canChooseImage()) {
            showFileChooser();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Permission is required to choose image", Toast.LENGTH_SHORT).show();
            requestPermissions(netPermissions(PERMISSIONS_CHOOSE_IMAGE), RESULT_PERMISSIONS_CHOOSE_IMAGE);
        } else {
            requestPermissions(netPermissions(PERMISSIONS_CHOOSE_IMAGE), RESULT_PERMISSIONS_CHOOSE_IMAGE);
        }
    }

    private void uploadImage() {
        if (mByteArray == null) return;

        showProgress();

        uploadImageObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        hideProgress();
                        Toast.makeText(getActivity(), "Image is uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgress();
                        Toast.makeText(getActivity(), "Error uploading photo. Please try again!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    private Observable<String> uploadImageObservable() {
        return Observable.fromCallable(() -> {
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("myImage", "fromAndroid",
                            RequestBody.create(MEDIA_TYPE_JPG, mByteArray))
                    .build();

            Request request = new Request.Builder()
                    .url(AppConstants.NODE_BASE_URL + "/api/image-upload")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        });
    }

    private void hideProgress() {
        mProgress.setVisibility(View.INVISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private boolean canChooseImage() {
        return hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private String[] netPermissions(String[] wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String permission : wantedPermissions) {
            if (!hasPermission(permission)) {
                result.add(permission);
            }
        }
        return (result.toArray(new String[result.size()]));
    }

    private boolean hasPermission(String permissionString) {
        return (ContextCompat.checkSelfPermission(getContext(), permissionString) == PackageManager.PERMISSION_GRANTED);
    }

}
