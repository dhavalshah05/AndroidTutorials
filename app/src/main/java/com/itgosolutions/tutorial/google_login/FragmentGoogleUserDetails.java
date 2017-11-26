package com.itgosolutions.tutorial.google_login;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.itgosolutions.tutorial.R;

public class FragmentGoogleUserDetails extends Fragment implements View.OnClickListener {

    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String PICTURE_URL = "pictureURL";

    private OnGoogleLogoutCompleteListener mListener;
    private String mName, mEmail, mID, mPictureURL;

    public FragmentGoogleUserDetails() {
    }

    public static FragmentGoogleUserDetails newInstance(User user) {
        FragmentGoogleUserDetails fragment = new FragmentGoogleUserDetails();
        Bundle args = new Bundle();
        args.putString(NAME, user.getName());
        args.putString(EMAIL, user.getEmail());
        args.putString(ID, user.getGoogleUserId());
        args.putString(PICTURE_URL, user.getPictureURL());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mName = getArguments().getString(NAME, "");
        mEmail = getArguments().getString(EMAIL, "");
        mID = getArguments().getString(ID, "");
        mPictureURL = getArguments().getString(PICTURE_URL, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_user_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvEmail = (TextView) view.findViewById(R.id.fragment_google_user_details_tv_email);
        TextView tvName = (TextView) view.findViewById(R.id.fragment_google_user_details_tv_name);
        TextView tvID = (TextView) view.findViewById(R.id.fragment_google_user_details_tv_id);
        Button btnLogout = (Button) view.findViewById(R.id.fragment_google_user_details_btn_logout);

        tvEmail.setText(mEmail);
        tvName.setText(mName);
        tvID.setText(mID);

        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_google_user_details_btn_logout:
                signOut();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnGoogleLogoutCompleteListener){
            mListener = (OnGoogleLogoutCompleteListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGoogleLogoutCompleteListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void signOut() {
        GoogleLoginHelper.getGoogleSignInClient(getContext()).signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mListener.onLogoutSuccess();
                        }else {
                            mListener.onLogoutFailed();
                        }
                    }
                });
    }

    public interface OnGoogleLogoutCompleteListener{
        void onLogoutSuccess();
        void onLogoutFailed();
    }

}
