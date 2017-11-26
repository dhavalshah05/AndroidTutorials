package com.itgosolutions.tutorial.google_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.itgosolutions.tutorial.R;

import static android.content.ContentValues.TAG;


public class FragmentGoogleLogin extends Fragment implements View.OnClickListener {

    private OnGoogleLogInCompleteListener mListener;

    public FragmentGoogleLogin() {
    }

    public static FragmentGoogleLogin newInstance() {
        FragmentGoogleLogin fragment = new FragmentGoogleLogin();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure google sign in client
        GoogleLoginHelper.configureGoogleSignInClient(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check for existing google sign in account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if(account != null){
            mListener.onLoginSuccess(account);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_google_login, container, false);

        SignInButton signInButton = (SignInButton) view.findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGoogleLogInCompleteListener) {
            mListener = (OnGoogleLogInCompleteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGoogleLogInCompleteListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                signIn();
                break;

        }
    }

    private void signIn() {
        Intent signInIntent = GoogleLoginHelper.getGoogleSignInClient(getContext()).getSignInIntent();
        startActivityForResult(signInIntent, GoogleLoginHelper.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GoogleLoginHelper.RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mListener.onLoginSuccess(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            mListener.onLoginFailed(e.getMessage());
        }
    }

    public interface OnGoogleLogInCompleteListener {
        void onLoginSuccess(GoogleSignInAccount account);
        void onLoginFailed(String message);
    }
}
